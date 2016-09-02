/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8

import org.junit.Test
import SpecializedTestSupport.IntIdentity

class SpecializedTest {
  @Test def specializationWorks() {
    val intIdentity: (Int => Int) = new IntIdentity().asInstanceOf[Int => Int]
    intIdentity(24) // this function checks that it was called via the specialized apply variant.
  }
}
