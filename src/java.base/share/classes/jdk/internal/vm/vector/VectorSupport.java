/*
 * Copyright (c) 2020, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 */

package jdk.internal.vm.vector;

import java.util.Objects;
import jdk.internal.vm.annotation.ForceInline;
import jdk.internal.vm.annotation.IntrinsicCandidate;
import jdk.internal.vm.annotation.MultiField;
import jdk.internal.misc.Unsafe;

import java.util.function.*;
import java.lang.reflect.*;

public class VectorSupport {
    static {
        registerNatives();
    }

    private static final Unsafe U = Unsafe.getUnsafe();

    // Unary
    public static final int VECTOR_OP_ABS  = 0;
    public static final int VECTOR_OP_NEG  = 1;
    public static final int VECTOR_OP_SQRT = 2;
    public static final int VECTOR_OP_BIT_COUNT = 3;

    // Binary
    public static final int VECTOR_OP_ADD  = 4;
    public static final int VECTOR_OP_SUB  = 5;
    public static final int VECTOR_OP_MUL  = 6;
    public static final int VECTOR_OP_DIV  = 7;
    public static final int VECTOR_OP_MIN  = 8;
    public static final int VECTOR_OP_MAX  = 9;

    public static final int VECTOR_OP_AND  = 10;
    public static final int VECTOR_OP_OR   = 11;
    public static final int VECTOR_OP_XOR  = 12;

    // Ternary
    public static final int VECTOR_OP_FMA  = 13;

    // Broadcast int
    public static final int VECTOR_OP_LSHIFT  = 14;
    public static final int VECTOR_OP_RSHIFT  = 15;
    public static final int VECTOR_OP_URSHIFT = 16;

    public static final int VECTOR_OP_CAST        = 17;
    public static final int VECTOR_OP_UCAST       = 18;
    public static final int VECTOR_OP_REINTERPRET = 19;

    // Mask manipulation operations
    public static final int VECTOR_OP_MASK_TRUECOUNT = 20;
    public static final int VECTOR_OP_MASK_FIRSTTRUE = 21;
    public static final int VECTOR_OP_MASK_LASTTRUE  = 22;
    public static final int VECTOR_OP_MASK_TOLONG    = 23;

    // Rotate operations
    public static final int VECTOR_OP_LROTATE = 24;
    public static final int VECTOR_OP_RROTATE = 25;

    // Compression expansion operations
    public static final int VECTOR_OP_COMPRESS = 26;
    public static final int VECTOR_OP_EXPAND = 27;
    public static final int VECTOR_OP_MASK_COMPRESS = 28;

    // Leading/Trailing zeros count operations
    public static final int VECTOR_OP_TZ_COUNT  = 29;
    public static final int VECTOR_OP_LZ_COUNT  = 30;

    // Reverse operation
    public static final int VECTOR_OP_REVERSE   = 31;
    public static final int VECTOR_OP_REVERSE_BYTES = 32;

    // Compress and Expand Bits operation
    public static final int VECTOR_OP_COMPRESS_BITS = 33;
    public static final int VECTOR_OP_EXPAND_BITS = 34;

    // Math routines
    public static final int VECTOR_OP_TAN = 101;
    public static final int VECTOR_OP_TANH = 102;
    public static final int VECTOR_OP_SIN = 103;
    public static final int VECTOR_OP_SINH = 104;
    public static final int VECTOR_OP_COS = 105;
    public static final int VECTOR_OP_COSH = 106;
    public static final int VECTOR_OP_ASIN = 107;
    public static final int VECTOR_OP_ACOS = 108;
    public static final int VECTOR_OP_ATAN = 109;
    public static final int VECTOR_OP_ATAN2 = 110;
    public static final int VECTOR_OP_CBRT = 111;
    public static final int VECTOR_OP_LOG = 112;
    public static final int VECTOR_OP_LOG10 = 113;
    public static final int VECTOR_OP_LOG1P = 114;
    public static final int VECTOR_OP_POW = 115;
    public static final int VECTOR_OP_EXP = 116;
    public static final int VECTOR_OP_EXPM1 = 117;
    public static final int VECTOR_OP_HYPOT = 118;

