# geotrellis-benchmark
Projects for benchmarking GeoTrellis.

## Deprecated
This project is deprecated in favor of using [`sbt-jmh`](https://github.com/ktoso/sbt-jmh) in [`geotrellis-bench`](https://github.com/locationtech/geotrellis/tree/master/bench) subproject.

## Running

There are multiple versions of GeoTrellis that can be benchmarked here. For instance, to benchmark the Convolve
operation in `geotrellis-0.9.2` against `geotrellis-0.10.0-SNAPSHOT`, make sure the appropriate versions are set in 
the subprojects, and then run:

```console
./sbt "project geotrellis09-benchmark" "test-only *Convolve*"
./sbt "project geotrellis-raster-benchmark" "test-only *Convolve*"
```
