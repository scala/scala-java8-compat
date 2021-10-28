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

import scala.collection.mutable

object WrapFnGen {
  /** all 43 interfaces in java.util.function package */
  private lazy val allJfn = Seq(
    "BiConsumer[T, U]: accept(T, U): Unit",
    "BiFunction[T, U, R]: apply(T, U): R",
    "BiPredicate[T, U]: test(T, U): Boolean",
    "BinaryOperator[T]: apply(T, T): T",
    "BooleanSupplier: getAsBoolean: Boolean",
    "Consumer[T]: accept(T): Unit",
    "DoubleBinaryOperator: applyAsDouble(Double, Double): Double",
    "DoubleConsumer: accept(Double): Unit",
    "DoubleFunction[R]: apply(Double): R",
    "DoublePredicate: test(Double): Boolean",
    "DoubleSupplier: getAsDouble: Double",
    "DoubleToIntFunction: applyAsInt(Double): Int",
    "DoubleToLongFunction: applyAsLong(Double): Long",
    "DoubleUnaryOperator: applyAsDouble(Double): Double",
    "Function[T, R]: apply(T): R",
    "IntBinaryOperator: applyAsInt(Int, Int): Int",
    "IntConsumer: accept(Int): Unit",
    "IntFunction[R]: apply(Int): R",
    "IntPredicate: test(Int): Boolean",
    "IntSupplier: getAsInt: Int",
    "IntToDoubleFunction: applyAsDouble(Int): Double",
    "IntToLongFunction: applyAsLong(Int): Long",
    "IntUnaryOperator: applyAsInt(Int): Int",
    "LongBinaryOperator: applyAsLong(Long, Long): Long",
    "LongConsumer: accept(Long): Unit",
    "LongFunction[R]: apply(Long): R",
    "LongPredicate: test(Long): Boolean",
    "LongSupplier: getAsLong: Long",
    "LongToDoubleFunction: applyAsDouble(Long): Double",
    "LongToIntFunction: applyAsInt(Long): Int",
    "LongUnaryOperator: applyAsLong(Long): Long",
    "ObjDoubleConsumer[T]: accept(T, Double): Unit",
    "ObjIntConsumer[T]: accept(T, Int): Unit",
    "ObjLongConsumer[T]: accept(T, Long): Unit",
    "Predicate[T]: test(T): Boolean",
    "Supplier[T]: get: T",
    "ToDoubleBiFunction[T, U]: applyAsDouble(T, U): Double",
    "ToDoubleFunction[T]: applyAsDouble(T): Double",
    "ToIntBiFunction[T, U]: applyAsInt(T, U): Int",
    "ToIntFunction[T]: applyAsInt(T): Int",
    "ToLongBiFunction[T, U]: applyAsLong(T, U): Long",
    "ToLongFunction[T]: applyAsLong(T): Long",
    "UnaryOperator[T]: apply(T): T",
  ).map(Jfn.apply)

