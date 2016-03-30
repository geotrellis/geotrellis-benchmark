package benchmark.geotrellis.raster.mapalgebra.local

import geotrellis.engine._
import geotrellis.raster._
import geotrellis.engine.op.local._
import geotrellis.raster.summary._
import geotrellis.raster.render._

import benchmark.geotrellis.util._
import scaliper._

class WeightedAdd extends Benchmarks {
  benchmark("Weighted addition") {
    for (size <- Array(256, 512, 1024, 2048, 4096)) {
      run("by folding over Seq") {
        new Benchmark {
          val names = Array("SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k")
          val weights = Array(2, 1, 5, 2,
                              2, 1, 5, 2,
                              2, 1, 5, 2,
                              2, 1, 5, 2)
          var source: RasterSource = null

          override def setUp() {
            val n = names.length
            val re = getRasterExtent(names(0), size, size)

            source =
              (0 until n).map(i => RasterSource(names(i), re) * weights(i))
                         .reduce(_ + _)
          }
        def run = get(source)

        }
      }
      run("by localAdd on Seq") {
        new Benchmark {
          val names = Array("SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                            "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k")
          val weights = Array(2, 1, 5, 2,
                              2, 1, 5, 2,
                              2, 1, 5, 2,
                              2, 1, 5, 2)
          var sourceSeq: RasterSource = null

          override def setUp() {
            val n = names.length
            val re = getRasterExtent(names(0), size, size)

            sourceSeq =
              (0 until n).map(i => RasterSource(names(i), re) * weights(i))
                         .localAdd
          }
          def run = get(sourceSeq)
        }
      }
    }
  }
}

