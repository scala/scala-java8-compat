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

import org.junit.Test
import SpecializedTestSupport.IntIdentity

class SpecializedTest {
  @Test def specializationWorks(): Unit = {
    val intIdentity: (Int => Int) = new IntIdentity().asInstanceOf[Int => Int]
    intIdentity(24) // this function checks that it was called via the specialized apply variant.
  }
}
