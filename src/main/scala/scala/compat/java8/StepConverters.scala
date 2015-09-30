package scala.compat.java8

import language.implicitConversions

import scala.compat.java8.collectionImpl._

package converterImpls {
  import Stepper._
    
  private[java8] class StepsObjectArray[A <: Object](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, Array[A], StepsObjectArray[A]](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsObjectArray[A](underlying, i0, half)
  }
  
  private[java8] class StepsAnyArray[A](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, Array[A], StepsAnyArray[A]](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyArray[A](underlying, i0, half)
  }
  
  private[java8] class StepsUnitArray(underlying: Array[Unit], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Unit, Array[Unit], StepsUnitArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; () } else throwNSEE
    def semiclone(half: Int) = new StepsUnitArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedBooleanArray(underlying: Array[Boolean], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Boolean, Array[Boolean], StepsBoxedBooleanArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedBooleanArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedByteArray(underlying: Array[Byte], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Byte, Array[Byte], StepsBoxedByteArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedByteArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedCharArray(underlying: Array[Char], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Char, Array[Char], StepsBoxedCharArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedCharArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedShortArray(underlying: Array[Short], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Short, Array[Short], StepsBoxedShortArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedShortArray(underlying, i0, half)
  }
  
  private[java8] class StepsBoxedFloatArray(underlying: Array[Float], _i0: Int, _iN: Int)
  extends StepsLikeIndexed[Float, Array[Float], StepsBoxedFloatArray](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsBoxedFloatArray(underlying, i0, half)
  }
  
  private[java8] class StepsDoubleArray(underlying: Array[Double], _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[Array[Double], StepsDoubleArray](underlying, _i0, _iN) {
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleArray(underlying, i0, half)
  }

  private[java8] class StepsIntArray(underlying: Array[Int], _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[Array[Int], StepsIntArray](underlying, _i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntArray(underlying, i0, half)
  }

  private[java8] class StepsLongArray(underlying: Array[Long], _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[Array[Long], StepsLongArray](underlying, _i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongArray(underlying, i0, half)
  }

  private[java8] class StepsAnyIndexedSeqOptimized[A, CC <: collection.IndexedSeqOptimized[A, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsLikeIndexed[A, CC, StepsAnyIndexedSeqOptimized[A, CC]](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsAnyIndexedSeqOptimized[A, CC](underlying, i0, half)
  }

  private[java8] class StepsDoubleIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Double, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsDoubleLikeIndexed[CC, StepsDoubleIndexedSeqOptimized[CC]](underlying, _i0, _iN) {
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsDoubleIndexedSeqOptimized[CC](underlying, i0, half)
  }
  
  private[java8] class StepsIntIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Int, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsIntLikeIndexed[CC, StepsIntIndexedSeqOptimized[CC]](underlying, _i0, _iN) {
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsIntIndexedSeqOptimized[CC](underlying, i0, half)
  }
  
  private[java8] class StepsLongIndexedSeqOptimized[CC <: collection.IndexedSeqOptimized[Long, _]](underlying: CC, _i0: Int, _iN: Int)
  extends StepsLongLikeIndexed[CC, StepsLongIndexedSeqOptimized[CC]](underlying, _i0, _iN) {
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def semiclone(half: Int) = new StepsLongIndexedSeqOptimized[CC](underlying, i0, half)
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

  final class RichIndexedSeqOptimizedCanStep[A, CC <: collection.IndexedSeqOptimized[A, _]](private val underlying: CC) extends AnyVal {
    @inline def stepper: AnyStepper[A] = new StepsAnyIndexedSeqOptimized[A, CC](underlying, 0, underlying.length)
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
  
  trait Priority3StepConverters {
    implicit def richArrayAnyCanStep[A](underlying: Array[A]) = new RichArrayAnyCanStep[A](underlying)
    implicit def richIndexedSeqOptimizedCanStep[A, CC <: collection.IndexedSeqOptimized[A, _]](underlying: CC) = new RichIndexedSeqOptimizedCanStep[A, CC](underlying)
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
  
  implicit class RichStringCanStep(val underlying: String) extends AnyVal {
    @inline def stepper: IntStepper = new StepperStringCodePoint(underlying, 0, underlying.length)
  }
}
