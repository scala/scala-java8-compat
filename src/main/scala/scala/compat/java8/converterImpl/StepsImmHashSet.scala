package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

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

final class RichImmHashSetCanStep[A](private val underlying: collection.immutable.HashSet[A]) extends AnyVal with MakesStepper[AnyStepper[A] with EfficientSubstep] {
  @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyImmHashSet(underlying.iterator, underlying.size)
}

final class RichDoubleHashSetCanStep(private val underlying: collection.immutable.HashSet[Double]) extends AnyVal with MakesStepper[DoubleStepper with EfficientSubstep] {
  @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashSet(underlying.iterator, underlying.size)
}

final class RichIntHashSetCanStep(private val underlying: collection.immutable.HashSet[Int]) extends AnyVal with MakesStepper[IntStepper with EfficientSubstep] {
  @inline def stepper: IntStepper with EfficientSubstep = new StepsIntImmHashSet(underlying.iterator, underlying.size)
}

final class RichLongHashSetCanStep(private val underlying: collection.immutable.HashSet[Long]) extends AnyVal with MakesStepper[LongStepper with EfficientSubstep] {
  @inline def stepper: LongStepper with EfficientSubstep = new StepsLongImmHashSet(underlying.iterator, underlying.size)
}
