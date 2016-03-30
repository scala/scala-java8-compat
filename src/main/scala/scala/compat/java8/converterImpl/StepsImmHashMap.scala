package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

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

final class RichImmHashMapCanStep[K, V](private val underlying: collection.immutable.HashMap[K, V])
extends AnyVal with MakesStepper[AnyStepper[(K, V)] with EfficientSubstep] with MakesKeyStepper[AnyStepper[K] with EfficientSubstep] with MakesValueStepper[AnyStepper[V] with EfficientSubstep] {
  @inline def stepper: AnyStepper[(K, V)] with EfficientSubstep = new StepsAnyImmHashMap[K, V](underlying, 0, underlying.size)
  @inline def keyStepper: AnyStepper[K] with EfficientSubstep = new StepsAnyImmHashMapKey[K, V](underlying, 0, underlying.size)
  @inline def valueStepper: AnyStepper[V] with EfficientSubstep = new StepsAnyImmHashMapValue[K, V](underlying, 0, underlying.size)
}

final class RichImmHashMapDoubleKeyCanStep[V](private val underlying: collection.immutable.HashMap[Double, V]) extends AnyVal with MakesKeyStepper[DoubleStepper with EfficientSubstep] {
  @inline def keyStepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashMapKey[V](underlying, 0, underlying.size)
}

final class RichImmHashMapDoubleValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Double]) extends AnyVal with MakesValueStepper[DoubleStepper with EfficientSubstep] {
  @inline def valueStepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashMapValue[K](underlying, 0, underlying.size)
}
  
final class RichImmHashMapIntKeyCanStep[V](private val underlying: collection.immutable.HashMap[Int, V]) extends AnyVal with MakesKeyStepper[IntStepper with EfficientSubstep] {
  @inline def keyStepper: IntStepper with EfficientSubstep = new StepsIntImmHashMapKey[V](underlying, 0, underlying.size)
}

final class RichImmHashMapIntValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Int]) extends AnyVal with MakesValueStepper[IntStepper with EfficientSubstep] {
  @inline def valueStepper: IntStepper with EfficientSubstep = new StepsIntImmHashMapValue[K](underlying, 0, underlying.size)
}
  
final class RichImmHashMapLongKeyCanStep[V](private val underlying: collection.immutable.HashMap[Long, V]) extends AnyVal with MakesKeyStepper[LongStepper with EfficientSubstep] {
  @inline def keyStepper: LongStepper with EfficientSubstep = new StepsLongImmHashMapKey[V](underlying, 0, underlying.size)
}

final class RichImmHashMapLongValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Long]) extends AnyVal with MakesValueStepper[LongStepper with EfficientSubstep] {
  @inline def valueStepper: LongStepper with EfficientSubstep = new StepsLongImmHashMapValue[K](underlying, 0, underlying.size)
}
  
