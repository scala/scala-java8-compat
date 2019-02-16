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

private[java8] class StepsObjectArray[A <: Object](underlying: Array[A], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsObjectArray[A]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsObjectArray[A](underlying, i0, half)
}

private[java8] class StepsBoxedBooleanArray(underlying: Array[Boolean], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Boolean, StepsBoxedBooleanArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedBooleanArray(underlying, i0, half)
}

private[java8] class StepsWidenedByteArray(underlying: Array[Byte], _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsWidenedByteArray](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsWidenedByteArray(underlying, i0, half)
}

private[java8] class StepsWidenedCharArray(underlying: Array[Char], _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsWidenedCharArray](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsWidenedCharArray(underlying, i0, half)
}

private[java8] class StepsWidenedShortArray(underlying: Array[Short], _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsWidenedShortArray](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsWidenedShortArray(underlying, i0, half)
}

private[java8] class StepsWidenedFloatArray(underlying: Array[Float], _i0: Int, _iN: Int)
extends StepsDoubleLikeIndexed[StepsWidenedFloatArray](_i0, _iN) {
  def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsWidenedFloatArray(underlying, i0, half)
}

private[java8] class StepsDoubleArray(underlying: Array[Double], _i0: Int, _iN: Int)
extends StepsDoubleLikeIndexed[StepsDoubleArray](_i0, _iN) {
  def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsDoubleArray(underlying, i0, half)
}

private[java8] class StepsIntArray(underlying: Array[Int], _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntArray](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsIntArray(underlying, i0, half)
}

private[java8] class StepsLongArray(underlying: Array[Long], _i0: Int, _iN: Int)
extends StepsLongLikeIndexed[StepsLongArray](_i0, _iN) {
  def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsLongArray(underlying, i0, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichArrayCanStep[T](private val underlying: Array[T]) extends AnyVal with MakesStepper[T, EfficientSubstep] {
  override def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.Reference =>
      if(underlying.isInstanceOf[Array[Boolean]])
                                     new StepsBoxedBooleanArray  (underlying.asInstanceOf[Array[Boolean]], 0, underlying.length)
      else                           new StepsObjectArray[AnyRef](underlying.asInstanceOf[Array[AnyRef ]], 0, underlying.length)
    case StepperShape.IntValue    => new StepsIntArray           (underlying.asInstanceOf[Array[Int    ]], 0, underlying.length)
    case StepperShape.LongValue   => new StepsLongArray          (underlying.asInstanceOf[Array[Long   ]], 0, underlying.length)
    case StepperShape.DoubleValue => new StepsDoubleArray        (underlying.asInstanceOf[Array[Double ]], 0, underlying.length)
    case StepperShape.ByteValue   => new StepsWidenedByteArray   (underlying.asInstanceOf[Array[Byte   ]], 0, underlying.length)
    case StepperShape.ShortValue  => new StepsWidenedShortArray  (underlying.asInstanceOf[Array[Short  ]], 0, underlying.length)
    case StepperShape.CharValue   => new StepsWidenedCharArray   (underlying.asInstanceOf[Array[Char   ]], 0, underlying.length)
    case StepperShape.FloatValue  => new StepsWidenedFloatArray  (underlying.asInstanceOf[Array[Float  ]], 0, underlying.length)
  }).asInstanceOf[S with EfficientSubstep]
}
