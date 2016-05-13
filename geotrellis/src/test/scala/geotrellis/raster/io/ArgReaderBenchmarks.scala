package benchmark.geotrellis.io

import geotrellis.raster._
import geotrellis.vector._
import geotrellis.raster.resample._
import geotrellis.raster.io._
import geotrellis.raster.mapalgebra.local._
import geotrellis.engine._
import geotrellis.engine.io._

import scaliper._

trait ArgReaderSetup { this: Benchmark =>
  val cellType: String

  val size = 256
  val layers =
    Map(
      ("bit", "wm_DevelopedLand"),
      ("byte", "SBN_car_share"),
      ("short", "travelshed-int16"),
      ("int", "travelshed-int32"),
      ("float", "aspect"),
      ("double", "aspect-double")
    )

  var path: String = ""
  var rasterExtent: RasterExtent = null
  var typ: CellType = null

  var targetExtent: RasterExtent = null

  override def setUp() {
    val id = layers(cellType)
    val layer = GeoTrellis.get(LoadRasterLayer(id)).asInstanceOf[ArgFileRasterLayer]
    path = layer.rasterPath
    typ = layer.info.cellType
    rasterExtent = layer.info.rasterExtent
    val RasterExtent(Extent(xmin, ymin, xmax, ymax), cw, ch, cols, rows) =
      rasterExtent

    val xdelta = (xmax - xmin) / 1.5
    val ydelta = (ymax - ymin) / 1.5
    val extent = Extent(xmin, ymin, xmin + xdelta, ymin + ydelta)
    targetExtent = RasterExtent(extent, size, size)
  }
}

class ArgReaderBenchmarks extends Benchmarks {
  benchmark("Reading ARGs") {
    for(ct <- Array("bit", "byte", "short", "int", "float", "double")) {
      run(s"loadRaster: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = { GeoTrellis.get(LoadRaster(layers(cellType))) }
        }
      }
      run(s"rasterSource: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = { RasterSource(layers(cellType)).get }
        }
      }
      run(s"loadRaster+Extent: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = { GeoTrellis.get(LoadRaster(layers(cellType), targetExtent)) }
        }
      }
      run(s"rasterSource+Extent: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = { RasterSource(layers(cellType), targetExtent).get }
        }
      }
      run(s"newReader: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = { arg.ArgReader.read(path, typ, rasterExtent, rasterExtent) }
        }
      }
      run(s"newReader+Extent: $ct") {
        new Benchmark with ArgReaderSetup {
          val cellType = ct
          def run = arg.ArgReader.read(path, typ, rasterExtent, targetExtent)
        }
      }
    }
  }
}

trait ReadAndResampleSetup { this: Benchmark =>
  val cellType: String
  val size: Int

  val layers =
    Map(
      ("bit", "wm_DevelopedLand"),
      ("byte", "SBN_car_share"),
      ("short", "travelshed-int16"),
      ("int", "travelshed-int32"),
      ("float", "aspect"),
      ("double", "aspect-double")
    )

  var path: String = ""
  var extent: RasterExtent = null
  var typ: CellType = null
  var targetExtent: RasterExtent = null

  override def setUp() {
    val id = layers(cellType)
    val layer = GeoTrellis.get(LoadRasterLayer(id)).asInstanceOf[ArgFileRasterLayer]
    path = layer.rasterPath
    typ = layer.info.cellType
    extent = layer.info.rasterExtent
    targetExtent = RasterExtent(extent.extent, size, size)
  }
}

class ReadARGBenchmark extends Benchmarks {
  benchmark("read and resample ARGs") {
    for (ct <- Array("bit", "byte", "short", "int", "float", "double")) {
      for (s<- Array(256, 512, 979, 1400, 2048, 4096)) {
        run(s"Read: ${ct}; ${s}") {
          new Benchmark with ReadAndResampleSetup {
            val cellType = ct
            val size = s
            def run = arg.ArgReader.read(path, typ, extent, targetExtent)
          }
        }
        run(s"Read+Resample: ${ct}; ${s}") {
          new Benchmark with ReadAndResampleSetup {
            val cellType = ct
            val size = s
            def run = {
              val r = arg.ArgReader.read(path, typ, extent, extent)
              r.resample(extent.extent, targetExtent)
            }
          }
        }
      }
    }
  }
}

