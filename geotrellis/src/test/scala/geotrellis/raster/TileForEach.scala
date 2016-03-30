package benchmark.geotrellis.raster

import geotrellis.raster._

import benchmark.geotrellis.util._

import scaliper._

trait TileForEachSetup { this: Benchmark =>
  val size: Int
  var tile: Tile = null

  override def setUp() {
    tile = get(loadRaster("SBN_farm_mkt", size, size))
  }
}

class TileForeachBenchmarks extends Benchmarks {
  benchmark("While Loop") {
    for (s <- Array(64, 128, 256, 512, 1024)) {
      run("size: ${s}") {
        new Benchmark with TileForEachSetup {
          val size = s

          def run = {
            var t = 0
            var i = 0
            val d = tile.toArray
            val len = tile.cols * tile.rows
            while (i < len) {
              t += d(i)
              i += 1
            }
            t
          }
        }
      }
    }
  }
  benchmark("Tile foreach") {
    for (s <- Array(64, 128, 256, 512, 1024)) {
      run("size: ${s}") {
        new Benchmark with TileForEachSetup {
          val size = s

          def run = {
            var t = 0
            tile.foreach(z => t += z)
            t
          }
        }
      }
    }
  }
}


