package utils

object ListUtils {
    fun List<String>.splitByBlank(): List<List<String>> =
        if (isEmpty()) {
            listOf()
        } else {
            val restInput = dropWhile { !it.isEmpty() }.drop(1)
            listOf(takeWhile { !it.isEmpty() }) + restInput.splitByBlank()
        }

    fun parseSpaceSeparatedNumberList(it: String): List<Long> = it.split(" ").map { it.toLong() }

    fun String.toIntList(separator: String = ","): List<Int> = this.split(separator).map { it.toInt() }
}