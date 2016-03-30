package benchmark.geotrellis.raster.local

import benchmark.geotrellis.util._

import geotrellis.engine._
import geotrellis.engine.op.local._
import geotrellis.engine.io.LoadRaster
import geotrellis.raster._
import geotrellis.raster.mapalgebra._
import geotrellis.raster.mapalgebra.local._

import scaliper._

class DivideBenchmark extends Benchmarks {
  benchmark("Local Divide") {
    for (size <- Array(256, 512, 1024, 2048, 4096, 8192)) {
      run("Op") {
        new Benchmark {
          var size: Int = 0

          var op: Op[Tile] = null

          override def setUp() {
            val name = "SBN_farm_mkt"
            val n = 10
            val re = getRasterExtent(name, size, size)
            val raster = get(LoadRaster(name, re))
            op = local.Divide(raster, n)
          }

          def run = get(op)
        }
      }
      run("Source") {
        new Benchmark {
          var size: Int = 0

          var op: Op[Tile] = null
          var source: RasterSource = null

          override def setUp() {
            val name = "SBN_farm_mkt"
            val n = 10
            val re = getRasterExtent(name, size, size)
            val raster = get(LoadRaster(name, re))
            op = local.Divide(raster, n)

            source = RasterSource(name, re)
              .cached
              .localDivide(n)
          }
          def run = get(source)
        }
      }
    }
  }
}

