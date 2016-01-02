package bench.codegen

import scala.util._
import control.NonFatal

object Generator {
  // Trailing * means that a collection is not expected to work efficiently in parallel
  // Trailing ! means that a collection is not expected to maintain original order
  val annotated = "arr ish! lst* ils*! que* stm* trs*! vec arb ars ast* mhs! lhs*! prq*! muq* wra jix jln".split(' ')

  // Parallel version if any appears after /
  // Trailing ! means that collection must maintain original order (i.e. don't use if collection is marked !)
  val allops = Seq(("OnInt", "I", "sum/psum trig/ptrig fmc/pfmc mdtc!"), ("OnString", "S", "nbr/pnbr htrg/phtrg fmc/pfmc mdtc!"))

  def parsefs(fs: String) = fs.split(' ').map(_.split('/') match {
    case Array(x) => (x.takeWhile(_.isLetter), None, x contains "!")
    case Array(x,y) => (x.takeWhile(_.isLetter), Some(y.takeWhile(_.isLetter)), (x+y) contains "!")
  })

  val names = annotated.map(_.takeWhile(_.isLetter))
  val nojname = names.filterNot(_ startsWith "j").toSet
  val parname = annotated.filterNot(_ contains "*").map(_.takeWhile(_.isLetter)).toSet
  val sqnname = names.filterNot(parname).toSet union names.filterNot(nojname).toSet
  val ordname = annotated.filterNot(_ contains "!").map(_.takeWhile(_.isLetter)).toSet

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
      pr(              """package bench.test""")
      pr(              """""")
      pr(              """import bench.generate._, bench.operate._, bench.generate.EnableIterators._""")
      pr(              """import scala.compat.java8.StreamConverters._""")
      pr(              """""")
      pr(              """object Agreement {""")
      pr(              """  def run() {""")
      pr(              """    val wrong = new collection.mutable.ArrayBuffer[String]""")
      pr(              """    def check[A](a1: A, a2: => A, msg: String) {""")
      pr(              """      var t = System.nanoTime""")
      pr(              """      if (!CloseEnough(a1, { val ans = a2; t = System.nanoTime - t; ans}))""")
      pr(              """        wrong += msg""")
      pr(              """      if (t > 2000000000) wrong += "Slow " + msg""")
      pr(              """    }""")
      pr(               s"    val m = (new bench.generate.Things(${sayArrayI(sizes)})).N;" )
      allops.foreach{ case (o, t, fs) =>
        names.foreach{ n =>
        pr(             s"    {  // Scope for operations $o collection $n")
        pr(             s"    val x = new bench.generate.Things(${sayArrayI(sizes)})" )
          parsefs(fs).foreach{ case (f, pf, ord) =>
            if (ordname(n) || !ord) {
              pr(      """    for (i <- 0 until m) {""")
              pr(       s"      val z = $o.$f(x.arr.c$t(i))")
              if (nojname(n)) {
                pr(     s"      check(z, $o.$f(x.$n.c$t(i)), ${q}c$t $f $n ${q}+i.toString)");
                pr(     s"      check(z, $o.$f(x.$n.i$t(i)), ${q}i$t $f $n ${q}+i.toString)")
              }
              if (sqnname(n)) {
                pr(     s"      check(z, $o.$f(x.$n.ss$t(i)), ${q}ss$t $f $n ${q}+i.toString)")
                if (nojname(n)) 
                  pr(   s"      check(z, $o.$f(x.$n.zs$t(i)), ${q}zs$t $f $n ${q}+i.toString)")     
              }
              if (parname(n) && pf.isDefined) {
                pr(     s"      check(z, $o.$f(x.$n.sp$t(i)), ${q}sp$t $f $n ${q}+i.toString)")
                if (nojname(n))
                  pr(   s"      check(z, $o.$f(x.$n.zp$t(i)), ${q}zp$t $f $n ${q}+i.toString)")     
              }
              pr(       s"    }")
            }
          }
          pr(           s"    }  // End scope for operations $o collection $n")
        }
      }
      pr(              """    wrong.foreach(println)""")
      pr(              """    if (wrong.nonEmpty) sys.exit(1) """)
      pr(              """  }""")
      pr(              """}""")
    } match {
      case Left(t) => println("Did not successfully write file: " + target.getPath); throw t
      case _ =>
    }
  }

  def quickBenchWithThyme(target: java.io.File, sizes: Option[Array[Int]]) {
    val q = "\""
    if (target.exists) throw new java.io.IOException("Generator will not write to existing file: " + target.getPath)
    writeTo(target){ pr =>
      pr(              """package bench.test""")
      pr(              """""")
      pr(              """import bench.generate._, bench.operate._, bench.generate.EnableIterators._""")
      pr(              """import scala.compat.java8.StreamConverters._""")
      pr(              """import ichi.bench.Thyme""")
      pr(              """""")
      pr(              """object Agreement {""")
      pr(              """  def run() {""")
      pr(              """    val th = Thyme.warmed()""")
      pr(               s"    val m = (new bench.generate.Things(${sayArrayI(sizes)})).N;" )
      pr(              """    def timings[A](x: bench.generate.Things, op: Int => A, name: String) {""")
      pr(              """      val ts = new collection.mutable.ArrayBuffer[(Double, Double, Double)]""")
      pr(              """      for (i <- 0 until m) {""")
      pr(              """        val b = Thyme.Benched.empty""")
      pr(              """        val a = th.bench(op(i))(b)""")
      pr(              """        if (a == null) ts += ((Double.NaN, Double.NaN, Double.NaN))""")
      pr(              """        else ts += ((b.runtime * 1e6, b.runtimeCI95._1 * 1e6, b.runtimeCI95._2 * 1e6)""")
      pr(              """      }""")
      pr(              """      val sb = new StringBuilder""")
      pr(              """      sb ++= name + $q: $q""")
      pr(              """      if (sb.length < 36) sb ++= $q $q * (36 - sb.length)""")
      pr(              """      ts.foreach{ case (c, lo, hi) =>""")
      pr(              """        sb ++= $q    $q""")
      pr(              """        sb ++= ${q}12.4f${q}.format(c)""")
      pr(              """        sb ++= ${q}12.4f${q}.format(lo)""")
      pr(              """        sb ++= ${q}12.4f${q}.format(hi)""")
      pr(              """      }""")
      pr(              """      println(sb.result)""")
      pr(              """    }""")
      allops.foreach{ case (o, t, fs) =>
        names.foreach{ n =>
        pr(             s"    {  // Scope for operations $o collection $n")
        pr(             s"    val x = new bench.generate.Things(${sayArrayI(sizes)})" )
          parsefs(fs).foreach{ case (f, pf, ord) =>
            if (ordname(n) || !ord) {
              if (nojname(n)) {
                pr(     s"      timings(x, i => $o.$f(x.$n.c$t(i)), ${q}c$t $f $n ${q}+i.toString)");
                pr(     s"      timings(x, i => $o.$f(x.$n.i$t(i)), ${q}i$t $f $n ${q}+i.toString)")
              }
              if (sqnname(n)) {
                pr(     s"      timings(x, i => $o.$f(x.$n.ss$t(i)), ${q}ss$t $f $n ${q}+i.toString)")
                if (nojname(n)) 
                  pr(   s"      timings(x, i => $o.$f(x.$n.zs$t(i)), ${q}zs$t $f $n ${q}+i.toString)")     
              }
              if (parname(n) && pf.isDefined) {
                pr(     s"      timings(x, i => $o.$f(x.$n.sp$t(i)), ${q}sp$t $f $n ${q}+i.toString)")
                if (nojname(n))
                  pr(   s"      timings(x, i => $o.$f(x.$n.zp$t(i)), ${q}zp$t $f $n ${q}+i.toString)")     
              }
              pr(       s"    }")
            }
          }
          pr(           s"    }  // End scope for operations $o collection $n")
        }
      }
      pr(              """  }""")
      pr(              """}""")
    } match {
      case Left(t) => println("Did not successfully write file: " + target.getPath); throw t
      case _ =>
    }
  }
}
