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
