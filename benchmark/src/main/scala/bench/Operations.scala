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

package bench.operate

import java.util.stream._
import java.util.{function => jf}

import scala.collection.parallel.ParIterable

import scala.compat.java8.StreamConverters._
import scala.compat.java8.converterImpl._
import scala.compat.java8.collectionImpl._

object CloseEnough {
  import scala.math._
  def apply[A](a: A, b: => A): Boolean = a match {
    case da: Double => b match {
      case db: Double => (da.isNaN && db.isNaN) || abs(da - db) <= max(1, max(abs(da), abs(db)))*1e-6
      case x => a == x
    }
    case _ => a == b
  }
}

object OnInt {
  def expensive(i: Int) = { var v = i.toDouble; var j = 0; while (j < 10) { v = math.exp(math.sin(v)); j += 1 }; v+j }

  def sum(a: Array[Int]): Int = { var s,i = 0; while (i < a.length) { s += a(i); i += 1 }; s }
  def sum(t: Traversable[Int]): Int = t.sum
  def sum(i: Iterator[Int]): Int = i.sum
  def sum(s: IntStepper): Int = s.fold(0)(_ + _)
  def sum(s: IntStream): Int = {
    s.sum
    /*var r = 0
    val it = s.iterator()
    while(it.hasNext) r += it.nextInt()
    r*/
  }
  def psum(i: ParIterable[Int]): Int = i.sum
  def psum(s: IntStream): Int = s.sum

  def trig(a: Array[Int]): Double = { var i = 0; var s = 0.0; while (i < a.length) { s += expensive(a(i)); i += 1 }; s }
  def trig(t: Traversable[Int]): Double = t.map(expensive).sum
  def trig(i: Iterator[Int]): Double = i.map(expensive).sum
  def trig(s: IntStepper): Double = s.fold(0.0)((x,i) => x + expensive(i))
  def trig(s: IntStream): Double = s.mapToDouble(new jf.IntToDoubleFunction{ def applyAsDouble(i: Int) = expensive(i) }).sum
  def ptrig(i: ParIterable[Int]): Double = i.map(expensive).sum
  def ptrig(s: IntStream): Double = trig(s)

  def fmc(a: Array[Int]): Int = { var s,i = 0; while (i < a.length) { if (i%7 == 1) s += (i/7)*i; i += 1 }; s }
  def fmc(t: Traversable[Int]): Int = t.filter(x => (x%7) == 1).map(x => (x/7)*x).sum
  def fmc(i: Iterator[Int]): Int = i.filter(x => (x%7) == 1).map(x => (x/7)*x).sum
  def fmc(s: IntStream): Int = s.
    filter(new jf.IntPredicate { def test(x: Int) = (x%7) == 1 }).
    map(new jf.IntUnaryOperator{ def applyAsInt(x: Int) = (x/7)*x }).
    sum
  def pfmc(i: ParIterable[Int]): Int = i.filter(x => (x%7) == 1).map(x => (x/7)*x).sum
  def pfmc(s: IntStream): Int = fmc(s)

  def mdtc(a: Array[Int]): Int  = { var i = 1; while(i < a.length) { if ((a(i) << 1) >= 42) return i-1; i += 1 }; i - 1 }
  def mdtc(t: Traversable[Int]): Int = t.map(_ << 1).drop(1).takeWhile(_ < 42).size
  def mdtc(i: Iterator[Int]): Int = i.map(_ << 1).drop(1).takeWhile(_ < 42).size
  def mdtc(s: IntStream): Int = {
    val temp = s.map(new jf.IntUnaryOperator { def applyAsInt(x: Int) = x << 1 }).skip(1)
    val acc = new IntAccumulator
    temp.allMatch(new jf.IntPredicate{ def test(x: Int) = if (x < 42) { acc += x; true } else false })
    acc.size.toInt
  }
}

object OnString {
  def expensive(s: String) = { val h = scala.util.hashing.MurmurHash3.stringHash(s); OnInt.expensive(h) }

