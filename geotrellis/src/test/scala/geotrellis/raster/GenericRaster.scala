package benchmark.geotrellis.raster

import geotrellis.vector._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._

import scala.util.Random

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

class GenericRaster extends OperationBenchmark {
  @Param(Array("128", "256", "512", "1024", "2048", "4096", "8192"))
  var size: Int = 0

  var tile: Tile = null
  var genericRaster: GRaster[Int] = null

  override def setUp() {
    val len = size * size
    tile = ArrayTile(init(len)(Random.nextInt), size, size)
    genericRaster = new GRaster(init(len)(Random.nextInt))

  }

  def timeGenericRasterMap(reps: Int) = run(reps)(genericRasterMap)
  def genericRasterMap = genericRaster.map { i => i * i }

  def timeRasterMap(reps: Int) = run(reps)(rasterMap)
  def rasterMap = tile.map { i => i * i }
}
