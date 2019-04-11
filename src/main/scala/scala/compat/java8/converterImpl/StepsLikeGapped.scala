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

/** Abstracts all the generic operations of stepping over a backing array
  * for some collection where the elements are stored generically and some
  * may be missing.  Subclasses should set `currentEntry` to `null` when it
  * is used as a signal to look for more entries in the array.  (This also
  * allows a subclass to traverse a sublist by updating `currentEntry`.)
  */
private[java8] abstract class AbstractStepsLikeGapped[Sub >: Null, Semi <: Sub](protected val underlying: Array[AnyRef], protected var i0: Int, protected var iN: Int)
  extends EfficientSubstep {

  protected var currentEntry: AnyRef = null
  def semiclone(half: Int): Semi
  def characteristics(): Int = Ordered
  def estimateSize(): Long = if (!hasNext) 0 else iN - i0
  def hasNext(): Boolean = currentEntry != null || (i0 < iN && {
      do { currentEntry = underlying(i0); i0 += 1 } while (currentEntry == null && i0 < iN)
      currentEntry != null
    })
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

/** Abstracts the process of stepping through an incompletely filled array of `AnyRefs`
  * and interpreting the contents as the elements of a collection.
  */
private[java8] abstract class StepsLikeGapped[A, STA >: Null <: StepsLikeGapped[A, _]](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends AbstractStepsLikeGapped[AnyStepper[A], STA](_underlying, _i0, _iN)
  with AnyStepper[A]
{}

/** Abstracts the process of stepping through an incompletely filled array of `AnyRefs`
  * and interpreting the contents as the elements of a collection of `Double`s.  Subclasses
  * are responsible for unboxing the `AnyRef` inside `nextDouble`.
  */
private[java8] abstract class StepsDoubleLikeGapped[STD >: Null <: StepsDoubleLikeGapped[_]](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends AbstractStepsLikeGapped[DoubleStepper, STD](_underlying, _i0, _iN)
  with DoubleStepper
{}

/** Abstracts the process of stepping through an incompletely filled array of `AnyRefs`
  * and interpreting the contents as the elements of a collection of `Int`s.  Subclasses
  * are responsible for unboxing the `AnyRef` inside `nextInt`.
  */
private[java8] abstract class StepsIntLikeGapped[STI >: Null <: StepsIntLikeGapped[_]](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends AbstractStepsLikeGapped[IntStepper, STI](_underlying, _i0, _iN)
  with IntStepper
{}

/** Abstracts the process of stepping through an incompletely filled array of `AnyRefs`
  * and interpreting the contents as the elements of a collection of `Long`s.  Subclasses
  * are responsible for unboxing the `AnyRef` inside `nextLong`.
  */
private[java8] abstract class StepsLongLikeGapped[STL >: Null <: StepsLongLikeGapped[_]](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends AbstractStepsLikeGapped[LongStepper, STL](_underlying, _i0, _iN)
  with LongStepper 
{}
