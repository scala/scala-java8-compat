package scala.compat.java8

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

package converterImpls {
  import Stepper._

  private[java8] class StepsObjectArray[A <: Object](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, StepsObjectArray[A]](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsObjectArray[A](underlying, i0, half)
  }
  
  private[java8] class StepsAnyArray[A](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, StepsAnyArray[A]](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyArray[A](underlying, i0, half)
  }
  
  private[java8] class StepsUnitArray(underlying: Array[Unit], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Unit, StepsUnitArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; () } else throwNSEE
    def semiclone(half: Int) = new StepsUnitArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedBooleanArray(underlying: Array[Boolean], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Boolean, StepsBoxedBooleanArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedBooleanArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedByteArray(underlying: Array[Byte], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Byte, StepsBoxedByteArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedByteArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedCharArray(underlying: Array[Char], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Char, StepsBoxedCharArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedCharArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedShortArray(underlying: Array[Short], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Short, StepsBoxedShortArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedShortArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedFloatArray(underlying: Array[Float], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Float, StepsBoxedFloatArray](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedFloatArray(underlying, i0, half)
  }
  
  private[java8] class StepsDoubleArray(underlying: Array[Double], _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[StepsDoubleArray](_i0, _iN) {
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleArray(underlying, i0, half)
  }

  private[java8] class StepsIntArray(underlying: Array[Int], _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntArray](_i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntArray(underlying, i0, half)
  }

  private[java8] class StepsLongArray(underlying: Array[Long], _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[StepsLongArray](_i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongArray(underlying, i0, half)
  }

  private[java8] class StepsAnyIndexedSeq[A](underlying: collection.IndexedSeqLike[A, _], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, StepsAnyIndexedSeq[A]](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyIndexedSeq[A](underlying, i0, half)
  }

  private[java8] class StepsDoubleIndexedSeq[CC <: collection.IndexedSeqLike[Double, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[StepsDoubleIndexedSeq[CC]](_i0, _iN) {
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleIndexedSeq[CC](underlying, i0, half)
  }
  
  private[java8] class StepsIntIndexedSeq[CC <: collection.IndexedSeqLike[Int, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntIndexedSeq[CC]](_i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntIndexedSeq[CC](underlying, i0, half)
  }
  
  private[java8] class StepsLongIndexedSeq[CC <: collection.IndexedSeqLike[Long, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[StepsLongIndexedSeq[CC]](_i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongIndexedSeq[CC](underlying, i0, half)
  }

  private[java8] class StepsAnyLinearSeq[A, CC >: Null <: collection.LinearSeqLike[A, CC]](_underlying: CC, _maxN: Long)
  extends StepsWithTail[A, CC, StepsAnyLinearSeq[A, CC]](_underlying, _maxN) {
    protected def myIsEmpty(cc: CC): Boolean = cc.isEmpty
    protected def myTailOf(cc: CC): CC = cc.tail
    def next() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
    def semiclone(half: Int) = new StepsAnyLinearSeq[A, CC](underlying, half)
  }

  private[java8] class StepsDoubleLinearSeq[CC >: Null <: collection.LinearSeqLike[Double, CC]](_underlying: CC, _maxN: Long)
  extends StepsDoubleWithTail[CC, StepsDoubleLinearSeq[CC]](_underlying, _maxN) {
    protected def myIsEmpty(cc: CC): Boolean = cc.isEmpty
    protected def myTailOf(cc: CC): CC = cc.tail
    def nextDouble() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleLinearSeq[CC](underlying, half)
  }

  private[java8] class StepsIntLinearSeq[CC >: Null <: collection.LinearSeqLike[Int, CC]](_underlying: CC, _maxN: Long)
  extends StepsIntWithTail[CC, StepsIntLinearSeq[CC]](_underlying, _maxN) {
    protected def myIsEmpty(cc: CC): Boolean = cc.isEmpty
    protected def myTailOf(cc: CC): CC = cc.tail
    def nextInt() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
    def semiclone(half: Int) = new StepsIntLinearSeq[CC](underlying, half)
  }

  private[java8] class StepsAnyIterator[A](_underlying: Iterator[A])
  extends StepsLikeIterator[A, StepsAnyIterator[A]](_underlying) {
    def semiclone() = new StepsAnyIterator(null)
    def next() = if (proxied ne null) proxied.nextStep else underlying.next
  }

  private[java8] class StepsDoubleIterator(_underlying: Iterator[Double])
  extends StepsDoubleLikeIterator[StepsDoubleIterator](_underlying) {
    def semiclone() = new StepsDoubleIterator(null)
    def nextDouble() = if (proxied ne null) proxied.nextStep else underlying.next
  }

  private[java8] class StepsIntIterator(_underlying: Iterator[Int])
  extends StepsIntLikeIterator[StepsIntIterator](_underlying) {
    def semiclone() = new StepsIntIterator(null)
    def nextInt() = if (proxied ne null) proxied.nextStep else underlying.next
  }

  private[java8] class StepsLongIterator(_underlying: Iterator[Long])
  extends StepsLongLikeIterator[StepsLongIterator](_underlying) {
    def semiclone() = new StepsLongIterator(null)
    def nextLong() = if (proxied ne null) proxied.nextStep else underlying.next
  }

  private[java8] class StepsLongLinearSeq[CC >: Null <: collection.LinearSeqLike[Long, CC]](_underlying: CC, _maxN: Long)
  extends StepsLongWithTail[CC, StepsLongLinearSeq[CC]](_underlying, _maxN) {
    protected def myIsEmpty(cc: CC): Boolean = cc.isEmpty
    protected def myTailOf(cc: CC): CC = cc.tail
    def nextLong() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
    def semiclone(half: Int) = new StepsLongLinearSeq[CC](underlying, half)
  }

  private[java8] class StepsIntRange(underlying: Range, _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntRange](_i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntRange(underlying, i0, half)
  }

  private[java8] class StepsAnyNumericRange[T](underlying: collection.immutable.NumericRange[T], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[T, StepsAnyNumericRange[T]](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyNumericRange[T](underlying, i0, half)
  }

  private[java8] class StepsIntNumericRange(underlying: collection.immutable.NumericRange[Int], _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntNumericRange](_i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntNumericRange(underlying, i0, half)
  }

  private[java8] class StepsLongNumericRange(underlying: collection.immutable.NumericRange[Long], _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[StepsLongNumericRange](_i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongNumericRange(underlying, i0, half)
  }

  private[java8] trait StepsVectorLike[A] {
    protected def myVector: Vector[A]
    protected var index: Int = 32
    protected var data: Array[AnyRef] = null
    protected var index1: Int = 32
    protected var data1: Array[AnyRef] = null
    protected final def advanceData(iX: Int) {
      index1 += 1
      if (index >= 32) initTo(iX)
      else {
        data = data1(index1).asInstanceOf[Array[AnyRef]]
        index = 0
      }
    }
    protected final def initTo(iX: Int) {
      myVector.length match {
        case x if x <=  0x20 => 
          index = iX
          data = CollectionInternals.getDisplay0(myVector)
        case x if x <= 0x400 => 
          index1 = iX >>> 5
          data1 = CollectionInternals.getDisplay1(myVector)
          index = iX & 0x1F
          data = data1(index1).asInstanceOf[Array[AnyRef]]
        case x =>
          var N = 0
          var dataN: Array[AnyRef] = 
            if      (x <=    0x8000) { N = 2; CollectionInternals.getDisplay2(myVector) }
            else if (x <=  0x100000) { N = 3; CollectionInternals.getDisplay3(myVector) }
            else if (x <= 0x2000000) { N = 4; CollectionInternals.getDisplay4(myVector) }
            else  /*x <= 0x40000000*/{ N = 5; CollectionInternals.getDisplay5(myVector) }
          while (N > 2) {
            dataN = dataN((iX >>> (5*N))&0x1F).asInstanceOf[Array[AnyRef]]
            N -= 1
          }
          index1 = (iX >>> 5) & 0x1F
          data1 = dataN((iX >>> 10) & 0x1F).asInstanceOf[Array[AnyRef]]
          index = iX & 0x1F
          data = data1(index1).asInstanceOf[Array[AnyRef]]
      }
    }
  }

  private[java8] class StepsAnyVector[A](underlying: Vector[A], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, StepsAnyVector[A]](_i0, _iN) 
  with StepsVectorLike[A] {
    protected def myVector = underlying
    def next() = if (hasNext()) {
      index += 1
      if (index >= 32) advanceData(i0)
      i0 += 1
      data(index).asInstanceOf[A]
    } else throwNSEE
    def semiclone(half: Int) = {
      val ans = new StepsAnyVector(underlying, i0, half)
      index = 32
      index1 = 32
      i0 = half
      ans
    }
  }

  private[java8] class StepsDoubleVector(underlying: Vector[Double], _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[StepsDoubleVector](_i0, _iN)
  with StepsVectorLike[Double] {
    protected def myVector = underlying
    def nextDouble() = if (hasNext()) {
      index += 1
      if (index >= 32) advanceData(i0)
      i0 += 1
      data(index).asInstanceOf[Double]
    } else throwNSEE
    def semiclone(half: Int) = {
      val ans = new StepsDoubleVector(underlying, i0, half)
      index = 32
      index1 = 32
      i0 = half
      ans
    }    
  }

  private[java8] class StepsIntVector(underlying: Vector[Int], _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntVector](_i0, _iN)
  with StepsVectorLike[Int] {
    protected def myVector = underlying
    def nextInt() = if (hasNext()) {
      index += 1
      if (index >= 32) advanceData(i0)
      i0 += 1
      data(index).asInstanceOf[Int]
    } else throwNSEE
    def semiclone(half: Int) = {
      val ans = new StepsIntVector(underlying, i0, half)
      index = 32
      index1 = 32
      i0 = half
      ans
    }    
  }

  private[java8] class StepsLongVector(underlying: Vector[Long], _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[StepsLongVector](_i0, _iN)
  with StepsVectorLike[Long] {
    protected def myVector = underlying
    def nextLong() = if (hasNext()) {
      index += 1
      if (index >= 32) advanceData(i0)
      i0 += 1
      data(index).asInstanceOf[Long]
    } else throwNSEE
    def semiclone(half: Int) = {
      val ans = new StepsLongVector(underlying, i0, half)
      index = 32
      index1 = 32
      i0 = half
      ans
    }    
  }

  private[java8] class StepsAnyFlatHashTable[A](_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends StepsLikeGapped[A, StepsAnyFlatHashTable[A]](_underlying, _i0, _iN) {
    def next() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[A]; currentEntry = null; ans }
    def semiclone(half: Int) = new StepsAnyFlatHashTable[A](underlying, i0, half)
  }

  private[java8] class StepsDoubleFlatHashTable(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends StepsDoubleLikeGapped[StepsDoubleFlatHashTable](_underlying, _i0, _iN) {
    def nextDouble() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[Double]; currentEntry = null; ans }
    def semiclone(half: Int) = new StepsDoubleFlatHashTable(underlying, i0, half)    
  }

  private[java8] class StepsIntFlatHashTable(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends StepsIntLikeGapped[StepsIntFlatHashTable](_underlying, _i0, _iN) {
    def nextInt() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[Int]; currentEntry = null; ans }
    def semiclone(half: Int) = new StepsIntFlatHashTable(underlying, i0, half)    
  }

  private[java8] class StepsLongFlatHashTable(_underlying: Array[AnyRef], _i0: Int, _iN: Int)
  extends StepsLongLikeGapped[StepsLongFlatHashTable](_underlying, _i0, _iN) {
    def nextLong() = if (currentEntry eq null) throwNSEE else { val ans = currentEntry.asInstanceOf[Long]; currentEntry = null; ans }
    def semiclone(half: Int) = new StepsLongFlatHashTable(underlying, i0, half)    
  }

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

  private[java8] class StepsAnyImmHashSet[A](_underlying: Iterator[A], _N: Int)
  extends StepsLikeTrieIterator[A, StepsAnyImmHashSet[A]](_underlying, _N) {
    protected def demiclone(it: Iterator[A], N: Int) = new StepsAnyImmHashSet(it, N)
    def next(): A = { val ans = underlying.next; i += 1; ans }
  }

  private[java8] class StepsDoubleImmHashSet(_underlying: Iterator[Double], _N: Int)
  extends StepsDoubleLikeTrieIterator[StepsDoubleImmHashSet](_underlying, _N) {
    protected def demiclone(it: Iterator[Double], N: Int) = new StepsDoubleImmHashSet(it, N)
    def nextDouble() = { val ans = underlying.next; i += 1; ans }
  }

  private[java8] class StepsIntImmHashSet(_underlying: Iterator[Int], _N: Int)
  extends StepsIntLikeTrieIterator[StepsIntImmHashSet](_underlying, _N) {
    protected def demiclone(it: Iterator[Int], N: Int) = new StepsIntImmHashSet(it, N)
    def nextInt() = { val ans = underlying.next; i += 1; ans }
  }

  private[java8] class StepsLongImmHashSet(_underlying: Iterator[Long], _N: Int)
  extends StepsLongLikeTrieIterator[StepsLongImmHashSet](_underlying, _N) {
    protected def demiclone(it: Iterator[Long], N: Int) = new StepsLongImmHashSet(it, N)
    def nextLong() = { val ans = underlying.next; i += 1; ans }
  }

  private[java8] class StepsIntBitSet(_underlying: Array[Long], _i0: Int, _iN: Int)
  extends StepsIntLikeSliced[Array[Long], StepsIntBitSet](_underlying, _i0, _iN) {
    private var mask: Long = (-1L) << (i & 0x3F)
    private var cache: Long = underlying(i >>> 6)
    private var found: Boolean = false
    def semiclone(half: Int) = {
      val ans = new StepsIntBitSet(underlying, i, half)
      i = half
      mask = (-1L) << (i & 0x3F)
      cache = underlying(i >>> 6)
      found = false
      ans
    }
    def hasNext(): Boolean = found || ((i < iN) && {
      while ((mask & cache) == 0) {
        i += java.lang.Long.numberOfLeadingZeros(~mask)
        if (i < 0 || i >= iN) { i = iN; return false }
        mask = -1L
        cache = underlying(i >>> 6)
      }
      var m = mask << 1
      while ((mask & cache) == (m & cache)) {
        mask = m
        m = mask << 1
        i += 1
      }
      if (i < 0 || i >= iN) {
        i = iN
        false
      }
      else {
        found = true
        true
      }
    })
    def nextInt() = if (hasNext) { val j = i; found = false; mask = mask << 1; i += 1; j } else throwNSEE
  }

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

  private[java8] class StepperStringCodePoint(underlying: String, var i0: Int, var iN: Int) extends IntStepper with EfficientSubstep {
    def characteristics() = NonNull
    def estimateSize = iN - i0
    def hasNext = i0 < iN
    def nextInt() = {
      if (hasNext()) {
        val cp = underlying.codePointAt(i0)
        i0 += java.lang.Character.charCount(cp)
        cp
      }
      else throwNSEE
    }
    def substep() = {
      if (iN-3 > i0) {
        var half = (i0+iN) >>> 1
        if (java.lang.Character.isLowSurrogate(underlying.charAt(half))) half -= 1
        val ans = new StepperStringCodePoint(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
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
