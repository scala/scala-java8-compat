val disableDocs =
  sys.props("nodocs") == "true" ||
    // on jdk 11 https://github.com/scala/scala-java8-compat/issues/160, seems to fail the build (not on 8)
    !sys.props("java.version").startsWith("1.")

lazy val JavaDoc = config("genjavadoc") extend Compile

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

ThisBuild / versionScheme := Some("early-semver")
ThisBuild / versionPolicyIntention := Compatibility.BinaryAndSourceCompatible

lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked"),

  Compile / unmanagedSourceDirectories ++= {
    (Compile / unmanagedSourceDirectories).value.flatMap { dir =>
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 11)) => Seq(file(dir.getPath ++ "-2.13-"), file(dir.getPath ++ "-2.11"))
        case Some((2, 12)) => Seq(file(dir.getPath ++ "-2.13-"))
        case _             => Seq(file(dir.getPath ++ "-2.13+"))
      }
    }
  },

  Test / unmanagedSourceDirectories ++= {
    (Test / unmanagedSourceDirectories).value.flatMap { dir =>
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 11)) => Seq(file(dir.getPath ++ "-2.13-"), file(dir.getPath ++ "-2.11"))
        case Some((2, 12)) => Seq(file(dir.getPath ++ "-2.13-"))
        case _             => Seq(file(dir.getPath ++ "-2.13+"))
      }
    }
  },
)

lazy val fnGen = (project in file("fnGen"))
  .settings(commonSettings)
  .settings(
    crossScalaVersions := Seq("2.12.13"),
    scalaVersion := crossScalaVersions.value.head,
    run / fork := true,  // Needed if you run this project directly
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
  )

lazy val scalaJava8Compat = (project in file("."))
  .settings(ScalaModulePlugin.scalaModuleSettings)
  .settings(ScalaModulePlugin.scalaModuleOsgiSettings)
  .settings(commonSettings)
  .settings(
    name := "scala-java8-compat"
  )
  .settings(
    fork := true, // This must be set so that runner task is forked when it runs fnGen and the compiler gets a proper classpath

    OsgiKeys.exportPackage := osgiExport(scalaVersion.value, version.value),

    OsgiKeys.privatePackage := List("scala.concurrent.java8.*"),

    libraryDependencies += "junit" % "junit" % "4.13.2" % "test",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.12.0" % "test",

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    scalaModuleMimaPreviousVersion := {
      // pending resolution of https://github.com/scalacenter/sbt-version-policy/issues/62
      if (isDotty.value) None
      else Some("0.9.1")
    },

    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._, ProblemFilters._
      Seq(
        // bah
        exclude[IncompatibleSignatureProblem]("*"),
        // mysterious -- see scala/scala-java8-compat#211
        exclude[DirectMissingMethodProblem  ]("scala.compat.java8.Priority1FunctionConverters.enrichAsJavaIntFunction"),
        exclude[ReversedMissingMethodProblem]("scala.compat.java8.Priority1FunctionConverters.enrichAsJavaIntFunction"),
        exclude[DirectMissingMethodProblem  ]("scala.compat.java8.FunctionConverters.package.enrichAsJavaIntFunction" ),
        exclude[ReversedMissingMethodProblem]("scala.compat.java8.FunctionConverters.package.enrichAsJavaIntFunction" ),
      )
    },

    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a"),

    (Compile / sourceGenerators) += Def.task {
      val out = (Compile / sourceManaged).value
      if (!out.exists) IO.createDirectory(out)
      val canon = out.getCanonicalPath
      val args = (new File(canon, "FunctionConverters.scala")).toString :: Nil
      val runTarget = (fnGen / Compile / mainClass).value getOrElse "No main class defined for function conversion generator"
      val classPath = (fnGen / Compile / fullClasspath).value
      runner.value.run(runTarget, classPath.files, args, streams.value.log)
      (out ** "*.scala").get
    }.taskValue,

    Compile / sourceGenerators += Def.task {
      val dir = (Compile / sourceManaged).value
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

    Test / sourceGenerators += Def.task {
      Seq(jwrite((Test / sourceManaged).value)("TestApi", CodeGen.testApi))
    }.taskValue,

    initialize := {
      // Run previously configured inialization...
      val _ = initialize.value
      // ... and then check the Java version.
      val specVersion = sys.props("java.specification.version")
      if (Set("1.5", "1.6", "1.7") contains specVersion)
        sys.error("Java 8 or higher is required for this project.")
    },

    packageDoc / publishArtifact := !disableDocs
  )
  .settings(
    inConfig(JavaDoc)(Defaults.configSettings) ++ {
      if (disableDocs) Nil
      else Seq(
        Compile / packageDoc := (JavaDoc / packageDoc).value,
        JavaDoc / sources := {
          val allJavaSources =
            (target.value / "java" ** "*.java").get ++
              (Compile / sources).value.filter(_.getName.endsWith(".java"))
          allJavaSources.filterNot(_.getName.contains("FuturesConvertersImpl.java")) // this file triggers bugs in genjavadoc
        },
        JavaDoc / javacOptions := Seq("-Xdoclint:none"),
        JavaDoc / packageDoc / artifactName := ((sv, mod, art) => "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar"),
        libraryDependencies ++= (
          if (isDotty.value) Seq()
          else Seq(compilerPlugin("com.typesafe.genjavadoc" % "genjavadoc-plugin" % "0.16" cross CrossVersion.full))
        ),
        Compile / scalacOptions ++= (
          if (isDotty.value) Seq()
          else Seq(s"""-P:genjavadoc:out=${target.value / "java"}""")
        ),
      )
    }
  )
  .settings(
    initialCommands :=
    """|import scala.concurrent._
       |import ExecutionContext.Implicits.global
       |import java.util.concurrent.{CompletionStage,CompletableFuture}
       |import scala.compat.java8.FutureConverters._
       |""".stripMargin
  )
