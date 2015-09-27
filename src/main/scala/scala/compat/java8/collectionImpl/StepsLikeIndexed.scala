package scala.compat.java8.collectionImpl

import Stepper._

abstract class StepsLikeIndexed[A, AA, STA >: Null <: StepsLikeIndexed[A, AA, _]](underlying: AA, var i0: Int, var iN: Int) extends AnyStepper[A] {
  def semiclone(half: Int): STA
  def characteristics() = NonNull + Sized + SubSized
  def estimateSize() = iN - i0
  def hasNext() = i0 < iN
  def substep(): AnyStepper[A] = {
    if (iN-1 > i0) {
      val half = (i0+iN) >>> 1
      val ans = semiclone(half)
      i0 = half
      ans
    }
    else null
  }
}

abstract class StepsDoubleLikeIndexed[DD, STD >: Null <: StepsDoubleLikeIndexed[DD, _]](underlying: DD, var i0: Int, var iN: Int) extends DoubleStepper {
  def semiclone(half: Int): STD
  def characteristics() = NonNull + Sized + SubSized
  def estimateSize() = iN - i0
  def hasNext() = i0 < iN
  def substep(): DoubleStepper = {
    if (iN-1 > i0) {
      val half = (i0+iN) >>> 1
      val ans = semiclone(half)
      i0 = half
      ans
    }
    else null
  }
}


abstract class StepsIntLikeIndexed[II, STI >: Null <: StepsIntLikeIndexed[II, _]](underlying: II, var i0: Int, var iN: Int) extends IntStepper {
  def semiclone(half: Int): STI
  def characteristics() = NonNull + Sized + SubSized
  def estimateSize() = iN - i0
  def hasNext() = i0 < iN
  def substep(): IntStepper = {
    if (iN-1 > i0) {
      val half = (i0+iN) >>> 1
      val ans = semiclone(half)
      i0 = half
      ans
    }
    else null
  }
}

abstract class StepsLongLikeIndexed[LL, STL >: Null <: StepsLongLikeIndexed[LL, _]](underlying: LL, var i0: Int, var iN: Int) extends LongStepper {
  def semiclone(half: Int): STL
  def characteristics() = NonNull + Sized + SubSized
  def estimateSize() = iN - i0
  def hasNext() = i0 < iN
  def substep(): LongStepper = {
    if (iN-1 > i0) {
      val half = (i0+iN) >>> 1
      val ans = semiclone(half)
      i0 = half
      ans
    }
    else null
  }
}
