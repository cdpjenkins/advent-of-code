import java.io.File

object FileUtil {
    fun readInputFileToList(filename: String): List<String> {
        return File("../../advent-of-code-input/2022/$filename").readLines()
    }
}
