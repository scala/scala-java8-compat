scalaVersion := "2.10.3"

organization := "org.scala-lang.modules"

name := "scala-compat-jdk8"

sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "compat" / "jdk8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("JFunction", CodeGen.factory)) ++ (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++ (1 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n)))
}

sourceGenerators in Test <+= sourceManaged in Test map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "compat" / "jdk8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("TestApi", CodeGen.testApi))
}
