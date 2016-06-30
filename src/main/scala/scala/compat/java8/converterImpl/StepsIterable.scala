package scala.compat.java8.converterImpl

import language.implicitConversions
import scala.annotation.switch

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

// Iterables just defer to iterator unless they can pattern match something better.
// TODO: implement pattern matching!

final class RichIterableCanStep[T](private val underlying: Iterable[T]) extends AnyVal with MakesStepper[T, Any] {
  override def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.iterator.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.iterator.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.iterator.asInstanceOf[Iterator[Double]])
    case _            => ss.seqUnbox(new StepsAnyIterator[T](underlying.iterator))
  }).asInstanceOf[S]
}
