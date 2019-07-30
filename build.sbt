name := "proxy-scraper"

version := "0.1"

scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.5.0",
  "org.typelevel" %% "cats-effect" % "1.2.0",
  "io.circe" % "circe-core_2.12" % "0.12.0-M4",
  "io.circe" %% "circe-generic" % "0.12.0-M4",
  "org.http4s" %% "http4s-async-http-client" % "0.21.0-M2",
  "org.http4s" %% "http4s-dsl" % "0.21.0-M2",
  "org.http4s" %% "http4s-circe" % "0.21.0-M2",
  "org.slf4j" % "slf4j-api" % "1.7.26",
  "org.slf4j" % "slf4j-log4j12" % "1.7.26"
)
