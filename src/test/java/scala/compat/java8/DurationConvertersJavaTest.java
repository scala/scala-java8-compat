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

package scala.compat.java8;

import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.java8.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class DurationConvertersJavaTest {

  @Test
  public void apiAccessibleFromJava() {
    DurationConverters.toScala(Duration.of(5, ChronoUnit.SECONDS));
    DurationConverters.toJava(FiniteDuration.create(5, TimeUnit.SECONDS));
  }

}