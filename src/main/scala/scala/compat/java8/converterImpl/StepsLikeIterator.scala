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

/** Common functionality for Steppers that step through an Iterator, caching the results as needed when a split is requested. */
private[java8] abstract class AbstractStepsLikeIterator[A, SP >: Null <: Stepper[A], Semi <: SP](final protected var underlying: Iterator[A]) {
  final protected var nextChunkSize = 16
  final protected var proxied: SP = null
  def semiclone(): Semi        // Must initialize with null iterator!
  def characteristics(): Int = if (proxied ne null) Ordered | Sized | SubSized else Ordered
  def estimateSize(): Long = if (proxied ne null) proxied.knownSize else Long.MaxValue
  def hasNext(): Boolean = if (proxied ne null) proxied.hasStep else underlying.hasNext
}

/** Abstracts the operation of stepping over an iterator (that needs to be cached when splitting) */
private[java8] abstract class StepsLikeIterator[A, SLI >: Null <: StepsLikeIterator[A, SLI] with AnyStepper[A]](_underlying: Iterator[A])
  extends AbstractStepsLikeIterator[A, AnyStepper[A], SLI](_underlying)
  with AnyStepper[A]
{
  override def substep(): AnyStepper[A] = if (proxied ne null) proxied.substep else {
    val acc = new Accumulator[A]
    var i = 0
    val n = (nextChunkSize & 0xFFFFFFFC)
    while (i < n && underlying.hasNext) { acc += underlying.next; i += 1 }
    if (i < n || !underlying.hasNext) {
      proxied = acc.stepper
      proxied.substep
    }
    else {
      val ans = semiclone()
      ans.proxied = acc.stepper
      nextChunkSize = if ((nextChunkSize&3) == 3) { if (n < 0x40000000) n*2 else n } else nextChunkSize + 1
      ans
    }
  }
}

/** Abstracts the operation of stepping over an iterator of Doubles (needs caching when split) */
private[java8] abstract class StepsDoubleLikeIterator[SLI >: Null <: StepsDoubleLikeIterator[SLI] with DoubleStepper](_underlying: Iterator[Double])
  extends AbstractStepsLikeIterator[Double, DoubleStepper, SLI](_underlying)
  with DoubleStepper
{
  override def substep(): DoubleStepper = if (proxied ne null) proxied.substep else {
    val acc = new DoubleAccumulator
    var i = 0
    val n = (nextChunkSize & 0xFFFFFFFC)
    while (i < n && underlying.hasNext) { acc += underlying.next; i += 1 }
    if (i < n || !underlying.hasNext) {
      proxied = acc.stepper
      proxied.substep
    }
    else {
      val ans = semiclone()
      ans.proxied = acc.stepper
      nextChunkSize = if ((nextChunkSize&3) == 3) { if (n < 0x40000000) n*2 else n } else nextChunkSize + 1
      ans
    }
  }
}

/** Abstracts the operation of stepping over an iterator of Ints (needs caching when split) */
private[java8] abstract class StepsIntLikeIterator[SLI >: Null <: StepsIntLikeIterator[SLI] with IntStepper](_underlying: Iterator[Int])
  extends AbstractStepsLikeIterator[Int, IntStepper, SLI](_underlying)
  with IntStepper
{
  override def substep(): IntStepper = if (proxied ne null) proxied.substep else {
    val acc = new IntAccumulator
    var i = 0
    val n = (nextChunkSize & 0xFFFFFFFC)
    while (i < n && underlying.hasNext) { acc += underlying.next; i += 1 }
    if (i < n || !underlying.hasNext) {
      proxied = acc.stepper
      proxied.substep
    }
    else {
      val ans = semiclone()
      ans.proxied = acc.stepper
      nextChunkSize = if ((nextChunkSize&3) == 3) { if (n < 0x40000000) n*2 else n } else nextChunkSize + 1
      ans
    }
  }
}

/** Abstracts the operation of stepping over an iterator of Longs (needs caching when split) */
private[java8] abstract class StepsLongLikeIterator[SLI >: Null <: StepsLongLikeIterator[SLI] with LongStepper](_underlying: Iterator[Long])
  extends AbstractStepsLikeIterator[Long, LongStepper, SLI](_underlying)
  with LongStepper
{
  override def substep: LongStepper = if (proxied ne null) proxied.substep else {
    val acc = new LongAccumulator
    var i = 0
    val n = (nextChunkSize & 0xFFFFFFFC)
    while (i < n && underlying.hasNext) { acc += underlying.next; i += 1 }
    if (i < n || !underlying.hasNext) {
      proxied = acc.stepper
      proxied.substep
    }
    else {
      val ans = semiclone()
      ans.proxied = acc.stepper
      nextChunkSize = if ((nextChunkSize&3) == 3) { if (n < 0x40000000) n*2 else n } else nextChunkSize + 1
      ans
    }
  }
}
