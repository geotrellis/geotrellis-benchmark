package benchmark.geotrellis.raster.mapalgebra.local

import geotrellis.engine._
import geotrellis.raster._
import geotrellis.engine.op.local._
import geotrellis.raster.summary._
import geotrellis.raster.render._

import benchmark.geotrellis.util._
import scaliper._

trait WeightedAddSetup { this: Benchmark =>
  val size: Int
  var source: RasterSource = null
  var sourceSeq: RasterSource = null
  var weights: Seq[Int] = null
  var names: Seq[String] = null

  override def setUp() {
    weights = Seq(2, 1, 5, 2,
                  2, 1, 5, 2,
                  2, 1, 5, 2,
                  2, 1, 5, 2)
    names = Seq("SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k",
                "SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k")
    val n = names.length
    val re = getRasterExtent(names(0), size, size)

    source =
      (0 until n).map(i => RasterSource(names(i), re) * weights(i))
                 .reduce(_ + _)
    sourceSeq =
      (0 until n).map(i => RasterSource(names(i), re) * weights(i))
                 .localAdd
  }
}

class WeightedAdd extends Benchmarks {
  benchmark("Weighted addition") {
    for (s <- Array(256, 512, 1024, 2048, 4096)) {
      run("by folding over Seq") {
        new Benchmark with WeightedAddSetup{
          val size = s
          def run = get(source)
        }
      }
      run("by localAdd on Seq") {
        new Benchmark with WeightedAddSetup {
          val size = s
          def run = get(sourceSeq)
        }
      }
    }
  }
}

