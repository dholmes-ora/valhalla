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
value class Long256Vector extends LongVector {
    static final LongSpecies VSPECIES =
        (LongSpecies) LongVector.SPECIES_256;

    static final VectorShape VSHAPE =
        VSPECIES.vectorShape();

    static final Class<Long256Vector> VCLASS = Long256Vector.class;

    static final int VSIZE = VSPECIES.vectorBitSize();

    static final int VLENGTH = VSPECIES.laneCount(); // used by the JVM

    static final Class<Long> ETYPE = long.class; // used by the JVM

    static final long MFOFFSET = VectorPayloadMF.multiFieldOffset(VectorPayloadMF256L.class);

    private final VectorPayloadMF256L payload;

    Long256Vector(Object value) {
        this.payload = (VectorPayloadMF256L) value;
    }

    @ForceInline
    @Override
    final VectorPayloadMF vec() {
        return payload;
    }

    static final Long256Vector ZERO = new Long256Vector(VectorPayloadMF.newVectorInstanceFactory(long.class, 4, false));
    static final Long256Vector IOTA = new Long256Vector(VectorPayloadMF.createVectPayloadInstanceL(VLENGTH, (long[])(VSPECIES.iotaArray()), false));

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
    public final Long256Vector broadcast(long e) {
        return (Long256Vector) super.broadcastTemplate(e);  // specialize
    }


    @Override
    @ForceInline
    Long256Mask maskFromPayload(VectorPayloadMF payload) {
        return new Long256Mask(payload);
    }

    @Override
    @ForceInline
    Long256Shuffle iotaShuffle() { return Long256Shuffle.IOTA; }

    @ForceInline
    Long256Shuffle iotaShuffle(int start, int step, boolean wrap) {
      if (wrap) {
        return (Long256Shuffle)VectorSupport.shuffleIota(ETYPE, Long256Shuffle.class, VSPECIES, VLENGTH, start, step, 1,
                (l, lstart, lstep, s) -> s.shuffleFromOp(i -> (VectorIntrinsics.wrapToRange(i*lstep + lstart, l))));
      } else {
        return (Long256Shuffle)VectorSupport.shuffleIota(ETYPE, Long256Shuffle.class, VSPECIES, VLENGTH, start, step, 0,
                (l, lstart, lstep, s) -> s.shuffleFromOp(i -> (i*lstep + lstart)));
      }
    }

    @Override
    @ForceInline
    Long256Shuffle shuffleFromBytes(VectorPayloadMF indexes) { return new Long256Shuffle(indexes); }

    @Override
    @ForceInline
    Long256Shuffle shuffleFromArray(int[] indexes, int i) { return new Long256Shuffle(indexes, i); }

    @Override
    @ForceInline
    Long256Shuffle shuffleFromOp(IntUnaryOperator fn) { return new Long256Shuffle(fn); }

    // Make a vector of the same species but the given elements:
    @ForceInline
    final @Override
    Long256Vector vectorFactory(VectorPayloadMF vec) {
        return new Long256Vector(vec);
    }

    @ForceInline
    final @Override
    Byte256Vector asByteVectorRaw() {
        return (Byte256Vector) super.asByteVectorRawTemplate();  // specialize
    }

    @ForceInline
    final @Override
    AbstractVector<?> asVectorRaw(LaneType laneType) {
        return super.asVectorRawTemplate(laneType);  // specialize
    }

    // Unary operator

    @ForceInline
    final @Override
    Long256Vector uOpMF(FUnOp f) {
        return (Long256Vector) super.uOpTemplateMF(f);  // specialize
    }

    @ForceInline
    final @Override
    Long256Vector uOpMF(VectorMask<Long> m, FUnOp f) {
        return (Long256Vector)
            super.uOpTemplateMF((Long256Mask)m, f);  // specialize
    }

    // Binary operator

    @ForceInline
    final @Override
    Long256Vector bOpMF(Vector<Long> v, FBinOp f) {
        return (Long256Vector) super.bOpTemplateMF((Long256Vector)v, f);  // specialize
    }

    @ForceInline
    final @Override
    Long256Vector bOpMF(Vector<Long> v,
                     VectorMask<Long> m, FBinOp f) {
        return (Long256Vector)
            super.bOpTemplateMF((Long256Vector)v, (Long256Mask)m,
                                f);  // specialize
    }

    // Ternary operator

    @ForceInline
    final @Override
    Long256Vector tOpMF(Vector<Long> v1, Vector<Long> v2, FTriOp f) {
        return (Long256Vector)
            super.tOpTemplateMF((Long256Vector)v1, (Long256Vector)v2,
                                f);  // specialize
    }

    @ForceInline
    final @Override
    Long256Vector tOpMF(Vector<Long> v1, Vector<Long> v2,
                     VectorMask<Long> m, FTriOp f) {
        return (Long256Vector)
            super.tOpTemplateMF((Long256Vector)v1, (Long256Vector)v2,
                                (Long256Mask)m, f);  // specialize
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
    public Long256Vector lanewise(Unary op) {
        return (Long256Vector) super.lanewiseTemplate(op);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector lanewise(Unary op, VectorMask<Long> m) {
        return (Long256Vector) super.lanewiseTemplate(op, Long256Mask.class, (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector lanewise(Binary op, Vector<Long> v) {
        return (Long256Vector) super.lanewiseTemplate(op, v);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector lanewise(Binary op, Vector<Long> v, VectorMask<Long> m) {
        return (Long256Vector) super.lanewiseTemplate(op, Long256Mask.class, v, (Long256Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Long256Vector
    lanewiseShift(VectorOperators.Binary op, int e) {
        return (Long256Vector) super.lanewiseShiftTemplate(op, e);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline Long256Vector
    lanewiseShift(VectorOperators.Binary op, int e, VectorMask<Long> m) {
        return (Long256Vector) super.lanewiseShiftTemplate(op, Long256Mask.class, e, (Long256Mask) m);  // specialize
    }

    /*package-private*/
    @Override
    @ForceInline
    public final
    Long256Vector
    lanewise(Ternary op, Vector<Long> v1, Vector<Long> v2) {
        return (Long256Vector) super.lanewiseTemplate(op, v1, v2);  // specialize
    }

    @Override
    @ForceInline
    public final
    Long256Vector
    lanewise(Ternary op, Vector<Long> v1, Vector<Long> v2, VectorMask<Long> m) {
        return (Long256Vector) super.lanewiseTemplate(op, Long256Mask.class, v1, v2, (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public final
    Long256Vector addIndex(int scale) {
        return (Long256Vector) super.addIndexTemplate(scale);  // specialize
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
        return super.reduceLanesTemplate(op, Long256Mask.class, (Long256Mask) m);  // specialized
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
        return (long) super.reduceLanesTemplate(op, Long256Mask.class, (Long256Mask) m);  // specialized
    }

    @ForceInline
    public VectorShuffle<Long> toShuffle() {
        return super.toShuffleTemplate(Long256Shuffle.class); // specialize
    }

    // Specialized unary testing

    @Override
    @ForceInline
    public final Long256Mask test(Test op) {
        return super.testTemplate(Long256Mask.class, op);  // specialize
    }

    @Override
    @ForceInline
    public final Long256Mask test(Test op, VectorMask<Long> m) {
        return super.testTemplate(Long256Mask.class, op, (Long256Mask) m);  // specialize
    }

    // Specialized comparisons

    @Override
    @ForceInline
    public final Long256Mask compare(Comparison op, Vector<Long> v) {
        return super.compareTemplate(Long256Mask.class, op, v);  // specialize
    }

    @Override
    @ForceInline
    public final Long256Mask compare(Comparison op, long s) {
        return super.compareTemplate(Long256Mask.class, op, s);  // specialize
    }


    @Override
    @ForceInline
    public final Long256Mask compare(Comparison op, Vector<Long> v, VectorMask<Long> m) {
        return super.compareTemplate(Long256Mask.class, op, v, (Long256Mask) m);
    }


    @Override
    @ForceInline
    public Long256Vector blend(Vector<Long> v, VectorMask<Long> m) {
        return (Long256Vector)
            super.blendTemplate(Long256Mask.class,
                                (Long256Vector) v,
                                (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector slice(int origin, Vector<Long> v) {
        return (Long256Vector) super.sliceTemplate(origin, v);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector slice(int origin) {
        return (Long256Vector) super.sliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector unslice(int origin, Vector<Long> w, int part) {
        return (Long256Vector) super.unsliceTemplate(origin, w, part);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector unslice(int origin, Vector<Long> w, int part, VectorMask<Long> m) {
        return (Long256Vector)
            super.unsliceTemplate(Long256Mask.class,
                                  origin, w, part,
                                  (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector unslice(int origin) {
        return (Long256Vector) super.unsliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector rearrange(VectorShuffle<Long> s) {
        return (Long256Vector)
            super.rearrangeTemplate(Long256Shuffle.class,
                                    (Long256Shuffle) s);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector rearrange(VectorShuffle<Long> shuffle,
                                  VectorMask<Long> m) {
        return (Long256Vector)
            super.rearrangeTemplate(Long256Shuffle.class,
                                    Long256Mask.class,
                                    (Long256Shuffle) shuffle,
                                    (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector rearrange(VectorShuffle<Long> s,
                                  Vector<Long> v) {
        return (Long256Vector)
            super.rearrangeTemplate(Long256Shuffle.class,
                                    (Long256Shuffle) s,
                                    (Long256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector compress(VectorMask<Long> m) {
        return (Long256Vector)
            super.compressTemplate(Long256Mask.class,
                                   (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector expand(VectorMask<Long> m) {
        return (Long256Vector)
            super.expandTemplate(Long256Mask.class,
                                   (Long256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector selectFrom(Vector<Long> v) {
        return (Long256Vector)
            super.selectFromTemplate((Long256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Long256Vector selectFrom(Vector<Long> v,
                                   VectorMask<Long> m) {
        return (Long256Vector)
            super.selectFromTemplate((Long256Vector) v,
                                     (Long256Mask) m);  // specialize
    }


    @ForceInline
    @Override
    public long lane(int i) {
        switch(i) {
            case 0: return laneHelper(0);
            case 1: return laneHelper(1);
            case 2: return laneHelper(2);
            case 3: return laneHelper(3);
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
                                 return (long)U.getLong(vecpayload, start_offset + ix * Long.BYTES);
                             });
    }

    @ForceInline
    @Override
    public Long256Vector withLane(int i, long e) {
        switch (i) {
            case 0: return withLaneHelper(0, e);
            case 1: return withLaneHelper(1, e);
            case 2: return withLaneHelper(2, e);
            case 3: return withLaneHelper(3, e);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    public Long256Vector withLaneHelper(int i, long e) {
       return VectorSupport.insert(
                                VCLASS, ETYPE, VLENGTH,
                                this, i, (long)e,
                                (v, ix, bits) -> {
                                    VectorPayloadMF vec = v.vec();
                                    VectorPayloadMF tpayload = U.makePrivateBuffer(vec);
                                    long start_offset = tpayload.multiFieldOffset();
                                    U.putLong(tpayload, start_offset + ix * Long.BYTES, (long)bits);
                                    tpayload = U.finishPrivateBuffer(tpayload);
                                    return v.vectorFactory(tpayload);
                                });
    }

    // Mask

    static final value class Long256Mask extends AbstractMask<Long> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Long> ETYPE = long.class; // used by the JVM

        Long256Mask(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF32Z) payload;
        }

        private final VectorPayloadMF32Z payload;

        Long256Mask(VectorPayloadMF payload, int offset) {
            this(prepare(payload, offset, VSPECIES));
        }

        Long256Mask(boolean val) {
            this(prepare(val, VSPECIES));
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
        Long256Vector toVector() {
            return (Long256Vector) super.toVectorTemplate();  // specialize
        }

        @Override
        @ForceInline
        /*package-private*/
        Long256Mask indexPartiallyInUpperRange(long offset, long limit) {
            return (Long256Mask) VectorSupport.indexPartiallyInUpperRange(
                Long256Mask.class, long.class, VLENGTH, offset, limit,
                (o, l) -> (Long256Mask) TRUE_MASK.indexPartiallyInRange(o, l));
        }

        // Unary operations

        @Override
        @ForceInline
        public Long256Mask not() {
            return xor(maskAll(true));
        }

        @Override
        @ForceInline
        public Long256Mask compress() {
            return (Long256Mask) VectorSupport.compressExpandOp(VectorSupport.VECTOR_OP_MASK_COMPRESS,
                Long256Vector.class, Long256Mask.class, ETYPE, VLENGTH, null, this,
                (v1, m1) -> VSPECIES.iota().compare(VectorOperators.LT, m1.trueCount()));
        }


        // Binary operations

        @Override
        @ForceInline
        public Long256Mask and(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long256Mask m = (Long256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_AND, Long256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long256Mask) m1.bOpMF(m2, (i, a, b) -> a & b));
        }

        @Override
        @ForceInline
        public Long256Mask or(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long256Mask m = (Long256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_OR, Long256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long256Mask) m1.bOpMF(m2, (i, a, b) -> a | b));
        }

        @Override
        @ForceInline
        public Long256Mask xor(VectorMask<Long> mask) {
            Objects.requireNonNull(mask);
            Long256Mask m = (Long256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_XOR, Long256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Long256Mask) m1.bOpMF(m2, (i, a, b) -> a ^ b));
        }

        // Mask Query operations

        @Override
        @ForceInline
        public int trueCount() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TRUECOUNT, Long256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long256Mask) m).trueCountHelper());
        }

        @Override
        @ForceInline
        public int firstTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_FIRSTTRUE, Long256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long256Mask) m).firstTrueHelper());
        }

        @Override
        @ForceInline
        public int lastTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_LASTTRUE, Long256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Long256Mask) m).lastTrueHelper());
        }

        @Override
        @ForceInline
        public long toLong() {
            if (length() > Long.SIZE) {
                throw new UnsupportedOperationException("too many lanes for one long");
            }
            return VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TOLONG, Long256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> ((Long256Mask) m).toLongHelper());
        }

        // laneIsSet

        @Override
        @ForceInline
        public boolean laneIsSet(int i) {
            Objects.checkIndex(i, length());
            return VectorSupport.extract(Long256Mask.class, long.class, VLENGTH,
                                         this, i, (m, idx) -> (((Long256Mask) m).laneIsSetHelper(idx) ? 1L : 0L)) == 1L;
        }

        // Reductions

        @Override
        @ForceInline
        public boolean anyTrue() {
            return VectorSupport.test(BT_ne, Long256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Long256Mask) m).anyTrueHelper());
        }

        @Override
        @ForceInline
        public boolean allTrue() {
            return VectorSupport.test(BT_overflow, Long256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Long256Mask) m).allTrueHelper());
        }

        @ForceInline
        /*package-private*/
        static Long256Mask maskAll(boolean bit) {
            return VectorSupport.fromBitsCoerced(Long256Mask.class, long.class, VLENGTH,
                                                 (bit ? -1 : 0), MODE_BROADCAST, null,
                                                 (v, __) -> (v != 0 ? TRUE_MASK : FALSE_MASK));
        }
        private static final Long256Mask  TRUE_MASK = new Long256Mask(true);
        private static final Long256Mask FALSE_MASK = new Long256Mask(false);

    }

    // Shuffle

    static final value class Long256Shuffle extends AbstractShuffle<Long> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Long> ETYPE = long.class; // used by the JVM

        private final VectorPayloadMF32B payload;

        Long256Shuffle(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF32B) payload;
            assert(VLENGTH == payload.length());
            assert(indexesInRange(payload));
        }

        public Long256Shuffle(int[] indexes, int i) {
            this(prepare(indexes, i, VSPECIES));
        }

        public Long256Shuffle(IntUnaryOperator fn) {
            this(prepare(fn, VSPECIES));
        }

        public Long256Shuffle(int[] indexes) {
            this(indexes, 0);
        }


        @ForceInline
        @Override
        protected final VectorPayloadMF indices() {
            return payload;
        }

        @Override
        public LongSpecies vspecies() {
            return VSPECIES;
        }

        static {
            // There must be enough bits in the shuffle lanes to encode
            // VLENGTH valid indexes and VLENGTH exceptional ones.
            assert(VLENGTH < Byte.MAX_VALUE);
            assert(Byte.MIN_VALUE <= -VLENGTH);
        }
        static final Long256Shuffle IOTA = new Long256Shuffle(IDENTITY);

        @Override
        @ForceInline
        public Long256Vector toVector() {
            return VectorSupport.shuffleToVector(VCLASS, ETYPE, Long256Shuffle.class, this, VLENGTH,
                                                    (s) -> ((Long256Vector)(((AbstractShuffle<Long>)(s)).toVectorTemplate())));
        }

        @Override
        @ForceInline
        public <F> VectorShuffle<F> cast(VectorSpecies<F> s) {
            AbstractSpecies<F> species = (AbstractSpecies<F>) s;
            if (length() != species.laneCount())
                throw new IllegalArgumentException("VectorShuffle length and species length differ");
            int[] shuffleArray = toArray();
            return s.shuffleFromArray(shuffleArray, 0).check(s);
        }

        @ForceInline
        @Override
        public Long256Shuffle rearrange(VectorShuffle<Long> shuffle) {
            Long256Shuffle s = (Long256Shuffle) shuffle;
            VectorPayloadMF indices1 = indices();
            VectorPayloadMF indices2 = s.indices();
            VectorPayloadMF r = VectorPayloadMF.newShuffleInstanceFactory(ETYPE, VLENGTH, false);
            r = U.makePrivateBuffer(r);
            long offset = r.multiFieldOffset();
            for (int i = 0; i < VLENGTH; i++) {
                int ssi = U.getByte(indices2, offset + i * Byte.BYTES);
                int si = U.getByte(indices1, offset + ssi * Byte.BYTES);
                U.putByte(r, offset + i * Byte.BYTES, (byte) si);
            }
            r = U.finishPrivateBuffer(r);
            return new Long256Shuffle(r);
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
        return super.fromArray0Template(Long256Mask.class, a, offset, (Long256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    LongVector fromArray0(long[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Long> m) {
        return super.fromArray0Template(Long256Mask.class, a, offset, indexMap, mapOffset, (Long256Mask) m);
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
        return super.fromMemorySegment0Template(Long256Mask.class, ms, offset, (Long256Mask) m, offsetInRange);  // specialize
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
        super.intoArray0Template(Long256Mask.class, a, offset, (Long256Mask) m);
    }

    @ForceInline
    @Override
    final
    void intoArray0(long[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Long> m) {
        super.intoArray0Template(Long256Mask.class, a, offset, indexMap, mapOffset, (Long256Mask) m);
    }


    @ForceInline
    @Override
    final
    void intoMemorySegment0(MemorySegment ms, long offset, VectorMask<Long> m) {
        super.intoMemorySegment0Template(Long256Mask.class, ms, offset, (Long256Mask) m);
    }


    // End of specialized low-level memory operations.

    // ================================================

}