  def nbr(a: Array[String]): Int = { var s,i = 0; while (i < a.length) { if (a(i).charAt(a(i).length-1) < '5') s += 1; i += 1 }; s }
  def nbr(t: Traversable[String]): Int = t.count(s => s.charAt(s.length-1) < '5')
  def nbr(i: Iterator[String]): Int = i.count(s => s.charAt(s.length-1) < '5')
  def nbr(p: Stepper[String]): Int = p.fold(0)((i,s) => if (s.charAt(s.length-1) < '5') i+1 else i)
  def nbr(q: Stream[String]): Int = q.filter(new jf.Predicate[String] { def test(s: String) = s.charAt(s.length-1) < '5' }).count.toInt
  def pnbr(i: ParIterable[String]): Int = i.count(s => s.charAt(s.length-1) < '5')
  def pnbr(q: Stream[String]): Int = nbr(q)

  def htrg(a: Array[String]): Double = { var s = 0.0; var i = 0; while (i < a.length) { s += expensive(a(i)); i += 1 }; s }
  def htrg(t: Traversable[String]): Double = t.map(expensive).sum
  def htrg(i: Iterator[String]): Double = i.map(expensive).sum
  def htrg(p: Stepper[String]): Double = p.fold(0.0)((x,s) => x + expensive(s))
  def htrg(q: Stream[String]): Double = q.mapToDouble(new jf.ToDoubleFunction[String]{ def applyAsDouble(s: String) = expensive(s) }).sum
  def phtrg(i: ParIterable[String]): Double = i.map(expensive).sum
  def phtrg(q: Stream[String]): Double = htrg(q)

  def fmc(a: Array[String]): Int = {
    var s, i = 0
    while (i < a.length) {
      val x = a(i)
      if (x.charAt(x.length-1) == '1' && (x.length > 2 || (x.charAt(0) != '-' && x.length > 1))) s += 1
      i += 1
    }
    s
  }
  def fmc(t: Traversable[String]): Int =
    t.filter(x => x.charAt(x.length-1) == '1').map(x => if (x.charAt(0) == '-') x.substring(1) else x).count(_.length > 1)
  def fmc(i: Iterator[String]): Int =
    i.filter(x => x.charAt(x.length-1) == '1').map(x => if (x.charAt(0) == '-') x.substring(1) else x).count(_.length > 1)
  def fmc(q: Stream[String]): Int =
    q.filter(new jf.Predicate[String]{ def test(x: String) = x.charAt(x.length-1) == '1' }).
      map[String](new jf.Function[String, String]{ def apply(x: String) = if (x.charAt(0) == '-') x.substring(1) else x }).
      filter(new jf.Predicate[String]{ def test(x: String) = x.length > 1 }).
      count.toInt
  def pfmc(i: ParIterable[String]): Int =
    i.filter(x => x.charAt(x.length-1) == '1').map(x => if (x.charAt(0) == '-') x.substring(1) else x).count(_.length > 1)
  def pfmc(q: Stream[String]): Int = fmc(q)

  def mdtc(a: Array[String]): Int = {
    var i = 1
    while (i < a.length) {
      if (a(i).reverse.length >= 3) return i-1
      i += 1
    }
    i-1
  }
  def mdtc(t: Traversable[String]): Int = t.map(_.reverse).drop(1).takeWhile(_.length < 3).size
  def mdtc(i: Iterator[String]): Int = i.map(_.reverse).drop(1).takeWhile(_.length < 3).size
  def mdtc(q: Stream[String]): Int = {
    val temp = q.map[String](new jf.UnaryOperator[String] { def apply(x: String) = x.reverse }).skip(1)
    val acc = new Accumulator[String]
    temp.allMatch(new jf.Predicate[String]{ def test(x: String) = if (x.length < 3) { acc += x; true } else false })
    acc.size.toInt
  }
}
