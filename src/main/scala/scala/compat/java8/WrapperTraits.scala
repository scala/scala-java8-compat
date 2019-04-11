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

/** A trait that indicates that the class is or can be converted to a Scala version by wrapping a Java class */
trait WrappedAsScala[S] { 
  /** Returns an appropriate Scala version */
  def asScala: S
}

/** A trait that indicates that the class is or can be converted to a Java version by wrapping a Scala class */
trait WrappedAsJava[J] {
  /** Returns an appropriate Java version */
  def asJava: J
}
