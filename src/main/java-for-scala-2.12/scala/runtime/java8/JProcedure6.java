/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure6<T1, T2, T3, T4, T5, T6> extends scala.Function6<T1, T2, T3, T4, T5, T6, BoxedUnit> {
    void applyVoid(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

    default BoxedUnit apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        applyVoid(t1, t2, t3, t4, t5, t6);
        return BoxedUnit.UNIT;
    }
}
