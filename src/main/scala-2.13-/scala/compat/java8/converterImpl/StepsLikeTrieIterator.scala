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

/** Abstracts all the generic operations of stepping over a TrieIterator by asking itself to
  * slice itself into pieces.  Note that `i` must be kept up to date in subclasses.
  */
private[java8] trait AbstractStepsLikeTrieIterator[A, Sub >: Null, Semi >: Null <: Sub with AbstractStepsLikeTrieIterator[A, Sub, _]]
extends AbstractStepsLikeSliced[Iterator[A], Sub, Semi] {
  protected def demiclone(it: Iterator[A], N: Int): Semi
  override def characteristics() = Immutable
  def hasNext(): Boolean = underlying.hasNext
  def semiclone(halfHint: Int): Semi = 
    if (!underlying.hasNext || i > iN-2) null
    else scala.compat.java8.runtime.CollectionInternals.trieIteratorSplit(underlying) match {
      case null => null
      case ((pre: Iterator[A], pno), post: Iterator[A]) =>
        val pn = (pno: Any) match { case i: Int => i; case _ => throw new Exception("Unexpected type") }
        val ans = demiclone(pre, pn)
        i += pn
        underlying = post
        i0 = i
        ans
      case _ => null
    }
}

private[java8] abstract class StepsLikeTrieIterator[A, STI >: Null <: StepsLikeTrieIterator[A, _]](_underlying: Iterator[A], _N: Int)
  extends StepsLikeSliced[A, Iterator[A], STI](_underlying, 0, _N)
  with AbstractStepsLikeTrieIterator[A, AnyStepper[A], STI]
{}

private[java8] abstract class StepsDoubleLikeTrieIterator[STI >: Null <: StepsDoubleLikeTrieIterator[STI]](_underlying: Iterator[Double], _N: Int)
  extends StepsDoubleLikeSliced[Iterator[Double], STI](_underlying, 0, _N)
  with AbstractStepsLikeTrieIterator[Double, DoubleStepper, STI]
{}

private[java8] abstract class StepsIntLikeTrieIterator[STI >: Null <: StepsIntLikeTrieIterator[STI]](_underlying: Iterator[Int], _N: Int)
  extends StepsIntLikeSliced[Iterator[Int], STI](_underlying, 0, _N)
  with AbstractStepsLikeTrieIterator[Int, IntStepper, STI]
{}

private[java8] abstract class StepsLongLikeTrieIterator[STI >: Null <: StepsLongLikeTrieIterator[STI]](_underlying: Iterator[Long], _N: Int)
  extends StepsLongLikeSliced[Iterator[Long], STI](_underlying, 0, _N)
  with AbstractStepsLikeTrieIterator[Long, LongStepper, STI]
{}
