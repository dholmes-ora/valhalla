/*
 * Copyright (c) 2017, 2023, Oracle and/or its affiliates. All rights reserved.
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
package jdk.incubator.vector;

import java.lang.foreign.MemorySegment;
import java.util.Arrays;
import java.util.Objects;
import jdk.internal.misc.Unsafe;
import java.util.function.IntUnaryOperator;

import jdk.internal.vm.annotation.ForceInline;
import jdk.internal.vm.vector.VectorSupport;

import static jdk.internal.vm.vector.VectorSupport.*;

import static jdk.incubator.vector.VectorOperators.*;

// -- This file was mechanically generated: Do not edit! -- //

@SuppressWarnings("cast")  // warning: redundant cast
value class Long64Vector extends LongVector {
    static final LongSpecies VSPECIES =
        (LongSpecies) LongVector.SPECIES_64;

    static final VectorShape VSHAPE =
        VSPECIES.vectorShape();

    static final Class<Long64Vector> VCLASS = Long64Vector.class;

    static final int VSIZE = VSPECIES.vectorBitSize();

    static final int VLENGTH = VSPECIES.laneCount(); // used by the JVM

    static final Class<Long> ETYPE = long.class; // used by the JVM

    static final long MFOFFSET = VectorPayloadMF.multiFieldOffset(VectorPayloadMF64L.class);

    private final VectorPayloadMF64L payload;

    Long64Vector(Object value) {
        this.payload = (VectorPayloadMF64L) value;
    }

    @ForceInline
    @Override
    final VectorPayloadMF vec() {
        return payload;
    }

    static final Long64Vector ZERO = new Long64Vector(VectorPayloadMF.newInstanceFactory(long.class, 1));
    static final Long64Vector IOTA = new Long64Vector(VectorPayloadMF.createVectPayloadInstanceL(1, (long[])(VSPECIES.iotaArray())));

    static {
        // Warm up a few species caches.
        // If we do this too much we will
        // get NPEs from bootstrap circularity.
        VSPECIES.dummyVectorMF();
        VSPECIES.withLanes(LaneType.BYTE);
    }

    // Specialized extractors

    @ForceInline
    final @Override
    public LongSpecies vspecies() {
        // ISSUE:  This should probably be a @Stable
        // field inside AbstractVector, rather than
        // a megamorphic method.
        return VSPECIES;
    }

    @ForceInline
    @Override
    public final Class<Long> elementType() { return long.class; }

    @ForceInline
    @Override
    public final int elementSize() { return Long.SIZE; }

    @ForceInline
    @Override
    public final VectorShape shape() { return VSHAPE; }

    @ForceInline
    @Override
    public final int length() { return VLENGTH; }

    @ForceInline
    @Override
    public final int bitSize() { return VSIZE; }

    @ForceInline
    @Override
    public final int byteSize() { return VSIZE / Byte.SIZE; }

    @ForceInline
    @Override
    public final long multiFieldOffset() { return MFOFFSET; }

    @Override
    @ForceInline
    public final Long64Vector broadcast(long e) {
        return (Long64Vector) super.broadcastTemplate(e);  // specialize
    }


    @Override
    @ForceInline
    Long64Mask maskFromPayload(VectorPayloadMF payload) {
        return new Long64Mask(payload);
    }

    @Override
    @ForceInline
    Long64Shuffle iotaShuffle() { return Long64Shuffle.IOTA; }

    @Override
    @ForceInline
    Long64Shuffle shuffleFromArray(int[] indices, int i) { return new Long64Shuffle(indices, i); }

    @Override
    @ForceInline
    Long64Shuffle shuffleFromOp(IntUnaryOperator fn) { return new Long64Shuffle(fn); }

    // Make a vector of the same species but the given elements:
    @ForceInline
    final @Override
    Long64Vector vectorFactory(VectorPayloadMF vec) {
        return new Long64Vector(vec);
    }

    @ForceInline
    final @Override
    Byte64Vector asByteVectorRaw() {
        return (Byte64Vector) super.asByteVectorRawTemplate();  // specialize
    }

    @ForceInline
    final @Override
    AbstractVector<?> asVectorRaw(LaneType laneType) {
        return super.asVectorRawTemplate(laneType);  // specialize
    }

    // Unary operator

    @ForceInline
    final @Override
    Long64Vector uOpMF(FUnOp f) {
        return (Long64Vector) super.uOpTemplateMF(f);  // specialize
    }

    @ForceInline
    final @Override
    Long64Vector uOpMF(VectorMask<Long> m, FUnOp f) {
        return (Long64Vector)
            super.uOpTemplateMF((Long64Mask)m, f);  // specialize
    }

    // Binary operator

    @ForceInline
    final @Override
    Long64Vector bOpMF(Vector<Long> v, FBinOp f) {
        return (Long64Vector) super.bOpTemplateMF((Long64Vector)v, f);  // specialize
    }

    @ForceInline
    final @Override
    Long64Vector bOpMF(Vector<Long> v,
                     VectorMask<Long> m, FBinOp f) {
        return (Long64Vector)
            super.bOpTemplateMF((Long64Vector)v, (Long64Mask)m,
                                f);  // specialize
    }

    // Ternary operator

    @ForceInline
    final @Override
    Long64Vector tOpMF(Vector<Long> v1, Vector<Long> v2, FTriOp f) {
        return (Long64Vector)
            super.tOpTemplateMF((Long64Vector)v1, (Long64Vector)v2,
                                f);  // specialize
    }

    @ForceInline
    final @Override
    Long64Vector tOpMF(Vector<Long> v1, Vector<Long> v2,
                     VectorMask<Long> m, FTriOp f) {
        return (Long64Vector)
            super.tOpTemplateMF((Long64Vector)v1, (Long64Vector)v2,
                                (Long64Mask)m, f);  // specialize
    }

    @ForceInline
    final @Override
    long rOpMF(long v, VectorMask<Long> m, FBinOp f) {
        return super.rOpTemplateMF(v, m, f);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> convertShape(VectorOperators.Conversion<Long,F> conv,
                           VectorSpecies<F> rsp, int part) {
        return super.convertShapeTemplate(conv, rsp, part);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> reinterpretShape(VectorSpecies<F> toSpecies, int part) {
        return super.reinterpretShapeTemplate(toSpecies, part);  // specialize
    }

    // Specialized algebraic operations:

    // The following definition forces a specialized version of this
    // crucial method into the v-table of this class.  A call to add()
    // will inline to a call to lanewise(ADD,), at which point the JIT
    // intrinsic will have the opcode of ADD, plus all the metadata
    // for this particular class, enabling it to generate precise
    // code.
    //
    // There is probably no benefit to the JIT to specialize the
    // masked or broadcast versions of the lanewise method.

    @Override
    @ForceInline
    public Long64Vector lanewise(Unary op) {
        return (Long64Vector) super.lanewiseTemplate(op);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector lanewise(Unary op, VectorMask<Long> m) {
        return (Long64Vector) super.lanewiseTemplate(op, Long64Mask.class, (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector lanewise(Binary op, Vector<Long> v) {
        return (Long64Vector) super.lanewiseTemplate(op, v);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector lanewise(Binary op, Vector<Long> v, VectorMask<Long> m) {
        return (Long64Vector) super.lanewiseTemplate(op, Long64Mask.class, v, (Long64Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Long64Vector
    lanewiseShift(VectorOperators.Binary op, int e) {
        return (Long64Vector) super.lanewiseShiftTemplate(op, e);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Long64Vector
    lanewiseShift(VectorOperators.Binary op, int e, VectorMask<Long> m) {
        return (Long64Vector) super.lanewiseShiftTemplate(op, Long64Mask.class, e, (Long64Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline
    public final
    Long64Vector
    lanewise(Ternary op, Vector<Long> v1, Vector<Long> v2) {
        return (Long64Vector) super.lanewiseTemplate(op, v1, v2);  // specialize
    }

    @Override
    @ForceInline
    public final
    Long64Vector
    lanewise(Ternary op, Vector<Long> v1, Vector<Long> v2, VectorMask<Long> m) {
        return (Long64Vector) super.lanewiseTemplate(op, Long64Mask.class, v1, v2, (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public final
    Long64Vector addIndex(int scale) {
        return (Long64Vector) super.addIndexTemplate(scale);  // specialize
    }

    // Type specific horizontal reductions

    @Override
    @ForceInline
    public final long reduceLanes(VectorOperators.Associative op) {
        return super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanes(VectorOperators.Associative op,
                                    VectorMask<Long> m) {
        return super.reduceLanesTemplate(op, Long64Mask.class, (Long64Mask) m);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op) {
        return (long) super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op,
                                        VectorMask<Long> m) {
        return (long) super.reduceLanesTemplate(op, Long64Mask.class, (Long64Mask) m);  // specialized
    }

    @Override
    @ForceInline
    public final
    <F> VectorShuffle<F> toShuffle(AbstractSpecies<F> dsp) {
        return super.toShuffleTemplate(dsp);
    }

    // Specialized unary testing

    @Override
    @ForceInline
    public final Long64Mask test(Test op) {
        return super.testTemplate(Long64Mask.class, op);  // specialize
    }

    @Override
    @ForceInline
    public final Long64Mask test(Test op, VectorMask<Long> m) {
        return super.testTemplate(Long64Mask.class, op, (Long64Mask) m);  // specialize
    }

    // Specialized comparisons

    @Override
    @ForceInline
    public final Long64Mask compare(Comparison op, Vector<Long> v) {
        return super.compareTemplate(Long64Mask.class, op, v);  // specialize
    }

    @Override
    @ForceInline
    public final Long64Mask compare(Comparison op, long s) {
        return super.compareTemplate(Long64Mask.class, op, s);  // specialize
    }


    @Override
    @ForceInline
    public final Long64Mask compare(Comparison op, Vector<Long> v, VectorMask<Long> m) {
        return super.compareTemplate(Long64Mask.class, op, v, (Long64Mask) m);
    }


    @Override
    @ForceInline
    public Long64Vector blend(Vector<Long> v, VectorMask<Long> m) {
        return (Long64Vector)
            super.blendTemplate(Long64Mask.class,
                                (Long64Vector) v,
                                (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector slice(int origin, Vector<Long> v) {
        return (Long64Vector) super.sliceTemplate(origin, v);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector slice(int origin) {
        return (Long64Vector) super.sliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector unslice(int origin, Vector<Long> w, int part) {
        return (Long64Vector) super.unsliceTemplate(origin, w, part);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector unslice(int origin, Vector<Long> w, int part, VectorMask<Long> m) {
        return (Long64Vector)
            super.unsliceTemplate(Long64Mask.class,
                                  origin, w, part,
                                  (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector unslice(int origin) {
        return (Long64Vector) super.unsliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector rearrange(VectorShuffle<Long> s) {
        return (Long64Vector)
            super.rearrangeTemplate(Long64Shuffle.class,
                                    (Long64Shuffle) s);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector rearrange(VectorShuffle<Long> shuffle,
                                  VectorMask<Long> m) {
        return (Long64Vector)
            super.rearrangeTemplate(Long64Shuffle.class,
                                    Long64Mask.class,
                                    (Long64Shuffle) shuffle,
                                    (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector rearrange(VectorShuffle<Long> s,
                                  Vector<Long> v) {
        return (Long64Vector)
            super.rearrangeTemplate(Long64Shuffle.class,
                                    (Long64Shuffle) s,
                                    (Long64Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector compress(VectorMask<Long> m) {
        return (Long64Vector)
            super.compressTemplate(Long64Mask.class,
                                   (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector expand(VectorMask<Long> m) {
        return (Long64Vector)
            super.expandTemplate(Long64Mask.class,
                                   (Long64Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector selectFrom(Vector<Long> v) {
        return (Long64Vector)
            super.selectFromTemplate((Long64Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Long64Vector selectFrom(Vector<Long> v,
                                   VectorMask<Long> m) {
        return (Long64Vector)
            super.selectFromTemplate((Long64Vector) v,
                                     (Long64Mask) m);  // specialize
    }


    @ForceInline
    @Override
    public long lane(int i) {
        switch(i) {
            case 0: return laneHelper(0);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    public long laneHelper(int i) {
        return (long) VectorSupport.extract(
                             VCLASS, ETYPE, VLENGTH,
                             this, i,
                             (vec, ix) -> {
                                 VectorPayloadMF vecpayload = vec.vec();
                                 long start_offset = vecpayload.multiFieldOffset();
                                 return (long)Unsafe.getUnsafe().getLong(vecpayload, start_offset + ix * Long.BYTES);
                             });
    }

    @ForceInline
    @Override
    public Long64Vector withLane(int i, long e) {
        switch (i) {
            case 0: return withLaneHelper(0, e);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    public Long64Vector withLaneHelper(int i, long e) {
       return VectorSupport.insert(
                                VCLASS, ETYPE, VLENGTH,
                                this, i, (long)e,
                                (v, ix, bits) -> {
                                    VectorPayloadMF vec = v.vec();
                                    VectorPayloadMF tpayload = Unsafe.getUnsafe().makePrivateBuffer(vec);
                                    long start_offset = tpayload.multiFieldOffset();
                                    Unsafe.getUnsafe().putLong(tpayload, start_offset + ix * Long.BYTES, (long)bits);
                                    tpayload = Unsafe.getUnsafe().finishPrivateBuffer(tpayload);
                                    return v.vectorFactory(tpayload);
                                });
    }

    // Mask

    static final value class Long64Mask extends AbstractMask<Long> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Long> ETYPE = long.class; // used by the JVM

        private final VectorPayloadMF8Z payload;

        Long64Mask(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF8Z) payload;
        }

        Long64Mask(VectorPayloadMF payload, int offset) {
            this(prepare(payload, offset, VLENGTH));
        }

        Long64Mask(boolean val) {
            this(prepare(val, VLENGTH));
        }

        @ForceInline
        final @Override
        public LongSpecies vspecies() {
            // ISSUE:  This should probably be a @Stable
            // field inside AbstractMask, rather than
            // a megamorphic method.
            return VSPECIES;
        }

        @ForceInline
        @Override
        final VectorPayloadMF getBits() {
            return payload;
        }

        @ForceInline
        @Override
        public final
        Long64Vector toVector() {
            return (Long64Vector) super.toVectorTemplate();  // specialize
        }

        @Override
        @ForceInline
        /*package-private*/
        Long64Mask indexPartiallyInUpperRange(long offset, long limit) {
            return (Long64Mask) VectorSupport.indexPartiallyInUpperRange(
                Long64Mask.class, long.class, VLENGTH, offset, limit,
                (o, l) -> (Long64Mask) TRUE_MASK.indexPartiallyInRange(o, l));
        }

        // Unary operations

        @Override
        @ForceInline
        public Long64Mask not() {
            return xor(maskAll(true));
        }

        @Override
        @ForceInline
        public Long64Mask compress() {
            return (Long64Mask) VectorSupport.compressExpandOp(VectorSupport.VECTOR_OP_MASK_COMPRESS,
                Long64Vector.class, Long64Mask.class, ETYPE, VLENGTH, null, this,
                (v1, m1) -> VSPECIES.iota().compare(VectorOperators.LT, m1.trueCount()));
        }


        // Binary operations

        @Override
        @ForceInline
        public Long64Mask and(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long64Mask m = (Long64Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_AND, Long64Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long64Mask) m1.bOpMF(m2, (i, a, b) -> a & b));
        }

        @Override
        @ForceInline
        public Long64Mask or(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long64Mask m = (Long64Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_OR, Long64Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long64Mask) m1.bOpMF(m2, (i, a, b) -> a | b));
        }

        @Override
        @ForceInline
        public Long64Mask xor(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long64Mask m = (Long64Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_XOR, Long64Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long64Mask) m1.bOpMF(m2, (i, a, b) -> a ^ b));
        }

        // Mask Query operations

        @Override
        @ForceInline
        public int trueCount() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TRUECOUNT, Long64Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long64Mask) m).trueCountHelper());
        }

        @Override
        @ForceInline
        public int firstTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_FIRSTTRUE, Long64Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long64Mask) m).firstTrueHelper());
        }

        @Override
        @ForceInline
        public int lastTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_LASTTRUE, Long64Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long64Mask) m).lastTrueHelper());
        }

        @Override
        @ForceInline
        public long toLong() {
            if (length() > Long.SIZE) {
                throw new UnsupportedOperationException("too many lanes for one long");
            }
            return VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TOLONG, Long64Mask.class, long.class, VLENGTH, this,
                                                      (m) -> ((Long64Mask) m).toLongHelper());
        }

        // Reductions

        @Override
        @ForceInline
        public boolean anyTrue() {
            return VectorSupport.test(BT_ne, Long64Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Long64Mask) m).anyTrueHelper());
        }

        @Override
        @ForceInline
        public boolean allTrue() {
            return VectorSupport.test(BT_overflow, Long64Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Long64Mask) m).allTrueHelper());
        }

        @ForceInline
        /*package-private*/
        static Long64Mask maskAll(boolean bit) {
            return VectorSupport.fromBitsCoerced(Long64Mask.class, long.class, VLENGTH,
                                                 (bit ? -1 : 0), MODE_BROADCAST, null,
                                                 (v, __) -> (v != 0 ? TRUE_MASK : FALSE_MASK));
        }
        private static final Long64Mask  TRUE_MASK = new Long64Mask(true);
        private static final Long64Mask FALSE_MASK = new Long64Mask(false);

    }

    // Shuffle

    static final value class Long64Shuffle extends AbstractShuffle<Long> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Long> ETYPE = long.class; // used by the JVM

        private final VectorPayloadMF64L payload;

        Long64Shuffle(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF64L) payload;
            assert(VLENGTH == payload.length());
            assert(indicesInRange(payload));
        }

        Long64Shuffle(int[] indices, int i) {
            this(prepare(indices, i));
        }

        Long64Shuffle(IntUnaryOperator fn) {
            this(prepare(fn));
        }

        @ForceInline
        @Override
        protected final VectorPayloadMF indices() {
            return payload;
        }

        @Override
        @ForceInline
        public LongSpecies vspecies() {
            return VSPECIES;
        }

        static {
            // There must be enough bits in the shuffle lanes to encode
            // VLENGTH valid indexes and VLENGTH exceptional ones.
            assert(VLENGTH < Long.MAX_VALUE);
            assert(Long.MIN_VALUE <= -VLENGTH);
        }
        static final Long64Shuffle IOTA = new Long64Shuffle(IDENTITY);

        @Override
        @ForceInline
        Long64Vector toBitsVector() {
            return (Long64Vector) super.toBitsVectorTemplate();
        }

        @Override
        @ForceInline
        LongVector toBitsVector0() {
            return Long64Vector.VSPECIES.dummyVectorMF().vectorFactory(indices());
        }

        @Override
        @ForceInline
        public int laneSource(int i) {
            return (int)toBitsVector().lane(i);
        }

        @Override
        @ForceInline
        public void intoArray(int[] a, int offset) {
            switch (length()) {
                case 1 -> a[offset] = laneSource(0);
                case 2 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_64, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 4 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_128, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 8 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_256, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                case 16 -> toBitsVector()
                        .convertShape(VectorOperators.L2I, IntVector.SPECIES_512, 0)
                        .reinterpretAsInts()
                        .intoArray(a, offset);
                default -> {
                    VectorIntrinsics.checkFromIndexSize(offset, length(), a.length);
                    for (int i = 0; i < length(); i++) {
                        a[offset + i] = laneSource(i);
                    }
                }
            }
        }

        private static VectorPayloadMF prepare(int[] indices, int offset) {
            VectorPayloadMF payload = VectorPayloadMF.newInstanceFactory(long.class, VLENGTH);
            payload = Unsafe.getUnsafe().makePrivateBuffer(payload);
            long mfOffset = payload.multiFieldOffset();
            for (int i = 0; i < VLENGTH; i++) {
                int si = indices[offset + i];
                si = partiallyWrapIndex(si, VLENGTH);
                Unsafe.getUnsafe().putLong(payload, mfOffset + i * Long.BYTES, (long) si);
            }
            payload = Unsafe.getUnsafe().finishPrivateBuffer(payload);
            return payload;
        }

        private static VectorPayloadMF prepare(IntUnaryOperator f) {
            VectorPayloadMF payload = VectorPayloadMF.newInstanceFactory(long.class, VLENGTH);
            payload = Unsafe.getUnsafe().makePrivateBuffer(payload);
            long offset = payload.multiFieldOffset();
            for (int i = 0; i < VLENGTH; i++) {
                int si = f.applyAsInt(i);
                si = partiallyWrapIndex(si, VLENGTH);
                Unsafe.getUnsafe().putLong(payload, offset + i * Long.BYTES, (long) si);
            }
            payload = Unsafe.getUnsafe().finishPrivateBuffer(payload);
            return payload;
        }


        private static boolean indicesInRange(VectorPayloadMF indices) {
            int length = indices.length();
            long offset = indices.multiFieldOffset();
            for (int i = 0; i < length; i++) {
                long si = Unsafe.getUnsafe().getLong(indices, offset + i * Long.BYTES);
                if (si >= length || si < -length) {
                    boolean assertsEnabled = false;
                    assert(assertsEnabled = true);
                    if (assertsEnabled) {
                        String msg = ("index "+si+"out of range ["+length+"] in "+
                                indices.toString());
                        throw new AssertionError(msg);
                    }
                    return false;
                }
            }
            return true;
        }
    }

    // ================================================

    // Specialized low-level memory operations.

    @ForceInline
    @Override
    final
    LongVector fromArray0(long[] a, int offset) {
        return super.fromArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    LongVector fromArray0(long[] a, int offset, VectorMask<Long> m, int offsetInRange) {
        return super.fromArray0Template(Long64Mask.class, a, offset, (Long64Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    LongVector fromArray0(long[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Long> m) {
        return super.fromArray0Template(Long64Mask.class, a, offset, indexMap, mapOffset, (Long64Mask) m);
    }



    @ForceInline
    @Override
    final
    LongVector fromMemorySegment0(MemorySegment ms, long offset) {
        return super.fromMemorySegment0Template(ms, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    LongVector fromMemorySegment0(MemorySegment ms, long offset, VectorMask<Long> m, int offsetInRange) {
        return super.fromMemorySegment0Template(Long64Mask.class, ms, offset, (Long64Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(long[] a, int offset) {
        super.intoArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(long[] a, int offset, VectorMask<Long> m) {
        super.intoArray0Template(Long64Mask.class, a, offset, (Long64Mask) m);
    }

    @ForceInline
    @Override
    final
    void intoArray0(long[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Long> m) {
        super.intoArray0Template(Long64Mask.class, a, offset, indexMap, mapOffset, (Long64Mask) m);
    }


    @ForceInline
    @Override
    final
    void intoMemorySegment0(MemorySegment ms, long offset, VectorMask<Long> m) {
        super.intoMemorySegment0Template(Long64Mask.class, ms, offset, (Long64Mask) m);
    }


    // End of specialized low-level memory operations.

    // ================================================

}

