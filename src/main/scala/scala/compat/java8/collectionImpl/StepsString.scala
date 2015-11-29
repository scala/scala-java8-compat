package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

private[java8] class StepperStringCodePoint(underlying: String, var i0: Int, var iN: Int) extends IntStepper with EfficientSubstep {
  def characteristics() = NonNull
  def estimateSize = iN - i0
  def hasNext = i0 < iN
  def nextInt() = {
    if (hasNext()) {
      val cp = underlying.codePointAt(i0)
      i0 += java.lang.Character.charCount(cp)
      cp
    }
    else throwNSEE
  }
  def substep() = {
    if (iN-3 > i0) {
      var half = (i0+iN) >>> 1
      if (java.lang.Character.isLowSurrogate(underlying.charAt(half))) half -= 1
      val ans = new StepperStringCodePoint(underlying, i0, half)
      i0 = half
      ans
    }
    else null
  }
}

