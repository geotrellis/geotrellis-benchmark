/*
 * Copyright (c) 2014 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package benchmark.geotrellis.raster.render

import geotrellis.raster._
import geotrellis.raster.histogram._
import geotrellis.raster.render._
import geotrellis.raster.render.png._

import scaliper._
import java.io._


class RenderBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Rendering/Encoding Benchmarks") {
    run("Rendering") {
      new Benchmark {
        var tile: Tile = _
        var cmap: ColorMap = _

        override def setUp() = {
          tile = IntArrayTile(1 to 2560 * 2560 toArray, 2560, 2560)
          var histogram = FastMapHistogram.fromTile(tile)
          val scc = StrictColorClassifier
            .fromQuantileBreaks(histogram, Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000).map(RGBA(_)))
          cmap = scc.toColorMap()
          var colorEncoding = PngColorEncoding(scc.getColors, scc.getNoDataColor)
        }

        def run() = {
          cmap.render(tile).convert(TypeByte)
        }
      }
    }

    run("PNG Encoding") {
      new Benchmark {
        var r2: Tile = _
        var colorEncoding: PngColorEncoding = _

        override def setUp() = {
          val tile = IntArrayTile(1 to 2560 * 2560 toArray, 2560, 2560)
          var histogram = FastMapHistogram.fromTile(tile)
          val scc = StrictColorClassifier
            .fromQuantileBreaks(histogram, Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000).map(RGBA(_)))
          val cmap = scc.toColorMap()
          colorEncoding = PngColorEncoding(scc.getColors, scc.getNoDataColor)
          r2 = cmap.render(tile).convert(TypeByte)
        }

        def run() = {
          new PngEncoder(Settings(colorEncoding, PaethFilter)).writeByteArray(r2)
        }
      }
    }

    run("Rendering and PNG Encoding") {
      new Benchmark {
        var tile: Tile = _
        var colorEncoding: PngColorEncoding = _
        var cmap: ColorMap = _

        override def setUp() = {
          tile = IntArrayTile(1 to 2560 * 2560 toArray, 2560, 2560)
          var histogram = FastMapHistogram.fromTile(tile)
          val scc = StrictColorClassifier
            .fromQuantileBreaks(histogram, Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000).map(RGBA(_)))
          cmap = scc.toColorMap()
          colorEncoding = PngColorEncoding(scc.getColors, scc.getNoDataColor)
        }

        def run() = {
          val r2 = cmap.render(tile).convert(TypeByte)
          new PngEncoder(Settings(colorEncoding, PaethFilter)).writeByteArray(r2)
        }
      }
    }
  }
}
