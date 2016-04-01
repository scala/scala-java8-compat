/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8

import language.implicitConversions
import java.util.{ Iterator => JIterator, PrimitiveIterator }

/** This class enables conversion from `scala.Iterator` to the set of
  * `java.util.PrimitiveIterator` classes.
  *
  * Scala's `Iterator` is generic, as is its `java.util` counterpart.  However,
  * `java.util.PrimitiveIterator` offers three manually-specialized variants of
  * `Iterator`: `OfDouble`, `OfInt`, and `OfLong`.  This class provides
  * `.asPrimitive` extension methods for Scala and Java iterators to present
  * the generic versions as the specialized version.
  *
  * Example usage:
  *
  * {{{
  * import scala.compat.java8.PrimitiveIteratorConverters._
  * val it = Iterator(1.0, 2.0, math.Pi)
  * val jpid = it.asPrimitive   // PrimitiveIterator.OfDouble
  * }}}
  */
object PrimitiveIteratorConverters {
  /** Type class implementing conversion from generic `Option` or `Optional` to manually specialized variants. */
  sealed abstract class SpecializerOfIterators[A, That] {
    /** Packages a Java `Iterator` as a manually specialized variant `That` */
    def fromJava(it: JIterator[A]): That
    /** Packages a Scala `Iterator` to a manually specialized Java variant `That` */
    def fromScala(it: Iterator[A]): That
  }
  
  /** Implementation of wrapping of `java.util.Iterator[Double]` or `scala.collection.Iterator[Double]` as a `java.util.PrimitiveIterator.OfDouble` */
  implicit val specializer_PrimitiveIteratorDouble = new SpecializerOfIterators[Double, PrimitiveIterator.OfDouble] {
    /** Packages a `java.util.Iterator[Double]` as a `java.util.PrimitiveIterator.OfDouble` */
    def fromJava(it: JIterator[Double]): PrimitiveIterator.OfDouble = 
      new wrappers.IteratorPrimitiveDoubleWrapper(it.asInstanceOf[JIterator[java.lang.Double]])
    
    /** Packages a `scala.collection.Iterator[Double]` as a `java.util.PrimitiveIterator.OfDouble` */
    def fromScala(it: Iterator[Double]): PrimitiveIterator.OfDouble = new PrimitiveIterator.OfDouble {
      def hasNext = it.hasNext
      def next() = it.next()
      def nextDouble() = it.next()
      def remove() { throw new UnsupportedOperationException("remove on scala.collection.Iterator") }
      def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Double]) {
        while (it.hasNext) c.accept(it.next)
      }
      def forEachRemaining(c: java.util.function.DoubleConsumer) {
        while (it.hasNext) c.accept(it.next)
      }
    }
  }
  
  /** Implementation of wrapping of `java.util.Iterator[Int]` or `scala.collection.Iterator[Int]` as a `java.util.PrimitiveIterator.OfInt` */
  implicit val specializer_PrimitiveIteratorInt = new SpecializerOfIterators[Int, PrimitiveIterator.OfInt] {
    /** Packages a `java.util.Iterator[Int]` as a `java.util.PrimitiveIterator.OfInt` */
    def fromJava(it: JIterator[Int]): PrimitiveIterator.OfInt = 
      new wrappers.IteratorPrimitiveIntWrapper(it.asInstanceOf[JIterator[java.lang.Integer]])
    
    /** Packages a `scala.collection.Iterator[Int]` as a `java.util.PrimitiveIterator.OfInt` */
    def fromScala(it: Iterator[Int]): PrimitiveIterator.OfInt = new PrimitiveIterator.OfInt {
      def hasNext = it.hasNext
      def next() = it.next()
      def nextInt() = it.next()
      def remove() { throw new UnsupportedOperationException("remove on scala.collection.Iterator") }
      def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Integer]) {
        while (it.hasNext) c.accept(it.next)
      }
      def forEachRemaining(c: java.util.function.IntConsumer) {
        while (it.hasNext) c.accept(it.next)
      }
    }
  }
  
  /** Implementation of wrapping of `java.util.Iterator[Long]` or `scala.collection.Iterator[Long]` as a `java.util.PrimitiveIterator.OfLong` */
  implicit val specializer_PrimitiveIteratorLong = new SpecializerOfIterators[Long, PrimitiveIterator.OfLong] {
    /** Packages a `java.util.Iterator[Long]` as a `java.util.PrimitiveIterator.OfLong` */
    def fromJava(it: JIterator[Long]): PrimitiveIterator.OfLong = 
      new wrappers.IteratorPrimitiveLongWrapper(it.asInstanceOf[JIterator[java.lang.Long]])
    
    /** Packages a `scala.collection.Iterator[Long]` as a `java.util.PrimitiveIterator.OfLong` */
    def fromScala(it: Iterator[Long]): PrimitiveIterator.OfLong = new PrimitiveIterator.OfLong {
      def hasNext = it.hasNext
      def next() = it.next()
      def nextLong() = it.next()
      def remove() { throw new UnsupportedOperationException("remove on scala.collection.Iterator") }
      def forEachRemaining(c: java.util.function.Consumer[_ >: java.lang.Long]) {
        while (it.hasNext) c.accept(it.next)
      }
      def forEachRemaining(c: java.util.function.LongConsumer) {
        while (it.hasNext) c.accept(it.next)
      }
    }
  }
  
  /** Provides conversions from Java `Iterator` to manually specialized `PrimitiveIterator` variants, when available */
  implicit final class RichJavaIteratorToPrimitives[A](private val underlying: JIterator[A]) extends AnyVal {
    /** Wraps this `java.util.Iterator` as a manually specialized variant, if possible */
    def asPrimitive[That](implicit specOp: SpecializerOfIterators[A, That]): That = specOp.fromJava(underlying)
  }
  
  /** Provides conversions from Scala `Iterator` to manually specialized `PrimitiveIterator` variants, when available */
  implicit final class RichIteratorToPrimitives[A](private val underlying: Iterator[A]) extends AnyVal {
    /** Wraps this `scala.collection.Iterator` as a manually specialized `java.util.PrimitiveIterator` variant, if possible */
    def asPrimitive[That](implicit specOp: SpecializerOfIterators[A, That]): That = specOp.fromScala(underlying)
  }
}
