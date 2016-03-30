package benchmark.geotrellis.raster.local

import geotrellis.engine._
import geotrellis.engine.op.local._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._

import scaliper._

class AddRasters extends Benchmarks with ConsoleReport {
  benchmark("Adding rasters") {
    for(size <- Array[Int](64, 128, 256, 512, 1024, 2048, 4096)) {
      run(s"size: $size") {

        def getRasterExtent(name: String, w: Int, h: Int): RasterExtent = {
          val ext = RasterSource(name).info.get.rasterExtent.extent
          RasterExtent(ext, w, h)
        }

        new Benchmark {
          val s: Int = size
          var source: RasterSource = null

          override def setUp() {
            val id = "SBN_farm_mkt"
            val re =  getRasterExtent(id, s, s)
            val r = RasterSource(RasterSource(id, re).get, re.extent)
            val r1 = r+1
            val r2 = r+2
            val r3 = r+3
            val r4 = r+4
            val r5 = r+5
            source = (r1+r2+r3+r4+r5)
          }

          def run() = GeoTrellis.get(source)
        }

      }
    }
  }
}
