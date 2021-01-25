import sbt._

object Dependencies {
  // Testing
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.2"
  lazy val scalaTestWordspec= "org.scalatest" %% "scalatest-wordspec" % "3.2.3"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.15.2"

  // Logging
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
