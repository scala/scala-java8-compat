package bench.codegen

import scala.util._
import scala.util.control.NonFatal

object Generator {
  val names = "arr ish lst ils que stm trs vec arb ars ast mhs lhs prq muq wra jix jln".split(' ')

  def writeTo(f: java.io.File)(pr: (String => Unit) => Unit): Either[Throwable, Unit] = {
    try {
      val pw = new java.io.PrintWriter(f)
      val wr: String => Unit = s => pw.println(s)
      try { pr(wr); Right(()) }
      catch { case NonFatal(t) => Left(t) }
      finally { pw.close() }
    }
    catch { case NonFatal(t) => Left(t) }
  }

  def sayArrayI(oa: Option[Array[Int]]) = oa match { case Some(a) => a.mkString("Array(", ",", ")"); case _ => "" }

  def agreement(target: java.io.File, sizes: Option[Array[Int]] = None) {
    if (target.exists) throw new java.io.IOException("Generator will not write to existing file: " + target.getPath)
    writeTo(target){ pr =>
      pr("""package bench.test""")
      pr("""""")
      pr("""object Agreement {""")
      pr("""  def run() {""")
      pr("""    val wrong = new collection.mutable.ArrayBuffer[String]""")
      pr("""    def check[A](a1: A, a2: A, msg: String) = if (a1 != a2) wrong += msg""")
      pr( s"    val x = new bench.generate.Things(${sayArrayI(sizes)})" )
      pr("""  }""")
      pr("""}""")
    } match {
      case Left(t) => println("Did not successfully write file: " + target.getPath); throw t
      case _ =>
    }
  }
}
