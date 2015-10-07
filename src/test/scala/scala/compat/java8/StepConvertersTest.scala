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

  def _eh_[X](x: => X) { IFFY(x) }

  def IFFY[X](x: => X) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(isAcc(x))
  }

  def good[X](x: => X) {
    assert(x.isInstanceOf[Stepper[_]])
    assert(!isAcc(x))
  }

  @Test
  def comprehensivelyGeneric() {
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
    IFFY( cm.IndexedSeq[String]("salmon").view.stepper )
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
    //Double-specific tests
  }

  @Test
  def comprehensivelyInt() {
    // Int-specific tests
    IFFY( co.BitSet(42).stepper )
    IFFY( ci.BitSet(42).stepper )
    good( ci.NumericRange(123456, 123458, 1).stepper )
    IFFY( cm.BitSet(42).stepper )
    good( (1 until 2).stepper )
  }

  @Test
  def comprehensivelyLong() {
    // Long-specific tests
    good( ci.NumericRange(9876543210L, 9876543212L, 1L).stepper )
  }

  @Test
  def comprehensivelySpecific() {
    good( ci.NumericRange(277: Short, 279: Short, 1: Short).stepper )
    IFFY( ci.PagedSeq.fromLines(Array("salmon").iterator).stepper )
    good( ("salmon": ci.StringOps).stepper )
    good( ("salmon": ci.WrappedString).stepper )
  }
}
