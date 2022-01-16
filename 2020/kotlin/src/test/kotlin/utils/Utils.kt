package utils

import java.io.File

fun readLines(day: String) = File("src/main/resources/$day.txt").readLines()
fun <E> List<E>.splitList(
    accumulator: List<List<E>> = emptyList(),
    predicate: (E) -> Boolean
): List<List<E>> =
    if (this.isEmpty()) accumulator
    else this.dropWhile { !predicate(it) }
        .dropWhile(predicate)
        .splitList(accumulator + listOf(takeWhile { !predicate(it) }), predicate)