package bench.codegen

import scala.util._
import scala.util.control.NonFatal

object Generator {
  val annotated = "arr ish lst* ils* que* stm* trs* vec arb ars ast* mhs lhs* prq* muq* wra jix jln".split(' ')
  val names = annotated.map(_.takeWhile(_.isLetter))
  val nojnames = names.filterNot(_ startsWith "j")

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
    val q = "\""
    if (target.exists) throw new java.io.IOException("Generator will not write to existing file: " + target.getPath)
    writeTo(target){ pr =>
      pr("""package bench.test""")
      pr("""""")
      pr("""import bench.generate._, bench.operate._, bench.generate.EnableIterators._""")
      pr("""import scala.compat.java8.StreamConverters._""")
      pr("""""")
      pr("""object Agreement {""")
      pr("""  def run() {""")
      pr("""    val wrong = new collection.mutable.ArrayBuffer[String]""")
      pr("""    def check[A](a1: A, a2: => A, msg: String) {""")
      pr("""      var t = System.nanoTime""")
      pr("""      if (!CloseEnough(a1, { val ans = a2; t = System.nanoTime - t; ans})) wrong += msg""")
      pr("""      if (t > 2000000000) wrong += "Slow " + msg""")
      pr("""    }""")
      pr( s"    val x = new bench.generate.Things(${sayArrayI(sizes)})" )
      pr("""    for (i <- 0 until x.N) {""")
      pr("""      val si = OnInt.sum(x.arr.cI(i))""")
      nojnames.tail.foreach{ n =>
      pr( s"      check(si, OnInt.sum(x.$n.cI(i)), ${q}cI sum $n ${q}+i.toString)")
      }
      nojnames.foreach{ n => 
      pr( s"      check(si, OnInt.sum(x.$n.iI(i)), ${q}iI sum $n ${q}+i.toString)")
      }
      annotated.foreach{ m =>
      val n = m.takeWhile(_.isLetter)
      val c = if (m contains "*") "ssI" else "spI"
      pr( s"      check(si, OnInt.sum(x.$n.$c(i)), ${q}$c sum $n ${q}+i.toString)")
      }
      pr( s"    }")
      pr("""    wrong.foreach(println)""")
      pr("""    if (wrong.nonEmpty) sys.exit(1) """)
      pr("""  }""")
      pr("""}""")
    } match {
      case Left(t) => println("Did not successfully write file: " + target.getPath); throw t
      case _ =>
    }
  }
}
