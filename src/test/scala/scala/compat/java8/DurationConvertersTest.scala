/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package scala.compat.java8

import org.junit.Test
import org.junit.Assert._
import java.time.{Duration => JavaDuration}

import scala.util.Try

class DurationConvertersTest {

  import DurationConverters._
  import scala.concurrent.duration._

  @Test
  def scalaNanosToJavaDuration(): Unit = {
    Seq[(Long, (Long, Int))](
      (Long.MinValue + 1) -> (-9223372037L, 145224193), // because java duration nanos are offset from the "wrong" direction
      -1000000001L        -> (-2, 999999999),
      -1L                 -> (-1, 999999999),
      0L                  -> (0, 0),
      1L                  -> (0, 1),
      1000000001L         -> (1,1),
      Long.MaxValue       -> (9223372036L, 854775807)
    ).foreach { case (n, (expSecs, expNanos)) =>
      val result = toJava(n.nanos)
      assertEquals(s"toJava($n nanos) -> $expSecs s)", expSecs, result.getSeconds)
      assertEquals(s"toJava($n nanos) -> $expNanos n)", expNanos, result.getNano)
    }
  }

  @Test
  def scalaMilliSecondsToJavaDuration(): Unit = {
    Seq[(Long, (Long, Int))](
      -9223372036854L -> (-9223372037L, 146000000),
      -1L             -> (-1L, 999000000),
      0L              -> (0L,  0),
      1L              -> (0L,  1000000),
      9223372036854L  -> (9223372036L, 854000000)
    ).foreach { case (n, (expSecs, expNanos)) =>
      val result = toJava(n.millis)
      assertEquals(s"toJava($n millis) -> $expSecs s)", expSecs, result.getSeconds)
      assertEquals(s"toJava($n millis) -> $expNanos n)", expNanos, result.getNano)
    }
  }

  @Test
  def scalaMicroSecondsToJavaDuration(): Unit = {
    Seq[(Long, (Long, Int))](
      -9223372036854775L -> (-9223372037L, 145225000),
      -1L                -> (-1L, 999999000),
      0L                 -> (0L,  0),
      1L                 -> (0L,  1000),
      9223372036854775L  -> (9223372036L, 854775000)
    ).foreach { case (n, (expSecs, expNanos)) =>
      val result = toJava(n.micros)
      assertEquals(s"toJava($n micros) -> $expSecs s)", expSecs, result.getSeconds)
      assertEquals(s"toJava($n micros) -> $expNanos n)", expNanos, result.getNano)
    }
  }

  @Test
  def scalaSecondsToJavaDuration(): Unit = {
    Seq[(Long, (Long, Int))](
      -9223372036L -> (-9223372036L, 0),
      -1L          -> (-1L, 0),
      0L           -> (0L,  0),
      1L           -> (1L,  0),
      9223372036L  -> (9223372036L, 0)
    ).foreach { case (n, (expSecs, expNanos)) =>
      val result = toJava(n.seconds)
      assertEquals(expSecs, result.getSeconds)
      assertEquals(expNanos, result.getNano)
    }
  }


  @Test
  def javaSecondsToScalaDuration(): Unit = {
    Seq[Long](-9223372036L, -1L, 0L, 1L, 9223372036L).foreach { n =>
      assertEquals(n, toScala(JavaDuration.ofSeconds(n)).toSeconds)
    }
  }


  @Test
  def javaNanosPartToScalaDuration(): Unit = {
    Seq[Long](Long.MinValue + 1L, -1L, 0L, 1L, Long.MaxValue).foreach { n =>
      assertEquals(n, toScala(JavaDuration.ofNanos(n)).toNanos)
    }
  }

  @Test
  def unsupportedScalaDurationThrows(): Unit = {
    Seq(Duration.Inf, Duration.MinusInf, Duration.Undefined).foreach { d =>
      val res = Try { toJava(d) }
      assertTrue(s"Expected exception for $d but got success", res.isFailure)
    }
  }

  @Test
  def unsupportedJavaDurationThrows(): Unit = {
    Seq(JavaDuration.ofSeconds(-9223372037L), JavaDuration.ofSeconds(9223372037L)).foreach { d =>
      val res = Try { toScala(d) }
      assertTrue(s"Expected exception for $d but got success", res.isFailure)
    }
  }


}
