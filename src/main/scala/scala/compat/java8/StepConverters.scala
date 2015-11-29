package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

trait Priority7StepConverters {
  implicit def richIterableCanStep[A](underlying: Iterable[A]) = new RichIterableCanStep(underlying)
}

trait Priority6StepConverters extends Priority7StepConverters {
  implicit def richDoubleIterableCanStep(underlying: Iterable[Double]) = new RichDoubleIterableCanStep(underlying)
  implicit def richIntIterableCanStep(underlying: Iterable[Int]) = new RichIntIterableCanStep(underlying)
  implicit def richLongIterableCanStep(underlying: Iterable[Long]) = new RichLongIterableCanStep(underlying)
  implicit def richMapCanStep[K, V](underlying: collection.Map[K, V]) = new RichMapCanStep[K, V](underlying)
}

trait Priority5StepConverters extends Priority6StepConverters {
  implicit def richDoubleKeyMapCanStep[V](underlying: collection.Map[Double, V]) = new RichDoubleKeyMapCanStep(underlying)
  implicit def richDoubleValueMapCanStep[K](underlying: collection.Map[K, Double]) = new RichDoubleValueMapCanStep(underlying)
  implicit def richIntKeyMapCanStep[V](underlying: collection.Map[Int, V]) = new RichIntKeyMapCanStep(underlying)
  implicit def richIntValueMapCanStep[K](underlying: collection.Map[K, Int]) = new RichIntValueMapCanStep(underlying)
  implicit def richLongKeyMapCanStep[V](underlying: collection.Map[Long, V]) = new RichLongKeyMapCanStep(underlying)
  implicit def richLongValueMapCanStep[K](underlying: collection.Map[K, Long]) = new RichLongValueMapCanStep(underlying)        
}

trait Priority4StepConverters extends Priority5StepConverters {
  implicit def richLinearSeqCanStep[A, CC[A] >: Null <: collection.LinearSeqLike[A, CC[A]]](underlying: CC[A]) = new RichLinearSeqCanStep[A, CC[A]](underlying)
  implicit def richHashTableKeyCanStep[K, HE >: Null <: collection.mutable.HashEntry[K, HE]](underlying: collection.mutable.HashTable[K, HE]) =
    new RichHashTableKeyCanStep[K, HE](underlying)
  implicit def richDefaultHashTableCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]]) =
    new RichDefaultHashTableCanStep[K, V](underlying)
  implicit def richDefaultHashTableValueCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]]) =
    new RichDefaultHashTableValueCanStep[K, V](underlying)
  implicit def richLinkedHashTableCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]]) =
    new RichLinkedHashTableCanStep[K, V](underlying)
  implicit def richLinkedHashTableValueCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]]) =
    new RichLinkedHashTableValueCanStep[K, V](underlying)
}

trait Priority3StepConverters extends Priority4StepConverters {
  implicit def richArrayAnyCanStep[A](underlying: Array[A]) = new RichArrayAnyCanStep[A](underlying)
  implicit def richIndexedSeqCanStep[A](underlying: collection.IndexedSeqLike[A, _]) =
    new RichIndexedSeqCanStep[A](underlying)
  implicit def richFlatHashTableCanStep[A](underlying: collection.mutable.FlatHashTable[A]) = new RichFlatHashTableCanStep[A](underlying)
  implicit def richDoubleLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Double, CC]](underlying: CC) = 
    new RichDoubleLinearSeqCanStep[CC](underlying)
  implicit def richIntLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Int, CC]](underlying: CC) = 
    new RichIntLinearSeqCanStep[CC](underlying)
  implicit def richLongLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Long, CC]](underlying: CC) = 
    new RichLongLinearSeqCanStep[CC](underlying)
  implicit def richIteratorCanStep[A](underlying: Iterator[A]) = new RichIteratorCanStep(underlying)
  implicit def richHashTableDoubleKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Double, HE]](underlying: collection.mutable.HashTable[Double, HE]) =
    new RichHashTableDoubleKeyCanStep[HE](underlying)
  implicit def richHashTableIntKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Int, HE]](underlying: collection.mutable.HashTable[Int, HE]) =
    new RichHashTableIntKeyCanStep[HE](underlying)
  implicit def richHashTableLongKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Long, HE]](underlying: collection.mutable.HashTable[Long, HE]) =
    new RichHashTableLongKeyCanStep[HE](underlying)
  implicit def richDefaultHashTableDoubleValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Double]]) =
    new RichDefaultHashTableDoubleValueCanStep[K](underlying)
  implicit def richDefaultHashTableIntValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Int]]) =
    new RichDefaultHashTableIntValueCanStep[K](underlying)
  implicit def richDefaultHashTableLongValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Long]]) =
    new RichDefaultHashTableLongValueCanStep[K](underlying)
  implicit def richLinkedHashTableDoubleValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Double]]) =
    new RichLinkedHashTableDoubleValueCanStep[K](underlying)
  implicit def richLinkedHashTableIntValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Int]]) =
    new RichLinkedHashTableIntValueCanStep[K](underlying)
  implicit def richLinkedHashTableLongValueCanStep[K](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Long]]) =
    new RichLinkedHashTableLongValueCanStep[K](underlying)

  implicit def richImmHashMapCanStep[K, V](underlying: collection.immutable.HashMap[K, V]) = new RichImmHashMapCanStep[K, V](underlying)
  implicit def richImmHashSetCanStep[A](underlying: collection.immutable.HashSet[A]) = new RichImmHashSetCanStep[A](underlying)
}

