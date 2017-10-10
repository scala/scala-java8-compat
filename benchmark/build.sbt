enablePlugins(JmhPlugin)

val generateJmh = TaskKey[Unit]("generateJmh", "Generates JMH benchmark sources.")
val parseJmh = TaskKey[Unit]("parseJmh", "Parses JMH benchmark logs in results/jmhbench.log.")

lazy val root = (project in file(".")).settings(
  name := "java8-compat-bench",
  scalaVersion := "2.11.11",
  crossScalaVersions := List("2.11.11" /* TODO, "2.12.0-M4"*/),
  organization := "org.scala-lang.modules",
  version := "0.6.0-SNAPSHOT",
  unmanagedJars in Compile ++= Seq(baseDirectory.value / "../target/scala-2.11/scala-java8-compat_2.11-0.9.0-SNAPSHOT.jar"),
  // This would be nicer but sbt-jmh doesn't like it:
  //unmanagedClasspath in Compile += Attributed.blank(baseDirectory.value / "../target/scala-2.11/classes"),
  generateJmh := (runMain in Compile).toTask(" bench.codegen.GenJmhBench").value,
  parseJmh := (runMain in Compile).toTask(" bench.examine.ParseJmhLog").value
)
