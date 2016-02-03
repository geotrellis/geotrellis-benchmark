name := "geotrellis09-benchmark"
libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis" % "0.9.2",
  "com.azavea.geotrellis" %% "geotrellis-geotools" % "0.9.0"
)
resolvers += "Geotools" at "http://download.osgeo.org/webdav/geotools/"