    // See src/hotspot/share/opto/subnode.hpp
    //     struct BoolTest, and enclosed enum mask
    public static final int BT_eq = 0;  // 0000
    public static final int BT_ne = 4;  // 0100
    public static final int BT_le = 5;  // 0101
    public static final int BT_ge = 7;  // 0111
    public static final int BT_lt = 3;  // 0011
    public static final int BT_gt = 1;  // 0001
    public static final int BT_overflow = 2;     // 0010
    public static final int BT_no_overflow = 6;  // 0110
    // never = 8    1000
    // illegal = 9  1001
    // Unsigned comparisons apply to BT_le, BT_ge, BT_lt, BT_gt for integral types
    public static final int BT_unsigned_compare = 0b10000;
    public static final int BT_ule = BT_le | BT_unsigned_compare;
    public static final int BT_uge = BT_ge | BT_unsigned_compare;
    public static final int BT_ult = BT_lt | BT_unsigned_compare;
    public static final int BT_ugt = BT_gt | BT_unsigned_compare;

    // Various broadcasting modes.
    public static final int MODE_BROADCAST = 0;
    public static final int MODE_BITS_COERCED_LONG_TO_MASK = 1;

    // BasicType codes, for primitives only:
    public static final int
        T_FLOAT   = 6,
        T_DOUBLE  = 7,
        T_BYTE    = 8,
        T_SHORT   = 9,
        T_INT     = 10,
        T_LONG    = 11;

    /* ============================================================================ */

    public static class VectorSpecies<E> { }

    public abstract static class VectorPayload { }

    public static abstract class Vector<E> extends VectorPayload { }

    public static abstract class VectorMask<E> extends VectorPayload { }

    public static abstract class VectorShuffle<E> extends VectorPayload { }

    public abstract static class VectorPayloadMF {
        public abstract long multiFieldOffset();

        @ForceInline
        public static VectorPayloadMF newMaskInstanceFactory(Class<?> elemType, int length, boolean max_payload) {
            if (!max_payload) {
                switch(length) {
                    case  1: return new VectorPayloadMF8Z();
                    case  2: return new VectorPayloadMF16Z();
                    case  4: return new VectorPayloadMF32Z();
                    case  8: return new VectorPayloadMF64Z();
                    case 16: return new VectorPayloadMF128Z();
                    case 32: return new VectorPayloadMF256Z();
                    case 64: return new VectorPayloadMF512Z();
                    default: assert false : "Unhandled vector mask size";
                }
            } else {
                if (elemType == byte.class) {
                   return new VectorPayloadMFMaxBZ();
                } else if (elemType == short.class) {
                   return new VectorPayloadMFMaxSZ();
                } else if (elemType == int.class || elemType == float.class) {
                   return new VectorPayloadMFMaxIZ();
                } else if (elemType == long.class || elemType == double.class) {
                   return new VectorPayloadMFMaxLZ();
                } else {
                   assert false : "Unexpected lane type";
                }
            }
            return null;
        }

        @ForceInline
        public static VectorPayloadMF newShuffleInstanceFactory(Class<?> elemType, int length, boolean max_payload) {
            if (!max_payload) {
                switch(length) {
                    case  1: return new VectorPayloadMF8B();
                    case  2: return new VectorPayloadMF16B();
                    case  4: return new VectorPayloadMF32B();
                    case  8: return new VectorPayloadMF64B();
                    case 16: return new VectorPayloadMF128B();
                    case 32: return new VectorPayloadMF256B();
                    case 64: return new VectorPayloadMF512B();
                    default: assert false : "Unhandled vector shuffle size";
                }
            } else {
                if (elemType == byte.class) {
                   return new VectorPayloadMFMaxBB();
                } else if (elemType == short.class) {
                   return new VectorPayloadMFMaxSB();
                } else if (elemType == int.class || elemType == float.class) {
                   return new VectorPayloadMFMaxIB();
                } else if (elemType == long.class || elemType == double.class) {
                   return new VectorPayloadMFMaxLB();
                } else {
                   assert false : "Unexpected lane type";
                }
            }
            return null;
        }

