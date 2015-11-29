package scala.compat.java8

import language.implicitConversions

import java.util.stream._
import scala.compat.java8.collectionImpl._
import scala.compat.java8.converterImpls._

trait PrimitiveStreamAccumulator[S, AA] {
  def streamAccumulate(stream: S): AA
}

trait PrimitiveStreamUnboxer[A, S] {
  def apply(boxed: Stream[A]): S
}

trait Priority5StreamConverters {
  // Note--conversion is only to make sure implicit conversion priority is lower than alternatives.
  implicit class EnrichScalaCollectionWithSeqStream[A, CC](cc: CC)(implicit steppize: CC => MakesAnySeqStepper[A]) {
    def seqStream: Stream[A] = StreamSupport.stream(steppize(cc).stepper, false)
  }
  implicit class EnrichScalaCollectionWithKeySeqStream[K, CC](cc: CC)(implicit steppize: CC => MakesAnyKeySeqStepper[K]) {
    def seqKeyStream: Stream[K] = StreamSupport.stream(steppize(cc).keyStepper, false)
  }
  implicit class EnrichScalaCollectionWithValueSeqStream[V, CC](cc: CC)(implicit steppize: CC => MakesAnyValueSeqStepper[V]) {
    def seqValueStream: Stream[V] = StreamSupport.stream(steppize(cc).valueStepper, false)
  }
}

trait Priority4StreamConverters extends Priority5StreamConverters {
  implicit class EnrichScalaCollectionWithSeqDoubleStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleSeqStepper) {
    def seqStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).stepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqIntStream[CC](cc: CC)(implicit steppize: CC => MakesIntSeqStepper) {
    def seqStream: IntStream = StreamSupport.intStream(steppize(cc).stepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqLongStream[CC](cc: CC)(implicit steppize: CC => MakesLongSeqStepper) {
    def seqStream: LongStream = StreamSupport.longStream(steppize(cc).stepper, false)
  }
  implicit class EnrichScalaCollectionWithSeqDoubleKeyStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleKeySeqStepper) {
    def seqKeyStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).keyStepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqIntKeyStream[CC](cc: CC)(implicit steppize: CC => MakesIntKeySeqStepper) {
    def seqKeyStream: IntStream = StreamSupport.intStream(steppize(cc).keyStepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqLongKeyStream[CC](cc: CC)(implicit steppize: CC => MakesLongKeySeqStepper) {
    def seqKeyStream: LongStream = StreamSupport.longStream(steppize(cc).keyStepper, false)
  }
  implicit class EnrichScalaCollectionWithSeqDoubleValueStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleValueSeqStepper) {
    def seqValueStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).valueStepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqIntValueStream[CC](cc: CC)(implicit steppize: CC => MakesIntValueSeqStepper) {
    def seqValueStream: IntStream = StreamSupport.intStream(steppize(cc).valueStepper, false)
  }  
  implicit class EnrichScalaCollectionWithSeqLongValueStream[CC](cc: CC)(implicit steppize: CC => MakesLongValueSeqStepper) {
    def seqValueStream: LongStream = StreamSupport.longStream(steppize(cc).valueStepper, false)
  }
}

trait Priority3StreamConverters extends Priority4StreamConverters {
  implicit class EnrichAnySteppableWithStream[A, CC](cc: CC)(implicit steppize: CC => MakesAnyStepper[A]) {
    def seqStream: Stream[A] = StreamSupport.stream(steppize(cc).stepper, false)
    def parStream: Stream[A] = StreamSupport.stream(steppize(cc).stepper.anticipateParallelism, true)
  }
  implicit class EnrichAnyKeySteppableWithStream[K, CC](cc: CC)(implicit steppize: CC => MakesAnyKeyStepper[K]) {
    def seqKeyStream: Stream[K] = StreamSupport.stream(steppize(cc).keyStepper, false)
    def parKeyStream: Stream[K] = StreamSupport.stream(steppize(cc).keyStepper.anticipateParallelism, true)    
  }
  implicit class EnrichAnyValueSteppableWithStream[V, CC](cc: CC)(implicit steppize: CC => MakesAnyValueStepper[V]) {
    def seqValueStream: Stream[V] = StreamSupport.stream(steppize(cc).valueStepper, false)
    def parValueStream: Stream[V] = StreamSupport.stream(steppize(cc).valueStepper.anticipateParallelism, true)    
  }
}

trait Priority2StreamConverters extends Priority3StreamConverters {
  implicit class EnrichDoubleSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleStepper) {
    def seqStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).stepper, false)
    def parStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).stepper.anticipateParallelism, true)
  }
  implicit class EnrichDoubleKeySteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleKeyStepper) {
    def seqKeyStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).keyStepper, false)
    def parKeyStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).keyStepper.anticipateParallelism, true)
  }
  implicit class EnrichDoubleValueSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesDoubleValueStepper) {
    def seqValueStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).valueStepper, false)
    def parValueStream: DoubleStream = StreamSupport.doubleStream(steppize(cc).valueStepper.anticipateParallelism, true)
  }
  implicit class EnrichIntSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesIntStepper) {
    def seqStream: IntStream = StreamSupport.intStream(steppize(cc).stepper, false)
    def parStream: IntStream = StreamSupport.intStream(steppize(cc).stepper.anticipateParallelism, true)
  }
  implicit class EnrichIntKeySteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesIntKeyStepper) {
    def seqKeyStream: IntStream = StreamSupport.intStream(steppize(cc).keyStepper, false)
    def parKeyStream: IntStream = StreamSupport.intStream(steppize(cc).keyStepper.anticipateParallelism, true)
  }
  implicit class EnrichIntValueSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesIntValueStepper) {
    def seqValueStream: IntStream = StreamSupport.intStream(steppize(cc).valueStepper, false)
    def parValueStream: IntStream = StreamSupport.intStream(steppize(cc).valueStepper.anticipateParallelism, true)
  }
  implicit class EnrichLongSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesLongStepper) {
    def seqStream: LongStream = StreamSupport.longStream(steppize(cc).stepper, false)
    def parStream: LongStream = StreamSupport.longStream(steppize(cc).stepper.anticipateParallelism, true)
  }
  implicit class EnrichLongKeySteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesLongKeyStepper) {
    def seqKeyStream: LongStream = StreamSupport.longStream(steppize(cc).keyStepper, false)
    def parKeyStream: LongStream = StreamSupport.longStream(steppize(cc).keyStepper.anticipateParallelism, true)
  }
  implicit class EnrichLongValueSteppableWithStream[CC](cc: CC)(implicit steppize: CC => MakesLongValueStepper) {
    def seqValueStream: LongStream = StreamSupport.longStream(steppize(cc).valueStepper, false)
    def parValueStream: LongStream = StreamSupport.longStream(steppize(cc).valueStepper.anticipateParallelism, true)
  }
}

