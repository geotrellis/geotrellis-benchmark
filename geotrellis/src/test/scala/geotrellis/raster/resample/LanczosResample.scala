package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class LanczosResample extends ResampleBenchmarks {
  def resamp = Lanczos
  def resampleType = "Lanczos"
}