        @ForceInline
        public static VectorPayloadMF newVectorInstanceFactory(Class<?> elemType, int length, boolean max_payload) {
            if (false == max_payload) {
                if (elemType == byte.class) {
                    switch(length) {
                        case  8: return new VectorPayloadMF64B();
                        case 16: return new VectorPayloadMF128B();
                        case 32: return new VectorPayloadMF256B();
                        case 64: return new VectorPayloadMF512B();
                        default: assert false : "Unhandled vector size";
                    }
                } else if (elemType == short.class) {
                    switch(length) {
                        case  4: return new VectorPayloadMF64S();
                        case  8: return new VectorPayloadMF128S();
                        case 16: return new VectorPayloadMF256S();
                        case 32: return new VectorPayloadMF512S();
                        default: assert false : "Unhandled vector size";
                    }
                } else if (elemType == int.class) {
                    switch(length) {
                        case  2: return new VectorPayloadMF64I();
                        case  4: return new VectorPayloadMF128I();
                        case  8: return new VectorPayloadMF256I();
                        case 16: return new VectorPayloadMF512I();
                        default: assert false : "Unhandled vector size";
                    }
                } else if (elemType == long.class) {
                    switch(length) {
                        case  1: return new VectorPayloadMF64L();
                        case  2: return new VectorPayloadMF128L();
                        case  4: return new VectorPayloadMF256L();
                        case  8: return new VectorPayloadMF512L();
                        default: assert false : "Unhandled vector size";
                    }
                } else if (elemType == float.class) {
                    switch(length) {
                        case  2: return new VectorPayloadMF64F();
                        case  4: return new VectorPayloadMF128F();
                        case  8: return new VectorPayloadMF256F();
                        case 16: return new VectorPayloadMF512F();
                        default: assert false : "Unhandled vector size";
                    }
                } else {
                    assert elemType == double.class;
                    switch(length) {
                        case  1: return new VectorPayloadMF64D();
                        case  2: return new VectorPayloadMF128D();
                        case  4: return new VectorPayloadMF256D();
                        case  8: return new VectorPayloadMF512D();
                        default: assert false : "Unhandled vector size";
                    }
                }
            } else {
                if (elemType == byte.class) {
                    return new VectorPayloadMFMaxB();
                } else if (elemType == short.class) {
                    return new VectorPayloadMFMaxS();
                } else if (elemType == int.class) {
                    return new VectorPayloadMFMaxI();
                } else if (elemType == long.class) {
                    return new VectorPayloadMFMaxL();
                } else if (elemType == float.class) {
                    return new VectorPayloadMFMaxF();
                } else {
                    assert elemType == double.class;
                    return new VectorPayloadMFMaxD();
                }
            }
            return null;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceB(int length, byte[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(byte.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putByte(obj, start_offset + i * Byte.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceS(int length, short[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(short.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putShort(obj, start_offset + i * Short.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceI(int length, int[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(int.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putInt(obj, start_offset + i * Integer.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceL(int length, long[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(long.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putLong(obj, start_offset + i * Long.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceF(int length, float[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(float.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putFloat(obj, start_offset + i * Float.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        @ForceInline
        public static VectorPayloadMF createVectPayloadInstanceD(int length, double[] init, boolean max_payload) {
            VectorPayloadMF obj = newVectorInstanceFactory(double.class, length, max_payload);
            obj = U.makePrivateBuffer(obj);
            long start_offset = obj.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                U.putDouble(obj, start_offset + i * Double.BYTES, init[i]);
            }
            obj = U.finishPrivateBuffer(obj);
            return obj;
        }

        public int length() {
            return getClass().getDeclaredFields().length - 1;
        }

        public static long multiFieldOffset(Class<? extends VectorPayloadMF> cls) {
            try {
                var field = cls.getDeclaredField("mfield");
                return U.objectFieldOffset(field);
            } catch (Exception e) {
                System.out.println(e);
            }
            return -1L;
        }
    }

    public primitive static class VectorPayloadMFMaxB extends VectorPayloadMF {
        @MultiField(value = -1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxB.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxS extends VectorPayloadMF {
        @MultiField(value = -1)
        short mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxS.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxI extends VectorPayloadMF {
        @MultiField(value = -1)
        int mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxI.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxL extends VectorPayloadMF {
        @MultiField(value = -1)
        long mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxL.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxF extends VectorPayloadMF {
        @MultiField(value = -1)
        float mfield = 0.0f;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxF.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxD extends VectorPayloadMF {
        @MultiField(value = -1)
        double mfield = 0.0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxD.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF8Z extends VectorPayloadMF {
        @MultiField(value = 1)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF8Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF16Z extends VectorPayloadMF {
        @MultiField(value = 2)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF16Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF32Z extends VectorPayloadMF {
        @MultiField(value = 4)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF32Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64Z extends VectorPayloadMF {
        @MultiField(value = 8)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128Z extends VectorPayloadMF {
        @MultiField(value = 16)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256Z extends VectorPayloadMF {
        @MultiField(value = 32)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512Z extends VectorPayloadMF {
        @MultiField(value = 64)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512Z.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxBZ extends VectorPayloadMF {
        @MultiField(value = -1)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxBZ.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxSZ extends VectorPayloadMF {
        @MultiField(value = -1)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxSZ.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxIZ extends VectorPayloadMF {
        @MultiField(value = -1)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxIZ.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxLZ extends VectorPayloadMF {
        @MultiField(value = -1)
        boolean mfield = false;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxLZ.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF8B extends VectorPayloadMF {
        @MultiField(value = 1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF8B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF16B extends VectorPayloadMF {
        @MultiField(value = 2)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF16B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF32B extends VectorPayloadMF {
        @MultiField(value = 4)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF32B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64B extends VectorPayloadMF {
        @MultiField(value = 8)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128B extends VectorPayloadMF {
        @MultiField(value = 16)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256B extends VectorPayloadMF {
        @MultiField(value = 32)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512B extends VectorPayloadMF {
        @MultiField(value = 64)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512B.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxBB extends VectorPayloadMF {
        @MultiField(value = -1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxBB.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxSB extends VectorPayloadMF {
        @MultiField(value = -1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxSB.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxIB extends VectorPayloadMF {
        @MultiField(value = -1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxIB.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMFMaxLB extends VectorPayloadMF {
        @MultiField(value = -1)
        byte mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMFMaxLB.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64S extends VectorPayloadMF {
        @MultiField(value = 4)
        short mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64S.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128S extends VectorPayloadMF {
        @MultiField(value = 8)
        short mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128S.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256S extends VectorPayloadMF {
        @MultiField(value = 16)
        short mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256S.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512S extends VectorPayloadMF {
        @MultiField(value = 32)
        short mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512S.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64I extends VectorPayloadMF {
        @MultiField(value = 2)
        int mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64I.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128I extends VectorPayloadMF {
        @MultiField(value = 4)
        int mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128I.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256I extends VectorPayloadMF {
        @MultiField(value = 8)
        int mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256I.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512I extends VectorPayloadMF {
        @MultiField(value = 16)
        int mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512I.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64L extends VectorPayloadMF {
        @MultiField(value = 1)
        final long mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64L.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128L extends VectorPayloadMF {
        @MultiField(value = 2)
        final long mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128L.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256L extends VectorPayloadMF {
        @MultiField(value = 4)
        final long mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256L.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512L extends VectorPayloadMF {
        @MultiField(value = 8)
        final long mfield = 0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512L.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64F extends VectorPayloadMF {
        @MultiField(value = 2)
        float mfield = 0.0f;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64F.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128F extends VectorPayloadMF {
        @MultiField(value = 4)
        float mfield = 0.0f;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128F.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256F extends VectorPayloadMF {
        @MultiField(value = 8)
        float mfield = 0.0f;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256F.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512F extends VectorPayloadMF {
        @MultiField(value = 16)
        float mfield = 0.0f;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512F.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF64D extends VectorPayloadMF {
        @MultiField(value = 1)
        double mfield = 0.0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF64D.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF128D extends VectorPayloadMF {
        @MultiField(value = 2)
        double mfield = 0.0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF128D.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF256D extends VectorPayloadMF {
        @MultiField(value = 4)
        double mfield = 0.0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF256D.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    public primitive static class VectorPayloadMF512D extends VectorPayloadMF {
        @MultiField(value = 8)
        double mfield = 0.0;
        static final long MFOFFSET = multiFieldOffset(VectorPayloadMF512D.class);

        @Override
        public long multiFieldOffset() { return MFOFFSET; }
    }

    /* ============================================================================ */
    public interface FromBitsCoercedOperation<VM extends VectorPayload,
                                              S extends VectorSpecies<?>> {
        VM fromBits(long l, S s);
    }

    @IntrinsicCandidate
    public static
    <VM extends VectorPayload,
     S extends VectorSpecies<E>,
     E>
    VM fromBitsCoerced(Class<? extends VM> vmClass, Class<E> eClass,
                       int length,
                       long bits, int mode, S s,
                       FromBitsCoercedOperation<VM, S> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.fromBits(bits, s);
    }

    /* ============================================================================ */
    public interface IndexPartiallyInUpperRangeOperation<E,
                                                         M extends VectorMask<E>> {
        M apply(long offset, long limit);
    }

    @IntrinsicCandidate
    public static
    <E,
     M extends VectorMask<E>>
    M indexPartiallyInUpperRange(Class<? extends M> mClass, Class<E> eClass,
                                 int length, long offset, long limit,
                                 IndexPartiallyInUpperRangeOperation<E, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(offset, limit);
    }

    /* ============================================================================ */
    public interface ShuffleIotaOperation<S extends VectorSpecies<?>,
                                          SH extends VectorShuffle<?>> {
        SH apply(int length, int start, int step, S s);
    }

    @IntrinsicCandidate
    public static
    <E,
     S extends VectorSpecies<E>,
     SH extends VectorShuffle<E>>
    SH shuffleIota(Class<E> eClass, Class<? extends SH> shClass, S s,
                   int length,
                   int start, int step, int wrap,
                   ShuffleIotaOperation<S, SH> defaultImpl) {
       assert isNonCapturingLambda(defaultImpl) : defaultImpl;
       return defaultImpl.apply(length, start, step, s);
    }

    public interface ShuffleToVectorOperation<V extends Vector<?>,
                                              SH extends VectorShuffle<?>> {
       V apply(SH sh);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     SH extends VectorShuffle<E>,
     E>
    V shuffleToVector(Class<? extends Vector<E>> vClass, Class<E> eClass, Class<? extends SH> shClass, SH sh,
                      int length,
                      ShuffleToVectorOperation<V, SH> defaultImpl) {
      assert isNonCapturingLambda(defaultImpl) : defaultImpl;
      return defaultImpl.apply(sh);
    }

    /* ============================================================================ */
    public interface IndexOperation<V extends Vector<?>,
                                    S extends VectorSpecies<?>> {
        V index(V v, int step, S s);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     E,
     S extends VectorSpecies<E>>
    V indexVector(Class<? extends V> vClass, Class<E> eClass,
                  int length,
                  V v, int step, S s,
                  IndexOperation<V, S> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.index(v, step, s);
    }

    /* ============================================================================ */

    public interface ReductionOperation<V extends Vector<?>,
                                        M extends VectorMask<?>> {
        long apply(V v, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    long reductionCoerced(int oprId,
                          Class<? extends V> vClass, Class<? extends M> mClass, Class<E> eClass,
                          int length,
                          V v, M m,
                          ReductionOperation<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, m);
    }


    /* ============================================================================ */

    public interface VecExtractOp<VM extends VectorPayload> {
        long apply(VM vm, int i);
    }

    @IntrinsicCandidate
    public static
    <VM extends VectorPayload,
     E>
    long extract(Class<? extends VM> vClass, Class<E> eClass,
                 int length,
                 VM vm, int i,
                 VecExtractOp<VM> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(vm, i);
    }

    /* ============================================================================ */

    public interface VecInsertOp<V extends Vector<?>> {
        V apply(V v, int i, long val);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     E>
    V insert(Class<? extends V> vClass, Class<E> eClass,
             int length,
             V v, int i, long val,
             VecInsertOp<V> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, i, val);
    }

    /* ============================================================================ */

    public interface UnaryOperation<V extends Vector<?>,
                                    M extends VectorMask<?>> {
        V apply(V v, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    V unaryOp(int oprId,
              Class<? extends V> vClass, Class<? extends M> mClass, Class<E> eClass,
              int length,
              V v, M m,
              UnaryOperation<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, m);
    }

    /* ============================================================================ */

    public interface BinaryOperation<VM extends VectorPayload,
                                     M extends VectorMask<?>> {
        VM apply(VM v1, VM v2, M m);
    }

    @IntrinsicCandidate
    public static
    <VM extends VectorPayload,
     M extends VectorMask<E>,
     E>
    VM binaryOp(int oprId,
                Class<? extends VM> vmClass, Class<? extends M> mClass, Class<E> eClass,
                int length,
                VM v1, VM v2, M m,
                BinaryOperation<VM, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v1, v2, m);
    }

    /* ============================================================================ */

    public interface TernaryOperation<V extends Vector<?>,
                                      M extends VectorMask<?>> {
        V apply(V v1, V v2, V v3, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    V ternaryOp(int oprId,
                Class<? extends V> vClass, Class<? extends M> mClass, Class<E> eClass,
                int length,
                V v1, V v2, V v3, M m,
                TernaryOperation<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v1, v2, v3, m);
    }

    /* ============================================================================ */

    // Memory operations

    public interface LoadOperation<C,
                                   VM extends VectorPayload,
                                   S extends VectorSpecies<?>> {
        VM load(C container, long index, S s);
    }

    @IntrinsicCandidate
    public static
    <C,
     VM extends VectorPayload,
     E,
     S extends VectorSpecies<E>>
    VM load(Class<? extends VM> vmClass, Class<E> eClass,
            int length,
            Object base, long offset,
            C container, long index, S s,
            LoadOperation<C, VM, S> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.load(container, index, s);
    }

    /* ============================================================================ */

    public interface LoadVectorMaskedOperation<C,
                                               V extends Vector<?>,
                                               S extends VectorSpecies<?>,
                                               M extends VectorMask<?>> {
        V load(C container, long index, S s, M m);
    }

    @IntrinsicCandidate
    public static
    <C,
     V extends Vector<?>,
     E,
     S extends VectorSpecies<E>,
     M extends VectorMask<E>>
    V loadMasked(Class<? extends V> vClass, Class<M> mClass, Class<E> eClass,
                 int length, Object base, long offset,
                 M m, int offsetInRange,
                 C container, long index, S s,
                 LoadVectorMaskedOperation<C, V, S, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.load(container, index, s, m);
    }

    /* ============================================================================ */

    public interface LoadVectorOperationWithMap<C,
                                                V extends Vector<?>,
                                                S extends VectorSpecies<?>,
                                                M extends VectorMask<?>> {
        V loadWithMap(C container, int index, int[] indexMap, int indexM, S s, M m);
    }

    @IntrinsicCandidate
    public static
    <C,
     V extends Vector<?>,
     W extends Vector<Integer>,
     S extends VectorSpecies<E>,
     M extends VectorMask<E>,
     E>
    V loadWithMap(Class<? extends V> vClass, Class<M> mClass, Class<E> eClass,
                  int length,
                  Class<? extends Vector<Integer>> vectorIndexClass,
                  Object base, long offset,
                  W index_vector,
                  M m, C container, int index, int[] indexMap, int indexM, S s,
                  LoadVectorOperationWithMap<C, V, S, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.loadWithMap(container, index, indexMap, indexM, s, m);
    }

    /* ============================================================================ */

    public interface StoreVectorOperation<C,
                                          V extends VectorPayload> {
        void store(C container, long index, V v);
    }

    @IntrinsicCandidate
    public static
    <C,
     V extends VectorPayload>
    void store(Class<?> vClass, Class<?> eClass,
               int length,
               Object base, long offset,
               V v, C container, long index,
               StoreVectorOperation<C, V> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        defaultImpl.store(container, index, v);
    }

    public interface StoreVectorMaskedOperation<C,
                                                V extends Vector<?>,
                                                M extends VectorMask<?>> {
        void store(C container, long index, V v, M m);
    }

    @IntrinsicCandidate
    public static
    <C,
     V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    void storeMasked(Class<? extends V> vClass, Class<M> mClass, Class<E> eClass,
                     int length,
                     Object base, long offset,
                     V v, M m, C container, long index,
                     StoreVectorMaskedOperation<C, V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        defaultImpl.store(container, index, v, m);
    }

    /* ============================================================================ */

    public interface StoreVectorOperationWithMap<C,
                                                 V extends Vector<?>,
                                                 M extends VectorMask<?>> {
        void storeWithMap(C container, int index, V v, int[] indexMap, int indexM, M m);
    }

    @IntrinsicCandidate
    public static
    <C,
     V extends Vector<E>,
     W extends Vector<Integer>,
     M extends VectorMask<E>,
     E>
    void storeWithMap(Class<? extends V> vClass, Class<M> mClass, Class<E> eClass,
                      int length,
                      Class<? extends Vector<Integer>> vectorIndexClass,
                      Object base, long offset,
                      W index_vector,
                      V v, M m, C container, int index, int[] indexMap, int indexM,
                      StoreVectorOperationWithMap<C, V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        defaultImpl.storeWithMap(container, index, v, indexMap, indexM, m);
    }

    /* ============================================================================ */

    @IntrinsicCandidate
    public static
    <M extends VectorMask<E>,
     E>
    boolean test(int cond,
                 Class<?> mClass, Class<?> eClass,
                 int length,
                 M m1, M m2,
                 BiFunction<M, M, Boolean> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(m1, m2);
    }

    /* ============================================================================ */

    public interface VectorCompareOp<V extends Vector<?>,
                                     M extends VectorMask<?>> {
        M apply(int cond, V v1, V v2, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    M compare(int cond,
              Class<? extends V> vectorClass, Class<M> mClass, Class<E> eClass,
              int length,
              V v1, V v2, M m,
              VectorCompareOp<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(cond, v1, v2, m);
    }

    /* ============================================================================ */
    public interface VectorRearrangeOp<V extends Vector<?>,
                                       SH extends VectorShuffle<?>,
                                       M extends VectorMask<?>> {
        V apply(V v, SH sh, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     SH extends VectorShuffle<E>,
     M  extends VectorMask<E>,
     E>
    V rearrangeOp(Class<? extends V> vClass, Class<SH> shClass, Class<M> mClass, Class<E> eClass,
                  int length,
                  V v, SH sh, M m,
                  VectorRearrangeOp<V, SH, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, sh, m);
    }

    /* ============================================================================ */

    public interface VectorBlendOp<V extends Vector<?>,
                                   M extends VectorMask<?>> {
        V apply(V v1, V v2, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    V blend(Class<? extends V> vClass, Class<M> mClass, Class<E> eClass,
            int length,
            V v1, V v2, M m,
            VectorBlendOp<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v1, v2, m);
    }

    /* ============================================================================ */

    public interface VectorBroadcastIntOp<V extends Vector<?>,
                                          M extends VectorMask<?>> {
        V apply(V v, int n, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    V broadcastInt(int opr,
                   Class<? extends V> vClass, Class<? extends M> mClass, Class<E> eClass,
                   int length,
                   V v, int n, M m,
                   VectorBroadcastIntOp<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, n, m);
    }

    /* ============================================================================ */

    public interface VectorConvertOp<VOUT extends VectorPayload,
                                     VIN extends VectorPayload,
                                     S extends VectorSpecies<?>> {
        VOUT apply(VIN v, S s);
    }

    // Users of this intrinsic assume that it respects
    // REGISTER_ENDIAN, which is currently ByteOrder.LITTLE_ENDIAN.
    // See javadoc for REGISTER_ENDIAN.

    @IntrinsicCandidate
    public static <VOUT extends VectorPayload,
                    VIN extends VectorPayload,
                      S extends VectorSpecies<?>>
    VOUT convert(int oprId,
              Class<?> fromVectorClass, Class<?> fromeClass, int fromVLen,
              Class<?>   toVectorClass, Class<?>   toeClass, int   toVLen,
              VIN v, S s,
              VectorConvertOp<VOUT, VIN, S> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, s);
    }

    /* ============================================================================ */

    public interface CompressExpandOperation<V extends Vector<?>,
                                     M extends VectorMask<?>> {
        VectorPayload apply(V v, M m);
    }

    @IntrinsicCandidate
    public static
    <V extends Vector<E>,
     M extends VectorMask<E>,
     E>
    VectorPayload compressExpandOp(int opr,
                                   Class<? extends V> vClass, Class<? extends M> mClass, Class<E> eClass,
                                   int length, V v, M m,
                                   CompressExpandOperation<V, M> defaultImpl) {
        assert isNonCapturingLambda(defaultImpl) : defaultImpl;
        return defaultImpl.apply(v, m);
    }

    /* ============================================================================ */

    @IntrinsicCandidate
    public static
    <VP extends VectorPayload>
    VP maybeRebox(VP v) {
        // The fence is added here to avoid memory aliasing problems in C2 between scalar & vector accesses.
        // TODO: move the fence generation into C2. Generate only when reboxing is taking place.
        U.loadFence();
        return v;
    }

    /* ============================================================================ */
    public interface VectorMaskOp<M extends VectorMask<?>> {
        long apply(M m);
    }

    @IntrinsicCandidate
    public static
    <M extends VectorMask<E>,
     E>
    long maskReductionCoerced(int oper,
                              Class<? extends M> mClass, Class<?> eClass,
                              int length,
                              M m,
                              VectorMaskOp<M> defaultImpl) {
       assert isNonCapturingLambda(defaultImpl) : defaultImpl;
       return defaultImpl.apply(m);
    }

    /* ============================================================================ */

    // query the JVM's supported vector sizes and types
    public static native int getMaxLaneCount(Class<?> etype);

    /* ============================================================================ */

    public static boolean isNonCapturingLambda(Object o) {
        return o.getClass().getDeclaredFields().length == 0;
    }

    /* ============================================================================ */

    private static native int registerNatives();
}
