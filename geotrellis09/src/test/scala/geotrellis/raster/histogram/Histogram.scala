package benchmark.geotrellis.raster.histogram

import geotrellis.Raster
import geotrellis.statistics._
import geotrellis.data._

import spire.syntax.cfor._

import scaliper._

class HistogramBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Histogram Benchmarks") {

    run("Integer Histogram") {
      new Benchmark {
        var raster: Raster = _
        var histogram: MutableHistogram = _
        var rows: Int = _
        var cols: Int = _

        override def setUp() = {
          raster = GeoTiff.readRaster("data/geotiff/SBN_inc_percap.tif")
          histogram = FastMapHistogram()
          rows = raster.rows
          cols = raster.cols
        }

        def run() = {
          cfor(0)(_ < rows, _ + 1) { row =>
            cfor(0)(_ < cols, _ + 1) { col =>
              histogram.countItem(raster.get(col, row))
            }
          }
        }
      }
    }

  }
}
