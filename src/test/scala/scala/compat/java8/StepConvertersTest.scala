package scala.compat.java8

import org.junit.Test
import org.junit.Assert._

class StepConvertersTest {
  import java.util._
  import collectionImpl._
  import StepConverters._
  import scala.{ collection => co }
  import collection.{ mutable => cm, immutable => ci, concurrent => cc }

  def isAcc[X](x: X) = x match {
    case _: AccumulatorStepper[_] => true
    case _: DoubleAccumulatorStepper => true
    case _: IntAccumulatorStepper => true
    case _: LongAccumulatorStepper => true
    case _ => false
  }

  trait SpecCheck { def apply[X](x: X): Boolean }
  object SpecCheck {
    def apply(f: Any => Boolean) = new SpecCheck { def apply[X](x: X): Boolean = f(x) }
  }

  def _eh_[X](x: => X)(implicit correctSpec: SpecCheck) { IFFY(x)(correctSpec) }

  def IFFY[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(isAcc(x))
  }

  def good[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(!isAcc(x))
  }

  def Tell[X](x: => X)(implicit correctSpec: SpecCheck) {
    println(x.getClass.getName + " -> " + isAcc(x))
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
  }

  @Test
  def comprehensivelyGeneric() {
    implicit val spec = SpecCheck(_.isInstanceOf[AnyStepper[_]])

    // Collection section
    IFFY( co.Iterator[String]("salmon").buffered.stepper )
    IFFY( co.IndexedSeq[String]("salmon").stepper )
    IFFY( co.Iterable[String]("salmon").stepper )
    IFFY( co.Iterable[String]("salmon").view.stepper )
    IFFY( co.Iterator[String]("salmon").stepper )
    IFFY( co.LinearSeq[String]("salmon").stepper )
    IFFY( co.Map[String, String]("fish" -> "salmon").stepper )
    IFFY( co.Seq[String]("salmon").stepper )
    IFFY( co.Seq[String]("salmon").view.stepper )
    IFFY( co.Set[String]("salmon").stepper )
    IFFY( co.SortedMap[String, String]("fish" -> "salmon").stepper )
    IFFY( co.SortedSet[String]("salmon").stepper )
    IFFY( co.Traversable[String]("salmon").stepper )
    IFFY( (co.Iterator[String]("salmon"): co.TraversableOnce[String]).stepper )
    IFFY( co.Traversable[String]("salmon").view.stepper )

    // Immutable section
    IFFY( ci.::("salmon", Nil).stepper )
    IFFY( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).stepper )
    IFFY( ci.HashMap[String, String]("fish" -> "salmon").stepper )
    IFFY( ci.HashSet[String]("salmon").stepper )
    IFFY( ci.IndexedSeq[String]("salmon").stepper )
    IFFY( ci.IntMap[String](123456 -> "salmon").stepper )
    IFFY( ci.Iterable[String]("salmon").stepper )
    IFFY( ci.LinearSeq[String]("salmon").stepper )
    IFFY( ci.List[String]("salmon").stepper )
    IFFY( ci.ListMap[String, String]("fish" -> "salmon").stepper )
    IFFY( ci.ListSet[String]("salmon").stepper )
    IFFY( ci.LongMap[String](9876543210L -> "salmon").stepper )
    IFFY( ci.Map[String, String]("fish" -> "salmon").stepper )
    IFFY( ci.Queue[String]("salmon").stepper )
    IFFY( ci.Seq[String]("salmon").stepper )
    IFFY( ci.Set[String]("salmon").stepper )
    IFFY( ci.SortedMap[String, String]("fish" -> "salmon").stepper )
    IFFY( ci.SortedSet[String]("salmon").stepper )
    IFFY( ci.Stack[String]("salmon").stepper )
    IFFY( ci.Stream[String]("salmon").stepper )
    IFFY( ci.Stream[String]("salmon").view.stepper )
    IFFY( ci.Traversable[String]("salmon").stepper )
    IFFY( ci.TreeMap[String, String]("fish" -> "salmon").stepper )
    IFFY( ci.TreeSet[String]("salmon").stepper )
    good( ci.Vector[String]("salmon").stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.AbstractBuffer[String]).stepper )
    IFFY( (cm.PriorityQueue[String]("salmon"): cm.AbstractIterable[String]).stepper )
    IFFY( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).stepper )
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.AbstractSeq[String]).stepper )
    IFFY( (cm.HashSet[String]("salmon"): cm.AbstractSet[String]).stepper )
    IFFY( cm.AnyRefMap[String,String]("fish" -> "salmon").stepper )
    good( cm.ArrayBuffer[String]("salmon").stepper )
    good( ((Array("salmon"): cm.WrappedArray[String]): cm.ArrayLike[String, cm.WrappedArray[String]]).stepper )
    good( (Array("salmon"): cm.ArrayOps[String]).stepper )
    good( cm.ArraySeq[String]("salmon").stepper )
    _eh_( cm.ArrayStack[String]("salmon").stepper )
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.Buffer[String]).stepper )
    IFFY( cm.HashMap[String, String]("fish" -> "salmon").stepper )
    good( cm.HashSet[String]("salmon").stepper )
    IFFY( cm.IndexedSeq[String]("salmon").stepper )
    good( cm.IndexedSeq[String]("salmon").view.stepper )
    IFFY( cm.Iterable[String]("salmon").stepper )
    IFFY( cm.LinearSeq[String]("salmon").stepper )
    IFFY( cm.LinkedHashMap[String, String]("fish" -> "salmon").stepper )
    IFFY( cm.LinkedHashSet[String]("salmon").stepper )
    IFFY( cm.ListBuffer[String]("salmon").stepper )
    IFFY( cm.ListMap[String, String]("fish" -> "salmon").stepper )
    IFFY( cm.LongMap[String](9876543210L -> "salmon").stepper )
    IFFY( cm.Map[String, String]("fish" -> "salmon").stepper )
    IFFY( cm.MutableList[String]("salmon").stepper )
    IFFY( cm.OpenHashMap[String, String]("fish" -> "salmon").stepper )
    IFFY( cm.PriorityQueue[String]("salmon").stepper )
    IFFY( cm.Queue[String]("salmon").stepper )
    good( cm.ResizableArray[String]("salmon").stepper )
    IFFY( cm.Seq[String]("salmon").stepper )
    IFFY( cm.Set[String]("salmon").stepper )
    IFFY( cm.SortedSet[String]("salmon").stepper )
    IFFY( cm.Stack[String]("salmon").stepper )
    IFFY( cm.Traversable[String]("salmon").stepper )
    IFFY( cm.TreeSet[String]("salmon").stepper )
    IFFY( cm.UnrolledBuffer[String]("salmon").stepper )
    IFFY( cm.WeakHashMap[String, String]("fish" -> "salmon").stepper )
    good( (Array("salmon"): cm.WrappedArray[String]).stepper )

    // Java 6 converters section

    // Concurrent section
    IFFY( cc.TrieMap[String, String]("fish" -> "salmon").stepper )
    IFFY( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).stepper )
  }

  @Test
  def comprehensivelyDouble() {
    implicit val spec = SpecCheck(_.isInstanceOf[DoubleStepper])
    //Double-specific tests

    // Collection section
    IFFY( co.Iterator[Double](3.14159).buffered.stepper )
    IFFY( co.IndexedSeq[Double](3.14159).stepper )
    IFFY( co.Iterable[Double](3.14159).stepper )
    IFFY( co.Iterable[Double](3.14159).view.stepper )
    IFFY( co.Iterator[Double](3.14159).stepper )
    IFFY( co.LinearSeq[Double](3.14159).stepper )
    //IFFY( co.Map[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( co.Seq[Double](3.14159).stepper )
    IFFY( co.Seq[Double](3.14159).view.stepper )
    IFFY( co.Set[Double](3.14159).stepper )
    //IFFY( co.SortedMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( co.SortedSet[Double](3.14159).stepper )
    IFFY( co.Traversable[Double](3.14159).stepper )
    IFFY( (co.Iterator[Double](3.14159): co.TraversableOnce[Double]).stepper )
    IFFY( co.Traversable[Double](3.14159).view.stepper )

    // Immutable section
    IFFY( ci.::(3.14159, Nil).stepper )
    //IFFY( (ci.HashMap[Double, Double](2.718281828 -> 3.14159): ci.AbstractMap[Double, Double]).stepper )
    //IFFY( ci.HashMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( ci.HashSet[Double](3.14159).stepper )
    IFFY( ci.IndexedSeq[Double](3.14159).stepper )
    //IFFY( ci.IntMap[Double](123456 -> 3.14159).stepper )
    IFFY( ci.Iterable[Double](3.14159).stepper )
    IFFY( ci.LinearSeq[Double](3.14159).stepper )
    IFFY( ci.List[Double](3.14159).stepper )
    //IFFY( ci.ListMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( ci.ListSet[Double](3.14159).stepper )
    //IFFY( ci.LongMap[Double](9876543210L -> 3.14159).stepper )
    //IFFY( ci.Map[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( ci.Queue[Double](3.14159).stepper )
    IFFY( ci.Seq[Double](3.14159).stepper )
    IFFY( ci.Set[Double](3.14159).stepper )
    //IFFY( ci.SortedMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( ci.SortedSet[Double](3.14159).stepper )
    IFFY( ci.Stack[Double](3.14159).stepper )
    IFFY( ci.Stream[Double](3.14159).stepper )
    IFFY( ci.Stream[Double](3.14159).view.stepper )
    IFFY( ci.Traversable[Double](3.14159).stepper )
    //IFFY( ci.TreeMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( ci.TreeSet[Double](3.14159).stepper )
    good( ci.Vector[Double](3.14159).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.AbstractBuffer[Double]).stepper )
    IFFY( (cm.PriorityQueue[Double](3.14159): cm.AbstractIterable[Double]).stepper )
    //IFFY( (cm.HashMap[Double, Double](2.718281828 -> 3.14159): cm.AbstractMap[Double, Double]).stepper )
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.AbstractSeq[Double]).stepper )
    IFFY( (cm.HashSet[Double](3.14159): cm.AbstractSet[Double]).stepper )
    //IFFY( cm.AnyRefMap[Double,Double](2.718281828 -> 3.14159).stepper )
    good( cm.ArrayBuffer[Double](3.14159).stepper )
    good( ((Array(3.14159): cm.WrappedArray[Double]): cm.ArrayLike[Double, cm.WrappedArray[Double]]).stepper )
    good( (Array(3.14159): cm.ArrayOps[Double]).stepper )
    good( cm.ArraySeq[Double](3.14159).stepper )
    _eh_( cm.ArrayStack[Double](3.14159).stepper )
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.Buffer[Double]).stepper )
    //IFFY( cm.HashMap[Double, Double](2.718281828 -> 3.14159).stepper )
    good( cm.HashSet[Double](3.14159).stepper )
    IFFY( cm.IndexedSeq[Double](3.14159).stepper )
    good( cm.IndexedSeq[Double](3.14159).view.stepper )
    IFFY( cm.Iterable[Double](3.14159).stepper )
    IFFY( cm.LinearSeq[Double](3.14159).stepper )
    //IFFY( cm.LinkedHashMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( cm.LinkedHashSet[Double](3.14159).stepper )
    IFFY( cm.ListBuffer[Double](3.14159).stepper )
    //IFFY( cm.ListMap[Double, Double](2.718281828 -> 3.14159).stepper )
    //IFFY( cm.LongMap[Double](9876543210L -> 3.14159).stepper )
    //IFFY( cm.Map[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( cm.MutableList[Double](3.14159).stepper )
    //IFFY( cm.OpenHashMap[Double, Double](2.718281828 -> 3.14159).stepper )
    IFFY( cm.PriorityQueue[Double](3.14159).stepper )
    IFFY( cm.Queue[Double](3.14159).stepper )
    good( cm.ResizableArray[Double](3.14159).stepper )
    IFFY( cm.Seq[Double](3.14159).stepper )
    IFFY( cm.Set[Double](3.14159).stepper )
    IFFY( cm.SortedSet[Double](3.14159).stepper )
    IFFY( cm.Stack[Double](3.14159).stepper )
    IFFY( cm.Traversable[Double](3.14159).stepper )
    IFFY( cm.TreeSet[Double](3.14159).stepper )
    IFFY( cm.UnrolledBuffer[Double](3.14159).stepper )
    //IFFY( cm.WeakHashMap[Double, Double](2.718281828 -> 3.14159).stepper )
    good( (Array(3.14159): cm.WrappedArray[Double]).stepper )

    // Java 6 converters section

    // Concurrent section
    //IFFY( cc.TrieMap[Double, Double](2.718281828 -> 3.14159).stepper )
    //IFFY( (cc.TrieMap[Double, Double](2.718281828 -> 3.14159): cc.Map[Double, Double]).stepper )
  }

  @Test
  def comprehensivelyInt() {
    implicit val spec = SpecCheck(_.isInstanceOf[IntStepper])

    // Int-specific tests
    IFFY( co.BitSet(42).stepper )
    IFFY( ci.BitSet(42).stepper )
    good( ci.NumericRange(123456, 123458, 1).stepper )
    IFFY( cm.BitSet(42).stepper )
    good( (1 until 2).stepper )

    // Collection section
    IFFY( co.Iterator[Int](654321).buffered.stepper )
    IFFY( co.IndexedSeq[Int](654321).stepper )
    IFFY( co.Iterable[Int](654321).stepper )
    IFFY( co.Iterable[Int](654321).view.stepper )
    IFFY( co.Iterator[Int](654321).stepper )
    IFFY( co.LinearSeq[Int](654321).stepper )
    //IFFY( co.Map[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( co.Seq[Int](654321).stepper )
    IFFY( co.Seq[Int](654321).view.stepper )
    IFFY( co.Set[Int](654321).stepper )
    //IFFY( co.SortedMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( co.SortedSet[Int](654321).stepper )
    IFFY( co.Traversable[Int](654321).stepper )
    IFFY( (co.Iterator[Int](654321): co.TraversableOnce[Int]).stepper )
    IFFY( co.Traversable[Int](654321).view.stepper )

    // Immutable section
    IFFY( ci.::(654321, Nil).stepper )
    //IFFY( (ci.HashMap[Int, Int](0xDEEDED -> 654321): ci.AbstractMap[Int, Int]).stepper )
    //IFFY( ci.HashMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( ci.HashSet[Int](654321).stepper )
    IFFY( ci.IndexedSeq[Int](654321).stepper )
    //IFFY( ci.IntMap[Int](123456 -> 654321).stepper )
    IFFY( ci.Iterable[Int](654321).stepper )
    IFFY( ci.LinearSeq[Int](654321).stepper )
    IFFY( ci.List[Int](654321).stepper )
    //IFFY( ci.ListMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( ci.ListSet[Int](654321).stepper )
    //IFFY( ci.LongMap[Int](9876543210L -> 654321).stepper )
    //IFFY( ci.Map[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( ci.Queue[Int](654321).stepper )
    IFFY( ci.Seq[Int](654321).stepper )
    IFFY( ci.Set[Int](654321).stepper )
    //IFFY( ci.SortedMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( ci.SortedSet[Int](654321).stepper )
    IFFY( ci.Stack[Int](654321).stepper )
    IFFY( ci.Stream[Int](654321).stepper )
    IFFY( ci.Stream[Int](654321).view.stepper )
    IFFY( ci.Traversable[Int](654321).stepper )
    //IFFY( ci.TreeMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( ci.TreeSet[Int](654321).stepper )
    good( ci.Vector[Int](654321).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Int](654321): cm.AbstractBuffer[Int]).stepper )
    IFFY( (cm.PriorityQueue[Int](654321): cm.AbstractIterable[Int]).stepper )
    //IFFY( (cm.HashMap[Int, Int](0xDEEDED -> 654321): cm.AbstractMap[Int, Int]).stepper )
    IFFY( (cm.ArrayBuffer[Int](654321): cm.AbstractSeq[Int]).stepper )
    IFFY( (cm.HashSet[Int](654321): cm.AbstractSet[Int]).stepper )
    //IFFY( cm.AnyRefMap[Int,Int](0xDEEDED -> 654321).stepper )
    good( cm.ArrayBuffer[Int](654321).stepper )
    good( ((Array(654321): cm.WrappedArray[Int]): cm.ArrayLike[Int, cm.WrappedArray[Int]]).stepper )
    good( (Array(654321): cm.ArrayOps[Int]).stepper )
    good( cm.ArraySeq[Int](654321).stepper )
    _eh_( cm.ArrayStack[Int](654321).stepper )
    IFFY( (cm.ArrayBuffer[Int](654321): cm.Buffer[Int]).stepper )
    //IFFY( cm.HashMap[Int, Int](0xDEEDED -> 654321).stepper )
    good( cm.HashSet[Int](654321).stepper )
    IFFY( cm.IndexedSeq[Int](654321).stepper )
    good( cm.IndexedSeq[Int](654321).view.stepper )
    IFFY( cm.Iterable[Int](654321).stepper )
    IFFY( cm.LinearSeq[Int](654321).stepper )
    //IFFY( cm.LinkedHashMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( cm.LinkedHashSet[Int](654321).stepper )
    IFFY( cm.ListBuffer[Int](654321).stepper )
    //IFFY( cm.ListMap[Int, Int](0xDEEDED -> 654321).stepper )
    //IFFY( cm.LongMap[Int](9876543210L -> 654321).stepper )
    //IFFY( cm.Map[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( cm.MutableList[Int](654321).stepper )
    //IFFY( cm.OpenHashMap[Int, Int](0xDEEDED -> 654321).stepper )
    IFFY( cm.PriorityQueue[Int](654321).stepper )
    IFFY( cm.Queue[Int](654321).stepper )
    good( cm.ResizableArray[Int](654321).stepper )
    IFFY( cm.Seq[Int](654321).stepper )
    IFFY( cm.Set[Int](654321).stepper )
    IFFY( cm.SortedSet[Int](654321).stepper )
    IFFY( cm.Stack[Int](654321).stepper )
    IFFY( cm.Traversable[Int](654321).stepper )
    IFFY( cm.TreeSet[Int](654321).stepper )
    IFFY( cm.UnrolledBuffer[Int](654321).stepper )
    //IFFY( cm.WeakHashMap[Int, Int](0xDEEDED -> 654321).stepper )
    good( (Array(654321): cm.WrappedArray[Int]).stepper )

    // Java 6 converters section

    // Concurrent section
    //IFFY( cc.TrieMap[Int, Int](0xDEEDED -> 654321).stepper )
    //IFFY( (cc.TrieMap[Int, Int](0xDEEDED -> 654321): cc.Map[Int, Int]).stepper )
  }

  @Test
  def comprehensivelyLong() {
    implicit val spec = SpecCheck(_.isInstanceOf[LongStepper])

    // Long-specific tests
    good( ci.NumericRange(9876543210L, 9876543212L, 1L).stepper )

     // Collection section
    IFFY( co.Iterator[Long](0x123456789L).buffered.stepper )
    IFFY( co.IndexedSeq[Long](0x123456789L).stepper )
    IFFY( co.Iterable[Long](0x123456789L).stepper )
    IFFY( co.Iterable[Long](0x123456789L).view.stepper )
    IFFY( co.Iterator[Long](0x123456789L).stepper )
    IFFY( co.LinearSeq[Long](0x123456789L).stepper )
    //IFFY( co.Map[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( co.Seq[Long](0x123456789L).stepper )
    IFFY( co.Seq[Long](0x123456789L).view.stepper )
    IFFY( co.Set[Long](0x123456789L).stepper )
    //IFFY( co.SortedMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( co.SortedSet[Long](0x123456789L).stepper )
    IFFY( co.Traversable[Long](0x123456789L).stepper )
    IFFY( (co.Iterator[Long](0x123456789L): co.TraversableOnce[Long]).stepper )
    IFFY( co.Traversable[Long](0x123456789L).view.stepper )

    // Immutable section
    IFFY( ci.::(0x123456789L, Nil).stepper )
    //IFFY( (ci.HashMap[Long, Long](1234567654321L -> 0x123456789L): ci.AbstractMap[Long, Long]).stepper )
    //IFFY( ci.HashMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( ci.HashSet[Long](0x123456789L).stepper )
    IFFY( ci.IndexedSeq[Long](0x123456789L).stepper )
    //IFFY( ci.IntMap[Long](123456 -> 0x123456789L).stepper )
    IFFY( ci.Iterable[Long](0x123456789L).stepper )
    IFFY( ci.LinearSeq[Long](0x123456789L).stepper )
    IFFY( ci.List[Long](0x123456789L).stepper )
    //IFFY( ci.ListMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( ci.ListSet[Long](0x123456789L).stepper )
    //IFFY( ci.LongMap[Long](9876543210L -> 0x123456789L).stepper )
    //IFFY( ci.Map[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( ci.Queue[Long](0x123456789L).stepper )
    IFFY( ci.Seq[Long](0x123456789L).stepper )
    IFFY( ci.Set[Long](0x123456789L).stepper )
    //IFFY( ci.SortedMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( ci.SortedSet[Long](0x123456789L).stepper )
    IFFY( ci.Stack[Long](0x123456789L).stepper )
    IFFY( ci.Stream[Long](0x123456789L).stepper )
    IFFY( ci.Stream[Long](0x123456789L).view.stepper )
    IFFY( ci.Traversable[Long](0x123456789L).stepper )
    //IFFY( ci.TreeMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( ci.TreeSet[Long](0x123456789L).stepper )
    good( ci.Vector[Long](0x123456789L).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractBuffer[Long]).stepper )
    IFFY( (cm.PriorityQueue[Long](0x123456789L): cm.AbstractIterable[Long]).stepper )
    //IFFY( (cm.HashMap[Long, Long](1234567654321L -> 0x123456789L): cm.AbstractMap[Long, Long]).stepper )
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractSeq[Long]).stepper )
    IFFY( (cm.HashSet[Long](0x123456789L): cm.AbstractSet[Long]).stepper )
    //IFFY( cm.AnyRefMap[Long,Long](1234567654321L -> 0x123456789L).stepper )
    good( cm.ArrayBuffer[Long](0x123456789L).stepper )
    good( ((Array(0x123456789L): cm.WrappedArray[Long]): cm.ArrayLike[Long, cm.WrappedArray[Long]]).stepper )
    good( (Array(0x123456789L): cm.ArrayOps[Long]).stepper )
    good( cm.ArraySeq[Long](0x123456789L).stepper )
    _eh_( cm.ArrayStack[Long](0x123456789L).stepper )
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.Buffer[Long]).stepper )
    //IFFY( cm.HashMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    good( cm.HashSet[Long](0x123456789L).stepper )
    IFFY( cm.IndexedSeq[Long](0x123456789L).stepper )
    good( cm.IndexedSeq[Long](0x123456789L).view.stepper )
    IFFY( cm.Iterable[Long](0x123456789L).stepper )
    IFFY( cm.LinearSeq[Long](0x123456789L).stepper )
    //IFFY( cm.LinkedHashMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( cm.LinkedHashSet[Long](0x123456789L).stepper )
    IFFY( cm.ListBuffer[Long](0x123456789L).stepper )
    //IFFY( cm.ListMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    //IFFY( cm.LongMap[Long](9876543210L -> 0x123456789L).stepper )
    //IFFY( cm.Map[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( cm.MutableList[Long](0x123456789L).stepper )
    //IFFY( cm.OpenHashMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    IFFY( cm.PriorityQueue[Long](0x123456789L).stepper )
    IFFY( cm.Queue[Long](0x123456789L).stepper )
    good( cm.ResizableArray[Long](0x123456789L).stepper )
    IFFY( cm.Seq[Long](0x123456789L).stepper )
    IFFY( cm.Set[Long](0x123456789L).stepper )
    IFFY( cm.SortedSet[Long](0x123456789L).stepper )
    IFFY( cm.Stack[Long](0x123456789L).stepper )
    IFFY( cm.Traversable[Long](0x123456789L).stepper )
    IFFY( cm.TreeSet[Long](0x123456789L).stepper )
    IFFY( cm.UnrolledBuffer[Long](0x123456789L).stepper )
    //IFFY( cm.WeakHashMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    good( (Array(0x123456789L): cm.WrappedArray[Long]).stepper )

    // Java 6 converters section

    // Concurrent section
    //IFFY( cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L).stepper )
    //IFFY( (cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L): cc.Map[Long, Long]).stepper )
 }

  @Test
  def comprehensivelySpecific() {
    implicit val spec = SpecCheck(_.isInstanceOf[AnyStepper[_]])

    good( ci.NumericRange(277: Short, 279: Short, 1: Short).stepper )
    IFFY( ci.PagedSeq.fromLines(Array("salmon").iterator).stepper )
    good( ("salmon": ci.StringOps).stepper )
    good( ("salmon": ci.WrappedString).stepper )
  }
}
