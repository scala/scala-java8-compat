package scala.compat.java8.converterImpls

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

private[java8] class StepsAnyIterator[A](_underlying: Iterator[A])
extends StepsLikeIterator[A, StepsAnyIterator[A]](_underlying) {
  def semiclone() = new StepsAnyIterator(null)
  def next() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsDoubleIterator(_underlying: Iterator[Double])
extends StepsDoubleLikeIterator[StepsDoubleIterator](_underlying) {
  def semiclone() = new StepsDoubleIterator(null)
  def nextDouble() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsIntIterator(_underlying: Iterator[Int])
extends StepsIntLikeIterator[StepsIntIterator](_underlying) {
  def semiclone() = new StepsIntIterator(null)
  def nextInt() = if (proxied ne null) proxied.nextStep else underlying.next
}

private[java8] class StepsLongIterator(_underlying: Iterator[Long])
extends StepsLongLikeIterator[StepsLongIterator](_underlying) {
  def semiclone() = new StepsLongIterator(null)
  def nextLong() = if (proxied ne null) proxied.nextStep else underlying.next
}
