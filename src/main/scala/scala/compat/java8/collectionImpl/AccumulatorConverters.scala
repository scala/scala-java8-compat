package scala.compat.java8.converterImpls

import language.implicitConversions

trait Priority3AccumulatorConverters {
  implicit def collectionCanAccumulate[A](underlying: TraversableOnce[A]) = new CollectionCanAccumulate[A](underlying)
}

trait Priority2AccumulatorConverters extends Priority3AccumulatorConverters {
  implicit def accumulateDoubleCollection(underlying: TraversableOnce[Double]) = new AccumulateDoubleCollection(underlying)
  implicit def accumulateIntCollection(underlying: TraversableOnce[Int]) = new AccumulateIntCollection(underlying)
  implicit def accumulateLongCollection(underlying: TraversableOnce[Long]) = new AccumulateLongCollection(underlying)
  implicit def accumulateAnyArray[A](underlying: Array[A]) = new AccumulateAnyArray(underlying)
}

trait Priority1AccumulatorConverters extends Priority2AccumulatorConverters {
  implicit def accumulateDoubleArray(underlying: Array[Double]) = new AccumulateDoubleArray(underlying)
  implicit def accumulateIntArray(underlying: Array[Int]) = new AccumulateIntArray(underlying)
  implicit def accumulateLongArray(underlying: Array[Long]) = new AccumulateLongArray(underlying)
}
