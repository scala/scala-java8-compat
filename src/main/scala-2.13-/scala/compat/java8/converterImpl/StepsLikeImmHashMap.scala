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

/** Abstracts all the generic operations of stepping over an immutable HashMap by slicing it into pieces.
  * `next` must update `i` but not `i0` so that later splitting steps can keep track of whether the
  * collection needs some sort of modification before transmission to the subclass.
  */
private[java8] trait AbstractStepsLikeImmHashMap[K, V, A, Sub >: Null, Semi >: Null <: Sub with AbstractStepsLikeImmHashMap[K, V, A, Sub, _]]
extends AbstractStepsLikeSliced[collection.immutable.HashMap[K, V], Sub, Semi] {
  protected var theIterator: Iterator[A] = null
  protected def demiclone(u: collection.immutable.HashMap[K,V], j0: Int, jN: Int): Semi
  override def characteristics() = Immutable
  def hasNext(): Boolean = i < iN
  def semiclone(halfHint: Int): Semi = 
    if (i >= iN) null
    else underlying match {
      case trie: collection.immutable.HashMap.HashTrieMap[K, V] =>
        val parts = if (i > i0) trie.drop(i-i0).split else trie.split
        if (parts.length != 2) null
        else {
          val ans = demiclone(parts.head, 0, parts.head.size)
          i = iN - parts.last.size
          underlying = parts.last
          ans.theIterator = theIterator
          theIterator = null
          i0 = i
          ans
        }
      case _ => null
    }
}

private[java8] abstract class StepsLikeImmHashMap[K, V, A, SIHM >: Null <: StepsLikeImmHashMap[K, V, A, _]](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
  extends StepsLikeSliced[A, collection.immutable.HashMap[K, V], SIHM](_underlying, _i0, _iN)
  with AbstractStepsLikeImmHashMap[K, V, A, AnyStepper[A], SIHM]
{}

private[java8] abstract class StepsDoubleLikeImmHashMap[K, V, SIHM >: Null <: StepsDoubleLikeImmHashMap[K, V, SIHM]](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
  extends StepsDoubleLikeSliced[collection.immutable.HashMap[K, V], SIHM](_underlying, _i0, _iN)
  with AbstractStepsLikeImmHashMap[K, V, Double, DoubleStepper, SIHM]
{}

private[java8] abstract class StepsIntLikeImmHashMap[K, V, SIHM >: Null <: StepsIntLikeImmHashMap[K, V, SIHM]](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
  extends StepsIntLikeSliced[collection.immutable.HashMap[K, V], SIHM](_underlying, _i0, _iN)
  with AbstractStepsLikeImmHashMap[K, V, Int, IntStepper, SIHM]
{}

private[java8] abstract class StepsLongLikeImmHashMap[K, V, SIHM >: Null <: StepsLongLikeImmHashMap[K, V, SIHM]](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
  extends StepsLongLikeSliced[collection.immutable.HashMap[K, V], SIHM](_underlying, _i0, _iN)
  with AbstractStepsLikeImmHashMap[K, V, Long, LongStepper, SIHM]
{}
