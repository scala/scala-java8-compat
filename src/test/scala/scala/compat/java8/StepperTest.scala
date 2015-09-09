package scala.compat.java8

import org.junit.Test
import org.junit.Assert._

import collectionImpl._


class IncStepperA(private val size0: Long) extends NextStepper[Int] {
  if (size0 < 0) throw new IllegalArgumentException("Size must be >= 0L")
  private var i = 0L
  def characteristics = 0
  def knownSize = math.max(0L, size0 - i)
  def hasStep = i < size0
  def nextStep() = { i += 1; (i - 1).toInt }
  def substep() = if ((knownSize - i) <= 1) null else {
    val sub = new IncStepperA(i + (size0 - i)/2)
    sub.i = i
    i = sub.size0
    sub
  }
  def typedPrecisely = this
  def spliterator = ???
}

class IncStepperB(private val size0: Long) extends TryStepper[Int] {
  if (size0 < 0) throw new IllegalArgumentException("Size must be >= 0L")
  protected var myCache: Int = 0
  private var i = 0L
  def characteristics = 0
  def knownSize = math.max(0L, size0 - i)
  def tryStep(f: Int => Unit): Boolean = if (i >= size0) false else { f(i.toInt); i += 1; true }
  def substep() = if ((knownSize - i) <= 1) null else {
    val sub = new IncStepperB(size0 - (size0 - i)/2)
    sub.i = i
    i = sub.size0
    sub
  }
  def typedPrecisely = this
  def spliterator = ???
}

class StepperTest {
  def subs[Z, A, CC <: Stepper[A]](zero: Z)(s: Stepper[A])(f: Stepper[A] => Z, op: (Z, Z) => Z): Z = {
    val ss = s.substep()
    if (ss == null) op(zero, f(s))
    else {
      val left = subs(zero)(ss)(f, op)
      subs(left)(s)(f, op)
    }
  }

  val sizes = Vector(0, 1, 2, 4, 15, 17, 2512)
  def sources: Vector[(Int, Stepper[Int])] = sizes.flatMap(i => Vector(i -> new IncStepperA(i), i -> new IncStepperB(i)))

  @Test
  def count_only() {
    sources.foreach{ case (i, s) => assertEquals(i, s.count) }
    sources.foreach{ case (i, s) => assertEquals(i, subs(0)(s)(_.count.toInt, _ + _)) }
  }

  @Test
  def count_conditionally() {
    sources.foreach{ case (i, s) => assertEquals((0 until i).count(_ % 3 == 0), s.count(_ % 3 == 0)) }
    sources.foreach{ case (i, s) => assertEquals((0 until i).count(_ % 3 == 0), subs(0)(s)(_.count(_ % 3 == 0).toInt, _ + _)) }
  }

  @Test
  def existence() {
    sources.foreach{ case (i, s) => assert(i > 0 == s.exists(_ >= 0)) }
    sources.foreach{ case (i, s) => assert(i > 16 == s.exists(_ % 17 == 16)) }
    sources.foreach{ case (i, s) => assert(i > 0 == subs(false)(s)(_.exists(_ >= 0), _ || _)) }
    sources.foreach{ case (i, s) => assert(i > 16 == subs(false)(s)(_.exists(_ % 17 == 16), _ || _)) }
  }

  @Test
  def finding() {
    for (k <- 0 until 100) {
      (sources zip sources).foreach{ case ((i,s), (j,t)) =>
        val x = util.Random.nextInt(math.min(i,j)+3)
        val a = s.find(_ == x)
        val b = subs(None: Option[Int])(t)(_.find(_ == x), _ orElse _)
        assertEquals(a, b)
        assertEquals(a.isDefined, x < math.min(i,j))
      }      
    }
  }

  @Test
  def folding() {
    sources.foreach{ case (i,s) => assertEquals((0 until i).mkString, s.fold("")(_ + _.toString)) }
    sources.foreach{ case (i,s) => assertEquals((0 until i).mkString, subs("")(s)(_.fold("")(_ + _.toString), _ + _)) }
    sources.foreach{ case (i,s) => assertEquals((0 until i).map(_.toDouble).sum, s.fold(0.0)(_ + _), 1e-10) }
    sources.foreach{ case (i,s) => assertEquals((0 until i).map(_.toDouble).sum, subs(0.0)(s)(_.fold(0.0)(_ + _), _ + _), 1e-10) }
  }

  @Test
  def foldingUntil() {
    def expected(i: Int) = (0 until i).scan(0)(_ + _).dropWhile(_ < 6*i).headOption.getOrElse((0 until i).sum)
    sources.foreach{ case (i,s) => assertEquals(expected(i), s.foldTo(0)(_ + _)(_ >= 6*i)) }
    sources.foreach{ case (_,s) => assertEquals(-1, s.foldTo(-1)(_ * _)(_ => true)) }
    sources.foreach{ case (i,s) =>
      val ss = s.substep
      val x = s.foldTo( if (ss == null) 0 else ss.foldTo(0)(_ + _)(_ >= 6*i) )(_ + _)(_ >= 6*i)
      assertEquals(expected(i), x)
    }
  }

  @Test
  def foreaching() {
    sources.foreach{ case (i,s) =>
      val clq = new java.util.concurrent.ConcurrentLinkedQueue[String]
      s.foreach( clq add _.toString )
      assertEquals((0 until i).map(_.toString).toSet, Iterator.continually(if (!clq.isEmpty) Some(clq.poll) else None).takeWhile(_.isDefined).toSet.flatten)
    }
    sources.foreach{ case (i,s) =>
      val clq = new java.util.concurrent.ConcurrentLinkedQueue[String]
      subs(())(s)(_.foreach( clq add _.toString ), (_, _) => ())
      assertEquals((0 until i).map(_.toString).toSet, Iterator.continually(if (!clq.isEmpty) Some(clq.poll) else None).takeWhile(_.isDefined).toSet.flatten)
    }
  }

  @Test
  def reducing() {
    sources.foreach{ case (i,s) => 
      if (i==0) assertEquals(s.hasStep, false)
      else assertEquals((0 until i).sum, s.reduce(_ + _))
    }
    sources.foreach{ case (i,s) =>
      assertEquals((0 until i).sum, subs(0)(s)(x => if (!x.hasStep) 0 else x.reduce(_ + _), _ + _))
    }
  }

  @Test
  def iterating() {
    sources.foreach{ case (i, s) => assert(Iterator.range(0,i) sameElements s.iterator) }
  }
}

