package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

////////////////////////////
// Steppers for keys (type of HashEntry doesn't matter)
////////////////////////////

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

////////////////////////////
// Steppers for entries stored in DefaultEntry HashEntry
// (both for key-value pair and for values alone)
////////////////////////////

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

////////////////////////////
// Steppers for entries stored in LinkedEntry HashEntry
// (both for key-value pair and for values alone)
////////////////////////////

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

