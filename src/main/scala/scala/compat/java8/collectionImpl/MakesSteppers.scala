package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

/** Classes or objects implementing this trait create generic steppers suitable for sequential use. */
trait MakesAnySeqStepper[A] extends Any {
  /** Generates a fresh stepper over `A`s suitable for sequential use */
  def stepper: AnyStepper[A] 
}

/** Classes or objects implementing this trait create generic steppers for map keys suitable for sequential use. */
trait MakesAnyKeySeqStepper[A] extends Any {
  /** Generates a fresh stepper over map keys of type `A` suitable for sequential use */
  def keyStepper: AnyStepper[A] 
}

/** Classes or objects implementing this trait create generic steppers for map values suitable for sequential use. */
trait MakesAnyValueSeqStepper[A] extends Any {
  /** Generates a fresh stepper over map values of type `A` suitable for sequential use */
  def valueStepper: AnyStepper[A] 
}

/** Classes or objects implementing this trait create `Double` steppers suitable for sequential use. */
trait MakesDoubleSeqStepper extends Any {
  /** Generates a fresh stepper over `Double`s suitable for sequential use */
  def stepper: DoubleStepper 
}

/** Classes or objects implementing this trait create `Double` steppers for map keys suitable for sequential use. */
trait MakesDoubleKeySeqStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Double` suitable for sequential use */
  def keyStepper: DoubleStepper 
}

/** Classes or objects implementing this trait create `Double` steppers  for map values suitable for sequential use. */
trait MakesDoubleValueSeqStepper extends Any {
  /** Generates a fresh stepper over map values of type `Double` suitable for sequential use */
  def valueStepper: DoubleStepper 
}

/** Classes or objects implementing this trait create `Int` steppers suitable for sequential use. */
trait MakesIntSeqStepper extends Any {
  /** Generates a fresh stepper over `Int`s suitable for sequential use */
  def stepper: IntStepper 
}

/** Classes or objects implementing this trait create `Int` steppers for map keys suitable for sequential use. */
trait MakesIntKeySeqStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Int` suitable for sequential use */
  def keyStepper: IntStepper 
}

/** Classes or objects implementing this trait create `Int` steppers for map values suitable for sequential use. */
trait MakesIntValueSeqStepper extends Any {
  /** Generates a fresh stepper over map values of type `Int` suitable for sequential use */
  def valueStepper: IntStepper 
}

/** Classes or objects implementing this trait create `Long` steppers suitable for sequential use. */
trait MakesLongSeqStepper extends Any {
  /** Generates a fresh stepper over `Long`s suitable for sequential use */
  def stepper: LongStepper 
}

/** Classes or objects implementing this trait create `Long` steppers for map keys suitable for sequential use. */
trait MakesLongKeySeqStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Long` suitable for sequential use */
  def keyStepper: LongStepper 
}

/** Classes or objects implementing this trait create `Long` steppers for map values suitable for sequential use. */
trait MakesLongValueSeqStepper extends Any {
  /** Generates a fresh stepper over map values of type `Long` suitable for sequential use */
  def valueStepper: LongStepper 
}

/** Classes or objects implementing this trait create generic steppers suitable for sequential or parallel use. */
trait MakesAnyStepper[A] extends Any {
  /** Generates a fresh stepper over `A`s that can be efficiently subdivided */
  def stepper: AnyStepper[A] with EfficientSubstep
}

/** Classes or objects implementing this trait create generic steppers for map keys suitable for sequential or parallel use. */
trait MakesAnyKeyStepper[A] extends Any {
  /** Generates a fresh stepper over map keys of type `A` that can be efficiently subdivided */
  def keyStepper: AnyStepper[A] with EfficientSubstep
}

/** Classes or objects implementing this trait create generic steppers for map values suitable for sequential or parallel use. */
trait MakesAnyValueStepper[A] extends Any {
  /** Generates a fresh stepper over map values of type `A` that can be efficiently subdivided */
  def valueStepper: AnyStepper[A] with EfficientSubstep
}

/** Classes or objects implementing this trait create `Double` steppers suitable for sequential or parallel use. */
trait MakesDoubleStepper extends Any {
  /** Generates a fresh stepper over `Double`s that can be efficiently subdivided */
  def stepper: DoubleStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Double` steppers for map keys suitable for sequential or parallel use. */
trait MakesDoubleKeyStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Double` that can be efficiently subdivided */
  def keyStepper: DoubleStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Double` steppers for map values suitable for sequential or parallel use. */
trait MakesDoubleValueStepper extends Any {
  /** Generates a fresh stepper over map values of type `Double` that can be efficiently subdivided */
  def valueStepper: DoubleStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Int` steppers suitable for sequential or parallel use. */
trait MakesIntStepper extends Any {
  /** Generates a fresh stepper over `Int`s that can be efficiently subdivided */
  def stepper: IntStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Int` steppers for map keys suitable for sequential or parallel use. */
trait MakesIntKeyStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Int` that can be efficiently subdivided */
  def keyStepper: IntStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Int` steppers for map values suitable for sequential or parallel use. */
trait MakesIntValueStepper extends Any {
  /** Generates a fresh stepper over map values of type `Int` that can be efficiently subdivided */
  def valueStepper: IntStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Long` steppers suitable for sequential or parallel use. */
trait MakesLongStepper extends Any {
  /** Generates a fresh stepper over `Long`s that can be efficiently subdivided */
  def stepper: LongStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Long` steppers for map keys suitable for sequential or parallel use. */
trait MakesLongKeyStepper extends Any {
  /** Generates a fresh stepper over map keys of type `Long` that can be efficiently subdivided */
  def keyStepper: LongStepper with EfficientSubstep
}

/** Classes or objects implementing this trait create `Long` steppers for map values suitable for sequential or parallel use. */
trait MakesLongValueStepper extends Any {
  /** Generates a fresh stepper over map values of type `Long` that can be efficiently subdivided */
  def valueStepper: LongStepper with EfficientSubstep
}
