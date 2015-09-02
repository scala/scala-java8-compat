package scala.compat.java8.collectionImpl

import java.util.Spliterator

/** A Stepper is a specialized collection that can step through its
  * contents once.  It provides the same test-and-get methods as
  * does `Iterator`, named `hasStep` and `nextStep` so they can
  * coexist with iterator methods.  However, like `Spliterator`,
  * steppers provide a `tryStep` method to call a closure if another
  * element exists, a `substep()` method to split into pieces, and
  * `characteristics` and size-reporting methods that
  * implement the subdivision and report what is known about the remaining
  * size of the `Stepper`.  `Stepper` thus naturally implements both
  * `Iterator` and `Spliterator`.
  *
  * A `Stepper` can present itself as a Spliterator via the `spliterator`
  * method, or as a Scala Iterator via the `iterator` method.  The `Stepper`
  * trait is compatible with both `Spliterator` and Java's generic and
  * primitive iterators, so a `Stepper` may already be one or both.
  *
  * Subtraits `NextStepper` and `TryStepper` fill in the basic capability
  * by either implementing `tryStep` in terms of `hasStep` and `nextStep`,
  * or vice versa.
  *
  * Subtraits `AnyStepper`, `DoubleStepper`, `IntStepper`, and `LongStepper`
  * implement both the `Stepper` trait and the corresponding Java
  * `Spliterator` and `Iterator`/`PrimitiveIterator`.
  */

trait Stepper[@specialized(Double, Int, Long) A, CC] { self =>
  def characteristics: Int
  def knownSize: Long
  def hasStep: Boolean
  def nextStep(): A
  def tryStep(f: A => Unit): Boolean
  def substep(): CC
  def typedPrecisely: CC
  
  ////
  // Terminal operations (do not produce another Stepper)
  ////
  def count(): Long = { var n = 0L; while (hasStep) { nextStep; n += 1 }; n }
  def count(p: A => Boolean): Long = { var n = 0L; while (hasStep) { if (p(nextStep)) n += 1 }; n }
  def exists(p: A => Boolean): Boolean = { while(hasStep) { if (p(nextStep)) return true }; false }
  def find(p: A => Boolean): Option[A] = { while (hasStep) { val a = nextStep; if (p(a)) return Some(a) }; None }
  def fold[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B) = { var b = zero; while (hasStep) { b = op(b, nextStep) }; b }
  def foldUntil[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B)(p: B => Boolean) = { var b = zero; while (p(b) && hasStep) { b = op(b, nextStep) }; b }
  def foreach(f: A => Unit) { while (hasStep) f(nextStep) }
  def reduce(f: (A, A) => A): A = { var a = nextStep; while (hasStep) { a = f(a, nextStep) }; a }

  ////
  // Operations that convert to another related type
  ////
  def spliterator: Spliterator[A]
  def iterator: Iterator[A] = new scala.collection.AbstractIterator[A] {
    def hasNext = self.hasStep
    def next = self.nextStep
  }
}

trait NextStepper[@specialized(Double, Int, Long) A, CC] extends Stepper[A, CC] {
  def tryStep(f: A => Unit) = if (hasStep) { f(nextStep()); true } else false
}

trait TryStepper[@specialized(Double, Int, Long) A, CC] extends Stepper[A, CC] {
  private var myCache: A = null.asInstanceOf[A]
  private var myCacheIsFull = false
  private def load(): Boolean = {
    myCacheIsFull = tryStep(myCache = _)
    myCacheIsFull
  }
  def hasStep = myCacheIsFull || load()
  def nextStep = {
    if (!myCacheIsFull) {
      load()
      if (!myCacheIsFull) throw new NoSuchElementException("nextStep in TryStepper")
    }
    val ans = myCache
    myCacheIsFull = false
    myCache = null.asInstanceOf[A]
    ans
  }
}

