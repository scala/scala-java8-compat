/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends scala.Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, BoxedUnit> {
    void applyVoid(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

    default BoxedUnit apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        applyVoid(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        return BoxedUnit.UNIT;
    }
}
