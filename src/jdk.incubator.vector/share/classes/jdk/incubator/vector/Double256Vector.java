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
value class Double256Vector extends DoubleVector {
    static final DoubleSpecies VSPECIES =
        (DoubleSpecies) DoubleVector.SPECIES_256;

    static final VectorShape VSHAPE =
        VSPECIES.vectorShape();

    static final Class<Double256Vector> VCLASS = Double256Vector.class;

    static final int VSIZE = VSPECIES.vectorBitSize();

    static final int VLENGTH = VSPECIES.laneCount(); // used by the JVM

    static final Class<Double> ETYPE = double.class; // used by the JVM

    static final long MFOFFSET = VectorPayloadMF.multiFieldOffset(VectorPayloadMF256D.class);

    private final VectorPayloadMF256D payload;

    Double256Vector(Object value) {
        this.payload = (VectorPayloadMF256D) value;
    }

    @ForceInline
    @Override
    final VectorPayloadMF vec() {
        return payload;
    }

    static final Double256Vector ZERO = new Double256Vector(VectorPayloadMF.newVectorInstanceFactory(double.class, 4, false));
    static final Double256Vector IOTA = new Double256Vector(VectorPayloadMF.createVectPayloadInstanceD(VLENGTH, (double[])(VSPECIES.iotaArray()), false));

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
    public DoubleSpecies vspecies() {
        // ISSUE:  This should probably be a @Stable
        // field inside AbstractVector, rather than
        // a megamorphic method.
        return VSPECIES;
    }

    @ForceInline
    @Override
    public final Class<Double> elementType() { return double.class; }

    @ForceInline
    @Override
    public final int elementSize() { return Double.SIZE; }

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
    public final Double256Vector broadcast(double e) {
        return (Double256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Vector broadcast(long e) {
        return (Double256Vector) super.broadcastTemplate(e);  // specialize
    }

    @Override
    @ForceInline
    Double256Mask maskFromPayload(VectorPayloadMF payload) {
        return new Double256Mask(payload);
    }

    @Override
    @ForceInline
    Double256Shuffle iotaShuffle() { return Double256Shuffle.IOTA; }

    @ForceInline
    Double256Shuffle iotaShuffle(int start, int step, boolean wrap) {
      if (wrap) {
        return (Double256Shuffle)VectorSupport.shuffleIota(ETYPE, Double256Shuffle.class, VSPECIES, VLENGTH, start, step, 1,
                (l, lstart, lstep, s) -> s.shuffleFromOp(i -> (VectorIntrinsics.wrapToRange(i*lstep + lstart, l))));
      } else {
        return (Double256Shuffle)VectorSupport.shuffleIota(ETYPE, Double256Shuffle.class, VSPECIES, VLENGTH, start, step, 0,
                (l, lstart, lstep, s) -> s.shuffleFromOp(i -> (i*lstep + lstart)));
      }
    }

    @Override
    @ForceInline
    Double256Shuffle shuffleFromBytes(VectorPayloadMF indexes) { return new Double256Shuffle(indexes); }

    @Override
    @ForceInline
    Double256Shuffle shuffleFromArray(int[] indexes, int i) { return new Double256Shuffle(indexes, i); }

    @Override
    @ForceInline
    Double256Shuffle shuffleFromOp(IntUnaryOperator fn) { return new Double256Shuffle(fn); }

    // Make a vector of the same species but the given elements:
    @ForceInline
    final @Override
    Double256Vector vectorFactory(VectorPayloadMF vec) {
        return new Double256Vector(vec);
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
    Double256Vector uOpMF(FUnOp f) {
        return (Double256Vector) super.uOpTemplateMF(f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector uOpMF(VectorMask<Double> m, FUnOp f) {
        return (Double256Vector)
            super.uOpTemplateMF((Double256Mask)m, f);  // specialize
    }

    // Binary operator

    @ForceInline
    final @Override
    Double256Vector bOpMF(Vector<Double> v, FBinOp f) {
        return (Double256Vector) super.bOpTemplateMF((Double256Vector)v, f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector bOpMF(Vector<Double> v,
                     VectorMask<Double> m, FBinOp f) {
        return (Double256Vector)
            super.bOpTemplateMF((Double256Vector)v, (Double256Mask)m,
                                f);  // specialize
    }

    // Ternary operator

    @ForceInline
    final @Override
    Double256Vector tOpMF(Vector<Double> v1, Vector<Double> v2, FTriOp f) {
        return (Double256Vector)
            super.tOpTemplateMF((Double256Vector)v1, (Double256Vector)v2,
                                f);  // specialize
    }

    @ForceInline
    final @Override
    Double256Vector tOpMF(Vector<Double> v1, Vector<Double> v2,
                     VectorMask<Double> m, FTriOp f) {
        return (Double256Vector)
            super.tOpTemplateMF((Double256Vector)v1, (Double256Vector)v2,
                                (Double256Mask)m, f);  // specialize
    }

    @ForceInline
    final @Override
    double rOpMF(double v, VectorMask<Double> m, FBinOp f) {
        return super.rOpTemplateMF(v, m, f);  // specialize
    }

    @Override
    @ForceInline
    public final <F>
    Vector<F> convertShape(VectorOperators.Conversion<Double,F> conv,
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
    public Double256Vector lanewise(Unary op) {
        return (Double256Vector) super.lanewiseTemplate(op);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Unary op, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Binary op, Vector<Double> v) {
        return (Double256Vector) super.lanewiseTemplate(op, v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector lanewise(Binary op, Vector<Double> v, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, v, (Double256Mask) m);  // specialize
    }


    /*package-private*/
    @Override
    @ForceInline
    public final
    Double256Vector
    lanewise(Ternary op, Vector<Double> v1, Vector<Double> v2) {
        return (Double256Vector) super.lanewiseTemplate(op, v1, v2);  // specialize
    }

    @Override
    @ForceInline
    public final
    Double256Vector
    lanewise(Ternary op, Vector<Double> v1, Vector<Double> v2, VectorMask<Double> m) {
        return (Double256Vector) super.lanewiseTemplate(op, Double256Mask.class, v1, v2, (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public final
    Double256Vector addIndex(int scale) {
        return (Double256Vector) super.addIndexTemplate(scale);  // specialize
    }

    // Type specific horizontal reductions

    @Override
    @ForceInline
    public final double reduceLanes(VectorOperators.Associative op) {
        return super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final double reduceLanes(VectorOperators.Associative op,
                                    VectorMask<Double> m) {
        return super.reduceLanesTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op) {
        return (long) super.reduceLanesTemplate(op);  // specialized
    }

    @Override
    @ForceInline
    public final long reduceLanesToLong(VectorOperators.Associative op,
                                        VectorMask<Double> m) {
        return (long) super.reduceLanesTemplate(op, Double256Mask.class, (Double256Mask) m);  // specialized
    }

    @ForceInline
    public VectorShuffle<Double> toShuffle() {
        return super.toShuffleTemplate(Double256Shuffle.class); // specialize
    }

    // Specialized unary testing

    @Override
    @ForceInline
    public final Double256Mask test(Test op) {
        return super.testTemplate(Double256Mask.class, op);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask test(Test op, VectorMask<Double> m) {
        return super.testTemplate(Double256Mask.class, op, (Double256Mask) m);  // specialize
    }

    // Specialized comparisons

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, Vector<Double> v) {
        return super.compareTemplate(Double256Mask.class, op, v);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, double s) {
        return super.compareTemplate(Double256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, long s) {
        return super.compareTemplate(Double256Mask.class, op, s);  // specialize
    }

    @Override
    @ForceInline
    public final Double256Mask compare(Comparison op, Vector<Double> v, VectorMask<Double> m) {
        return super.compareTemplate(Double256Mask.class, op, v, (Double256Mask) m);
    }


    @Override
    @ForceInline
    public Double256Vector blend(Vector<Double> v, VectorMask<Double> m) {
        return (Double256Vector)
            super.blendTemplate(Double256Mask.class,
                                (Double256Vector) v,
                                (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector slice(int origin, Vector<Double> v) {
        return (Double256Vector) super.sliceTemplate(origin, v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector slice(int origin) {
        return (Double256Vector) super.sliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin, Vector<Double> w, int part) {
        return (Double256Vector) super.unsliceTemplate(origin, w, part);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin, Vector<Double> w, int part, VectorMask<Double> m) {
        return (Double256Vector)
            super.unsliceTemplate(Double256Mask.class,
                                  origin, w, part,
                                  (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector unslice(int origin) {
        return (Double256Vector) super.unsliceTemplate(origin);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> s) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    (Double256Shuffle) s);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> shuffle,
                                  VectorMask<Double> m) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    Double256Mask.class,
                                    (Double256Shuffle) shuffle,
                                    (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector rearrange(VectorShuffle<Double> s,
                                  Vector<Double> v) {
        return (Double256Vector)
            super.rearrangeTemplate(Double256Shuffle.class,
                                    (Double256Shuffle) s,
                                    (Double256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector compress(VectorMask<Double> m) {
        return (Double256Vector)
            super.compressTemplate(Double256Mask.class,
                                   (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector expand(VectorMask<Double> m) {
        return (Double256Vector)
            super.expandTemplate(Double256Mask.class,
                                   (Double256Mask) m);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector selectFrom(Vector<Double> v) {
        return (Double256Vector)
            super.selectFromTemplate((Double256Vector) v);  // specialize
    }

    @Override
    @ForceInline
    public Double256Vector selectFrom(Vector<Double> v,
                                   VectorMask<Double> m) {
        return (Double256Vector)
            super.selectFromTemplate((Double256Vector) v,
                                     (Double256Mask) m);  // specialize
    }


    @ForceInline
    @Override
    public double lane(int i) {
        long bits;
        switch(i) {
            case 0: bits = laneHelper(0); break;
            case 1: bits = laneHelper(1); break;
            case 2: bits = laneHelper(2); break;
            case 3: bits = laneHelper(3); break;
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
        return Double.longBitsToDouble(bits);
    }

    public long laneHelper(int i) {
        return (long) VectorSupport.extract(
                     VCLASS, ETYPE, VLENGTH,
                     this, i,
                     (vec, ix) -> {
                         VectorPayloadMF vecpayload = vec.vec();
                         long start_offset = vecpayload.multiFieldOffset();
                         return (long)Double.doubleToLongBits(U.getDouble(vecpayload, start_offset + ix * Double.BYTES));
                     });
    }

    @ForceInline
    @Override
    public Double256Vector withLane(int i, double e) {
        switch(i) {
            case 0: return withLaneHelper(0, e);
            case 1: return withLaneHelper(1, e);
            case 2: return withLaneHelper(2, e);
            case 3: return withLaneHelper(3, e);
            default: throw new IllegalArgumentException("Index " + i + " must be zero or positive, and less than " + VLENGTH);
        }
    }

    public Double256Vector withLaneHelper(int i, double e) {
        return VectorSupport.insert(
                                VCLASS, ETYPE, VLENGTH,
                                this, i, (long)Double.doubleToLongBits(e),
                                (v, ix, bits) -> {
                                    VectorPayloadMF vec = v.vec();
                                    VectorPayloadMF tpayload = U.makePrivateBuffer(vec);
                                    long start_offset = tpayload.multiFieldOffset();
                                    U.putDouble(tpayload, start_offset + ix * Double.BYTES, Double.longBitsToDouble((long)bits));
                                    tpayload = U.finishPrivateBuffer(tpayload);
                                    return v.vectorFactory(tpayload);
                                });
    }

    // Mask

    static final value class Double256Mask extends AbstractMask<Double> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Double> ETYPE = double.class; // used by the JVM

        Double256Mask(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF32Z) payload;
        }

        private final VectorPayloadMF32Z payload;

        Double256Mask(VectorPayloadMF payload, int offset) {
            this(prepare(payload, offset, VSPECIES));
        }

        Double256Mask(boolean val) {
            this(prepare(val, VSPECIES));
        }


        @ForceInline
        final @Override
        public DoubleSpecies vspecies() {
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
        Double256Vector toVector() {
            return (Double256Vector) super.toVectorTemplate();  // specialize
        }

        @Override
        @ForceInline
        /*package-private*/
        Double256Mask indexPartiallyInUpperRange(long offset, long limit) {
            return (Double256Mask) VectorSupport.indexPartiallyInUpperRange(
                Double256Mask.class, double.class, VLENGTH, offset, limit,
                (o, l) -> (Double256Mask) TRUE_MASK.indexPartiallyInRange(o, l));
        }

        // Unary operations

        @Override
        @ForceInline
        public Double256Mask not() {
            return xor(maskAll(true));
        }

        @Override
        @ForceInline
        public Double256Mask compress() {
            return (Double256Mask) VectorSupport.compressExpandOp(VectorSupport.VECTOR_OP_MASK_COMPRESS,
                Double256Vector.class, Double256Mask.class, ETYPE, VLENGTH, null, this,
                (v1, m1) -> VSPECIES.iota().compare(VectorOperators.LT, m1.trueCount()));
        }


        // Binary operations

        @Override
        @ForceInline
        public Double256Mask and(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_AND, Double256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Double256Mask) m1.bOpMF(m2, (i, a, b) -> a & b));
        }

        @Override
        @ForceInline
        public Double256Mask or(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_OR, Double256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Double256Mask) m1.bOpMF(m2, (i, a, b) -> a | b));
        }

        @Override
        @ForceInline
        public Double256Mask xor(VectorMask<Double> mask) {
            Objects.requireNonNull(mask);
            Double256Mask m = (Double256Mask)mask;
            return VectorSupport.binaryOp(VECTOR_OP_XOR, Double256Mask.class, null,
                                          long.class, VLENGTH, this, m, null,
                                          (m1, m2, vm) -> (Double256Mask) m1.bOpMF(m2, (i, a, b) -> a ^ b));
        }

        // Mask Query operations

        @Override
        @ForceInline
        public int trueCount() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TRUECOUNT, Double256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Double256Mask) m).trueCountHelper());
        }

        @Override
        @ForceInline
        public int firstTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_FIRSTTRUE, Double256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Double256Mask) m).firstTrueHelper());
        }

        @Override
        @ForceInline
        public int lastTrue() {
            return (int) VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_LASTTRUE, Double256Mask.class, long.class, VLENGTH, this,
                                                            (m) -> ((Double256Mask) m).lastTrueHelper());
        }

        @Override
        @ForceInline
        public long toLong() {
            if (length() > Long.SIZE) {
                throw new UnsupportedOperationException("too many lanes for one long");
            }
            return VectorSupport.maskReductionCoerced(VECTOR_OP_MASK_TOLONG, Double256Mask.class, long.class, VLENGTH, this,
                                                      (m) -> ((Double256Mask) m).toLongHelper());
        }

        // laneIsSet

        @Override
        @ForceInline
        public boolean laneIsSet(int i) {
            Objects.checkIndex(i, length());
            return VectorSupport.extract(Double256Mask.class, double.class, VLENGTH,
                                         this, i, (m, idx) -> (((Double256Mask) m).laneIsSetHelper(idx) ? 1L : 0L)) == 1L;
        }

        // Reductions

        @Override
        @ForceInline
        public boolean anyTrue() {
            return VectorSupport.test(BT_ne, Double256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Double256Mask) m).anyTrueHelper());
        }

        @Override
        @ForceInline
        public boolean allTrue() {
            return VectorSupport.test(BT_overflow, Double256Mask.class, long.class, VLENGTH,
                                         this, vspecies().maskAll(true),
                                         (m, __) -> ((Double256Mask) m).allTrueHelper());
        }

        @ForceInline
        /*package-private*/
        static Double256Mask maskAll(boolean bit) {
            return VectorSupport.fromBitsCoerced(Double256Mask.class, long.class, VLENGTH,
                                                 (bit ? -1 : 0), MODE_BROADCAST, null,
                                                 (v, __) -> (v != 0 ? TRUE_MASK : FALSE_MASK));
        }
        private static final Double256Mask  TRUE_MASK = new Double256Mask(true);
        private static final Double256Mask FALSE_MASK = new Double256Mask(false);

    }

    // Shuffle

    static final value class Double256Shuffle extends AbstractShuffle<Double> {
        static final int VLENGTH = VSPECIES.laneCount();    // used by the JVM
        static final Class<Double> ETYPE = double.class; // used by the JVM

        private final VectorPayloadMF32B payload;

        Double256Shuffle(VectorPayloadMF payload) {
            this.payload = (VectorPayloadMF32B) payload;
            assert(VLENGTH == payload.length());
            assert(indexesInRange(payload));
        }

        public Double256Shuffle(int[] indexes, int i) {
            this(prepare(indexes, i, VSPECIES));
        }

        public Double256Shuffle(IntUnaryOperator fn) {
            this(prepare(fn, VSPECIES));
        }

        public Double256Shuffle(int[] indexes) {
            this(indexes, 0);
        }


        @ForceInline
        @Override
        protected final VectorPayloadMF indices() {
            return payload;
        }

        @Override
        public DoubleSpecies vspecies() {
            return VSPECIES;
        }

        static {
            // There must be enough bits in the shuffle lanes to encode
            // VLENGTH valid indexes and VLENGTH exceptional ones.
            assert(VLENGTH < Byte.MAX_VALUE);
            assert(Byte.MIN_VALUE <= -VLENGTH);
        }
        static final Double256Shuffle IOTA = new Double256Shuffle(IDENTITY);

        @Override
        @ForceInline
        public Double256Vector toVector() {
            return VectorSupport.shuffleToVector(VCLASS, ETYPE, Double256Shuffle.class, this, VLENGTH,
                                                    (s) -> ((Double256Vector)(((AbstractShuffle<Double>)(s)).toVectorTemplate())));
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
        public Double256Shuffle rearrange(VectorShuffle<Double> shuffle) {
            Double256Shuffle s = (Double256Shuffle) shuffle;
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
            return new Double256Shuffle(r);
        }
    }

    // ================================================

    // Specialized low-level memory operations.

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset) {
        return super.fromArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset, VectorMask<Double> m, int offsetInRange) {
        return super.fromArray0Template(Double256Mask.class, a, offset, (Double256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromArray0(double[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Double> m) {
        return super.fromArray0Template(Double256Mask.class, a, offset, indexMap, mapOffset, (Double256Mask) m);
    }



    @ForceInline
    @Override
    final
    DoubleVector fromMemorySegment0(MemorySegment ms, long offset) {
        return super.fromMemorySegment0Template(ms, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    DoubleVector fromMemorySegment0(MemorySegment ms, long offset, VectorMask<Double> m, int offsetInRange) {
        return super.fromMemorySegment0Template(Double256Mask.class, ms, offset, (Double256Mask) m, offsetInRange);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset) {
        super.intoArray0Template(a, offset);  // specialize
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset, VectorMask<Double> m) {
        super.intoArray0Template(Double256Mask.class, a, offset, (Double256Mask) m);
    }

    @ForceInline
    @Override
    final
    void intoArray0(double[] a, int offset, int[] indexMap, int mapOffset, VectorMask<Double> m) {
        super.intoArray0Template(Double256Mask.class, a, offset, indexMap, mapOffset, (Double256Mask) m);
    }


    @ForceInline
    @Override
    final
    void intoMemorySegment0(MemorySegment ms, long offset, VectorMask<Double> m) {
        super.intoMemorySegment0Template(Double256Mask.class, ms, offset, (Double256Mask) m);
    }


    // End of specialized low-level memory operations.

    // ================================================

}

