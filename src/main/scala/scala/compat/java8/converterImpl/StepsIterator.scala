package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyIterator[A](_underlying: Iterator[A])
extends StepsLikeIterator[A, StepsAnyIterator[A]](_underlying) {
  def semiclone() = new StepsAnyIterator(null)
  def next() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsDoubleIterator(_underlying: Iterator[Double])
extends StepsDoubleLikeIterator[StepsDoubleIterator](_underlying) {
  def semiclone() = new StepsDoubleIterator(null)
  def nextDouble() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsIntIterator(_underlying: Iterator[Int])
extends StepsIntLikeIterator[StepsIntIterator](_underlying) {
  def semiclone() = new StepsIntIterator(null)
  def nextInt() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsLongIterator(_underlying: Iterator[Long])
extends StepsLongLikeIterator[StepsLongIterator](_underlying) {
  def semiclone() = new StepsLongIterator(null)
  def nextLong() = if (proxied ne null) proxied.nextStep else underlying.next
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichIteratorCanStep[A](private val underlying: Iterator[A]) extends AnyVal with MakesStepper[AnyStepper[A]] {
  @inline def stepper: AnyStepper[A] = new StepsAnyIterator[A](underlying)
}

final class RichDoubleIteratorCanStep(private val underlying: Iterator[Double]) extends AnyVal with MakesStepper[DoubleStepper] {
  @inline def stepper: DoubleStepper = new StepsDoubleIterator(underlying)
}

final class RichIntIteratorCanStep(private val underlying: Iterator[Int]) extends AnyVal with MakesStepper[IntStepper] {
  @inline def stepper: IntStepper = new StepsIntIterator(underlying)
}

final class RichLongIteratorCanStep(private val underlying: Iterator[Long]) extends AnyVal with MakesStepper[LongStepper] {
  @inline def stepper: LongStepper = new StepsLongIterator(underlying)
}

