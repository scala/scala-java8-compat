/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.compat.java8;

import org.apache.commons.lang3.SerializationUtils;
import scala.runtime.*;

import static junit.framework.Assert.assertEquals;
import static scala.compat.java8.JFunction.*;
import static scala.compat.java8.TestAPI.*;

import org.junit.Test;


public class LambdaTest {
    @Test
    public void lambdaDemo() {
        // Scala 2.12+ only:
        //scala.Function1<String, String> f1 = (String s) -> s;

        // Not allowed with Scala 2.10 nor 2.11
        // "incompatible types: Function1 is not a functional interface"
        // scala.Function1<String, String> f = (String s) -> s;

        // Function1 is not a functional interface because it has abstract
        // methods in addition to apply, namely `compose` and `andThen`
        // (which are implemented in Scala-derived subclasses with mixin
        // inheritance), and the specialized variants of apply (also provided
        // by scalac.)

        // That's a pity, but we can get pretty close with this library!

        // We have to tell javac to use `JFunction1` as the functional interface.
        // Scala 2.12 does not have or need JFunction anymore. We provide it as a
        // deprecated stub for backwards compatibility. Use `scala.Function1` for
        // code that targets Scala 2.12+ exclusively.
        JFunction1<String, String> f1 = (String s) -> s;

        // That's more or less equivalent to the old, anonymous class syntax:
        new JFunction1<String, String>() {
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

        // Factory methods in `JFunction` can reduce the verbosity a little:
        // `func` is actually just an identity method; it only exists to
        // trigger lambda creation using the `JFunction1` functional interface.
        scala.Function1<String, String> f3 = func((String s) -> s);

        // Note that javac's type inference can infer the parameter type here,
        // based on the acribed type of `f4`.
        scala.Function1<String, String> f4 = func(s -> s);

        // f1.apply("");

        // Specialized variants of the `apply` method are provided but implementing a specialized
        // Scala function in this straight-forward way results in boxing and unboxing because the
        // Java lambda operates on boxed types:
        JFunction1<Integer, Integer> f5 = (i) -> -i;
        assert(f5.apply(1) == -1);
        assert(f5.apply$mcII$sp(1) == -1);

        // We provide `JFunction.funcSpecialized` and `JFunction.procSpecialized` methods to avoid
        // boxing:
        scala.Function1<Integer, Integer> f5b = funcSpecialized((int i) -> -i);
        assert(f5b.apply(1) == -1);
        assert(f5b.apply$mcII$sp(1) == -1);

        // as are `curried`, `tupled`, `compose`, `andThen`.
        f3.compose(f3).andThen(f3).apply("");
        scala.Function2<String, String, String> f6 = func((s1, s2) -> join(s1, s2));
        assert(f6.curried().apply("1").apply("2").equals("12"));

        // Functions returning unit can use the `JProcedure1`, ... functional interfaces
        // in order to convert a void lamdba return to Scala's Unit:
        JProcedure1<String> f7b = s -> sideEffect();
        scala.Function1<String, BoxedUnit> f7c = f7b;

        // The easiest way to do this is via `JFunction.proc`, ....
        //
        // Note that the lambda has a return type of `void` if the last
        // statement is a call to a `void` returning method, or if it is
        // a `return`.
        scala.Function1<String, BoxedUnit> f7 = proc(s -> sideEffect());
        scala.Function1<String, BoxedUnit> f8 = proc(s -> {s.toUpperCase(); return;});

        // Function0 is also available
        scala.Function0<String> f9 = func(() -> "42");
        assert(f9.apply().equals("42"));

        // You can go up to 22 (the highest arity function defined in the Scala standard library.)
        assert(acceptFunction1(func(v1 -> v1.toUpperCase())).equals("1"));
        acceptFunction1Unit(proc(v1 -> sideEffect()));
        acceptFunction1Unit(proc(v1 -> {v1.toUpperCase(); return;}));

        assert(acceptFunction2(func((v1, v2) -> join(v1, v2))).equals("12"));
        acceptFunction2Unit(proc((v1, v2) -> {v1.toUpperCase(); return;}));

        assert(acceptFunction3(func((v1, v2, v3) -> join(v1, v2, v3))).equals("123"));
        acceptFunction3Unit(proc((v1, v2, v3) -> {v1.toUpperCase(); return;}));

        assert(acceptFunction22(func((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> join(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))).equals("12345678910111213141516171819202122"));
        acceptFunction22Unit(   proc((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> {v1.toUpperCase(); return;}));
    }

    @Test
    public void isSerializable() {
        JFunction0<String> f0 = () -> "foo";
        assertEquals("foo", SerializationUtils.clone(f0).apply());

        JFunction1<String, String> f1 = (a) -> a.toUpperCase();
        assertEquals("FOO", SerializationUtils.clone(f1).apply("foo"));

        JFunction2<String, String, String> f2 = (a, b) -> a + b;
        assertEquals("foobar", SerializationUtils.clone(f2).apply("foo", "bar"));

        JFunction3<String, String, String, String> f3 = (a, b, c) -> a + b + c;
        assertEquals("foobarbaz", SerializationUtils.clone(f3).apply("foo", "bar", "baz"));
    }

    private static scala.concurrent.Future<Integer> futureExample(
            scala.concurrent.Future<String> future, scala.concurrent.ExecutionContext ec) {
        return future.map(func(s -> s.toUpperCase()), ec).map(func(s -> s.length()), ec);
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
