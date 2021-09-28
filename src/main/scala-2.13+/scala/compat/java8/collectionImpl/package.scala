/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.compat.java8

package object collectionImpl {
  type Accumulator[A] = scala.jdk.AnyAccumulator[A]
  val Accumulator = scala.jdk.AnyAccumulator

  type IntAccumulator = scala.jdk.IntAccumulator
  val IntAccumulator = scala.jdk.IntAccumulator

  type LongAccumulator = scala.jdk.LongAccumulator
  val LongAccumulator = scala.jdk.LongAccumulator

  type DoubleAccumulator = scala.jdk.DoubleAccumulator
  val DoubleAccumulator = scala.jdk.DoubleAccumulator

  type Stepper[A] = scala.collection.Stepper[A]
  val Stepper = scala.collection.Stepper

  type AnyStepper[A] = scala.collection.AnyStepper[A]
  val AnyStepper = scala.collection.AnyStepper

  type IntStepper = scala.collection.IntStepper
  val IntStepper = scala.collection.IntStepper

  type LongStepper = scala.collection.LongStepper
  val LongStepper = scala.collection.LongStepper

  type DoubleStepper = scala.collection.DoubleStepper
  val DoubleStepper = scala.collection.DoubleStepper
}
