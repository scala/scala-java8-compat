package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

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

