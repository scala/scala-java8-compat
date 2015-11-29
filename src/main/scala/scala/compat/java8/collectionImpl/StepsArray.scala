package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

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
