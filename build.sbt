import com.typesafe.tools.mima.plugin.{MimaPlugin, MimaKeys}

scalaModuleSettings

scalaVersion := "2.10.4"

snapshotScalaBinaryVersion := "2.10.4"

// TODO this project can be cross build against 2.11 and 2.10, express that here.

organization := "org.scala-lang.modules"

name := "scala-java8-compat"

// important!! must come here (why?)
scalaModuleOsgiSettings

OsgiKeys.exportPackage := Seq(s"scala.compat.java8.*;version=${version.value}")

libraryDependencies += "junit" % "junit" % "4.11" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"

MimaPlugin.mimaDefaultSettings

MimaKeys.previousArtifact := None

// run mima during tests
test in Test := {
  MimaKeys.reportBinaryIssues.value
  (test in Test).value
}

sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "compat" / "java8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("JFunction", CodeGen.factory)) ++ (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++ (1 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n)))
}

sourceGenerators in Test <+= sourceManaged in Test map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "java" / "scala" / "compat" / "java8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("TestApi", CodeGen.testApi))
}

initialize := {
  val _ = initialize.value
  val specVersion = sys.props("java.specification.version")
  if (Set("1.5", "1.6", "1.7") contains specVersion)
    sys.error("Java 8 or higher is required for this project.")
}
