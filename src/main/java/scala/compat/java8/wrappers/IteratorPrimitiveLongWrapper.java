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
