package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

// Steppers for keys (type of HashEntry doesn't matter)

private[java8] class StepsAnyHashTableKey[K, HE <: collection.mutable.HashEntry[K, HE]](_underlying: Array[collection.mutable.HashEntry[K, HE]], _i0: Int, _iN: Int)
extends StepsLikeGapped[K, StepsAnyHashTableKey[K, HE]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def next() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[HE].key; currentEntry = currentEntry.asInstanceOf[HE].next; ans }
  def semiclone(half: Int) = new StepsAnyHashTableKey[K, HE](underlying.asInstanceOf[Array[collection.mutable.HashEntry[K, HE]]], i0, half)
}

private[java8] class StepsDoubleHashTableKey[HE <: collection.mutable.HashEntry[Double, HE]](_underlying: Array[collection.mutable.HashEntry[Double, HE]], _i0: Int, _iN: Int)
extends StepsDoubleLikeGapped[StepsDoubleHashTableKey[HE]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextDouble() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[HE].key; currentEntry = currentEntry.asInstanceOf[HE].next; ans }
  def semiclone(half: Int) = new StepsDoubleHashTableKey[HE](underlying.asInstanceOf[Array[collection.mutable.HashEntry[Double, HE]]], i0, half)
}

private[java8] class StepsIntHashTableKey[HE <: collection.mutable.HashEntry[Int, HE]](_underlying: Array[collection.mutable.HashEntry[Int, HE]], _i0: Int, _iN: Int)
extends StepsIntLikeGapped[StepsIntHashTableKey[HE]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextInt() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[HE].key; currentEntry = currentEntry.asInstanceOf[HE].next; ans }
  def semiclone(half: Int) = new StepsIntHashTableKey[HE](underlying.asInstanceOf[Array[collection.mutable.HashEntry[Int, HE]]], i0, half)
}

private[java8] class StepsLongHashTableKey[HE <: collection.mutable.HashEntry[Long, HE]](_underlying: Array[collection.mutable.HashEntry[Long, HE]], _i0: Int, _iN: Int)
extends StepsLongLikeGapped[StepsLongHashTableKey[HE]](_underlying.asInstanceOf[Array[AnyRef]], _i0, _iN) {
  def nextLong() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[HE].key; currentEntry = currentEntry.asInstanceOf[HE].next; ans }
  def semiclone(half: Int) = new StepsLongHashTableKey[HE](underlying.asInstanceOf[Array[collection.mutable.HashEntry[Long, HE]]], i0, half)
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

// Steppers for keys (type of HashEntry doesn't matter)

final class RichHashTableKeyCanStep[K, HE >: Null <: collection.mutable.HashEntry[K, HE]](private val underlying: collection.mutable.HashTable[K, HE])
extends AnyVal with MakesKeyStepper[AnyStepper[K] with EfficientSubstep] {
  @inline def keyStepper: AnyStepper[K] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, HE](underlying)
    new StepsAnyHashTableKey(tbl, 0, tbl.length)
  }
}

final class RichHashTableDoubleKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Double, HE]](private val underlying: collection.mutable.HashTable[Double, HE])
extends AnyVal with MakesKeyStepper[DoubleStepper with EfficientSubstep] {
  @inline def keyStepper: DoubleStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[Double, HE](underlying)
    new StepsDoubleHashTableKey(tbl, 0, tbl.length)
  }
}

final class RichHashTableIntKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Int, HE]](private val underlying: collection.mutable.HashTable[Int, HE])
extends AnyVal with MakesKeyStepper[IntStepper with EfficientSubstep] {
  @inline def keyStepper: IntStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[Int, HE](underlying)
    new StepsIntHashTableKey(tbl, 0, tbl.length)
  }
}

final class RichHashTableLongKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Long, HE]](private val underlying: collection.mutable.HashTable[Long, HE])
extends AnyVal with MakesKeyStepper[LongStepper with EfficientSubstep] {
  @inline def keyStepper: LongStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[Long, HE](underlying)
    new StepsLongHashTableKey(tbl, 0, tbl.length)
  }
}

// Steppers for entries stored in DefaultEntry HashEntry
// (both for key-value pair and for values alone)

final class RichDefaultHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]])
extends AnyVal with MakesStepper[AnyStepper[(K,V)] with EfficientSubstep] {
  @inline def stepper: AnyStepper[(K,V)] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
    new StepsAnyDefaultHashTable(tbl, 0, tbl.length)
  }
}

final class RichDefaultHashTableValueCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]])
extends AnyVal with MakesValueStepper[AnyStepper[V] with EfficientSubstep] {
  @inline def valueStepper: AnyStepper[V] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
    new StepsAnyDefaultHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichDefaultHashTableDoubleValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Double]])
extends AnyVal with MakesValueStepper[DoubleStepper with EfficientSubstep] {
  @inline def valueStepper: DoubleStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Double]](underlying)
    new StepsDoubleDefaultHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichDefaultHashTableIntValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Int]])
extends AnyVal with MakesValueStepper[IntStepper with EfficientSubstep] {
  @inline def valueStepper: IntStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Int]](underlying)
    new StepsIntDefaultHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichDefaultHashTableLongValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Long]])
extends AnyVal with MakesValueStepper[LongStepper with EfficientSubstep] {
  @inline def valueStepper: LongStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Long]](underlying)
    new StepsLongDefaultHashTableValue(tbl, 0, tbl.length)
  }
}
  
// Steppers for entries stored in LinkedEntry HashEntry
// (both for key-value pair and for values alone)

final class RichLinkedHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]])
extends AnyVal with MakesStepper[AnyStepper[(K,V)] with EfficientSubstep] {
  @inline def stepper: AnyStepper[(K,V)] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
    new StepsAnyLinkedHashTable(tbl, 0, tbl.length)
  }
}

final class RichLinkedHashTableValueCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]])
extends AnyVal with MakesValueStepper[AnyStepper[V] with EfficientSubstep] {
  @inline def valueStepper: AnyStepper[V] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
    new StepsAnyLinkedHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichLinkedHashTableDoubleValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Double]])
extends AnyVal with MakesValueStepper[DoubleStepper with EfficientSubstep] {
  @inline def valueStepper: DoubleStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Double]](underlying)
    new StepsDoubleLinkedHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichLinkedHashTableIntValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Int]])
extends AnyVal with MakesValueStepper[IntStepper with EfficientSubstep] {
  @inline def valueStepper: IntStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Int]](underlying)
    new StepsIntLinkedHashTableValue(tbl, 0, tbl.length)
  }
}

final class RichLinkedHashTableLongValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Long]])
extends AnyVal with MakesValueStepper[LongStepper with EfficientSubstep] {
  @inline def valueStepper: LongStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Long]](underlying)
    new StepsLongLinkedHashTableValue(tbl, 0, tbl.length)
  }
}
