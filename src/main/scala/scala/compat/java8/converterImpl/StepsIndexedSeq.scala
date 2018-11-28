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

import scala.annotation.switch
import scala.compat.java8.collectionImpl._
import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyIndexedSeq[A](underlying: collection.IndexedSeqOps[A, Any, _], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsAnyIndexedSeq[A]](_i0, _iN) {
  def next() = if (hasNext) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  protected def semiclone(half: Int) = new StepsAnyIndexedSeq[A](underlying, i0, half)
}

private[java8] class StepsDoubleIndexedSeq[CC <: collection.IndexedSeqOps[Double, Any, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsDoubleLikeIndexed[StepsDoubleIndexedSeq[CC]](_i0, _iN) {
  def nextDouble() = if (hasNext) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  protected def semiclone(half: Int) = new StepsDoubleIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsIntIndexedSeq[CC <: collection.IndexedSeqOps[Int, Any, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntIndexedSeq[CC]](_i0, _iN) {
  def nextInt() = if (hasNext) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  protected def semiclone(half: Int) = new StepsIntIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsLongIndexedSeq[CC <: collection.IndexedSeqOps[Long, Any, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsLongLikeIndexed[StepsLongIndexedSeq[CC]](_i0, _iN) {
  def nextLong() = if (hasNext) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  protected def semiclone(half: Int) = new StepsLongIndexedSeq[CC](underlying, i0, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichIndexedSeqCanStep[T](private val underlying: collection.IndexedSeqOps[T, Any, _]) extends AnyVal with MakesStepper[T, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntIndexedSeq   (underlying.asInstanceOf[collection.IndexedSeqOps[Int, Any, _]],    0, underlying.length)
    case StepperShape.LongValue   => new StepsLongIndexedSeq  (underlying.asInstanceOf[collection.IndexedSeqOps[Long, Any, _]],   0, underlying.length)
    case StepperShape.DoubleValue => new StepsDoubleIndexedSeq(underlying.asInstanceOf[collection.IndexedSeqOps[Double, Any, _]], 0, underlying.length)
    case _            => ss.parUnbox(new StepsAnyIndexedSeq[T](underlying,                                                        0, underlying.length))
  }).asInstanceOf[S with EfficientSubstep]
}
