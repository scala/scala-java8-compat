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

import org.junit.Test;

import scala.collection.mutable.ArrayBuffer;
import scala.compat.java8.ScalaStreamSupport;


public class StreamConvertersExampleTest {
  @Test
  public void MakeAndUseArrayBuffer() {
    ArrayBuffer<String> ab = new ArrayBuffer<String>();
    ab.$plus$eq("salmon");
    ab.$plus$eq("herring");
    assert( ScalaStreamSupport.stream(ab).mapToInt(x -> x.length()).sum() == 13 );
  }
}

