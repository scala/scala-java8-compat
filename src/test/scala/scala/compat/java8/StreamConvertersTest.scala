package scala.compat.java8

import org.junit.Test
import org.junit.Assert._

class StreamConvertersTest {
  import java.util.stream._
  import StreamConverters._
  
  def assertEq[A](a1: A, a2: A, s: String) { assertEquals(s, a1, a2) }  // Weird order normally!
  def assertEq[A](a1: A, a2: A) { assertEq(a1, a2, "not equal") }
  
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
  def streamAccumulate() {
    for (n <- ns) {
      val vecO = arrayO(n).toVector
      val accO = newStream(n).parallel.accumulate
      assertEq(vecO, newStream(n).accumulate.to[Vector], s"stream $n to vector")
      assertEq(vecO, accO.to[Vector], s"stream $n to vector in parallel")
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
        assertEq(vecD, newDoubleStream(n).accumulate.to[Vector], s"double stream $n to vector $sbox")
        assertEq(vecD, accD.to[Vector], s"double stream $n to vector in parallel $sbox")
        assertEq(vecD, accD.toArray.toVector, s"double stream $n to vector via array in parallel $sbox")
        assertEq(vecD, accD.iterator.toVector, s"double stream $n to vector via iterator in parallel $sbox")
        assertEq(vecD, accD.toList.toVector, s"double stream $n to vector via list in parallel $sbox")
        assert((0 until accD.size.toInt).forall(i => vecD(i) == accD(i)), s"double stream $n indexed via accumulator $sbox")
        assert(accD.isInstanceOf[scala.compat.java8.collectionImpl.DoubleAccumulator], s"double stream $n to generic accumulator $sbox")

        val vecI = arrayI(n).toVector
        val accI =
          if (boxless) newIntStream(n).parallel.accumulate
          else newIntStream(n).boxed.parallel.accumulatePrimitive
        assertEq(vecI, newIntStream(n).accumulate.to[Vector], s"int stream $n to vector $sbox")
        assertEq(vecI, accI.to[Vector], s"int stream $n to vector in parallel $sbox")
        assertEq(vecI, accI.toArray.toVector, s"int stream $n to vector via array in parallel $sbox")
        assertEq(vecI, accI.iterator.toVector, s"int stream $n to vector via iterator in parallel $sbox")
        assertEq(vecI, accI.toList.toVector, s"int stream $n to vector via list in parallel $sbox")
        assert((0 until accI.size.toInt).forall(i => vecI(i) == accI(i)), s"int stream $n indexed via accumulator $sbox")
        assert(accI.isInstanceOf[scala.compat.java8.collectionImpl.IntAccumulator], s"int stream $n to generic accumulator $sbox")

        val vecL = arrayL(n).toVector
        val accL =
          if (boxless) newLongStream(n).parallel.accumulate
          else newLongStream(n).boxed.parallel.accumulatePrimitive
        assertEq(vecL, newLongStream(n).accumulate.to[Vector], s"long stream $n to vector $sbox")
        assertEq(vecL, accL.to[Vector], s"long stream $n to vector in parallel $sbox")
        assertEq(vecL, accL.toArray.toVector, s"long stream $n to vector via array in parallel $sbox")
        assertEq(vecL, accL.iterator.toVector, s"long stream $n to vector via iterator in parallel $sbox")
        assertEq(vecL, accL.toList.toVector, s"long stream $n to vector via list in parallel $sbox")
        assert((0 until accL.size.toInt).forall(i => vecL(i) == accL(i)), s"long stream $n indexed via accumulator $sbox")
        assert(accL.isInstanceOf[scala.compat.java8.collectionImpl.LongAccumulator], s"long stream $n to generic accumulator $sbox")
      }
    }
  }
  
  @Test
  def streamToScala() {
    for (n <- ns) {
      val vecO = arrayO(n).toVector
      assertEq(vecO, newStream(n).toScala[Vector])
      assertEq(vecO, newStream(n).parallel.toScala[Vector])
      
      val vecD = arrayD(n).toVector
      assertEq(vecD, newDoubleStream(n).toScala[Vector])
      assertEq(vecD, newDoubleStream(n).parallel.toScala[Vector])
      
      val vecI = arrayI(n).toVector
      assertEq(vecI, newIntStream(n).toScala[Vector])
      assertEq(vecI, newIntStream(n).parallel.toScala[Vector])
      
      val vecL = arrayL(n).toVector
      assertEq(vecL, newLongStream(n).toScala[Vector])
      assertEq(vecL, newLongStream(n).parallel.toScala[Vector])
    }
  }
  
  @Test
  def streamUnbox() {
    assert(newDoubleStream(1).boxed.unboxed.isInstanceOf[DoubleStream])
    assert(newIntStream(1).boxed.unboxed.isInstanceOf[IntStream])
    assert(newLongStream(1).boxed.unboxed.isInstanceOf[LongStream])
  }

  import collection.mutable.{ ArrayBuffer, WrappedArray }
  def abufO(n: Int) = { val ab = new ArrayBuffer[String]; arrayO(n).foreach(ab += _); ab }
  def abufD(n: Int) = { val ab = new ArrayBuffer[Double]; arrayD(n).foreach(ab += _); ab }
  def abufI(n: Int) = { val ab = new ArrayBuffer[Int]; arrayI(n).foreach(ab += _); ab }
  def abufL(n: Int) = { val ab = new ArrayBuffer[Long]; arrayL(n).foreach(ab += _); ab }
  def wrapO(n: Int): WrappedArray[String] = arrayO(n)
  def wrapD(n: Int): WrappedArray[Double] = arrayD(n)
  def wrapI(n: Int): WrappedArray[Int] = arrayI(n)
  def wrapL(n: Int): WrappedArray[Long] = arrayL(n)
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
  def scalaToStream() {
    for (n <- ns) {
      val arrO = arrayO(n)
      val seqO = arrO.toSeq
      val abO = abufO(n)
      val wrO = wrapO(n)
      val vecO = vectO(n)
      val hsO = hsetO(n)
      // Seems like a lot of boilerplate, but we need it to test implicit resolution
      assertEq(seqO, seqO.seqStream.toScala[Seq])
      assertEq(seqO, seqO.parStream.toScala[Seq])
      assertEq(seqO, arrO.seqStream.toScala[Seq])
      assertEq(seqO, arrO.parStream.toScala[Seq])
      assertEq(seqO, abO.seqStream.toScala[Seq])
      assertEq(seqO, abO.parStream.toScala[Seq])
      assertEq(seqO, wrO.seqStream.toScala[Seq])
      assertEq(seqO, wrO.parStream.toScala[Seq])
      assertEq(seqO, vecO.seqStream.toScala[Seq])
      assertEq(seqO, vecO.parStream.toScala[Seq])
      assertEq(seqO, hsO.seqStream.toScala[Seq].sortBy(_.toInt))
      assertEq(seqO, hsO.parStream.toScala[Seq].sortBy(_.toInt))
      
      val arrD = arrayD(n)
      val seqD = arrD.toSeq
      val abD = abufD(n)
      val wrD = wrapD(n)
      val vecD = vectD(n)
      val hsD = hsetD(n)
      assertEq(seqD, seqD.seqStream.toScala[Seq])
      assertEq(seqD, seqD.parStream.toScala[Seq])
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
      assertEq(seqD, hsD.seqStream.toScala[Seq].sorted)
      assertEq(seqD, hsD.parStream.toScala[Seq].sorted)
      assert(hsD.seqStream.isInstanceOf[DoubleStream])
      assert(hsD.parStream.isInstanceOf[DoubleStream])
      
      val arrI = arrayI(n)
      val seqI = arrI.toSeq
      val abI = abufI(n)
      val wrI = wrapI(n)
      val vecI = vectI(n)
      val hsI = hsetI(n)
      assertEq(seqI, seqI.seqStream.toScala[Seq])
      assertEq(seqI, seqI.parStream.toScala[Seq])
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
      assertEq(seqI, hsI.seqStream.toScala[Seq].sorted)
      assertEq(seqI, hsI.parStream.toScala[Seq].sorted)
      assert(hsI.seqStream.isInstanceOf[IntStream])
      assert(hsI.parStream.isInstanceOf[IntStream])
      
      val arrL = arrayL(n)
      val seqL = arrL.toSeq
      val abL = abufL(n)
      val wrL = wrapL(n)
      val vecL = vectL(n)
      val hsL = hsetL(n)
      assertEq(seqL, seqL.seqStream.toScala[Seq])
      assertEq(seqL, seqL.parStream.toScala[Seq])
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
      assertEq(seqL, hsL.seqStream.toScala[Seq].sorted)
      assertEq(seqL, hsL.parStream.toScala[Seq].sorted)
      assert(hsL.seqStream.isInstanceOf[LongStream])
      assert(hsL.parStream.isInstanceOf[LongStream])
    }
  }
}
