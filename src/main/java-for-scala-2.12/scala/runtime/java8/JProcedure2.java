/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure2<T1, T2> extends scala.Function2<T1, T2, BoxedUnit> {
    void applyVoid(T1 t1, T2 t2);

    default BoxedUnit apply(T1 t1, T2 t2) {
        applyVoid(t1, t2);
        return BoxedUnit.UNIT;
    }
}
