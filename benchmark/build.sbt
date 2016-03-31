enablePlugins(JmhPlugin)

lazy val root = (project in file(".")).settings(
  name := "java8-compat-bench",
  scalaVersion := "2.11.7",
  crossScalaVersions := List("2.11.7" /* TODO, "2.12.0-M3"*/),
  organization := "org.scala-lang.modules",
  version := "0.6.0-SNAPSHOT",
  unmanagedJars in Compile ++= Seq(baseDirectory.value / "../target/scala-2.11/scala-java8-compat_2.11-0.6.0-SNAPSHOT.jar")
)
