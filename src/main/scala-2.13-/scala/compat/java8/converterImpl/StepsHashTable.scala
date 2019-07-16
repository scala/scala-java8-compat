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

private[java8] class StepsAnyHashTableKey[K](_underlying: Array[collection.mutable.HashEntry[K, _]], _i0: Int, _iN: Int)
extends StepsLikeGapped[K, StepsAnyHashTableKey[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[collection.mutable.HashEntry[K, _]].key; currentEntry = currentEntry.asInstanceOf[collection.mutable.HashEntry[K, _]].next.asInstanceOf[AnyRef]; ans }
  def semiclone(half: Int) = new StepsAnyHashTableKey[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, _]]], i0, half)
}

private[java8] class StepsDoubleHashTableKey(_underlying: Array[collection.mutable.HashEntry[Double, _]], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleHashTableKey](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextDouble() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[collection.mutable.HashEntry[Double, _]].key; currentEntry = currentEntry.asInstanceOf[collection.mutable.HashEntry[Double, _]].next.asInstanceOf[AnyRef]; ans }
  def semiclone(half: Int) = new StepsDoubleHashTableKey(underlying.asInstanceOf[Array[collection.mutable.HashEntry[Double, _]]], i0, half)
}

private[java8] class StepsIntHashTableKey(_underlying: Array[collection.mutable.HashEntry[Int, _]], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntHashTableKey](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextInt() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[collection.mutable.HashEntry[Int, _]].key; currentEntry = currentEntry.asInstanceOf[collection.mutable.HashEntry[Int, _]].next.asInstanceOf[AnyRef]; ans }
  def semiclone(half: Int) = new StepsIntHashTableKey(underlying.asInstanceOf[Array[collection.mutable.HashEntry[Int, _]]], i0, half)
}

private[java8] class StepsLongHashTableKey(_underlying: Array[collection.mutable.HashEntry[Long, _]], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongHashTableKey](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextLong() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[collection.mutable.HashEntry[Long, _]].key; currentEntry = currentEntry.asInstanceOf[collection.mutable.HashEntry[Long, _]].next.asInstanceOf[AnyRef]; ans }
  def semiclone(half: Int) = new StepsLongHashTableKey(underlying.asInstanceOf[Array[collection.mutable.HashEntry[Long, _]]], i0, half)
}

// Steppers for entries stored in DefaultEntry HashEntry
// (both for key-value pair and for values alone)

private[java8] class StepsAnyDefaultHashTable[K, V](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, V]]], _i0: Int, _iN: Int)
extends StepsLikeGapped[(K, V), StepsAnyDefaultHashTable[K, V]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.DefaultEntry[K, V]]; currentEntry = de.next; (de.key, de.value) }
  def semiclone(half: Int) =
    new StepsAnyDefaultHashTable[K, V](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, V]]]], i0, half)
}

private[java8] class StepsAnyDefaultHashTableValue[K, V](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, V]]], _i0: Int, _iN: Int)
extends StepsLikeGapped[V, StepsAnyDefaultHashTableValue[K, V]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.DefaultEntry[K, V]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsAnyDefaultHashTableValue[K, V](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, V]]]], i0, half)
}

private[java8] class StepsDoubleDefaultHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Double]]], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleDefaultHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextDouble() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.DefaultEntry[K, Double]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsDoubleDefaultHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Double]]]], i0, half)
}

private[java8] class StepsIntDefaultHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Int]]], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntDefaultHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextInt() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.DefaultEntry[K, Int]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsIntDefaultHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Int]]]], i0, half)
}

private[java8] class StepsLongDefaultHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Long]]], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongDefaultHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextLong() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.DefaultEntry[K, Long]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsLongDefaultHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Long]]]], i0, half)
}

// Steppers for entries stored in LinkedEntry HashEntry
// (both for key-value pair and for values alone)

