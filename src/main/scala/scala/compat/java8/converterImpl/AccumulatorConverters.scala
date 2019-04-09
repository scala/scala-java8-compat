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

import scala.language.implicitConversions

trait Priority3AccumulatorConverters {
  implicit def collectionCanAccumulate[A](underlying: IterableOnce[A]) = new CollectionCanAccumulate[A](underlying)
}

trait Priority2AccumulatorConverters extends Priority3AccumulatorConverters {
  implicit def accumulateDoubleCollection(underlying: IterableOnce[Double]) = new AccumulateDoubleCollection(underlying)
  implicit def accumulateIntCollection(underlying: IterableOnce[Int]) = new AccumulateIntCollection(underlying)
  implicit def accumulateLongCollection(underlying: IterableOnce[Long]) = new AccumulateLongCollection(underlying)
  implicit def accumulateAnyArray[A](underlying: Array[A]) = new AccumulateAnyArray(underlying)
}

trait Priority1AccumulatorConverters extends Priority2AccumulatorConverters {
  implicit def accumulateDoubleArray(underlying: Array[Double]) = new AccumulateDoubleArray(underlying)
  implicit def accumulateIntArray(underlying: Array[Int]) = new AccumulateIntArray(underlying)
  implicit def accumulateLongArray(underlying: Array[Long]) = new AccumulateLongArray(underlying)
}
