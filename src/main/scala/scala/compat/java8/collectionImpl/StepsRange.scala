package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

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

final class RichRangeCanStep(private val underlying: Range) extends AnyVal with MakesIntStepper {
  @inline def stepper: IntStepper with EfficientSubstep = new StepsIntRange(underlying, 0, underlying.length)
}  

final class RichNumericRangeCanStep[T](private val underlying: collection.immutable.NumericRange[T]) extends AnyVal with MakesAnyStepper[T] {
  @inline def stepper: AnyStepper[T] with EfficientSubstep = new StepsAnyNumericRange[T](underlying, 0, underlying.length)
}

final class RichIntNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Int]) extends AnyVal with MakesIntStepper {
  @inline def stepper: IntStepper with EfficientSubstep = new StepsIntNumericRange(underlying, 0, underlying.length)
}

final class RichLongNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Long]) extends AnyVal with MakesLongStepper {
  @inline def stepper: LongStepper with EfficientSubstep = new StepsLongNumericRange(underlying, 0, underlying.length)
}
