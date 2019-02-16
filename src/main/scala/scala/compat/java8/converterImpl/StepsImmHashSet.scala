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

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyImmHashSet[A](_underlying: Iterator[A], _N: Int)
extends StepsLikeTrieIterator[A, StepsAnyImmHashSet[A]](_underlying, _N) {
  protected def demiclone(it: Iterator[A], N: Int) = new StepsAnyImmHashSet(it, N)
  def next(): A = { val ans = underlying.next; i += 1; ans }
}

private[java8] class StepsDoubleImmHashSet(_underlying: Iterator[Double], _N: Int)
extends StepsDoubleLikeTrieIterator[StepsDoubleImmHashSet](_underlying, _N) {
  protected def demiclone(it: Iterator[Double], N: Int) = new StepsDoubleImmHashSet(it, N)
  def nextDouble() = { val ans = underlying.next; i += 1; ans }
}

private[java8] class StepsIntImmHashSet(_underlying: Iterator[Int], _N: Int)
extends StepsIntLikeTrieIterator[StepsIntImmHashSet](_underlying, _N) {
  protected def demiclone(it: Iterator[Int], N: Int) = new StepsIntImmHashSet(it, N)
  def nextInt() = { val ans = underlying.next; i += 1; ans }
}

private[java8] class StepsLongImmHashSet(_underlying: Iterator[Long], _N: Int)
extends StepsLongLikeTrieIterator[StepsLongImmHashSet](_underlying, _N) {
  protected def demiclone(it: Iterator[Long], N: Int) = new StepsLongImmHashSet(it, N)
  def nextLong() = { val ans = underlying.next; i += 1; ans }
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichImmHashSetCanStep[T](private val underlying: collection.immutable.HashSet[T]) extends AnyVal with MakesStepper[T, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntImmHashSet   (underlying.iterator.asInstanceOf[Iterator[Int]],    underlying.size)
    case StepperShape.LongValue   => new StepsLongImmHashSet  (underlying.iterator.asInstanceOf[Iterator[Long]],   underlying.size)
    case StepperShape.DoubleValue => new StepsDoubleImmHashSet(underlying.iterator.asInstanceOf[Iterator[Double]], underlying.size)
    case _            => ss.parUnbox(new StepsAnyImmHashSet[T](underlying.iterator,                                underlying.size))
  }).asInstanceOf[S with EfficientSubstep]
}
