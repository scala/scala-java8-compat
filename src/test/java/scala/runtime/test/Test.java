/*
 * Copyright (C) 2012-2014 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.runtime.test;

import scala.runtime.*;

public class Test {
    public static void main(String[] args) {
        new F1<String, String>() {
            public String apply(String s) { return s; }
        };
        F1<String, String> f1 = (String s) -> s;

        f1.apply("");

        F1<Integer, Integer> f2 = (i) -> i;
        f2.apply(0);
        f2.apply$mcII$sp(0);

        F1$mcII$sp f3 = (int i) -> i;

        f3.apply$mcII$sp(1);
        f3.apply(1);

        P1<Integer> f4 = (i) -> i;

        needFunction1(F.f1(x -> x.toUpperCase()));
        needProc1(F.p1(x -> x.toUpperCase()));

        String s1 = needFunction1Infer(F.f1(x -> x.toUpperCase())).toUpperCase();
    }

    static void needFunction1(scala.Function1<String, String> f1) {
    }

    static void needProc1(scala.Function1<String, BoxedUnit> f1) {
    }

    static <T> T needFunction1Infer(scala.Function1<String, T> f1) {
        return f1.apply("");
    }
}