private[java8] class StepsAnyLinkedHashTable[K, V](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, V]]], _i0: Int, _iN: Int)
extends StepsLikeGapped[(K, V), StepsAnyLinkedHashTable[K, V]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.LinkedEntry[K, V]]; currentEntry = de.next; (de.key, de.value) }
  def semiclone(half: Int) =
    new StepsAnyLinkedHashTable[K, V](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, V]]]], i0, half)
}

private[java8] class StepsAnyLinkedHashTableValue[K, V](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, V]]], _i0: Int, _iN: Int)
extends StepsLikeGapped[V, StepsAnyLinkedHashTableValue[K, V]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.LinkedEntry[K, V]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsAnyLinkedHashTableValue[K, V](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, V]]]], i0, half)
}

private[java8] class StepsDoubleLinkedHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Double]]], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleLinkedHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextDouble() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.LinkedEntry[K, Double]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsDoubleLinkedHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Double]]]], i0, half)
}

private[java8] class StepsIntLinkedHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Int]]], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntLinkedHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextInt() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.LinkedEntry[K, Int]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsIntLinkedHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Int]]]], i0, half)
}

private[java8] class StepsLongLinkedHashTableValue[K](_underlying: Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Long]]], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongLinkedHashTableValue[K]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextLong() = 
    if (currentEntry eq null) throwNSEE
    else { val de = currentEntry.asInstanceOf[collection.mutable.LinkedEntry[K, Long]]; currentEntry = de.next; de.value }
  def semiclone(half: Int) =
    new StepsLongLinkedHashTableValue[K](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Long]]]], i0, half)
}


//////////////////////////
// Value class adapters //
//////////////////////////

// Steppers for entries stored in DefaultEntry HashEntry

final class RichDefaultHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]]) extends AnyVal with MakesKeyValueStepper[K, V, EfficientSubstep] with MakesStepper[(K, V), EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[(K, V), S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
    new StepsAnyDefaultHashTable(tbl, 0, tbl.length).asInstanceOf[S with EfficientSubstep]
  }

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntHashTableKey   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[Int,    _]]], 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongHashTableKey  (tbl.asInstanceOf[Array[collection.mutable.HashEntry[Long,   _]]], 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleHashTableKey(tbl.asInstanceOf[Array[collection.mutable.HashEntry[Double, _]]], 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyHashTableKey   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K,      _]]], 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntDefaultHashTableValue   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Int   ]]]], 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongDefaultHashTableValue  (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Long  ]]]], 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleDefaultHashTableValue(tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.DefaultEntry[K, Double]]]], 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyDefaultHashTableValue   (tbl,                                                                                                  0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }
}

// Steppers for entries stored in LinkedEntry HashEntry

final class RichLinkedHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]]) extends AnyVal with MakesKeyValueStepper[K, V, EfficientSubstep] with MakesStepper[(K, V), EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[(K, V), S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
    new StepsAnyLinkedHashTable(tbl, 0, tbl.length).asInstanceOf[S with EfficientSubstep]
  }

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntHashTableKey   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[Int,    _]]], 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongHashTableKey  (tbl.asInstanceOf[Array[collection.mutable.HashEntry[Long,   _]]], 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleHashTableKey(tbl.asInstanceOf[Array[collection.mutable.HashEntry[Double, _]]], 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyHashTableKey   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K,      _]]], 0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
    ((ss.shape: @switch) match {
      case StepperShape.IntValue    => new StepsIntLinkedHashTableValue   (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Int   ]]]], 0, tbl.length)
      case StepperShape.LongValue   => new StepsLongLinkedHashTableValue  (tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Long  ]]]], 0, tbl.length)
      case StepperShape.DoubleValue => new StepsDoubleLinkedHashTableValue(tbl.asInstanceOf[Array[collection.mutable.HashEntry[K, collection.mutable.LinkedEntry[K, Double]]]], 0, tbl.length)
      case _            => ss.parUnbox(new StepsAnyLinkedHashTableValue   (tbl,                                                                                                  0, tbl.length))
    }).asInstanceOf[S with EfficientSubstep]
  }
}
