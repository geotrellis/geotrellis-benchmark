package benchmark.geotrellis.raster.op.focal

import geotrellis.raster._
import geotrellis.raster.mapalgebra.focal._
import geotrellis.raster.io.geotiff._

import scaliper._

trait ConvolveBenchmarkSetup { self: Benchmark =>
  def getTile: Tile

  def sbnTile = SinglebandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
  def byteFillTile = {
    val sbnTile = SinglebandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
    val m = sbnTile.mutable
    for(col <- 0 until sbnTile.cols) {
      for(row <- 0 until sbnTile.rows) {
        val z = scala.util.Random.nextInt
        m.set(col, row, if(z == NODATA) 1 else z )
      }
    }
    m
  }

  var tile: Tile = _
  var kernel: Kernel = _

  override def setUp() = {
    tile = getTile
    kernel = Kernel.gaussian(5, 4.0, 50.0)
  }
}

class ConvolveBenchmark extends Benchmarks with ConsoleReport with EndpointReport {
  benchmark("Convolve Benchmarks") {
    run("Convolve SBN tile") {
      new Benchmark with ConvolveBenchmarkSetup {
        def getTile = sbnTile
        def run() = tile.convolve(kernel)
      }
    }

    run("Convole Byte Raster") {
      new Benchmark with ConvolveBenchmarkSetup {
        def getTile = byteFillTile
        def run() = tile.convolve(kernel)
      }
    }
  }
}
