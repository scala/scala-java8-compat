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

import scala.language.higherKinds

import java.util.stream._
import scala.compat.java8.collectionImpl._
import scala.compat.java8.converterImpl._

/** Classes or objects implementing this trait create streams suitable for sequential use */
trait MakesSequentialStream[T, SS <: java.util.stream.BaseStream[_, SS]] extends Any {
  def seqStream: SS
}

/** Classes or objects implementing this trait create streams suitable for parallel use */
trait MakesParallelStream[T, SS <: java.util.stream.BaseStream[_, SS]] extends Any {
  def parStream: SS
}

sealed trait StreamShape[T, S <: BaseStream[_, S]] {
  def fromStepper     (mk: MakesStepper[T, _],            par: Boolean): S
  def fromKeyStepper  (mk: MakesKeyValueStepper[T, _, _], par: Boolean): S
  def fromValueStepper(mk: MakesKeyValueStepper[_, T, _], par: Boolean): S
}
object StreamShape extends StreamShapeLowPriority {
  // primitive
  implicit val IntValue =    intStreamShape[Int]
  implicit val LongValue =   longStreamShape[Long]
  implicit val DoubleValue = doubleStreamShape[Double]

  // widening
  implicit val ByteValue    = intStreamShape[Byte]
  implicit val ShortValue   = intStreamShape[Short]
  implicit val CharValue    = intStreamShape[Char]
  implicit val FloatValue   = doubleStreamShape[Float]
}
trait StreamShapeLowPriority {
  protected[this] abstract class BaseStreamShape[T, S <: BaseStream[_, S], St <: Stepper[_]](implicit ss: StepperShape[T, St]) extends StreamShape[T, S] {
    final def fromStepper     (mk: MakesStepper[T, _],            par: Boolean): S = stream(mk.stepper,      par)
    final def fromKeyStepper  (mk: MakesKeyValueStepper[T, _, _], par: Boolean): S = stream(mk.keyStepper,   par)
    final def fromValueStepper(mk: MakesKeyValueStepper[_, T, _], par: Boolean): S = stream(mk.valueStepper, par)
    @inline private[this] def stream(st: St, par: Boolean): S = mkStream(if(par) st.anticipateParallelism else st, par)
    protected[this] def mkStream(st: St, par: Boolean): S
  }
  protected[this] def intStreamShape[T](implicit ss: StepperShape[T, IntStepper]): StreamShape[T, IntStream] = new BaseStreamShape[T, IntStream, IntStepper] {
    protected[this] def mkStream(st: IntStepper, par: Boolean): IntStream = StreamSupport.intStream(st, par)
  }
  protected[this] def longStreamShape[T](implicit ss: StepperShape[T, LongStepper]): StreamShape[T, LongStream] = new BaseStreamShape[T, LongStream, LongStepper] {
    protected[this] def mkStream(st: LongStepper, par: Boolean): LongStream = StreamSupport.longStream(st, par)
  }
  protected[this] def doubleStreamShape[T](implicit ss: StepperShape[T, DoubleStepper]): StreamShape[T, DoubleStream] = new BaseStreamShape[T, DoubleStream, DoubleStepper] {
    protected[this] def mkStream(st: DoubleStepper, par: Boolean): DoubleStream = StreamSupport.doubleStream(st, par)
  }

  // reference
  implicit def anyStreamShape[T]: StreamShape[T, Stream[T]] = new BaseStreamShape[T, Stream[T], AnyStepper[T]] {
    protected[this] def mkStream(st: AnyStepper[T], par: Boolean): Stream[T] = StreamSupport.stream(st, par)
  }
}

trait PrimitiveStreamAccumulator[S, AA] {
  def streamAccumulate(stream: S): AA
}

trait PrimitiveStreamUnboxer[A, S] {
  def apply(boxed: Stream[A]): S
}

