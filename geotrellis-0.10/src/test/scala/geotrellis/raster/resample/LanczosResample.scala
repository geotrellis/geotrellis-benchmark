package benchmark.geotrellis.raster.resample
import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import geotrellis.raster.reproject._
import geotrellis.raster.resample._
import geotrellis.vector.Extent
import geotrellis.proj4._

import benchmark.geotrellis.util._
import scaliper._

class LanczosResample extends Benchmarks {
  benchmark(s"lanczos resample") {
    run("Int resample") {
      new Benchmark with IntResampleBenchmark {
        val method = Lanczos
      }
    }
  }
}
