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
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

// Steppers for keys (type of HashEntry doesn't matter)

private[java8] class StepsAnyHashTableKey[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLikeGapped[K, StepsAnyHashTableKey[K]](_underlying, _i0, _iN) {
  def next() = if (currentEntry eq null) throwNSEE else { val ans = CollectionInternals.hashEntryKey[K](currentEntry); currentEntry = CollectionInternals.hashEntryNext(currentEntry); ans }
  protected def semiclone(half: Int) = new StepsAnyHashTableKey[K](underlying, i0, half)
}

private[java8] class StepsDoubleHashTableKey(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleHashTableKey](_underlying, _i0, _iN) {
  def nextDouble() = if (currentEntry eq null) throwNSEE else { val ans = CollectionInternals.hashEntryKey[Double](currentEntry); currentEntry = CollectionInternals.hashEntryNext(currentEntry); ans }
  protected def semiclone(half: Int) = new StepsDoubleHashTableKey(underlying, i0, half)
}

private[java8] class StepsIntHashTableKey(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntHashTableKey](_underlying, _i0, _iN) {
  def nextInt() = if (currentEntry eq null) throwNSEE else { val ans = CollectionInternals.hashEntryKey[Int](currentEntry); currentEntry = CollectionInternals.hashEntryNext(currentEntry); ans }
  protected def semiclone(half: Int) = new StepsIntHashTableKey(underlying, i0, half)
}

private[java8] class StepsLongHashTableKey(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongHashTableKey](_underlying, _i0, _iN) {
  def nextLong() = if (currentEntry eq null) throwNSEE else { val ans = CollectionInternals.hashEntryKey[Long](currentEntry); currentEntry = CollectionInternals.hashEntryNext(currentEntry); ans }
  protected def semiclone(half: Int) = new StepsLongHashTableKey(underlying, i0, half)
}

// Steppers for entries stored in DefaultEntry HashEntry
// (both for key-value pair and for values alone)

private[java8] class StepsAnyDefaultHashTable[K, V](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLikeGapped[(K, V), StepsAnyDefaultHashTable[K, V]](_underlying, _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); (CollectionInternals.hashEntryKey[K](e), CollectionInternals.defaultEntryValue[V](e)) }
  protected def semiclone(half: Int) =
    new StepsAnyDefaultHashTable[K, V](underlying, i0, half)
}

private[java8] class StepsAnyDefaultHashTableValue[K, V](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLikeGapped[V, StepsAnyDefaultHashTableValue[K, V]](_underlying, _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.defaultEntryValue[V](e) }
  protected def semiclone(half: Int) =
    new StepsAnyDefaultHashTableValue[K, V](underlying, i0, half)
}

private[java8] class StepsDoubleDefaultHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleDefaultHashTableValue[K]](_underlying, _i0, _iN) {
  def nextDouble() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.defaultEntryValue[Double](e) }
  protected def semiclone(half: Int) =
    new StepsDoubleDefaultHashTableValue[K](underlying, i0, half)
}

private[java8] class StepsIntDefaultHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntDefaultHashTableValue[K]](_underlying, _i0, _iN) {
  def nextInt() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.defaultEntryValue[Int](e) }
  protected def semiclone(half: Int) =
    new StepsIntDefaultHashTableValue[K](underlying, i0, half)
}

private[java8] class StepsLongDefaultHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongDefaultHashTableValue[K]](_underlying, _i0, _iN) {
  def nextLong() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.defaultEntryValue[Long](e) }
  protected def semiclone(half: Int) =
    new StepsLongDefaultHashTableValue[K](underlying, i0, half)
}

// Steppers for entries stored in LinkedEntry HashEntry
// (both for key-value pair and for values alone)

private[java8] class StepsAnyLinkedHashTable[K, V](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLikeGapped[(K, V), StepsAnyLinkedHashTable[K, V]](_underlying, _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); (CollectionInternals.hashEntryKey[K](e), CollectionInternals.linkedEntryValue[V](e)) }
  protected def semiclone(half: Int) =
    new StepsAnyLinkedHashTable[K, V](underlying, i0, half)
}

private[java8] class StepsAnyLinkedHashTableValue[K, V](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLikeGapped[V, StepsAnyLinkedHashTableValue[K, V]](_underlying, _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.linkedEntryValue[V](e) }
  protected def semiclone(half: Int) =
    new StepsAnyLinkedHashTableValue[K, V](underlying, i0, half)
}

private[java8] class StepsDoubleLinkedHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleLinkedHashTableValue[K]](_underlying, _i0, _iN) {
  def nextDouble() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.linkedEntryValue[Double](e) }
  protected def semiclone(half: Int) =
    new StepsDoubleLinkedHashTableValue[K](underlying, i0, half)
}

private[java8] class StepsIntLinkedHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntLinkedHashTableValue[K]](_underlying, _i0, _iN) {
  def nextInt() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.linkedEntryValue[Int](e) }
  protected def semiclone(half: Int) =
    new StepsIntLinkedHashTableValue[K](underlying, i0, half)
}

private[java8] class StepsLongLinkedHashTableValue[K](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongLinkedHashTableValue[K]](_underlying, _i0, _iN) {
  def nextLong() = 
    if (currentEntry eq null) throwNSEE
    else { val e = currentEntry; currentEntry = CollectionInternals.hashEntryNext(e); CollectionInternals.linkedEntryValue[Long](e) }
  protected def semiclone(half: Int) =
    new StepsLongLinkedHashTableValue[K](underlying, i0, half)
}


//////////////////////////
// Value class adapters //
//////////////////////////

// Steppers for entries stored in DefaultEntry HashEntry

final class RichHashMapCanStep[K, V](private val underlying: collection.mutable.HashMap[K, V]) extends AnyVal with MakesKeyValueStepper[K, V, EfficientSubstep] with MakesStepper[(K, V), EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[(K, V), S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    new StepsAnyDefaultHashTable(tbl, 0, tbl.length).asInstanceOf[S with EfficientSubstep]
  }

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntHashTableKey   (tbl, 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongHashTableKey  (tbl, 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleHashTableKey(tbl, 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyHashTableKey   (tbl, 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntDefaultHashTableValue   (tbl, 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongDefaultHashTableValue  (tbl, 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleDefaultHashTableValue(tbl, 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyDefaultHashTableValue   (tbl, 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }
}

// Steppers for entries stored in LinkedEntry HashEntry

final class RichLinkedHashMapCanStep[K, V](private val underlying: collection.mutable.LinkedHashMap[K, V]) extends AnyVal with MakesKeyValueStepper[K, V, EfficientSubstep] with MakesStepper[(K, V), EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[(K, V), S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    new StepsAnyLinkedHashTable(tbl, 0, tbl.length).asInstanceOf[S with EfficientSubstep]
  }

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntHashTableKey   (tbl, 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongHashTableKey  (tbl, 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleHashTableKey(tbl, 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyHashTableKey   (tbl, 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = {
    val tbl = CollectionInternals.getTable[K, V](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntLinkedHashTableValue   (tbl, 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongLinkedHashTableValue  (tbl, 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleLinkedHashTableValue(tbl, 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyLinkedHashTableValue   (tbl, 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }
}
