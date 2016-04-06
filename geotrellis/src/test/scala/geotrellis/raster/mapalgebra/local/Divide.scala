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
    for (s <- Array(256, 512, 1024, 2048, 4096, 8192)) {
      run(s"Op; size: ${s}") {
        new Benchmark {
          val size: Int = s

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
      run(s"Source; size: ${s}") {
        new Benchmark {
          val size: Int = s

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

