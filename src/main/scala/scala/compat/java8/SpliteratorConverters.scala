package scala.compat.java8

import language.implicitConversions

import java.util._
import scala.compat.java8.collectionImpl._

package SpliteratorConverters {
  class SpliteratorToStepper[A] private[java8] (private val underlying: Spliterator[A]) extends AnyVal {
    def stepper: StepperGeneric[A] = Stepper.ofSpliterator(underlying)
  }
  
  trait Priority2SpliteratorConverters {
    implicit def spliteratorToStepper[A](sp: Spliterator[A]) = new SpliteratorToStepper[A](sp)
  }
}


package object SpliteratorConverters extends SpliteratorConverters.Priority2SpliteratorConverters {
  implicit class SpliteratorOfDoubleToStepper(private val underlying: Spliterator.OfDouble) extends AnyVal {
    def stepper: StepperDouble = Stepper.ofSpliterator(underlying)
  }
  implicit class SpliteratorOfIntToStepper(private val underlying: Spliterator.OfInt) extends AnyVal {
    def stepper: StepperInt = Stepper.ofSpliterator(underlying)
  }
  implicit class SpliteratorOfLongToStepper(private val underlying: Spliterator.OfLong) extends AnyVal {
    def stepper: StepperLong = Stepper.ofSpliterator(underlying)
  }
}
