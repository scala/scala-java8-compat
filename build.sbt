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

lazy val commonSettings = Seq(
  crossScalaVersions := Seq("2.13.11", "2.12.18", "2.11.12", "3.3.1"),
  scalaVersion := crossScalaVersions.value.head,
  // we could make this stricter again (BinaryAndSourceCompatible)
  // after our reference version was built on Scala 3.1.x
  versionPolicyIntention := Compatibility.BinaryCompatible,
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

lazy val scalaJava8Compat = (project in file("."))
  .settings(ScalaModulePlugin.scalaModuleSettings)
  .settings(ScalaModulePlugin.scalaModuleOsgiSettings)
  .settings(commonSettings)
  .settings(
    name := "scala-java8-compat",
    scalaModuleAutomaticModuleName := Some("scala.compat.java8"),
  )
  .settings(
    OsgiKeys.exportPackage := osgiExport(scalaVersion.value, version.value),

    OsgiKeys.privatePackage := List("scala.concurrent.java8.*"),

    libraryDependencies += "junit" % "junit" % "4.13.2" % "test",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.12.0" % "test",

    libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.3" % "test",

    // see https://github.com/scala/scala-java8-compat/issues/247
    versionPolicyPreviousVersions := versionPolicyPreviousVersions.value.flatMap {
      case VersionNumber(Seq(0, _*), _, _) => Nil
      case VersionNumber(Seq(1, 0, n, _*), _, _) if n <= 1 => Nil
      case v => Seq(v)
    },

    // shouldn't be needed anymore after our reference version is a version
    // built on Scala 3.1.x
    mimaBinaryIssueFilters := {
      import com.typesafe.tools.mima.core.ProblemFilters._
      import com.typesafe.tools.mima.core._
      Seq(
        exclude[IncompatibleSignatureProblem]("scala.compat.java8.*"),
        exclude[IncompatibleSignatureProblem]("scala.concurrent.java8.*"),
      ),
    },

    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a"),

    (Compile / sourceGenerators) += Def.task {
      val f = (Compile / sourceManaged).value / "FunctionConverters.scala"
      IO.write(f, WrapFnGen.code)
      Seq(f)
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
        libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((3, _)) => Seq()
          case _            => Seq(compilerPlugin("com.typesafe.genjavadoc" % "genjavadoc-plugin" % "0.18" cross CrossVersion.full))
        }),
        Compile / scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((3, _)) => Seq()
          case _            => Seq(s"""-P:genjavadoc:out=${target.value / "java"}""")
        }),
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
