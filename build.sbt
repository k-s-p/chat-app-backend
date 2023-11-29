name := """chat-app"""
organization := "com.jp.bupi"
scalaVersion := "2.13.12"
version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(ScalikejdbcPlugin)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
      "mysql" % "mysql-connector-java" % "8.0.33", // your jdbc driver here
      "org.scalikejdbc" %% "scalikejdbc" % "4.1.0",
      "org.scalikejdbc" %% "scalikejdbc-config" % "4.1.0",
      "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-4.0",
      "joda-time" % "joda-time" % "2.12.5",
      "org.joda" % "joda-convert" % "2.2.2"
    ),
    dependencyOverrides ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0",
      "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
    )
  )

