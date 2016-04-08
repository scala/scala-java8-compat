/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.runtime.java8;

import org.junit.Test;
import scala.runtime.BoxedUnit;

public class SpecializedFactoryTest {
    @Test public void intIntFunction() {
        scala.Function1<Integer, Integer> f1 = JFunction.funcSpecialized((int x) -> x);
        assert(f1 instanceof JFunction1$mcII$sp);

        scala.Function1<Integer, BoxedUnit> f2 = JFunction.procSpecialized((int x) -> System.out.print(""));
        assert(f2 instanceof JFunction1$mcVI$sp);

        scala.Function0<BoxedUnit> f3 = JFunction.procSpecialized(() -> System.out.print(""));
        assert (f3 instanceof JFunction0$mcV$sp);
    }
}
