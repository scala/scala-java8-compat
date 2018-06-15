package scala.compat.java8.converterImpl

import language.implicitConversions

trait Priority3StepConverters {
  implicit def richIterableCanStep[A](underlying: Iterable[A]) = new RichIterableCanStep(underlying)
  implicit def richMapCanStep[K, V](underlying: collection.Map[K, V]) = new RichMapCanStep[K, V](underlying)
}

trait Priority2StepConverters extends Priority3StepConverters {
  implicit def richLinearSeqCanStep[A](underlying: collection.LinearSeq[A]) = new RichLinearSeqCanStep[A](underlying)
  implicit def richIndexedSeqCanStep[A](underlying: collection.IndexedSeqOps[A, Any, _]) = new RichIndexedSeqCanStep[A](underlying)
}

trait Priority1StepConverters extends Priority2StepConverters {
  implicit def richDefaultHashMapCanStep[K, V](underlying: collection.mutable.HashMap[K, V]) = new RichHashMapCanStep[K, V](underlying)
  implicit def richLinkedHashMapCanStep[K, V](underlying: collection.mutable.LinkedHashMap[K, V]) = new RichLinkedHashMapCanStep[K, V](underlying)
  implicit def richArrayCanStep[A](underlying: Array[A]) = new RichArrayCanStep[A](underlying)
  implicit def richArraySeqCanStep[A](underlying: collection.mutable.ArraySeq[A]) = new RichArrayCanStep[A](underlying.array)
  implicit def richHashSetCanStep[A](underlying: collection.mutable.HashSet[A]) = new RichHashSetCanStep[A](underlying)
  implicit def richIteratorCanStep[A](underlying: Iterator[A]) = new RichIteratorCanStep(underlying)
  implicit def richImmHashMapCanStep[K, V](underlying: collection.immutable.HashMap[K, V]) = new RichImmHashMapCanStep[K, V](underlying)
  implicit def richImmHashSetCanStep[A](underlying: collection.immutable.HashSet[A]) = new RichImmHashSetCanStep[A](underlying)
  implicit def richNumericRangeCanStep[T](underlying: collection.immutable.NumericRange[T]) = new RichNumericRangeCanStep(underlying)
  implicit def richVectorCanStep[A](underlying: Vector[A]) = new RichVectorCanStep[A](underlying)
  implicit def richBitSetCanStep(underlying: collection.BitSet) = new RichBitSetCanStep(underlying)
  implicit def richRangeCanStep(underlying: Range) = new RichRangeCanStep(underlying)
  implicit def richStringCanStep(underlying: String) = new RichStringCanStep(underlying)
}
