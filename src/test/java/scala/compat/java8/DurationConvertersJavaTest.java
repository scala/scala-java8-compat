/*
 * Copyright (C) 2012-2018 Lightbend Inc. <http://www.lightbend.com>
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