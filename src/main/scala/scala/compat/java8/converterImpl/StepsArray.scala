package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsObjectArray[A <: Object](underlying: Array[A], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsObjectArray[A]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsObjectArray[A](underlying, i0, half)
}

private[java8] class StepsAnyArray[A](underlying: Array[A], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsAnyArray[A]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsAnyArray[A](underlying, i0, half)
}

private[java8] class StepsUnitArray(underlying: Array[Unit], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Unit, StepsUnitArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; () } else throwNSEE
  def semiclone(half: Int) = new StepsUnitArray(underlying, i0, half)
}

private[java8] class StepsBoxedBooleanArray(underlying: Array[Boolean], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Boolean, StepsBoxedBooleanArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedBooleanArray(underlying, i0, half)
}

private[java8] class StepsBoxedByteArray(underlying: Array[Byte], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Byte, StepsBoxedByteArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedByteArray(underlying, i0, half)
}

private[java8] class StepsBoxedCharArray(underlying: Array[Char], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Char, StepsBoxedCharArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedCharArray(underlying, i0, half)
}

private[java8] class StepsBoxedShortArray(underlying: Array[Short], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Short, StepsBoxedShortArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedShortArray(underlying, i0, half)
}

private[java8] class StepsBoxedFloatArray(underlying: Array[Float], _i0: Int, _iN: Int)
extends StepsLikeIndexed[Float, StepsBoxedFloatArray](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsBoxedFloatArray(underlying, i0, half)
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

final class RichArrayAnyCanStep[A](private val underlying: Array[A]) extends AnyVal with MakesStepper[AnyStepper[A] with EfficientSubstep] {
  @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyArray[A](underlying, 0, underlying.length)
}

final class RichArrayObjectCanStep[A <: Object](private val underlying: Array[A]) extends AnyVal with MakesStepper[AnyStepper[A] with EfficientSubstep] {
  @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsObjectArray[A](underlying, 0, underlying.length)
}

final class RichArrayUnitCanStep(private val underlying: Array[Unit]) extends AnyVal with MakesStepper[AnyStepper[Unit] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Unit] with EfficientSubstep = new StepsUnitArray(underlying, 0, underlying.length)
}

final class RichArrayBooleanCanStep(private val underlying: Array[Boolean]) extends AnyVal with MakesStepper[AnyStepper[Boolean] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Boolean] with EfficientSubstep = new StepsBoxedBooleanArray(underlying, 0, underlying.length)
}

final class RichArrayByteCanStep(private val underlying: Array[Byte]) extends AnyVal with MakesStepper[AnyStepper[Byte] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Byte] with EfficientSubstep = new StepsBoxedByteArray(underlying, 0, underlying.length)
}

final class RichArrayCharCanStep(private val underlying: Array[Char]) extends AnyVal with MakesStepper[AnyStepper[Char] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Char] with EfficientSubstep = new StepsBoxedCharArray(underlying, 0, underlying.length)
}

final class RichArrayShortCanStep(private val underlying: Array[Short]) extends AnyVal with MakesStepper[AnyStepper[Short] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Short] with EfficientSubstep = new StepsBoxedShortArray(underlying, 0, underlying.length)
}

final class RichArrayFloatCanStep(private val underlying: Array[Float]) extends AnyVal with MakesStepper[AnyStepper[Float] with EfficientSubstep] {
  @inline def stepper: AnyStepper[Float] with EfficientSubstep = new StepsBoxedFloatArray(underlying, 0, underlying.length)
}

final class RichArrayDoubleCanStep(private val underlying: Array[Double]) extends AnyVal with MakesStepper[DoubleStepper with EfficientSubstep] {
  @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleArray(underlying, 0, underlying.length)
}

final class RichArrayIntCanStep(private val underlying: Array[Int]) extends AnyVal with MakesStepper[IntStepper with EfficientSubstep] {
  @inline def stepper: IntStepper with EfficientSubstep = new StepsIntArray(underlying, 0, underlying.length)
}

final class RichArrayLongCanStep(private val underlying: Array[Long]) extends AnyVal with MakesStepper[LongStepper with EfficientSubstep] {
  @inline def stepper: LongStepper with EfficientSubstep = new StepsLongArray(underlying, 0, underlying.length)
}
