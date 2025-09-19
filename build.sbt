lazy val baseSettings = Seq(
  organization := "com.example",
  scalaVersion := "3.7.3",
  version := "0.1.0-SNAPSHOT",
)

lazy val predef = project.in(file("predef"))
  .settings(baseSettings)
  .settings(
    name := "scala3-Yimports-opaque-type-given-predef",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.13.0",
  )

lazy val usage = project.in(file("usage"))
  .settings(baseSettings)
  .settings(
    name := "scala3-Yimports-opaque-type-given-usage",
    scalacOptions += "-Yimports:java.lang,scala,scala.Predef,com.example.Predef"
  )
  .dependsOn(predef)
  .aggregate(predef)
