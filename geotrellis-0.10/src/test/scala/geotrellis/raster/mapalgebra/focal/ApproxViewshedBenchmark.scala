package benchmark.geotrellis.raster.op.focal

import geotrellis.raster.viewshed._
import geotrellis.vector._
import geotrellis.raster._

import scala.util.Random

import scaliper._

class  ApproxViewshedBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Approx. Corner viewshed") {
    for(size <- Array[Int](256, 512, 1024, 2048, 4096, 8192)) {
      run(s"size: $size") {
        new Benchmark {
          var tile: Tile = null
          val s: Int = size
          override def setUp() {
            tile = {
              val a = Array.ofDim[Int](size * size).map(a => Random.nextInt(255))
              IntArrayTile(a, size, size)
            }
          }
          def run() = ApproxViewshed(tile, 0, 0)
        }
      }
    }
  }

  benchmark("Approx. Center viewshed") {
    for(size <- Array[Int](256, 512, 1024, 2048, 4096, 8192)) {
      run(s"size: $size") {
        new Benchmark {
          var tile: Tile = null
          val s: Int = size
          override def setUp() {
            tile = {
              val a = Array.ofDim[Int](size * size).map(a => Random.nextInt(255))
              IntArrayTile(a, size, size)
            }
          }
          def run() = ApproxViewshed(tile, size/2, size/2)
        }
      }
    }
  }
}

