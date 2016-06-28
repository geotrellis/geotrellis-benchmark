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

trait BaseRenderBenchmark extends Benchmark {
  var tile: Tile = _
  var cmap: ColorMap = _
  var colorEncoding: PngColorEncoding = _

  def setup(size: Int, cmapSize: Int): Unit = {
    tile = IntArrayTile(1 to size * size toArray, size, size)
    val step = (size * size) / cmapSize
    val colors = Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000)
    val map =
      (for(x <- 0 until cmapSize) yield {
        (x * step -> colors(x % colors.size))
      }).toMap
    cmap =
      ColorMap(map)

    colorEncoding =
      PngColorEncoding(cmap.colors, cmap.options.noDataColor, cmap.options.fallbackColor)
  }

}

class RenderBenchmarks extends Benchmarks with ConsoleReport {
  benchmark("Rendering/Encoding Benchmarks") {
    val size = 2560 / 2
    val cmapSize = 50

    run("Rendering") {
      new BaseRenderBenchmark {
        override def setUp() = {
          setup(size, cmapSize)
        }

        def run() = {
          cmap.render(tile)
        }
      }
    }

    run("PNG Encoding") {
      new BaseRenderBenchmark {
        override def setUp() = {
          setup(size, cmapSize)
        }

        def run() = {
          new PngEncoder(Settings(colorEncoding, PaethFilter)).writeByteArray(tile)
        }
      }
    }

    run("Rendering and PNG Encoding") {
      new BaseRenderBenchmark {
        override def setUp() = {
          setup(size, cmapSize)
        }

        def run() = {
          val r2 = cmap.render(tile)
          new PngEncoder(Settings(colorEncoding, PaethFilter)).writeByteArray(r2)
        }
      }
    }
  }
}
