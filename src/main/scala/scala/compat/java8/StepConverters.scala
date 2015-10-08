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

  private[java8] class StepsAnyIndexedSeqOptimized[A](underlying: collection.IndexedSeqOptimized[A, _], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, StepsAnyIndexedSeqOptimized[A]](_i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyIndexedSeqOptimized[A](underlying, i0, half)
  }

  private[java8] class StepsDoubleIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Double, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[StepsDoubleIndexedSeqOptimized[CC]](_i0, _iN) {
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleIndexedSeqOptimized[CC](underlying, i0, half)
  }
  
  private[java8] class StepsIntIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Int, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[StepsIntIndexedSeqOptimized[CC]](_i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntIndexedSeqOptimized[CC](underlying, i0, half)
  }
  
  private[java8] class StepsLongIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Long, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[StepsLongIndexedSeqOptimized[CC]](_i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongIndexedSeqOptimized[CC](underlying, i0, half)
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

  final class RichArrayAnyCanStep[A](private val underlying: Array[A]) extends AnyVal {
    @inline def stepper: AnyStepper[A] = new StepsAnyArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayObjectCanStep[A <: Object](private val underlying: Array[A]) extends AnyVal{
    @inline def stepper: AnyStepper[A] = new StepsObjectArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayUnitCanStep(private val underlying: Array[Unit]) extends AnyVal{
    @inline def stepper: AnyStepper[Unit] = new StepsUnitArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayBooleanCanStep(private val underlying: Array[Boolean]) extends AnyVal{
    @inline def stepper: AnyStepper[Boolean] = new StepsBoxedBooleanArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayByteCanStep(private val underlying: Array[Byte]) extends AnyVal{
    @inline def stepper: AnyStepper[Byte] = new StepsBoxedByteArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayCharCanStep(private val underlying: Array[Char]) extends AnyVal {
    @inline def stepper: AnyStepper[Char] = new StepsBoxedCharArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayShortCanStep(private val underlying: Array[Short]) extends AnyVal{
    @inline def stepper: AnyStepper[Short] = new StepsBoxedShortArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayFloatCanStep(private val underlying: Array[Float]) extends AnyVal {
    @inline def stepper: AnyStepper[Float] = new StepsBoxedFloatArray(underlying, 0, underlying.length)
  }

  final class RichIndexedSeqOptimizedCanStep[A](private val underlying: collection.IndexedSeqOptimized[A, _]) extends AnyVal {
    @inline def stepper: AnyStepper[A] = new StepsAnyIndexedSeqOptimized[A](underlying, 0, underlying.length)
  }
  
  final class RichDoubleIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Double, _]](private val underlying: CC) extends AnyVal {
    @inline def stepper: DoubleStepper = new StepsDoubleIndexedSeqOptimized[CC](underlying, 0, underlying.length)
  }
  
  final class RichIntIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Int, _]](private val underlying: CC) extends AnyVal {
    @inline def stepper: IntStepper = new StepsIntIndexedSeqOptimized[CC](underlying, 0, underlying.length)
  }
  
  final class RichLongIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Long, _]](private val underlying: CC) extends AnyVal {
    @inline def stepper: LongStepper = new StepsLongIndexedSeqOptimized[CC](underlying, 0, underlying.length)
  }

  final class RichNumericRangeCanStep[T](private val underlying: collection.immutable.NumericRange[T]) extends AnyVal {
    @inline def stepper: AnyStepper[T] = new StepsAnyNumericRange[T](underlying, 0, underlying.length)
  }

  final class RichVectorCanStep[A](private val underlying: Vector[A]) extends AnyVal {
    @inline def stepper: AnyStepper[A] = new StepsAnyVector[A](underlying, 0, underlying.length)
  }

  final class RichFlatHashTableCanStep[A](private val underlying: collection.mutable.FlatHashTable[A]) extends AnyVal {
    @inline def stepper: AnyStepper[A] = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsAnyFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichDoubleFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Double]) extends AnyVal {
    @inline def stepper: DoubleStepper = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsDoubleFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichIntFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Int]) extends AnyVal {
    @inline def stepper: IntStepper = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsIntFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  final class RichLongFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Long]) extends AnyVal {
    @inline def stepper: LongStepper = {
      val tbl = runtime.CollectionInternals.getTable(underlying)
      new StepsLongFlatHashTable(tbl, 0, tbl.length)
    }
  }
  
  private[java8] class StepperStringCodePoint(underlying: String, var i0: Int, var iN: Int) extends IntStepper {
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

  final class RichTraversableOnceCanStep[A](private val underlying: TraversableOnce[A]) extends AnyVal {
    def stepper: AnyStepper[A] = {
      val acc = new Accumulator[A]
      underlying.foreach(acc += _)
      acc.stepper
    }
  }

  final class RichDoubleTraversableOnceCanStep(private val underlying: TraversableOnce[Double]) extends AnyVal {
    def stepper: DoubleStepper = {
      val acc = new DoubleAccumulator
      underlying.foreach(acc += _)
      acc.stepper
    }
  }

  final class RichIntTraversableOnceCanStep(private val underlying: TraversableOnce[Int]) extends AnyVal {
    def stepper: IntStepper = {
      val acc = new IntAccumulator
      underlying.foreach(acc += _)
      acc.stepper
    }
  }

  final class RichLongTraversableOnceCanStep(private val underlying: TraversableOnce[Long]) extends AnyVal {
    def stepper: LongStepper = {
      val acc = new LongAccumulator
      underlying.foreach(acc += _)
      acc.stepper
    }
  }

  trait Priority5StepConverters {
    implicit def richTraversableOnceCanStep[A](underlying: TraversableOnce[A]) = new RichTraversableOnceCanStep(underlying)
  }

  trait Priority4StepConverters extends Priority5StepConverters {
    implicit def richDoubleTraversableOnceCanStep(underlying: TraversableOnce[Double]) = new RichDoubleTraversableOnceCanStep(underlying)
    implicit def richIntTraversableOnceCanStep(underlying: TraversableOnce[Int]) = new RichIntTraversableOnceCanStep(underlying)
    implicit def richLongTraversableOnceCanStep(underlying: TraversableOnce[Long]) = new RichLongTraversableOnceCanStep(underlying)
  }
  
  trait Priority3StepConverters extends Priority4StepConverters {
    implicit def richArrayAnyCanStep[A](underlying: Array[A]) = new RichArrayAnyCanStep[A](underlying)
    implicit def richIndexedSeqOptimizedCanStep[A](underlying: collection.IndexedSeqOptimized[A, _]) =
      new RichIndexedSeqOptimizedCanStep[A](underlying)
    implicit def richFlatHashTableCanStep[A](underlying: collection.mutable.FlatHashTable[A]) = new RichFlatHashTableCanStep[A](underlying)
  }
  
  trait Priority2StepConverters extends Priority3StepConverters {
    implicit def richArrayObjectCanStep[A <: Object](underlying: Array[A]) = new RichArrayObjectCanStep[A](underlying)
    implicit def richArrayUnitCanStep(underlying: Array[Unit]) = new RichArrayUnitCanStep(underlying)
    implicit def richArrayBooleanCanStep(underlying: Array[Boolean]) = new RichArrayBooleanCanStep(underlying)
    implicit def richArrayByteCanStep(underlying: Array[Byte]) = new RichArrayByteCanStep(underlying)
    implicit def richArrayCharCanStep(underlying: Array[Char]) = new RichArrayCharCanStep(underlying)
    implicit def richArrayShortCanStep(underlying: Array[Short]) = new RichArrayShortCanStep(underlying)
    implicit def richArrayFloatCanStep(underlying: Array[Float]) = new RichArrayFloatCanStep(underlying)
    implicit def richDoubleIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Double, _]](underlying: CC) =
      new RichDoubleIndexedSeqOptimizedCanStep[CC](underlying)
    implicit def richIntIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Int, _]](underlying: CC) =
      new RichIntIndexedSeqOptimizedCanStep[CC](underlying)
    implicit def richLongIndexedSeqOptimizedCanStep[CC <: collection.IndexedSeqOptimized[Long, _]](underlying: CC) =
      new RichLongIndexedSeqOptimizedCanStep[CC](underlying)
    implicit def richNumericRangeCanStep[T](underlying: collection.immutable.NumericRange[T]) = new RichNumericRangeCanStep(underlying)
    implicit def richVectorCanStep[A](underlying: Vector[A]) = new RichVectorCanStep[A](underlying)
    implicit def richDoubleFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Double]) = new RichDoubleFlatHashTableCanStep(underlying)
    implicit def richIntFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Int]) = new RichIntFlatHashTableCanStep(underlying)
    implicit def richLongFlatHashTableCanStep(underlying: collection.mutable.FlatHashTable[Long]) = new RichLongFlatHashTableCanStep(underlying)
  }
}

