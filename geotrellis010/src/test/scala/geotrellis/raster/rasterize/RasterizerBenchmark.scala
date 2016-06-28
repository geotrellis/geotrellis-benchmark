package benchmark.geotrellis.raster.rasterize

import geotrellis._
import geotrellis.engine._
import geotrellis.raster._
import geotrellis.raster.mapalgebra._
import geotrellis.vector._
import geotrellis.vector.io._
import geotrellis.vector.io.json._
import geotrellis.raster.PixelSampleType
import geotrellis.raster.rasterize.polygon._
import geotrellis.raster.rasterize.Rasterizer.Options

import scala.math.{min, max}
import scala.util.Random

import benchmark.geotrellis.util._
import scaliper._

trait RasterizerSetup { this: Benchmark =>
  val size: Int

  val tile: IntArrayTile = IntArrayTile.empty(size, size)

  var r: Tile = null
  var re: RasterExtent = null
  var poly: vector.PolygonFeature[Int] = null
  var transitPoly: Polygon = null
  var transitPolyNoHoles: Polygon = null
  var transitRe: RasterExtent = null

  def randomRasterN(n: Int) = {
    val a = Array.ofDim[Int](n*n).map(a => Random.nextInt(255))
    IntArrayTile(a, n, n)
  }

  override def setUp() {
    r = randomRasterN(size)

    val p1 = Point(0,0)
    val p2 = Point(10*size,0)
    val p3 = Point(10*size/2, 10*size)
    poly = PolygonFeature(Polygon(Line(p1,p2,p3,p1)), 1)

    transitPoly = GeoJson.fromFile[Polygon]("geotrellis/src/test/resources/data/transitgeo.json")
    transitPolyNoHoles = Polygon(transitPoly.exterior)
    val vector.Extent(xmin, ymin, xmax, ymax) = transitPoly.envelope
    val dx = (xmax - xmin) / 4
    val dy = (ymax - ymin) / 4
    val ext = Extent(xmin - dx, ymin - dy, xmax + dx, ymax + dy)
    transitRe = RasterExtent(ext, size, size)
  }
}

class RasterizerBenchmark extends Benchmarks {
  /*benchmark("Rasterizing a polygon") {
    for (s <-Array(512, 1024, 2048, 4096, 8192)) {
      run(s"size: ${s}") {
        new Benchmark with RasterizerSetup {
          val size = s
          def run = poly.geom.foreach(re)({ (col: Int, row: Int) =>
            tile.set(col,row,4)
          })
        }
      }
    }
  }*/
  benchmark("Rasterizing transit polygon") {
    for (s <-Array(512, 1024, 2048, 4096, 8192)) {
      run(s"size: ${s}") {
        new Benchmark with RasterizerSetup {
          val size = s

          def run = {
            var x = 0
            val options = Options(includePartial = true, sampleType = PixelIsArea)
            transitPoly.foreach(transitRe, options)({ (col: Int, row: Int) =>
              x += (col + row)
            })
          }
        }
      }
    }
  }
  benchmark("Rasterizing transit polygon w/o holes") {
    for (s <-Array(512, 1024, 2048, 4096, 8192)) {
      run(s"size: ${s}") {
        new Benchmark with RasterizerSetup {
          val size = s

          def run = {
            var x = 0
            val options = Options(includePartial = true, sampleType = PixelIsArea)
            transitPolyNoHoles.foreach(transitRe, options)({ (col: Int, row: Int) =>
              x += (col + row)
            })
          }
        }
      }
    }
  }
}


