/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure1<T1> extends scala.Function1<T1, BoxedUnit> {
    void applyVoid(T1 t1);

    default BoxedUnit apply(T1 t1) {
        applyVoid(t1);
        return BoxedUnit.UNIT;
    }
}
