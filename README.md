## Functional Interfaces for Scala functions

A set of [Functional Interfaces](http://download.java.net/jdk8/docs/api/java/lang/FunctionalInterface.html)
for `scala.FunctionN`. These are designed for convenient construction of Scala functions
using Java 8 lambda syntax.

### Usage

```java
import scala.concurrent.*;
import static scala.runtime.F.func;

class Test {
	private static Future<Integer> futureExample(Future<String> future, ExecutionContext ec) {
	    return future.map(func(s -> s.toUpperCase()), ec).map(func(s -> s.length()), ec);
	}
}
```

[More Examples / Documentation](https://github.com/retronym/java-8-function1/blob/master/src/test/java/scala/runtime/test/Test.java)

### Hacking

[Code Generator](https://github.com/retronym/java-8-function1/blob/master/project/CodeGen.scala)

#### Running Examples

```
% (export JAVA_HOME=`java_home 1.8`; export PATH=$JAVA_HOME/bin:$PATH; sbt 'test:runMain scala.runtime.test.Test')
```
