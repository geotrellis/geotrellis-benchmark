package benchmark.geotrellis.raster.op.focal

import benchmark.geotrellis.util._
import geotrellis.vector._
import geotrellis.engine._
import geotrellis.engine.op.focal._
import geotrellis.engine.op.elevation._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.focal._
import geotrellis.raster.mapalgebra.focal.hillshade._

import geotrellis.raster.mapalgebra.focal.{Square, Circle, Convolve}

import scala.math.{min, max}
import scala.util.Random

import scaliper._

trait FocalOperationsSetup { this: Benchmark =>
  var re: RasterExtent = null
  var r: Tile = null

  var rs: RasterSource = null

  var tiledRS256: RasterSource = null
  var tiledRS512: RasterSource = null

  override def setUp() {
    val name = "SBN_inc_percap"

    val extent = Extent(-8475497.88485957, 4825540.69147447,
                   -8317922.884859569, 4954765.69147447)
    re = RasterExtent(extent, 75.0, 75.0, 2101, 1723)
    r = RasterSource(name, re).get

    rs = RasterSource(name, re).cached

    tiledRS256 = {
      val tileCols = (re.cols + 256 - 1) / 256
      val tileRows = (re.rows + 256 - 1) / 256
      val tileLayout = TileLayout(tileCols, tileRows, 256, 256)
      RasterSource(CompositeTile.wrap(r, tileLayout, cropped = false), extent)
    }
    tiledRS512 = {
      val tileCols = (re.cols + 512 - 1) / 512
      val tileRows = (re.rows + 512 - 1) / 512
      val tileLayout = TileLayout(tileCols, tileRows, 512, 512)
      RasterSource(CompositeTile.wrap(r, tileLayout, cropped = false), extent)
    }
  }
}

class FocalOperationsBenchmark extends Benchmarks {
  benchmark("Focal MapAlgebra operations") {
    run("Conway's Game of Life") {
      new Benchmark with FocalOperationsSetup {
        def run = get(r.focalConway())
      }
    }
    run("hillshade") {
      new Benchmark with FocalOperationsSetup {
        def run = get(r.hillshade(re.cellSize))
      }
    }
    run("slope") {
      new Benchmark with FocalOperationsSetup {
        def run = get(r.slope(re.cellSize, 1.0))
      }
    }
    run("aspect") {
      new Benchmark with FocalOperationsSetup {
        def run = get(r.aspect(re.cellSize))
      }
    }
    run("convolve") {
      new Benchmark with FocalOperationsSetup {
        def run = get(Convolve(r, Kernel.gaussian(5, 4.0, 50.0)))
      }
    }
    for (neighborhood <- Array[Int => Neighborhood](Square.apply, { i: Int => Circle.apply(i.toDouble) })) {
      run("max - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalMax(neighborhood(1)))
        }
      }
      run("min - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalMin(neighborhood(1)))
        }
      }
      run("std deviation - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalStandardDeviation(neighborhood(1)))
        }
      }
      run("median - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalMedian(neighborhood(1)))
        }
      }
      run("mean - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalMean(neighborhood(1)))
        }
      }
      run("mode - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(Mode(r, neighborhood(1)))
        }
      }
      run("moran - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.tileMoransI(neighborhood(1)))
        }
      }
      run("sum - ${neighborhood}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(r.focalSum(neighborhood(1)))
        }
      }
    }
  }
  benchmark("FocalMean vs FastFocalMean") {
    for (neighborhoodSize <- Array[Int](1, 3, 7)) {
      run("FocalMean - size: ${neighborhoodSize}; type: ${neighborhoodType}") {
        new Benchmark with FocalOperationsSetup {
          def run = get(Mean(r, Square(neighborhoodSize)))
        }
      }
      run("FastFocalMean") {
        new Benchmark with FocalOperationsSetup {
          def run = get(FastFocalMean(r, 1))
        }
      }
    }
  }
  benchmark("FocalMean on different sizes of tile") {
    run("tiled 256") {
      new Benchmark with FocalOperationsSetup {
        def run = get(tiledRS256.focalMean(Square(3)))
      }
    }
    run("tiled 512") {
      new Benchmark with FocalOperationsSetup {
        def run = get(tiledRS512.focalMean(Square(3)))
      }
    }
  }
}

