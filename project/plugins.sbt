scalacOptions ++= (sys.props("java.specification.version") match {
  // work around weird dbuild thing for JDK 12 community build; see
  // https://github.com/scala/community-builds/issues/862#issuecomment-464227096
  case "12" => Seq()
  case _ => Seq("-Xfatal-warnings")
})

addSbtPlugin("org.scala-lang.modules" % "sbt-scala-module" % "2.0.0")
