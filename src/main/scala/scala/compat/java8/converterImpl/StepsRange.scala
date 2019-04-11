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

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsIntRange(underlying: Range, _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntRange](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsIntRange(underlying, i0, half)
}

private[java8] class StepsAnyNumericRange[T](underlying: collection.immutable.NumericRange[T], _i0: Int, _iN: Int)
extends StepsLikeIndexed[T, StepsAnyNumericRange[T]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsAnyNumericRange[T](underlying, i0, half)
}

private[java8] class StepsIntNumericRange(underlying: collection.immutable.NumericRange[Int], _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntNumericRange](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsIntNumericRange(underlying, i0, half)
}

private[java8] class StepsLongNumericRange(underlying: collection.immutable.NumericRange[Long], _i0: Int, _iN: Int)
extends StepsLongLikeIndexed[StepsLongNumericRange](_i0, _iN) {
  def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsLongNumericRange(underlying, i0, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichRangeCanStep[T](private val underlying: Range) extends AnyVal with MakesStepper[Int, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[Int, S]) =
    new StepsIntRange(underlying, 0, underlying.length).asInstanceOf[S with EfficientSubstep]
}

final class RichNumericRangeCanStep[T](private val underlying: collection.immutable.NumericRange[T]) extends AnyVal with MakesStepper[T, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntNumericRange   (underlying.asInstanceOf[collection.immutable.NumericRange[Int]],  0, underlying.length)
    case StepperShape.LongValue   => new StepsLongNumericRange  (underlying.asInstanceOf[collection.immutable.NumericRange[Long]], 0, underlying.length)
    case _            => ss.parUnbox(new StepsAnyNumericRange[T](underlying,                                                       0, underlying.length))
  }).asInstanceOf[S with EfficientSubstep]
}
