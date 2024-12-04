import java.io.File

object FileUtil {
    fun readInputFileToList(filename: String): List<String> {
        return File("../../advent-of-code-input/2024/$filename").readLines()
    }

    fun readInputFileToString(filename: String): String {
        return File("../../advent-of-code-input/2024/$filename").readText()
    }
}
