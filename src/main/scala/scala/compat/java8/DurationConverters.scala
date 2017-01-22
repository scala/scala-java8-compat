/*
 * Copyright (C) 2012-2017 Typesafe Inc. <http://www.typesafe.com>
 */
package scala.compat.java8

import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import java.time.{Duration => JavaDuration}

import scala.concurrent.duration.{FiniteDuration, Duration => ScalaDuration}


/**
 * This class contains static methods which convert between Java Durations
 * and the durations from the Scala concurrency package. This is useful when mediating between Scala and Java
 * libraries with asynchronous APIs where timeouts for example are often expressed as durations.
 */
object DurationConverters {

  /**
   * Transform a Java duration into a Scala duration. If the nanosecond part of the Java duration is zero the returned
   * duration will have a time unit of seconds and if there is a nanoseconds part the Scala duration will have a time
   * unit of nanoseconds.
   *
   * @throws IllegalArgumentException If the given Java Duration is out of bounds of what can be expressed with the
   *                                  Scala Durations.
   */
  final def toScala(duration: java.time.Duration): scala.concurrent.duration.Duration = {
    if (duration.getNano == 0) {
      if (duration.getSeconds == 0) ScalaDuration.Zero
      else FiniteDuration(duration.getSeconds, TimeUnit.SECONDS)
    } else {
      FiniteDuration(
        duration.getSeconds * 1000000000 + duration.getNano,
        TimeUnit.NANOSECONDS
      )
    }
  }

  /**
   * Transform a Scala duration into a Java duration. Note that the Scala duration keeps the time unit it was created
   * with while a Java duration always is a pair of seconds and nanos, so the unit it lost.
   *
   * @throws IllegalArgumentException If the Scala duration express a amount of time for the time unit that
   *                                  a Java Duration can not express (infinite durations and undefined durations)
   */
  final def toJava(duration: scala.concurrent.duration.Duration): java.time.Duration = {
    require(duration.isFinite(), s"Got [$duration] but only finite Scala durations can be expressed as a Java Durations")
    if (duration.length == 0) JavaDuration.ZERO
    else duration.unit match {
      case TimeUnit.NANOSECONDS => JavaDuration.ofNanos(duration.length)
      case TimeUnit.MICROSECONDS => JavaDuration.of(duration.length, ChronoUnit.MICROS)
      case TimeUnit.MILLISECONDS => JavaDuration.ofMillis(duration.length)
      case TimeUnit.SECONDS => JavaDuration.ofSeconds(duration.length)
      case TimeUnit.MINUTES => JavaDuration.ofMinutes(duration.length)
      case TimeUnit.HOURS => JavaDuration.ofHours(duration.length)
      case TimeUnit.DAYS => JavaDuration.ofDays(duration.length)
    }
  }

}
