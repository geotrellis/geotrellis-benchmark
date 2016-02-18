package benchmark.geotrellis.raster.histogram

import geotrellis.raster._
import geotrellis.raster.histogram._
import geotrellis.raster.io.geotiff._

import spire.syntax.cfor._

import scaliper._

class HistogramBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Histogram Benchmarks") {

    run("Integer Histogram") {
      new Benchmark {
        var tile: Tile = _
        var histogram: MutableHistogram[Int] = _
        var rows: Int = _
        var cols: Int = _

        override def setUp() = {
          tile = SingleBandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
          histogram = FastMapHistogram()
          rows = tile.rows
          cols = tile.cols
        }

        def run() = {
          cfor(0)(_ < rows, _ + 1) { row =>
            cfor(0)(_ < cols, _ + 1) { col =>
              histogram.countItem(tile.get(col, row))
            }
          }
        }
      }
    }

    run("Double Histogram 80 Buckets") {
      new Benchmark {
        var tile: Tile = _
        var histogram: MutableHistogram[Double] = _
        var rows: Int = _
        var cols: Int = _

        override def setUp() = {
          tile = SingleBandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
          histogram = StreamingHistogram(80)
          rows = tile.rows
          cols = tile.cols
        }

        def run() = {
          cfor(0)(_ < rows, _ + 1) { row =>
            cfor(0)(_ < cols, _ + 1) { col =>
              histogram.countItem(tile.get(col, row))
            }
          }
        }
      }
    }

    run("Double Histogram 1000 Buckets") {
      new Benchmark {
        var tile: Tile = _
        var histogram: MutableHistogram[Double] = _
        var rows: Int = _
        var cols: Int = _

        override def setUp() = {
          tile = SingleBandGeoTiff("data/geotiff/SBN_inc_percap.tif").tile
          histogram = StreamingHistogram(1000)
          rows = tile.rows
          cols = tile.cols
        }

        def run() = {
          cfor(0)(_ < rows, _ + 1) { row =>
            cfor(0)(_ < cols, _ + 1) { col =>
              histogram.countItem(tile.get(col, row))
            }
          }
        }
      }
    }

  }
}
