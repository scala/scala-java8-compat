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

package scala.compat.java8
package converterImpl

import language.implicitConversions
import scala.reflect.ClassTag

trait Priority3StepConverters {
  implicit def richIterableCanStep[A](underlying: Iterable[A]): RichIterableCanStep[A] =
    new RichIterableCanStep(underlying)
  implicit def richMapCanStep[K, V](underlying: collection.Map[K, V]): RichMapCanStep[K, V] =
    new RichMapCanStep[K, V](underlying)
}

trait Priority2StepConverters extends Priority3StepConverters {
  implicit def richLinearSeqCanStep[A](underlying: collection.LinearSeq[A]): RichLinearSeqCanStep[A] =
    new RichLinearSeqCanStep[A](underlying)
  implicit def richIndexedSeqCanStep[A](underlying: collection.IndexedSeqOps[A, Any, _]): RichIndexedSeqCanStep[A] =
    new RichIndexedSeqCanStep[A](underlying)
}

trait Priority1StepConverters extends Priority2StepConverters {
  implicit def richDefaultHashMapCanStep[K, V](underlying: collection.mutable.HashMap[K, V]): RichHashMapCanStep[K, V] =
    new RichHashMapCanStep[K, V](underlying)
  implicit def richLinkedHashMapCanStep[K, V](underlying: collection.mutable.LinkedHashMap[K, V]): RichLinkedHashMapCanStep[K, V] =
    new RichLinkedHashMapCanStep[K, V](underlying)
  implicit def richArrayCanStep[A](underlying: Array[A]): RichArrayCanStep[A] =
    new RichArrayCanStep[A](underlying)
  implicit def richArraySeqCanStep[A: ClassTag](underlying: collection.mutable.ArraySeq[A]): RichArrayCanStep[A] =
    new RichArrayCanStep[A](StreamConverters.unsafeArrayIfPossible(underlying))
  implicit def richHashSetCanStep[A](underlying: collection.mutable.HashSet[A]): RichHashSetCanStep[A] =
    new RichHashSetCanStep[A](underlying)
  implicit def richIteratorCanStep[A](underlying: Iterator[A]): RichIteratorCanStep[A] =
    new RichIteratorCanStep(underlying)
  implicit def richImmHashSetCanStep[A](underlying: collection.immutable.HashSet[A]): RichImmHashSetCanStep[A] =
    new RichImmHashSetCanStep[A](underlying)
  implicit def richNumericRangeCanStep[T](underlying: collection.immutable.NumericRange[T]): RichNumericRangeCanStep[T] =
    new RichNumericRangeCanStep(underlying)
  implicit def richVectorCanStep[A](underlying: Vector[A]): RichVectorCanStep[A] =
    new RichVectorCanStep[A](underlying)
  implicit def richBitSetCanStep(underlying: collection.BitSet): RichBitSetCanStep =
    new RichBitSetCanStep(underlying)
  implicit def richRangeCanStep(underlying: Range): RichRangeCanStep[Int] =
    new RichRangeCanStep(underlying)
  implicit def richStringCanStep(underlying: String): RichStringCanStep =
    new RichStringCanStep(underlying)
}
