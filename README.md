## scala-java8-compat [<img src="https://api.travis-ci.org/scala/scala-java8-compat.png"/>](https://travis-ci.org/scala/scala-java8-compat)

A Java 8 compatibility kit for Scala.

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

 - [API](src/main/java/scala/compat/java8/FutureConverters.java)
 - [Test Cases](src/test/java/scala/compat/java8/FutureConvertersTest.java)

## Adding an SBT dependency
To depend on scala-xml in SBT, add something like this to your build.sbt:

```
libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.2.0"
```

Maven users, or sbt users looking to support multiple Scala versions, please see the more elaborate example in https://github.com/scala/scala-module-dependency-sample.

The API is currently still experimental: we do not yet guarantee source or binary compatibility with future releases.

## Future work
  - Converters for `java.util.function`, `java.util.stream`
  - [`Spliterator`](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)s for Scala collections
