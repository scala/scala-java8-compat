package scala.compat.java8

import language.implicitConversions

import java.util._
import scala.compat.java8.collectionImpl._

package SpliteratorConverters {
  class SpliteratorToStepper[A] private[java8] (private val underlying: Spliterator[A]) extends AnyVal {
    def stepper: AnyStepper[A] = Stepper.ofSpliterator(underlying)
  }
  
  trait Priority2SpliteratorConverters {
    implicit def spliteratorToStepper[A](sp: Spliterator[A]) = new SpliteratorToStepper[A](sp)
  }
}


package object SpliteratorConverters extends SpliteratorConverters.Priority2SpliteratorConverters {
  implicit final class SpliteratorOfDoubleToStepper(private val underlying: Spliterator.OfDouble) extends AnyVal {
    def stepper: DoubleStepper = Stepper.ofSpliterator(underlying)
  }
  implicit final class SpliteratorOfIntToStepper(private val underlying: Spliterator.OfInt) extends AnyVal {
    def stepper: IntStepper = Stepper.ofSpliterator(underlying)
  }
  implicit final class SpliteratorOfLongToStepper(private val underlying: Spliterator.OfLong) extends AnyVal {
    def stepper: LongStepper = Stepper.ofSpliterator(underlying)
  }
}
