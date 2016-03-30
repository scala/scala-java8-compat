package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

/** Classes or objects implementing this trait create streams suitable for sequential use */
trait MakesSequentialStream[A, SS <: java.util.stream.BaseStream[A, SS]] extends Any {
  def seqStream: SS
}

/** Classes or objects implementing this trait create streams suitable for parallel use */
trait MakesParallelStream[A, SS <: java.util.stream.BaseStream[A, SS]] extends Any {
  def parStream: SS
}

trait MakesStepper[+T <: Stepper[_]] extends Any {
  /** Generates a fresh stepper of type `T` */
  def stepper: T
}

trait MakesKeyStepper[+T <: Stepper[_]] extends Any {
  /** Generates a fresh stepper of type `T` over map keys */
  def keyStepper: T
}

trait MakesValueStepper[+T <: Stepper[_]] extends Any {
  /** Generates a fresh stepper of type `T` over map values */
  def valueStepper: T
}
