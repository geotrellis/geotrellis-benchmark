package benchmark

import java.io.File

object BenchmarkUtil {
  def dataPath(subPath: String): String = {
    new File("data", subPath).getAbsolutePath
  }
}
