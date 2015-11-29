package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

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

