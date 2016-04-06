package benchmark.geotrellis.raster

import geotrellis.vector._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._

import scala.util.Random

import benchmark.geotrellis.util._
import scaliper._


class GRaster[T](val array: Array[T]) {
  val size = array.size
  val newArr = array.clone
  def map(f: T=>T) = {
    var i = 0
    while(i < size) {
      newArr(i) = f(array(i))
      i += 1
    }
    new GRaster(newArr)
  }
}

trait GenericRasterSetup { this: Benchmark =>
  val size: Int

  var tile: Tile = null
  var genericRaster: GRaster[Int] = null

  override def setUp() {
    val len = size * size
    tile = ArrayTile(init(len)(Random.nextInt), size, size)
    genericRaster = new GRaster(init(len)(Random.nextInt))
  }
}

class GenericRaster extends Benchmarks {
  benchmark("Generic Raster Map") {
    for (s <- Array(128, 256, 512, 1024, 2048, 4096, 8192)) {
      run(s"size: ${s}") {
        new Benchmark with GenericRasterSetup {
          val size = s
          def run = genericRaster.map { i => i * i }
        }
      }
    }
  }
  benchmark("Raster Map") {
    for (s <- Array(128, 256, 512, 1024, 2048, 4096, 8192)) {
      run(s"size: ${s}") {
        new Benchmark with GenericRasterSetup {
          val size = s
          def run = tile.map { i => i * i }
        }
      }
    }
  }
}
