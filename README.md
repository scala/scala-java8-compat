# scala-java8-compat [![Build Status](https://travis-ci.org/scala/scala-java8-compat.svg?branch=master)](https://travis-ci.org/scala/scala-java8-compat) [<img src="https://img.shields.io/maven-central/v/org.scala-lang.modules/scala-java8-compat_2.11.svg?label=latest%20release%20for%202.11"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.scala-lang.modules%20a%3Ascala-java8-compat_2.11) [<img src="https://img.shields.io/maven-central/v/org.scala-lang.modules/scala-java8-compat_2.12.svg?label=latest%20release%20for%202.12"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.scala-lang.modules%20a%3Ascala-java8-compat_2.12) [<img src="https://img.shields.io/maven-central/v/org.scala-lang.modules/scala-java8-compat_2.13.svg?label=latest%20release%20for%202.13"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.scala-lang.modules%20a%3Ascala-java8-compat_2.13)


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


## Converters from Scala collections to Java 8 Streams

Scala collections gain `seqStream` and `parStream` as extension methods that produce a Java 8 Stream
running sequentially or in parallel, respectively.  These are automatically specialized to a primitive
type if possible, including automatically applied widening conversions.  For instance, `List(1,2).seqStream`
produces an `IntStream`, and so does `List(1.toShort, 2.toShort).parStream`.  Maps additionally have
`seqKeyStream`, `seqValueStream`, `parKeyStream`, and `parValueStream` methods.

Scala collections also gain `accumulate` and `stepper` methods that produce utility collections that
can be useful when working with Java 8 Streams.  `accumulate` produces an `Accumulator` or its primitive
counterpart (`DoubleAccumulator`, etc.), which is a low-level collection designed for efficient collection
and dispatching of results to and from Streams.  Unlike most collections, it can contain more than
`Int.MaxValue` elements.

`stepper` produces a `Stepper` which is a fusion of `Spliterator` and `Iterator`. `Stepper`s underlie the Scala
collections' instances of Java 8 Streams.  Steppers are intended as low-level building blocks for streams.
Usually you would not create them directly or call their methods but you can implement them alongside custom
collections to get better performance when streaming from these collections.

Java 8 Streams gain `toScala[Coll]` and `accumulate` methods, to make it easy to produce Scala collections
or Accumulators, respectively, from Java 8 Streams. For instance, `myStream.to[Vector]` will collect the
contents of a Stream into a `scala.collection.immutable.Vector`.  Note that standard sequential builders
are used for collections, so this is best done to gather the results of an expensive computation.

Finally, there is a Java class, `ScalaStreamSupport`, that has a series of `stream` methods that can be used to
obtain Java 8 Streams from Scala collections from within Java.

#### Performance Considerations

For sequential operations, Scala's `iterator` almost always equals or exceeds the performance of a Java 8 stream.  Thus,
one should favor `iterator` (and its richer set of operations) over `seqStream` for general use.  However, long
chains of processing of primitive types can sometimes benefit from the manually specialized methods in `DoubleStream`,
`IntStream`, and `LongStream`.

Note that although `iterator` typically has superior performance in a sequential context, the advantage is modest
(usually less than 50% higher throughput for `iterator`).

For parallel operations, `parStream` and even `seqStream.parallel` meets or exceeds the performance of Scala parallel
collections methods (invoked with `.par`).  Especially for small collections, the difference can be substantial.  In
some cases, when a Scala (parallel) collection is the ultimate result, Scala parallel collections can have an advantage
as the collection can (in some cases) be built in parallel.

Because the wrappers are invoked based on the static type of the collection, there are also cases where parallelization
is inefficient when interfacing with Java 8 Streams (e.g. when a collection is typed as `Seq[String]` so might have linear
access like `List`, but actually is a `WrappedArray[String]` (`ArraySeq` on 2.13) that can be efficiently parallelized) but can be efficient
with Scala parallel collections.  The `parStream` method is only available when the static type is known to be compatible
with rapid parallel operation; `seqStream` can be parallelized by using `.parallel`, but may or may not be efficient.

If the operations available on Java 8 Streams are sufficient, the collection type is known statically with enough precision
to enable parStream,  and an `Accumulator` or non-collection type is an acceptable result, Java 8 Streams will essentially
always outperform the Scala parallel collections.

#### Scala Usage Example

