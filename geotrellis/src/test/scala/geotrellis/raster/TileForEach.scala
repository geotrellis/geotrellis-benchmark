package benchmark.geotrellis.raster

import geotrellis.raster._

import benchmark.geotrellis.util._

import scaliper._

class TileForeach extends Benchmarks {
  benchmark("While Loop") {
    for (size <- Array(64, 128, 256, 512, 1024)) {
      run("size: ${size}") {
        new Benchmark {
          var tile: Tile = null

          override def setUp() {
            tile = get(loadRaster("SBN_farm_mkt", size, size))
          }

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
    for (size <- Array(64, 128, 256, 512, 1024)) {
      run("size: ${size}") {
        new Benchmark {
          var tile: Tile = null

          override def setUp() {
            tile = get(loadRaster("SBN_farm_mkt", size, size))
          }

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


