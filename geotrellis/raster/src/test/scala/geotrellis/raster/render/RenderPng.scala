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

class RenderBenchmark extends Benchmarks with ConsoleReport {
  benchmark("Rendering Benchmarks") {
    run("PNG Rendering") {
      new Benchmark {
        var tile: Tile = _
        var classifier: StrictIntColorClassifier = _

        override def setUp() = {
          tile = IntArrayTile(1 to 256 * 256 toArray, 256, 256)
          val histogram = FastMapHistogram.fromTile(tile)
          classifier = StrictColorClassifier
            .fromQuantileBreaks(histogram, Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000).map(RGBA(_)))
        }

        def run() = tile.renderPng(classifier)
      }
    }

    run("JPG Rendering") {
      new Benchmark {
        var tile: Tile = _
        var classifier: StrictIntColorClassifier = _

        override def setUp() = {
          tile = IntArrayTile(1 to 256 * 256 toArray, 256, 256)
          val histogram = FastMapHistogram.fromTile(tile)
          classifier = StrictColorClassifier
            .fromQuantileBreaks(histogram, Array(0x0000FF, 0x0080FF, 0x00FF80, 0xFFFF00, 0xFF8000).map(RGBA(_)))
        }

        def run() = tile.renderJpg(classifier)
      }
    }
  }
}
