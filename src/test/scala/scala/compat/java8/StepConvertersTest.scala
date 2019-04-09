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
import org.junit.Assert._

class StepConvertersTest {
  import collectionImpl._
  import converterImpl._
  import StreamConverters._   // Includes StepConverters!
  import scala.{ collection => co }
  import collection.{ mutable => cm, immutable => ci, concurrent => cc }

  def isAcc[X](x: X): Boolean = x.getClass.getSimpleName.contains("AccumulatorStepper")

  trait SpecCheck {
    def check[X](x: X): Boolean
    def msg[X](x: X): String
    def assert(x: Any): Unit =
      if(!check(x)) assertTrue(msg(x), false)
  }
  object SpecCheck {
    def apply(f: Any => Boolean, err: Any => String = (_ => "SpecCheck failed")) = new SpecCheck {
      def check[X](x: X): Boolean = f(x)
      def msg[X](x: X): String = err(x)
    }
  }

  def _eh_[X](x: => X)(implicit correctSpec: SpecCheck): Unit = { 
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
  }

  def IFFY[X](x: => X)(implicit correctSpec: SpecCheck): Unit = {
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
    assertTrue(isAcc(x))
  }

  def Okay[X](x: => X)(implicit correctSpec: SpecCheck): Unit = {
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
    assertTrue(!isAcc(x))
  }

  def Fine[X](x: => X)(implicit correctSpec: SpecCheck): Unit = {
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
    assertTrue(!isAcc(x))
  }

  def good[X](x: => X)(implicit correctSpec: SpecCheck): Unit = {
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
    assertTrue(!isAcc(x))
  }

  def Tell[X](x: => X)(implicit correctSpec: SpecCheck): Unit = {
    println(x.getClass.getName + " -> " + isAcc(x))
    assertTrue(x.isInstanceOf[Stepper[_]])
    correctSpec.assert(x)
  }

  @Test
  def comprehensivelyGeneric(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[AnyStepper[_]])

