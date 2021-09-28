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

package scala.compat.java8.runtime;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public final class LambdaDeserializerTest {
    private LambdaHost lambdaHost = new LambdaHost();

    // We skip most tests on Java 17+ because of https://github.com/scala/bug/issues/12419
    // which we only fixed for 2.12+

    @Test
    public void serializationPrivate() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByPrivateImplMethod();
        Assert.assertEquals(f1.apply(true), reconstitute(f1).apply(true));
    }

    @Test
    public void serializationStatic() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByStaticImplMethod();
        Assert.assertEquals(f1.apply(true), reconstitute(f1).apply(true));
    }

    @Test
    public void serializationVirtualMethodReference() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByVirtualMethodReference();
        Assert.assertEquals(f1.apply(true), reconstitute(f1).apply(true));
    }

    @Test
    public void serializationInterfaceMethodReference() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F1<I, Object> f1 = lambdaHost.lambdaBackedByInterfaceMethodReference();
        I i = new I() {
        };
        Assert.assertEquals(f1.apply(i), reconstitute(f1).apply(i));
    }

    @Test
    public void serializationStaticMethodReference() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByStaticMethodReference();
        Assert.assertEquals(f1.apply(true), reconstitute(f1).apply(true));
    }

    @Test
    public void serializationNewInvokeSpecial() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F0<Object> f1 = lambdaHost.lambdaBackedByConstructorCall();
        Assert.assertEquals(f1.apply(), reconstitute(f1).apply());
    }

    @Test
    public void uncached() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        F0<Object> f1 = lambdaHost.lambdaBackedByConstructorCall();
        F0<Object> reconstituted1 = reconstitute(f1);
        F0<Object> reconstituted2 = reconstitute(f1);
        Assert.assertNotEquals(reconstituted1.getClass(), reconstituted2.getClass());
    }

    @Test
    public void cached() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        HashMap<String, MethodHandle> cache = new HashMap<>();
        F0<Object> f1 = lambdaHost.lambdaBackedByConstructorCall();
        F0<Object> reconstituted1 = reconstitute(f1, cache);
        F0<Object> reconstituted2 = reconstitute(f1, cache);
        Assert.assertEquals(reconstituted1.getClass(), reconstituted2.getClass());
    }

    @Test
    public void cachedStatic() {
        Assume.assumeFalse(scala.util.Properties.isJavaAtLeast("17"));
        HashMap<String, MethodHandle> cache = new HashMap<>();
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByStaticImplMethod();
        // Check that deserialization of a static lambda always returns the
        // same instance.
        Assert.assertSame(reconstitute(f1, cache), reconstitute(f1, cache));

        // (as is the case with regular invocation.)
        Assert.assertSame(f1, lambdaHost.lambdaBackedByStaticImplMethod());
    }

    @Test
    public void implMethodNameChanged() {
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByStaticImplMethod();
        SerializedLambda sl = writeReplace(f1);
        checkIllegalAccess(copySerializedLambda(sl, sl.getImplMethodName() + "___", sl.getImplMethodSignature()));
    }

    @Test
    public void implMethodSignatureChanged() {
        F1<Boolean, String> f1 = lambdaHost.lambdaBackedByStaticImplMethod();
        SerializedLambda sl = writeReplace(f1);
        checkIllegalAccess(copySerializedLambda(sl, sl.getImplMethodName(), sl.getImplMethodSignature().replace("Boolean", "Integer")));
    }

    private void checkIllegalAccess(SerializedLambda serialized) {
        try {
            LambdaDeserializer.deserializeLambda(MethodHandles.lookup(), null, serialized);
            throw new AssertionError();
        } catch (IllegalArgumentException iae) {
            if (!iae.getMessage().contains("Illegal lambda deserialization")) {
                Assert.fail("Unexpected message: " + iae.getMessage());
            }
        }
    }

    private SerializedLambda copySerializedLambda(SerializedLambda sl, String implMethodName, String implMethodSignature) {
        Object[] captures = new Object[sl.getCapturedArgCount()];
        for (int i = 0; i < captures.length; i++) {
            captures[i] = sl.getCapturedArg(i);
        }
        return new SerializedLambda(loadClass(sl.getCapturingClass()), sl.getFunctionalInterfaceClass(), sl.getFunctionalInterfaceMethodName(),
                sl.getFunctionalInterfaceMethodSignature(), sl.getImplMethodKind(), sl.getImplClass(), implMethodName, implMethodSignature,
                sl.getInstantiatedMethodType(), captures);
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className.replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private <A, B> A reconstitute(A f1) {
        return reconstitute(f1, null);
    }

    @SuppressWarnings("unchecked")
    private <A, B> A reconstitute(A f1, java.util.HashMap<String, MethodHandle> cache) {
        try {
            return (A) LambdaDeserializer.deserializeLambda(LambdaHost.lookup(), cache, writeReplace(f1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <A> SerializedLambda writeReplace(A f1) {
        try {
            Method writeReplace = f1.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(f1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


interface F1<A, B> extends Serializable {
    B apply(A a);
}

interface F0<A> extends Serializable {
    A apply();
}

class LambdaHost {
    public F1<Boolean, String> lambdaBackedByPrivateImplMethod() {
        int local = 42;
        return (b) -> Arrays.asList(local, b ? "true" : "false", LambdaHost.this).toString();
    }

    @SuppressWarnings("Convert2MethodRef")
    public F1<Boolean, String> lambdaBackedByStaticImplMethod() {
        return (b) -> String.valueOf(b);
    }

    public F1<Boolean, String> lambdaBackedByStaticMethodReference() {
        return String::valueOf;
    }

    public F1<Boolean, String> lambdaBackedByVirtualMethodReference() {
        return Object::toString;
    }

    public F1<I, Object> lambdaBackedByInterfaceMethodReference() {
        return I::i;
    }

    public F0<Object> lambdaBackedByConstructorCall() {
        return String::new;
    }

    public static MethodHandles.Lookup lookup() {
        return MethodHandles.lookup();
    }
}

interface I {
    default String i() { return "i"; };
}
