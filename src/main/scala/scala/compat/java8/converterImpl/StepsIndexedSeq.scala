package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyIndexedSeq[A](underlying: collection.IndexedSeqLike[A, _], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsAnyIndexedSeq[A]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsAnyIndexedSeq[A](underlying, i0, half)
}

private[java8] class StepsDoubleIndexedSeq[CC <: collection.IndexedSeqLike[Double, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsDoubleLikeIndexed[StepsDoubleIndexedSeq[CC]](_i0, _iN) {
  def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsDoubleIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsIntIndexedSeq[CC <: collection.IndexedSeqLike[Int, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntIndexedSeq[CC]](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsIntIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsLongIndexedSeq[CC <: collection.IndexedSeqLike[Long, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsLongLikeIndexed[StepsLongIndexedSeq[CC]](_i0, _iN) {
  def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsLongIndexedSeq[CC](underlying, i0, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichIndexedSeqCanStep[A](private val underlying: collection.IndexedSeqLike[A, _]) extends AnyVal with MakesStepper[AnyStepper[A] with EfficientSubstep] {
  @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyIndexedSeq[A](underlying, 0, underlying.length)
}

final class RichDoubleIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Double, _]](private val underlying: CC) extends AnyVal with MakesStepper[DoubleStepper with EfficientSubstep] {
  @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleIndexedSeq[CC](underlying, 0, underlying.length)
}

final class RichIntIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Int, _]](private val underlying: CC) extends AnyVal with MakesStepper[IntStepper with EfficientSubstep] {
  @inline def stepper: IntStepper with EfficientSubstep = new StepsIntIndexedSeq[CC](underlying, 0, underlying.length)
}

final class RichLongIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Long, _]](private val underlying: CC) extends AnyVal with MakesStepper[LongStepper with EfficientSubstep] {
  @inline def stepper: LongStepper with EfficientSubstep = new StepsLongIndexedSeq[CC](underlying, 0, underlying.length)
}
