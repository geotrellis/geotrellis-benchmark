package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class CubicConvolutionResample extends ResampleBenchmarks {
  def resamp = CubicConvolution
  def resampleType = "CubicConvolution"
}
