package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

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
