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
  
  private[java8] class StepsDoubleArray(underlying: Array[Double], var i0: Int, var iN: Int) extends DoubleStepper {
    def characteristics() = NonNull + Sized + SubSized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepsDoubleArray(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }

  private[java8] class StepsIntArray(underlying: Array[Int], var i0: Int, var iN: Int) extends IntStepper {
    def characteristics() = NonNull + Sized + SubSized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepsIntArray(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }

  private[java8] class StepsLongArray(underlying: Array[Long], var i0: Int, var iN: Int) extends LongStepper {
    def characteristics() = NonNull + Sized + SubSized
    def estimateSize() = iN - i0
    def hasNext() = i0 < iN
    def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
    def substep() = {
      if (iN-1 > i0) {
        val half = (i0+iN) >>> 1
        val ans = new StepsLongArray(underlying, i0, half)
        i0 = half
        ans
      }
      else null
    }
  }
  
  final class RichArrayAnyCanStep[A](val underlying: Array[A]) extends AnyVal {
    @inline def stepper: AnyStepper[A] = new StepsAnyArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayObjectCanStep[A <: Object](val underlying: Array[A]) extends AnyVal{
    @inline def stepper: AnyStepper[A] = new StepsObjectArray[A](underlying, 0, underlying.length)
  }
  
  final class RichArrayUnitCanStep(val underlying: Array[Unit]) extends AnyVal{
    @inline def stepper: AnyStepper[Unit] = new StepsUnitArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayBooleanCanStep(val underlying: Array[Boolean]) extends AnyVal{
    @inline def stepper: AnyStepper[Boolean] = new StepsBoxedBooleanArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayByteCanStep(val underlying: Array[Byte]) extends AnyVal{
    @inline def stepper: AnyStepper[Byte] = new StepsBoxedByteArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayCharCanStep(val underlying: Array[Char]) extends AnyVal{
    @inline def stepper: AnyStepper[Char] = new StepsBoxedCharArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayShortCanStep(val underlying: Array[Short]) extends AnyVal{
    @inline def stepper: AnyStepper[Short] = new StepsBoxedShortArray(underlying, 0, underlying.length)
  }
  
  final class RichArrayFloatCanStep(val underlying: Array[Float]) extends AnyVal{
    @inline def stepper: AnyStepper[Float] = new StepsBoxedFloatArray(underlying, 0, underlying.length)
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
