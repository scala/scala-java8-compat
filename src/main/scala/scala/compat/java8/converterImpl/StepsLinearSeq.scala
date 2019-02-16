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

private[java8] class StepsAnyLinearSeq[A](_underlying: collection.LinearSeq[A], _maxN: Long)
extends StepsWithTail[A, collection.LinearSeq[A], StepsAnyLinearSeq[A]](_underlying, _maxN) {
  protected def myIsEmpty(cc: collection.LinearSeq[A]): Boolean = cc.isEmpty
  protected def myTailOf(cc: collection.LinearSeq[A]) = cc.tail
  def next() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
  def semiclone(half: Int) = new StepsAnyLinearSeq[A](underlying, half)
}

private[java8] class StepsDoubleLinearSeq(_underlying: collection.LinearSeq[Double], _maxN: Long)
extends StepsDoubleWithTail[collection.LinearSeq[Double], StepsDoubleLinearSeq](_underlying, _maxN) {
  protected def myIsEmpty(cc: collection.LinearSeq[Double]): Boolean = cc.isEmpty
  protected def myTailOf(cc: collection.LinearSeq[Double]) = cc.tail
  def nextDouble() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
  def semiclone(half: Int) = new StepsDoubleLinearSeq(underlying, half)
}

private[java8] class StepsIntLinearSeq(_underlying: collection.LinearSeq[Int], _maxN: Long)
extends StepsIntWithTail[collection.LinearSeq[Int], StepsIntLinearSeq](_underlying, _maxN) {
  protected def myIsEmpty(cc: collection.LinearSeq[Int]): Boolean = cc.isEmpty
  protected def myTailOf(cc: collection.LinearSeq[Int]) = cc.tail
  def nextInt() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
  def semiclone(half: Int) = new StepsIntLinearSeq(underlying, half)
}

private[java8] class StepsLongLinearSeq(_underlying: collection.LinearSeq[Long], _maxN: Long)
extends StepsLongWithTail[collection.LinearSeq[Long], StepsLongLinearSeq](_underlying, _maxN) {
  protected def myIsEmpty(cc: collection.LinearSeq[Long]): Boolean = cc.isEmpty
  protected def myTailOf(cc: collection.LinearSeq[Long]) = cc.tail
  def nextLong() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
  def semiclone(half: Int) = new StepsLongLinearSeq(underlying, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichLinearSeqCanStep[T](private val underlying: collection.LinearSeq[T]) extends AnyVal with MakesStepper[T, Any] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntLinearSeq   (underlying.asInstanceOf[collection.LinearSeq[Int]],    Long.MaxValue)
    case StepperShape.LongValue   => new StepsLongLinearSeq  (underlying.asInstanceOf[collection.LinearSeq[Long]],   Long.MaxValue)
    case StepperShape.DoubleValue => new StepsDoubleLinearSeq(underlying.asInstanceOf[collection.LinearSeq[Double]], Long.MaxValue)
    case _            => ss.seqUnbox(new StepsAnyLinearSeq[T](underlying,                                            Long.MaxValue))
  }).asInstanceOf[S]
}
