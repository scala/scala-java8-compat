package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

trait MakesStepper[T, +Extra] extends Any {
  /** Generates a fresh stepper of type `S` for element type `T` */
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]): S with Extra
}

trait MakesKeyValueStepper[K, V, +Extra] extends Any {
  /** Generates a fresh stepper of type `S` over map keys of type `K` */
  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]): S with Extra

  /** Generates a fresh stepper of type `S` over map values of type `V` */
  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]): S with Extra
}

sealed trait StepperShape[T, S <: Stepper[_]] { def ref: Boolean }
object StepperShape extends StepperShapeLowPrio {
  private[this] def valueShape[T, S <: Stepper[_]]: StepperShape[T, S] = new StepperShape[T, S] { def ref = false }

  // primitive
  implicit val IntValue     = valueShape[Int, IntStepper]
  implicit val LongValue    = valueShape[Long, LongStepper]
  implicit val DoubleValue  = valueShape[Double, DoubleStepper]

  // widening
  implicit val ByteValue    = valueShape[Byte, IntStepper]
  implicit val ShortValue   = valueShape[Short, IntStepper]
  implicit val CharValue    = valueShape[Char, IntStepper]
  implicit val FloatValue   = valueShape[Float, DoubleStepper]
}
trait StepperShapeLowPrio {
  // reference
  implicit def anyStepperShape[T]: StepperShape[T, AnyStepper[T]] = new StepperShape[T, AnyStepper[T]] { def ref = true }
}

/** Superclass for `MakesStepper` implementations which support parallelization. At least the `AnyStepper` case must be
  * implemented, all others default to building an `AnyStepper` and putting an unboxing conversion on top. */
trait MakesParStepper[T] extends Any with MakesStepper[T, EfficientSubstep] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Int   ]]) with EfficientSubstep
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Long  ]]) with EfficientSubstep
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Double]]) with EfficientSubstep
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Byte  ]]) with EfficientSubstep
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Short ]]) with EfficientSubstep
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Char  ]]) with EfficientSubstep
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Float ]]) with EfficientSubstep
    case _                        => throw new NotImplementedError("AnyStepper must be handled in `stepper` implementations")
  }).asInstanceOf[S with EfficientSubstep]
}

/** Superclass for `MakesStepper` implementations which do not support parallelization. At least the `AnyStepper` case must be
  * implemented, all others default to building an `AnyStepper` and putting an unboxing conversion on top. */
trait MakesSeqStepper[T] extends Any with MakesStepper[T, Any] {
  def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Int   ]])
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Long  ]])
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Double]])
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Byte  ]])
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Short ]])
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Char  ]])
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (stepper(StepperShape.anyStepperShape[T]).asInstanceOf[AnyStepper[Float ]])
    case _                        => throw new NotImplementedError("AnyStepper must be handled in `stepper` implementations")
  }).asInstanceOf[S]
}

/** Superclass for `MakesKeyalueStepper` implementations which support parallelization. At least the `AnyStepper` case must be
  * implemented, all others default to building an `AnyStepper` and putting an unboxing conversion on top. */
trait MakesKeyValueParStepper[K, V] extends Any with MakesKeyValueStepper[K, V, EfficientSubstep] {
  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Int   ]]) with EfficientSubstep
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Long  ]]) with EfficientSubstep
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Double]]) with EfficientSubstep
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Byte  ]]) with EfficientSubstep
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Short ]]) with EfficientSubstep
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Char  ]]) with EfficientSubstep
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Float ]]) with EfficientSubstep
    case _                        => throw new NotImplementedError("AnyStepper case must be handled in `keyStepper` implementations")
  }).asInstanceOf[S with EfficientSubstep]

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Int   ]]) with EfficientSubstep
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Long  ]]) with EfficientSubstep
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Double]]) with EfficientSubstep
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Byte  ]]) with EfficientSubstep
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Short ]]) with EfficientSubstep
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Char  ]]) with EfficientSubstep
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Float ]]) with EfficientSubstep
    case _                        => throw new NotImplementedError("AnyStepper case must be handled in `valueStepper` implementations")
  }).asInstanceOf[S with EfficientSubstep]
}

/** Superclass for `MakesKeyalueStepper` implementations which do not support parallelization. At least the `AnyStepper` case must be
  * implemented, all others default to building an `AnyStepper` and putting an unboxing conversion on top. */
trait MakesKeyValueSeqStepper[K, V] extends Any with MakesKeyValueStepper[K, V, Any] {
  def keyStepper[S <: Stepper[_]](implicit ss: StepperShape[K, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Int   ]])
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Long  ]])
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Double]])
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Byte  ]])
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Short ]])
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Char  ]])
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (keyStepper(StepperShape.anyStepperShape[K]).asInstanceOf[AnyStepper[Float ]])
    case _                        => throw new NotImplementedError("AnyStepper case must be handled in `keyStepper` implementations")
  }).asInstanceOf[S]

  def valueStepper[S <: Stepper[_]](implicit ss: StepperShape[V, S]) = (ss match {
    case StepperShape.IntValue    => new Stepper.UnboxingIntStepper   (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Int   ]])
    case StepperShape.LongValue   => new Stepper.UnboxingLongStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Long  ]])
    case StepperShape.DoubleValue => new Stepper.UnboxingDoubleStepper(valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Double]])
    case StepperShape.ByteValue   => new Stepper.UnboxingByteStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Byte  ]])
    case StepperShape.ShortValue  => new Stepper.UnboxingShortStepper (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Short ]])
    case StepperShape.CharValue   => new Stepper.UnboxingCharStepper  (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Char  ]])
    case StepperShape.FloatValue  => new Stepper.UnboxingFloatStepper (valueStepper(StepperShape.anyStepperShape[V]).asInstanceOf[AnyStepper[Float ]])
    case _                        => throw new NotImplementedError("AnyStepper case must be handled in `valueStepper` implementations")
  }).asInstanceOf[S]
}
