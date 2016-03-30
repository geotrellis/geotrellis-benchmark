package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class CubicSplineResample extends ResampleBenchmarks {
  def resamp = CubicSpline
  def resampleType = "CubicSpline"
}
