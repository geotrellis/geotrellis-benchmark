package benchmark.geotrellis.geotools

import benchmark.BenchmarkUtil

import geotrellis.geotools._
import geotrellis.raster._
import org.geotools.coverage.grid._
import org.geotools.gce.geotiff._
import org.geotools.coverage.grid.io._
import scaliper._

import java.awt.image.DataBuffer
import scala.collection.JavaConverters._

object GridCoverage2DConvertersBenchmark {
  def getImage(path: String): GridCoverage2D = {
    val gridSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue
    val file = new java.io.File(path)
    val preImage = new GeoTiffReader(file).read(null).getRenderedImage
    val height = preImage.getHeight
    val width = preImage.getWidth
    val reader = new GeoTiffReader(file)

    gridSize.setValue(s"$width,$height")

    val image = reader.read(Array(gridSize))

    image
  }
}

class GridCoverage2DConvertersBenchmark extends Benchmarks with ConsoleReport {
  import GridCoverage2DConvertersBenchmark._
  val paths =
    List(
      BenchmarkUtil.dataPath("geotiff/SBN_inc_percap.tif"),
      BenchmarkUtil.dataPath("geotiff/elevation-comp.tif"),
      BenchmarkUtil.dataPath("geotiff/r-nir-wm-clipped.tif")
    )

  for(path <- paths) {
    benchmark(s"Converting a GeoTools GridCoverage2D to a GeoTrellis type: ${new java.io.File(path).getName}") {
      run("Foreach over GeoToolsTile") {
        new Benchmark {
          val p = path
          var gridCoverage: GridCoverage2D = _

          override def setUp(): Unit = {
            gridCoverage = getImage(p)
          }

          def run(): Unit = {
            val Raster(tile, extent) = GridCoverage2DToRaster(gridCoverage).head
            var s = 0
            tile.foreach { z => if(isData(z)) s += z }
          }
        }
      }

      run("Foreach over GeoToolsTile Refactor") {
        new Benchmark {
          val p = path
          var gridCoverage: GridCoverage2D = _

          override def setUp(): Unit = {
            gridCoverage = getImage(p)
          }

          def run(): Unit = {
            val Raster(tile, extent) = gridCoverage.toRaster(0)
            var s = 0
            tile.foreach { z => if(isData(z)) s += z }
          }
        }
      }
    }
  }
}
