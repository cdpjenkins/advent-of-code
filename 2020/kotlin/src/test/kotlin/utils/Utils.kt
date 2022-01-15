package utils

import java.io.File

fun readLines(day: String) = File("src/main/resources/$day.txt").readLines()
