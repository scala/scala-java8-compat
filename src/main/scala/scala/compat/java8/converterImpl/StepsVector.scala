package scala.compat.java8.converterImpl

import scala.annotation.switch

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

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

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichVectorCanStep[T](private val underlying: Vector[T]) extends AnyVal with MakesStepper[T, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = ((ss.shape: @switch) match {
    case StepperShape.IntValue    => new StepsIntVector   (underlying.asInstanceOf[Vector[Int]],    0, underlying.length)
    case StepperShape.LongValue   => new StepsLongVector  (underlying.asInstanceOf[Vector[Long]],   0, underlying.length)
    case StepperShape.DoubleValue => new StepsDoubleVector(underlying.asInstanceOf[Vector[Double]], 0, underlying.length)
    case _            => ss.parUnbox(new StepsAnyVector[T](underlying,                              0, underlying.length))
  }).asInstanceOf[S with EfficientSubstep]
}