trait AnyStepper[A] extends Stepper[A, AnyStepper[A]] with java.util.Iterator[A] with Spliterator[A] {
  def forEachRemaining(c: java.util.function.Consumer[_ >: A]) { while (hasNext) { c.accept(next) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = next
  def tryAdvance(c: java.util.function.Consumer[_ >: A]): Boolean = if (hasNext) { c.accept(next); true } else false
  def tryStep(f: A => Unit): Boolean = if (hasNext) { f(next); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely: AnyStepper[A] = this
  override def spliterator: Spliterator[A] = this
}

trait DoubleStepper extends Stepper[Double, DoubleStepper] with java.util.PrimitiveIterator.OfDouble with Spliterator.OfDouble {
  def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Double]) { while (hasNext) { c.accept(java.lang.Double.valueOf(nextDouble)) } }
  def forEachRemaining(c: java.util.function.DoubleConsumer) { while (hasNext) { c.accept(nextDouble) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = nextDouble
  def tryAdvance(c: java.util.function.Consumer[_ >: java.lang.Double]): Boolean = if (hasNext) { c.accept(java.lang.Double.valueOf(nextDouble)); true } else false
  def tryAdvance(c: java.util.function.DoubleConsumer): Boolean = if (hasNext) { c.accept(nextDouble); true } else false
  def tryStep(f: Double => Unit): Boolean = if (hasNext) { f(nextDouble); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely: DoubleStepper = this
  override def spliterator: Spliterator[Double] = this.asInstanceOf[Spliterator[Double]]  // Scala and Java disagree about whether it's java.lang.Double or double
}

trait IntStepper extends Stepper[Int, IntStepper] with java.util.PrimitiveIterator.OfInt with Spliterator.OfInt {
  def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Integer]) { while (hasNext) { c.accept(java.lang.Integer.valueOf(nextInt)) } }
  def forEachRemaining(c: java.util.function.IntConsumer) { while (hasNext) { c.accept(nextInt) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = nextInt
  def tryAdvance(c: java.util.function.Consumer[_ >: java.lang.Integer]): Boolean = if (hasNext) { c.accept(java.lang.Integer.valueOf(nextInt)); true } else false
  def tryAdvance(c: java.util.function.IntConsumer): Boolean = if (hasNext) { c.accept(nextInt); true } else false
  def tryStep(f: Int => Unit): Boolean = if (hasNext) { f(nextInt); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely = this
  override def spliterator: Spliterator[Int] = this.asInstanceOf[Spliterator[Int]]  // Scala and Java disagree about whether it's java.lang.Integer or int
}

trait LongStepper extends Stepper[Long, LongStepper] with java.util.PrimitiveIterator.OfLong with Spliterator.OfLong {
  def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Long]) { while (hasNext) { c.accept(java.lang.Long.valueOf(nextLong)) } }
  def forEachRemaining(c: java.util.function.LongConsumer) { while (hasNext) { c.accept(nextLong) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = nextLong
  def tryAdvance(c: java.util.function.Consumer[_ >: java.lang.Long]): Boolean = if (hasNext) { c.accept(java.lang.Long.valueOf(nextLong)); true } else false
  def tryAdvance(c: java.util.function.LongConsumer): Boolean = if (hasNext) { c.accept(nextLong); true } else false
  def tryStep(f: Long => Unit): Boolean = if (hasNext) { f(nextLong); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely = this
  override def spliterator: Spliterator[Long] = this.asInstanceOf[Spliterator[Long]]  // Scala and Java disagree about whether it's java.lang.Long or long
}
  

object Stepper {

  private class OfSpliterator[A](sp: Spliterator[A])
  extends AnyStepper[A] with java.util.function.Consumer[A] {
    private var cache: A = null.asInstanceOf[A]
    private var cached: Boolean = false
    def accept(a: A) { cache = a; cached = true }
    
    private def loadCache: Boolean = sp.tryAdvance(this)
    private def useCache(c: java.util.function.Consumer[_ >: A]): Boolean = {
      if (cached) {
        c.accept(cache)
        cache = null.asInstanceOf[A]
        cached = false
        true
      }
      else false
    }
    
    def characteristics = sp.characteristics
    def estimateSize = {
      val sz = sp.estimateSize
      if (cached && sz < Long.MaxValue && sz >= 0) sz + 1
      else sz
    }
    override def forEachRemaining(c: java.util.function.Consumer[_ >: A]) {
      useCache(c)
      sp.forEachRemaining(c)
    }
    def hasNext = cached || loadCache
    def next = {
      if (!hasNext) throw new NoSuchElementException("Empty Spliterator in Stepper")
      val ans = cache
      cache = null.asInstanceOf[A]
      cached = false
      ans
    }
    def substep(): AnyStepper[A] = {
      val subSp = sp.trySplit()
      if (subSp eq null) null
      else {
        val sub = new OfSpliterator(subSp)
        if (cached) {
          sub.cache = cache
          sub.cached = true
          cache = null.asInstanceOf[A]
          cached = false
        }
        sub
      }
    }
    override def tryAdvance(c: java.util.function.Consumer[_ >: A]) = useCache(c) || sp.tryAdvance(c)
  }
  
  private class OfDoubleSpliterator(sp: Spliterator.OfDouble)
  extends DoubleStepper with java.util.function.DoubleConsumer {
    private var cache: Double = Double.NaN
    private var cached: Boolean = false
    def accept(d: Double) { cache = d; cached = true }
    
    private def loadCache: Boolean = sp.tryAdvance(this)
    private def useCache(c: java.util.function.DoubleConsumer): Boolean = {
      if (cached) {
        c.accept(cache)
        cached = false
        true
      }
      else false
    }
    
    def characteristics = sp.characteristics
    def estimateSize = {
      val sz = sp.estimateSize
      if (cached && sz < Long.MaxValue && sz >= 0) sz + 1
      else sz
    }
    override def forEachRemaining(c: java.util.function.DoubleConsumer) {
      useCache(c)
      sp.forEachRemaining(c)
    }
    def hasNext = cached || loadCache
    def nextDouble = {
      if (!hasNext) throw new NoSuchElementException("Empty Spliterator in Stepper")
      val ans = cache
      cached = false
      ans
    }
    def substep(): DoubleStepper = {
      val subSp = sp.trySplit()
      if (subSp eq null) null
      else {
        val sub = new OfDoubleSpliterator(subSp)
        if (cached) {
          sub.cache = cache
          sub.cached = true
          cached = false
        }
        sub
      }
    }
    override def tryAdvance(c: java.util.function.DoubleConsumer) = useCache(c) || sp.tryAdvance(c)
  }
  
  private class OfIntSpliterator(sp: Spliterator.OfInt)
  extends IntStepper with java.util.function.IntConsumer {
    private var cache: Int = 0
    private var cached: Boolean = false
    def accept(i: Int) { cache = i; cached = true }
    
    private def loadCache: Boolean = sp.tryAdvance(this)
    private def useCache(c: java.util.function.IntConsumer): Boolean = {
      if (cached) {
        c.accept(cache)
        cached = false
        true
      }
      else false
    }
    
    def characteristics = sp.characteristics
    def estimateSize = {
      val sz = sp.estimateSize
      if (cached && sz < Long.MaxValue && sz >= 0) sz + 1
      else sz
    }
    override def forEachRemaining(c: java.util.function.IntConsumer) {
      useCache(c)
      sp.forEachRemaining(c)
    }
    def hasNext = cached || loadCache
    def nextInt = {
      if (!hasNext) throw new NoSuchElementException("Empty Spliterator in Stepper")
      val ans = cache
      cached = false
      ans
    }
    def substep(): IntStepper = {
      val subSp = sp.trySplit()
      if (subSp eq null) null
      else {
        val sub = new OfIntSpliterator(subSp)
        if (cached) {
          sub.cache = cache
          sub.cached = true
          cached = false
        }
        sub
      }
    }
    override def tryAdvance(c: java.util.function.IntConsumer) = useCache(c) || sp.tryAdvance(c)
  }
  
  private class OfLongSpliterator(sp: Spliterator.OfLong)
  extends LongStepper with java.util.function.LongConsumer {
    private var cache: Long = 0L
    private var cached: Boolean = false
    def accept(l: Long) { cache = l; cached = true }
    
    private def loadCache: Boolean = sp.tryAdvance(this)
    private def useCache(c: java.util.function.LongConsumer): Boolean = {
      if (cached) {
        c.accept(cache)
        cached = false
        true
      }
      else false
    }
    
    def characteristics = sp.characteristics
    def estimateSize = {
      val sz = sp.estimateSize
      if (cached && sz < Long.MaxValue && sz >= 0) sz + 1
      else sz
    }
    override def forEachRemaining(c: java.util.function.LongConsumer) {
      useCache(c)
      sp.forEachRemaining(c)
    }
    def hasNext = cached || loadCache
    def nextLong = {
      if (!hasNext) throw new NoSuchElementException("Empty Spliterator in Stepper")
      val ans = cache
      cached = false
      ans
    }
    def substep(): LongStepper = {
      val subSp = sp.trySplit()
      if (subSp eq null) null
      else {
        val sub = new OfLongSpliterator(subSp)
        if (cached) {
          sub.cache = cache
          sub.cached = true
          cached = false
        }
        sub
      }
    }
    override def tryAdvance(c: java.util.function.LongConsumer) = useCache(c) || sp.tryAdvance(c)
  }
  
  def ofSpliterator[A](sp: Spliterator[A]): AnyStepper[A] = new OfSpliterator[A](sp)
  def ofSpliterator(sp: Spliterator.OfDouble): DoubleStepper = new OfDoubleSpliterator(sp)
  def ofSpliterator(sp: Spliterator.OfInt): IntStepper = new OfIntSpliterator(sp)
  def ofSpliterator(sp: Spliterator.OfLong): LongStepper = new OfLongSpliterator(sp)
}
