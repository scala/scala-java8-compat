package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

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

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichFlatHashTableCanStep[A](private val underlying: collection.mutable.FlatHashTable[A]) extends AnyVal with MakesStepper[AnyStepper[A] with EfficientSubstep] {
  @inline def stepper: AnyStepper[A] with EfficientSubstep = {
    val tbl = CollectionInternals.getTable(underlying)
    new StepsAnyFlatHashTable(tbl, 0, tbl.length)
  }
}

final class RichDoubleFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Double]) extends AnyVal with MakesStepper[DoubleStepper with EfficientSubstep] {
  @inline def stepper: DoubleStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable(underlying)
    new StepsDoubleFlatHashTable(tbl, 0, tbl.length)
  }
}

final class RichIntFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Int]) extends AnyVal with MakesStepper[IntStepper with EfficientSubstep] {
  @inline def stepper: IntStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable(underlying)
    new StepsIntFlatHashTable(tbl, 0, tbl.length)
  }
}

final class RichLongFlatHashTableCanStep(private val underlying: collection.mutable.FlatHashTable[Long]) extends AnyVal with MakesStepper[LongStepper with EfficientSubstep] {
  @inline def stepper: LongStepper with EfficientSubstep = {
    val tbl = CollectionInternals.getTable(underlying)
    new StepsLongFlatHashTable(tbl, 0, tbl.length)
  }
}
