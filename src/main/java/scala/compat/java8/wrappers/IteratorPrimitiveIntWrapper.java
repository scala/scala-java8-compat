/*
 * Copyright (C) 2012-2015 Lightbend Inc. <http://www.lightbend.com>
 */
package scala.compat.java8.wrappers;

public class IteratorPrimitiveIntWrapper implements java.util.PrimitiveIterator.OfInt {
  private java.util.Iterator<Integer> it;
  public IteratorPrimitiveIntWrapper(java.util.Iterator<Integer> it) {
    this.it = it;
  }
  public boolean hasNext() { return it.hasNext(); }
  public int nextInt() { return it.next().intValue(); }
  public void forEachRemaining(java.util.function.Consumer<? super Integer> c) {
    it.forEachRemaining(c);
  }
}
