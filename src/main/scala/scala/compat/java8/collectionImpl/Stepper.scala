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
  *
  * Example:
  * {{{
  * val s = Stepper.of(Vector(1,2,3,4))
  * if (s.hasStep) println(s.nextStep)      //  Prints 1
  * println(s.tryStep(i => println(i*i)))   //  Prints 4, then true
  * s.substep.foreach(println)              //  Prints 3
  * println(s.count(_ > 3))                 //  Prints 4
  * println(s.hasStep)                      //  Prints `false`
  * }}}
  */
trait Stepper[@specialized(Double, Int, Long) A] extends StepperLike[A, Stepper[A]] {}

/** Provides functionality for Stepper while keeping track of a more precise type of the collection.
  */
trait StepperLike[@specialized(Double, Int, Long) A, +CC] { self =>
  /** Characteristics are bit flags that indicate runtime characteristics of this Stepper.
    *
    * - `Distinct` means that no duplicates exist
    * - `Immutable` means that the underlying collection is guaranteed not to change during traversal
    * - `NonNull` means that no nulls will be returned during traversal
    * - `Sized` means that the collection knows its exact size
    * - `SubSized` means that sub-Steppers created with `substep()` will also know their own size.  `SubSized` steppers must also be `Sized`.
    *
    * The Java flags `CONCURRENT` and `SORTED` are not supported; modification of a concurrency-aware underlying collection is not
    * guaranteed to be any safer than modification of any generic mutable collection, and if the underlying collection is ordered by
    * virtue of sorting, `Stepper` will not keep track of that fact.
    */
  def characteristics: Int

  /** Returns the size of the collection, if known exactly, or `-1` if not. */
  def knownSize: Long

  /** `true` if there are more elements to step through, `false` if not. */
  def hasStep: Boolean

  /** The next element traversed by this Stepper.
    * `nextStep()` throws an exception if no elements exist, so check `hasStep` immediately prior
    * to calling.  Note that `tryStep` also consumes an element, so the result of `hasStep` will
    * be invalid after `tryStep` is called.
    */
  def nextStep(): A

  /** If another element exists, apply `f` to it and return `true`; otherwise, return `false`. */
  def tryStep(f: A => Unit): Boolean

  /** Attempt to split this `Stepper` in half, with the new (returned) copy taking the first half
    * of the collection, and this one advancing to cover the second half.  If subdivision is not
    * possible or not advisable, `substep()` will return `null`.
    */
  def substep(): CC

  /** Returns the precise underlying type of this `Stepper`. */
  def typedPrecisely: CC
  

  ////
  // Terminal operations (do not produce another Stepper)
  ////

  /** Consumes all remaining elements in this `Stepper` and counts how many there are.
    * This is a terminal operation.
    */
  def count(): Long = { var n = 0L; while (hasStep) { nextStep; n += 1 }; n }

  /** Consumes all remaining elements in this `Stepper` and counts how many satisfy condition `p`.
    * This is a terminal operation.
    */
  def count(p: A => Boolean): Long = { var n = 0L; while (hasStep) { if (p(nextStep)) n += 1 }; n }

  /** Searches for an element that satisfies condition `p`.  If none are found, it returns `false`.
    * This is a terminal operation.
    */
  def exists(p: A => Boolean): Boolean = { while(hasStep) { if (p(nextStep)) return true }; false }

  /** Searches for an element that satisifes condition `p`, returning it wrapped in `Some` if one is found, or `None` otherwise.
    * This is a terminal operation.
    */
  def find(p: A => Boolean): Option[A] = { while (hasStep) { val a = nextStep; if (p(a)) return Some(a) }; None }

  /** Repeatedly applies `op` to propagate an initial value `zero` through all elements of the collection.
    * Traversal order is left-to-right.
    * This is a terminal operation.
    */
  def fold[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B) = { var b = zero; while (hasStep) { b = op(b, nextStep) }; b }

  /** Repeatedly applies `op` to propagate an initial value `zero` through the collection until a condition `p` is met.
    * If `p` is never met, the result of the last operation is returned.
    * This is a terminal operation.
    */
  def foldUntil[@specialized(Double, Int, Long) B](zero: B)(op: (B, A) => B)(p: B => Boolean) = { var b = zero; while (p(b) && hasStep) { b = op(b, nextStep) }; b }

