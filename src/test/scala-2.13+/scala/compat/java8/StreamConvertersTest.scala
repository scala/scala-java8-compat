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

package scala.compat.java8

import scala.language.higherKinds

import org.junit.Test
import org.junit.Assert._

import java.util.stream._
import StreamConverters._

class StreamConvertersTest {

  def assertEq[A](a1: A, a2: A, s: String): Unit = { assertEquals(s, a1, a2) }  // Weird order normally!
  def assertEq[A](a1: A, a2: A): Unit = { assertEq(a1, a2, "not equal") }
  def assert(b: Boolean): Unit = { assertTrue(b) }
  def assert(b: Boolean, s: String): Unit = { assertTrue(s, b) }

  def arrayO(n: Int) = (1 to n).map(_.toString).toArray
  def arrayD(n: Int) = (1 to n).map(_.toDouble).toArray
  def arrayI(n: Int) = (1 to n).toArray
  def arrayL(n: Int) = (1 to n).map(_.toLong).toArray
  
  def newStream(n: Int) = java.util.Arrays.stream(arrayO(n))
  def newDoubleStream(n: Int) = java.util.Arrays.stream(arrayD(n))
  def newIntStream(n: Int) = java.util.Arrays.stream(arrayI(n))
  def newLongStream(n: Int) = java.util.Arrays.stream(arrayL(n))
  
  val ns = Vector(0, 1, 2, 12, 15, 16, 17, 31, 32, 33, 151, 1298, 7159)
  
  @Test
  def streamAccumulate(): Unit = {
    for (n <- ns) {
      val vecO = arrayO(n).toVector
      val accO = newStream(n).parallel.accumulate
      assertEq(vecO, newStream(n).accumulate.to(Vector), s"stream $n to vector")
      assertEq(vecO, accO.to(Vector), s"stream $n to vector in parallel")
      assertEq(vecO, accO.toArray.toVector, s"stream $n to vector via array in parallel")
      assertEq(vecO, accO.iterator.toVector, s"stream $n to vector via iterator in parallel")
      assertEq(vecO, accO.toList.toVector, s"stream $n to vector via list in parallel")
      assert((0 until accO.size.toInt).forall(i => vecO(i) == accO(i)), s"stream $n indexed via accumulator")
      assert(accO.isInstanceOf[scala.compat.java8.collectionImpl.Accumulator[_]], s"stream $n to generic accumulator")

      for (boxless <- Seq(false, true)) {
        val sbox = (if (boxless) "" else "(boxed)")
        val vecD = arrayD(n).toVector
        val accD =
          if (boxless) newDoubleStream(n).parallel.accumulate
          else newDoubleStream(n).boxed.parallel.accumulatePrimitive
        assertEq(vecD, newDoubleStream(n).accumulate.to(Vector), s"double stream $n to vector $sbox")
        assertEq(vecD, accD.to(Vector), s"double stream $n to vector in parallel $sbox")
        assertEq(vecD, accD.toArray.toVector, s"double stream $n to vector via array in parallel $sbox")
        assertEq(vecD, accD.iterator.toVector, s"double stream $n to vector via iterator in parallel $sbox")
        assertEq(vecD, accD.toList.toVector, s"double stream $n to vector via list in parallel $sbox")
        assert((0 until accD.size.toInt).forall(i => vecD(i) == accD(i)), s"double stream $n indexed via accumulator $sbox")
        assert(accD.isInstanceOf[scala.compat.java8.collectionImpl.DoubleAccumulator], s"double stream $n to generic accumulator $sbox")

        val vecI = arrayI(n).toVector
        val accI =
          if (boxless) newIntStream(n).parallel.accumulate
          else newIntStream(n).boxed.parallel.accumulatePrimitive
        assertEq(vecI, newIntStream(n).accumulate.to(Vector), s"int stream $n to vector $sbox")
        assertEq(vecI, accI.to(Vector), s"int stream $n to vector in parallel $sbox")
        assertEq(vecI, accI.toArray.toVector, s"int stream $n to vector via array in parallel $sbox")
        assertEq(vecI, accI.iterator.toVector, s"int stream $n to vector via iterator in parallel $sbox")
        assertEq(vecI, accI.toList.toVector, s"int stream $n to vector via list in parallel $sbox")
        assert((0 until accI.size.toInt).forall(i => vecI(i) == accI(i)), s"int stream $n indexed via accumulator $sbox")
        assert(accI.isInstanceOf[scala.compat.java8.collectionImpl.IntAccumulator], s"int stream $n to generic accumulator $sbox")

        val vecL = arrayL(n).toVector
        val accL =
          if (boxless) newLongStream(n).parallel.accumulate
          else newLongStream(n).boxed.parallel.accumulatePrimitive
        assertEq(vecL, newLongStream(n).accumulate.to(Vector), s"long stream $n to vector $sbox")
        assertEq(vecL, accL.to(Vector), s"long stream $n to vector in parallel $sbox")
        assertEq(vecL, accL.toArray.toVector, s"long stream $n to vector via array in parallel $sbox")
        assertEq(vecL, accL.iterator.toVector, s"long stream $n to vector via iterator in parallel $sbox")
        assertEq(vecL, accL.toList.toVector, s"long stream $n to vector via list in parallel $sbox")
        assert((0 until accL.size.toInt).forall(i => vecL(i) == accL(i)), s"long stream $n indexed via accumulator $sbox")
        assert(accL.isInstanceOf[scala.compat.java8.collectionImpl.LongAccumulator], s"long stream $n to generic accumulator $sbox")
      }
    }
  }

