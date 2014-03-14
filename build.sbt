scalaVersion := "2.11.0-RC1"

sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "runtime" / "jfunc" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("JFunc", CodeGen.factory)) ++ (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++ (1 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n)))
}

sourceGenerators in Test <+= sourceManaged in Test map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "runtime" / "jfunc" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("TestApi", CodeGen.testApi))
}
