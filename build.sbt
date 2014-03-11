scalaVersion := "2.11.0-RC1"

javaHome := Some(file("/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home"))

sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "scala" / "runtime" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("F", CodeGen.factory)) ++ (0 to 22).map(n => write("F" + n, CodeGen.fN(n))) ++ (1 to 22).map(n => write("P" + n, CodeGen.pN(n)))
}
