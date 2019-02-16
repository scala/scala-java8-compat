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
