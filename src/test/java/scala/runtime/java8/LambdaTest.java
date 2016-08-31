/*
 * Copyright (C) 2012-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.runtime.java8;

import org.apache.commons.lang3.SerializationUtils;
import scala.runtime.*;

import static junit.framework.Assert.assertEquals;
import static scala.runtime.java8.TestAPI.*;

import org.junit.Test;


public class LambdaTest {
    /*
    // This version is for Scala 2.12.0-RC1 and is not compatible with 2.11. It's commented out to allow cross-building.
    @Test
    public void lambdaDemo() {
        scala.Function1<String, String> f1 = (String s) -> s;

        // That's more or less equivalent to the old, anonymous class syntax:
        new scala.Function1<String, String>() {
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

        scala.Function1<String, String> f3 = (String s) -> s;
        scala.Function1<String, String> f4 = s -> s;

        // Specialized variants of the `apply` method are implenented in the
        // functional interface
        scala.Function1<Integer, Integer> f5 = (i -> -i);
        assert(f5.apply(1) == -1);
        assert(f5.apply$mcII$sp(1) == -1);

        // as are `curried`, `tupled`, `compose`, `andThen`.
        f3.compose(f3).andThen(f3).apply("");
        scala.Function2<String, String, String> f6 = ((s1, s2) -> join(s1, s2));
        assert(f6.curried().apply("1").apply("2").equals("12"));

        // Functions returning unit must return BoxedUnit.UNIT explicitly.
        //
        // Note that the lambda has a return type of `void` if the last
        // statement is a call to a `void` returning method, or if it is
        // a `return`.
        scala.Function1<String, BoxedUnit> f7 = (s -> { sideEffect(); return scala.runtime.BoxedUnit.UNIT; });
        scala.Function1<String, BoxedUnit> f8 = (s -> { s.toUpperCase(); return scala.runtime.BoxedUnit.UNIT; });

        // Function0 is also available
        scala.Function0<String> f9 = (() -> "42");
        assert(f9.apply().equals("42"));

        // You can go up to 22 (the highest arity function defined in the Scala standard library.)
        assert(acceptFunction1((v1 -> v1.toUpperCase())).equals("1"));
        acceptFunction1Unit((v1 -> {sideEffect(); return scala.runtime.BoxedUnit.UNIT;}));
        acceptFunction1Unit((v1 -> {v1.toUpperCase(); return scala.runtime.BoxedUnit.UNIT;}));

        assert(acceptFunction2(((v1, v2) -> join(v1, v2))).equals("12"));
        acceptFunction2Unit(((v1, v2) -> {v1.toUpperCase(); return scala.runtime.BoxedUnit.UNIT;}));

        assert(acceptFunction3(((v1, v2, v3) -> join(v1, v2, v3))).equals("123"));
        acceptFunction3Unit(((v1, v2, v3) -> {v1.toUpperCase(); return scala.runtime.BoxedUnit.UNIT;}));

        assert(acceptFunction22(((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> join(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22))).equals("12345678910111213141516171819202122"));
        acceptFunction22Unit(   ((v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22) -> {v1.toUpperCase(); return scala.runtime.BoxedUnit.UNIT;}));
    }
    */

    /*
    // The JFunctions in 2.12.0-M4 are not Serializable anymore
    @Test
    public void isSerializable() {
        scala.Function0<String> f0 = () -> "foo";
        assertEquals("foo", SerializationUtils.clone(f0).apply());

        scala.Function1<String, String> f1 = (a) -> a.toUpperCase();
        assertEquals("FOO", SerializationUtils.clone(f1).apply("foo"));

        scala.Function2<String, String, String> f2 = (a, b) -> a + b;
        assertEquals("foobar", SerializationUtils.clone(f2).apply("foo", "bar"));

        scala.Function3<String, String, String, String> f3 = (a, b, c) -> a + b + c;
        assertEquals("foobarbaz", SerializationUtils.clone(f3).apply("foo", "bar", "baz"));
    }
    */

    /*
    // This version is for Scala 2.12.0-RC1 and is not compatible with 2.11. It's commented out to allow cross-building.
    private static scala.concurrent.Future<Integer> futureExample(
        scala.concurrent.Future<String> future, scala.concurrent.ExecutionContext ec) {
        return future.map(s -> s.toUpperCase(), ec).map(s -> s.length(), ec);
    }
    */

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
