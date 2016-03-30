package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class ModeResample extends ResampleBenchmarks {
  def resamp = Mode
  def resampleType = "Mode"
}
