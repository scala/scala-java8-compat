/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure3<T1, T2, T3> extends scala.Function3<T1, T2, T3, BoxedUnit> {
    void applyVoid(T1 t1, T2 t2, T3 t3);

    default BoxedUnit apply(T1 t1, T2 t2, T3 t3) {
        applyVoid(t1, t2, t3);
        return BoxedUnit.UNIT;
    }
}