  @Test
  def streamToScala(): Unit = {
    for (n <- ns) {
      val vecO = arrayO(n).toVector
      assertEq(vecO, newStream(n).toScala(Vector))
      assertEq(vecO, newStream(n).parallel.toScala(Vector))
      assertEq(vecO, newStream(n).toScala[Vector])
      assertEq(vecO, newStream(n).parallel.toScala[Vector])

      val vecD = arrayD(n).toVector
      assertEq(vecD, newDoubleStream(n).toScala(Vector))
      assertEq(vecD, newDoubleStream(n).parallel.toScala(Vector))
      assertEq(vecD, newDoubleStream(n).toScala[Vector])
      assertEq(vecD, newDoubleStream(n).parallel.toScala[Vector])

      val vecI = arrayI(n).toVector
      assertEq(vecI, newIntStream(n).toScala(Vector))
      assertEq(vecI, newIntStream(n).parallel.toScala(Vector))
      assertEq(vecI, newIntStream(n).toScala[Vector])
      assertEq(vecI, newIntStream(n).parallel.toScala[Vector])

      val vecL = arrayL(n).toVector
      assertEq(vecL, newLongStream(n).toScala(Vector))
      assertEq(vecL, newLongStream(n).parallel.toScala(Vector))
      assertEq(vecL, newLongStream(n).toScala[Vector])
      assertEq(vecL, newLongStream(n).parallel.toScala[Vector])
    }
  }

  @Test
  def streamUnbox(): Unit = {
    assert(newDoubleStream(1).boxed.unboxed.isInstanceOf[DoubleStream])
    assert(newIntStream(1).boxed.unboxed.isInstanceOf[IntStream])
    assert(newLongStream(1).boxed.unboxed.isInstanceOf[LongStream])
  }

  import collection.mutable.{ ArrayBuffer, ArraySeq }
  def abufO(n: Int) = { val ab = new ArrayBuffer[String]; arrayO(n).foreach(ab += _); ab }
  def abufD(n: Int) = { val ab = new ArrayBuffer[Double]; arrayD(n).foreach(ab += _); ab }
  def abufI(n: Int) = { val ab = new ArrayBuffer[Int]; arrayI(n).foreach(ab += _); ab }
  def abufL(n: Int) = { val ab = new ArrayBuffer[Long]; arrayL(n).foreach(ab += _); ab }
  def wrapO(n: Int): ArraySeq[String] = arrayO(n)
  def wrapD(n: Int): ArraySeq[Double] = arrayD(n)
  def wrapI(n: Int): ArraySeq[Int] = arrayI(n)
  def wrapL(n: Int): ArraySeq[Long] = arrayL(n)
  def vectO(n: Int) = arrayO(n).toVector
  def vectD(n: Int) = arrayD(n).toVector
  def vectI(n: Int) = arrayI(n).toVector
  def vectL(n: Int) = arrayL(n).toVector
  def genhset[A](aa: Array[A]) = { val hs = new collection.mutable.HashSet[A]; aa.foreach(hs += _); hs }
  def hsetO(n: Int) = genhset(arrayO(n))
  def hsetD(n: Int) = genhset(arrayD(n))
  def hsetI(n: Int) = genhset(arrayI(n))
  def hsetL(n: Int) = genhset(arrayL(n))