  /** Applies `f` to every remaining element in the collection.
    * This is a terminal operation.
    */
  def foreach(f: A => Unit) { while (hasStep) f(nextStep) }

  /** Repeatedly merges elements with `op` until only a single element remains.
    * Throws an exception if the `Stepper` is empty.
    * Merging occurs from left to right.
    * This is a terminal operation.
    */
  def reduce(op: (A, A) => A): A = { var a = nextStep; while (hasStep) { a = op(a, nextStep) }; a }


  ////
  // Operations that convert to another related type
  ////

  /** Returns this `Stepper` as a `java.util.Spliterator`.
    * This is a terminal operation.
    */
  def spliterator: Spliterator[A]

  /** Returns this `Stepper` as a Scala `Iterator`.
    * This is a terminal operation.
    */
  def iterator: Iterator[A] = new scala.collection.AbstractIterator[A] {
    def hasNext = self.hasStep
    def next = self.nextStep
  }
}

/** This trait indicates that a `Stepper` will implement `tryStep` in terms of `hasNext` and `nextStep`. */
trait NextStepper[@specialized(Double, Int, Long) A] extends Stepper[A] with StepperLike[A, NextStepper[A]] {
  def tryStep(f: A => Unit) = if (hasStep) { f(nextStep()); true } else false
}

/** This trait indicates that a `Stepper` will implement `hasNext` and `nextStep` by caching applications of `tryStep`. */
trait TryStepper[@specialized(Double, Int, Long) A] extends Stepper[A] with StepperLike[A, TryStepper[A]] {
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
  override def foreach(f: A => Unit) { while(tryStep(f)) {} }
}

/** Any `AnyStepper` combines the functionality of a Java `Iterator`, a Java `Spliterator`, and a `Stepper`. */
trait AnyStepper[A] extends Stepper[A] with java.util.Iterator[A] with Spliterator[A] with StepperLike[A, AnyStepper[A]] {
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

/** A `DoubleStepper` combines the functionality of a Java `PrimitiveIterator`, a Java `Spliterator`, and a `Stepper`, all specialized for `Double` values. */
trait DoubleStepper extends Stepper[Double] with java.util.PrimitiveIterator.OfDouble with Spliterator.OfDouble with StepperLike[Double, DoubleStepper] {
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

/** An `IntStepper` combines the functionality of a Java `PrimitiveIterator`, a Java `Spliterator`, and a `Stepper`, all specialized for `Int` values. */
trait IntStepper extends Stepper[Int] with java.util.PrimitiveIterator.OfInt with Spliterator.OfInt with StepperLike[Int, IntStepper] {
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

/** A `LongStepper` combines the functionality of a Java `PrimitiveIterator`, a Java `Spliterator`, and a `Stepper`, all specialized for `Long` values. */
trait LongStepper extends Stepper[Long] with java.util.PrimitiveIterator.OfLong with Spliterator.OfLong with StepperLike[Long, LongStepper] {
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
  /** Indicates that a Stepper delivers distinct values (e.g. is backed by a `Set`) */
  val Distinct = Spliterator.DISTINCT

  /** Indicates that a Stepper runs over an immutable collection */
  val Immutable = Spliterator.IMMUTABLE

  /** Indicates that a Stepper will not return any `null` values */
  val NonNull = Spliterator.NONNULL

  /** Indicates that a Stepper delivers elements in a particular order that should be maintained */
  val Ordered = Spliterator.ORDERED

  /** Indicates that a Stepper knows exactly how many elements it contains */
  val Sized = Spliterator.SIZED

  /** Indicates that a Stepper's children (created with substep()) will all know their size.  Steppers that are SubSized must also be Sized. */
  val SubSized = Spliterator.SUBSIZED


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
  
  /** Creates a `Stepper` over a generic `Spliterator`. */
  def ofSpliterator[A](sp: Spliterator[A]): AnyStepper[A] = new OfSpliterator[A](sp)

  /** Creates a `Stepper` over a `DoubleSpliterator`. */
  def ofSpliterator(sp: Spliterator.OfDouble): DoubleStepper = new OfDoubleSpliterator(sp)

  /** Creates a `Stepper` over an `IntSpliterator`. */
  def ofSpliterator(sp: Spliterator.OfInt): IntStepper = new OfIntSpliterator(sp)

  /** Creates a `Stepper` over a `LongSpliterator`. */
  def ofSpliterator(sp: Spliterator.OfLong): LongStepper = new OfLongSpliterator(sp)
}