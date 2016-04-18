package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

// Generic maps defer to the iterator steppers if a more precise type cannot be found via pattern matching
// TODO: implement pattern matching

final class RichMapCanStep[K, V](private val underlying: collection.Map[K, V]) extends AnyVal with MakesKeyValueSeqStepper[K, V] {
  // No generic stepper because RichIterableCanStep will get that anyway, and we don't pattern match here

  override def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = (ss match {
    case ss if ss.ref             => new StepsAnyIterator   (underlying.keysIterator)
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.keysIterator.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.keysIterator.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.keysIterator.asInstanceOf[Iterator[Double]])
    case ss                       => super.keyStepper(ss)
  }).asInstanceOf[S]

  override def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = (ss match {
    case ss if ss.ref             => new StepsAnyIterator   (underlying.valuesIterator)
    case StepperShape.IntValue    => new StepsIntIterator   (underlying.valuesIterator.asInstanceOf[Iterator[Int]])
    case StepperShape.LongValue   => new StepsLongIterator  (underlying.valuesIterator.asInstanceOf[Iterator[Long]])
    case StepperShape.DoubleValue => new StepsDoubleIterator(underlying.valuesIterator.asInstanceOf[Iterator[Double]])
    case ss                       => super.valueStepper(ss)
  }).asInstanceOf[S]
}
