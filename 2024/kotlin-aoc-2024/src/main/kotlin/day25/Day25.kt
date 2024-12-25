package day25

import utils.ListUtils.splitByBlank

fun part1(input: List<String>): Int {
    val (locks, keys) = input.parse()

    return locks.sumOf { lock ->
        keys.count { it.fitsIn(lock) }
    }
}

fun List<String>.isLock() = this.first().all { it == '#' }
fun List<String>.isKey() = this.last().all { it == '#' }
fun List<Int>.fitsIn(lock: List<Int>) = zip(lock).all { (a, b) -> a + b <= 5 }

private fun List<String>.parse(): Pair<List<List<Int>>, List<List<Int>>> {
    val sections = splitByBlank()

    return Pair(
        sections.filter { it.isLock() }.map { it.countHeights() },
        sections.filter { it.isKey() }.map { it.countHeights() }
    )
}

private fun List<String>.countHeights() =
    (0..<5).map { x ->
        (0..<7).count { y -> this[y][x] == '#' } - 1
    }
