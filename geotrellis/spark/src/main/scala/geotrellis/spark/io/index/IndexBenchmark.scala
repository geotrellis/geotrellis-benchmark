package benchmark.geotrellis.spark

import geotrellis.spark._
import geotrellis.spark.io.index._
import geotrellis.spark.io.index.zcurve._
import scala.util.Random
import scala.collection.mutable._
import scala.concurrent.duration._

case class Rec(bounds: KeyBounds[SpatialKey], rangeCount: Int, nanos: Long) {
  def width: Int = bounds.maxKey.col - bounds.minKey.col
  def height: Int = bounds.maxKey.row - bounds.minKey.row
  def col: Int = bounds.minKey.col
  def row: Int = bounds.maxKey.row
}

object SpatialIndexBenchmark {
  val bounds: Array[KeyBounds[SpatialKey]] = {
    for {
      i <- 0 to 1024
    } yield {
      val col = scala.util.Random.nextInt(65536)
      val row = scala.util.Random.nextInt(65536)
      val size = scala.util.Random.nextInt(1000000)    
      // val width = scala.util.Random.nextInt(size+1)
      val width = 9000 //scala.util.Random.nextInt(65536)
      val height = 9000 //scala.util.Random.nextInt(65536)
      KeyBounds(
        SpatialKey(col, row),
        SpatialKey(col+width, row+height))   
    }
  }.toArray

  def time[R](f: => R): (R, Long) = {
    val t1 = System.nanoTime
    val ret = f
    val t2 = System.nanoTime
    (ret, t2 - t1)
  }

  def benchmark(index: KeyIndex[SpatialKey]): Array[Rec] = {
    var out = new Array[Rec](bounds.length)

    for { 
      i <- 0 until bounds.length 
    } {
      val b = bounds(i)      
      val ts = for ( _ <- 1 to 3) yield {
        time { index.indexRanges(b.minKey, b.maxKey) }
      }        

      val ranges = ts.head._1
      val nanos = ts.map(_._2).min
      out(i) = Rec(b, ranges.length, nanos)
      println(out(i))
    }

    out
  }
}
    

object ZCurveBenchmark extends App {
  import java.io._

  def printToFile(fileNameString: String)(toWrite: Any): Unit = {
    val fos = new FileOutputStream(new File(fileNameString), false)
    val p = new java.io.PrintWriter(fos)
    try { p.append(s"\n$toWrite") } finally { p.close() }
  }

  def writeToFile(recs: Seq[Rec], file: String): Unit = {
    val str = recs.map { r =>
      s"${r.col},${r.row},${r.width},${r.height},${r.rangeCount},${r.nanos}" 
    }.mkString("\n")
    printToFile(file)("col,row,width,height,ranges,nanos\n" + str)
  }

  val zorder = new ZSpatialKeyIndex()
  val hilbert = {
    import geotrellis.spark.io.index.hilbert._
    val max = (math.pow(2,32)-1).toInt
    new HilbertSpatialKeyIndex(KeyBounds(SpatialKey(0,0), SpatialKey(max, max)), 31, 31)
  }
  val rowmajor = {
    import geotrellis.spark.io.index.rowmajor._
    val max = (math.pow(2,32)-1).toInt
    new RowMajorSpatialKeyIndex(KeyBounds(SpatialKey(0,0), SpatialKey(max, max)))
  }

  println("Hilbert ...")
  val hilbertBench = SpatialIndexBenchmark.benchmark(hilbert)
  writeToFile(hilbertBench, "/Users/eugene/hilbert-fixed-area-bench.csv")

  println("ZOrder ...")
  val bench = SpatialIndexBenchmark.benchmark(zorder)
  writeToFile(bench, "/Users/eugene/z-fixed-area-bench.csv")

  println("RowMajor ...")
  val rowMajorBench = SpatialIndexBenchmark.benchmark(rowmajor)
  writeToFile(rowMajorBench, "/Users/eugene/rowmajor-fixed-area-bench.csv")

}
