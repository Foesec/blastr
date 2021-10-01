import sbt._

object Dependencies {

  val AkkaVersion         = "2.6.16"
  val AkkaKafkaVersion    = "2.1.1"
  val AkkaHttpVersion     = "10.2.6"
  val AkkaHttpJsonVersion = "1.38.2"
  val JacksonVersion      = "2.11.4"
  val catsVersion         = "2.6.1"
  val circeVersion        = "0.14.1"

  val logbackVersion      = "1.2.6"
  val scalaLoggingVersion = "3.9.4"

  val prod = Seq(
    "com.typesafe.akka"          %% "akka-actor-typed"  % AkkaVersion,
    "com.typesafe.akka"          %% "akka-http"         % AkkaHttpVersion,
    "com.typesafe.akka"          %% "akka-stream"       % AkkaVersion,
    "com.typesafe.akka"          %% "akka-stream-kafka" % AkkaKafkaVersion,
    "com.typesafe.akka"          %% "akka-discovery"    % AkkaVersion,
    "com.fasterxml.jackson.core"  % "jackson-databind"  % JacksonVersion,
    "org.typelevel"              %% "cats-core"         % catsVersion,
    "ch.qos.logback"              % "logback-classic"   % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging"     % scalaLoggingVersion,
    "io.circe"                   %% "circe-core"        % circeVersion,
    "io.circe"                   %% "circe-generic"     % circeVersion,
    "io.circe"                   %% "circe-parser"      % circeVersion,
    "de.heikoseeberger"          %% "akka-http-circe"   % AkkaHttpJsonVersion
  )

  val test = Seq.empty[ModuleID]

  val all = prod ++ test

}
