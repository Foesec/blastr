import sbt._

object Dependencies {

  val AkkaVersion      = "2.6.16"
  val AkkaKafkaVersion = "2.1.1"
  val AkkaHttpVersion  = "10.2.6"
  val JacksonVersion   = "2.11.4"

  val prod = Seq(
    "com.typesafe.akka"         %% "akka-actor-typed"  % AkkaVersion,
    "com.typesafe.akka"         %% "akka-http"         % AkkaHttpVersion,
    "com.typesafe.akka"         %% "akka-stream"       % AkkaVersion,
    "com.typesafe.akka"         %% "akka-stream-kafka" % AkkaKafkaVersion,
    "com.fasterxml.jackson.core" % "jackson-databind"  % JacksonVersion
  )

  val test = Seq.empty[ModuleID]

  val all = prod ++ test

}
