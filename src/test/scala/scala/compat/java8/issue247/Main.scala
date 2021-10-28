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

package scala.compat.java8.issue247

import scala.compat.java8.FunctionConverters._
import java.util.function.IntFunction

object Main {
  def invoke(jfun: IntFunction[String]): String = jfun(2)

  def main(args: Array[String]): Unit = {
    val sfun = (i: Int) => s"ret: $i"
    val ret = invoke(sfun.asJava)
    assert(ret == "ret: 2")
    println(s"OK. $ret")
  }
}
