There are two goals: 

- Encode anonymous functions in the same manner as Java 8 lambdas (LambdaMetafactory) without losing the benefits of specialization.
- Enable Java code to treat `scala.Function1` as an functional interface

## `Function1` via `LambdaMetafactory`

Benefits: smaller bytecode, profit from ongoing JVM optimizations
for lambda elision, inlining, etc.
   
This requires a functional interface for FunctionN, which is a bit
harder than it sounds in the face of specialized variants of apply,
as well as compose/andThen. But, without changing our library at all,
this still look possible!

We need to create interfaces for all specialized variants of Function1.
The abstract method is the specialized apply, and all other applies must
forward to this.

To emit smaller code, we can create a base functional interface in which
the generic apply is abstract, and all of the specialized variants forward
to it. This way, each specialized functional interface need only reabstract
one specialized apply and redirect the unspecialized apply to it.

Here's how they could look:

 - `scala.Function1`
   - [`runtime.F1`](https://github.com/retronym/java-8-function1/blob/master/src/main/java/scala/runtime/F1.java)
     - [`runtime.F1$mcII$sps`](https://github.com/retronym/java-8-function1/blob/master/src/main/java/scala/runtime/F1%24mcII%24sp.java)
     - ... (other specialized variants)

We will then need to modify the backend of scalac to emit
`invokedynamic` against the `LambdaMetafactory`, passing a method
handle to the function-body-in-a-method that results from `-Ydelambdafy:method`
lifted method body. This behaviour would be conditional on a flag, and require
that you have `F1*` on the classpath at runtime. These could be shipped in a
separate JAR.

We could actually do all of this without needing to emit any default methods ourselves; we can simply use a code generator and javac to generate `F1*`!

### Optimizer

We will need to modify `GenBCodeOpt`'s to understand `indy` calls to spin
up lambdas so it can still recognize opportunities for closure inlining.

### Bridges

Today, in Scala:

```
scala> ((s: String) => s).getClass.getDeclaredMethods.mkString("\n")
res2: String =
public final java.lang.String $anonfun$1.apply(java.lang.String)
public final java.lang.Object $anonfun$1.apply(java.lang.Object)
```

In Java8, the the metafactory just spins up a class with *just* the generic
signature. This is safe as the class is anonymous and only ever
called through invoke-interface, so no harm done. Seems like a leaner
representation.

So, to emit only the generic `java.lang.Object $anonfun$1.apply(java.lang.Object)` version,
we inline `java.lang.String $anonfun$1.apply(java.lang.String)` into it,
so that, what ordinarly would be the bridge method, has the closure's body,
with a prelude to do the unboxing.

To call this `apply` method we emit `invoke-interface Object runtime.F1(Object)`.
This is very close to what we currently do, except that we use Function1 as the target.)

Furthermore, by *only* creating the generic signature for anonymous functions,
we would avoid the rather brutal limitation imposed by erasure for value classes, [SI-6260](https://issues.scala-lang.org/browse/SI-6260).

LamdaMetaFactory does have an [advanced API](http://download.java.net/jdk8/docs/api/java/lang/invoke/LambdaMetafactory.html#FLAG_BRIDGES)
that allows to create additional bridges, if needed. We can also mark
the closure as serializable, which should be done to be compatible
with what we do today.

## `scala.Function1` as a functional interface

To do this, we would need to pull up the defender methods from s.r.F1
directly to the trait interface class in the standard library. We can
definitely do this when we mandate Java 8. But is it safe to do it earlier?

I notice that `Function.{andThen, compose}` are still generated in
specialized droves, despite the attempt to mark them as @unspecialized.
This looks like a bug in scalac.

## `invokedynamic` calls, courtesy of javac

[`Test.java`](https://github.com/retronym/java-8-function1/blob/master/src/main/java/scala/runtime/Test.java) contains Java 8 lambdas against `F1` and `F1$mcII$sp`. 

In the following decompilation, you can see the invokedynamic calls:

```
% `java_home -v 1.8`/bin/javap -v -p -classpath target/scala-2.11.0-M7/classes Test
Classfile /Users/jason/code/java-8-function1/target/scala-2.11.0-M7/classes/Test.class
  Last modified Jan 28, 2014; size 1890 bytes
  MD5 checksum 7b6665961e5a4a10440571d0fcbdd2b3
  Compiled from "Test.java"
public class Test
  SourceFile: "Test.java"
  InnerClasses:
       static #2; //class Test$1
       public static final #88= #87 of #90; //Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
  BootstrapMethods:
    0: #49 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
      Method arguments:
        #50 (Ljava/lang/Object;)Ljava/lang/Object;
        #51 invokestatic Test.lambda$main$0:(Ljava/lang/String;)Ljava/lang/String;
        #52 (Ljava/lang/String;)Ljava/lang/String;
    1: #49 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
      Method arguments:
        #50 (Ljava/lang/Object;)Ljava/lang/Object;
        #57 invokestatic Test.lambda$main$1:(Ljava/lang/Integer;)Ljava/lang/Integer;
        #58 (Ljava/lang/Integer;)Ljava/lang/Integer;
    2: #49 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
      Method arguments:
        #62 (I)I
        #63 invokestatic Test.lambda$main$2:(I)I
        #62 (I)I
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   ...
{
  public Test();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 1: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LTest;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: new           #2                  // class Test$1
         3: dup
         4: invokespecial #3                  // Method Test$1."<init>":()V
         7: pop
         8: invokedynamic #4,  0              // InvokeDynamic #0:apply:()Lscala/runtime/F1;
        13: astore_1
        14: aload_1
        15: ldc           #5                  // String
        17: invokeinterface #6,  2            // InterfaceMethod scala/runtime/F1.apply:(Ljava/lang/Object;)Ljava/lang/Object;
        22: pop
        23: invokedynamic #7,  0              // InvokeDynamic #1:apply:()Lscala/runtime/F1;
        28: astore_2
        29: aload_2
        30: iconst_0
        31: invokestatic  #8                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        34: invokeinterface #6,  2            // InterfaceMethod scala/runtime/F1.apply:(Ljava/lang/Object;)Ljava/lang/Object;
        39: pop
        40: aload_2
        41: iconst_0
        42: invokeinterface #9,  2            // InterfaceMethod scala/runtime/F1.apply$mcII$sp:(I)I
        47: pop
        48: invokedynamic #10,  0             // InvokeDynamic #2:apply$mcII$sp:()Lscala/runtime/F1$mcII$sp;
        53: astore_3
        54: aload_2
        55: iconst_1
        56: invokeinterface #9,  2            // InterfaceMethod scala/runtime/F1.apply$mcII$sp:(I)I
        61: pop
        62: aload_2
        63: iconst_1
        64: invokestatic  #8                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        67: invokeinterface #6,  2            // InterfaceMethod scala/runtime/F1.apply:(Ljava/lang/Object;)Ljava/lang/Object;
        72: pop
        73: return
      LineNumberTable:
        line 3: 0
        line 6: 8
        line 8: 14
        line 10: 23
        line 11: 29
        line 12: 40
        line 14: 48
        line 16: 54
        line 17: 62
        line 18: 73
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      74     0  args   [Ljava/lang/String;
           14      60     1    f1   Lscala/runtime/F1;
           29      45     2    f2   Lscala/runtime/F1;
           54      20     3    f3   Lscala/runtime/F1$mcII$sp;
      LocalVariableTypeTable:
        Start  Length  Slot  Name   Signature
           14      60     1    f1   Lscala/runtime/F1<Ljava/lang/String;Ljava/lang/String;>;
           29      45     2    f2   Lscala/runtime/F1<Ljava/lang/Integer;Ljava/lang/Integer;>;

  private static int lambda$main$2(int);
    descriptor: (I)I
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=1, locals=1, args_size=1
         0: iload_0
         1: ireturn
      LineNumberTable:
        line 14: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       2     0     i   I

  private static java.lang.Integer lambda$main$1(java.lang.Integer);
    descriptor: (Ljava/lang/Integer;)Ljava/lang/Integer;
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: areturn
      LineNumberTable:
        line 10: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       2     0     i   Ljava/lang/Integer;

  private static java.lang.String lambda$main$0(java.lang.String);
    descriptor: (Ljava/lang/String;)Ljava/lang/String;
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: areturn
      LineNumberTable:
        line 6: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       2     0     s   Ljava/lang/String;
}
```