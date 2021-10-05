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

import org.junit.Assert._
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.function.ThrowingRunnable

import java.nio.file.{Files, Paths}
import scala.compat.java8.StreamConverters._
import scala.compat.java8.issue247.Main
import scala.sys.process._
import scala.util.Try

class Issue247Test {
  @Test
  def runMainDirectly(): Unit = Main.main(Array.empty)

  val mainCls = "scala.compat.java8.issue247.Main"

  @Test
  def runMainMatrix(): Unit = {
    assumeTrue("only run in Linux/OSX", "which which".! == 0)

    val pwd = "pwd".!!.trim

    val coursier = Try {
      ("which cs" #|| "which coursier").!!.trim
    }.getOrElse {
      val cs = s"$pwd/target/coursier"
      if (!Files.isExecutable(Paths.get(cs)))
        ( s"curl -fLo $cs https://git.io/coursier-cli" #&&
          s"chmod +x $cs"
        ).!.ensuring(_ == 0)
      cs
    }

    for {
      scalaBinV <- Seq("2.11", "2.12", "2.13", "3")
      compatV <- Seq("0.9.1", "1.0.0", "1.0.1")
      // scala-java8-compat for scala3 don't have version 0.9.1
      if scalaBinV != "3" || compatV != "0.9.1"
      scalaDir <- Files.list(Paths.get(pwd, "target")).toScala[List]
      if scalaDir.toFile.getName.startsWith(s"scala-$scalaBinV")
      classesDir = scalaDir.resolve("test-classes")
      if classesDir.resolve("scala/compat/java8/issue247/Main.class").toFile.isFile
    } {
      val classpath = Process(
        Seq(
          coursier, // may contain spaces
          "fetch", "--classpath",
          s"org.scala-lang.modules:scala-java8-compat_$scalaBinV:$compatV"
        )
      ).!!.trim

      val testCmd = s"java -cp $classpath:$classesDir $mainCls"

      val run: ThrowingRunnable = new ThrowingRunnable {
        def run(): Unit = {
          println(testCmd)
          testCmd.!!
        }
      }

      if ((scalaBinV, compatV) == ("2.13", "0.9.1")) {
        run.run() // no Exception
      } else {
        assertThrows(classOf[RuntimeException], run)
      }
    }
  }
}
