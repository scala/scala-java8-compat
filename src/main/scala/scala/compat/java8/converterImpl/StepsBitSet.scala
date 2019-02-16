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
import scala.compat.java8.runtime._

import Stepper._

////////////////////////////
// Stepper implementation //
////////////////////////////

private[java8] class StepsIntBitSet(_underlying: Array[Long], _i0: Int, _iN: Int)
extends StepsIntLikeSliced[Array[Long], StepsIntBitSet](_underlying, _i0, _iN) {
  private var mask: Long = (-1L) << (i & 0x3F)
  private var cache: Long = underlying(i >>> 6)
  private var found: Boolean = false
  def semiclone(half: Int) = {
    val ans = new StepsIntBitSet(underlying, i, half)
    i = half
    mask = (-1L) << (i & 0x3F)
    cache = underlying(i >>> 6)
    found = false
    ans
  }
  def hasNext(): Boolean = found || ((i < iN) && {
    while ((mask & cache) == 0) {
      i += java.lang.Long.numberOfLeadingZeros(~mask)
      if (i < 0 || i >= iN) { i = iN; return false }
      mask = -1L
      cache = underlying(i >>> 6)
    }
    var m = mask << 1
    while ((mask & cache) == (m & cache)) {
      mask = m
      m = mask << 1
      i += 1
    }
    if (i < 0 || i >= iN) {
      i = iN
      false
    }
    else {
      found = true
      true
    }
  })
  def nextInt() = if (hasNext) { val j = i; found = false; mask = mask << 1; i += 1; j } else throwNSEE
}

/////////////////////////
// Value class adapter //
/////////////////////////

final class RichBitSetCanStep(private val underlying: collection.BitSet) extends AnyVal with MakesStepper[Int, EfficientSubstep] {
  override def stepper[S <: Stepper[_]](implicit ss: StepperShape[Int, S]) = {
    val bits: Array[Long] = underlying match {
      case m: collection.mutable.BitSet => CollectionInternals.getBitSetInternals(m)
      case n: collection.immutable.BitSet.BitSetN => RichBitSetCanStep.reflectInternalsN(n)
      case x => x.toBitMask
    }
    new StepsIntBitSet(bits, 0, math.min(bits.length*64L, Int.MaxValue).toInt).asInstanceOf[S with EfficientSubstep]
  }
}

private[java8] object RichBitSetCanStep {
  private val reflector = classOf[collection.immutable.BitSet.BitSetN].getMethod("elems")
  def reflectInternalsN(bsn: collection.immutable.BitSet.BitSetN): Array[Long] = reflector.invoke(bsn).asInstanceOf[Array[Long]]
}

