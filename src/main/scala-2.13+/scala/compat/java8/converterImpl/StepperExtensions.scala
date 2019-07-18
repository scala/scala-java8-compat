package scala.compat.java8.converterImpl

import scala.collection.convert.StreamExtensions.AccumulatorFactoryInfo
import scala.compat.java8.collectionImpl.{DoubleAccumulator, IntAccumulator, LongAccumulator, Stepper}
import scala.jdk.AnyAccumulator

class StepperExtensions[@specialized(Double, Int, Long) A](private val s: Stepper[A]) {
  def accumulate[C](implicit info: AccumulatorFactoryInfo[A, C]): C = {
    info.companion match {
      case IntAccumulator =>
        val a = new IntAccumulator()
        val is = s.asInstanceOf[Stepper[Int]]
        while (is.hasStep) a += is.nextStep()
        a.asInstanceOf[C]
      case LongAccumulator =>
        val a = new LongAccumulator()
        val is = s.asInstanceOf[Stepper[Long]]
        while (is.hasStep) a += is.nextStep()
        a.asInstanceOf[C]
      case DoubleAccumulator =>
        val a = new DoubleAccumulator()
        val is = s.asInstanceOf[Stepper[Double]]
        while (is.hasStep) a += is.nextStep()
        a.asInstanceOf[C]
      case AnyAccumulator | null =>
        val a = new AnyAccumulator[A]
        while (s.hasStep) a += s.nextStep()
        a.asInstanceOf[C]
    }
  }

  def substep(): Stepper[A] = s.trySplit()

  /** Consumes all remaining elements in this `Stepper` and counts how many there are.
    * This is a terminal operation.
    */
  def count(): Long = { var n = 0L; while (s.hasStep) { s.nextStep(); n += 1 }; n }

  /** Consumes all remaining elements in this `Stepper` and counts how many satisfy condition `p`.
    * This is a terminal operation.
    */
  def count(p: A => Boolean): Long = { var n = 0L; while (s.hasStep) { if (p(s.nextStep())) n += 1 }; n }

  /** Searches for an element that satisfies condition `p`.  If none are found, it returns `false`.
    * This is a terminal operation.
    */
  def exists(p: A => Boolean): Boolean = { while(s.hasStep) { if (p(s.nextStep())) return true }; false }

  /** Searches for an element that satisifes condition `p`, returning it wrapped in `Some` if one is found, or `None` otherwise.
    * This is a terminal operation.
    */
  def find(p: A => Boolean): Option[A] = { while (s.hasStep) { val a = s.nextStep(); if (p(a)) return Some(a) }; None }

  /** Repeatedly applies `op` to propagate an initial value `zero` through all elements of the collection.
    * Traversal order is left-to-right.
    * This is a terminal operation.
    */
  def fold[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B) = { var b = zero; while (s.hasStep) { b = op(b, s.nextStep()) }; b }

  /** Repeatedly applies `op` to propagate an initial value `zero` through the collection until a condition `p` is met.
    * If `p` is never met, the result of the last operation is returned.
    * This is a terminal operation.
    */
  def foldTo[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B)(p: B => Boolean) = { var b = zero; while (!p(b) && s.hasStep) { b = op(b, s.nextStep()) }; b }

  /** Applies `f` to every remaining element in the collection.
    * This is a terminal operation.
    */
  def foreach(f: A => Unit): Unit = { while (s.hasStep) f(s.nextStep()) }

  /** Repeatedly merges elements with `op` until only a single element remains.
    * Throws an exception if the `Stepper` is empty.
    * Merging occurs from left to right.
    * This is a terminal operation.
    */
  def reduce(op: (A, A) => A): A = { var a = s.nextStep(); while (s.hasStep) { a = op(a, s.nextStep()) }; a }
}
