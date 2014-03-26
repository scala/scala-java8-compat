## scala-java8-compat [<img src="https://api.travis-ci.org/scala/scala-java8-compat.png"/>](https://travis-ci.org/scala/scala-java8-compat)

A Java 8 compatibility Kit for Scala.

### Functional Interfaces for Scala functions

A set of [Functional Interfaces](http://download.java.net/jdk8/docs/api/java/lang/FunctionalInterface.html)
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

### Converters between `scala.concurrent` and `java.util.concurrent`

[API](src/test/java/scala/compat/java8/FutureConverters.java)
[Test Cases](src/test/java/scala/compat/java8/FutureConvertersTest.java)

### Converters for `java.util.function`

TODO

### Converters for `java.util.stream`

TODO

### Hacking

[Code Generator](project/CodeGen.scala)

#### Running Examples

```
% (export JAVA_HOME=`java_home 1.8`; export PATH=$JAVA_HOME/bin:$PATH; sbt test)
```

### Future work

  - Augment the code generator to also generate specialized variants of the functional interface and
    modify scalac to emit lambdas as calls to the lambda MetaFactory against them.

```java
@FunctionalInterface
public interface JFunction1$mcII$sp extends JFunction1 {
    abstract int apply$mcII$sp(int v1);

    default Object apply(Object s) { return (Integer) apply$mcII$sp((Integer) s); }
}
```