trait Priority2StepConverters extends Priority3StepConverters {
  implicit def richArrayObjectCanStep[A <: Object](underlying: Array[A]) = new RichArrayObjectCanStep[A](underlying)
  implicit def richArrayUnitCanStep(underlying: Array[Unit]) = new RichArrayUnitCanStep(underlying)
  implicit def richArrayBooleanCanStep(underlying: Array[Boolean]) = new RichArrayBooleanCanStep(underlying)
  implicit def richArrayByteCanStep(underlying: Array[Byte]) = new RichArrayByteCanStep(underlying)
  implicit def richArrayCharCanStep(underlying: Array[Char]) = new RichArrayCharCanStep(underlying)
  implicit def richArrayShortCanStep(underlying: Array[Short]) = new RichArrayShortCanStep(underlying)
  implicit def richArrayFloatCanStep(underlying: Array[Float]) = new RichArrayFloatCanStep(underlying)
  implicit def richDoubleIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Double, _]](underlying: CC) =
    new RichDoubleIndexedSeqCanStep[CC](underlying)
  implicit def richIntIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Int, _]](underlying: CC) =
    new RichIntIndexedSeqCanStep[CC](underlying)
  implicit def richLongIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Long, _]](underlying: CC) =
    new RichLongIndexedSeqCanStep[CC](underlying)
  implicit def richNumericRangeCanStep[T](underlying: collection.immutable.NumericRange[T]) = new RichNumericRangeCanStep(underlying)
  implicit def richVectorCanStep[A](underlying: Vector[A]) = new RichVectorCanStep[A](underlying)
  implicit def richDoubleFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Double]) = new RichDoubleFlatHashTableCanStep(underlying)
  implicit def richIntFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Int]) = new RichIntFlatHashTableCanStep(underlying)
  implicit def richLongFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Long]) = new RichLongFlatHashTableCanStep(underlying)
  implicit def richDoubleIteratorCanStep(underlying: Iterator[Double]) = new RichDoubleIteratorCanStep(underlying)
  implicit def richIntIteratorCanStep(underlying: Iterator[Int]) = new RichIntIteratorCanStep(underlying)
  implicit def richLongIteratorCanStep(underlying: Iterator[Long]) = new RichLongIteratorCanStep(underlying)

  implicit def richImmHashMapDoubleKeyCanStep[V](underlying: collection.immutable.HashMap[Double, V]) = new RichImmHashMapDoubleKeyCanStep(underlying)
  implicit def richImmHashMapDoubleValueCanStep[K](underlying: collection.immutable.HashMap[K, Double]) = new RichImmHashMapDoubleValueCanStep(underlying)
  implicit def richImmHashMapIntKeyCanStep[V](underlying: collection.immutable.HashMap[Int, V]) = new RichImmHashMapIntKeyCanStep(underlying)
  implicit def richImmHashMapIntValueCanStep[K](underlying: collection.immutable.HashMap[K, Int]) = new RichImmHashMapIntValueCanStep(underlying)
  implicit def richImmHashMapLongKeyCanStep[V](underlying: collection.immutable.HashMap[Long, V]) = new RichImmHashMapLongKeyCanStep(underlying)
  implicit def richImmHashMapLongValueCanStep[K](underlying: collection.immutable.HashMap[K, Long]) = new RichImmHashMapLongValueCanStep(underlying)

  implicit def richBitSetCanStep(underlying: collection.BitSet) = new RichBitSetCanStep(underlying)
}

trait Priority1StepConverters extends Priority2StepConverters {
  implicit def richArrayDoubleCanStep(underlying: Array[Double]) = new RichArrayDoubleCanStep(underlying)
  implicit def richArrayIntCanStep(underlying: Array[Int]) = new RichArrayIntCanStep(underlying)
  implicit def richArrayLongCanStep(underlying: Array[Long]) = new RichArrayLongCanStep(underlying)
  
  implicit def richIntNumericRangeCanStep(underlying: collection.immutable.NumericRange[Int]) = new RichIntNumericRangeCanStep(underlying)
  implicit def richLongNumericRangeCanStep(underlying: collection.immutable.NumericRange[Long]) = new RichLongNumericRangeCanStep(underlying)
  implicit def richRangeCanStep(underlying: Range) = new RichRangeCanStep(underlying)

  implicit def richDoubleVectorCanStep[A](underlying: Vector[Double]) = new RichDoubleVectorCanStep(underlying)
  implicit def richIntVectorCanStep[A](underlying: Vector[Int]) = new RichIntVectorCanStep(underlying)
  implicit def richLongVectorCanStep[A](underlying: Vector[Long]) = new RichLongVectorCanStep(underlying)

  implicit def richDoubleHashSetCanStep(underlying: collection.immutable.HashSet[Double]) = new RichDoubleHashSetCanStep(underlying)
  implicit def richIntHashSetCanStep(underlying: collection.immutable.HashSet[Int]) = new RichIntHashSetCanStep(underlying)
  implicit def richLongHashSetCanStep(underlying: collection.immutable.HashSet[Long]) = new RichLongHashSetCanStep(underlying)

  implicit def richStringCanStep(underlying: String) = new RichStringCanStep(underlying)
}

