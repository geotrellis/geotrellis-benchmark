name := "geotrellis-raster-benchmark"
libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis-raster" % "0.10.0-SNAPSHOT",
  "com.azavea" %% "scaliper" % "0.5.0-e9d4266",
  "org.scalatest"       %%  "scalatest"      % Version.scalaTest % "test"
)