  @Test
  def scalaToStream(): Unit = {
    for (n <- ns) {
      val arrO = arrayO(n)
      val seqO = arrO.toSeq
      val abO = abufO(n)
      val wrO = wrapO(n)
      val vecO = vectO(n)
      val hsO = hsetO(n)
      // Seems like a lot of boilerplate, but we need it to test implicit resolution
      assertEq(seqO, seqO.seqStream.toScala[Seq])
//      assertEq(seqO, seqO.stepper.parStream.toScala[Seq])  // Must go through stepper if we're unsure whether we can parallelize well
      assertEq(seqO, arrO.seqStream.toScala[Seq])
      assertEq(seqO, arrO.parStream.toScala[Seq])
      assertEq(seqO, abO.seqStream.toScala[Seq])
      assertEq(seqO, abO.parStream.toScala[Seq])
      assertEq(seqO, wrO.seqStream.toScala[Seq])
      assertEq(seqO, wrO.parStream.toScala[Seq])
      assertEq(seqO, vecO.seqStream.toScala[Seq])
      assertEq(seqO, vecO.parStream.toScala[Seq])
//      assertEq(seqO, hsO.seqStream.toScala[Seq].sortBy(_.toInt))
//      assertEq(seqO, hsO.parStream.toScala[Seq].sortBy(_.toInt))

      val arrD = arrayD(n)
      val seqD = arrD.toSeq
      val abD = abufD(n)
      val wrD = wrapD(n)
      val vecD = vectD(n)
      val hsD = hsetD(n)
      assertEq(seqD, seqD.seqStream.toScala[Seq])
//      assertEq(seqD, seqD.stepper.parStream.toScala[Seq])
      assertEq(seqD, arrD.seqStream.toScala[Seq])
      assertEq(seqD, arrD.parStream.toScala[Seq])
      assert(arrD.seqStream.isInstanceOf[DoubleStream])
      assert(arrD.parStream.isInstanceOf[DoubleStream])
      assertEq(seqD, abD.seqStream.toScala[Seq])
      assertEq(seqD, abD.parStream.toScala[Seq])
      assert(abD.seqStream.isInstanceOf[DoubleStream])
      assert(abD.parStream.isInstanceOf[DoubleStream])
      assertEq(seqD, wrD.seqStream.toScala[Seq])
      assertEq(seqD, wrD.parStream.toScala[Seq])
      assert(wrD.seqStream.isInstanceOf[DoubleStream])
      assert(wrD.parStream.isInstanceOf[DoubleStream])
      assertEq(seqD, vecD.seqStream.toScala[Seq])
      assertEq(seqD, vecD.parStream.toScala[Seq])
      assert(vecD.seqStream.isInstanceOf[DoubleStream])
      assert(vecD.parStream.isInstanceOf[DoubleStream])
//      assertEq(seqD, hsD.seqStream.toScala[Seq].sorted)
//      assertEq(seqD, hsD.parStream.toScala[Seq].sorted)
//      assert(hsD.seqStream.isInstanceOf[DoubleStream])
//      assert(hsD.parStream.isInstanceOf[DoubleStream])

      val arrI = arrayI(n)
      val seqI = arrI.toSeq
      val abI = abufI(n)
      val wrI = wrapI(n)
      val vecI = vectI(n)
      val hsI = hsetI(n)
      assertEq(seqI, seqI.seqStream.toScala[Seq])
//      assertEq(seqI, seqI.stepper.parStream.toScala[Seq])
      assertEq(seqI, arrI.seqStream.toScala[Seq])
      assertEq(seqI, arrI.parStream.toScala[Seq])
      assert(arrI.seqStream.isInstanceOf[IntStream])
      assert(arrI.parStream.isInstanceOf[IntStream])
      assertEq(seqI, abI.seqStream.toScala[Seq])
      assertEq(seqI, abI.parStream.toScala[Seq])
      assert(abI.seqStream.isInstanceOf[IntStream])
      assert(abI.parStream.isInstanceOf[IntStream])
      assertEq(seqI, wrI.seqStream.toScala[Seq])
      assertEq(seqI, wrI.parStream.toScala[Seq])
      assert(wrI.seqStream.isInstanceOf[IntStream])
      assert(wrI.parStream.isInstanceOf[IntStream])
      assertEq(seqI, vecI.seqStream.toScala[Seq])
      assertEq(seqI, vecI.parStream.toScala[Seq])
      assert(vecI.seqStream.isInstanceOf[IntStream])
      assert(vecI.parStream.isInstanceOf[IntStream])
//      assertEq(seqI, hsI.seqStream.toScala[Seq].sorted)
//      assertEq(seqI, hsI.parStream.toScala[Seq].sorted)
//      assert(hsI.seqStream.isInstanceOf[IntStream])
//      assert(hsI.parStream.isInstanceOf[IntStream])

      val arrL = arrayL(n)
      val seqL = arrL.toSeq
      val abL = abufL(n)
      val wrL = wrapL(n)
      val vecL = vectL(n)
      val hsL = hsetL(n)
      assertEq(seqL, seqL.seqStream.toScala[Seq])
//      assertEq(seqL, seqL.stepper.parStream.toScala[Seq])
      assertEq(seqL, arrL.seqStream.toScala[Seq])
      assertEq(seqL, arrL.parStream.toScala[Seq])
      assert(arrL.seqStream.isInstanceOf[LongStream])
      assert(arrL.parStream.isInstanceOf[LongStream])
      assertEq(seqL, abL.seqStream.toScala[Seq])
      assertEq(seqL, abL.parStream.toScala[Seq])
      assert(abL.seqStream.isInstanceOf[LongStream])
      assert(abL.parStream.isInstanceOf[LongStream])
      assertEq(seqD, wrD.seqStream.toScala[Seq])
      assertEq(seqD, wrD.parStream.toScala[Seq])
      assert(wrL.seqStream.isInstanceOf[LongStream])
      assert(wrL.parStream.isInstanceOf[LongStream])
      assertEq(seqD, wrD.seqStream.toScala[Seq])
      assertEq(seqD, wrD.parStream.toScala[Seq])
      assert(vecL.seqStream.isInstanceOf[LongStream])
      assert(vecL.parStream.isInstanceOf[LongStream])
//      assertEq(seqL, hsL.seqStream.toScala[Seq].sorted)
//      assertEq(seqL, hsL.parStream.toScala[Seq].sorted)
//      assert(hsL.seqStream.isInstanceOf[LongStream])
//      assert(hsL.parStream.isInstanceOf[LongStream])
    }
  }

