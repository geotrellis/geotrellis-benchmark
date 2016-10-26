package benchmark.geotrellis.vectortile

import geotrellis.raster.TileLayout
import geotrellis.spark.SpatialKey
import geotrellis.spark.tiling.LayoutDefinition
import geotrellis.vector.Extent
import geotrellis.vectortile.VectorTile
import geotrellis.vectortile.protobuf.ProtobufTile
import geotrellis.vectortile.protobuf.internal.{vector_tile => vt}

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

        def run() = vt.Tile.parseFrom(bytes)
      }
    }

    run("linestring.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
        }

        def run() = vt.Tile.parseFrom(bytes)
      }
    }

    run("polygon.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
        }

        def run() = vt.Tile.parseFrom(bytes)
      }
    }

    run("roads.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
        }

        def run() = vt.Tile.parseFrom(bytes)
      }
    }
  }

  benchmark("Decode: Array[Byte] -> VectorTile") {
    run("onepoint.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _
        var tileExtent: Extent = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/onepoint.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

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
        var tileExtent: Extent = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

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
        var tileExtent: Extent = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

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
        var tileExtent: Extent = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

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
        var tileExtent: Extent = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/onepoint.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
          tile = ProtobufTile.fromBytes(bytes, tileExtent)
        }

        def run() = {
          tile.asInstanceOf[ProtobufTile].toBytes
        }
      }
    }


    run("linestring.mvt") {
      new Benchmark {
        var tile: VectorTile = _
        var tileExtent: Extent = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/linestring.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
          tile = ProtobufTile.fromBytes(bytes, tileExtent)
        }

        def run() = {
          tile.asInstanceOf[ProtobufTile].toBytes
        }
      }
    }


    run("polygon.mvt") {
      new Benchmark {
        var tile: VectorTile = _
        var tileExtent: Extent = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
          tile = ProtobufTile.fromBytes(bytes, tileExtent)
        }

        def run() = {
          tile.asInstanceOf[ProtobufTile].toBytes
        }
      }
    }


    run("roads.mvt") {
      new Benchmark {
        var tile: VectorTile = _
        var tileExtent: Extent = _

        override def setUp() = {
          val bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
          tile = ProtobufTile.fromBytes(bytes, tileExtent)
        }

        def run() = {
          tile.asInstanceOf[ProtobufTile].toBytes
        }
      }
    }
  }

  benchmark("Data Access: Fetching first Polygon") {
    run("polygon.mvt") {
      new Benchmark {
        var bytes: Array[Byte] = _
        var tileExtent: Extent = _


        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/polygon.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

          t.layers.get("OnePolygon").get.polygons.head.geom
        }
      }
    }

    run("roads.mvt (water layer)") {
      new Benchmark {
        var bytes: Array[Byte] = _
        var tileExtent: Extent = _

        override def setUp() = {
          bytes = Files.readAllBytes(Paths.get("geotrellis/src/test/resources/data/roads.mvt"))
          tileExtent = Extent(0, 0, 4096, 4096)
        }

        def run() = {
          val t = ProtobufTile.fromBytes(bytes, tileExtent)

          t.layers.get("water").get.multiPolygons.head.geom.polygons.head
        }
      }
    }
  }
}
