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

import scala.compat.java8.collectionImpl._

import Stepper._

////////////////////////////
// Stepper implementation //
////////////////////////////

private[java8] class StepperStringChar(underlying: CharSequence, _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepperStringChar](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying.charAt(j) } else throwNSEE
  def semiclone(half: Int) = new StepperStringChar(underlying, i0, half)
}

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

/////////////////////////
// Value class adapter //
/////////////////////////

final class RichStringCanStep(private val underlying: String) extends AnyVal with MakesStepper[Char, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[Char, S]) = charStepper.asInstanceOf[S with EfficientSubstep]
  @inline def charStepper: IntStepper with EfficientSubstep = new StepperStringChar(underlying, 0, underlying.length)
  @inline def codepointStepper: IntStepper with EfficientSubstep = new StepperStringCodePoint(underlying, 0, underlying.length)
}
