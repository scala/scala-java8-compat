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

package scala.compat.java8.collectionImpl

/** An accumulator that works with Java 8 streams; it accepts elements of type `A`,
  * is itself an `AC`.  Accumulators can handle more than `Int.MaxValue` elements.
  */
trait AccumulatorLike[@specialized(Double, Int, Long) A, AC] {
  private[java8] var index: Int = 0
  private[java8] var hIndex: Int = 0
  private[java8] var totalSize: Long = 0L
  private[java8] def cumulative(i: Int): Long
  
  private[java8] def nextBlockSize: Int = {
    if (totalSize < 32) 16
    else if (totalSize <= Int.MaxValue) {
      val bit = (64 - java.lang.Long.numberOfLeadingZeros(totalSize))
      1 << (bit - (bit >> 2))
    }
    else 1 << 24
  }
  
  /** Size of the accumulated collection, as a `Long` */
  final def size = totalSize
  
  /** Remove all accumulated elements from this accumulator. */
  def clear(): Unit = {
    index = 0
    hIndex = 0
    totalSize = 0L
  }

  private[java8] def seekSlot(ix: Long): Long = {
    var lo = -1
    var hi = hIndex
    while (lo + 1 < hi) {
      val m = (lo + hi) >>> 1    // Shift allows division-as-unsigned, prevents overflow
      if (cumulative(m) > ix) hi = m
      else lo = m
    }
    (hi.toLong << 32) | (if (hi==0) ix else (ix - cumulative(hi-1))).toInt
  }
}
