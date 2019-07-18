/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.compat.java8

import java.util.stream._

import scala.annotation.{implicitNotFound, unused}
import scala.collection.Stepper.EfficientSplit
import scala.collection.convert.StreamExtensions.{AccumulatorFactoryInfo, StreamShape, StreamUnboxer}
import scala.collection.{IterableOnce, Stepper, StepperShape}
import scala.compat.java8.converterImpl._
import scala.jdk.CollectionConverters._
import scala.jdk._
import scala.language.{higherKinds, implicitConversions}

/** Defines extension methods to create Java Streams for Scala collections, available through
  * [[scala.compat.java8.StreamConverters]].
  */
trait StreamExtensions {
  implicit def richStepper[A](s: Stepper[A]): StepperExtensions[A] = new StepperExtensions[A](s)

  // collections

  implicit class IterableHasSeqStream[A](cc: IterableOnce[A]) {
    /** Create a sequential [[java.util.stream.Stream Java Stream]] for this collection. If the
      * collection contains primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def seqStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[A, S, St], st: StepperShape[A, St]): S =
      s.fromStepper(cc.stepper, par = false)
  }

  // Not `CC[X] <: IterableOnce[X]`, but `C` with an extra constraint, to support non-parametric classes like IntAccumulator
  implicit class IterableNonGenericHasParStream[A, C <: IterableOnce[_]](c: C)(implicit ev: C <:< IterableOnce[A]) {
    private type IterableOnceWithEfficientStepper = IterableOnce[A] {
      def stepper[S <: Stepper[_]](implicit shape : StepperShape[A, S]) : S with EfficientSplit
    }

    /** Create a parallel [[java.util.stream.Stream Java Stream]] for this collection. If the
      * collection contains primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def parStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit
        s: StreamShape[A, S, St],
        st: StepperShape[A, St],
        @implicitNotFound("`parStream` can only be called on collections where `stepper` returns a `Stepper with EfficientSplit`")
        isEfficient: C <:< IterableOnceWithEfficientStepper): S =
      s.fromStepper(ev(c).stepper, par = true)
  }

  // maps

  implicit class MapHasSeqKeyValueStream[K, V, CC[X, Y] <: collection.MapOps[X, Y, collection.Map, _]](cc: CC[K, V]) {
    /** Create a sequential [[java.util.stream.Stream Java Stream]] for the keys of this map. If
      * the keys are primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def seqKeyStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[K, S, St], st: StepperShape[K, St]): S =
      s.fromStepper(cc.keyStepper, par = false)

    /** Create a sequential [[java.util.stream.Stream Java Stream]] for the values of this map. If
      * the values are primitives, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def seqValueStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[V, S, St], st: StepperShape[V, St]): S =
      s.fromStepper(cc.valueStepper, par = false)

    // The seqStream extension method for IterableOnce doesn't apply because its `CC` takes a single type parameter, whereas the one here takes two
    /** Create a sequential [[java.util.stream.Stream Java Stream]] for the `(key, value)` pairs of
      * this map.
      */
    def seqStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[(K, V), S, St], st: StepperShape[(K, V), St]): S =
      s.fromStepper(cc.stepper, par = false)
  }


  implicit class MapHasParKeyValueStream[K, V, CC[X, Y] <: collection.MapOps[X, Y, collection.Map, _]](cc: CC[K, V]) {
    private type MapOpsWithEfficientKeyStepper = collection.MapOps[K, V, collection.Map, _] { def keyStepper[S <: Stepper[_]](implicit shape : StepperShape[K, S]) : S with EfficientSplit }
    private type MapOpsWithEfficientValueStepper = collection.MapOps[K, V, collection.Map, _] { def valueStepper[V1 >: V, S <: Stepper[_]](implicit shape : StepperShape[V1, S]) : S with EfficientSplit }
    private type MapOpsWithEfficientStepper = collection.MapOps[K, V, collection.Map, _] { def stepper[S <: Stepper[_]](implicit shape : StepperShape[(K, V), S]) : S with EfficientSplit }

    /** Create a parallel [[java.util.stream.Stream Java Stream]] for the keys of this map. If
      * the keys are primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def parKeyStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit
        s: StreamShape[K, S, St],
        st: StepperShape[K, St],
        @implicitNotFound("parKeyStream can only be called on maps where `keyStepper` returns a `Stepper with EfficientSplit`")
        isEfficient: CC[K, V] <:< MapOpsWithEfficientKeyStepper): S =
      s.fromStepper(cc.keyStepper, par = true)

    /** Create a parallel [[java.util.stream.Stream Java Stream]] for the values of this map. If
      * the values are primitives, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def parValueStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit
        s: StreamShape[V, S, St],
        st: StepperShape[V, St],
        @implicitNotFound("parValueStream can only be called on maps where `valueStepper` returns a `Stepper with EfficientSplit`")
        isEfficient: CC[K, V] <:< MapOpsWithEfficientValueStepper): S =
      s.fromStepper(cc.valueStepper, par = true)

    // The parStream extension method for IterableOnce doesn't apply because its `CC` takes a single type parameter, whereas the one here takes two
    /** Create a parallel [[java.util.stream.Stream Java Stream]] for the `(key, value)` pairs of
      * this map.
      */
    def parStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit
        s: StreamShape[(K, V), S, St],
        st: StepperShape[(K, V), St],
        @implicitNotFound("parStream can only be called on maps where `stepper` returns a `Stepper with EfficientSplit`")
        isEfficient: CC[K, V] <:< MapOpsWithEfficientStepper): S =
      s.fromStepper(cc.stepper, par = true)
  }

  // steppers

  implicit class StepperHasSeqStream[A](stepper: Stepper[A]) {
    /** Create a sequential [[java.util.stream.Stream Java Stream]] for this stepper. If the
      * stepper yields primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def seqStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[A, S, St], @unused st: StepperShape[A, St]): S =
      s.fromStepper(stepper.asInstanceOf[St], par = false)
  }

  implicit class StepperHasParStream[A](stepper: Stepper[A] with EfficientSplit) {
    /** Create a parallel [[java.util.stream.Stream Java Stream]] for this stepper. If the
      * stepper yields primitive values, a corresponding specialized Stream is returned (e.g.,
      * [[java.util.stream.IntStream `IntStream`]]).
      */
    def parStream[S <: BaseStream[_, _], St <: Stepper[_]](implicit s: StreamShape[A, S, St], @unused st: StepperShape[A, St]): S =
      s.fromStepper(stepper.asInstanceOf[St], par = true)
  }

  // arrays
  // uses the JDK array spliterators (`DoubleArraySpliterator`). users can also call
  // `array.stepper.seqStream`, which then uses the Scala steppers (`DoubleArrayStepper`). the
  // steppers are also available on byte/short/char/float arrays (`WidenedByteArrayStepper`),
  // JDK spliterators only for double/int/long/reference.

  implicit class DoubleArrayHasSeqParStream(a: Array[Double]) {
    /** Create a sequential [[java.util.stream.DoubleStream Java DoubleStream]] for this array. */
    def seqStream: DoubleStream = java.util.Arrays.stream(a)
    /** Create a parallel [[java.util.stream.DoubleStream Java DoubleStream]] for this array. */
    def parStream: DoubleStream = seqStream.parallel
  }

  implicit class IntArrayHasSeqParStream(a: Array[Int]) {
    /** Create a sequential [[java.util.stream.IntStream Java IntStream]] for this array. */
    def seqStream: IntStream = java.util.Arrays.stream(a)
    /** Create a parallel [[java.util.stream.IntStream Java IntStream]] for this array. */
    def parStream: IntStream = seqStream.parallel
  }

  implicit class LongArrayHasSeqParStream(a: Array[Long]) {
    /** Create a sequential [[java.util.stream.LongStream Java LongStream]] for this array. */
    def seqStream: LongStream = java.util.Arrays.stream(a)
    /** Create a parallel [[java.util.stream.LongStream Java LongStream]] for this array. */
    def parStream: LongStream = seqStream.parallel
  }

  implicit class AnyArrayHasSeqParStream[A <: AnyRef](a: Array[A]) {
    /** Create a sequential [[java.util.stream.Stream Java Stream]] for this array. */
    def seqStream: Stream[A] = java.util.Arrays.stream(a)
    /** Create a parallel [[java.util.stream.Stream Java Stream]] for this array. */
    def parStream: Stream[A] = seqStream.parallel
  }

  implicit class ByteArrayHasSeqParStream(a: Array[Byte]) {
    /** Create a sequential [[java.util.stream.IntStream Java IntStream]] for this array. */
    def seqStream: IntStream = a.stepper.seqStream
    /** Create a parallel [[java.util.stream.IntStream Java IntStream]] for this array. */
    def parStream: IntStream = a.stepper.parStream
  }

  implicit class ShortArrayHasSeqParStream(a: Array[Short]) {
    /** Create a sequential [[java.util.stream.IntStream Java IntStream]] for this array. */
    def seqStream: IntStream = a.stepper.seqStream
    /** Create a parallel [[java.util.stream.IntStream Java IntStream]] for this array. */
    def parStream: IntStream = a.stepper.parStream
  }

  implicit class CharArrayHasSeqParStream(a: Array[Char]) {
    /** Create a sequential [[java.util.stream.IntStream Java IntStream]] for this array. */
    def seqStream: IntStream = a.stepper.seqStream
    /** Create a parallel [[java.util.stream.IntStream Java IntStream]] for this array. */
    def parStream: IntStream = a.stepper.parStream
  }

  implicit class FloatArrayHasSeqParStream(a: Array[Float]) {
    /** Create a sequential [[java.util.stream.DoubleStream Java DoubleStream]] for this array. */
    def seqStream: DoubleStream = a.stepper.seqStream

    /** Create a parallel [[java.util.stream.DoubleStream Java DoubleStream]] for this array. */
    def parStream: DoubleStream = a.stepper.parStream
  }

  // toScala for streams

  implicit class StreamHasToScala[A](stream: Stream[A]) {
    def accumulate: AnyAccumulator[A] = toScalaFactory(Accumulator)


    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * Converting a parallel streams to an [[scala.jdk.Accumulator]] using `stream.toScalaFactory(Accumulator)`
      * builds the result in parallel.
      *
      * A `toScalaFactory(Accumulator)` call automatically converts streams of boxed integers, longs or
      * doubles are converted to the primitive accumulators ([[scala.jdk.IntAccumulator]], etc.).
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    private[java8] def toScalaFactory[C](factory: collection.Factory[A, C])(implicit info: AccumulatorFactoryInfo[A, C]): C = {
      def anyAcc = stream.collect(AnyAccumulator.supplier[A], AnyAccumulator.adder[A], AnyAccumulator.merger[A])
      if (info.companion == AnyAccumulator) anyAcc.asInstanceOf[C]
      else if (info.companion == IntAccumulator) stream.asInstanceOf[Stream[Int]].collect(IntAccumulator.supplier, IntAccumulator.boxedAdder, IntAccumulator.merger).asInstanceOf[C]
      else if (info.companion == LongAccumulator) stream.asInstanceOf[Stream[Long]].collect(LongAccumulator.supplier, LongAccumulator.boxedAdder, LongAccumulator.merger).asInstanceOf[C]
      else if (info.companion == DoubleAccumulator) stream.asInstanceOf[Stream[Double]].collect(DoubleAccumulator.supplier, DoubleAccumulator.boxedAdder, DoubleAccumulator.merger).asInstanceOf[C]
      else if (stream.isParallel) anyAcc.to(factory)
      else factory.fromSpecific(stream.iterator.asScala)
    }

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * For parallel streams, using [[accumulate]] is recommended as it builds the [[scala.jdk.Accumulator]]
      * in parallel.
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    def toScala[CC[_]](implicit factory: collection.Factory[A, CC[A]]): CC[A] = {
      if (stream.isParallel) toScalaFactory(Accumulator).to(factory)
      else factory.fromSpecific(stream.iterator.asScala)
    }

    /** Convert a generic Java Stream wrapping a primitive type to a corresponding primitive
      * Stream.
      */
    def unboxed[S](implicit unboxer: StreamUnboxer[A, S]): S = unboxer(stream)
  }

  implicit class StreamIntHasAccumulatePrimitive(s: Stream[Int]) {
    def accumulatePrimitive: IntAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class StreamLongHasAccumulatePrimitive(s: Stream[Long]) {
    def accumulatePrimitive: LongAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class StreamDoubleHasAccumulatePrimitive(s: Stream[Double]) {
    def accumulatePrimitive: DoubleAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class StreamJIntegerHasAccumulatePrimitive(s: Stream[java.lang.Integer]) {
    def accumulatePrimitive: IntAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class StreamJLongHasAccumulatePrimitive(s: Stream[java.lang.Long]) {
    def accumulatePrimitive: LongAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class StreamJDoubleHasAccumulatePrimitive(s: Stream[java.lang.Double]) {
    def accumulatePrimitive: DoubleAccumulator = s.toScalaFactory(Accumulator)
  }

  implicit class IntStreamHasToScala(stream: IntStream) {
    def accumulate: IntAccumulator = toScalaFactory(IntAccumulator)

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * Converting a parallel streams to an [[scala.jdk.Accumulator]] using `stream.toScalaFactory(Accumulator)`
      * builds the result in parallel.
      *
      * A `toScalaFactory(Accumulator)` call automatically converts the `IntStream` to a primitive
      * [[scala.jdk.IntAccumulator]].
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    private[java8] def toScalaFactory[C](factory: collection.Factory[Int, C])(implicit info: AccumulatorFactoryInfo[Int, C]): C = {
      def intAcc = stream.collect(IntAccumulator.supplier, IntAccumulator.adder, IntAccumulator.merger)
      if (info.companion == AnyAccumulator) stream.collect(AnyAccumulator.supplier[Int], AnyAccumulator.unboxedIntAdder, AnyAccumulator.merger[Int]).asInstanceOf[C]
      else if (info.companion == IntAccumulator) intAcc.asInstanceOf[C]
      else if (stream.isParallel) intAcc.to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Int]].asScala)
    }

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * For parallel streams, using [[accumulate]] is recommended as it builds the [[scala.jdk.IntAccumulator]]
      * in parallel.
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    def toScala[CC[_]](implicit factory: collection.Factory[Int, CC[Int]]): CC[Int] = {
      if (stream.isParallel) toScalaFactory(IntAccumulator).to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Int]].asScala)
    }
  }

  implicit class LongStreamHasToScala(stream: LongStream) {
    def accumulate: LongAccumulator = toScalaFactory(LongAccumulator)

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * Converting a parallel streams to an [[scala.jdk.Accumulator]] using `stream.toScalaFactory(Accumulator)`
      * builds the result in parallel.
      *
      * A `toScalaFactory(Accumulator)` call automatically converts the `LongStream` to a primitive
      * [[scala.jdk.LongAccumulator]].
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    private[java8] def toScalaFactory[C](factory: collection.Factory[Long, C])(implicit info: AccumulatorFactoryInfo[Long, C]): C = {
      def longAcc = stream.collect(LongAccumulator.supplier, LongAccumulator.adder, LongAccumulator.merger)
      if (info.companion == AnyAccumulator) stream.collect(AnyAccumulator.supplier[Long], AnyAccumulator.unboxedLongAdder, AnyAccumulator.merger[Long]).asInstanceOf[C]
      else if (info.companion == LongAccumulator) longAcc.asInstanceOf[C]
      else if (stream.isParallel) longAcc.to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Long]].asScala)
    }

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * For parallel streams, using [[accumulate]] is recommended as it builds the [[scala.jdk.LongAccumulator]]
      * in parallel.
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    def toScala[CC[_]](implicit factory: collection.Factory[Long, CC[Long]]): CC[Long] = {
      if (stream.isParallel) toScalaFactory(LongAccumulator).to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Long]].asScala)
    }
  }

  implicit class DoubleStreamHasToScala(stream: DoubleStream) {
    def accumulate: DoubleAccumulator = toScalaFactory(DoubleAccumulator)

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * Converting a parallel streams to an [[scala.jdk.Accumulator]] using `stream.toScalaFactory(Accumulator)`
      * builds the result in parallel.
      *
      * A `toScalaFactory(Accumulator)` call automatically converts the `DoubleStream` to a primitive
      * [[scala.jdk.DoubleAccumulator]].
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    private[java8] def toScalaFactory[C](factory: collection.Factory[Double, C])(implicit info: AccumulatorFactoryInfo[Double, C]): C = {
      def doubleAcc = stream.collect(DoubleAccumulator.supplier, DoubleAccumulator.adder, DoubleAccumulator.merger)
      if (info.companion == AnyAccumulator) stream.collect(AnyAccumulator.supplier[Double], AnyAccumulator.unboxedDoubleAdder, AnyAccumulator.merger[Double]).asInstanceOf[C]
      else if (info.companion == DoubleAccumulator) doubleAcc.asInstanceOf[C]
      else if (stream.isParallel) doubleAcc.to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Double]].asScala)
    }

    /**
      * Copy the elements of this stream into a Scala collection.
      *
      * For parallel streams, using [[accumulate]] is recommended as it builds the [[scala.jdk.DoubleAccumulator]]
      * in parallel.
      *
      * When converting a parallel stream to a different Scala collection, the stream is first
      * converted into an [[scala.jdk.Accumulator]], which supports parallel building. The accumulator is
      * then converted to the target collection. Note that the stream is processed eagerly while
      * building the accumulator, even if the target collection is lazy.
      *
      * Sequential streams are directly converted to the target collection. If the target collection
      * is lazy, the conversion is lazy as well.
      */
    def toScala[CC[_]](implicit factory: collection.Factory[Double, CC[Double]]): CC[Double] = {
      if (stream.isParallel) toScalaFactory(DoubleAccumulator).to(factory)
      else factory.fromSpecific(stream.iterator.asInstanceOf[java.util.Iterator[Double]].asScala)
    }
  }
}