trait Priority2StreamConverters {
  implicit class EnrichAnySteppableWithParStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesStepper[A, EfficientSubstep], ss: StreamShape[A, S])
    extends MakesParallelStream[A, S] {
    def parStream: S = ss.fromStepper(steppize(cc), true)
  }
  implicit class EnrichAnySteppableWithSeqStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesStepper[A, Any], ss: StreamShape[A, S])
    extends MakesSequentialStream[A, S] {
    def seqStream: S = ss.fromStepper(steppize(cc), false)
  }
  implicit class EnrichAnySteppableWithParKeyStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesKeyValueStepper[A, _, EfficientSubstep], ss: StreamShape[A, S]) {
    def parKeyStream: S = ss.fromKeyStepper(steppize(cc), true)
  }
  implicit class EnrichScalaCollectionWithSeqKeyStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesKeyValueStepper[A, _, Any], ss: StreamShape[A, S]) {
    def seqKeyStream: S = ss.fromKeyStepper(steppize(cc), false)
  }
  implicit class EnrichAnySteppableWithParValueStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesKeyValueStepper[_, A, EfficientSubstep], ss: StreamShape[A, S]) {
    def parValueStream: S = ss.fromValueStepper(steppize(cc), true)
  }
  implicit class EnrichScalaCollectionWithSeqValueStream[A, S <: BaseStream[_, S], CC](cc: CC)(implicit steppize: CC => MakesKeyValueStepper[_, A, Any], ss: StreamShape[A, S]) {
    def seqValueStream: S = ss.fromValueStepper(steppize(cc), false)
  }
}

