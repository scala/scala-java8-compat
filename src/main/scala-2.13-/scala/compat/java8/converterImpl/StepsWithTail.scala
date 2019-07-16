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

/** Abstracts all the generic operations of stepping over a collection with a fast tail operation.
  * Because of how Java 8 streams subdivide their spliterators, we do this by chunking a bit at
  * a time, generating a long chain of chunks.  These won't necessarily all be the same size,
  * but if there are enough hopefully it won't matter.
  *
  * Subclasses MUST decrement `maxN` when consuming elements, or this will not work!
  */
private[java8] abstract class AbstractStepsWithTail[CC >: Null, Sub >: Null, Semi <: Sub](final protected var underlying: CC, final protected var maxN: Long) {
  private var nextChunkSize: Int = 0
  protected def myIsEmpty(cc: CC): Boolean
  protected def myTailOf(cc: CC): CC
  def prepareParallelOperation(): this.type = {
    if (maxN >= Int.MaxValue && nextChunkSize == 0) {
      // Need parallel context to know whether to run this or not!
      var u = underlying
      var i = 0
      while (i < 1024 && !myIsEmpty(u)) { u = myTailOf(u); i += 1 }
      if (i < 1024) maxN = i
      else nextChunkSize = 64   // Guaranteed at least 16 chunks
    }
    this
  }
  def semiclone(chunk: Int): Semi
  def characteristics(): Int = if (maxN < Int.MaxValue) Ordered | Sized | SubSized else Ordered
  def estimateSize(): Long = if (maxN < Int.MaxValue) maxN else Long.MaxValue
  def hasNext(): Boolean = if (maxN < Int.MaxValue) maxN > 0 else if (myIsEmpty(underlying)) { maxN = 0; false } else true
  def substep(): Sub = {
    prepareParallelOperation()
    maxN match {
      case x if x < 2 => null
      case x if x >= Int.MaxValue =>
        var u = underlying
        var i = 0
        val n = (nextChunkSize & 0xFFFFFFFC)  // Use bottom two bits to count up
        while (i < n && !myIsEmpty(u)) {
          u = myTailOf(u)
          i += 1
        }
        if (myIsEmpty(u)) {
          maxN = i
          substep()  // Different branch now, recursion is an easy way to get it
        }
        else {
          val sub = semiclone(n)
          underlying = u
          if ((nextChunkSize & 3) == 3) nextChunkSize = if (n < 0x40000000) 2*n else n else nextChunkSize += 1
          sub
        }
      case x =>
        var half = x.toInt >>> 1
        val sub = semiclone(half)
        maxN -= half
        while (half > 0) { underlying = myTailOf(underlying); half -= 1 }
        sub
    }
  }
}

/** Abstracts the operation of stepping over a generic indexable collection */
private[java8] abstract class StepsWithTail[A, CC >: Null, STA >: Null <: StepsWithTail[A, CC, _]](_underlying: CC, _maxN: Long)
  extends AbstractStepsWithTail[CC, AnyStepper[A], STA](_underlying, _maxN)
  with AnyStepper[A]
{}

/** Abstracts the operation of stepping over an indexable collection of Doubles */
private[java8] abstract class StepsDoubleWithTail[CC >: Null, STD >: Null <: StepsDoubleWithTail[CC, _]](_underlying: CC, _maxN: Long)
  extends AbstractStepsWithTail[CC, DoubleStepper, STD](_underlying, _maxN)
  with DoubleStepper
{}

/** Abstracts the operation of stepping over an indexable collection of Ints */
private[java8] abstract class StepsIntWithTail[CC >: Null, STI >: Null <: StepsIntWithTail[CC, _]](_underlying: CC, _maxN: Long)
  extends AbstractStepsWithTail[CC, IntStepper, STI](_underlying, _maxN)
  with IntStepper
{}

/** Abstracts the operation of stepping over an indexable collection of Longs */
private[java8] abstract class StepsLongWithTail[CC >: Null, STL >: Null <: StepsLongWithTail[CC, _]](_underlying: CC, _maxN: Long)
  extends AbstractStepsWithTail[CC, LongStepper, STL](_underlying, _maxN)
  with LongStepper
{}
