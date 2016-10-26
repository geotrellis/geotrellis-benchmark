package benchmark.geotrellis.raster

import geotrellis.vector._
import geotrellis.raster._
import geotrellis.raster.mapalgebra.local._

import benchmark.geotrellis.util._

import scala.util.Random
import scala.annotation.tailrec

import scaliper._


trait DataMapSetup { this: Benchmark =>
  val size: Int

  var bytes: Array[Byte] = null
  var ints: Array[Int] = null
  var doubles: Array[Double] = null
  var tile: Tile = null
  var bitTile: BitArrayTile = null
  var byteTile: ByteArrayTile = null
  var shortTile: ShortArrayTile = null

  override def setUp() {
    val len = size * size
    bytes = init(len)(Random.nextInt.toByte)
    ints = init(len)(Random.nextInt)
    doubles = init(len)(Random.nextDouble)
    tile = ArrayTile(init(len)(Random.nextInt), size, size)

    bitTile = BitArrayTile(init((len + 7) / 8)(Random.nextInt.toByte), size, size)
    byteTile = ByteArrayTile(bytes, size, size)
    shortTile = ShortArrayTile(init(len)(Random.nextInt.toShort), size, size)
  }
}

/** times tile loops, as well as provides grounds to test timing of various Array methods */
class DataMap extends Benchmarks {
  benchmark("Mapping over data") {
    for (s <- Array(64, 128, 256, 512, 1024, 2048, 4096)) {
      run(s"int array while loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val goal = ints.clone
            var i = 0
            val len = goal.size
            while (i < len) {
              val z = goal(i)
              if (isData(z)) goal(i) = z * 2
              i += 1
            }
            goal
          }
        }
      }
      run(s"int array while+@tailrec; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val goal = ints.clone
            val len = goal.size
            @inline @tailrec def loop(i: Int) {
              if (i < len) {
                val z = goal(i)
                if (isData(z)) goal(i) = z * 2
                loop(i + 1)
              }
            }
            loop(0)
            goal
          }
        }
      }
      run(s"double array while loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val goal = doubles.clone
            var i = 0
            val len = goal.size
            while (i < len) {
              val z = goal(i)
              if (isData(z)) goal(i) = z * 2.0
              i += 1
            }
            goal
          }
        }
      }
      run(s"int array cfor loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          import spire.syntax.cfor._
          val size = s
          def run = {
            val goal = ints.clone
            val len = goal.size
            cfor(0)(_ < len, _ + 1) { i =>
              val z = goal(i)
              if (isData(z)) goal(i) = z * 2
            }
            goal
          }
        }
      }
      run(s"tile while loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val rcopy = tile.toArrayTile
            val goal = rcopy.toArray
            var i = 0
            val len = goal.size
            while (i < len) {
              val z = goal(i)
              if (isData(z)) goal(i) = goal(i) * 2
              i += 1
            }
            rcopy
          }
        }
      }
      run(s"tile map; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = tile.map(z => if (isData(z)) z * 2 else NODATA)
        }
      }
      run(s"tile map (if set); size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = tile.mapIfSet(z => z * 2)
        }
      }
      run(s"bit tile while loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val data = bitTile
            var i = 0
            val len = data.size
            while (i < len) {
              val z = data(i)
              if (isData(z)) data(i) = data(i) * 2
              i += 1
            }
            data
          }
        }
      }
      run(s"bit tile map; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = bitTile.map(z => if (isData(z)) z * 2 else NODATA)
        }
      }
      run(s"byte tile while loop; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val data = byteTile
            var i = 0
            val len = data.size
            while (i < len) {
              val z = data(i)
              if (isData(z)) data(i) = data(i) * 2
              i += 1
            }
            data
          }
        }
      }
      run(s"byte tile map; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = byteTile.map(z => if (isData(z)) z * 2 else NODATA)
        }
      }
      run(s"short tile while; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = {
            val data = shortTile
            var i = 0
            val len = data.size
            while (i < len) {
              val z = data(i)
              if (isData(z)) data(i) = data(i) * 2
              i += 1
            }
            data
          }
        }
      }
      run(s"short tile map; size: ${s}") {
        new Benchmark with DataMapSetup {
          val size = s
          def run = shortTile.map(z => if (isData(z)) z * 2 else NODATA)
        }
      }
    }
  }
}

trait ByteBufferSetup { this: Benchmark =>
  val size: Int

  var bytes: Array[Byte] = null

  override def setUp() {
    val len = size * size
    bytes = init(len * 8)(Random.nextInt.toByte)
  }

}

class ByteBufferBenchmarks extends Benchmarks {
  import java.nio.ByteBuffer
  import spire.syntax.cfor._

  benchmark("Byte Buffer Benchmarks") {
    for (s <- Array(2048, 4096)) {
      run(s"double byte buffer bulk copy; size: ${s}") {
        new Benchmark with ByteBufferSetup {
          val size = s
          def run = {
            val doubles: Array[Double] = Array.ofDim[Double](size*size)
            val buf = ByteBuffer.wrap(bytes).asDoubleBuffer
            buf.get(doubles)
          }
        }
      }
      run(s"double byte buffer item copy; size: ${s}") {
        new Benchmark with ByteBufferSetup {
          val size = s
          def run = {
            val doubles: Array[Double] = Array.ofDim[Double](size*size)
            val buf = ByteBuffer.wrap(bytes).asDoubleBuffer
            cfor(0)(_ < size*size, _ + 1) { i =>
              doubles(i) = buf.get(i)
            }
          }
        }
      }
    }
  }
}

/** Result: Array.fill is really slow and should not be used */
class ArrayFill extends Benchmarks {
  benchmark("Fill array") {
    for (s <- Array(2048, 4096, 8192)) {
      run(s"scala array fill bytes; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.fill[Byte](size * size)(byteNODATA)
          }
        }
      }
      run(s"java array fill bytes; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.ofDim[Byte](size * size)
            java.util.Arrays.fill(arr, byteNODATA)
          }
        }
      }
      run(s"filler bytes; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            Array.ofDim[Byte](size * size).fill(byteNODATA)
          }
        }
      }
      run(s"scala array fill floats; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.fill[Float](size * size)(Float.NaN)
          }
        }
      }
      run(s"java arrays fill floats; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.ofDim[Float](size * size)
            java.util.Arrays.fill(arr, Float.NaN)
          }
        }
      }
      run(s"filler floats; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            Array.ofDim[Float](size * size).fill(Float.NaN)
          }
        }
      }
      run(s"scala array fill doubles; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.fill[Double](size * size)(Double.NaN)
          }
        }
      }
      run(s"java arrays fill doubles; size: ${s}") {
        new Benchmark {
          val size = s
          def run = {
            val arr = Array.ofDim[Double](size * size)
            java.util.Arrays.fill(arr, Double.NaN)
          }
        }
      }
    }
  }
}
