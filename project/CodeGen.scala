/*
 * Copyright (C) 2012-2014 Typesafe Inc. <http://www.typesafe.com>
 */

sealed abstract class Type(val code: Char, val prim: String, val ref: String)
object Type {
  case object Boolean extends Type('Z', "boolean", "Boolean")
  case object Byte extends Type('B', "byte", "Byte")
  case object Char extends Type('C', "char", "Character")
  case object Short extends Type('S', "short", "Short")
  case object Int extends Type('I', "int", "Integer")
  case object Float extends Type('F', "float", "Float")
  case object Double extends Type('D', "double", "Double")
  case object Long extends Type('J', "long", "Long")
  case object Void extends Type('V', "void", "Void")
  case object Object extends Type('L', "Object", "Object")
}

object CodeGen {
  private val initName = "$init$"
  private val function1ImplClass = "scala.Function1$class"
  private val copyright = """
/*
 * Copyright (C) 2012-2014 Typesafe Inc. <http://www.typesafe.com>
 */""".trim

  private def f0Header = s"""
$copyright

package scala.runtime;

@FunctionalInterface
public interface F0<R> extends scala.Function0<R> {
    default void $initName() {
    };
"""
  private def f1Header = s"""
$copyright

package scala.runtime;

@FunctionalInterface
public interface F1<T1, R> extends scala.Function1<T1, R> {
    default void $initName() {
    };

    @Override
    default <A> scala.Function1<T1, A> andThen(scala.Function1<R, A> g) {
        return $function1ImplClass.andThen(this, g);
    }

    @Override
    default <A> scala.Function1<A, R> compose(scala.Function1<A, T1> g) {
        return $function1ImplClass.compose(this, g);
    }

"""

  private def f2Header = fNHeader(2)

  def fNHeader(n: Int) = {
    require(n > 1, n)
    val tparams = (1 to n).map("T" + _).mkString(", ")
    val curriedReturn = (1 to n).reverse.foldLeft("R")((x, y) => s"scala.Function1<T$y, $x>")
    val tupledReturn = s"scala.Function1<scala.Tuple${n}<$tparams>, R>"
    val implClass = s"scala.Function$n" + "$class"
s"""
$copyright

package scala.runtime;

@FunctionalInterface
public interface F$n<$tparams, R> extends scala.Function$n<$tparams, R> {
    default void $initName() {
    };

    default $curriedReturn curried() {
      return $implClass.curried(this);
    }

    default $tupledReturn tupled() {
      return $implClass.tupled(this);
    }

"""
}

  private def apply0MethodSpec(r: Type): String = {
    val name = "apply$mc" + s"${r.code}" + "$sp"
    val applyCall = s"apply();"
    def body = if (r == Type.Void) applyCall else s"return (${r.ref}) $applyCall"

s"""
default ${r.prim} $name() {
    $body
}
""".trim
  }

  private def apply0SpecMethods = {
    val rs = List(Type.Void, Type.Byte, Type.Short, Type.Int, Type.Long, Type.Char, Type.Float, Type.Double, Type.Boolean)
    val methods = for (r <- rs) yield apply0MethodSpec(r)
    methods.map(indent).mkString("\n\n")
  }

  private def apply1MethodSpec(t1: Type, r: Type): String = {
    val name = "apply$mc" + s"${r.code}${t1.code}" + "$sp"
    val applyCall = s"apply((T1) ((${t1.ref}) v1));"
    def body = if (r == Type.Void) applyCall else s"return (${r.ref}) $applyCall"

s"""
default ${r.prim} $name(${t1.prim} v1) {
    $body
}
""".trim
  }

  private def apply1SpecMethods = {
    val ts = List(Type.Int, Type.Long, Type.Float, Type.Double)
    val rs = List(Type.Void, Type.Boolean, Type.Int, Type.Float, Type.Long, Type.Double)
    val methods = for (t1 <- ts; r <- rs) yield apply1MethodSpec(t1, r)
    methods.map(indent).mkString("\n\n")
  }

  private def apply2MethodSpec(t1: Type, t2: Type, r: Type): String = {
    val name = "apply$mc" + s"${r.code}${t1.code}${t2.code}" + "$sp"
    val applyCall = s"apply((T1) ((${t1.ref}) v1), (T2) ((${t2.ref}) v2));"
    def body = if (r == Type.Void) applyCall else s"return (${r.ref}) $applyCall"

s"""
default ${r.prim} $name(${t1.prim} v1, ${t2.prim} v2) {
    $body
}
""".trim
  }

  private def apply2SpecMethods = {
    val ts = List(Type.Int, Type.Long, Type.Double)
    val rs = List(Type.Void, Type.Boolean, Type.Int, Type.Float, Type.Long, Type.Double)
    val methods = for (t1 <- ts; t2 <- ts; r <- rs) yield apply2MethodSpec(t1, t2, r)
    methods.map(indent).mkString("\n\n")
  }

  def f0 = f0Header + apply0SpecMethods + "}\n"

  def f1 = f1Header + apply1SpecMethods + "}\n"

  def f2 = f2Header + apply2SpecMethods + "}\n"

  def fN(arity: Int) = arity match {
    case 0 => f0
    case 1 => f1
    case 2 => f2
    case x => fNHeader(arity) + "\n}\n"
  }

  def pN(arity: Int): String = {
    def csv(f: Int => String): String = 
      (1 to arity).map(f).mkString(", ")
    val tparams = (1 to arity).map("T" + _).mkString(", ")
    val vparams = (1 to arity).map(n => s"T$n t$n").mkString(", ")
    val vparamRefs = (1 to arity).map(n => s"t$n").mkString(", ")
    val parent = "F" + arity
s"""
$copyright

package scala.runtime;

import scala.runtime.BoxedUnit;

@FunctionalInterface
public interface P${arity}<${tparams}> extends ${parent}<$tparams, BoxedUnit> {
    default void $initName() {
    }

    void applyVoid($vparams);

    default BoxedUnit apply($vparams) {
        applyVoid($vparamRefs);
        return BoxedUnit.UNIT;
    }
}
"""
}

  def factory: String = {
    def factory0(n: Int) = {
      val tparams = (1 to n).map("T" + _).mkString(", ")
s"""
public static <$tparams, R> scala.Function$n<$tparams, R> func(F$n<$tparams, R> f) { return f; }
public static <$tparams> scala.Function$n<$tparams, BoxedUnit> proc(P$n<$tparams> p) { return p; }
""".trim
    }
    val ms = (1 to 22).map(factory0).mkString("\n")

s"""
$copyright

package scala.runtime;

import scala.runtime.BoxedUnit;

public final class F {
    private F() {}
    public static <R> scala.Function0<R> f0(F0<R> f) { return f; }
${indent(ms)}
}

"""
  }

  def accept(n: Int): String = {
    val targs = (1 to n).map(_ => "String").mkString(", ")
    val vargs = (1 to n).map("\"" + _ + "\"").mkString(", ")
s"""
static <T> T acceptFunction$n(scala.Function$n<$targs, T> f) {
    return f.apply($vargs);
}
static void acceptFunction${n}Unit(scala.Function$n<$targs, scala.runtime.BoxedUnit> f) {
    f.apply($vargs);
}
"""
  }

  def testApi: String = {
s"""
$copyright

package scala.runtime.test;

final class TestAPI {
${(1 to 22).map(accept).map(indent).mkString("\n\n")}
}
"""
  }

  def indent(s: String) = s.linesIterator.map("    " + _).mkString("\n")
}
