package benchmark.geotrellis.raster

import geotrellis.engine._
import geotrellis.raster._
import geotrellis.engine.op.global._

import benchmark.geotrellis.util._
import scaliper._

trait RescaleSetup { this: Benchmark =>
  val size: Int

  val n = 10
  val name = "SBN_farm_mkt"

  var op: Op[Tile] = null
  var source: RasterSource = null

  override def setUp() {
    val re = getRasterExtent(name, size, size)
    val raster = RasterSource(name, re).get
    op = raster.rescale(0, 100)

    source =
      RasterSource(name, re)
        .cached
        .rescale(0, 100)
  }
}

class RescaleBenchmarks extends Benchmarks {
  benchmark("Rescaling tile values") {
    for (s <- Array(256, 512, 1024, 2048, 4096, 8192)) {
      run("Op - size: ${s}") {
        new Benchmark with RescaleSetup {
          val size = s
          def run = get(op)
        }
      }
      run("Source - size: ${s}") {
        new Benchmark with RescaleSetup {
          val size = s
          def run = get(source)
        }
      }
    }
  }
}

