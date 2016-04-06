package benchmark.geotrellis.raster

import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._
import geotrellis.engine._
import geotrellis.engine.op.local._

import benchmark.geotrellis.util._

import scaliper._

trait MiniWeightedSetup { this: Benchmark =>
  val size: Int
  val cellType: String
  val layers =
    Map(
      ("bit", "wm_DevelopedLand"),
      ("byte", "SBN_car_share"),
      ("short", "travelshed-int16"),
      ("int", "travelshed-int32"),
      ("float", "aspect"),
      ("double", "aspect-double")
    )


  var source: RasterSource = null
  override def setUp() {
    val name = layers(cellType)
    val re = getRasterExtent(name, size, size)

    source = (RasterSource(name, re) * 5) + (RasterSource(name, re) * 2)
  }
}

class MiniWeightedOverlay extends Benchmarks {
  benchmark("Mini Weighted Overlay") {
    for (s <- Array(256, 512, 1024)) {
      for (ct <- Array("bit", "byte", "short", "int", "float", "double")) {
        run(s"size: ${s}; ${ct}") {
          new Benchmark with MiniWeightedSetup {
            val size = s
            val cellType = ct
            def run = get(source)
          }
        }
      }
    }
  }
}

trait SmallIOSetup { this: Benchmark =>
  val size: Int
  val cellType: String

  val path = "data/aspect.arg"

  val layers =
    Map(
      ("bit", "wm_DevelopedLand"),
      ("byte", "SBN_car_share"),
      ("short", "travelshed-int16"),
      ("int", "travelshed-int32"),
      ("float", "aspect"),
      ("double", "aspect-double")
    )

  var source: RasterSource = null
  var op: Op[Tile] = null

  var re: RasterExtent = null
  var baseRe: RasterExtent = null

  override def setUp() {
    val name = layers(cellType)
    baseRe = get(geotrellis.engine.io.LoadRasterExtent(name))
    re = getRasterExtent(name, size, size)
    op = geotrellis.engine.io.LoadRaster(name, re)
    source = RasterSource(name, re)
  }

}

class SmallIOBenchmark extends Benchmarks {
  benchmark("Small IO Benchmarks") {
    for (s <- Array(256, 512, 1024)) {
      for (ct <- Array("bit", "byte", "short", "int", "float", "double")) {
        run(s"load raster: ${s}; ${ct}") {
          new Benchmark with SmallIOSetup {
            val size: Int = s
            val cellType: String = ct

            def run = get(source)
          }
        }
        run(s"load raster(op): ${s}; ${ct}") {
          new Benchmark with SmallIOSetup {
            val size: Int = s
            val cellType: String = ct

            def run = get(op)
          }
        }
        //run(s"load w/ reader: ${s}; ${ct}") {
        //  new Benchmark with SmallIOSetup {
        //    val size: Int = s
        //    val cellType: String = ct
        //
        //    def run = geotrellis.raster.io.arg.ArgReader.read(path, FloatConstantNoDataCellType, baseRe, re)
        //  }
        //}
        run(s"load 2 raster: ${s}; ${ct}") {
          new Benchmark with SmallIOSetup {
            val size: Int = s
            val cellType: String = ct

            def run = get(geotrellis.engine.io.LoadRaster(layers(cellType)))
          }
        }
      }
    }
  }
}

