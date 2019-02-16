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

package bench

import java.util.stream._

import scala.collection.generic.CanBuildFrom
import scala.compat.java8.StreamConverters._
import scala.compat.java8.collectionImpl._
import scala.compat.java8.converterImpl._
import scala.compat.java8.{MakesSequentialStream, MakesParallelStream}

package object generate {
  private def myInty(n: Int) = 0 until n
  private def myStringy(n: Int) = myInty(n).map(i => (i*i).toString)

  object Coll {
    def i[CC[_]](n: Int)(implicit cbf: CanBuildFrom[Nothing, Int, CC[Int]]): CC[Int] = {
      val b = cbf();
      myInty(n).foreach(b += _)
      b.result()
    }
    def s[CC[_]](n: Int)(implicit cbf: CanBuildFrom[Nothing, String, CC[String]]): CC[String] = {
      val b = cbf();
      myStringy(n).foreach(b += _)
      b.result()
    }
  }

  object Pstep {
    def i[CC](cc: CC)(implicit steppize: CC => MakesStepper[Int, EfficientSubstep]): IntStepper =
      steppize(cc).stepper
    def s[CC](cc: CC)(implicit steppize: CC => MakesStepper[String, EfficientSubstep]): AnyStepper[String] =
      steppize(cc).stepper
  }

  object Sstep {
    def i[CC](cc: CC)(implicit steppize: CC => MakesStepper[Int, Any]): IntStepper =
      steppize(cc).stepper
    def s[CC](cc: CC)(implicit steppize: CC => MakesStepper[String, Any]): AnyStepper[String] =
      steppize(cc).stepper
  }

  object PsStream {
    def i[CC](cc: CC)(implicit steppize: CC => MakesStepper[Int, EfficientSubstep]): IntStream =
      steppize(cc).stepper.parStream
    def s[CC](cc: CC)(implicit steppize: CC => MakesStepper[String, EfficientSubstep]): Stream[String] =
      steppize(cc).stepper.parStream
  }

  object SsStream {
    def i[CC](cc: CC)(implicit steppize: CC => MakesStepper[Int, Any]): IntStream =
      steppize(cc).stepper.seqStream
    def s[CC](cc: CC)(implicit steppize: CC => MakesStepper[String, Any]): Stream[String] =
      steppize(cc).stepper.seqStream
  }

  object Sstream {
    def i[CC](cc: CC)(implicit streamize: CC => MakesSequentialStream[Int, IntStream]) =
      streamize(cc).seqStream
    def s[CC](cc: CC)(implicit streamize: CC => MakesSequentialStream[String, Stream[String]]) =
      streamize(cc).seqStream
  }

  object Pstream {
    def i[CC](cc: CC)(implicit streamize: CC => MakesParallelStream[Int, IntStream]) =
      streamize(cc).parStream
    def s[CC](cc: CC)(implicit streamize: CC => MakesParallelStream[String, Stream[String]]) =
      streamize(cc).parStream
  }

  trait GenThingsOf[CC[_]] {
    def title: String
    def sizes: Array[Int]    
  }

  trait IntThingsOf[CC[_]] extends GenThingsOf[CC] {
    implicit def myCBFi: CanBuildFrom[Nothing, Int, CC[Int]]
    // Base collection
    val cI = sizes.map(n => Coll.i[CC](n))
    // Iterator
    def iI(j: Int)(implicit x: CC[Int] => Iterator[Int]) = x(cI(j))
    // Steppers (second letter--s = sequential, p = parallel)
    def tsI(j: Int)(implicit x: CC[Int] => MakesStepper[Int, Any]) = Sstep i cI(j)
    def tpI(j: Int)(implicit x: CC[Int] => MakesStepper[Int, EfficientSubstep]) = Pstep i cI(j)
    // Streams
    def ssI(j: Int)(implicit x: CC[Int] => MakesSequentialStream[Int, IntStream]) = Sstream i cI(j)
    def spI(j: Int)(implicit x: CC[Int] => MakesParallelStream[Int, IntStream]) = Pstream i cI(j)
    // Streams via steppers
    def zsI(j: Int)(implicit x: CC[Int] => MakesStepper[Int, Any]) = SsStream i cI(j)
    def zpI(j: Int)(implicit x: CC[Int] => MakesStepper[Int, EfficientSubstep]) = PsStream i cI(j)
  }

  trait StringThingsOf[CC[_]] extends GenThingsOf[CC] {
    implicit def myCBFs: CanBuildFrom[Nothing, String, CC[String]]
    // Base collection
    val cS = sizes.map(n => Coll.s[CC](n))
    // Iterator
    def iS(j: Int)(implicit x: CC[String] => Iterator[String]) = x(cS(j))
    // Steppers (second letter--s = sequential, p = parallel)
    def tsS(j: Int)(implicit x: CC[String] => MakesStepper[String, Any]) = Sstep s cS(j)
    def tpS(j: Int)(implicit x: CC[String] => MakesStepper[String, EfficientSubstep]) = Pstep s cS(j)
    // Streams
    def ssS(j: Int)(implicit x: CC[String] => MakesSequentialStream[String, Stream[String]]) = Sstream s cS(j)
    def spS(j: Int)(implicit x: CC[String] => MakesParallelStream[String, Stream[String]]) = Pstream s cS(j)
    // Streams via steppers
    def zsS(j: Int)(implicit x: CC[String] => MakesStepper[String, Any]) = SsStream s cS(j)
    def zpS(j: Int)(implicit x: CC[String] => MakesStepper[String, EfficientSubstep]) = PsStream s cS(j)
  }

  trait ThingsOf[CC[_]] extends IntThingsOf[CC] with StringThingsOf[CC] {}

  abstract class AbstractThings[CC[_]](val title: String)(
    implicit 
    outerCBFi: CanBuildFrom[Nothing, Int, CC[Int]],
    outerCBFs: CanBuildFrom[Nothing, String, CC[String]]
  )
  extends ThingsOf[CC] {
    implicit def myCBFi = outerCBFi
    implicit def myCBFs = outerCBFs
  }

  // Java collection CBFs

  implicit val javaUtilArrayListIntCanBuildFrom = new CanBuildFrom[Nothing, Int, java.util.ArrayList[Int]] {
    def apply(from: Nothing): collection.mutable.Builder[Int, java.util.ArrayList[Int]] = apply()
    def apply(): collection.mutable.Builder[Int, java.util.ArrayList[Int]] = new collection.mutable.Builder[Int, java.util.ArrayList[Int]] {
      private var myAL = new java.util.ArrayList[Int]
      def clear() = { myAL = new java.util.ArrayList[Int]; () }
      def result() = { val ans = myAL; clear(); ans }
      def +=(x: Int) = { myAL add x; this }
    }
  }
  implicit val javaUtilArrayListStringCanBuildFrom = new CanBuildFrom[Nothing, String, java.util.ArrayList[String]] {
    def apply(from: Nothing): collection.mutable.Builder[String, java.util.ArrayList[String]] = apply()
    def apply(): collection.mutable.Builder[String, java.util.ArrayList[String]] = new collection.mutable.Builder[String, java.util.ArrayList[String]] {
      private var myAL = new java.util.ArrayList[String]
      def clear() = { myAL = new java.util.ArrayList[String]; () }
      def result() = { val ans = myAL; clear(); ans }
      def +=(x: String) = { myAL add x; this }
    }
  }
  implicit val javaUtilLinkedListIntCanBuildFrom = new CanBuildFrom[Nothing, Int, java.util.LinkedList[Int]] {
    def apply(from: Nothing): collection.mutable.Builder[Int, java.util.LinkedList[Int]] = apply()
    def apply(): collection.mutable.Builder[Int, java.util.LinkedList[Int]] = new collection.mutable.Builder[Int, java.util.LinkedList[Int]] {
      private var myLL = new java.util.LinkedList[Int]
      def clear() = { myLL = new java.util.LinkedList[Int]; () }
      def result() = { val ans = myLL; clear(); ans }
      def +=(x: Int) = { myLL add x; this }
    }
  }
  implicit val javaUtilLinkedListStringCanBuildFrom = new CanBuildFrom[Nothing, String, java.util.LinkedList[String]] {
    def apply(from: Nothing): collection.mutable.Builder[String, java.util.LinkedList[String]] = apply()
    def apply(): collection.mutable.Builder[String, java.util.LinkedList[String]] = new collection.mutable.Builder[String, java.util.LinkedList[String]] {
      private var myLL = new java.util.LinkedList[String]
      def clear() = { myLL = new java.util.LinkedList[String]; () }
      def result() = { val ans = myLL; clear(); ans }
      def +=(x: String) = { myLL add x; this }
    }
  }

  // Streams from ArrayList (Java)

  implicit val getsParStreamFromArrayListInt: (java.util.ArrayList[Int] => MakesParallelStream[Int, IntStream]) = ali => {
    new MakesParallelStream[Int, IntStream] {
      def parStream: IntStream = ali.
        asInstanceOf[java.util.ArrayList[java.lang.Integer]].
        parallelStream.parallel.
        mapToInt(new java.util.function.ToIntFunction[java.lang.Integer]{ def applyAsInt(i: java.lang.Integer) = i.intValue })
    }
  }
  implicit val getsSeqStreamFromArrayListInt: (java.util.ArrayList[Int] => MakesSequentialStream[Int, IntStream]) = ali => {
    new MakesSequentialStream[Int, IntStream] {
      def seqStream: IntStream = ali.
        asInstanceOf[java.util.ArrayList[java.lang.Integer]].
        stream().
        mapToInt(new java.util.function.ToIntFunction[java.lang.Integer]{ def applyAsInt(i: java.lang.Integer) = i.intValue })
    }
  }
  implicit val getsParStreamFromArrayListString: (java.util.ArrayList[String] => MakesParallelStream[String, Stream[String]]) = als => {
    new MakesParallelStream[String, Stream[String]] {
      def parStream: Stream[String] = als.parallelStream.parallel
    }
  }
  implicit val getsSeqStreamFromArrayListString: (java.util.ArrayList[String] => MakesSequentialStream[String, Stream[String]]) = als => {
    new MakesSequentialStream[String, Stream[String]] {
      def seqStream: Stream[String] = als.stream
    }
  }

  // Streams from LinkedList (Java)

  implicit val getsParStreamFromLinkedListInt: (java.util.LinkedList[Int] => MakesParallelStream[Int, IntStream]) = ali => {
    new MakesParallelStream[Int, IntStream] {
      def parStream: IntStream = ali.
        asInstanceOf[java.util.LinkedList[java.lang.Integer]].
        parallelStream.parallel.
        mapToInt(new java.util.function.ToIntFunction[java.lang.Integer]{ def applyAsInt(i: java.lang.Integer) = i.intValue })
    }
  }
  implicit val getsSeqStreamFromLinkedListInt: (java.util.LinkedList[Int] => MakesSequentialStream[Int, IntStream]) = ali => {
    new MakesSequentialStream[Int, IntStream] {
      def seqStream: IntStream = ali.
        asInstanceOf[java.util.LinkedList[java.lang.Integer]].
        stream().
        mapToInt(new java.util.function.ToIntFunction[java.lang.Integer]{ def applyAsInt(i: java.lang.Integer) = i.intValue })
    }
  }
  implicit val getsParStreamFromLinkedListString: (java.util.LinkedList[String] => MakesParallelStream[String, Stream[String]]) = als => {
    new MakesParallelStream[String, Stream[String]] {
      def parStream: Stream[String] = als.parallelStream.parallel
    }
  }
  implicit val getsSeqStreamFromLinkedListString: (java.util.LinkedList[String] => MakesSequentialStream[String, Stream[String]]) = als => {
    new MakesSequentialStream[String, Stream[String]] {
      def seqStream: Stream[String] = als.stream
    }
  }

  object EnableIterators {
    implicit val iterableIntToIterator: (Iterable[Int] => Iterator[Int]) = _.iterator
    implicit val iterableStringToIterator: (Iterable[String] => Iterator[String]) = _.iterator
    implicit val arrayIntToIterator: (Array[Int] => Iterator[Int]) = (a: Array[Int]) => new Iterator[Int] {
      private[this] var i = 0
      def hasNext = i < a.length
      def next = if (hasNext) { var ans = a(i); i += 1; ans } else throw new NoSuchElementException(i.toString)
    }
    implicit val arrayStringToIterator: (Array[String] => Iterator[String]) = _.iterator
  }

  class ArrThings(val sizes: Array[Int]) extends AbstractThings[Array]("Array") {}

  class IshThings(val sizes: Array[Int]) extends AbstractThings[collection.immutable.HashSet]("immutable.HashSet") {}

  class LstThings(val sizes: Array[Int]) extends AbstractThings[List]("List") {}

  class IlsThings(val sizes: Array[Int]) extends AbstractThings[collection.immutable.ListSet]("immutable.ListSet") {}

  class QueThings(val sizes: Array[Int]) extends AbstractThings[collection.immutable.Queue]("immutable.Queue") {}

  class StmThings(val sizes: Array[Int]) extends AbstractThings[collection.immutable.Stream]("immutable.Stream") {}

  class TrsThings(val sizes: Array[Int]) extends AbstractThings[collection.immutable.TreeSet]("immutable.TreeSet") {}

  class VecThings(val sizes: Array[Int]) extends AbstractThings[Vector]("Vector") {}

  class ArbThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.ArrayBuffer]("mutable.ArrayBuffer") {}

  class ArsThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.ArraySeq]("mutable.ArraySeq") {}

  class AstThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.ArrayStack]("mutable.ArrayStack") {}

  class MhsThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.HashSet]("mutable.HashSet") {}

  class LhsThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.LinkedHashSet]("mutable.LinkedHashSet") {}

  class PrqThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.PriorityQueue]("mutable.PriorityQueue") {}

  class MuqThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.Queue]("mutable.Queue") {}

  class WraThings(val sizes: Array[Int]) extends AbstractThings[collection.mutable.WrappedArray]("mutable.WrappedArray") {}

  class JixThings(val sizes: Array[Int]) extends AbstractThings[java.util.ArrayList]("java.util.ArrayList") {}

  class JlnThings(val sizes: Array[Int]) extends AbstractThings[java.util.LinkedList]("java.util.LinkedList") {}

  class Things(sizes: Array[Int] = Array(0, 1, 2, 5, 7, 15, 16, 32, 33, 64, 129, 256, 1023, 2914, 7151/*, 20000, 50000, 200000*/)) {
    def N = sizes.length
    lazy val arr = new ArrThings(sizes)
    lazy val ish = new IshThings(sizes)
    lazy val lst = new LstThings(sizes)
    lazy val ils = new IlsThings(sizes)
    lazy val que = new QueThings(sizes)
    lazy val stm = new StmThings(sizes)
    lazy val trs = new TrsThings(sizes)
    lazy val vec = new VecThings(sizes)
    lazy val arb = new ArbThings(sizes)
    lazy val ars = new ArsThings(sizes)
    lazy val ast = new AstThings(sizes)
    lazy val mhs = new MhsThings(sizes)
    lazy val lhs = new LhsThings(sizes)
    lazy val prq = new PrqThings(sizes)
    lazy val muq = new MuqThings(sizes)
    lazy val wra = new WraThings(sizes)
    lazy val jix = new JixThings(sizes)
    lazy val jln = new JlnThings(sizes)
  }
}
