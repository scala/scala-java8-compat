package scala.compat.java8

/** A trait that indicates that the class is or can be converted to a Scala version by wrapping a Java class */
trait WrappedAsScala[S] { 
  /** Returns an appropriate Scala version */
  def asScala: S
}

/** A trait that indicates that the class is or can be converted to a Java version by wrapping a Scala class */
trait WrappedAsJava[J] {
  /** Returns an appropriate Java version */
  def asJava: J
}
