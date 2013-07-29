import spray.revolver.RevolverPlugin.Revolver

name := "Animatron"

version := "0.1"

scalaVersion := "2.9.3"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
    "io.spray" % "spray-client" % "1.0-M+",
    "com.typesafe.akka" % "akka-actor" % "2.0.+",
    "com.typesafe.akka" % "akka-testkit" % "2.0.+",
    "org.scalatest" %% "scalatest" % "latest.release" % "test"
)

seq(Revolver.settings: _*)