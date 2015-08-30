package scala.compat.java8.collectionImpl

/** A Stepper is a specialized collection that can step through its
  * contents once.  It provides the same test-and-get methods as
  * does `Iterator`, named `hasStep` and `nextStep` so they can
  * coexist with iterator methods.  However, like `java.util.Spliterator`,
  * steppers provide a `tryStep` method to call a closure if another
  * element exists, a `substep()` method to split into pieces, and
  * `characteristics` and size-reporting methods that
  * implement the subdivision and report what is known about the remaining
  * size of the `Stepper`.  `Stepper` thus naturally implements both
  * `Iterator` and `Spliterator`.
  *
  * Subtraits `NextStepper` and `TryStepper` fill in the basic capability
  * by either implementing `tryStep` in terms of `hasStep` and `nextStep`,
  * or vice versa.
  *
  * `Stepper` implements four core intermediate operations that process
  * the sequence of elements and return another `Stepper`, and six core
  * terminal operations that consume the `Stepper` and produce a result.
  * If additional functionality is needed, one can convert a `Stepper` to
  * a Scala `Iterator` or a Java 8 `Stream`.
  */
trait Stepper[@specialized(Double, Int, Long) A, CC] extends Any {
  def characteristics: Int
  def knownSize: Long
  def hasStep: Boolean
  def nextStep(): A
  def tryStep(f: A => Unit): Boolean
  def substep(): CC
  def typedPrecisely: CC
  
  ////
  // Intermediate operations (do produce another Stepper)
  ////
  def drop(n: Int): Stepper[A, CC]
  def filter(p: A => Boolean): Stepper[A, CC]
  def flatMap[@specialized(Double, Int, Long) B, DD](f: A => Stepper[B, DD])(implicit impl: StepImpl[A, B, CC, DD]): Stepper[B, DD] =
    impl.flatmapOf(this, f)
  def map[@specialized(Double, Int, Long) B, DD](f: A => B)(implicit impl: StepImpl[A, B, CC, DD]): Stepper[B, DD] =
    impl.mapOf(this, f)
  def take(n: Int): Stepper[A, CC]

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
}

trait NextStepper[@specialized(Double, Int, Long) A, CC] extends Any with Stepper[A, CC] {
  def tryStep(f: A => Unit) = if (hasStep) { f(nextStep()); true } else false
}

trait TryStepper[@specialized(Double, Int, Long) A, CC] extends Any with Stepper[A, CC] {
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
  

object Stepper {

  class OfSpliterator[A] private[java8] (sp: java.util.Spliterator[A])
  extends StepperGeneric[A] with java.util.function.Consumer[A] {
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
    def substep(): StepperGeneric[A] = {
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
  
  class OfDoubleSpliterator private[java8] (sp: java.util.Spliterator.OfDouble)
  extends StepperDouble with java.util.function.DoubleConsumer {
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
    def substep(): StepperDouble = {
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
  
  class OfIntSpliterator private[java8] (sp: java.util.Spliterator.OfInt)
  extends StepperInt with java.util.function.IntConsumer {
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
    def substep(): StepperInt = {
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
  
  class OfLongSpliterator private[java8] (sp: java.util.Spliterator.OfLong)
  extends StepperLong with java.util.function.LongConsumer {
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
    def substep(): StepperLong = {
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
  
  def ofSpliterator[A](sp: java.util.Spliterator[A]) = new OfSpliterator[A](sp)
  def ofSpliterator(sp: java.util.Spliterator.OfDouble) = new OfDoubleSpliterator(sp)
  def ofSpliterator(sp: java.util.Spliterator.OfInt) = new OfIntSpliterator(sp)
  def ofSpliterator(sp: java.util.Spliterator.OfLong) = new OfLongSpliterator(sp)
}

trait StepperGeneric[A] extends Stepper[A, StepperGeneric[A]] with java.util.Iterator[A] with java.util.Spliterator[A] {
  def forEachRemaining(c: java.util.function.Consumer[_ >: A]) { while (hasNext) { c.accept(next) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = next
  def tryAdvance(c: java.util.function.Consumer[_ >: A]): Boolean = if (hasNext) { c.accept(next); true } else false
  def tryStep(f: A => Unit): Boolean = if (hasNext) { f(next); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely: StepperGeneric[A] = this
  
  def filter(p: A => Boolean): StepperGeneric[A] = new FilteredStepperGeneric(this, p)
}

trait FilteredStepperGeneric[A](orig: StepperGeneric[A], predicate: A => Boolean) extends StepperGeneric[A] {
  private val knownComplete = false
  private val myCache: A = null.asInstanceOf[A]
  private val isCached = false
  private var myOrig = orig
  private var myPred = predicate
  
  private def loadCache(): Boolean = if (knownComplete) false else {
    while (orig.hasNext) {
      val temp = orig.next
      if (predicate(temp)) {
        isCached = true
        myCache = temp
        return true
      }
    }
    false
  }
  
  override def getExactSizeIfKnown = if (knownComplete) 0L else -1L
  override def hasNext(): Boolean = isCached || loadCache
  override def next(): A =
    if (hasNext) { val ans = myCache; myCache = null.asInstanceOf[A]; isCached = false; ans }
    else throw new NoSuchElementException("Out of elements in Stepper.next")
  override def substep() = {
    val ss = orig.substep()
    if (ss == null) null
    else {
      val init = new FilteredStepperGeneric[A](ss, predicate)
      init.myCache = myCache
      init.isCached = isCached
      myCache = null.asInstanceOf[A]
      isCached = false
      init
    }
  }
}

trait StepperDouble extends Stepper[Double, StepperDouble] with java.util.PrimitiveIterator.OfDouble with java.util.Spliterator.OfDouble {
  def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Double]) { while (hasNext) { c.accept(java.lang.Double.valueOf(nextDouble)) } }
  def forEachRemaining(c: java.util.function.DoubleConsumer) { while (hasNext) { c.accept(nextDouble) } }
  def hasStep = hasNext()
  def knownSize = getExactSizeIfKnown
  def nextStep = nextDouble
  def tryAdvance(c: java.util.function.Consumer[_ >: java.lang.Double]): Boolean = if (hasNext) { c.accept(java.lang.Double.valueOf(nextDouble)); true } else false
  def tryAdvance(c: java.util.function.DoubleConsumer): Boolean = if (hasNext) { c.accept(nextDouble); true } else false
  def tryStep(f: Double => Unit): Boolean = if (hasNext) { f(nextDouble); true } else false
  def trySplit() = substep.typedPrecisely
  final def typedPrecisely = this
}

trait StepperInt extends Stepper[Int, StepperInt] with java.util.PrimitiveIterator.OfInt with java.util.Spliterator.OfInt {
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
}

trait StepperLong extends Stepper[Long, StepperLong] with java.util.PrimitiveIterator.OfLong with java.util.Spliterator.OfLong {
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
}

