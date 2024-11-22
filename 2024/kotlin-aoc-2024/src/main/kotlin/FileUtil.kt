import java.io.File

object FileUtil {
    fun readInputFileToList(filename: String): List<String> {
        return File("../../advent-of-code-input/2024/$filename").readLines()
    }
}
