package day07

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val (startX, _) = input.first().withIndex().find { (_, c) -> c == 'S' }!!

    var xs = setOf(startX)
    var splits = 0
    for (y in 1..<input.size) {
        val thisLine = input[y]

        val splitterXs = thisLine.withIndex().filter { (_, c) -> c == '^' }.map { it.index }.toSet()

        val newXs = xs.flatMap {
            if (it in splitterXs) {
                splits++
                setOf(it - 1, it + 1)
            } else {
                setOf(it)
            }
        }
        xs = newXs.toSet()
    }

    return splits
}

private fun part2(input: List<String>): Long {
    val (startX, _) = input.first().withIndex().find { (_, c) -> c == 'S' }!!

    var xs = mapOf(startX to 1L)
    for (y in 1..<input.size) {
        val thisLine = input[y]

        val splitterXs = thisLine.withIndex().filter { (_, c) -> c == '^' }.map { it.index }

        val newXs = xs.flatMap { (x, n) ->
            if (x in splitterXs) {
                listOf((x - 1) to n, (x + 1) to n)
            } else {
                listOf(x to n)
            }
        }

        xs = combineBeamMaps(newXs)
    }

    return xs.values.sum()
}

private fun combineBeamMaps(newXs: List<Pair<Int, Long>>): MutableMap<Int, Long> {
    val combined: MutableMap<Int, Long> = mutableMapOf()
    newXs.forEach { (x, n) ->
        combined[x] = combined.getOrDefault(x, 0) + n
    }
    return combined
}

class Day07Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 21
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day07.txt")) shouldBe 1560
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 40L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day07.txt")) shouldBe 25592971184998L
    }
}

val testInput =
    """
        .......S.......
        ...............
        .......^.......
        ...............
        ......^.^......
        ...............
        .....^.^.^.....
        ...............
        ....^.^...^....
        ...............
        ...^.^...^.^...
        ...............
        ..^...^.....^..
        ...............
        .^.^.^.^.^...^.
        ...............
    """.trimIndent().lines()