    // Collection section
    Okay( co.Iterator[String]("salmon").buffered.stepper )
    good( co.IndexedSeq[String]("salmon").stepper )
    Okay( co.Iterable[String]("salmon").stepper )
    Okay( co.Iterable[String]("salmon").view.stepper )
    Okay( co.Iterator[String]("salmon").stepper )
    Okay( co.LinearSeq[String]("salmon").stepper )
    Okay( co.Map[String, String]("fish" -> "salmon").stepper )
    Okay( co.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( co.Map[String, String]("fish" -> "salmon").valueStepper )
    Okay( co.Seq[String]("salmon").stepper )
    Okay( co.Seq[String]("salmon").view.stepper )
    Okay( co.Set[String]("salmon").stepper )
    Okay( co.SortedMap[String, String]("fish" -> "salmon").stepper )
    Okay( co.SortedMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( co.SortedMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( co.SortedSet[String]("salmon").stepper )
    IFFY( co.Iterable[String]("salmon").accumulate.stepper )
    IFFY( (co.Iterator[String]("salmon"): co.IterableOnce[String]).accumulate.stepper )
    IFFY( co.Iterable[String]("salmon").view.accumulate.stepper )

    // Immutable section
    Okay( ci.::("salmon", Nil).stepper )
    Okay( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).stepper )
    Okay( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).keyStepper )
    Okay( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).valueStepper )
    good( ci.HashSet[String]("salmon").stepper )
    good( ci.IndexedSeq[String]("salmon").stepper )
    Okay( ci.IntMap[String](123456 -> "salmon").stepper )
    Okay( ci.IntMap[String](123456 -> "salmon").valueStepper )
    Okay( ci.Iterable[String]("salmon").stepper )
    Okay( ci.LinearSeq[String]("salmon").stepper )
    Okay( ci.List[String]("salmon").stepper )
    Okay( ci.ListMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.ListMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.ListMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( ci.ListSet[String]("salmon").stepper )
    Okay( ci.LongMap[String](9876543210L -> "salmon").stepper )
    Okay( ci.LongMap[String](9876543210L -> "salmon").valueStepper )
    Okay( ci.Map[String, String]("fish" -> "salmon").stepper )
    Okay( ci.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.Map[String, String]("fish" -> "salmon").valueStepper )
    Okay( ci.Queue[String]("salmon").stepper )
    Okay( ci.Seq[String]("salmon").stepper )
    Okay( ci.Set[String]("salmon").stepper )
    Okay( ci.SortedMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.SortedMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.SortedMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( ci.SortedSet[String]("salmon").stepper )
    Okay( ci.Stream[String]("salmon").stepper )
    _eh_( ci.Stream[String]("salmon").view.stepper )
    Okay( ci.LazyList[String]("salmon").stepper )
    _eh_( ci.LazyList[String]("salmon").view.stepper )
    IFFY( ci.Iterable[String]("salmon").accumulate.stepper )
    Okay( ci.TreeMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.TreeMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.TreeMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( ci.TreeSet[String]("salmon").stepper )
    good( ci.Vector[String]("salmon").stepper )

    // Mutable section
    Okay( (cm.ArrayBuffer[String]("salmon"): cm.AbstractBuffer[String]).stepper )
    Okay( (cm.PriorityQueue[String]("salmon"): cm.AbstractIterable[String]).stepper )
    Okay( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).stepper )
    Okay( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).keyStepper )
    Okay( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).valueStepper )
    Okay( (cm.ArrayBuffer[String]("salmon"): cm.AbstractSeq[String]).stepper )
    Okay( (cm.HashSet[String]("salmon"): cm.AbstractSet[String]).stepper )
    Okay( cm.AnyRefMap[String,String]("fish" -> "salmon").stepper )
    Okay( cm.AnyRefMap[String,String]("fish" -> "salmon").keyStepper )
    Okay( cm.AnyRefMap[String,String]("fish" -> "salmon").valueStepper )
    good( cm.ArrayBuffer[String]("salmon").stepper )
    good( (Array("salmon"): cm.ArraySeq[String]).stepper )
    good( cm.ArraySeq[String]("salmon").stepper )
    _eh_( cm.ArrayStack[String]("salmon").stepper )
    Okay( (cm.ArrayBuffer[String]("salmon"): cm.Buffer[String]).stepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").stepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").keyStepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").valueStepper )
    good( cm.HashSet[String]("salmon").stepper )
    good( cm.IndexedSeq[String]("salmon").stepper )
    good( cm.IndexedSeq[String]("salmon").view.stepper )
    Okay( cm.Iterable[String]("salmon").stepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").stepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").keyStepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( cm.LinkedHashSet[String]("salmon").stepper )
    Okay( cm.ListBuffer[String]("salmon").stepper )
    Okay( cm.ListMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.ListMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.ListMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.LongMap[String](9876543210L -> "salmon").stepper )
    Okay( cm.LongMap[String](9876543210L -> "salmon").valueStepper )
    Okay( cm.Map[String, String]("fish" -> "salmon").stepper )
    Okay( cm.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.Map[String, String]("fish" -> "salmon").valueStepper )
    Okay( cm.OpenHashMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.OpenHashMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.OpenHashMap[String, String]("fish" -> "salmon").valueStepper )
    Okay( cm.PriorityQueue[String]("salmon").stepper )
    Fine( cm.Queue[String]("salmon").stepper ) // Used to be `Good` in 2.12, in 2.13 `Queue` is no longer a `LinearSeq`
    Okay( cm.Seq[String]("salmon").stepper )
    Okay( cm.Set[String]("salmon").stepper )
    Okay( cm.SortedSet[String]("salmon").stepper )
    Fine( cm.Stack[String]("salmon").stepper ) // Used to be `Good` in 2.12, in 2.13 `Stack` is no longer a `LinearSeq`
    IFFY( cm.Iterable[String]("salmon").accumulate.stepper )
    Okay( cm.TreeSet[String]("salmon").stepper )
    Okay( cm.UnrolledBuffer[String]("salmon").stepper )
    Okay( cm.WeakHashMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.WeakHashMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.WeakHashMap[String, String]("fish" -> "salmon").valueStepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[String, String]("fish" -> "salmon").stepper )
    Okay( cc.TrieMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cc.TrieMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).stepper )
    Okay( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).keyStepper )
    Okay( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).valueStepper )
  }

  @Test
  def comprehensivelyDouble(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[DoubleStepper])
    //Double-specific tests

    // Collection section
    Okay( co.Iterator[Double](3.14159).buffered.stepper )
    good( co.IndexedSeq[Double](3.14159).stepper )
    Okay( co.Iterable[Double](3.14159).stepper )
    Okay( co.Iterable[Double](3.14159).view.stepper )
    Okay( co.Iterator[Double](3.14159).stepper )
    Okay( co.LinearSeq[Double](3.14159).stepper )
    Okay( co.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( co.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( co.Seq[Double](3.14159).stepper )
    Okay( co.Seq[Double](3.14159).view.stepper )
    Okay( co.Set[Double](3.14159).stepper )
    Okay( co.SortedMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( co.SortedMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( co.SortedSet[Double](3.14159).stepper )
    IFFY( co.Iterable[Double](3.14159).accumulate.stepper )
    IFFY( (co.Iterator[Double](3.14159): co.IterableOnce[Double]).accumulate.stepper )
    IFFY( co.Iterable[Double](3.14159).view.accumulate.stepper )

    // Immutable section
    Okay( ci.::(3.14159, Nil).stepper )
    Okay( (ci.HashMap[Double, Double](2.718281828 -> 3.14159): ci.AbstractMap[Double, Double]).keyStepper )
    Okay( (ci.HashMap[Double, Double](2.718281828 -> 3.14159): ci.AbstractMap[Double, Double]).valueStepper )
    good( ci.HashSet[Double](3.14159).stepper )
    good( ci.IndexedSeq[Double](3.14159).stepper )
    Okay( ci.IntMap[Double](123456 -> 3.14159).valueStepper )
    Okay( ci.Iterable[Double](3.14159).stepper )
    Okay( ci.LinearSeq[Double](3.14159).stepper )
    Okay( ci.List[Double](3.14159).stepper )
    Okay( ci.ListMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.ListMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( ci.ListSet[Double](3.14159).stepper )
    Okay( ci.LongMap[Double](9876543210L -> 3.14159).valueStepper )
    Okay( ci.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( ci.Queue[Double](3.14159).stepper )
    Okay( ci.Seq[Double](3.14159).stepper )
    Okay( ci.Set[Double](3.14159).stepper )
    Okay( ci.SortedMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.SortedMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( ci.SortedSet[Double](3.14159).stepper )
    Okay( ci.Stream[Double](3.14159).stepper )
    _eh_( ci.Stream[Double](3.14159).view.stepper )
    Okay( ci.LazyList[Double](3.14159).stepper )
    _eh_( ci.LazyList[Double](3.14159).view.stepper )
    IFFY( ci.Iterable[Double](3.14159).accumulate.stepper )
    Okay( ci.TreeMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.TreeMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( ci.TreeSet[Double](3.14159).stepper )
    good( ci.Vector[Double](3.14159).stepper )

    // Mutable section
    Okay( (cm.ArrayBuffer[Double](3.14159): cm.AbstractBuffer[Double]).stepper )
    Okay( (cm.PriorityQueue[Double](3.14159): cm.AbstractIterable[Double]).stepper )
    Okay( (cm.HashMap[Double, Double](2.718281828 -> 3.14159): cm.AbstractMap[Double, Double]).keyStepper )
    Okay( (cm.HashMap[Double, Double](2.718281828 -> 3.14159): cm.AbstractMap[Double, Double]).valueStepper )
    Okay( (cm.ArrayBuffer[Double](3.14159): cm.AbstractSeq[Double]).stepper )
    Okay( (cm.HashSet[Double](3.14159): cm.AbstractSet[Double]).stepper )
    Okay( cm.AnyRefMap[String,Double]("fish" -> 3.14159).valueStepper )
    good( cm.ArrayBuffer[Double](3.14159).stepper )
    good( (Array(3.14159): cm.ArraySeq[Double]).stepper )
    good( cm.ArraySeq[Double](3.14159).stepper )
    _eh_( cm.ArrayStack[Double](3.14159).stepper )
    Okay( (cm.ArrayBuffer[Double](3.14159): cm.Buffer[Double]).stepper )
    good( cm.HashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    good( cm.HashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    good( cm.HashSet[Double](3.14159).stepper )
    good( cm.IndexedSeq[Double](3.14159).stepper )
    good( cm.IndexedSeq[Double](3.14159).view.stepper )
    Okay( cm.Iterable[Double](3.14159).stepper )
    good( cm.LinkedHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    good( cm.LinkedHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.LinkedHashSet[Double](3.14159).stepper )
    Okay( cm.ListBuffer[Double](3.14159).stepper )
    Okay( cm.ListMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.ListMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.LongMap[Double](9876543210L -> 3.14159).valueStepper )
    Okay( cm.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.OpenHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.OpenHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.PriorityQueue[Double](3.14159).stepper )
    Fine( cm.Queue[Double](3.14159).stepper ) // Used to be `Good` in 2.12, in 2.13 `Queue` is no longer a `LinearSeq`
    Okay( cm.Seq[Double](3.14159).stepper )
    Okay( cm.Set[Double](3.14159).stepper )
    Okay( cm.SortedSet[Double](3.14159).stepper )
    Fine( cm.Stack[Double](3.14159).stepper ) // Used to be `Good` in 2.12, in 2.13 `Stack` is no longer a `LinearSeq`
    IFFY( cm.Iterable[Double](3.14159).accumulate.stepper )
    Okay( cm.TreeSet[Double](3.14159).stepper )
    Okay( cm.UnrolledBuffer[Double](3.14159).stepper )
    Okay( cm.WeakHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.WeakHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cc.TrieMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( (cc.TrieMap[Double, Double](2.718281828 -> 3.14159): cc.Map[Double, Double]).keyStepper )
    Okay( (cc.TrieMap[Double, Double](2.718281828 -> 3.14159): cc.Map[Double, Double]).valueStepper )
  }

  @Test
  def comprehensivelyInt(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[IntStepper], x => s"$x should be an IntStepper")

    // Int-specific tests
    good( co.BitSet(42).stepper )
    good( ci.BitSet(42).stepper )
    good( ci.NumericRange(123456, 123458, 1).stepper )
    good( cm.BitSet(42).stepper )
    good( (1 until 2).stepper )
    Okay( ci.IntMap[String](123456 -> "salmon").keyStepper )
    Okay( ci.IntMap[Double](123456 -> 3.14159).keyStepper )
    Okay( ci.IntMap[Long](123456 -> 0x123456789L).keyStepper )

    // Collection section
    Okay( co.Iterator[Int](654321).buffered.stepper )
    good( co.IndexedSeq[Int](654321).stepper )
    Okay( co.Iterable[Int](654321).stepper )
    Okay( co.Iterable[Int](654321).view.stepper )
    Okay( co.Iterator[Int](654321).stepper )
    Okay( co.LinearSeq[Int](654321).stepper )
    Okay( co.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( co.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( co.Seq[Int](654321).stepper )
    Okay( co.Seq[Int](654321).view.stepper )
    Okay( co.Set[Int](654321).stepper )
    Okay( co.SortedMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( co.SortedMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( co.SortedSet[Int](654321).stepper )
    IFFY( co.Iterable[Int](654321).accumulate.stepper )
    IFFY( (co.Iterator[Int](654321): co.IterableOnce[Int]).accumulate.stepper )
    IFFY( co.Iterable[Int](654321).view.accumulate.stepper )

    // Immutable section
    Okay( ci.::(654321, Nil).stepper )
    Okay( (ci.HashMap[Int, Int](0xDEEDED -> 654321): ci.AbstractMap[Int, Int]).keyStepper )
    Okay( (ci.HashMap[Int, Int](0xDEEDED -> 654321): ci.AbstractMap[Int, Int]).valueStepper )
    good( ci.HashSet[Int](654321).stepper )
    good( ci.IndexedSeq[Int](654321).stepper )
    Okay( ci.IntMap[Int](123456 -> 654321).keyStepper )
    Okay( ci.IntMap[Int](123456 -> 654321).valueStepper )
    Okay( ci.Iterable[Int](654321).stepper )
    Okay( ci.LinearSeq[Int](654321).stepper )
    Okay( ci.List[Int](654321).stepper )
    Okay( ci.ListMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.ListMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( ci.ListSet[Int](654321).stepper )
    Okay( ci.LongMap[Int](9876543210L -> 654321).valueStepper )
    Okay( ci.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( ci.Queue[Int](654321).stepper )
    Okay( ci.Seq[Int](654321).stepper )
    Okay( ci.Set[Int](654321).stepper )
    Okay( ci.SortedMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.SortedMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( ci.SortedSet[Int](654321).stepper )
    Okay( ci.Stream[Int](654321).stepper )
    _eh_( ci.Stream[Int](654321).view.stepper )
    Okay( ci.LazyList[Int](654321).stepper )
    _eh_( ci.LazyList[Int](654321).view.stepper )
    IFFY( ci.Iterable[Int](654321).accumulate.stepper )
    Okay( ci.TreeMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.TreeMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( ci.TreeSet[Int](654321).stepper )
    good( ci.Vector[Int](654321).stepper )

    // Mutable section
    Okay( (cm.ArrayBuffer[Int](654321): cm.AbstractBuffer[Int]).stepper )
    Okay( (cm.PriorityQueue[Int](654321): cm.AbstractIterable[Int]).stepper )
    Okay( (cm.HashMap[Int, Int](0xDEEDED -> 654321): cm.AbstractMap[Int, Int]).keyStepper )
    Okay( (cm.HashMap[Int, Int](0xDEEDED -> 654321): cm.AbstractMap[Int, Int]).valueStepper )
    Okay( (cm.ArrayBuffer[Int](654321): cm.AbstractSeq[Int]).stepper )
    Okay( (cm.HashSet[Int](654321): cm.AbstractSet[Int]).stepper )
    Okay( cm.AnyRefMap[String, Int]("fish" -> 654321).valueStepper )
    good( cm.ArrayBuffer[Int](654321).stepper )
    good( (Array(654321): cm.ArraySeq[Int]).stepper )
    good( cm.ArraySeq[Int](654321).stepper )
    _eh_( cm.ArrayStack[Int](654321).stepper )
    Okay( (cm.ArrayBuffer[Int](654321): cm.Buffer[Int]).stepper )
    good( cm.HashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    good( cm.HashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    good( cm.HashSet[Int](654321).stepper )
    good( cm.IndexedSeq[Int](654321).stepper )
    good( cm.IndexedSeq[Int](654321).view.stepper )
    Okay( cm.Iterable[Int](654321).stepper )
    good( cm.LinkedHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    good( cm.LinkedHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.LinkedHashSet[Int](654321).stepper )
    Okay( cm.ListBuffer[Int](654321).stepper )
    Okay( cm.ListMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.ListMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.LongMap[Int](9876543210L -> 654321).valueStepper )
    Okay( cm.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.OpenHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.OpenHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.PriorityQueue[Int](654321).stepper )
    Fine( cm.Queue[Int](654321).stepper ) // Used to be `Good` in 2.12, in 2.13 `Queue` is no longer a `LinearSeq`
    Okay( cm.Seq[Int](654321).stepper )
    Okay( cm.Set[Int](654321).stepper )
    Okay( cm.SortedSet[Int](654321).stepper )
    Fine( cm.Stack[Int](654321).stepper ) // Used to be `Good` in 2.12, in 2.13 `Stack` is no longer a `LinearSeq`
    IFFY( cm.Iterable[Int](654321).accumulate.stepper )
    Okay( cm.TreeSet[Int](654321).stepper )
    Okay( cm.UnrolledBuffer[Int](654321).stepper )
    Okay( cm.WeakHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.WeakHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cc.TrieMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( (cc.TrieMap[Int, Int](0xDEEDED -> 654321): cc.Map[Int, Int]).keyStepper )
    Okay( (cc.TrieMap[Int, Int](0xDEEDED -> 654321): cc.Map[Int, Int]).valueStepper )
  }

  @Test
  def shortWidening(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[IntStepper], x => s"$x should be an IntStepper")

    good( Array[Short](654321.toShort).stepper )
    good( (Array[Short](654321.toShort): cm.ArraySeq[Short]).stepper )

    //TODO: None of these currently work because there are no native Stepper implementations:

    //good( ci.NumericRange(123456.toShort, 123458.toShort, 1.toShort).stepper )
    //good( ((Array[Short](654321.toShort): cm.ArraySeq[Short]): cm.ArrayLike[Short, cm.ArraySeq[Short]]).stepper )
    //good( (Array[Short](654321.toShort): cm.ArrayOps[Short]).stepper )
    //good( cm.ResizableArray[Short](654321.toShort).stepper )
  }

  @Test
  def comprehensivelyLong(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[LongStepper])

    // Long-specific tests
    good( ci.NumericRange(9876543210L, 9876543212L, 1L).stepper )
    Okay( ci.LongMap[String](9876543210L -> "salmon").keyStepper )
    Okay( cm.LongMap[String](9876543210L -> "salmon").keyStepper )
    Okay( ci.LongMap[Double](9876543210L -> 3.14159).keyStepper )
    Okay( cm.LongMap[Double](9876543210L -> 3.14159).keyStepper )
    Okay( ci.LongMap[Int](9876543210L -> 654321).keyStepper )
    Okay( cm.LongMap[Int](9876543210L -> 654321).keyStepper )

     // Collection section
    Okay( co.Iterator[Long](0x123456789L).buffered.stepper )
    good( co.IndexedSeq[Long](0x123456789L).stepper )
    Okay( co.Iterable[Long](0x123456789L).stepper )
    Okay( co.Iterable[Long](0x123456789L).view.stepper )
    Okay( co.Iterator[Long](0x123456789L).stepper )
    Okay( co.LinearSeq[Long](0x123456789L).stepper )
    Okay( co.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( co.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( co.Seq[Long](0x123456789L).stepper )
    Okay( co.Seq[Long](0x123456789L).view.stepper )
    Okay( co.Set[Long](0x123456789L).stepper )
    Okay( co.SortedMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( co.SortedMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( co.SortedSet[Long](0x123456789L).stepper )
    IFFY( co.Iterable[Long](0x123456789L).accumulate.stepper )
    IFFY( (co.Iterator[Long](0x123456789L): co.IterableOnce[Long]).accumulate.stepper )
    IFFY( co.Iterable[Long](0x123456789L).view.accumulate.stepper )

    // Immutable section
    Okay( ci.::(0x123456789L, Nil).stepper )
    Okay( (ci.HashMap[Long, Long](1234567654321L -> 0x123456789L): ci.AbstractMap[Long, Long]).keyStepper )
    Okay( (ci.HashMap[Long, Long](1234567654321L -> 0x123456789L): ci.AbstractMap[Long, Long]).valueStepper )
    good( ci.HashSet[Long](0x123456789L).stepper )
    good( ci.IndexedSeq[Long](0x123456789L).stepper )
    Okay( ci.IntMap[Long](123456 -> 0x123456789L).valueStepper )
    Okay( ci.Iterable[Long](0x123456789L).stepper )
    Okay( ci.LinearSeq[Long](0x123456789L).stepper )
    Okay( ci.List[Long](0x123456789L).stepper )
    Okay( ci.ListMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.ListMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( ci.ListSet[Long](0x123456789L).stepper )
    Okay( ci.LongMap[Long](9876543210L -> 0x123456789L).keyStepper )
    Okay( ci.LongMap[Long](9876543210L -> 0x123456789L).valueStepper )
    Okay( ci.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( ci.Queue[Long](0x123456789L).stepper )
    Okay( ci.Seq[Long](0x123456789L).stepper )
    Okay( ci.Set[Long](0x123456789L).stepper )
    Okay( ci.SortedMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.SortedMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( ci.SortedSet[Long](0x123456789L).stepper )
    Okay( ci.Stream[Long](0x123456789L).stepper )
    _eh_( ci.Stream[Long](0x123456789L).view.stepper )
    Okay( ci.LazyList[Long](0x123456789L).stepper )
    _eh_( ci.LazyList[Long](0x123456789L).view.stepper )
    IFFY( ci.Iterable[Long](0x123456789L).accumulate.stepper )
    Okay( ci.TreeMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.TreeMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( ci.TreeSet[Long](0x123456789L).stepper )
    good( ci.Vector[Long](0x123456789L).stepper )

    // Mutable section
    Okay( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractBuffer[Long]).stepper )
    Okay( (cm.PriorityQueue[Long](0x123456789L): cm.AbstractIterable[Long]).stepper )
    Okay( (cm.HashMap[Long, Long](1234567654321L -> 0x123456789L): cm.AbstractMap[Long, Long]).keyStepper )
    Okay( (cm.HashMap[Long, Long](1234567654321L -> 0x123456789L): cm.AbstractMap[Long, Long]).valueStepper )
    Okay( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractSeq[Long]).stepper )
    Okay( (cm.HashSet[Long](0x123456789L): cm.AbstractSet[Long]).stepper )
    Okay( cm.AnyRefMap[String,Long]("fish" -> 0x123456789L).valueStepper )
    good( cm.ArrayBuffer[Long](0x123456789L).stepper )
    good( (Array(0x123456789L): cm.ArraySeq[Long]).stepper )
    good( cm.ArraySeq[Long](0x123456789L).stepper )
    _eh_( cm.ArrayStack[Long](0x123456789L).stepper )
    Okay( (cm.ArrayBuffer[Long](0x123456789L): cm.Buffer[Long]).stepper )
    good( cm.HashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    good( cm.HashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    good( cm.HashSet[Long](0x123456789L).stepper )
    good( cm.IndexedSeq[Long](0x123456789L).stepper )
    good( cm.IndexedSeq[Long](0x123456789L).view.stepper )
    Okay( cm.Iterable[Long](0x123456789L).stepper )
    good( cm.LinkedHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    good( cm.LinkedHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.LinkedHashSet[Long](0x123456789L).stepper )
    Okay( cm.ListBuffer[Long](0x123456789L).stepper )
    Okay( cm.ListMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.ListMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.LongMap[Long](9876543210L -> 0x123456789L).keyStepper )
    Okay( cm.LongMap[Long](9876543210L -> 0x123456789L).valueStepper )
    Okay( cm.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.OpenHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.OpenHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.PriorityQueue[Long](0x123456789L).stepper )
    Fine( cm.Queue[Long](0x123456789L).stepper ) // Used to be `Good` in 2.12, in 2.13 `Queue` is no longer a `LinearSeq`
    Okay( cm.Seq[Long](0x123456789L).stepper )
    Okay( cm.Set[Long](0x123456789L).stepper )
    Okay( cm.SortedSet[Long](0x123456789L).stepper )
    Fine( cm.Stack[Long](0x123456789L).stepper ) // Used to be `Good` in 2.12, in 2.13 `Stack` is no longer a `LinearSeq`
    IFFY( cm.Iterable[Long](0x123456789L).accumulate.stepper )
    Okay( cm.TreeSet[Long](0x123456789L).stepper )
    Okay( cm.UnrolledBuffer[Long](0x123456789L).stepper )
    Okay( cm.WeakHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.WeakHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( (cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L): cc.Map[Long, Long]).keyStepper )
    Okay( (cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L): cc.Map[Long, Long]).valueStepper )
 }

  @Test
  def comprehensivelySpecific(): Unit = {
    implicit val spec = SpecCheck(_.isInstanceOf[IntStepper], x => s"$x should be an IntStepper")

    good( ci.NumericRange(277: Short, 279: Short, 1: Short).stepper )
    good( ("salmon": ci.WrappedString).stepper )
  }
}
