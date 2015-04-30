package scala.compat.java8

import language.implicitConversions

package converterImpls {
  import StepConverters.SplitFlags._
  
  private[java8] abstract class StepperArrayAny[A, AA, STA >: Null <: StepperArrayAny[A, AA, _]](underlying: AA, var i0: Int, var iN: Int)
  extends StepperGeneric[A] {
    def semiclone(half: Int): STA
    def characteristics() = NonNull + Sized + Subsized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def substep(): StepperGeneric[A] = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = semiclone(half)
        i0 = half
        ans
      }
      else null
    }
  }
  
  private[java8] class StepperArrayGenObject[A <: Object](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepperArrayAny[A, Array[A], StepperArrayGenObject[A]](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenObject[A](underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenAny[A](underlying: Array[A], _i0: Int, _iN: Int)
  extends StepperArrayAny[A, Array[A], StepperArrayGenAny[A]](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenAny[A](underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenUnit(underlying: Array[Unit], _i0: Int, _iN: Int)
  extends StepperArrayAny[Unit, Array[Unit], StepperArrayGenUnit](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; () } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenUnit(underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenBoolean(underlying: Array[Boolean], _i0: Int, _iN: Int)
  extends StepperArrayAny[Boolean, Array[Boolean], StepperArrayGenBoolean](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenBoolean(underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenByte(underlying: Array[Byte], _i0: Int, _iN: Int)
  extends StepperArrayAny[Byte, Array[Byte], StepperArrayGenByte](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenByte(underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenChar(underlying: Array[Char], _i0: Int, _iN: Int)
  extends StepperArrayAny[Char, Array[Char], StepperArrayGenChar](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenChar(underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenShort(underlying: Array[Short], _i0: Int, _iN: Int)
  extends StepperArrayAny[Short, Array[Short], StepperArrayGenShort](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenShort(underlying, i0, half)
  }
  
  private[java8] class StepperArrayGenFloat(underlying: Array[Float], _i0: Int, _iN: Int)
  extends StepperArrayAny[Float, Array[Float], StepperArrayGenFloat](underlying, _i0, _iN) {
    def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def semiclone(half: Int) = new StepperArrayGenFloat(underlying, i0, half)
  }
  
  private[java8] class StepperArrayDouble(underlying: Array[Double], var i0: Int, var iN: Int) extends StepperDouble {
    def characteristics() = NonNull + Sized + Subsized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepperArrayDouble(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }

  private[java8] class StepperArrayInt(underlying: Array[Int], var i0: Int, var iN: Int) extends StepperInt {
    def characteristics() = NonNull + Sized + Subsized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepperArrayInt(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }

  private[java8] class StepperArrayLong(underlying: Array[Long], var i0: Int, var iN: Int) extends StepperLong {
    def characteristics() = NonNull + Sized + Subsized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throw new NoSuchElementException("Empty Stepper")
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepperArrayLong(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }
  
  final class RichArrayAnyCanStep[A](val underlying: Array[A]) extends AnyVal {
    @inline def stepper: Stepper[A, StepperGeneric[A]] = new StepperArrayGenAny[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayObjectCanStep[A <: Object](val underlying: Array[A]) extends AnyVal{
    @inline def stepper: Stepper[A, StepperGeneric[A]] = new StepperArrayGenObject[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayUnitCanStep(val underlying: Array[Unit]) extends AnyVal{
    @inline def stepper: Stepper[Unit, StepperGeneric[Unit]] = new StepperArrayGenUnit(underlying, 0, underlying.length)
  }
  
  final class RichArrayBooleanCanStep(val underlying: Array[Boolean]) extends AnyVal{
    @inline def stepper: Stepper[Boolean, StepperGeneric[Boolean]] = new StepperArrayGenBoolean(underlying, 0, underlying.length)
  }
  
  final class RichArrayByteCanStep(val underlying: Array[Byte]) extends AnyVal{
    @inline def stepper: Stepper[Byte, StepperGeneric[Byte]] = new StepperArrayGenByte(underlying, 0, underlying.length)
  }
  
  final class RichArrayCharCanStep(val underlying: Array[Char]) extends AnyVal{
    @inline def stepper: Stepper[Char, StepperGeneric[Char]] = new StepperArrayGenChar(underlying, 0, underlying.length)
  }
  
  final class RichArrayShortCanStep(val underlying: Array[Short]) extends AnyVal{
    @inline def stepper: Stepper[Short, StepperGeneric[Short]] = new StepperArrayGenShort(underlying, 0, underlying.length)
  }
  
  final class RichArrayFloatCanStep(val underlying: Array[Float]) extends AnyVal{
    @inline def stepper: Stepper[Float, StepperGeneric[Float]] = new StepperArrayGenFloat(underlying, 0, underlying.length)
  }
  
  trait Priority3StepConverters {
    implicit def richArrayAnyCanStep[A](underlying: Array[A]) = new RichArrayAnyCanStep[A](underlying)
  }
  
  trait Priority2StepConverters extends Priority3StepConverters {
    implicit def richArrayObjectCanStep[A <: Object](underlying: Array[A]) = new RichArrayObjectCanStep[A](underlying)
    implicit def richArrayUnitCanStep(underlying: Array[Unit]) = new RichArrayUnitCanStep(underlying)
    implicit def richArrayBooleanCanStep(underlying: Array[Boolean]) = new RichArrayBooleanCanStep(underlying)
    implicit def richArrayByteCanStep(underlying: Array[Byte]) = new RichArrayByteCanStep(underlying)
    implicit def richArrayCharCanStep(underlying: Array[Char]) = new RichArrayCharCanStep(underlying)
    implicit def richArrayShortCanStep(underlying: Array[Short]) = new RichArrayShortCanStep(underlying)
    implicit def richArrayFloatCanStep(underlying: Array[Float]) = new RichArrayFloatCanStep(underlying)
  }
}

object StepConverters extends converterImpls.Priority2StepConverters {
  object SplitFlags {
    final val Concurrent = java.util.Spliterator.CONCURRENT
    final val Distinct = java.util.Spliterator.DISTINCT
    final val Immutable = java.util.Spliterator.IMMUTABLE
    final val NonNull = java.util.Spliterator.NONNULL
    final val HasOrder = java.util.Spliterator.ORDERED
    final val Sized = java.util.Spliterator.SIZED
    final val Sorted = java.util.Spliterator.SORTED
    final val Subsized = java.util.Spliterator.SUBSIZED
  }
  import SplitFlags._
  import converterImpls._

  implicit class RichArrayDoubleCanStep(val underlying: Array[Double]) extends AnyVal {
    def stepper: Stepper[Double, StepperDouble] = new StepperArrayDouble(underlying, 0, underlying.length)
  }

  implicit class RichArrayIntCanStep(val underlying: Array[Int]) extends AnyVal {
    def stepper: Stepper[Int, StepperInt] = new StepperArrayInt(underlying, 0, underlying.length)
  }
  
  implicit class RichArrayLongCanStep(val underlying: Array[Long]) extends AnyVal {
    def stepper: Stepper[Long, StepperLong] = new StepperArrayLong(underlying, 0, underlying.length)
  }
}
