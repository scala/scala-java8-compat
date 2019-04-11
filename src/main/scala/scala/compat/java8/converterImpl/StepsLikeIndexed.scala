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

/** Abstracts all the generic operations of stepping over an indexable collection */
private[java8] abstract class AbstractStepsLikeIndexed[Sub >: Null, Semi <: Sub](protected var i0: Int, protected var iN: Int)
  extends EfficientSubstep {

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
private[java8] abstract class StepsLikeIndexed[A, STA >: Null <: StepsLikeIndexed[A, _]](_i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[AnyStepper[A], STA](_i0, _iN)
  with AnyStepper[A]
{}

/** Abstracts the operation of stepping over an indexable collection of Doubles */
private[java8] abstract class StepsDoubleLikeIndexed[STD >: Null <: StepsDoubleLikeIndexed[_]](_i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[DoubleStepper, STD](_i0, _iN)
  with DoubleStepper
  with java.util.Spliterator.OfDouble  // Compiler wants this for mixin forwarder
{}

/** Abstracts the operation of stepping over an indexable collection of Ints */
private[java8] abstract class StepsIntLikeIndexed[STI >: Null <: StepsIntLikeIndexed[_]](_i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[IntStepper, STI](_i0, _iN)
  with IntStepper
  with java.util.Spliterator.OfInt  // Compiler wants this for mixin forwarder
{}

/** Abstracts the operation of stepping over an indexable collection of Longs */
private[java8] abstract class StepsLongLikeIndexed[STL >: Null <: StepsLongLikeIndexed[_]](_i0: Int, _iN: Int)
  extends AbstractStepsLikeIndexed[LongStepper, STL](_i0, _iN)
  with LongStepper
  with java.util.Spliterator.OfLong  // Compiler wants this for mixin forwarder
{}
