/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
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
