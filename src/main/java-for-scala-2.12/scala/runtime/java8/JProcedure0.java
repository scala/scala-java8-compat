/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface JProcedure0 extends scala.Function0<BoxedUnit> {
    void applyVoid();

    default BoxedUnit apply() {
        applyVoid();
        return BoxedUnit.UNIT;
    }
}
