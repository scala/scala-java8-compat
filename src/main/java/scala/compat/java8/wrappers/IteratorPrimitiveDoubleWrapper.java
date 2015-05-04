/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8.wrappers;

public class IteratorPrimitiveDoubleWrapper implements java.util.PrimitiveIterator.OfDouble {
  private java.util.Iterator<Double> it;
  public IteratorPrimitiveDoubleWrapper(java.util.Iterator<Double> it) {
    this.it = it;
  }
  public boolean hasNext() { return it.hasNext(); }
  public double nextDouble() { return it.next().doubleValue(); }
  public void forEachRemaining(java.util.function.Consumer<? super Double> c) {
    it.forEachRemaining(c);
  }
}
