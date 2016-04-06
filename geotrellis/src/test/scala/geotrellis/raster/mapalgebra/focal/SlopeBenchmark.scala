package benchmark.geotrellis.raster.op.focal

import geotrellis.raster._
//import geotrellis.raster.mapalgebra.elevation._
import geotrellis.raster.io.geotiff._

import scaliper._

class SlopeBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Slope Benchmarks") {
    run("Slope Byte Raster") {
      new Benchmark {
        var tile: Tile = _
        var cellSize: CellSize = _

        override def setUp() = {
          tile = SinglebandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
          cellSize = CellSize(75.0, 75.0)
        }

        def run() = tile.slope(cellSize)
      }
    }
  }
}
