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

import language.implicitConversions

import scala.compat.java8.collectionImpl._

trait Priority3AccumulatorConverters {
  implicit def collectionCanAccumulate[A](underlying: TraversableOnce[A]) = new CollectionCanAccumulate[A](underlying)
}

trait Priority2AccumulatorConverters extends Priority3AccumulatorConverters {
  implicit def accumulateDoubleCollection(underlying: TraversableOnce[Double]) = new AccumulateDoubleCollection(underlying)
  implicit def accumulateIntCollection(underlying: TraversableOnce[Int]) = new AccumulateIntCollection(underlying)
  implicit def accumulateLongCollection(underlying: TraversableOnce[Long]) = new AccumulateLongCollection(underlying)
  implicit def accumulateAnyArray[A](underlying: Array[A]) = new AccumulateAnyArray(underlying)
}

trait Priority1AccumulatorConverters extends Priority2AccumulatorConverters {
  implicit def accumulateDoubleArray(underlying: Array[Double]) = new AccumulateDoubleArray(underlying)
  implicit def accumulateIntArray(underlying: Array[Int]) = new AccumulateIntArray(underlying)
  implicit def accumulateLongArray(underlying: Array[Long]) = new AccumulateLongArray(underlying)

  implicit def accumulateAnyStepper[A]: AccumulatesFromStepper[A, Accumulator[A]] =
    PrivateAccumulatorConverters.genericAccumulateAnyStepper.asInstanceOf[AccumulatesFromStepper[A, Accumulator[A]]]
}

private[java8] object PrivateAccumulatorConverters {
  val genericAccumulateAnyStepper: AccumulatesFromStepper[Any, Accumulator[Any]] = new AccumulatesFromStepper[Any, Accumulator[Any]] {
    def apply(stepper: Stepper[Any]) = {
      val a = new Accumulator[Any]
      while (stepper.hasStep) a += stepper.nextStep
      a
    }
  }
}
