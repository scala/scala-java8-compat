package scala.compat.java8

import language.implicitConversions

import java.util.stream._
import scala.compat.java8.collectionImpl._

trait PrimitiveStreamAccumulator[S, AA] {
  def streamAccumulate(stream: S): AA
}

trait PrimitiveStreamUnboxer[A, S] {
  def apply(boxed: Stream[A]): S
}

trait Priority3StreamConverters {
  implicit class EnrichAnyScalaCollectionWithStream[A](t: TraversableOnce[A]) {
    private def mkAcc() = {
      val acc = new Accumulator[A]
      t.foreach{ acc += _ }
      acc
    }
      
    def seqStream: Stream[A] = mkAcc().seqStream
    
    def parStream: Stream[A] = mkAcc().parStream
  }  
}

trait Priority2StreamConverters extends Priority3StreamConverters {
  implicit class EnrichScalaCollectionWithStream[A <: AnyRef](t: TraversableOnce[A]) {
    private def mkArr()(implicit tag: reflect.ClassTag[A]): Array[A] = {
      if (t.isTraversableAgain && t.hasDefiniteSize) {
        val sz = t.size
        val a = new Array[A](sz)
        t.copyToArray(a, 0, sz)
        a
      }
      else t.toArray[A]
    }
    
    def seqStream(implicit tag: reflect.ClassTag[A]): Stream[A] =
      java.util.Arrays.stream(mkArr())
      
    def parStream(implicit tag: reflect.ClassTag[A]): Stream[A] = seqStream.parallel
  }

  implicit class EnrichMissingPrimitiveArrayWithStream[A](a: Array[A]) {
    private def mkAcc() = {
      val acc = new Accumulator[A]
      var i = 0
      while (i < a.length) {
        acc += a(i)
        i += 1
      }
      acc
    }
    
    def seqStream: Stream[A] = mkAcc().seqStream
    
    def parStream: Stream[A] = mkAcc().parStream
  }
}

trait Priority1StreamConverters extends Priority2StreamConverters {
  implicit class EnrichGenericArrayWithStream[A <: AnyRef](a: Array[A]) {
    def seqStream: Stream[A] = java.util.Arrays.stream(a)
    def parStream: Stream[A] = seqStream.parallel
  }

  implicit class EnrichGenericIndexedSeqOptimizedWithStream[A, CC <: collection.IndexedSeqOptimized[A, _]](c: CC) {
    private def someStream(parallel: Boolean): Stream[A] =
      StreamSupport.stream(new converterImpls.StepsAnyIndexedSeqOptimized[A, CC](c, 0, c.length), parallel)
    def seqStream: Stream[A] = someStream(false)
    def parStream: Stream[A] = someStream(true)
  }

  implicit class EnrichAnyVectorWithStream[A](c: Vector[A]) {
    private def someStream(parallel: Boolean): Stream[A] =
      StreamSupport.stream(new converterImpls.StepsAnyVector[A](c, 0, c.length), parallel)
    def seqStream: Stream[A] = someStream(false)
    def parStream: Stream[A] = someStream(true)
  }

  implicit class EnrichGenericFlatHashTableWithStream[A](fht: collection.mutable.FlatHashTable[A]) {
    private def someStream(parallel: Boolean): Stream[A] = {
      val tbl = runtime.CollectionInternals.getTable(fht)
      StreamSupport.stream(new converterImpls.StepsAnyFlatHashTable[A](tbl, 0, tbl.length), parallel)
    }
    def seqStream: Stream[A] = someStream(false)
    def parStream: Stream[A] = someStream(true)
  }

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
object StreamConverters extends Priority1StreamConverters {
  implicit class EnrichDoubleIndexedSeqOptimizedWithStream[CC <: collection.IndexedSeqOptimized[Double, _]](c: CC) {
    private def someStream(parallel: Boolean): DoubleStream =
      StreamSupport.doubleStream(new converterImpls.StepsDoubleIndexedSeqOptimized[CC](c, 0, c.length), parallel)
    def seqStream: DoubleStream = someStream(false)
    def parStream: DoubleStream = someStream(true)
  }

  implicit class EnrichIntIndexedSeqOptimizedWithStream[CC <: collection.IndexedSeqOptimized[Int, _]](c: CC) {
    private def someStream(parallel: Boolean): IntStream =
      StreamSupport.intStream(new converterImpls.StepsIntIndexedSeqOptimized[CC](c, 0, c.length), parallel)
    def seqStream: IntStream = someStream(false)
    def parStream: IntStream = someStream(true)
  }

  implicit class EnrichLongIndexedSeqOptimizedWithStream[CC <: collection.IndexedSeqOptimized[Long, _]](c: CC) {
    private def someStream(parallel: Boolean): LongStream =
      StreamSupport.longStream(new converterImpls.StepsLongIndexedSeqOptimized[CC](c, 0, c.length), parallel)
    def seqStream: LongStream = someStream(false)
    def parStream: LongStream = someStream(true)
  }

  implicit class EnrichDoubleVectorWithStream(c: Vector[Double]) {
    private def someStream(parallel: Boolean): DoubleStream =
      StreamSupport.doubleStream(new converterImpls.StepsDoubleVector(c, 0, c.length), parallel)
    def seqStream: DoubleStream = someStream(false)
    def parStream: DoubleStream = someStream(true)
  }

  implicit class EnrichIntVectorWithStream(c: Vector[Int]) {
    private def someStream(parallel: Boolean): IntStream =
      StreamSupport.intStream(new converterImpls.StepsIntVector(c, 0, c.length), parallel)
    def seqStream: IntStream = someStream(false)
    def parStream: IntStream = someStream(true)
  }

  implicit class EnrichLongVectorWithStream(c: Vector[Long]) {
    private def someStream(parallel: Boolean): LongStream =
      StreamSupport.longStream(new converterImpls.StepsLongVector(c, 0, c.length), parallel)
    def seqStream: LongStream = someStream(false)
    def parStream: LongStream = someStream(true)
  }

  implicit class EnrichDoubleFlatHashTableWithStream(fht: collection.mutable.FlatHashTable[Double]) {
    private def someStream(parallel: Boolean): DoubleStream = {
      val tbl = runtime.CollectionInternals.getTable(fht)
      StreamSupport.doubleStream(new converterImpls.StepsDoubleFlatHashTable(tbl, 0, tbl.length), parallel)
    }
    def seqStream: DoubleStream = someStream(false)
    def parStream: DoubleStream = someStream(true)
  }

  implicit class EnrichIntFlatHashTableWithStream(fht: collection.mutable.FlatHashTable[Int]) {
    private def someStream(parallel: Boolean): IntStream = {
      val tbl = runtime.CollectionInternals.getTable(fht)
      StreamSupport.intStream(new converterImpls.StepsIntFlatHashTable(tbl, 0, tbl.length), parallel)
    }
    def seqStream: IntStream = someStream(false)
    def parStream: IntStream = someStream(true)
  }

  implicit class EnrichLongFlatHashTableWithStream(fht: collection.mutable.FlatHashTable[Long]) {
    private def someStream(parallel: Boolean): LongStream = {
      val tbl = runtime.CollectionInternals.getTable(fht)
      StreamSupport.longStream(new converterImpls.StepsLongFlatHashTable(tbl, 0, tbl.length), parallel)
    }
    def seqStream: LongStream = someStream(false)
    def parStream: LongStream = someStream(true)
  }

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
