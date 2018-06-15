import ScalaModulePlugin._

crossScalaVersions in ThisBuild := List("2.13.0-M4")

val disableDocs = sys.props("nodocs") == "true"

//lazy val JavaDoc = config("genjavadoc") extend Compile

def jwrite(dir: java.io.File, pck: String = "scala/compat/java8")(name: String, content: String) = {
  val f = dir / pck / s"${name}.java"
  IO.write(f, content)
  f
}

def osgiExport(scalaVersion: String, version: String) = {
  (CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 11)) => Seq(s"scala.runtime.java8.*;version=${version}")
    case _ => Nil
  }) ++ Seq(s"scala.compat.java8.*;version=${version}")
}

lazy val commonSettings = Seq(
  organization := "org.scala-lang.modules",
  version := "0.9.1-SNAPSHOT",
  scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")
)

lazy val fnGen = (project in file("fnGen")).
  settings(commonSettings: _*).
  settings(
    fork in run := true,  // Needed if you run this project directly
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
  )

lazy val root = (project in file(".")).
  settings(scalaModuleSettings: _*).
  settings(commonSettings: _*).
  settings(
    name := "scala-java8-compat"
  ).
  settings(
    fork := true, // This must be set so that runner task is forked when it runs fnGen and the compiler gets a proper classpath

    OsgiKeys.exportPackage := osgiExport(scalaVersion.value, version.value),

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
      runner.value.run(runTarget, classPath.files, args, streams.value.log)
        .foreach(sys.error)
      (out ** "*.scala").get
    }.taskValue,

    sourceGenerators in Compile += Def.task {
      val dir = (sourceManaged in Compile).value
      val write = jwrite(dir) _
      if(scalaVersion.value.startsWith("2.11.")) {
        Seq(write("JFunction", CodeGen.factory)) ++
          (0 to 22).map(n => write("JFunction" + n, CodeGen.fN(n))) ++
          (0 to 22).map(n => write("JProcedure" + n, CodeGen.pN(n))) ++
          CodeGen.specializedF0.map(write.tupled) ++
          CodeGen.specializedF1.map(write.tupled) ++
          CodeGen.specializedF2.map(write.tupled) ++
          CodeGen.packageDummy.map((jwrite(dir, "java/runtime/java8") _).tupled)
      } else CodeGen.create212.map(write.tupled)
    }.taskValue,

    sourceGenerators in Test += Def.task {
      Seq(jwrite((sourceManaged in Test).value)("TestApi", CodeGen.testApi))
    }.taskValue,

    initialize := {
      // Run previously configured inialization...
      val _ = initialize.value
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
    sources in (Compile,doc) := Seq.empty,
    publishArtifact in (Compile, packageDoc) := false).
/*  settings(
    (inConfig(JavaDoc)(Defaults.configSettings) ++ (if (disableDocs) Nil else Seq(
      packageDoc in Compile := (packageDoc in JavaDoc).value,
      sources in JavaDoc := {
        val allJavaSources =
          (target.value / "java" ** "*.java").get ++
            (sources in Compile).value.filter(_.getName.endsWith(".java"))
        allJavaSources.filterNot(_.getName.contains("FuturesConvertersImpl.java")) // this file triggers bugs in genjavadoc
      },
      javacOptions in JavaDoc := Seq(),
      artifactName in packageDoc in JavaDoc := ((sv, mod, art) => "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar"),
      libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" % "genjavadoc-plugin" % "0.11" cross CrossVersion.full),
      scalacOptions in Compile += "-P:genjavadoc:out=" + (target.value / "java")
    ))): _*
  ).*/
  settings(
    initialCommands :=
    """|import scala.concurrent._
       |import ExecutionContext.Implicits.global
       |import java.util.concurrent.{CompletionStage,CompletableFuture}
       |import scala.compat.java8.FutureConverters._
       |""".stripMargin
  )
