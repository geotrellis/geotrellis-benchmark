package benchmark.geotrellis.raster.mapalgebra.focal

import geotrellis.raster._
import geotrellis.raster.mapalgebra.focal._
import geotrellis.raster.io.geotiff._

import scaliper._
/*
class MeanBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Mean Benchmarks") {
    run("Mean Byte Raster") {
      new Benchmark {
        var tile: Tile = _
        var cellSize: CellSize = _

        override def setUp() = {
          tile = SingleBandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
          cellSize = CellSize(75.0, 75.0)
        }

        def run() = tile.focalMean(Square(1))
      }
    }
  }
}*/
