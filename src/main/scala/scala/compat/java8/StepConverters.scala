package scala.compat.java8

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

package converterImpls {
  import Stepper._

  final class RichArrayAnyCanStep[A](private val underlying: Array[A]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayObjectCanStep[A <: Object](private val underlying: Array[A]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsObjectArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayUnitCanStep(private val underlying: Array[Unit]) extends AnyVal with MakesAnyStepper[Unit] {
    @inline def stepper: AnyStepper[Unit] with EfficientSubstep = new StepsUnitArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayBooleanCanStep(private val underlying: Array[Boolean]) extends AnyVal with MakesAnyStepper[Boolean] {
    @inline def stepper: AnyStepper[Boolean] with EfficientSubstep = new StepsBoxedBooleanArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayByteCanStep(private val underlying: Array[Byte]) extends AnyVal with MakesAnyStepper[Byte] {
    @inline def stepper: AnyStepper[Byte] with EfficientSubstep = new StepsBoxedByteArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayCharCanStep(private val underlying: Array[Char]) extends AnyVal with MakesAnyStepper[Char] {
    @inline def stepper: AnyStepper[Char] with EfficientSubstep = new StepsBoxedCharArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayShortCanStep(private val underlying: Array[Short]) extends AnyVal with MakesAnyStepper[Short] {
    @inline def stepper: AnyStepper[Short] with EfficientSubstep = new StepsBoxedShortArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayFloatCanStep(private val underlying: Array[Float]) extends AnyVal with MakesAnyStepper[Float] {
    @inline def stepper: AnyStepper[Float] with EfficientSubstep = new StepsBoxedFloatArray(underlying, 0, underlying.length)
  }

  final class RichIndexedSeqCanStep[A](private val underlying: collection.IndexedSeqLike[A, _]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyIndexedSeq[A](underlying, 0, underlying.length)
  }
  
  final class RichDoubleIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Double, _]](private val underlying: CC) extends AnyVal with MakesDoubleStepper {
    @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleIndexedSeq[CC](underlying, 0, underlying.length)
  }
  
  final class RichIntIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Int, _]](private val underlying: CC) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntIndexedSeq[CC](underlying, 0, underlying.length)
  }
  
  final class RichLongIndexedSeqCanStep[CC <: collection.IndexedSeqLike[Long, _]](private val underlying: CC) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = new StepsLongIndexedSeq[CC](underlying, 0, underlying.length)
  }

  final class RichLinearSeqCanStep[A, CC >: Null <: collection.LinearSeqLike[A, CC]](private val underlying: CC) extends AnyVal with MakesAnySeqStepper[A] {
    @inline def stepper: AnyStepper[A] = new StepsAnyLinearSeq[A, CC](underlying, Long.MaxValue)
  }

  final class RichDoubleLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Double, CC]](private val underlying: CC) extends AnyVal with MakesDoubleSeqStepper {
    @inline def stepper: DoubleStepper = new StepsDoubleLinearSeq[CC](underlying, Long.MaxValue)
  }

  final class RichIntLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Int, CC]](private val underlying: CC) extends AnyVal with MakesIntSeqStepper {
    @inline def stepper: IntStepper = new StepsIntLinearSeq[CC](underlying, Long.MaxValue)
  }

  final class RichLongLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Long, CC]](private val underlying: CC) extends AnyVal with MakesLongSeqStepper {
    @inline def stepper: LongStepper = new StepsLongLinearSeq[CC](underlying, Long.MaxValue)
  }

  final class RichIteratorCanStep[A](private val underlying: Iterator[A]) extends AnyVal with MakesAnySeqStepper[A] {
    @inline def stepper: AnyStepper[A] = new StepsAnyIterator[A](underlying)
  }

  final class RichDoubleIteratorCanStep(private val underlying: Iterator[Double]) extends AnyVal with MakesDoubleSeqStepper {
    @inline def stepper: DoubleStepper = new StepsDoubleIterator(underlying)
  }

  final class RichIntIteratorCanStep(private val underlying: Iterator[Int]) extends AnyVal with MakesIntSeqStepper {
    @inline def stepper: IntStepper = new StepsIntIterator(underlying)
  }

  final class RichLongIteratorCanStep(private val underlying: Iterator[Long]) extends AnyVal with MakesLongSeqStepper {
    @inline def stepper: LongStepper = new StepsLongIterator(underlying)
  }

  final class RichNumericRangeCanStep[T](private val underlying: collection.immutable.NumericRange[T]) extends AnyVal with MakesAnyStepper[T] {
    @inline def stepper: AnyStepper[T] with EfficientSubstep = new StepsAnyNumericRange[T](underlying, 0, underlying.length)
  }

  final class RichVectorCanStep[A](private val underlying: Vector[A]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyVector[A](underlying, 0, underlying.length)
  }

  final class RichFlatHashTableCanStep[A](private val underlying: collection.mutable.FlatHashTable[A]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsAnyFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichDoubleFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Double]) extends AnyVal with MakesDoubleStepper {
    @inline def stepper: DoubleStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsDoubleFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichIntFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Int]) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsIntFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichLongFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Long]) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsLongFlatHashTable(tbl, 0, tbl.length)
    }
  }

  final class RichHashTableKeyCanStep[K, HE >: Null <: collection.mutable.HashEntry[K, HE]](private val underlying: collection.mutable.HashTable[K, HE])
  extends AnyVal with MakesAnyKeyStepper[K] {
    @inline def keyStepper: AnyStepper[K] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, HE](underlying)
      new StepsAnyHashTableKey(tbl, 0, tbl.length)
    }
  }

  final class RichHashTableDoubleKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Double, HE]](private val underlying: collection.mutable.HashTable[Double, HE])
  extends AnyVal with MakesDoubleKeyStepper {
    @inline def keyStepper: DoubleStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[Double, HE](underlying)
      new StepsDoubleHashTableKey(tbl, 0, tbl.length)
    }
  }

  final class RichHashTableIntKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Int, HE]](private val underlying: collection.mutable.HashTable[Int, HE])
  extends AnyVal with MakesIntKeyStepper {
    @inline def keyStepper: IntStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[Int, HE](underlying)
      new StepsIntHashTableKey(tbl, 0, tbl.length)
    }
  }

  final class RichHashTableLongKeyCanStep[HE >: Null <: collection.mutable.HashEntry[Long, HE]](private val underlying: collection.mutable.HashTable[Long, HE])
  extends AnyVal with MakesLongKeyStepper {
    @inline def keyStepper: LongStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[Long, HE](underlying)
      new StepsLongHashTableKey(tbl, 0, tbl.length)
    }
  }

  final class RichDefaultHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]])
  extends AnyVal with MakesAnyStepper[(K, V)] {
    @inline def stepper: AnyStepper[(K,V)] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
      new StepsAnyDefaultHashTable(tbl, 0, tbl.length)
    }
  }

  final class RichDefaultHashTableValueCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, V]])
  extends AnyVal with MakesAnyValueStepper[V] {
    @inline def valueStepper: AnyStepper[V] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, V]](underlying)
      new StepsAnyDefaultHashTableValue(tbl, 0, tbl.length)
    }
  }
  
  final class RichDefaultHashTableDoubleValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Double]])
  extends AnyVal with MakesDoubleValueStepper {
    @inline def valueStepper: DoubleStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Double]](underlying)
      new StepsDoubleDefaultHashTableValue(tbl, 0, tbl.length)
    }
  }

  final class RichDefaultHashTableIntValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Int]])
  extends AnyVal with MakesIntValueStepper {
    @inline def valueStepper: IntStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Int]](underlying)
      new StepsIntDefaultHashTableValue(tbl, 0, tbl.length)
    }
  }
  
  final class RichDefaultHashTableLongValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.DefaultEntry[K, Long]])
  extends AnyVal with MakesLongValueStepper {
    @inline def valueStepper: LongStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.DefaultEntry[K, Long]](underlying)
      new StepsLongDefaultHashTableValue(tbl, 0, tbl.length)
    }
  }
    
  final class RichLinkedHashTableCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]])
  extends AnyVal with MakesAnyStepper[(K,V)] {
    @inline def stepper: AnyStepper[(K,V)] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
      new StepsAnyLinkedHashTable(tbl, 0, tbl.length)
    }
  }

  final class RichLinkedHashTableValueCanStep[K, V](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, V]])
  extends AnyVal with MakesAnyValueStepper[V] {
    @inline def valueStepper: AnyStepper[V] with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, V]](underlying)
      new StepsAnyLinkedHashTableValue(tbl, 0, tbl.length)
    }
  }
  
  final class RichLinkedHashTableDoubleValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Double]])
  extends AnyVal with MakesDoubleValueStepper {
    @inline def valueStepper: DoubleStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Double]](underlying)
      new StepsDoubleLinkedHashTableValue(tbl, 0, tbl.length)
    }
  }

  final class RichLinkedHashTableIntValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Int]])
  extends AnyVal with MakesIntValueStepper {
    @inline def valueStepper: IntStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Int]](underlying)
      new StepsIntLinkedHashTableValue(tbl, 0, tbl.length)
    }
  }
  
  final class RichLinkedHashTableLongValueCanStep[K](private val underlying: collection.mutable.HashTable[K, collection.mutable.LinkedEntry[K, Long]])
  extends AnyVal with MakesLongValueStepper {
    @inline def valueStepper: LongStepper with EfficientSubstep = {
      val tbl = runtime.CollectionInternals.getTable[K, collection.mutable.LinkedEntry[K, Long]](underlying)
      new StepsLongLinkedHashTableValue(tbl, 0, tbl.length)
    }
  }

  final class RichImmHashMapCanStep[K, V](private val underlying: collection.immutable.HashMap[K, V])
  extends AnyVal with MakesAnyStepper[(K, V)] with MakesAnyKeyStepper[K] with MakesAnyValueStepper[V] {
    @inline def stepper: AnyStepper[(K, V)] with EfficientSubstep = new StepsAnyImmHashMap[K, V](underlying, 0, underlying.size)
    @inline def keyStepper: AnyStepper[K] with EfficientSubstep = new StepsAnyImmHashMapKey[K, V](underlying, 0, underlying.size)
    @inline def valueStepper: AnyStepper[V] with EfficientSubstep = new StepsAnyImmHashMapValue[K, V](underlying, 0, underlying.size)
  }

  final class RichImmHashMapDoubleKeyCanStep[V](private val underlying: collection.immutable.HashMap[Double, V]) extends AnyVal with MakesDoubleKeyStepper {
    @inline def keyStepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashMapKey[V](underlying, 0, underlying.size)
  }

  final class RichImmHashMapDoubleValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Double]) extends AnyVal with MakesDoubleValueStepper {
    @inline def valueStepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashMapValue[K](underlying, 0, underlying.size)
  }
    
  final class RichImmHashMapIntKeyCanStep[V](private val underlying: collection.immutable.HashMap[Int, V]) extends AnyVal with MakesIntKeyStepper {
    @inline def keyStepper: IntStepper with EfficientSubstep = new StepsIntImmHashMapKey[V](underlying, 0, underlying.size)
  }

  final class RichImmHashMapIntValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Int]) extends AnyVal with MakesIntValueStepper {
    @inline def valueStepper: IntStepper with EfficientSubstep = new StepsIntImmHashMapValue[K](underlying, 0, underlying.size)
  }
    
  final class RichImmHashMapLongKeyCanStep[V](private val underlying: collection.immutable.HashMap[Long, V]) extends AnyVal with MakesLongKeyStepper {
    @inline def keyStepper: LongStepper with EfficientSubstep = new StepsLongImmHashMapKey[V](underlying, 0, underlying.size)
  }

  final class RichImmHashMapLongValueCanStep[K](private val underlying: collection.immutable.HashMap[K, Long]) extends AnyVal with MakesLongValueStepper {
    @inline def valueStepper: LongStepper with EfficientSubstep = new StepsLongImmHashMapValue[K](underlying, 0, underlying.size)
  }
    
  final class RichImmHashSetCanStep[A](private val underlying: collection.immutable.HashSet[A]) extends AnyVal with MakesAnyStepper[A] {
    @inline def stepper: AnyStepper[A] with EfficientSubstep = new StepsAnyImmHashSet(underlying.iterator, underlying.size)
  }

  final class RichBitSetCanStep(private val underlying: collection.BitSet) extends AnyVal with MakesIntStepper {
    def stepper: IntStepper with EfficientSubstep = {
      val bits: Array[Long] = underlying match {
        case m: collection.mutable.BitSet => runtime.CollectionInternals.getBitSetInternals(m)
        case n: collection.immutable.BitSet.BitSetN => RichBitSetCanStep.reflectInternalsN(n)
        case x => x.toBitMask
      }
      new StepsIntBitSet(bits, 0, math.min(bits.length*64L, Int.MaxValue).toInt)
    }
  }
  private[java8] object RichBitSetCanStep {
    private val reflector = classOf[collection.immutable.BitSet.BitSetN].getMethod("elems")
    def reflectInternalsN(bsn: collection.immutable.BitSet.BitSetN): Array[Long] = reflector.invoke(bsn).asInstanceOf[Array[Long]]
  }

  final class RichMapCanStep[K, V](private val underlying: collection.Map[K, V]) extends AnyVal with MakesAnyKeySeqStepper[K] with MakesAnyValueSeqStepper[V] {
    def keyStepper: AnyStepper[K] = new StepsAnyIterator[K](underlying.keysIterator)
    def valueStepper: AnyStepper[V] = new StepsAnyIterator[V](underlying.valuesIterator)
  }

  final class RichDoubleKeyMapCanStep[V](private val underlying: collection.Map[Double, V]) extends AnyVal with MakesDoubleKeySeqStepper {
    def keyStepper: DoubleStepper = new StepsDoubleIterator(underlying.keysIterator)
  }

  final class RichDoubleValueMapCanStep[K](private val underlying: collection.Map[K, Double]) extends AnyVal with MakesDoubleValueSeqStepper {
    def valueStepper: DoubleStepper = new StepsDoubleIterator(underlying.valuesIterator)
  }

  final class RichIntKeyMapCanStep[V](private val underlying: collection.Map[Int, V]) extends AnyVal with MakesIntKeySeqStepper {
    def keyStepper: IntStepper = new StepsIntIterator(underlying.keysIterator)
  }

  final class RichIntValueMapCanStep[K](private val underlying: collection.Map[K, Int]) extends AnyVal with MakesIntValueSeqStepper {
    def valueStepper: IntStepper = new StepsIntIterator(underlying.valuesIterator)
  }

  final class RichLongKeyMapCanStep[V](private val underlying: collection.Map[Long, V]) extends AnyVal with MakesLongKeySeqStepper {
    def keyStepper: LongStepper = new StepsLongIterator(underlying.keysIterator)
  }

  final class RichLongValueMapCanStep[K](private val underlying: collection.Map[K, Long]) extends AnyVal with MakesLongValueSeqStepper {
    def valueStepper: LongStepper = new StepsLongIterator(underlying.valuesIterator)
  }

  final class RichIterableCanStep[A](private val underlying: Iterable[A]) extends AnyVal with MakesAnySeqStepper[A] {
    @inline def stepper: AnyStepper[A] = new StepsAnyIterator[A](underlying.iterator)
  }

  final class RichDoubleIterableCanStep(private val underlying: Iterable[Double]) extends AnyVal with MakesDoubleSeqStepper {
    @inline def stepper: DoubleStepper = new StepsDoubleIterator(underlying.iterator)
  }

  final class RichIntIterableCanStep(private val underlying: Iterable[Int]) extends AnyVal with MakesIntSeqStepper {
    @inline def stepper: IntStepper = new StepsIntIterator(underlying.iterator)
  }

  final class RichLongIterableCanStep(private val underlying: Iterable[Long]) extends AnyVal with MakesLongSeqStepper {
    @inline def stepper: LongStepper = new StepsLongIterator(underlying.iterator)
  }

  final class RichArrayDoubleCanStep(private val underlying: Array[Double]) extends AnyVal with MakesDoubleStepper {
    @inline def stepper: DoubleStepper with EfficientSubstep with EfficientSubstep = new StepsDoubleArray(underlying, 0, underlying.length)
  }

  final class RichArrayIntCanStep(private val underlying: Array[Int]) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayLongCanStep(private val underlying: Array[Long]) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = new StepsLongArray(underlying, 0, underlying.length)
  }
  
  final class RichIntNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Int]) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntNumericRange(underlying, 0, underlying.length)
  }

  final class RichLongNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Long]) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = new StepsLongNumericRange(underlying, 0, underlying.length)
  }

  final class RichRangeCanStep(private val underlying: Range) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntRange(underlying, 0, underlying.length)
  }  

  final class RichDoubleVectorCanStep[A](private val underlying: Vector[Double]) extends AnyVal with MakesDoubleStepper {
    @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleVector(underlying, 0, underlying.length)
  }

  final class RichIntVectorCanStep[A](private val underlying: Vector[Int]) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntVector(underlying, 0, underlying.length)
  }

  final class RichLongVectorCanStep[A](private val underlying: Vector[Long]) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = new StepsLongVector(underlying, 0, underlying.length)
  }

  final class RichDoubleHashSetCanStep(private val underlying: collection.immutable.HashSet[Double]) extends AnyVal with MakesDoubleStepper {
    @inline def stepper: DoubleStepper with EfficientSubstep = new StepsDoubleImmHashSet(underlying.iterator, underlying.size)
  }

  final class RichIntHashSetCanStep(private val underlying: collection.immutable.HashSet[Int]) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepsIntImmHashSet(underlying.iterator, underlying.size)
  }

  final class RichLongHashSetCanStep(private val underlying: collection.immutable.HashSet[Long]) extends AnyVal with MakesLongStepper {
    @inline def stepper: LongStepper with EfficientSubstep = new StepsLongImmHashSet(underlying.iterator, underlying.size)
  }

  final class RichStringCanStep(private val underlying: String) extends AnyVal with MakesIntStepper {
    @inline def stepper: IntStepper with EfficientSubstep = new StepperStringCodePoint(underlying, 0, underlying.length)
  }    

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

  final class CollectionCanAccumulate[A](private val underlying: TraversableOnce[A]) extends AnyVal {
    def accumulate: Accumulator[A] = {
      val a = new Accumulator[A]
      underlying.foreach(a += _)
      a
    }
  }

  final class AccumulateDoubleCollection(private val underlying: TraversableOnce[Double]) extends AnyVal {
    def accumulate: DoubleAccumulator = {
      val da = new DoubleAccumulator
      underlying.foreach(da += _)
      da
    }
  }
  final class AccumulateIntCollection(private val underlying: TraversableOnce[Int]) extends AnyVal {
    def accumulate: IntAccumulator = {
      val da = new IntAccumulator
      underlying.foreach(da += _)
      da
    }
  }
  final class AccumulateLongCollection(private val underlying: TraversableOnce[Long]) extends AnyVal {
    def accumulate: LongAccumulator = {
      val da = new LongAccumulator
      underlying.foreach(da += _)
      da
    }
  }
  final class AccumulateAnyArray[A](private val underlying: Array[A]) extends AnyVal {
    def accumulate: Accumulator[A] = {
      val a = new Accumulator[A]
      var i = 0
      while (i < underlying.length) { a += underlying(i); i += 1 }
      a
    }
  }

  final class AccumulateDoubleArray(private val underlying: Array[Double]) extends AnyVal {
    def accumulate: DoubleAccumulator = {
      val da = new DoubleAccumulator
      var i = 0
      while (i < underlying.length) { da += underlying(i); i += 1 }
      da
    }
  }
  final class AccumulateIntArray(private val underlying: Array[Int]) extends AnyVal {
    def accumulate: IntAccumulator = {
      val da = new IntAccumulator
      var i = 0
      while (i < underlying.length) { da += underlying(i); i += 1 }
      da
    }
  }
  final class AccumulateLongArray(private val underlying: Array[Long]) extends AnyVal {
    def accumulate: LongAccumulator = {
      val da = new LongAccumulator
      var i = 0
      while (i < underlying.length) { da += underlying(i); i += 1 }
      da
    }
  }

  trait Priority3AccumulatorConverters {
    implicit def collectionCanAccumulate[A](underlying: TraversableOnce[A]) = new CollectionCanAccumulate[A](underlying)
  }

  trait Priority2AccumulatorConverters extends Priority3AccumulatorConverters {
    implicit def accumulateDoubleCollection(underlying: TraversableOnce[Double]) = new AccumulateDoubleCollection(underlying)
    implicit def accumulateIntCollection(underlying: TraversableOnce[Int]) = new AccumulateIntCollection(underlying)
    implicit def accumulateLongCollection(underlying: TraversableOnce[Long]) = new AccumulateLongCollection(underlying)
    implicit def accumulateAnyArray[A](underlying: Array[A]) = new AccumulateAnyArray(underlying)
  }

  trait Priority1AccumulatorConverters extends Priority2AccumulatorConverters {
    implicit def accumulateDoubleArray(underlying: Array[Double]) = new AccumulateDoubleArray(underlying)
    implicit def accumulateIntArray(underlying: Array[Int]) = new AccumulateIntArray(underlying)
    implicit def accumulateLongArray(underlying: Array[Long]) = new AccumulateLongArray(underlying)
  }
}

object StepConverters
extends converterImpls.Priority1StepConverters
with converterImpls.Priority1AccumulatorConverters
{}
