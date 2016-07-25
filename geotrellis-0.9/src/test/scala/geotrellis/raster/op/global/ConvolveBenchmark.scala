package benchmark.geotrellis.raster.op.global

import geotrellis._
import geotrellis.raster._
import geotrellis.raster.op.global._
import geotrellis.source._
import geotrellis.process._

import scaliper._

class ConvolveBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Convolve Benchmarks") {
    run("Convolve SBN tile") {
      new Benchmark {
        var raster: Raster = _
        var server: Server = _
        var kernel: Kernel = _

        override def setUp() = {
          server = GeoTrellis.server
          raster = RasterSource.fromPath("data/arg/SBN_inc_percap.arg").get
          kernel = Kernel.gaussian(5, raster.rasterExtent.cellwidth, 4.0, 50.0)
        }

        def run() = server.get(Convolve(raster, kernel))

        override def tearDown() = {
          server.shutdown()
        }
      }
    }

    run("Convole Byte Raster") {
      new Benchmark {
        var raster: Raster = _
        var server: Server = _
        var kernel: Kernel = _

        override def setUp() = {
          server = GeoTrellis.server
          val sbnRaster = RasterSource.fromPath("data/arg/SBN_inc_percap.arg").get
          val rd = sbnRaster.toArrayRaster.data.mutable

          for(col <- 0 until sbnRaster.cols) {
            for(row <- 0 until sbnRaster.rows) {
              val z = scala.util.Random.nextInt
              rd.set(col, row, if(z == NODATA) 1 else z )
            }
          }
          raster = ArrayRaster(rd, sbnRaster.rasterExtent)
          kernel = Kernel.gaussian(5, raster.rasterExtent.cellwidth, 4.0, 50.0)
        }

        def run() = server.get(Convolve(raster, kernel))

        override def tearDown() = {
          server.shutdown()
        }
      }
    }
  }
}
