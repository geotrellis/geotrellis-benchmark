package benchmark.geotrellis.raster.op.focal

import geotrellis._
import geotrellis.raster._
import geotrellis.raster.op.focal._
import geotrellis.source._
import geotrellis.process._

import scaliper._

class SlopeBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Slope Benchmarks") {
    run("Slope Byte Raster") {
      new Benchmark {
        var raster: Raster = _
        var server: Server = _

        override def setUp() = {
          server = GeoTrellis.server
          raster = RasterSource.fromPath("data/arg/SBN_inc_percap.arg").get
        }

        def run() = server.get(Slope(raster))

        override def tearDown() = {
          server.shutdown()
        }
      }
    }
  }
}
