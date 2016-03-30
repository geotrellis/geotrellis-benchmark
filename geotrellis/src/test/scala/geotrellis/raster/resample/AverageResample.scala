package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class AverageResample extends ResampleBenchmarks {
  def resamp = Average
  def resampleType = "Average"
}
