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

package scala.compat.java8;

import org.junit.Test;
import scala.runtime.java8.*;

public class BoxingTest {
    @Test
    public void nullBoxesInterpretedAsZeroF1() {
        Object o = new JFunction1$mcII$sp() {
            @Override
            public int apply$mcII$sp(int v1) {
                return v1 + 1;
            }
        };
        scala.Function1<Integer, Integer> jFunction1 = (scala.Function1<Integer, Integer>)o;
        Integer result = (Integer) jFunction1.apply(null);
        assert (result.intValue() == 1);
    }

    @Test
    public void nullBoxesInterpretedAsZeroF2() {
        Object o = new JFunction2$mcIII$sp() {
            @Override
            public int apply$mcIII$sp(int v1, int v2) {
                return v1 + v2 + 1;
            }
        };
        scala.Function2<Integer, Integer, Integer> jFunction2 = (scala.Function2<Integer, Integer, Integer>)o;
        Integer result = (Integer) jFunction2.apply(null, null);
        assert (result.intValue() == 1);
    }
}