```scala
import scala.compat.java8.StreamConverters._

object Test {
  val m = collection.immutable.HashMap("fish" -> 2, "bird" -> 4)
  val s = m.parValueStream.sum          // 6, potientially computed in parallel
  val t = m.seqKeyStream.toScala[List]  // List("fish", "bird")
  val a = m.accumulate                  // Accumulator[(String, Int)]

  val n = a.stepper.fold(0)(_ + _._1.length) +
          a.parStream.count             // 8 + 2 = 10

  val b = java.util.Arrays.stream(Array(2L, 3L, 4L)).
          accumulate                    // LongAccumulator
	val l = b.to[List]                    // List(2L, 3L, 4L)
}
```

#### Using Java 8 Streams with Scala Function Converters

Scala can emit Java SAMs for lambda expressions that are arguments to methods that take a Java SAM rather than
a Scala Function.  However, it can be convenient to restrict the SAM interface to interactions with Java code
(including Java 8 Streams) rather than having it propagate throughout Scala code.

Using Java 8 Stream converters together with function converters allows one to accomplish this with only a modest
amount of fuss.

Example:

```scala
import scala.compat.java8.FunctionConverters._
import scala.compat.java8.StreamConverters._

def mapToSortedString[A](xs: Vector[A], f: A => String, sep: String) =
  xs.parStream.                     // Creates java.util.stream.Stream[String]
    map[String](f.asJava).sorted.   // Maps A to String and sorts (in parallel)
    toArray.mkString(sep)           // Back to an Array to use Scala's mkString
```

Note that explicit creation of a new lambda will tend to lead to improved type inference and at least equal
performance:

```scala
def mapToSortedString[A](xs: Vector[A], f: A => String, sep: String) =
  xs.parStream.
    map[String](a => f(a)).sorted.  // Explicit lambda creates a SAM wrapper for f
    toArray.mkString(sep)
```

#### Java Usage Example

To convert a Scala collection to a Java 8 Stream from within Java, it usually
suffices to call `ScalaStreamSupport.stream(xs)` on your collection `xs`.  If `xs` is
a map, you may wish to get the keys or values alone by using `fromKeys` or
`fromValues`.  If the collection has an underlying representation that is not
efficiently parallelized (e.g. `scala.collection.immutable.List`), then
`fromAccumulated` (and `fromAccumulatedKeys` and `fromAccumulatedValues`) will
first gather the collection into an `Accumulator` and then return a stream over
that accumulator.  If not running in parallel, `from` is preferable (faster and
less memory usage).

Note that a Scala `Iterator` cannot fulfill the contract of a Java 8 Stream
(because it cannot support `trySplit` if it is called).  Presently, one must
call `fromAccumulated` on the `Iterator` to cache it, even if the Stream will
be evaluated sequentially, or wrap it as a Java Iterator and use static
methods in `Spliterator` to wrap that as a `Spliterator` and then a `Stream`.

Here is an example of conversion of a Scala collection within Java 8:

```java
import scala.collection.mutable.ArrayBuffer;
import scala.compat.java8.ScalaStreamSupport;

public class StreamConvertersExample {
  public int MakeAndUseArrayBuffer() {
    ArrayBuffer<String> ab = new ArrayBuffer<String>();
    ab.$plus$eq("salmon");
    ab.$plus$eq("herring");
    return ScalaStreamSupport.stream(ab).mapToInt(x -> x.length()).sum();  // 6+7 = 13
  }
}
```

## Converters between `scala.concurrent.duration.FiniteDuration` and `java.time.Duration`

Interconversion between Java's standard `java.time.Duration` type 
and the `scala.concurrent.duration.FiniteDuration` types. The Java `Duration` does 
not contain a time unit, so when converting from `FiniteDuration` the time unit used 
to create it is lost. 

For the opposite conversion a `Duration` can potentially express a larger time span than
a `FiniteDuration`, for such cases an exception is thrown.

Example of conversions from the Java type ways: 

```scala
import scala.concurrent.duration._
import scala.compat.java8.DurationConverters._

val javaDuration: java.time.Duration = 5.seconds.toJava
val finiteDuration: FiniteDuration = javaDuration.toScala
```

From Java:
```java
import scala.compat.java8.DurationConverters;
import scala.concurrent.duration.FiniteDuration;

DurationConverters.toScala(Duration.of(5, ChronoUnit.SECONDS));
DurationConverters.toJava(FiniteDuration.create(5, TimeUnit.SECONDS));
```


