val disableDocs = sys.props("nodocs") == "true"

lazy val JavaDoc = config("genjavadoc") extend Compile

def jwrite(dir: java.io.File)(name: String, content: String) = {
  val f = dir / "scala" / "compat" / "java8" / s"${name}.java"
  IO.write(f, content)
  f
}

lazy val commonSettings = Seq(
  scalaVersion := "2.11.7",
  crossScalaVersions := List("2.11.7" /* TODO, "2.12.0-M3"*/),
  organization := "org.scala-lang.modules",
  version := "0.6.0-SNAPSHOT",
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
)

lazy val fnGen = (project in file("fnGen")).
  settings(commonSettings: _*).
  settings(
    fork in run := true  // Needed if you run this project directly
  )

lazy val root = (project in file(".")).
  dependsOn(fnGen).
  settings(scalaModuleSettings: _*).
  settings(commonSettings: _*).
  settings(
    name := "scala-java8-compat"
  ).
  settings(
    // important!! must come here (why?)
    scalaModuleOsgiSettings: _*
  ).
  settings(
    fork := true, // This must be set so that runner task is forked when it runs fnGen and the compiler gets a proper classpath

    OsgiKeys.exportPackage := Seq(s"scala.compat.java8.*;version=${version.value}"),

    OsgiKeys.privatePackage := List("scala.concurrent.java8.*"),

    libraryDependencies += "junit" % "junit" % "4.11" % "test",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4" % "test",

    libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test",

    mimaPreviousVersion := None,

    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a"),

    (sourceGenerators in Compile) += Def.task {
      val out = (sourceManaged in Compile).value
      if (!out.exists) IO.createDirectory(out)
      val canon = out.getCanonicalPath
      val args = (new File(canon, "FunctionConverters.scala")).toString :: Nil
      val runTarget = (mainClass in Compile in fnGen).value getOrElse "No main class defined for function conversion generator"
      val classPath = (fullClasspath in Compile in fnGen).value
      toError(runner.value.run(runTarget, classPath.files, args, streams.value.log))
      (out ** "*.scala").get
    }.taskValue,

    sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
      val write = jwrite(dir) _
      Seq(write("JFunction", CodeGen.factory)) ++
      (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++
      (0 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n))) ++
      CodeGen.specializedF0.map(write.tupled) ++
      CodeGen.specializedF1.map(write.tupled) ++
      CodeGen.specializedF2.map(write.tupled)
    },

    sourceGenerators in Test <+= sourceManaged in Test map { dir =>
      Seq(jwrite(dir)("TestApi", CodeGen.testApi))
    },

    initialize := {
      // Run previously configured inialization...
      initialize.value
      // ... and then check the Java version.
      val specVersion = sys.props("java.specification.version")
      if (Set("1.5", "1.6", "1.7") contains specVersion)
        sys.error("Java 8 or higher is required for this project.")
    },

    publishArtifact in packageDoc := !disableDocs,

    sources in (Compile, doc) := {
      val orig = (sources in (Compile, doc)).value
      orig.filterNot(_.getName.endsWith(".java")) // raw types not cooked by scaladoc: https://issues.scala-lang.org/browse/SI-8449
    }
  ).
  settings(
    (inConfig(JavaDoc)(Defaults.configSettings) ++ (if (disableDocs) Nil else Seq(
      packageDoc in Compile <<= packageDoc in JavaDoc,
      sources in JavaDoc <<= (target, compile in Compile, sources in Compile) map {(t, c, s) =>
        val allJavaSources = (t / "java" ** "*.java").get ++ s.filter(_.getName.endsWith(".java"))
        allJavaSources.filterNot(_.getName.contains("FuturesConvertersImpl.java")) // this file triggers bugs in genjavadoc
      },
      javacOptions in JavaDoc := Seq(),
      artifactName in packageDoc in JavaDoc := ((sv, mod, art) => "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar"),
      libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" % "genjavadoc-plugin" % "0.9" cross CrossVersion.full),
      scalacOptions in Compile += "-P:genjavadoc:out=" + (target.value / "java")
    ))): _*
  ).
  settings(
    initialCommands :=
    """|import scala.concurrent._
       |import ExecutionContext.Implicits.global
       |import java.util.concurrent.{CompletionStage,CompletableFuture}
       |import scala.compat.java8.FutureConverter._
       |""".stripMargin
  )
