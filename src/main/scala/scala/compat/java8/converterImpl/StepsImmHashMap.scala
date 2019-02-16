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

import Stepper._

// Note that there are separate implementations for keys, values, and key-value pairs

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyImmHashMap[K, V](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
extends StepsLikeImmHashMap[K, V, (K, V), StepsAnyImmHashMap[K, V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,V], j0: Int, jN: Int) = new StepsAnyImmHashMap[K, V](u, j0, jN)
  def next(): (K, V) = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.iterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsAnyImmHashMapKey[K, V](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
extends StepsLikeImmHashMap[K, V, K, StepsAnyImmHashMapKey[K, V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,V], j0: Int, jN: Int) = new StepsAnyImmHashMapKey[K, V](u, j0, jN)
  def next(): K = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.keysIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsAnyImmHashMapValue[K, V](_underlying: collection.immutable.HashMap[K, V], _i0: Int, _iN: Int)
extends StepsLikeImmHashMap[K, V, V, StepsAnyImmHashMapValue[K, V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,V], j0: Int, jN: Int) = new StepsAnyImmHashMapValue[K, V](u, j0, jN)
  def next(): V = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.valuesIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsDoubleImmHashMapKey[V](_underlying: collection.immutable.HashMap[Double, V], _i0: Int, _iN: Int)
extends StepsDoubleLikeImmHashMap[Double, V, StepsDoubleImmHashMapKey[V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[Double,V], j0: Int, jN: Int) = new StepsDoubleImmHashMapKey[V](u, j0, jN)
  def nextDouble() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.keysIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsDoubleImmHashMapValue[K](_underlying: collection.immutable.HashMap[K, Double], _i0: Int, _iN: Int)
extends StepsDoubleLikeImmHashMap[K, Double, StepsDoubleImmHashMapValue[K]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,Double], j0: Int, jN: Int) = new StepsDoubleImmHashMapValue[K](u, j0, jN)
  def nextDouble() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.valuesIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsIntImmHashMapKey[V](_underlying: collection.immutable.HashMap[Int, V], _i0: Int, _iN: Int)
extends StepsIntLikeImmHashMap[Int, V, StepsIntImmHashMapKey[V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[Int,V], j0: Int, jN: Int) = new StepsIntImmHashMapKey[V](u, j0, jN)
  def nextInt() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.keysIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsIntImmHashMapValue[K](_underlying: collection.immutable.HashMap[K, Int], _i0: Int, _iN: Int)
extends StepsIntLikeImmHashMap[K, Int, StepsIntImmHashMapValue[K]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,Int], j0: Int, jN: Int) = new StepsIntImmHashMapValue[K](u, j0, jN)
  def nextInt() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.valuesIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsLongImmHashMapKey[V](_underlying: collection.immutable.HashMap[Long, V], _i0: Int, _iN: Int)
extends StepsLongLikeImmHashMap[Long, V, StepsLongImmHashMapKey[V]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[Long,V], j0: Int, jN: Int) = new StepsLongImmHashMapKey[V](u, j0, jN)
  def nextLong() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.keysIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

private[java8] class StepsLongImmHashMapValue[K](_underlying: collection.immutable.HashMap[K, Long], _i0: Int, _iN: Int)
extends StepsLongLikeImmHashMap[K, Long, StepsLongImmHashMapValue[K]](_underlying, _i0, _iN) {
  protected def demiclone(u: collection.immutable.HashMap[K,Long], j0: Int, jN: Int) = new StepsLongImmHashMapValue[K](u, j0, jN)
  def nextLong() = 
    if (hasNext) { 
      if (theIterator eq null) theIterator = underlying.valuesIterator
      i += 1
      theIterator.next
    }
    else throwNSEE  
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichImmHashMapCanStep[K, V](private val underlying: collection.immutable.HashMap[K, V]) extends AnyVal with MakesKeyValueStepper[K, V, EfficientSubstep] with MakesStepper[(K, V), EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[(K, V), S]) =
    new StepsAnyImmHashMap[K, V](underlying, 0, underlying.size).asInstanceOf[S with EfficientSubstep]

  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntImmHashMapKey      (underlying.asInstanceOf[collection.immutable.HashMap[Int, V]],    0, underlying.size)
    case StepperShape.LongValue   => new StepsLongImmHashMapKey     (underlying.asInstanceOf[collection.immutable.HashMap[Long, V]],   0, underlying.size)
    case StepperShape.DoubleValue => new StepsDoubleImmHashMapKey   (underlying.asInstanceOf[collection.immutable.HashMap[Double, V]], 0, underlying.size)
    case _            => ss.parUnbox(new StepsAnyImmHashMapKey[K, V](underlying,                                                       0, underlying.size))
  }).asInstanceOf[S with EfficientSubstep]

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntImmHashMapValue      (underlying.asInstanceOf[collection.immutable.HashMap[K, Int]],    0, underlying.size)
    case StepperShape.LongValue   => new StepsLongImmHashMapValue     (underlying.asInstanceOf[collection.immutable.HashMap[K, Long]],   0, underlying.size)
    case StepperShape.DoubleValue => new StepsDoubleImmHashMapValue   (underlying.asInstanceOf[collection.immutable.HashMap[K, Double]], 0, underlying.size)
    case _            => ss.parUnbox(new StepsAnyImmHashMapValue[K, V](underlying,                                                       0, underlying.size))
  }).asInstanceOf[S with EfficientSubstep]
}
