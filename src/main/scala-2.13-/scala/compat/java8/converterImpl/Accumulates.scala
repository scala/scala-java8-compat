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

trait AccumulatesFromStepper[@specialized(Double, Int, Long) A, Acc <: AccumulatorLike[A, Acc]] {
  def apply(stepper: Stepper[A]): Acc
}

final class CollectionCanAccumulate[A](private val underlying: TraversableOnce[A]) extends AnyVal {
  def accumulate: Accumulator[A] = {
    val a = new Accumulator[A]
    underlying.foreach(a += _)
    a
  }
}

final class AccumulateDoubleCollection(private val underlying: TraversableOnce[Double]) extends AnyVal {
  def accumulate: DoubleAccumulator = {
    val da = new DoubleAccumulator
    underlying.foreach(da += _)
    da
  }
}

final class AccumulateIntCollection(private val underlying: TraversableOnce[Int]) extends AnyVal {
  def accumulate: IntAccumulator = {
    val da = new IntAccumulator
    underlying.foreach(da += _)
    da
  }
}

final class AccumulateLongCollection(private val underlying: TraversableOnce[Long]) extends AnyVal {
  def accumulate: LongAccumulator = {
    val da = new LongAccumulator
    underlying.foreach(da += _)
    da
  }
}

final class AccumulateAnyArray[A](private val underlying: Array[A]) extends AnyVal {
  def accumulate: Accumulator[A] = {
    val a = new Accumulator[A]
    var i = 0
    while (i < underlying.length) { a += underlying(i); i += 1 }
    a
  }
}

final class AccumulateDoubleArray(private val underlying: Array[Double]) extends AnyVal {
  def accumulate: DoubleAccumulator = {
    val da = new DoubleAccumulator
    var i = 0
    while (i < underlying.length) { da += underlying(i); i += 1 }
    da
  }
}

final class AccumulateIntArray(private val underlying: Array[Int]) extends AnyVal {
  def accumulate: IntAccumulator = {
    val da = new IntAccumulator
    var i = 0
    while (i < underlying.length) { da += underlying(i); i += 1 }
    da
  }
}

final class AccumulateLongArray(private val underlying: Array[Long]) extends AnyVal {
  def accumulate: LongAccumulator = {
    val da = new LongAccumulator
    var i = 0
    while (i < underlying.length) { da += underlying(i); i += 1 }
    da
  }
}
