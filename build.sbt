name := """decd-poc"""
organization := "com.decd"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)
