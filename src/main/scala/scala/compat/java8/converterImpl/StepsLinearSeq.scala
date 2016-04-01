package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

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

private[java8] class StepsLongLinearSeq[CC >: Null <: collection.LinearSeqLike[Long, CC]](_underlying: CC, _maxN: Long)
extends StepsLongWithTail[CC, StepsLongLinearSeq[CC]](_underlying, _maxN) {
  protected def myIsEmpty(cc: CC): Boolean = cc.isEmpty
  protected def myTailOf(cc: CC): CC = cc.tail
  def nextLong() = if (hasNext()) { maxN -= 1; val ans = underlying.head; underlying = underlying.tail; ans } else throwNSEE
  def semiclone(half: Int) = new StepsLongLinearSeq[CC](underlying, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichLinearSeqCanStep[A, CC >: Null <: collection.LinearSeqLike[A, CC]](private val underlying: CC) extends AnyVal with MakesStepper[AnyStepper[A]] {
  @inline def stepper: AnyStepper[A] = new StepsAnyLinearSeq[A, CC](underlying, Long.MaxValue)
}

final class RichDoubleLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Double, CC]](private val underlying: CC) extends AnyVal with MakesStepper[DoubleStepper] {
  @inline def stepper: DoubleStepper = new StepsDoubleLinearSeq[CC](underlying, Long.MaxValue)
}

final class RichIntLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Int, CC]](private val underlying: CC) extends AnyVal with MakesStepper[IntStepper] {
  @inline def stepper: IntStepper = new StepsIntLinearSeq[CC](underlying, Long.MaxValue)
}

final class RichLongLinearSeqCanStep[CC >: Null <: collection.LinearSeqLike[Long, CC]](private val underlying: CC) extends AnyVal with MakesStepper[LongStepper] {
  @inline def stepper: LongStepper = new StepsLongLinearSeq[CC](underlying, Long.MaxValue)
}
