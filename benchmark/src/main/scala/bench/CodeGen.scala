package bench.codegen

import scala.util._
import control.NonFatal

object Generator {
  val annotated = "arr ish lst* ils* que* stm* trs* vec arb ars ast* mhs lhs* prq* muq* wra jix jln".split(' ')
  val allops = Seq(("OnInt", "I", "sum/psum trig/ptrig fmc/pfmc mdtc"), ("OnString", "S", "nbr/pnbr htrg/phtrg fmc/pfmc mdtc"))
  def parsefs(fs: String) = fs.split(' ').map(_.split('/') match { case Array(x) => (x, None); case Array(x,y) => (x, Some(y)) })

  val names = annotated.map(_.takeWhile(_.isLetter))
  val nojname = names.filterNot(_ startsWith "j").toSet
  val parname = annotated.filter(_.forall(_.isLetter)).toSet
  val sqnname = names.filterNot(parname).toSet union names.filterNot(nojname).toSet

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
      pr("""      if (!CloseEnough(a1, { val ans = a2; t = System.nanoTime - t; ans}))""")
      pr("""        wrong += msg""")
      pr("""      if (t > 2000000000) wrong += "Slow " + msg""")
      pr("""    }""")
      pr( s"    val m = (new bench.generate.Things(${sayArrayI(sizes)})).N;" )
      allops.foreach{ case (o, t, fs) =>
        names.foreach{ n =>
      pr( s"    {  // Scope for operations $o collection $n")
      pr( s"    val x = new bench.generate.Things(${sayArrayI(sizes)})" )
          parsefs(fs).foreach{ case (f, pf) =>
      pr("""    for (i <- 0 until m) {""")
      pr( s"      val z = $o.$f(x.arr.c$t(i))")
            if (nojname(n)) {
      pr( s"      check(z, $o.$f(x.$n.c$t(i)), ${q}c$t $f $n ${q}+i.toString)");
      pr( s"      check(z, $o.$f(x.$n.i$t(i)), ${q}i$t $f $n ${q}+i.toString)")
            }
            if (sqnname(n)) {
      pr( s"      check(z, $o.$f(x.$n.ss$t(i)), ${q}ss$t $f $n ${q}+i.toString)")
              if (nojname(n)) {  
      pr( s"      check(z, $o.$f(x.$n.zs$t(i)), ${q}zs$t $f $n ${q}+i.toString)")     
              }
            }
            if (parname(n)) {
      pr( s"      check(z, $o.$f(x.$n.sp$t(i)), ${q}sp$t $f $n ${q}+i.toString)")
             if (nojname(n)) {
      pr( s"      check(z, $o.$f(x.$n.zp$t(i)), ${q}zp$t $f $n ${q}+i.toString)")     
              }
            }
      pr( s"    }")
          }
      pr( s"    }  // End scope for operations $o collection $n")
        }
      }
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