trait Priority1StreamConverters extends Priority2StreamConverters {
  implicit class RichStream[A](stream: Stream[A]) {
    def accumulate = stream.collect(Accumulator.supplier[A], Accumulator.adder[A], Accumulator.merger[A])
    
    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, A, Coll[A]]): Coll[A] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.Consumer[A]{ def accept(a: A): Unit = { b += a } })
        b.result()
      }
    }
    
    def unboxed[S](implicit ubx: PrimitiveStreamUnboxer[A, S]): S = ubx(stream)
  }
  
  implicit class RichStreamCanAccumulatePrimitive[S](stream: S) {
    def accumulatePrimitive[AA](implicit psa: PrimitiveStreamAccumulator[S, AA]) = psa.streamAccumulate(stream)
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
extends Priority1StreamConverters
with converterImpl.Priority1StepConverters
with converterImpl.Priority1AccumulatorConverters
{
  implicit final class EnrichDoubleArrayWithStream(private val a: Array[Double])
  extends AnyVal with MakesSequentialStream[Double, DoubleStream] with MakesParallelStream[Double, DoubleStream] {
    def seqStream: DoubleStream = java.util.Arrays.stream(a)
    def parStream: DoubleStream = seqStream.parallel
  }

  implicit final class EnrichIntArrayWithStream(private val a: Array[Int])
  extends AnyVal with MakesSequentialStream[Int, IntStream] with MakesParallelStream[Int, IntStream] {
    def seqStream: IntStream = java.util.Arrays.stream(a)
    def parStream: IntStream = seqStream.parallel
  }

  implicit final class EnrichLongArrayWithStream(private val a: Array[Long])
  extends AnyVal with MakesSequentialStream[Long, LongStream] with MakesParallelStream[Long, LongStream] {
    def seqStream: LongStream = java.util.Arrays.stream(a)
    def parStream: LongStream = seqStream.parallel
  }

  implicit final class EnrichDoubleWrappedArrayWithStream(private val a: collection.mutable.WrappedArray[Double])
    extends AnyVal with MakesSequentialStream[Double, DoubleStream] with MakesParallelStream[Double, DoubleStream] {
    def seqStream: DoubleStream = java.util.Arrays.stream(a.array)
    def parStream: DoubleStream = seqStream.parallel
  }

  implicit final class EnrichIntWrappedArrayWithStream(private val a: collection.mutable.WrappedArray[Int])
    extends AnyVal with MakesSequentialStream[Int, IntStream] with MakesParallelStream[Int, IntStream] {
    def seqStream: IntStream = java.util.Arrays.stream(a.array)
    def parStream: IntStream = seqStream.parallel
  }

  implicit final class EnrichLongWrappedArrayWithStream(private val a: collection.mutable.WrappedArray[Long])
    extends AnyVal with MakesSequentialStream[Long, LongStream] with MakesParallelStream[Long, LongStream] {
    def seqStream: LongStream = java.util.Arrays.stream(a.array)
    def parStream: LongStream = seqStream.parallel
  }

  implicit val primitiveAccumulateDoubleStream = new PrimitiveStreamAccumulator[Stream[Double], DoubleAccumulator] {
    def streamAccumulate(stream: Stream[Double]): DoubleAccumulator = 
      stream.collect(DoubleAccumulator.supplier, DoubleAccumulator.boxedAdder, DoubleAccumulator.merger)
  }
  
  implicit val primitiveAccumulateDoubleStream2 =
    primitiveAccumulateDoubleStream.asInstanceOf[PrimitiveStreamAccumulator[Stream[java.lang.Double], DoubleAccumulator]]
    
  implicit val primitiveUnboxDoubleStream = new PrimitiveStreamUnboxer[Double, DoubleStream] {
    def apply(boxed: Stream[Double]): DoubleStream = 
      boxed.mapToDouble(new java.util.function.ToDoubleFunction[Double]{ def applyAsDouble(d: Double) = d })
  }
  
  implicit val primitiveUnboxDoubleStream2 =
    primitiveUnboxDoubleStream.asInstanceOf[PrimitiveStreamUnboxer[java.lang.Double, DoubleStream]]
  
  implicit val primitiveAccumulateIntStream = new PrimitiveStreamAccumulator[Stream[Int], IntAccumulator] { 
    def streamAccumulate(stream: Stream[Int]): IntAccumulator = 
      stream.collect(IntAccumulator.supplier, IntAccumulator.boxedAdder, IntAccumulator.merger)
  }

  implicit val primitiveAccumulateIntStream2 =
    primitiveAccumulateIntStream.asInstanceOf[PrimitiveStreamAccumulator[Stream[java.lang.Integer], IntAccumulator]]
  
  implicit val primitiveUnboxIntStream = new PrimitiveStreamUnboxer[Int, IntStream] {
    def apply(boxed: Stream[Int]): IntStream = 
      boxed.mapToInt(new java.util.function.ToIntFunction[Int]{ def applyAsInt(d: Int) = d })
  }
  
  implicit val primitiveUnboxIntStream2 =
    primitiveUnboxIntStream.asInstanceOf[PrimitiveStreamUnboxer[java.lang.Integer, IntStream]]
  
  implicit val primitiveAccumulateLongStream = new PrimitiveStreamAccumulator[Stream[Long], LongAccumulator] { 
    def streamAccumulate(stream: Stream[Long]): LongAccumulator = 
      stream.collect(LongAccumulator.supplier, LongAccumulator.boxedAdder, LongAccumulator.merger)
  }

  implicit val primitiveAccumulateLongStream2 =
    primitiveAccumulateLongStream.asInstanceOf[PrimitiveStreamAccumulator[Stream[java.lang.Long], LongAccumulator]]
  
  implicit val primitiveUnboxLongStream = new PrimitiveStreamUnboxer[Long, LongStream] {
    def apply(boxed: Stream[Long]): LongStream = 
      boxed.mapToLong(new java.util.function.ToLongFunction[Long]{ def applyAsLong(d: Long) = d })
  }
  
  implicit val primitiveUnboxLongStream2 =
    primitiveUnboxLongStream.asInstanceOf[PrimitiveStreamUnboxer[java.lang.Long, LongStream]]
  
  implicit final class RichDoubleStream(private val stream: DoubleStream) extends AnyVal {
    def accumulate = stream.collect(DoubleAccumulator.supplier, DoubleAccumulator.adder, DoubleAccumulator.merger)
    
    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Double, Coll[Double]]): Coll[Double] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.DoubleConsumer{ def accept(d: Double): Unit = { b += d } })
        b.result()
      }
    }
  }
  
  implicit final class RichIntStream(private val stream: IntStream) extends AnyVal {
    def accumulate = stream.collect(IntAccumulator.supplier, IntAccumulator.adder, IntAccumulator.merger)

    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Int, Coll[Int]]): Coll[Int] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.IntConsumer{ def accept(d: Int): Unit = { b += d } })
        b.result()
      }
    }
  }
  
  implicit final class RichLongStream(private val stream: LongStream) extends AnyVal {
    def accumulate = stream.collect(LongAccumulator.supplier, LongAccumulator.adder, LongAccumulator.merger)

    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Long, Coll[Long]]): Coll[Long] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.LongConsumer{ def accept(d: Long): Unit = { b += d } })
        b.result()
      }
    }
  }

  implicit val accumulateDoubleStepper = new AccumulatesFromStepper[Double, DoubleAccumulator] {
    def apply(stepper: Stepper[Double]) = {
      val a = new DoubleAccumulator
      while (stepper.hasStep) a += stepper.nextStep
      a
    }
  }

  implicit val accumulateIntStepper = new AccumulatesFromStepper[Int, IntAccumulator] {
    def apply(stepper: Stepper[Int]) = {
      val a = new IntAccumulator
      while (stepper.hasStep) a += stepper.nextStep
      a
    }
  }

  implicit val accumulateLongStepper = new AccumulatesFromStepper[Long, LongAccumulator] {
    def apply(stepper: Stepper[Long]) = {
      val a = new LongAccumulator
      while (stepper.hasStep) a += stepper.nextStep
      a
    }
  }
}