  @Test
  def primitiveStreamTypes(): Unit = {
    // Unboxed native + widening Steppers available:
    assertEquals(Vector[Int](1, 2, 3), (Array[Int](1, 2, 3).seqStream: IntStream).toScala[Vector])
    assertEquals(Vector[Short](1.toShort, 2.toShort, 3.toShort), (Array[Short](1.toShort, 2.toShort, 3.toShort).seqStream: IntStream).toScala[Vector])
    assertEquals(Vector[String]("a", "b"), (Array[String]("a", "b").seqStream: Stream[String]).toScala[Vector])

    // Boxed collections, widening via boxed AnySteppers:
    assertEquals(Vector[Int](1, 2, 3), (Vector[Int](1, 2, 3).seqStream: IntStream).toScala[Vector])
    assertEquals(Vector[Short](1.toShort, 2.toShort, 3.toShort), (Vector[Short](1.toShort, 2.toShort, 3.toShort).seqStream: IntStream).toScala[Vector])
    assertEquals(Vector[String]("a", "b"), (Vector[String]("a", "b").seqStream: Stream[String]).toScala[Vector])
  }

  @Test
  def issue_87(): Unit = {
    // Vectors that are generated from other vectors tend _not_ to
    // have all their display vectors consistent; the cached vectors
    // are correct, but the higher-level vector does _not_ contain
    // the cached vector in the correct place (for efficiency)!  This
    // is called being "dirty", and needs to be handled specially.
    val dirtyDisplayVector = Vector.fill(120)("a").slice(0, 40)
    val shouldNotNPE =
      dirtyDisplayVector.seqStream.collect(Collectors.toList())
    assertEq(shouldNotNPE.toArray(new Array[String](0)).toVector, dirtyDisplayVector, "Vector[Any].seqStream (with dirty display)")

    val dirtyDisplayVectorInt = Vector.fill(120)(999).slice(0, 40)
    val shouldNotNPEInt =
      dirtyDisplayVectorInt.seqStream.sum()
    assertEq(shouldNotNPEInt, dirtyDisplayVectorInt.sum, "Vector[Int].seqStream (with dirty display)")

    val dirtyDisplayVectorLong = Vector.fill(120)(99999999999L).slice(0, 40)
    val shouldNotNPELong =
      dirtyDisplayVectorLong.seqStream.sum()
    assertEq(shouldNotNPELong, dirtyDisplayVectorLong.sum, "Vector[Long].seqStream (with dirty display)")

    val dirtyDisplayVectorDouble = Vector.fill(120)(0.1).slice(0, 40)
    val shouldNotNPEDouble =
      math.rint(dirtyDisplayVectorDouble.seqStream.sum() * 10)
    assertEq(shouldNotNPEDouble, math.rint(dirtyDisplayVectorDouble.sum * 10), "Vector[Double].seqStream (with dirty display)")
  }
}
