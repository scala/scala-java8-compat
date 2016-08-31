/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

/**
 * @deprecated Use scala.Function2 in Scala 2.12
 */
@Deprecated
@FunctionalInterface
public interface JFunction2<T1, T2, R> extends scala.Function2<T1, T2, R>, java.io.Serializable {
}
