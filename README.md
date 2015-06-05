## scala-java8-compat [<img src="https://img.shields.io/maven-central/v/org.scala-lang.modules/scala-java8-compat_2.11*.svg?label=latest%20release%20for%202.11"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.scala-lang.modules%20a%3Ascala-java8-compat_2.11*)

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
  - Converters for `java.util.function`, `java.util.stream`
  - [`Spliterator`](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)s for Scala collections
