package scala.compat.java8

import org.junit.Test
import org.junit.Assert._

class StepConvertersTest {
  import java.util._
  import collectionImpl._
  import StepConverters._
  import scala.{ collection => co }
  import collection.{ mutable => cm, immutable => ci, concurrent => cc }

  def isAcc[X](x: X): Boolean = x match {
    case _: AccumulatorStepper[_] => true
    case _: DoubleAccumulatorStepper => true
    case _: IntAccumulatorStepper => true
    case _: LongAccumulatorStepper => true
    case _ => false
  }

  def isLin[X](x: X): Boolean = x match {
    case _: AbstractStepsLikeIterator[_, _, _] => true
    case _: AbstractStepsWithTail[_, _, _] => true
    case _ => false
  }

  trait SpecCheck { def apply[X](x: X): Boolean }
  object SpecCheck {
    def apply(f: Any => Boolean) = new SpecCheck { def apply[X](x: X): Boolean = f(x) }
  }

  def _eh_[X](x: => X)(implicit correctSpec: SpecCheck) { 
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
  }

  def IFFY[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(isAcc(x))
  }

  def Okay[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(!isAcc(x))
    assert(isLin(x))
  }

  def Fine[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(!isAcc(x))
  }

  def good[X](x: => X)(implicit correctSpec: SpecCheck) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(correctSpec(x))
    assert(!isAcc(x))
    assert(!isLin(x))
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
    Okay( co.Iterator[String]("salmon").buffered.stepper )
    good( co.IndexedSeq[String]("salmon").stepper )
    IFFY( co.Iterable[String]("salmon").stepper )
    IFFY( co.Iterable[String]("salmon").view.stepper )
    Okay( co.Iterator[String]("salmon").stepper )
    Okay( co.LinearSeq[String]("salmon").stepper )
    IFFY( co.Map[String, String]("fish" -> "salmon").stepper )
    Okay( co.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( co.Map[String, String]("fish" -> "salmon").valueStepper )
    IFFY( co.Seq[String]("salmon").stepper )
    IFFY( co.Seq[String]("salmon").view.stepper )
    IFFY( co.Set[String]("salmon").stepper )
    IFFY( co.SortedMap[String, String]("fish" -> "salmon").stepper )
    Okay( co.SortedMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( co.SortedMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( co.SortedSet[String]("salmon").stepper )
    IFFY( co.Traversable[String]("salmon").accumulate.stepper )
    IFFY( (co.Iterator[String]("salmon"): co.TraversableOnce[String]).accumulate.stepper )
    IFFY( co.Traversable[String]("salmon").view.accumulate.stepper )

    // Immutable section
    IFFY( ci.::("salmon", Nil).stepper )
    IFFY( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).stepper )
    Okay( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).keyStepper )
    Okay( (ci.HashMap[String, String]("fish" -> "salmon"): ci.AbstractMap[String, String]).valueStepper )
    good( ci.HashMap[String, String]("fish" -> "salmon").stepper )
    good( ci.HashMap[String, String]("fish" -> "salmon").keyStepper )
    good( ci.HashMap[String, String]("fish" -> "salmon").valueStepper )
    good( ci.HashSet[String]("salmon").stepper )
    good( ci.IndexedSeq[String]("salmon").stepper )
    IFFY( ci.IntMap[String](123456 -> "salmon").stepper )
    Okay( ci.IntMap[String](123456 -> "salmon").valueStepper )
    IFFY( ci.Iterable[String]("salmon").stepper )
    Okay( ci.LinearSeq[String]("salmon").stepper )
    Okay( ci.List[String]("salmon").stepper )
    IFFY( ci.ListMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.ListMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.ListMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( ci.ListSet[String]("salmon").stepper )
    IFFY( ci.LongMap[String](9876543210L -> "salmon").stepper )
    Okay( ci.LongMap[String](9876543210L -> "salmon").valueStepper )
    IFFY( ci.Map[String, String]("fish" -> "salmon").stepper )
    Okay( ci.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.Map[String, String]("fish" -> "salmon").valueStepper )
    Okay( ci.Queue[String]("salmon").stepper )
    IFFY( ci.Seq[String]("salmon").stepper )
    IFFY( ci.Set[String]("salmon").stepper )
    IFFY( ci.SortedMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.SortedMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.SortedMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( ci.SortedSet[String]("salmon").stepper )
    Okay( ci.Stack[String]("salmon").stepper )
    Okay( ci.Stream[String]("salmon").stepper )
    _eh_( ci.Stream[String]("salmon").view.stepper )
    IFFY( ci.Traversable[String]("salmon").stepper )
    IFFY( ci.TreeMap[String, String]("fish" -> "salmon").stepper )
    Okay( ci.TreeMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( ci.TreeMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( ci.TreeSet[String]("salmon").stepper )
    good( ci.Vector[String]("salmon").stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.AbstractBuffer[String]).stepper )
    IFFY( (cm.PriorityQueue[String]("salmon"): cm.AbstractIterable[String]).stepper )
    IFFY( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).stepper )
    Okay( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).keyStepper )
    Okay( (cm.HashMap[String, String]("fish" -> "salmon"): cm.AbstractMap[String, String]).valueStepper )
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.AbstractSeq[String]).stepper )
    IFFY( (cm.HashSet[String]("salmon"): cm.AbstractSet[String]).stepper )
    IFFY( cm.AnyRefMap[String,String]("fish" -> "salmon").stepper )
    Okay( cm.AnyRefMap[String,String]("fish" -> "salmon").keyStepper )
    Okay( cm.AnyRefMap[String,String]("fish" -> "salmon").valueStepper )
    good( cm.ArrayBuffer[String]("salmon").stepper )
    good( ((Array("salmon"): cm.WrappedArray[String]): cm.ArrayLike[String, cm.WrappedArray[String]]).stepper )
    good( (Array("salmon"): cm.ArrayOps[String]).stepper )
    good( cm.ArraySeq[String]("salmon").stepper )
    _eh_( cm.ArrayStack[String]("salmon").stepper )
    IFFY( (cm.ArrayBuffer[String]("salmon"): cm.Buffer[String]).stepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").stepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").keyStepper )
    good( cm.HashMap[String, String]("fish" -> "salmon").valueStepper )
    good( cm.HashSet[String]("salmon").stepper )
    good( cm.IndexedSeq[String]("salmon").stepper )
    good( cm.IndexedSeq[String]("salmon").view.stepper )
    IFFY( cm.Iterable[String]("salmon").stepper )
    Okay( cm.LinearSeq[String]("salmon").stepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").stepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").keyStepper )
    good( cm.LinkedHashMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( cm.LinkedHashSet[String]("salmon").stepper )
    IFFY( cm.ListBuffer[String]("salmon").stepper )
    IFFY( cm.ListMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.ListMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.ListMap[String, String]("fish" -> "salmon").keyStepper )
    IFFY( cm.LongMap[String](9876543210L -> "salmon").stepper )
    Okay( cm.LongMap[String](9876543210L -> "salmon").valueStepper )
    IFFY( cm.Map[String, String]("fish" -> "salmon").stepper )
    Okay( cm.Map[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.Map[String, String]("fish" -> "salmon").valueStepper )
    Okay( cm.MutableList[String]("salmon").stepper )
    IFFY( cm.OpenHashMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.OpenHashMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.OpenHashMap[String, String]("fish" -> "salmon").valueStepper )
    IFFY( cm.PriorityQueue[String]("salmon").stepper )
    Okay( cm.Queue[String]("salmon").stepper )
    good( cm.ResizableArray[String]("salmon").stepper )
    IFFY( cm.Seq[String]("salmon").stepper )
    IFFY( cm.Set[String]("salmon").stepper )
    IFFY( cm.SortedSet[String]("salmon").stepper )
    IFFY( cm.Stack[String]("salmon").stepper )
    IFFY( cm.Traversable[String]("salmon").stepper )
    IFFY( cm.TreeSet[String]("salmon").stepper )
    IFFY( cm.UnrolledBuffer[String]("salmon").stepper )
    IFFY( cm.WeakHashMap[String, String]("fish" -> "salmon").stepper )
    Okay( cm.WeakHashMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cm.WeakHashMap[String, String]("fish" -> "salmon").valueStepper )
    good( (Array("salmon"): cm.WrappedArray[String]).stepper )

    // Java 6 converters section

    // Concurrent section
    IFFY( cc.TrieMap[String, String]("fish" -> "salmon").stepper )
    Okay( cc.TrieMap[String, String]("fish" -> "salmon").keyStepper )
    Okay( cc.TrieMap[String, String]("fish" -> "salmon").keyStepper )
    IFFY( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).stepper )
    Okay( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).keyStepper )
    Okay( (cc.TrieMap[String, String]("fish" -> "salmon"): cc.Map[String, String]).valueStepper )
  }

  @Test
  def comprehensivelyDouble() {
    implicit val spec = SpecCheck(_.isInstanceOf[DoubleStepper])
    //Double-specific tests

    // Collection section
    Okay( co.Iterator[Double](3.14159).buffered.stepper )
    good( co.IndexedSeq[Double](3.14159).stepper )
    IFFY( co.Iterable[Double](3.14159).stepper )
    IFFY( co.Iterable[Double](3.14159).view.stepper )
    Okay( co.Iterator[Double](3.14159).stepper )
    Okay( co.LinearSeq[Double](3.14159).stepper )
    Okay( co.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( co.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( co.Seq[Double](3.14159).stepper )
    IFFY( co.Seq[Double](3.14159).view.stepper )
    IFFY( co.Set[Double](3.14159).stepper )
    Okay( co.SortedMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( co.SortedMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( co.SortedSet[Double](3.14159).stepper )
    IFFY( co.Traversable[Double](3.14159).stepper )
    IFFY( (co.Iterator[Double](3.14159): co.TraversableOnce[Double]).stepper )
    IFFY( co.Traversable[Double](3.14159).view.stepper )

    // Immutable section
    IFFY( ci.::(3.14159, Nil).stepper )
    Okay( (ci.HashMap[Double, Double](2.718281828 -> 3.14159): ci.AbstractMap[Double, Double]).keyStepper )
    Okay( (ci.HashMap[Double, Double](2.718281828 -> 3.14159): ci.AbstractMap[Double, Double]).valueStepper )
    good( ci.HashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    good( ci.HashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    good( ci.HashSet[Double](3.14159).stepper )
    good( ci.IndexedSeq[Double](3.14159).stepper )
    Okay( ci.IntMap[Double](123456 -> 3.14159).valueStepper )
    IFFY( ci.Iterable[Double](3.14159).stepper )
    Okay( ci.LinearSeq[Double](3.14159).stepper )
    Okay( ci.List[Double](3.14159).stepper )
    Okay( ci.ListMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.ListMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( ci.ListSet[Double](3.14159).stepper )
    Okay( ci.LongMap[Double](9876543210L -> 3.14159).valueStepper )
    Okay( ci.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( ci.Queue[Double](3.14159).stepper )
    IFFY( ci.Seq[Double](3.14159).stepper )
    IFFY( ci.Set[Double](3.14159).stepper )
    Okay( ci.SortedMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.SortedMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( ci.SortedSet[Double](3.14159).stepper )
    Okay( ci.Stack[Double](3.14159).stepper )
    Okay( ci.Stream[Double](3.14159).stepper )
    _eh_( ci.Stream[Double](3.14159).view.stepper )
    IFFY( ci.Traversable[Double](3.14159).stepper )
    Okay( ci.TreeMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( ci.TreeMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( ci.TreeSet[Double](3.14159).stepper )
    good( ci.Vector[Double](3.14159).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.AbstractBuffer[Double]).stepper )
    IFFY( (cm.PriorityQueue[Double](3.14159): cm.AbstractIterable[Double]).stepper )
    Okay( (cm.HashMap[Double, Double](2.718281828 -> 3.14159): cm.AbstractMap[Double, Double]).keyStepper )
    Okay( (cm.HashMap[Double, Double](2.718281828 -> 3.14159): cm.AbstractMap[Double, Double]).valueStepper )
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.AbstractSeq[Double]).stepper )
    IFFY( (cm.HashSet[Double](3.14159): cm.AbstractSet[Double]).stepper )
    Okay( cm.AnyRefMap[String,Double]("fish" -> 3.14159).valueStepper )
    good( cm.ArrayBuffer[Double](3.14159).stepper )
    good( ((Array(3.14159): cm.WrappedArray[Double]): cm.ArrayLike[Double, cm.WrappedArray[Double]]).stepper )
    good( (Array(3.14159): cm.ArrayOps[Double]).stepper )
    good( cm.ArraySeq[Double](3.14159).stepper )
    _eh_( cm.ArrayStack[Double](3.14159).stepper )
    IFFY( (cm.ArrayBuffer[Double](3.14159): cm.Buffer[Double]).stepper )
    good( cm.HashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    good( cm.HashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    good( cm.HashSet[Double](3.14159).stepper )
    good( cm.IndexedSeq[Double](3.14159).stepper )
    good( cm.IndexedSeq[Double](3.14159).view.stepper )
    IFFY( cm.Iterable[Double](3.14159).stepper )
    Okay( cm.LinearSeq[Double](3.14159).stepper )
    good( cm.LinkedHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    good( cm.LinkedHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( cm.LinkedHashSet[Double](3.14159).stepper )
    IFFY( cm.ListBuffer[Double](3.14159).stepper )
    Okay( cm.ListMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.ListMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.LongMap[Double](9876543210L -> 3.14159).valueStepper )
    Okay( cm.Map[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.Map[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( cm.MutableList[Double](3.14159).stepper )
    Okay( cm.OpenHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.OpenHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    IFFY( cm.PriorityQueue[Double](3.14159).stepper )
    Okay( cm.Queue[Double](3.14159).stepper )
    good( cm.ResizableArray[Double](3.14159).stepper )
    IFFY( cm.Seq[Double](3.14159).stepper )
    IFFY( cm.Set[Double](3.14159).stepper )
    IFFY( cm.SortedSet[Double](3.14159).stepper )
    IFFY( cm.Stack[Double](3.14159).stepper )
    IFFY( cm.Traversable[Double](3.14159).stepper )
    IFFY( cm.TreeSet[Double](3.14159).stepper )
    IFFY( cm.UnrolledBuffer[Double](3.14159).stepper )
    Okay( cm.WeakHashMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cm.WeakHashMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    good( (Array(3.14159): cm.WrappedArray[Double]).stepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Double, Double](2.718281828 -> 3.14159).keyStepper )
    Okay( cc.TrieMap[Double, Double](2.718281828 -> 3.14159).valueStepper )
    Okay( (cc.TrieMap[Double, Double](2.718281828 -> 3.14159): cc.Map[Double, Double]).keyStepper )
    Okay( (cc.TrieMap[Double, Double](2.718281828 -> 3.14159): cc.Map[Double, Double]).valueStepper )
  }

  @Test
  def comprehensivelyInt() {
    implicit val spec = SpecCheck(_.isInstanceOf[IntStepper])

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
    IFFY( co.Iterable[Int](654321).stepper )
    IFFY( co.Iterable[Int](654321).view.stepper )
    Okay( co.Iterator[Int](654321).stepper )
    Okay( co.LinearSeq[Int](654321).stepper )
    Okay( co.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( co.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( co.Seq[Int](654321).stepper )
    IFFY( co.Seq[Int](654321).view.stepper )
    IFFY( co.Set[Int](654321).stepper )
    Okay( co.SortedMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( co.SortedMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( co.SortedSet[Int](654321).stepper )
    IFFY( co.Traversable[Int](654321).stepper )
    IFFY( (co.Iterator[Int](654321): co.TraversableOnce[Int]).stepper )
    IFFY( co.Traversable[Int](654321).view.stepper )

    // Immutable section
    IFFY( ci.::(654321, Nil).stepper )
    Okay( (ci.HashMap[Int, Int](0xDEEDED -> 654321): ci.AbstractMap[Int, Int]).keyStepper )
    Okay( (ci.HashMap[Int, Int](0xDEEDED -> 654321): ci.AbstractMap[Int, Int]).valueStepper )
    good( ci.HashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    good( ci.HashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    good( ci.HashSet[Int](654321).stepper )
    good( ci.IndexedSeq[Int](654321).stepper )
    Okay( ci.IntMap[Int](123456 -> 654321).keyStepper )
    Okay( ci.IntMap[Int](123456 -> 654321).valueStepper )
    IFFY( ci.Iterable[Int](654321).stepper )
    Okay( ci.LinearSeq[Int](654321).stepper )
    Okay( ci.List[Int](654321).stepper )
    Okay( ci.ListMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.ListMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( ci.ListSet[Int](654321).stepper )
    Okay( ci.LongMap[Int](9876543210L -> 654321).valueStepper )
    Okay( ci.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( ci.Queue[Int](654321).stepper )
    IFFY( ci.Seq[Int](654321).stepper )
    IFFY( ci.Set[Int](654321).stepper )
    Okay( ci.SortedMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.SortedMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( ci.SortedSet[Int](654321).stepper )
    Okay( ci.Stack[Int](654321).stepper )
    Okay( ci.Stream[Int](654321).stepper )
    _eh_( ci.Stream[Int](654321).view.stepper )
    IFFY( ci.Traversable[Int](654321).stepper )
    Okay( ci.TreeMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( ci.TreeMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( ci.TreeSet[Int](654321).stepper )
    good( ci.Vector[Int](654321).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Int](654321): cm.AbstractBuffer[Int]).stepper )
    IFFY( (cm.PriorityQueue[Int](654321): cm.AbstractIterable[Int]).stepper )
    Okay( (cm.HashMap[Int, Int](0xDEEDED -> 654321): cm.AbstractMap[Int, Int]).keyStepper )
    Okay( (cm.HashMap[Int, Int](0xDEEDED -> 654321): cm.AbstractMap[Int, Int]).valueStepper )
    IFFY( (cm.ArrayBuffer[Int](654321): cm.AbstractSeq[Int]).stepper )
    IFFY( (cm.HashSet[Int](654321): cm.AbstractSet[Int]).stepper )
    Okay( cm.AnyRefMap[String, Int]("fish" -> 654321).valueStepper )
    good( cm.ArrayBuffer[Int](654321).stepper )
    good( ((Array(654321): cm.WrappedArray[Int]): cm.ArrayLike[Int, cm.WrappedArray[Int]]).stepper )
    good( (Array(654321): cm.ArrayOps[Int]).stepper )
    good( cm.ArraySeq[Int](654321).stepper )
    _eh_( cm.ArrayStack[Int](654321).stepper )
    IFFY( (cm.ArrayBuffer[Int](654321): cm.Buffer[Int]).stepper )
    good( cm.HashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    good( cm.HashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    good( cm.HashSet[Int](654321).stepper )
    good( cm.IndexedSeq[Int](654321).stepper )
    good( cm.IndexedSeq[Int](654321).view.stepper )
    IFFY( cm.Iterable[Int](654321).stepper )
    Okay( cm.LinearSeq[Int](654321).stepper )
    good( cm.LinkedHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    good( cm.LinkedHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( cm.LinkedHashSet[Int](654321).stepper )
    IFFY( cm.ListBuffer[Int](654321).stepper )
    Okay( cm.ListMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.ListMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.LongMap[Int](9876543210L -> 654321).valueStepper )
    Okay( cm.Map[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.Map[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( cm.MutableList[Int](654321).stepper )
    Okay( cm.OpenHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.OpenHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    IFFY( cm.PriorityQueue[Int](654321).stepper )
    Okay( cm.Queue[Int](654321).stepper )
    good( cm.ResizableArray[Int](654321).stepper )
    IFFY( cm.Seq[Int](654321).stepper )
    IFFY( cm.Set[Int](654321).stepper )
    IFFY( cm.SortedSet[Int](654321).stepper )
    IFFY( cm.Stack[Int](654321).stepper )
    IFFY( cm.Traversable[Int](654321).stepper )
    IFFY( cm.TreeSet[Int](654321).stepper )
    IFFY( cm.UnrolledBuffer[Int](654321).stepper )
    Okay( cm.WeakHashMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cm.WeakHashMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    good( (Array(654321): cm.WrappedArray[Int]).stepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Int, Int](0xDEEDED -> 654321).keyStepper )
    Okay( cc.TrieMap[Int, Int](0xDEEDED -> 654321).valueStepper )
    Okay( (cc.TrieMap[Int, Int](0xDEEDED -> 654321): cc.Map[Int, Int]).keyStepper )
    Okay( (cc.TrieMap[Int, Int](0xDEEDED -> 654321): cc.Map[Int, Int]).valueStepper )
  }

  @Test
  def comprehensivelyLong() {
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
    IFFY( co.Iterable[Long](0x123456789L).stepper )
    IFFY( co.Iterable[Long](0x123456789L).view.stepper )
    Okay( co.Iterator[Long](0x123456789L).stepper )
    Okay( co.LinearSeq[Long](0x123456789L).stepper )
    Okay( co.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( co.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( co.Seq[Long](0x123456789L).stepper )
    IFFY( co.Seq[Long](0x123456789L).view.stepper )
    IFFY( co.Set[Long](0x123456789L).stepper )
    Okay( co.SortedMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( co.SortedMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( co.SortedSet[Long](0x123456789L).stepper )
    IFFY( co.Traversable[Long](0x123456789L).stepper )
    IFFY( (co.Iterator[Long](0x123456789L): co.TraversableOnce[Long]).stepper )
    IFFY( co.Traversable[Long](0x123456789L).view.stepper )

    // Immutable section
    IFFY( ci.::(0x123456789L, Nil).stepper )
    Okay( (ci.HashMap[Long, Long](1234567654321L -> 0x123456789L): ci.AbstractMap[Long, Long]).keyStepper )
    Okay( (ci.HashMap[Long, Long](1234567654321L -> 0x123456789L): ci.AbstractMap[Long, Long]).valueStepper )
    good( ci.HashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    good( ci.HashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    good( ci.HashSet[Long](0x123456789L).stepper )
    good( ci.IndexedSeq[Long](0x123456789L).stepper )
    Okay( ci.IntMap[Long](123456 -> 0x123456789L).valueStepper )
    IFFY( ci.Iterable[Long](0x123456789L).stepper )
    Okay( ci.LinearSeq[Long](0x123456789L).stepper )
    Okay( ci.List[Long](0x123456789L).stepper )
    Okay( ci.ListMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.ListMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( ci.ListSet[Long](0x123456789L).stepper )
    Okay( ci.LongMap[Long](9876543210L -> 0x123456789L).keyStepper )
    Okay( ci.LongMap[Long](9876543210L -> 0x123456789L).valueStepper )
    Okay( ci.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( ci.Queue[Long](0x123456789L).stepper )
    IFFY( ci.Seq[Long](0x123456789L).stepper )
    IFFY( ci.Set[Long](0x123456789L).stepper )
    Okay( ci.SortedMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.SortedMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( ci.SortedSet[Long](0x123456789L).stepper )
    Okay( ci.Stack[Long](0x123456789L).stepper )
    Okay( ci.Stream[Long](0x123456789L).stepper )
    _eh_( ci.Stream[Long](0x123456789L).view.stepper )
    IFFY( ci.Traversable[Long](0x123456789L).stepper )
    Okay( ci.TreeMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( ci.TreeMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( ci.TreeSet[Long](0x123456789L).stepper )
    good( ci.Vector[Long](0x123456789L).stepper )

    // Mutable section
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractBuffer[Long]).stepper )
    IFFY( (cm.PriorityQueue[Long](0x123456789L): cm.AbstractIterable[Long]).stepper )
    Okay( (cm.HashMap[Long, Long](1234567654321L -> 0x123456789L): cm.AbstractMap[Long, Long]).keyStepper )
    Okay( (cm.HashMap[Long, Long](1234567654321L -> 0x123456789L): cm.AbstractMap[Long, Long]).valueStepper )
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.AbstractSeq[Long]).stepper )
    IFFY( (cm.HashSet[Long](0x123456789L): cm.AbstractSet[Long]).stepper )
    Okay( cm.AnyRefMap[String,Long]("fish" -> 0x123456789L).valueStepper )
    good( cm.ArrayBuffer[Long](0x123456789L).stepper )
    good( ((Array(0x123456789L): cm.WrappedArray[Long]): cm.ArrayLike[Long, cm.WrappedArray[Long]]).stepper )
    good( (Array(0x123456789L): cm.ArrayOps[Long]).stepper )
    good( cm.ArraySeq[Long](0x123456789L).stepper )
    _eh_( cm.ArrayStack[Long](0x123456789L).stepper )
    IFFY( (cm.ArrayBuffer[Long](0x123456789L): cm.Buffer[Long]).stepper )
    good( cm.HashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    good( cm.HashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    good( cm.HashSet[Long](0x123456789L).stepper )
    good( cm.IndexedSeq[Long](0x123456789L).stepper )
    good( cm.IndexedSeq[Long](0x123456789L).view.stepper )
    IFFY( cm.Iterable[Long](0x123456789L).stepper )
    Okay( cm.LinearSeq[Long](0x123456789L).stepper )
    good( cm.LinkedHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    good( cm.LinkedHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( cm.LinkedHashSet[Long](0x123456789L).stepper )
    IFFY( cm.ListBuffer[Long](0x123456789L).stepper )
    Okay( cm.ListMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.ListMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.LongMap[Long](9876543210L -> 0x123456789L).keyStepper )
    Okay( cm.LongMap[Long](9876543210L -> 0x123456789L).valueStepper )
    Okay( cm.Map[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.Map[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( cm.MutableList[Long](0x123456789L).stepper )
    Okay( cm.OpenHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.OpenHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    IFFY( cm.PriorityQueue[Long](0x123456789L).stepper )
    Okay( cm.Queue[Long](0x123456789L).stepper )
    good( cm.ResizableArray[Long](0x123456789L).stepper )
    IFFY( cm.Seq[Long](0x123456789L).stepper )
    IFFY( cm.Set[Long](0x123456789L).stepper )
    IFFY( cm.SortedSet[Long](0x123456789L).stepper )
    IFFY( cm.Stack[Long](0x123456789L).stepper )
    IFFY( cm.Traversable[Long](0x123456789L).stepper )
    IFFY( cm.TreeSet[Long](0x123456789L).stepper )
    IFFY( cm.UnrolledBuffer[Long](0x123456789L).stepper )
    Okay( cm.WeakHashMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cm.WeakHashMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    good( (Array(0x123456789L): cm.WrappedArray[Long]).stepper )

    // Java 6 converters section

    // Concurrent section
    Okay( cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L).keyStepper )
    Okay( cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L).valueStepper )
    Okay( (cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L): cc.Map[Long, Long]).keyStepper )
    Okay( (cc.TrieMap[Long, Long](1234567654321L -> 0x123456789L): cc.Map[Long, Long]).valueStepper )
 }

  @Test
  def comprehensivelySpecific() {
    implicit val spec = SpecCheck(_.isInstanceOf[AnyStepper[_]])

    good( ci.NumericRange(277: Short, 279: Short, 1: Short).stepper )
    _eh_( ci.PagedSeq.fromLines(Array("salmon").iterator).stepper )
    good( ("salmon": ci.StringOps).stepper )
    good( ("salmon": ci.WrappedString).stepper )
  }
}
