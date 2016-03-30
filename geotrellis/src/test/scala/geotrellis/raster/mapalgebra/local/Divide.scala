package benchmark.geotrellis.raster.local

import geotrellis.engine._
import geotrellis.engine.op.local._
import geotrellis.engine.io.LoadRaster
import geotrellis.raster._
import geotrellis.raster.mapalgebra._
import geotrellis.raster.mapalgebra.local._

import com.google.caliper.Param

object DivideBenchmark extends BenchmarkRunner(classOf[DivideBenchmark])
class DivideBenchmark extends OperationBenchmark {
  val n = 10
  val name = "SBN_farm_mkt"

  @Param(Array("256", "512", "1024", "2048", "4096", "8192"))
  var size: Int = 0

  var op: Op[Tile] = null
  var source: RasterSource = null

  override def setUp() {
    val re = getRasterExtent(name, size, size)
    val raster = get(LoadRaster(name, re))
    op = local.Divide(raster, n)

    source = 
      RasterSource(name, re)
        .cached
        .localDivide(n)
  }

  def timeDivideOp(reps: Int) = run(reps)(divideOp)
  def divideOp = get(op)

  def timeDivideSource(reps: Int) = run(reps)(divideSource)
  def divideSource = get(source)
}
