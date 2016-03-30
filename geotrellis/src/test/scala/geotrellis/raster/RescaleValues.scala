package benchmark.geotrellis.raster

import geotrellis.engine._
import geotrellis.raster._
import geotrellis.engine.op.global._

class RescaleBenchmark extends OperationBenchmark {
  val n = 10
  val name = "SBN_farm_mkt"

  @Param(Array("256", "512", "1024", "2048", "4096", "8192"))
  var size: Int = 0

  var op: Op[Tile] = null
  var source: RasterSource = null
  override def setUp() {
    val re = getRasterExtent(name, size, size)
    val raster = RasterSource(name, re).get
    op = raster.rescale(0, 100)

    source = 
      RasterSource(name, re)
        .cached
        .rescale(0, 100)
  }

  def timeRescaleOp(reps: Int) = run(reps)(rescaleOp)
  def rescaleOp = get(op)

  def timeRescaleSource(reps: Int) = run(reps)(rescaleSource)
  def rescaleSource = get(source)
}
