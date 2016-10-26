package benchmark.geotrellis

import geotrellis.raster._
import geotrellis.engine._

package object util {

  def getRasterExtent(name: String, w: Int, h: Int): RasterExtent = {
    val ext = RasterSource(name).info.get.rasterExtent.extent
    RasterExtent(ext, w, h)
  }
  /** Loads a given raster with a particular height / width */
  def loadRaster(name: String, w: Int, h: Int): Tile =
    RasterSource(name, getRasterExtent(name, w, h)).get

  def get[T](op: Op[T]): T = GeoTrellis.get(op)
  def get[T](source: DataSource[_, T]): T = GeoTrellis.get(source)

  /** Sugar for building arrays using a per-cell init function */
  def init[A: Manifest](size: Int)(init: => A) = {
    val data = Array.ofDim[A](size)
    for (i <- 0 until size) data(i) = init
    data
  }
}
