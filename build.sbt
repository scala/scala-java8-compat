import com.typesafe.tools.mima.plugin.{MimaPlugin, MimaKeys}

scalaModuleSettings

scalaVersion := "2.11.5"

snapshotScalaBinaryVersion := "2.11.5"

organization := "org.scala-lang.modules"

name := "scala-java8-compat"

version := "0.5.0-SNAPSHOT"

// important!! must come here (why?)
scalaModuleOsgiSettings

OsgiKeys.exportPackage := Seq(s"scala.compat.java8.*;version=${version.value}")

OsgiKeys.privatePackage := List("scala.concurrent.java8.*")

libraryDependencies += "junit" % "junit" % "4.11" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"

MimaPlugin.mimaDefaultSettings

MimaKeys.previousArtifact := None

// run mima during tests
test in Test := {
  MimaKeys.reportBinaryIssues.value
  (test in Test).value
}

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")

sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "scala" / "compat" / "java8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  (
    Seq(write("JFunction", CodeGen.factory)) ++
    (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++
    (0 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n))) ++
    CodeGen.specializedF0.map((write _).tupled) ++
    CodeGen.specializedF1.map((write _).tupled) ++
    CodeGen.specializedF2.map((write _).tupled)
  )
}

sourceGenerators in Test <+= sourceManaged in Test map { dir =>
  def write(name: String, content: String) = {
    val f = dir / "scala" / "compat" / "java8" / s"${name}.java"
    IO.write(f, content)
    f
  }
  Seq(write("TestApi", CodeGen.testApi))
}

initialize := {
  // Run previously configured inialization...
  initialize.value
  // ... and then check the Java version.
  val specVersion = sys.props("java.specification.version")
  if (Set("1.5", "1.6", "1.7") contains specVersion)
    sys.error("Java 8 or higher is required for this project.")
}

val disableDocs = sys.props("nodocs") == "true"

publishArtifact in packageDoc := !disableDocs

lazy val JavaDoc = config("genjavadoc") extend Compile

sources in (Compile, doc) := {
  val orig = (sources in (Compile, doc)).value
  orig.filterNot(_.getName.endsWith(".java")) // raw types not cooked by scaladoc: https://issues.scala-lang.org/browse/SI-8449
}

inConfig(JavaDoc)(Defaults.configSettings) ++ (if (disableDocs) Nil else Seq(
  packageDoc in Compile <<= packageDoc in JavaDoc,
  sources in JavaDoc <<= (target, compile in Compile, sources in Compile) map {(t, c, s) =>
    val allJavaSources = (t / "java" ** "*.java").get ++ s.filter(_.getName.endsWith(".java"))
    allJavaSources.filterNot(_.getName.contains("FuturesConvertersImpl.java")) // this file triggers bugs in genjavadoc
  },
  javacOptions in JavaDoc := Seq(),
  artifactName in packageDoc in JavaDoc := ((sv, mod, art) => "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar"),
  libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" % "genjavadoc-plugin" % "0.8" cross CrossVersion.full),
  scalacOptions in Compile <+= target map (t => "-P:genjavadoc:out=" + (t / "java"))
))

initialCommands :=
"""|import scala.concurrent._
   |import ExecutionContext.Implicits.global
   |import java.util.concurrent.{CompletionStage,CompletableFuture}
   |import scala.compat.java8.FutureConverter._
   |""".stripMargin

