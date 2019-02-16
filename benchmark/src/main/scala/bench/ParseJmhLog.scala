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

package bench.examine

import scala.util._
import scala.util.control.NonFatal

object ParseJmhLog {
  // Manual path to the log file (which may have been put there manually)
  val logLocation = "results/jmhbench.log"

  // Warning--this is maintained manually!  Please take care to keep it up to date.
  val collectionNames = Map(
    "arb" -> "collection.mutable.ArrayBuffer",
    "arr" -> "Array",
    "ars" -> "collection.mutable.ArraySeq",
    "ast" -> "collection.mutable.ArrayStack",
    "ils" -> "collection.immutable.ListSet",
    "ish" -> "collection.immutable.HashSet",
    "lhs" -> "collection.mutable.LinkedHashSet",
    "lst" -> "collection.immutable.List",
    "mhs" -> "collection.mutable.HashSet",
    "muq" -> "collection.mutable.Queue",
    "prq" -> "collection.mutable.PriorityQueue",
    "que" -> "collection.immutable.Queue",
    "stm" -> "collection.immutable.Stream",
    "trs" -> "collection.immutable.TreeSet",
    "vec" -> "collection.immutable.Vector",
    "wra" -> "collection.mutable.WrappedArray",
    "jix" -> "java.util.ArrayList",
    "jln" -> "java.util.LinkedList"
  )

  // Warning--this is maintained manually!  Please take care to keep it up to date.
  val dataNames = Map(
    "cI" -> "Int, base collection",
    "cpI" -> "Int, par collection",
    "iI" -> "Int, iterator on coll",
    "ssI" -> "Int, serial stream",
    "spI" -> "Int, parallel stream",
    "tpI" -> "Int Stepper (can par)",
    "tsI" -> "Int Stepper (seq only)",
    "cS" -> "String, base collection",
    "cpS" -> "String, par collection",
    "iS" -> "String, iterator on coll",
    "ssS" -> "String, serial stream",
    "spS" -> "String, parallel stream",
    "tpS" -> "String Stepper (can par)",
    "tsS" -> "String Stepper (seq only)"
  )
  val dataColumnWidth = dataNames.map(_._2).map(_.length).max + 1

  // Warning--this is maintained manually!  Please take care to keep it up to date.
  val operationNames = Map(
    "sum" -> "fast summation",
    "psum" -> "fast parallel summation",
    "nbr" -> "fast sum of lengths",
    "pnbr" -> "fast parallel sum of lengths",
    "trig" -> "slow trigonometry",
    "ptrig" -> "slow parallel trigonometry",
    "htrg" -> "slow trig on hashCode",
    "phtrg" -> "slow parallel trig on hashCode",
    "fmc" -> "filter/map/sum trio",
    "pfmc" -> "parallel filter/map/sum trio",
    "mdtc" -> "map/filter/take trio"
  )

  private def ensure(satisfied: Boolean)(not: => Unit): Unit = {
    if (!satisfied) {
      not
      assert(satisfied)
    }
  }

  private def indicate(cols: Int, value: Double): String = {
    val one = math.min((3*cols)/4, cols-5)
    val me = math.rint(one*3*value).toInt
    if (me <= 3*(cols-1)) {
      val filled =
        if ((me%3) != 0) "#"*(me/3) + (if ((me%3) == 1) "-" else "=")
        else "#"*(me/3)
      filled + " "*(cols - filled.length)
    }
    else "#"*(cols-4) + "~~# "
  }

  case class Entry(op: String, coll: String, big: Boolean, data: String, speed: Double, errbar: Double) {
    ensure(collectionNames contains coll){ println(coll) }
    ensure(dataNames contains data){ println(data) }
    ensure(operationNames contains op){ println(op) }
  }

  def apply(f: java.io.File = new java.io.File(logLocation)) = {
    val lines = {
      val s = scala.io.Source.fromFile(f)
      try { s.getLines().toVector } finally { s.close }
    }

    val relevant = lines.
      dropWhile(x => !(x contains "Run complete.")).
      dropWhile(x => !(x contains "JmhBench.bench_")).
      takeWhile(x => x contains "JmhBench.bench_").
      map{ x => 
        val ys = x.split("\\s+")
        ys(1).split('_').drop(1) match {
          case Array(dat, op, coll, n) => Entry (op, coll, n == "1", dat, ys(4).toDouble, ys(6).toDouble) 
          case _ => throw new Exception("Bad benchmark log line, please investigate manually.")
        }
      }.
      toArray

    val normalized = relevant.
      groupBy(e => (e.op, e.big, e.data.takeRight(1))).
      mapValues{ vs =>
        val one = vs.find(e => e.coll == "arr" && e.data.startsWith("c")).get.speed
        vs.map(v => v.copy(speed = v.speed/one, errbar = 100 * v.errbar/v.speed))  // Error bar in percent error from mean
      }.
      map(_._2).
      toArray.
      sortBy(_(0) match { case Entry(o, _, b, d, _, _) => (o, d.takeRight(1), b) }).
      map(_.sortBy{ case Entry(_, c, _, d, _, _) => (c, d.dropRight(1)) })

    normalized.foreach{ es =>
      println
      println(">"*79)
      println
      var remaining = es.toList
      while (remaining.nonEmpty) {
        val (esa, esb) = remaining.span(_.coll == remaining.head.coll)
        remaining = esb
        println(operationNames(esa.head.op))
        println(if (esa.head.big) "10000 elements" else "10 elements")
        println(collectionNames(esa.head.coll))
        esa.foreach{ e =>
          println(
            s"  %-${dataColumnWidth}s ".format(dataNames(e.data)) +
            indicate(79 - (dataColumnWidth+3) - 16, e.speed) +
            "%5.3f +- %5.1f %%".format(e.speed, e.errbar)
          )
        }
        if (remaining.nonEmpty) println
      }
    }
    println
    println("-"*79)
    println
  }

  def main(args: Array[String]): Unit = apply()
}