object StepConverters extends converterImpls.Priority2StepConverters {
  import converterImpls._
  import Stepper._

  implicit class RichArrayDoubleCanStep(val underlying: Array[Double]) extends AnyVal {
    @inline def stepper: DoubleStepper = new StepsDoubleArray(underlying, 0, underlying.length)
  }

  implicit class RichArrayIntCanStep(val underlying: Array[Int]) extends AnyVal {
    @inline def stepper: IntStepper = new StepsIntArray(underlying, 0, underlying.length)
  }
  
  implicit class RichArrayLongCanStep(val underlying: Array[Long]) extends AnyVal {
    @inline def stepper: LongStepper = new StepsLongArray(underlying, 0, underlying.length)
  }
  
  implicit class RichDoubleVectorCanStep[A](private val underlying: Vector[Double]) extends AnyVal {
    @inline def stepper: DoubleStepper = new StepsDoubleVector(underlying, 0, underlying.length)
  }

  implicit final class RichIntNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Int]) extends AnyVal {
    @inline def stepper: IntStepper = new StepsIntNumericRange(underlying, 0, underlying.length)
  }

  implicit final class RichLongNumericRangeCanStep(private val underlying: collection.immutable.NumericRange[Long]) extends AnyVal {
    @inline def stepper: LongStepper = new StepsLongNumericRange(underlying, 0, underlying.length)
  }

  implicit final class RichRangeCanStep(private val underlying: Range) extends AnyVal {
    @inline def stepper: IntStepper = new StepsIntRange(underlying, 0, underlying.length)
  }  

  implicit class RichIntVectorCanStep[A](private val underlying: Vector[Int]) extends AnyVal {
    @inline def stepper: IntStepper = new StepsIntVector(underlying, 0, underlying.length)
  }

  implicit class RichLongVectorCanStep[A](private val underlying: Vector[Long]) extends AnyVal {
    @inline def stepper: LongStepper = new StepsLongVector(underlying, 0, underlying.length)
  }

  implicit class RichStringCanStep(val underlying: String) extends AnyVal {
    @inline def stepper: IntStepper = new StepperStringCodePoint(underlying, 0, underlying.length)
  }
}
