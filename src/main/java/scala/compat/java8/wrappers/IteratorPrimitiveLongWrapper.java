/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8.wrappers;

public class IteratorPrimitiveLongWrapper implements java.util.PrimitiveIterator.OfLong {
  private java.util.Iterator<Long> it;
  public IteratorPrimitiveLongWrapper(java.util.Iterator<Long> it) {
    this.it = it;
  }
  public boolean hasNext() { return it.hasNext(); }
  public long nextLong() { return it.next().longValue(); }
  public void forEachRemaining(java.util.function.Consumer<? super Long> c) {
    it.forEachRemaining(c);
  }
}
