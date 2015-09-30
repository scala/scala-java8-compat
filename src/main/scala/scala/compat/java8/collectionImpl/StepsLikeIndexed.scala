package scala.compat.java8.collectionImpl

import java.util.Spliterator
import Stepper._

/** Abstracts all the generic operations of stepping over an indexable collection */
abstract class AbstractStepsLikeIndexed[Sub >: Null, Semi <: Sub](protected var i0: Int, protected var iN: Int) {
  def semiclone(half: Int): Semi
  def characteristics(): Int = Ordered + Sized + SubSized
  def estimateSize(): Long = iN - i0
  def hasNext(): Boolean = i0 < iN
  def substep(): Sub = {
    if (iN-1 > i0) {
      val half = (i0+iN) >>> 1
      val ans = semiclone(half)
      i0 = half
      ans
    }
    else null
  }
}

/** Abstracts the operation of stepping over a generic indexable collection */
abstract class StepsLikeIndexed[A, AA, STA >: Null <: StepsLikeIndexed[A, AA, _]](underlying: AA, _i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[AnyStepper[A], STA](_i0, _iN)
  with AnyStepper[A]
{}

/** Abstracts the operation of stepping over an indexable collection of Doubles */
abstract class StepsDoubleLikeIndexed[DD, STD >: Null <: StepsDoubleLikeIndexed[DD, _]](underlying: DD, _i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[DoubleStepper, STD](_i0, _iN)
  with DoubleStepper
{}

/** Abstracts the operation of stepping over an indexable collection of Ints */
abstract class StepsIntLikeIndexed[II, STI >: Null <: StepsIntLikeIndexed[II, _]](underlying: II, _i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[IntStepper, STI](_i0, _iN)
  with IntStepper
{}

/** Abstracts the operation of stepping over an indexable collection of Longs */
abstract class StepsLongLikeIndexed[LL, STL >: Null <: StepsLongLikeIndexed[LL, _]](underlying: LL, _i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[LongStepper, STL](_i0, _iN)
  with LongStepper
{}
