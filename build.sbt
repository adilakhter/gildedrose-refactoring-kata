import Dependencies._

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.gildedrose"

lazy val root = (project in file("."))
  .settings(
    name := "gildedrose-refactoring-kata",
    libraryDependencies ++= Seq(
      scalaLogging,
      logbackClassic,
      scalaTest % Test,
      scalaTestWordspec % Test,
      scalaCheck % Test)
  )
