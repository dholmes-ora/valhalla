/*
 * Copyright (c) 2015, 2023, Oracle and/or its affiliates. All rights reserved.
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

#ifndef SHARE_OOPS_INSTANCEKLASS_INLINE_HPP
#define SHARE_OOPS_INSTANCEKLASS_INLINE_HPP

#include "oops/instanceKlass.hpp"

#include "memory/memRegion.hpp"
#include "oops/fieldInfo.inline.hpp"
#include "oops/klass.inline.hpp"
#include "oops/oop.inline.hpp"
#include "runtime/atomic.hpp"
#include "utilities/devirtualizer.inline.hpp"
#include "utilities/globalDefinitions.hpp"

inline intptr_t* InstanceKlass::start_of_itable()   const { return (intptr_t*)start_of_vtable() + vtable_length(); }
inline intptr_t* InstanceKlass::end_of_itable()     const { return start_of_itable() + itable_length(); }

inline oop InstanceKlass::static_field_base_raw() { return java_mirror(); }

inline Symbol* InstanceKlass::field_name(int index) const { return field(index).name(constants()); }
inline Symbol* InstanceKlass::field_signature(int index) const { return field(index).signature(constants()); }

inline int InstanceKlass::java_fields_count() const { return FieldInfoStream::num_java_fields(fieldinfo_stream()); }
inline int InstanceKlass::total_fields_count() const { return FieldInfoStream::num_total_fields(fieldinfo_stream()); }

inline OopMapBlock* InstanceKlass::start_of_nonstatic_oop_maps() const {
  return (OopMapBlock*)(start_of_itable() + itable_length());
}

inline Klass** InstanceKlass::end_of_nonstatic_oop_maps() const {
  return (Klass**)(start_of_nonstatic_oop_maps() +
                   nonstatic_oop_map_count());
}

inline InstanceKlass* volatile* InstanceKlass::adr_implementor() const {
  if (is_interface()) {
    return (InstanceKlass* volatile*)end_of_nonstatic_oop_maps();
  } else {
    return nullptr;
  }
}

inline InlineKlass* InstanceKlass::get_inline_type_field_klass(int idx) const {
  assert(has_inline_type_fields(), "Sanity checking");
  assert(idx < java_fields_count(), "IOOB");
  InlineKlass* k = inline_type_field_klasses_array()->at(idx);
  assert(k != nullptr, "Should always be set before being read");
  return k;
}

inline InlineKlass* InstanceKlass::get_inline_type_field_klass_or_null(int idx) const {
  assert(has_inline_type_fields(), "Sanity checking");
  assert(idx < java_fields_count(), "IOOB");
  InlineKlass* k = inline_type_field_klasses_array()->at(idx);
  return k;
}

inline void InstanceKlass::set_inline_type_field_klass(int idx, InlineKlass* k) {
  assert(has_inline_type_fields(), "Sanity checking");
  assert(idx < java_fields_count(), "IOOB");
  assert(k != nullptr, "Should not be set to nullptr");
  assert(inline_type_field_klasses_array() != nullptr, "array must have been created");
  assert(inline_type_field_klasses_array()->at(idx) == nullptr, "Should not be set twice");
  inline_type_field_klasses_array()->at_put(idx, k);
}

inline void InstanceKlass::reset_inline_type_field_klass(int idx) {
  assert(has_inline_type_fields(), "Sanity checking");
  assert(idx < java_fields_count(), "IOOB");
  inline_type_field_klasses_array()->at_put(idx, nullptr);
}


inline ArrayKlass* InstanceKlass::array_klasses_acquire() const {
  return Atomic::load_acquire(&_array_klasses);
}

inline void InstanceKlass::release_set_array_klasses(ArrayKlass* k) {
  Atomic::release_store(&_array_klasses, k);
}

inline jmethodID* InstanceKlass::methods_jmethod_ids_acquire() const {
  return Atomic::load_acquire(&_methods_jmethod_ids);
}

inline void InstanceKlass::release_set_methods_jmethod_ids(jmethodID* jmeths) {
  Atomic::release_store(&_methods_jmethod_ids, jmeths);
}

// The iteration over the oops in objects is a hot path in the GC code.
// By force inlining the following functions, we get similar GC performance
// as the previous macro based implementation.

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_map(OopMapBlock* map, oop obj, OopClosureType* closure) {
  T* p         = obj->field_addr<T>(map->offset());
  T* const end = p + map->count();

  for (; p < end; ++p) {
    Devirtualizer::do_oop(closure, p);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_map_reverse(OopMapBlock* map, oop obj, OopClosureType* closure) {
  T* const start = obj->field_addr<T>(map->offset());
  T*       p     = start + map->count();

  while (start < p) {
    --p;
    Devirtualizer::do_oop(closure, p);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_map_bounded(OopMapBlock* map, oop obj, OopClosureType* closure, MemRegion mr) {
  T* p   = obj->field_addr<T>(map->offset());
  T* end = p + map->count();

  T* const l   = (T*)mr.start();
  T* const h   = (T*)mr.end();
  assert(mask_bits((intptr_t)l, sizeof(T)-1) == 0 &&
         mask_bits((intptr_t)h, sizeof(T)-1) == 0,
         "bounded region must be properly aligned");

  if (p < l) {
    p = l;
  }
  if (end > h) {
    end = h;
  }

  for (;p < end; ++p) {
    Devirtualizer::do_oop(closure, p);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_maps(oop obj, OopClosureType* closure) {
  OopMapBlock* map           = start_of_nonstatic_oop_maps();
  OopMapBlock* const end_map = map + nonstatic_oop_map_count();

  for (; map < end_map; ++map) {
    oop_oop_iterate_oop_map<T>(map, obj, closure);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_maps_reverse(oop obj, OopClosureType* closure) {
  OopMapBlock* const start_map = start_of_nonstatic_oop_maps();
  OopMapBlock* map             = start_map + nonstatic_oop_map_count();

  while (start_map < map) {
    --map;
    oop_oop_iterate_oop_map_reverse<T>(map, obj, closure);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_oop_maps_bounded(oop obj, OopClosureType* closure, MemRegion mr) {
  OopMapBlock* map           = start_of_nonstatic_oop_maps();
  OopMapBlock* const end_map = map + nonstatic_oop_map_count();

  for (;map < end_map; ++map) {
    oop_oop_iterate_oop_map_bounded<T>(map, obj, closure, mr);
  }
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate(oop obj, OopClosureType* closure) {
  if (Devirtualizer::do_metadata(closure)) {
    Devirtualizer::do_klass(closure, this);
  }

  oop_oop_iterate_oop_maps<T>(obj, closure);
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_reverse(oop obj, OopClosureType* closure) {
  assert(!Devirtualizer::do_metadata(closure),
      "Code to handle metadata is not implemented");

  oop_oop_iterate_oop_maps_reverse<T>(obj, closure);
}

template <typename T, class OopClosureType>
ALWAYSINLINE void InstanceKlass::oop_oop_iterate_bounded(oop obj, OopClosureType* closure, MemRegion mr) {
  if (Devirtualizer::do_metadata(closure)) {
    if (mr.contains(obj)) {
      Devirtualizer::do_klass(closure, this);
    }
  }

  oop_oop_iterate_oop_maps_bounded<T>(obj, closure, mr);
}

#endif // SHARE_OOPS_INSTANCEKLASS_INLINE_HPP
