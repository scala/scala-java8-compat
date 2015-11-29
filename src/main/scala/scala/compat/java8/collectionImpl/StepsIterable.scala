package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

// Iterables just defer to iterator unless they can pattern match something better.
// TODO: implement pattern matching!

final class RichIterableCanStep[A](private val underlying: Iterable[A]) extends AnyVal with MakesAnySeqStepper[A] {
  @inline def stepper: AnyStepper[A] = new StepsAnyIterator[A](underlying.iterator)
}

final class RichDoubleIterableCanStep(private val underlying: Iterable[Double]) extends AnyVal with MakesDoubleSeqStepper {
  @inline def stepper: DoubleStepper = new StepsDoubleIterator(underlying.iterator)
}

final class RichIntIterableCanStep(private val underlying: Iterable[Int]) extends AnyVal with MakesIntSeqStepper {
  @inline def stepper: IntStepper = new StepsIntIterator(underlying.iterator)
}

final class RichLongIterableCanStep(private val underlying: Iterable[Long]) extends AnyVal with MakesLongSeqStepper {
  @inline def stepper: LongStepper = new StepsLongIterator(underlying.iterator)
}
