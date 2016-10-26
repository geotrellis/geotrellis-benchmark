package benchmark.geotrellis.raster.op.focal

import geotrellis._
import geotrellis.raster.viewshed.Viewshed
import geotrellis.vector._
import geotrellis.raster._

import scala.util.Random
import scaliper._

class  ViewshedBenchmark extends Benchmarks {
  benchmark("Corner viewshed") {
    for (size <- Array(256, 512, 1024, 2048, 4096, 8192)) {
      run("size: ${size}") {
        new Benchmark {
          var r: Tile = _

          override def setUp() {
            val a = Array.ofDim[Int](size * size).map(a => Random.nextInt(255))
            r = IntArrayTile(a, size, size)
          }

          def run = Viewshed(r, 0, 0)
        }
      }
    }
  }
  benchmark("Center viewshed") {
    for (size <- Array(256, 512, 1024, 2048, 4096, 8192)) {
      run("size: ${size}") {
        new Benchmark {
          var r: Tile = _

          override def setUp() {
            val a = Array.ofDim[Int](size * size).map(a => Random.nextInt(255))
            r = IntArrayTile(a, size, size)
          }

          def run = Viewshed(r, size/2, size/2)
        }
      }
    }
  }
}

