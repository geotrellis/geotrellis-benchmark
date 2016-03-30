package benchmark.geotrellis.raster.resample

import geotrellis.raster.resample._

class NearestNeighborResample extends ResampleBenchmarks {
  def resamp = NearestNeighbor
  def resampleType = "NearestNeighbor"
}