/** `StreamConverters` provides extension methods and other functionality to
  * ease interoperability of Scala collections with `java.util.stream` classes.
  * 
  * Scala collections gain extension methods `seqStream` and
  * `parStream` that allow them to be used as the source of a `Stream`.
  * Some collections either intrinsically cannot be paralellized, or
  * could be but an efficient implementation is missing.  It this case,
  * only `seqStream` is provided.  If a collection cannot be stepped over
  * at all (e.g. `Traversable`), then it gains neither method.
  *
  * `Array` also gains `seqStream` and `parStream` methods, and calling those
  * on `Array[Double]`, `Array[Int]`, or `Array[Long]` will produce the
  * corresponding primitive stream.
  *
  * Streams gain `accumulate` and `toScala[_]` methods, which collect the stream
  * into a custom high-performance `scala.collection.mutable.java8.Accumulator`,
  * which is not part of the standard collections hierarchy, or into a named
  * Scala collection, respectively.
  *
  * Generic streams also gain an `unboxed` method that will convert to the
  * corresponding unboxed primitive stream, if appropriate.  Unboxed streams
  * have custom accumulators with improved performance.
  *
  * Accumulators have `toArray`, `toList`, `iterator`, and `to[_]` methods
  * to convert to standard Scala collections.  Note that if you wish to
  * create an array from a `Stream`, going through an `Accumulator` is
  * not the most efficient option: just create the `Array` directly.
  *
  * Internally, Scala collections implement a hybrid of `Iterator` and
  * `java.util.Spliterator` to implement `Stream` compatibility; these
  * are called `Stepper`s.  In particular, they can test for the presence
  * of a next element using `hasStep`, can retrieve the next value with
  * `nextStep`, or can optionally retrieve and operate on a value if present
  * with `tryStep`, which works like `tryAdvance` in `java.util.Spliterator`.
  *
  * Every Scala collection that can be stepped
  * through has a `stepper` method implicitly provided.  In addition,
  * maps have `keyStepper` and `valueStepper` methods.  A limited number
  * of collections operations are defined on `Stepper`s, including conversion
  * to Scala collections with `to` or accumulation via `accumulate`.
  * `Stepper`s also implement `seqStream` and `parStream` to generate `Stream`s.
  * These are provided regardless of whether a `Stepper` can efficiently
  * subdivide itself for parallel processing (though one can check for the
  * presence of the `EfficientSubstep` trait to know that parallel execution will
  * not be limited by long sequential searching steps, and one can call
  * `anticipateParallelism` to warn a `Stepper` that it will be used in a parallel
  * context and thus may wish to make different tradeoffs).
  *
  * Examples:
  * {{{
  * import scala.compat.java8.StreamConverters._
  *
  * val s = Vector(1,2,3,4).parStream    // Stream[Int]
  * val si = s.unboxed                   // Stream.OfInt
  * val ai = si.accumulate               // IntAccumulator
  * val v = ai.to[Vector]                // Vector[Int] again
  *
  * val t = Array(2.0, 3.0, 4.0).parStream               // DoubleStream
  * val q = t.toScala[scala.collection.immutable.Queue]  // Queue[Double]
  *
  * val x = List(1L, 2L, 3L, 4L).stepper.parStream.sum   // 10, potentially computed in parallel
  * }}}
  */
object StreamConverters
extends StreamExtensions
with converterImpl.Priority1AccumulatorConverters
{
  implicit def richIntStepper(s: Stepper[Int]): StepperExtensions[Int] = new StepperExtensions[Int](s)
  implicit def richLongStepper(s: Stepper[Long]): StepperExtensions[Long] = new StepperExtensions[Long](s)
  implicit def richDoubleStepper(s: Stepper[Double]): StepperExtensions[Double] = new StepperExtensions[Double](s)
}
