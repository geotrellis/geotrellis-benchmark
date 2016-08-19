lazy val commonSettings = Seq(
  version := Version.benchmarks,
  scalaVersion := Version.scala,
  crossScalaVersions := Version.crossScala,
  description := "GeoTrellis benchmarking project",
  organization := "com.azavea.geotrellis",
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-Yinline-warnings",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-feature"),

  resolvers += Resolver.bintrayRepo("azavea", "maven"),
  libraryDependencies ++= Seq(
    "com.azavea" %% "scaliper" % "0.5.0-SNAPSHOT" % "test",
    "org.scalatest"       %%  "scalatest"      % Version.scalaTest % "test"
  ),

  parallelExecution in Test := false,

  shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings

lazy val root = Project("root", file(".")).
  dependsOn(geotrellisRaster, geotrellisSpark)

lazy val geotrellisRaster = Project("geotrellis-benchmark", file("geotrellis")).
  settings(commonSettings: _*)

lazy val geotrellisSpark = Project("geotrellis-spark-benchmark", file("geotrellis/spark")).
  settings(commonSettings: _*)

lazy val geotrellisRaster010 = Project("geotrellis010-benchmark", file("geotrellis-0.10")).
  settings(commonSettings: _*)

lazy val geotrellisSpark010 = Project("geotrellis010-spark-benchmark", file("geotrellis-0.10/spark")).
  settings(commonSettings: _*)

lazy val geotrellis09 = Project("geotrellis09-benchmark", file("geotrellis-0.9")).
  settings(commonSettings: _*)
