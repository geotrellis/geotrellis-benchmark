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

import geotrellis._
import geotrellis.statistics._
import geotrellis.render._
import geotrellis.render.png._

import scaliper._

class RenderBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Rendering Benchmarks") {
    run("PNG Rendering") {
      new Benchmark {
        var raster: Raster = _
        var renderer: Renderer = _

        override def setUp() = {
          val extent = Extent(1, 1, 1, 1)
          val re = RasterExtent(extent, 1.0, 1.0, 256, 256)
          val histogram = FastMapHistogram.fromRaster(raster)
          val colors = Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000)

          raster = Raster(1 to 256 * 256 toArray, re)
          renderer = Renderer(histogram.getQuantileBreaks(colors.length), colors, 0x00000000, None)
        }

        def run() = {
          renderer.render(raster)
          ()
        }
      }
    }
  }
}
