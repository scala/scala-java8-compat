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

final class CollectionCanAccumulate[A](private val underlying: IterableOnce[A]) extends AnyVal {
  def accumulate: Accumulator[A] = underlying.iterator.to(Accumulator)
}

final class AccumulateDoubleCollection(private val underlying: IterableOnce[Double]) extends AnyVal {
  def accumulate: DoubleAccumulator = underlying.iterator.to(DoubleAccumulator)
}

final class AccumulateIntCollection(private val underlying: IterableOnce[Int]) extends AnyVal {
  def accumulate: IntAccumulator = underlying.iterator.to(IntAccumulator)
}

final class AccumulateLongCollection(private val underlying: IterableOnce[Long]) extends AnyVal {
  def accumulate: LongAccumulator = underlying.iterator.to(LongAccumulator)
}

final class AccumulateAnyArray[A](private val underlying: Array[A]) extends AnyVal {
  def accumulate: Accumulator[A] = underlying.to(Accumulator)
}

final class AccumulateDoubleArray(private val underlying: Array[Double]) extends AnyVal {
  def accumulate: DoubleAccumulator = underlying.to(DoubleAccumulator)
}

final class AccumulateIntArray(private val underlying: Array[Int]) extends AnyVal {
  def accumulate: IntAccumulator = underlying.to(IntAccumulator)
}

final class AccumulateLongArray(private val underlying: Array[Long]) extends AnyVal {
  def accumulate: LongAccumulator = underlying.to(LongAccumulator)
}
