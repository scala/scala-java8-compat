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

trait Priority3StepConverters {
  implicit def richIterableCanStep[A](underlying: Iterable[A]) = new RichIterableCanStep(underlying)
  implicit def richMapCanStep[K, V](underlying: collection.Map[K, V]) = new RichMapCanStep[K, V](underlying)
}

trait Priority2StepConverters extends Priority3StepConverters {
  implicit def richLinearSeqCanStep[A](underlying: collection.LinearSeq[A]) = new RichLinearSeqCanStep[A](underlying)
  implicit def richIndexedSeqCanStep[A](underlying: collection.IndexedSeqLike[A, _]) = new RichIndexedSeqCanStep[A](underlying)
}

trait Priority1StepConverters extends Priority2StepConverters {
  implicit def richDefaultHashTableCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]]) = new RichDefaultHashTableCanStep[K, V](underlying)
  implicit def richLinkedHashTableCanStep[K, V](underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]]) = new RichLinkedHashTableCanStep[K, V](underlying)
  implicit def richArrayCanStep[A](underlying: Array[A]) = new RichArrayCanStep[A](underlying)
  implicit def richWrappedArrayCanStep[A](underlying: collection.mutable.WrappedArray[A]) = new RichArrayCanStep[A](underlying.array)
  implicit def richFlatHashTableCanStep[A](underlying: collection.mutable.FlatHashTable[A]) = new RichFlatHashTableCanStep[A](underlying)
  implicit def richIteratorCanStep[A](underlying: Iterator[A]) = new RichIteratorCanStep(underlying)
  implicit def richImmHashMapCanStep[K, V](underlying: collection.immutable.HashMap[K, V]) = new RichImmHashMapCanStep[K, V](underlying)
  implicit def richImmHashSetCanStep[A](underlying: collection.immutable.HashSet[A]) = new RichImmHashSetCanStep[A](underlying)
  implicit def richNumericRangeCanStep[T](underlying: collection.immutable.NumericRange[T]) = new RichNumericRangeCanStep(underlying)
  implicit def richVectorCanStep[A](underlying: Vector[A]) = new RichVectorCanStep[A](underlying)
  implicit def richBitSetCanStep(underlying: collection.BitSet) = new RichBitSetCanStep(underlying)
  implicit def richRangeCanStep(underlying: Range) = new RichRangeCanStep(underlying)
  implicit def richStringCanStep(underlying: String) = new RichStringCanStep(underlying)
}