  /** @param sig - ex: "BiConsumer[T,U]: accept(T,U): Unit"
    *            or "DoubleToIntFunction: applyAsInt(Double): Int" */
  case class Jfn(sig: String) {
    val Array(
    iface, // interface name included type args, ex: BiConsumer[T,U] | DoubleToIntFunction
    _method, // Temp val, ex: accept(T,U) | applyAsInt(Double)
    rType // java function return type, ex: Unit | Int
    ) = sig.split(':').map(_.trim)

    // interface name and java interface's type args,
    // ex: ("BiConsumer", "[T,U]") | ("DoubleToIntFunction", "")
    val (ifaceName, jtargs) = iface.span(_ != '[')

    // java method name and temp val, ex: "accept" -> "(T,U)" | "applyAsInt" -> "(Double)"
    val (jmethod, _targs) = _method.span(_ != '(')

    // java method's type args, ex: Seq("T", "U") | Seq("Double")
    val pTypes: Seq[String] = _targs.unwrapMe

    // arguments names, ex: Seq("x1", "x2")
    val args: Seq[String] = pTypes.indices.map { i => "x" + (i+1) }
    // ex: "(x1: T, x2: U)" | "(x1: Double)"
    val argsDecl: String = args.zip(pTypes).map {
      // Don't really need this case. Only here so the generated code is
      // exactly == the code gen by the old method using scala-compiler + scala-reflect
      case (p, t @ ("Double"|"Long"|"Int")) => s"$p: scala.$t"
      case (p, t) => s"$p: $t"
    }.mkString("(", ", ", ")")
    // ex: "(x1, x2)"
    val argsCall: String = args.mkString("(", ", ", ")")

    // arity of scala.Function
    val arity: Int = args.length

    // ex: "java.util.function.BiConsumer[T,U]" | "java.util.function.DoubleToIntFunction"
    val javaFn = s"java.util.function.$iface"

    // ex: "scala.Function2[T, U, Unit]" | "scala.Function1[Double, Int]"
    val scalaFn = s"scala.Function$arity[${(pTypes :+ rType).mkString(", ")}]"

    def fromJavaCls: String =
      s"""class FromJava$iface(jf: $javaFn) extends $scalaFn {
         |  def apply$argsDecl = jf.$jmethod$argsCall
         |}""".stripMargin

    val richAsFnClsName = s"Rich${ifaceName}AsFunction$arity$jtargs"
    def richAsFnCls: String =
      s"""class $richAsFnClsName(private val underlying: $javaFn) extends AnyVal {
         |  @inline def asScala: $scalaFn = new FromJava$iface(underlying)
         |}""".stripMargin

    def asJavaCls: String =
      s"""class AsJava$iface(sf: $scalaFn) extends $javaFn {
         |  def $jmethod$argsDecl = sf.apply$argsCall
         |}""".stripMargin

    val richFnAsClsName = s"RichFunction${arity}As$iface"
    def richFnAsCls: String =
      s"""class $richFnAsClsName(private val underlying: $scalaFn) extends AnyVal {
         |  @inline def asJava: $javaFn = new AsJava$iface(underlying)
         |}""".stripMargin

    def converterImpls: String =
      s"""$fromJavaCls\n
         |$richAsFnCls\n
         |$asJavaCls\n
         |$richFnAsCls\n
         |""".stripMargin

    /** @return "implicit def enrichAsJavaXX.." code */
    def enrichAsJavaDef: String = {
      // This is especially tricky because functions are contravariant in their arguments
      // Need to prevent e.g. Any => String from "downcasting" itself to Int => String; we want the more exact conversion
      // Instead of foo[A](f: (Int, A) => Long): Fuu[A] = new Foo[A](f)
      //    we want foo[X, A](f: (X, A) => Long)(implicit evX: Int =:= X): Fuu[A] = new Foo[A](f.asInstanceOf[(Int, A) => Long])
      // Instead of bar[A](f: A => A): Brr[A] = new Foo[A](f)
      //    we want bar[A, B](f: A => B)(implicit evB: A =:= B): Brr[A] = new Foo[A](f.asInstanceOf[A => B])

      val finalTypes = Set("Double", "Long", "Int", "Boolean", "Unit")
      val An = "A(\\d+)".r
      val numberedA = mutable.Set.empty[Int]
      val evidences = mutable.ArrayBuffer.empty[(String, String)] // ex: "A0" -> "Double"
      numberedA ++= pTypes.collect{ case An(digits) if (digits.length < 10) => digits.toInt }
      val scalafnTnames = (pTypes :+ rType).zipWithIndex.map {
        case (pt, i) if i < pTypes.length && finalTypes(pt) || !finalTypes(pt) && pTypes.take(i).contains(pt) =>
          val j = Iterator.from(i).dropWhile(numberedA).next()
          val genericName = s"A$j"
          numberedA += j
          evidences += (genericName -> pt)
          genericName
        case (pt, _) => pt
      }
      val scalafnTdefs = scalafnTnames.dropRight(if (finalTypes(rType)) 1 else 0).wrapMe()
      val scalaFnGeneric = s"scala.Function${scalafnTnames.length - 1}[${scalafnTnames.mkString(", ")}]"
      val evs = evidences
        .map { case (generic, specific) => s"ev$generic: =:=[$generic, $specific]" }
        .wrapMe("(implicit ", ")")
      val sf = if (evs.isEmpty) "sf" else s"sf.asInstanceOf[$scalaFn]"
      s"@inline implicit def enrichAsJava$ifaceName$scalafnTdefs(sf: $scalaFnGeneric)$evs: $richFnAsClsName = new $richFnAsClsName($sf)"
    }

    def asScalaFromDef = s"@inline def asScalaFrom$iface(jf: $javaFn): $scalaFn = new FromJava$iface(jf)"

    def asJavaDef = s"@inline def asJava$iface(sf: $scalaFn): $javaFn = new AsJava$iface(sf)"

    def enrichAsScalaDef = s"@inline implicit def enrichAsScalaFrom$iface(jf: $javaFn): $richAsFnClsName = new $richAsFnClsName(jf)"
  }

  def converters: String = {
    val groups = allJfn
      .map(jfn => jfn.jtargs.unwrapMe.length -> jfn.enrichAsJavaDef)
      .groupBy(_._1)
      .toSeq
      .sortBy(_._1)
      .reverse
    val maxPriority = groups.head._1
    groups.map { case (priority, seq) =>
      val parent =
        if (priority == maxPriority) ""
        else s" extends Priority${priority + 1}FunctionConverters"
      val me =
        if (priority == 0) "package object FunctionConverters"
        else s"trait Priority${priority}FunctionConverters"

      val enrichAsJava = seq.map(_._2)
      val (asXx, enrichAsScala) =
        if (priority != 0) Nil -> Nil
        else allJfn.map { jfn => jfn.asScalaFromDef + "\n\n" + jfn.asJavaDef } ->
          allJfn.map(_.enrichAsScalaDef)

      s"""$me$parent {
         |  import functionConverterImpls._
         |${asXx.mkString("\n\n\n").indentMe}
         |${enrichAsJava.mkString("\n\n").indentMe}
         |${enrichAsScala.mkString("\n\n").indentMe}
         |}""".stripMargin
    }.mkString("\n\n\n")
  }

  def code: String =
    s"""
       |/*
       | * Copyright EPFL and Lightbend, Inc.
       | * This file auto-generated by WrapFnGen.scala.  Do not modify directly.
       | */
       |
       |package scala.compat.java8
       |
       |import language.implicitConversions
       |
       |package functionConverterImpls {
       |${allJfn.map(_.converterImpls).mkString("\n").indentMe}
       |}
       |\n
       |$converters
       |""".stripMargin

  implicit class StringExt(private val s: String) extends AnyVal {
    def indentMe: String = s.linesIterator.map("  " + _).mkString("\n")
    def unwrapMe: Seq[String] = s match {
      case "" => Nil
      case _ => s
        .substring(1, s.length - 1) // drop "(" and ")" or "[" and "]"
        .split(',').map(_.trim).toSeq
    }
  }
  
  implicit class WrapMe(private val s: Seq[String]) extends AnyVal {
    def wrapMe(start: String = "[", end: String = "]"): String = s match {
      case Nil => ""
      case _ => s.mkString(start, ", ", end)
    }
  }
}
