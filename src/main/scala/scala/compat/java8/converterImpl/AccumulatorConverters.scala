package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._

trait Priority3AccumulatorConverters {
  implicit def collectionCanAccumulate[A](underlying: IterableOnce[A]) = new CollectionCanAccumulate[A](underlying)
}

trait Priority2AccumulatorConverters extends Priority3AccumulatorConverters {
  implicit def accumulateDoubleCollection(underlying: IterableOnce[Double]) = new AccumulateDoubleCollection(underlying)
  implicit def accumulateIntCollection(underlying: IterableOnce[Int]) = new AccumulateIntCollection(underlying)
  implicit def accumulateLongCollection(underlying: IterableOnce[Long]) = new AccumulateLongCollection(underlying)
  implicit def accumulateAnyArray[A](underlying: Array[A]) = new AccumulateAnyArray(underlying)
}

trait Priority1AccumulatorConverters extends Priority2AccumulatorConverters {
  implicit def accumulateDoubleArray(underlying: Array[Double]) = new AccumulateDoubleArray(underlying)
  implicit def accumulateIntArray(underlying: Array[Int]) = new AccumulateIntArray(underlying)
  implicit def accumulateLongArray(underlying: Array[Long]) = new AccumulateLongArray(underlying)

  implicit def accumulateAnyStepper[A]: AccumulatesFromStepper[A, Accumulator[A]] =
    PrivateAccumulatorConverters.genericAccumulateAnyStepper.asInstanceOf[AccumulatesFromStepper[A, Accumulator[A]]]
}

private[java8] object PrivateAccumulatorConverters {
  val genericAccumulateAnyStepper: AccumulatesFromStepper[Any, Accumulator[Any]] = new AccumulatesFromStepper[Any, Accumulator[Any]] {
    def apply(stepper: Stepper[Any]) = {
      val a = new Accumulator[Any]
      while (stepper.hasStep) a += stepper.nextStep
      a
    }
  }
}
