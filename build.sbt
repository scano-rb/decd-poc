name := """decd-poc"""
organization := "com.decd"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

lazy val circeVersion = "0.12.2"
lazy val playSlickVersion = "4.0.2"
lazy val postgresVersion  = "42.2.2"

libraryDependencies ++= Seq(
  guice,
  ws,
  // Json
  "com.dripower" %% "play-circe"           % "2812.0",
  "io.circe"     %% "circe-core"           % circeVersion,
  "io.circe"     %% "circe-generic"        % circeVersion,
  "io.circe"     %% "circe-parser"         % circeVersion,
  "io.circe"     %% "circe-generic-extras" % circeVersion,
  // Functional Programming
  "org.typelevel" %% "cats-core"   % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.2",
  // DB
  "com.typesafe.play" %% "play-slick"            % playSlickVersion,
  "org.postgresql"    % "postgresql"  % postgresVersion,
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)
