/*
 * Copyright (C) 2012-2014 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.runtime.test;

import scala.runtime.*;
import static scala.runtime.test.TestAPI.*;

public class Test {
    public static void main(String[] args) {
        // Not allowed with Scala 2.10 nor 2.11
        // "incompatible types: Function1 is not a functional interface"
        // scala.Function1<String, String> f = (String s) -> s;

        // Function1 is not a functional interface because it has abstract
        // methods in addition to apply, namely `compose` and `andThen`
        // (which are implemented in Scala-derived subclasses with mixin
        // inheritance), and the specialized variants of apply (also provided
        // by scalac.)

        // That's a pity, but we can get pretty close with this library!

        // We have to tell javac to use `F1` as the functional interface.
        F1<String, String> f1 = (String s) -> s;

        // That's more or less equivalent to the old, anonymous class syntax:
        new F1<String, String>() {
            public String apply(String s) { return s; }
        };

        // You might have seen this form before:
        new AbstractFunction1<String, String>() {
            public String apply(String s) { return s; }
        };

        // However, we can't use `AbstractFunction1` as a functional interface
        // as it is a class. Further

        // F1 is a subclass of Function1:
        scala.Function1<String, String> f2 = f1;

        // Factory methods in `F` can reduce the verbosity a little:
        // `f1` is actually just an identity method; it only exists to
        // trigger lambda creation using the `F1` functional interface.
        scala.Function1<String, String> f3 = F.f1((String s) -> s);

        // Note that javac's type inference can infer the parameter type here,
        // based on the acribed type of `f4`.
        scala.Function1<String, String> f4 = F.f1(s -> s);

        // f1.apply("");

        // Specialized variants of the `apply` method are implenented in the
        // functional interface
        F1<Integer, Integer> f5 = (i) -> -i;
        assert(f5.apply(1) == -1);
        assert(f5.apply$mcII$sp(1) == -1);

        // as are `curried`, `tupled`, `compose`, `andThen`.
        f3.compose(f3).andThen(f3).apply("");
        scala.Function2<String, String, String> f6 = F.f2((s1, s2) -> join(s1, s2));
        assert(f6.curried().apply("1").apply("2") == "12");

        // Functions returning unit must use the `P1`, ... functional interfaces
        // in order to convert a void lamdba return to Scala's Unit.
        //
        // The easiest way to do this is via `F.p1`, ....
        //
        // Note that the lambda has a return type of `void` if the last
        // statement is a call to a `void` returning method, or if it is
        // a `return`.
        scala.Function1<String, BoxedUnit> f7 = F.p1(s -> sideEffect());
        scala.Function1<String, BoxedUnit> f8 = F.p1(s -> {s.toUpperCase(); return;});

        // Function0 is also available
        scala.Function0<String> f9 = F.f0(() -> "42");
        assert(f9.apply() == "42");

        // You can go up to 22 (the highest arity function defined in the Scala standard library.)
        assert(acceptFunction1(F.f1(v1 -> v1.toUpperCase())) == "1");
        acceptFunction1Unit(F.p1(v1 -> sideEffect()));
        acceptFunction1Unit(F.p1(v1 -> {v1.toUpperCase(); return;}));

        assert(acceptFunction2(F.f2((v1, v2) -> join(v1, v2))) == "12");
        acceptFunction2Unit(F.p2((v1, v2) -> {v1.toUpperCase(); return;}));

        assert(acceptFunction3(F.f3((v1, v2, v3) -> join(v1, v2, v3))) == "123");
        acceptFunction3Unit(F.p3((v1, v2, v3) -> {v1.toUpperCase(); return;}));

        assert(acceptFunction22(F.f22((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> join(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))) == "12345678910111213141516171819202122");
        acceptFunction22Unit(   F.p22((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> {v1.toUpperCase(); return;}));
    }

    private static void sideEffect() {
    }

    private static String join(String... ss) {
        String result = "";
        for (String s : ss) {
            result = result + s;
        }
        return result;
    }
}
