name := "blastr"

version := "0.1"

scalaVersion := "2.13.6"

lazy val blastr = (project in file("."))
  .enablePlugins(AkkaGrpcPlugin)
  .settings(
    name := "Blastr",
    libraryDependencies ++= Dependencies.all,
    akkaGrpcGeneratedSources := Seq(AkkaGrpc.Server)
  )