trait SmallTileReadAndResampleSetup { this: Benchmark =>
  val cellType: String

  val size = 256
  val layers =
    Map(
      ("bit", "wm_DevelopedLand"),
      ("byte", "SBN_car_share"),
      ("short", "travelshed-int16"),
      ("int", "travelshed-int32"),
      ("float", "aspect"),
      ("double", "aspect-double")
    )

  var path: String = ""
  var extent: RasterExtent = null
  var typ: CellType = null
  var rasterExtent: RasterExtent = null
  var targetExtent: RasterExtent = null

  override def setUp() {
    val id = layers(cellType)

    val layer = GeoTrellis.get(LoadRasterLayer(id)).asInstanceOf[ArgFileRasterLayer]
    path = layer.rasterPath
    typ = layer.info.cellType
    rasterExtent = layer.info.rasterExtent
    val RasterExtent(Extent(xmin, ymin, xmax, ymax), cw, ch, cols, rows) =
      rasterExtent

    val extent = Extent(xmin, ymin, (xmin + xmax) / 2.0, (ymin + ymax) / 2.0)
    targetExtent = RasterExtent(extent, size, size)
  }
}

class SmallTileARGBenchmarks extends Benchmarks {
  benchmark("read and resample w/ small tiles") {
    for (ct <- Array("bit", "byte", "short", "int", "float", "double")) {
      run(s"NewReader+Extent: $ct"){
        new Benchmark with SmallTileReadAndResampleSetup {
          val cellType: String = ct
          def run = arg.ArgReader.read(path, typ, rasterExtent, targetExtent)
        }
      }
      run(s"NewReader+Extent+Resample: $ct"){
        new Benchmark with SmallTileReadAndResampleSetup {
          val cellType: String = ct
          def run = {
            val r = arg.ArgReader.read(path, typ, rasterExtent.cols, rasterExtent.rows)
            r.resample(rasterExtent.extent, targetExtent)
          }
        }
      }
    }
  }
}

/** Reading the same raster as a .tif (with GeoTools) and as an ARG with GeoTrellis */
class GeoTiffVsArgSetup extends Benchmarks {
  benchmark("GeoTiff vs ARG read") {
    run("ARG") {
      new Benchmark {
        def run = RasterSource("aspect").get
      }
    }
    run("GeoTiff") {
      new Benchmark {
        def run = RasterSource("aspect-tif").get
      }
    }
  }
}

trait TileIOSetup { this: Benchmark =>
  var targetExtent: RasterExtent = null

  override def setUp() {
    val info = RasterSource("mtsthelens_tiled").info.get
    val re = info.rasterExtent
    val Extent(xmin, _, _, ymax) = re.extent
    val te = Extent(xmin, xmin + (re.extent.width / 2.0), ymax - (re.extent.height / 2.0), ymax)
    targetExtent = RasterExtent(te, re.cols / 2, re.rows / 2)
  }
}

class TileIOBenchmarks extends Benchmarks {
  benchmark("Tile IO") {
    run("LoadRaster"){
      new Benchmark with TileIOSetup {
        def run = GeoTrellis.get(LoadRaster("mtsthelens_tiled"))
      }
    }
    run("RasterSource"){
      new Benchmark with TileIOSetup {
        def run = RasterSource("mtsthelens_tiled").get
      }
    }
    run("LoadRaster+Extent"){
      new Benchmark with TileIOSetup {
        def run = GeoTrellis.get(LoadRaster("mtsthelens_tiled", targetExtent))
      }
    }
    run("RasterSource+Extent"){
      new Benchmark with TileIOSetup {
        def run = RasterSource("mtsthelens_tiled", targetExtent).get
      }
    }
  }
}

