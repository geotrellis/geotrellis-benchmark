package benchmark.geotrellis.raster.resample

import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import geotrellis.raster.reproject._
import geotrellis.raster.resample._
import geotrellis.vector.Extent
import geotrellis.proj4._

import benchmark.geotrellis.util._
import scaliper._

/** The trait used to provide uniform resampling inputs to all int resample benchmarks */
trait IntResampleBenchmark { this: Benchmark =>
  val method: ResampleMethod
  var raster: Raster[Tile] = null
  var rasterExtent: RasterExtent = null
  var trans: Transform = null
  var invTrans: Transform = null

  override def setUp() {
    val geotiff = SinglebandGeoTiff("geotrellis/src/test/resources/data/aspect-tif.tif")
    trans = Transform(LatLng, WebMercator)
    invTrans = Transform(WebMercator, LatLng)
    raster = geotiff.raster
    rasterExtent = raster.rasterExtent
  }

  override def run() = {
    raster.reproject(rasterExtent, trans, invTrans, method)
    ()
  }
}
// TODO: Write double version of the above
