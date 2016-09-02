/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8;

import org.junit.Test;
import scala.runtime.java8.*;

public class BoxingTest {
    @Test
    public void nullBoxesInterpretedAsZeroF1() {
        scala.Function1<Integer, Integer> jFunction1 = new JFunction1$mcII$sp() {
            @Override
            public int apply$mcII$sp(int v1) {
                return v1 + 1;
            }
        };
        Integer result = (Integer) jFunction1.apply(null);
        assert (result.intValue() == 1);
    }

    @Test
    public void nullBoxesInterpretedAsZeroF2() {
        scala.Function2<Integer, Integer, Integer> jFunction2 = new JFunction2$mcIII$sp() {
            @Override
            public int apply$mcIII$sp(int v1, int v2) {
                return v1 + v2 + 1;
            }
        };
        Integer result = (Integer) jFunction2.apply(null, null);
        assert (result.intValue() == 1);
    }
}