trait Priority1StreamConverters extends Priority2StreamConverters {
  implicit class RichStream[A](stream: Stream[A]) {
    def accumulate = stream.collect(Accumulator.supplier[A], Accumulator.adder[A], Accumulator.merger[A])
    
    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, A, Coll[A]]): Coll[A] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.Consumer[A]{ def accept(a: A) { b += a } })
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
  * to convert to standard Scala collections.
  *
  * Example:
  * ```
  * import scala.compat.java8.StreamConverers._
  *
  * val s = Vector(1,2,3,4).parStream    // Stream[Int]
  * val si = s.unboxed                   // Stream.OfInt
  * val ai = si.accumulate               // IntAccumulator
  * val v = ai.to[Vector]                // Vector[Int] again
  *
  * val t = Array(2.0, 3.0, 4.0).parStream               // DoubleStream
  * val q = t.toScala[scala.collection.immutable.Queue]  // Queue[Double]
  * ```
  */
object StreamConverters
extends Priority1StreamConverters
with converterImpls.Priority1StepConverters
with converterImpls.Priority1AccumulatorConverters
{
  implicit class EnrichDoubleArrayWithStream(a: Array[Double]) {
    def seqStream: DoubleStream = java.util.Arrays.stream(a)
    def parStream: DoubleStream = seqStream.parallel
  }

  implicit class EnrichIntArrayWithStream(a: Array[Int]) {
    def seqStream: IntStream = java.util.Arrays.stream(a)
    def parStream: IntStream = seqStream.parallel
  }

  implicit class EnrichLongArrayWithStream(a: Array[Long]) {
    def seqStream: LongStream = java.util.Arrays.stream(a)
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
  
  implicit class RichDoubleStream(stream: DoubleStream) {
    def accumulate = stream.collect(DoubleAccumulator.supplier, DoubleAccumulator.adder, DoubleAccumulator.merger)
    
    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Double, Coll[Double]]): Coll[Double] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.DoubleConsumer{ def accept(d: Double) { b += d } })
        b.result()
      }
    }
  }
  
  implicit class RichIntStream(stream: IntStream) {
    def accumulate = stream.collect(IntAccumulator.supplier, IntAccumulator.adder, IntAccumulator.merger)

    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Int, Coll[Int]]): Coll[Int] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.IntConsumer{ def accept(d: Int) { b += d } })
        b.result()
      }
    }
  }
  
  implicit class RichLongStream(stream: LongStream) {
    def accumulate = stream.collect(LongAccumulator.supplier, LongAccumulator.adder, LongAccumulator.merger)

    def toScala[Coll[_]](implicit cbf: collection.generic.CanBuildFrom[Nothing, Long, Coll[Long]]): Coll[Long] = {
      if (stream.isParallel) accumulate.to[Coll](cbf)
      else {
        val b = cbf()
        stream.forEachOrdered(new java.util.function.LongConsumer{ def accept(d: Long) { b += d } })
        b.result()
      }
    }
  }
}
