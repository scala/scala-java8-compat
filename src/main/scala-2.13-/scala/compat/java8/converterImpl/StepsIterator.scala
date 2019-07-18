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

package scala.compat.java8.converterImpl

import scala.annotation.switch

import scala.compat.java8.collectionImpl._

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

final class RichIteratorCanStep[T](private val underlying: Iterator[T]) extends AnyVal with MakesStepper[T, Any] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.asInstanceOf[Iterator[Double]])
    case _            => ss.seqUnbox(new StepsAnyIterator[T](underlying))
  }).asInstanceOf[S]
}
