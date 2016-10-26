name := "geotrellis-spark-benchmark"
libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis-spark" % "1.0.0-SNAPSHOT",
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.hadoop" % "hadoop-client" % "2.2.0"
)

javaOptions ++= List(
  "-Xmx6G",
  "-XX:MaxPermSize=384m")

fork := true
