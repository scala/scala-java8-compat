/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
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

