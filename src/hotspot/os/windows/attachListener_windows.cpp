/*
 * Copyright (c) 2005, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

#include "precompiled.hpp"
#include "logging/log.hpp"
#include "runtime/interfaceSupport.inline.hpp"
#include "runtime/os.hpp"
#include "services/attachListener.hpp"

#include <windows.h>
#include <signal.h>             // SIGBREAK
#include <stdio.h>

// The AttachListener thread services a queue of operations. It blocks in the dequeue
// function until an operation is enqueued. A client enqueues an operation by creating
// a thread in this process using the Win32 CreateRemoteThread function. That thread
// executes a small stub generated by the client. The stub invokes the
// JVM_EnqueueOperation function which checks the operation parameters and enqueues
// the operation to the queue serviced by the attach listener. The thread created by
// the client is a native thread and is restricted to a single page of stack. To keep
// it simple operations are pre-allocated at initialization time. An enqueue thus
// takes a preallocated operation, populates the operation parameters, adds it to
// queue and wakes up the attach listener.
//
// When an operation has completed the attach listener is required to send the
// operation result and any result data to the client. In this implementation the
// client is a pipe server. In the enqueue operation it provides the name of pipe
// to this process. When the operation is completed this process opens the pipe and
// sends the result and output back to the client. Note that writing to the pipe
// (and flushing the output) is a blocking operation. This means that a non-responsive
// client could potentially hang the attach listener thread indefinitely. In that
// case no new operations would be executed but the VM would continue as normal.
// As only suitably privileged processes can open this process we concluded that
// this wasn't worth worrying about.


// forward reference
class Win32AttachOperation;


class Win32AttachListener: AllStatic {
 private:
  enum {
    max_enqueued_operations = 4
  };

  // protects the preallocated list and the operation list
  static HANDLE _mutex;

  // head of preallocated operations list
  static Win32AttachOperation* _avail;

  // head and tail of enqueue operations list
  static Win32AttachOperation* _head;
  static Win32AttachOperation* _tail;


  static Win32AttachOperation* head()                       { return _head; }
  static void set_head(Win32AttachOperation* head)          { _head = head; }

  static Win32AttachOperation* tail()                       { return _tail; }
  static void set_tail(Win32AttachOperation* tail)          { _tail = tail; }


  // A semaphore is used for communication about enqueued operations.
  // The maximum count for the semaphore object will be set to "max_enqueued_operations".
  // The state of a semaphore is signaled when its count is greater than
  // zero (there are operations enqueued), and nonsignaled when it is zero.
  static HANDLE _enqueued_ops_semaphore;
  static HANDLE enqueued_ops_semaphore() { return _enqueued_ops_semaphore; }

 public:
  enum {
    ATTACH_ERROR_DISABLED               = 100,              // error codes
    ATTACH_ERROR_RESOURCE               = 101,
    ATTACH_ERROR_ILLEGALARG             = 102,
    ATTACH_ERROR_INTERNAL               = 103
  };

  static int init();
  static HANDLE mutex()                                     { return _mutex; }

  static Win32AttachOperation* available()                  { return _avail; }
  static void set_available(Win32AttachOperation* avail)    { _avail = avail; }

  // enqueue an operation to the end of the list
  static int enqueue(char* cmd, char* arg1, char* arg2, char* arg3, char* pipename);

  // dequeue an operation from from head of the list
  static Win32AttachOperation* dequeue();
};

// statics
HANDLE Win32AttachListener::_mutex;
HANDLE Win32AttachListener::_enqueued_ops_semaphore;
Win32AttachOperation* Win32AttachListener::_avail;
Win32AttachOperation* Win32AttachListener::_head;
Win32AttachOperation* Win32AttachListener::_tail;


// Win32AttachOperation is an AttachOperation that additionally encapsulates the name
// of a pipe which is used to send the operation reply/output to the client.
// Win32AttachOperation can also be linked in a list.

class Win32AttachOperation: public AttachOperation {
 private:
  friend class Win32AttachListener;

  enum {
    pipe_name_max = 256             // maximum pipe name
  };

  char _pipe[pipe_name_max + 1];

  const char* pipe() const                              { return _pipe; }
  void set_pipe(const char* pipe) {
    assert(strlen(pipe) <= pipe_name_max, "exceeds maximum length of pipe name");
    os::snprintf(_pipe, sizeof(_pipe), "%s", pipe);
  }

  HANDLE open_pipe();
  static BOOL write_pipe(HANDLE hPipe, char* buf, int len);

  Win32AttachOperation* _next;

  Win32AttachOperation* next() const                    { return _next; }
  void set_next(Win32AttachOperation* next)             { _next = next; }

  // noarg constructor as operation is preallocated
  Win32AttachOperation() : AttachOperation("<noname>") {
    set_pipe("<nopipe>");
    set_next(nullptr);
  }

 public:
  void complete(jint result, bufferedStream* result_stream);
};


// Preallocate the maximum number of operations that can be enqueued.
int Win32AttachListener::init() {
  _mutex = (void*)::CreateMutex(nullptr, FALSE, nullptr);
  guarantee(_mutex != (HANDLE)nullptr, "mutex creation failed");

  _enqueued_ops_semaphore = ::CreateSemaphore(nullptr, 0, max_enqueued_operations, nullptr);
  guarantee(_enqueued_ops_semaphore != (HANDLE)nullptr, "semaphore creation failed");

  set_head(nullptr);
  set_tail(nullptr);
  set_available(nullptr);

  for (int i=0; i<max_enqueued_operations; i++) {
    Win32AttachOperation* op = new Win32AttachOperation();
    op->set_next(available());
    set_available(op);
  }

  return 0;
}

// Enqueue an operation. This is called from a native thread that is not attached to VM.
// Also we need to be careful not to execute anything that results in more than a 4k stack.
//
int Win32AttachListener::enqueue(char* cmd, char* arg0, char* arg1, char* arg2, char* pipename) {
  // wait up to 10 seconds for listener to be up and running
  int sleep_count = 0;
  while (!AttachListener::is_initialized()) {
    Sleep(1000); // 1 second
    sleep_count++;
    if (sleep_count > 10) { // try for 10 seconds
      return ATTACH_ERROR_DISABLED;
    }
  }

  // check that all parameters to the operation
  if (strlen(cmd) > AttachOperation::name_length_max) return ATTACH_ERROR_ILLEGALARG;
  if (strlen(arg0) > AttachOperation::arg_length_max) return ATTACH_ERROR_ILLEGALARG;
  if (strlen(arg1) > AttachOperation::arg_length_max) return ATTACH_ERROR_ILLEGALARG;
  if (strlen(arg2) > AttachOperation::arg_length_max) return ATTACH_ERROR_ILLEGALARG;
  if (strlen(pipename) > Win32AttachOperation::pipe_name_max) return ATTACH_ERROR_ILLEGALARG;

  // check for a well-formed pipename
  if (strstr(pipename, "\\\\.\\pipe\\") != pipename) return ATTACH_ERROR_ILLEGALARG;

  // grab the lock for the list
  DWORD res = ::WaitForSingleObject(mutex(), INFINITE);
  if (res != WAIT_OBJECT_0) {
    return ATTACH_ERROR_INTERNAL;
  }

  // try to get an operation from the available list
  Win32AttachOperation* op = available();
  if (op != nullptr) {
    set_available(op->next());

    // add to end (tail) of list
    op->set_next(nullptr);
    if (tail() == nullptr) {
      set_head(op);
    } else {
      tail()->set_next(op);
    }
    set_tail(op);

    op->set_name(cmd);
    op->set_arg(0, arg0);
    op->set_arg(1, arg1);
    op->set_arg(2, arg2);
    op->set_pipe(pipename);

    // Increment number of enqueued operations.
    // Side effect: Semaphore will be signaled and will release
    // any blocking waiters (i.e. the AttachListener thread).
    BOOL not_exceeding_semaphore_maximum_count =
      ::ReleaseSemaphore(enqueued_ops_semaphore(), 1, nullptr);
    guarantee(not_exceeding_semaphore_maximum_count, "invariant");
  }
  ::ReleaseMutex(mutex());

  return (op != nullptr) ? 0 : ATTACH_ERROR_RESOURCE;
}


// dequeue the operation from the head of the operation list.
Win32AttachOperation* Win32AttachListener::dequeue() {
  for (;;) {
    DWORD res = ::WaitForSingleObject(enqueued_ops_semaphore(), INFINITE);
    // returning from WaitForSingleObject will have decreased
    // the current count of the semaphore by 1.
    guarantee(res != WAIT_FAILED,   "WaitForSingleObject failed with error code: %lu", GetLastError());
    guarantee(res == WAIT_OBJECT_0, "WaitForSingleObject failed with return value: %lu", res);

    res = ::WaitForSingleObject(mutex(), INFINITE);
    guarantee(res != WAIT_FAILED,   "WaitForSingleObject failed with error code: %lu", GetLastError());
    guarantee(res == WAIT_OBJECT_0, "WaitForSingleObject failed with return value: %lu", res);


    Win32AttachOperation* op = head();
    if (op != nullptr) {
      set_head(op->next());
      if (head() == nullptr) {     // list is empty
        set_tail(nullptr);
      }
    }
    ::ReleaseMutex(mutex());

    if (op != nullptr) {
      return op;
    }
  }
}


// open the pipe to the client
HANDLE Win32AttachOperation::open_pipe() {
  HANDLE hPipe = ::CreateFile( pipe(),  // pipe name
                        GENERIC_WRITE,   // write only
                        0,              // no sharing
                        nullptr,           // default security attributes
                        OPEN_EXISTING,  // opens existing pipe
                        0,              // default attributes
                        nullptr);          // no template file
  return hPipe;
}

// write to the pipe
BOOL Win32AttachOperation::write_pipe(HANDLE hPipe, char* buf, int len) {
  do {
    DWORD nwrote;

    BOOL fSuccess = WriteFile(  hPipe,                  // pipe handle
                                (LPCVOID)buf,           // message
                                (DWORD)len,             // message length
                                &nwrote,                // bytes written
                                nullptr);                  // not overlapped
    if (!fSuccess) {
      return fSuccess;
    }
    buf += nwrote;
    len -= nwrote;
  } while (len > 0);
  return TRUE;
}

// Complete the operation:
//   - open the pipe to the client
//   - write the operation result (a jint)
//   - write the operation output (the result stream)
//
void Win32AttachOperation::complete(jint result, bufferedStream* result_stream) {
  JavaThread* thread = JavaThread::current();
  ThreadBlockInVM tbivm(thread);

  HANDLE hPipe = open_pipe();
  int lastError = (int)::GetLastError();
  if (hPipe != INVALID_HANDLE_VALUE) {
    BOOL fSuccess;

    char msg[32];
    _snprintf(msg, sizeof(msg), "%d\n", result);
    msg[sizeof(msg) - 1] = '\0';

    fSuccess = write_pipe(hPipe, msg, (int)strlen(msg));
    if (fSuccess) {
      fSuccess = write_pipe(hPipe, (char*)result_stream->base(), (int)(result_stream->size()));
    }
    lastError = (int)::GetLastError();

    // Need to flush buffers
    FlushFileBuffers(hPipe);
    CloseHandle(hPipe);

    if (fSuccess) {
      log_debug(attach)("wrote result of attach operation %s to pipe %s", name(), pipe());
    } else {
      log_error(attach)("failure (%d) writing result of operation %s to pipe %s", lastError, name(), pipe());
    }
  } else {
    log_error(attach)("could not open (%d) pipe %s to send result of operation %s", lastError, pipe(), name());
  }

  DWORD res = ::WaitForSingleObject(Win32AttachListener::mutex(), INFINITE);
  assert(res != WAIT_FAILED,   "WaitForSingleObject failed with error code: %lu", GetLastError());
  assert(res == WAIT_OBJECT_0, "WaitForSingleObject failed with return value: %lu", res);

  if (res == WAIT_OBJECT_0) {

    // put the operation back on the available list
    set_next(Win32AttachListener::available());
    Win32AttachListener::set_available(this);

    ::ReleaseMutex(Win32AttachListener::mutex());
  }
}


// AttachOperation functions

AttachOperation* AttachListener::dequeue() {
  JavaThread* thread = JavaThread::current();
  ThreadBlockInVM tbivm(thread);

  AttachOperation* op = Win32AttachListener::dequeue();

  return op;
}

void AttachListener::vm_start() {
  // nothing to do
}

int AttachListener::pd_init() {
  return Win32AttachListener::init();
}

// This function is used for Un*x OSes only.
// We need not to implement it for Windows.
bool AttachListener::check_socket_file() {
  return false;
}

bool AttachListener::init_at_startup() {
  return true;
}

// no trigger mechanism on Windows to start Attach Listener lazily
bool AttachListener::is_init_trigger() {
  return false;
}

void AttachListener::abort() {
  // nothing to do
}

void AttachListener::pd_data_dump() {
  os::signal_notify(SIGBREAK);
}

void AttachListener::pd_detachall() {
  // do nothing for now
}

// Native thread started by remote client executes this.
extern "C" {
  JNIEXPORT jint JNICALL
    JVM_EnqueueOperation(char* cmd, char* arg0, char* arg1, char* arg2, char* pipename) {
      return (jint)Win32AttachListener::enqueue(cmd, arg0, arg1, arg2, pipename);
    }

} // extern
