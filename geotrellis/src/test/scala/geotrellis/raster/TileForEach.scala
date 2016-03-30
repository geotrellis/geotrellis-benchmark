package benchmark.geotrellis.raster

import geotrellis.raster._

class TileForeach extends Benchmarks {
  @Param(Array("64", "128", "256", "512", "1024"))
  var size: Int = 0
  
  var tile: Tile = null

  override def setUp() {
    tile = get(loadRaster("SBN_farm_mkt", size, size))
  }

  def timeRasterForeach(reps: Int) = run(reps)(rasterForeach)
  def rasterForeach = {
    var t = 0
    tile.foreach(z => t += z)
    t
  }

  def timeRasterWhile(reps: Int) = run(reps)(rasterWhile)
  def rasterWhile = {
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
