package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class BilinearResample extends ResampleBenchmarks {
  def resamp = Bilinear
  def resampleType = "Bilinear"
}
