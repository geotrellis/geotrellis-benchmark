package benchmark.geotrellis.raster.resample

import geotrellis.raster._
import geotrellis.raster.reproject._
import geotrellis.raster.resample._
import geotrellis.vector.Extent
import geotrellis.proj4._

import benchmark.geotrellis.util._
import scaliper._

/** The trait used to provide uniform resampling inputs to all resample benchmarks */
trait ResampleBenchmarks extends Benchmarks with ConsoleReport {

  def resamp: ResampleMethod
  def resampleType: String

  benchmark(s"Int-based $resampleType resample") {
    for (size <- Array[Int](64, 128, 256, 512, 1024)) {
      run(s"size: $size") {
        new Benchmark {
          val tileName = "SBN_farm_mkt"
          val s: Int = size

          var tile: Tile = null
          var extent: Extent = null

          override def setUp() {
            tile = get(loadRaster(tileName, size, size))
            extent = Extent(0, 0, size / 100.0, size / 100.0)
          }

          def run() = tile.reproject(extent, LatLng, WebMercator, Reproject.Options(resamp, 0.0))
        }
      }
    }
  }

  benchmark(s"Double-based $resampleType resample") {
    for (size <- Array[Int](64, 128, 256, 512, 1024)) {
      run(s"size: $size") {
        new Benchmark {
          val tileDoubleName = "mtsthelens_tiled"
          val s: Int = size

          var tileDouble: Tile = null
          var extent: Extent = null

          override def setUp() {
            tileDouble = get(loadRaster(tileDoubleName, size, size))
            extent = Extent(0, 0, size / 100.0, size / 100.0)
          }

          def run =
            tileDouble.reproject(extent, LatLng, WebMercator, Reproject.Options(resamp, 0.0))
        }
      }
    }
  }
}
