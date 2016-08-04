package benchmark.geotrellis.vectortile

import geotrellis.vectortile.VectorTile
import geotrellis.vectortile.protobuf._

import scaliper._

import java.nio.file.{Files, Paths}

// --- //

class VectorTileBench extends Benchmarks with ConsoleReport {

  benchmark("Decode: Array[Byte] -> vector_tile.Tile (ScalaPB)") {
    run("onepoint.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/onepoint.mvt"))
        }

        def run() = Protobuf.decode(bytes)
      }
    }

    run("linestring.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
        }

        def run() = Protobuf.decode(bytes)
      }
    }

    run("polygon.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
        }

        def run() = Protobuf.decode(bytes)
      }
    }

    run("roads.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
        }

        def run() = Protobuf.decode(bytes)
      }
    }
  }

  benchmark("Decode: Array[Byte] -> VectorTile") {
    run("onepoint.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/onepoint.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          /* Force geometry evaluation */
          t.layers.foreach {
            case (_, l) =>
              l.points.length
              l.multiPoints.length
              l.lines.length
              l.multiLines.length
              l.polygons.length
              l.multiPolygons.length
          }
        }
      }
    }

    run("linestring.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          /* Force geometry evaluation */
          t.layers.foreach {
            case (_, l) =>
              l.points.length
              l.multiPoints.length
              l.lines.length
              l.multiLines.length
              l.polygons.length
              l.multiPolygons.length
          }
        }
      }

    }

    run("polygon.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          /* Force geometry evaluation */
          t.layers.foreach {
            case (_, l) =>
              l.points.length
              l.multiPoints.length
              l.lines.length
              l.multiLines.length
              l.polygons.length
              l.multiPolygons.length
          }
        }

      }
    }

    run("roads.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          /* Force geometry evaluation */
          t.layers.foreach {
            case (_, l) =>
              l.points.length
              l.multiPoints.length
              l.lines.length
              l.multiLines.length
              l.polygons.length
              l.multiPolygons.length
          }
        }

      }
    }
  }

  benchmark("Encoding: VectorTile -> Array[Byte]") {
    run("onepoint.mvt") {
      new Benchmark {
        var tile: VectorTile = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/onepoint.mvt"))
          tile = ProtobufTile(bytes)
        }

        def run() = {
          Protobuf.encode(tile.asInstanceOf[ProtobufTile].toProtobuf)
        }
      }
    }

    run("linestring.mvt") {
      new Benchmark {
        var tile: VectorTile = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
          tile = ProtobufTile(bytes)
        }

        def run() = {
          Protobuf.encode(tile.asInstanceOf[ProtobufTile].toProtobuf)
        }
      }
        }

    run("polygon.mvt") {
      new Benchmark {
        var tile: VectorTile = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
          tile = ProtobufTile(bytes)
        }

        def run() = {
          Protobuf.encode(tile.asInstanceOf[ProtobufTile].toProtobuf)
        }
      }
        }

    run("roads.mvt") {
      new Benchmark {
        var tile: VectorTile = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
          tile = ProtobufTile(bytes)
        }

        def run() = {
          Protobuf.encode(tile.asInstanceOf[ProtobufTile].toProtobuf)
        }
      }
    }
  }

  benchmark("Data Access: Fetching first Polygon") {
    run("polygon.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          t.layers.get("OnePolygon").get.polygons.head.geom
        }
      }
    }

    run("roads.mvt (water layer)") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
        }

        def run() = {
          val t = ProtobufTile(bytes)

          t.layers.get("water").get.multiPolygons.head.geom.polygons.head
        }
      }
    }
  }
}
