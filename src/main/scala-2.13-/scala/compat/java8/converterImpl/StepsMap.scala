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

import scala.annotation.switch

import scala.compat.java8.collectionImpl._

// Generic maps defer to the iterator steppers if a more precise type cannot be found via pattern matching
// TODO: implement pattern matching

final class RichMapCanStep[K, V](private val underlying: collection.Map[K, V]) extends AnyVal with MakesKeyValueStepper[K, V, Any] {
  // No generic stepper because RichIterableCanStep will get that anyway, and we don't pattern match here

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.keysIterator.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.keysIterator.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.keysIterator.asInstanceOf[Iterator[Double]])
    case _            => ss.seqUnbox(new StepsAnyIterator   (underlying.keysIterator))
  }).asInstanceOf[S]

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.valuesIterator.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.valuesIterator.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.valuesIterator.asInstanceOf[Iterator[Double]])
    case _            => ss.seqUnbox(new StepsAnyIterator   (underlying.valuesIterator))
  }).asInstanceOf[S]
}
