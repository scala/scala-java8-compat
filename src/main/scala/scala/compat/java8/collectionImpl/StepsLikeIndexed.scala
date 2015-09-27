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
