name := "geotrellis010-spark-benchmark"
libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis-spark" % "0.10.0",
  "org.apache.spark" %% "spark-core" % "1.5.0",
  "org.apache.hadoop" % "hadoop-client" % "2.2.0"
)

javaOptions ++= List(
  "-Xmx6G",
  "-XX:MaxPermSize=384m")

fork := true
