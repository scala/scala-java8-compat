## scala-java8-compat [<img src="https://img.shields.io/travis/scala/async.svg"/>](https://travis-ci.org/scala/async) [<img src="https://img.shields.io/maven-central/v/org.scala-lang.modules/scala-java8-compat_2.11*.svg?label=latest%20release%20for%202.11"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.scala-lang.modules%20a%3Ascala-java8-compat_2.11*)

A Java 8 compatibility kit for Scala.

The API is currently still experimental: we do not yet guarantee source or binary compatibility with future releases.

## Functional Interfaces for Scala functions

A set of [Functional Interfaces](https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html)
for `scala.FunctionN`. These are designed for convenient construction of Scala functions
using Java 8 lambda syntax.

#### Usage

```java
import scala.concurrent.*;
import static scala.compat.java8.JFunction.*;

class Test {
	private static Future<Integer> futureExample(Future<String> future, ExecutionContext ec) {
	    return future.map(func(s -> s.toUpperCase()), ec).map(func(s -> s.length()), ec);
	}
}
```

[More Examples / Documentation](src/test/java/scala/compat/java8/LambdaTest.java)

## Converters between `scala.FunctionN` and `java.util.function`

A set of converters that enable interconversion between Java's standard
Functional Interfaces defined in `java.util.function` and Scala's `Function0`,
`Function1`, and `Function2` traits.  These are intended for use when you
already have an instance of a `java.util.function` and need a Scala function,
or have a Scala function and need an instance of a `java.util.function`.

The `.asScala` extension method will convert a `java.util.function` to the corresponding
Scala function.  The `.asJava` extension method will convert a Scala function to
the most specific corresponding Java functional interface.  If you wish to obtain
a less specific functional interface, there are named methods that start with `asJava`
and continue with the name of the Java functional interface.  For instance, the
most specific interface corresponding to the Scala function `val rev = (s: String) => s.reverse`
is `UnaryOperator[String]`, and that is what `rev.asJava` will produce.  However,
`asJavaFunction(rev)` will return a `java.util.function.Function[String, String]` instead.

The `asJava` methods can also be called conveniently from Java.  There are additional
`asScalaFrom` methods (e.g. `asScalaFromUnaryOperator`) that will perform the
functional-interface-to-Scala-function conversion; this is primarily of use when calling
from Java since the `.asScala` extension method is more convenient in Scala.

#### Usage examples

In Scala:

```scala
import java.util.function._
import scala.compat.java8.FunctionConverters._

val foo: Int => Boolean = i => i > 7
def testBig(ip: IntPredicate) = ip.test(9)
println(testBig(foo.asJava))  // Prints true

val bar = new UnaryOperator[String]{ def apply(s: String) = s.reverse }
List("cod", "herring").map(bar.asScala)    // List("doc", "gnirrih")

def testA[A](p: Predicate[A])(a: A) = p.test(a)
println(testA(asJavaPredicate(foo))(4))  // Prints false

// println(testA(foo.asJava)(4))  <-- doesn't work
//                                    IntPredicate does not extend Predicate!
```

In Java:

```java
import java.util.function.*;
import scala.compat.java8.FunctionConverters;

class Example {
  String foo(UnaryOperator<String> f) {
    return f.apply("halibut");
  }
  String bar(scala.Function1<String, String> f) {
    return foo(functionConverters.asJavaUnaryOperator(f));
  }
  String baz(Function<String, String> f) {
    return bar(functionConverters.asScalaFromFunction(f));
  }
}
```

## Converters between `scala.concurrent` and `java.util.concurrent`

 - [API](src/main/scala/scala/compat/java8/FutureConverters.scala)
 - [Test Cases](src/test/java/scala/compat/java8/FutureConvertersTest.java)

## Converters between `scala.Option` and `java.util` classes `Optional`, `OptionalDouble`, `OptionalInt`, and `OptionalLong`.

A set of extension methods to enable explicit conversion between [Scala Option](http://www.scala-lang.org/files/archive/api/2.11.6/#scala.Option) and the Java 8
optional types, [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html),
[OptionalDouble](https://docs.oracle.com/javase/8/docs/api/java/util/OptionalDouble.html),
[OptionalInt](https://docs.oracle.com/javase/8/docs/api/java/util/OptionalInt.html),
and [OptionalLong](https://docs.oracle.com/javase/8/docs/api/java/util/OptionalLong.html).

Note that the four Java classes have no inheritance relationship despite all encoding optional types.

#### Usage example

```scala
import scala.compat.java8.OptionConverters._

class Test {
  val o = Option(2.7)
  val oj = o.asJava        // Optional[Double]
  val ojd = o.asPrimitive  // OptionalDouble
  val ojds = ojd.asScala   // Option(2.7) again
}
```


## Future work
  - Converters for `java.util.stream`
  - [`Spliterator`](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)s for Scala collections
