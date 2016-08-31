/*
 * Copyright (C) 2012-2016 Lightbend Inc. <http://www.lightbend.com>
 */

package scala.runtime.java8;

/**
 * @deprecated Use scala.Function1 in Scala 2.12
 */
@Deprecated
@FunctionalInterface
public interface JFunction1<T1, R> extends scala.Function1<T1, R>, java.io.Serializable {
}
