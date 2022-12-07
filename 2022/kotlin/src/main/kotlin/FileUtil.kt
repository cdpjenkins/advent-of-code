import java.io.File

object FileUtil {
    fun readInputFileToList(filename: String) = File("src/main/resources/$filename").readLines()
}
