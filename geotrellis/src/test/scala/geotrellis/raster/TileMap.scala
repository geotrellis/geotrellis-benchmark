package benchmark.geotrellis.raster

import benchmark.geotrellis.util._
import geotrellis.engine._
import geotrellis.engine.op.local._
import geotrellis.engine.op.global._
import geotrellis.vector._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._

import scala.util.Random
import scala.annotation.tailrec

import scaliper._


trait TileMapSetup { this: Benchmark =>
  val size: Int
  def run

  var tile: Tile = null
  var doubleTile: Tile = null
  var computedRaster: Tile = null
  var names: Array[String] = null
  var weights: Array[Int] = null
  var array: Array[Byte] = null
  var length: Int = 0
  var rasterData: ByteArrayTile = null

  override def setUp() {
    val len = size * size
    val re = RasterExtent(Extent(0, 0, size, size), 1.0, 1.0, size, size)
    names = Array("SBN_farm_mkt", "SBN_RR_stops_walk", "SBN_inc_percap", "SBN_street_den_1k")
    weights = Array(2, 1, 5, 2)
    tile = ArrayTile(init(len)(Random.nextInt), size, size)
    doubleTile = ArrayTile(init(len)(Random.nextDouble), size, size)

    val re2 = getRasterExtent(names(0), size, size)

    computedRaster =
      (0 until names.length)
        .map(i => RasterSource(names(i), re2) * weights(i))
        .reduce(_ + _)
        .localDivide(weights.sum)
        .rescale(1, 100)
        .get

    rasterData = computedRaster.asInstanceOf[ByteArrayTile]
    array = rasterData.array
    length = array.length
  }
}


class TileMap extends Benchmarks {
  benchmark("Mapping over an int-based tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = tile map { i => i * 2 }
        }
      }
    }
  }
  benchmark("Mapping (if set) over an int-based tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = tile.mapIfSet(z => z * 2)
        }
      }
    }
  }
  benchmark("Mapping over a double-based tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = tile.mapDouble(z => if (isData(z)) z * 2.0 else Double.NaN)
        }
      }
    }
  }
  benchmark("Mapping (if set) over a double-based tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = doubleTile.mapIfSetDouble(z => z * 2 )
        }
      }
    }
  }
  benchmark("Mapping over a computed tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = computedRaster.map(z => if (isData(z)) z * 2 else NODATA)
        }
      }
    }
  }
  benchmark("Mapping (if set) over a computed tile") {
    for(s <- Array[Int](256, 512, 1024, 2048, 4096)) {
      run(s"size: $s") {
        new Benchmark with TileMapSetup {
          val size: Int = s
          def run = computedRaster.mapIfSet(z => z * 2)
        }
      }
    }
  }
}
