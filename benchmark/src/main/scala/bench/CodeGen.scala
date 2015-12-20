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
  def agreement(target: java.io.File, sizes: Option[Array[Int]] = None) {
    if (target.exists) throw new java.io.IOException("Generator will not write to existing file: " + target.getPath)
    writeTo(target){ pr =>
      Seq("test").foreach(pr)
    } match {
      case Left(t) => println("Did not successfully write file: " + target.getPath); throw t
      case _ =>
    }
  }
}
