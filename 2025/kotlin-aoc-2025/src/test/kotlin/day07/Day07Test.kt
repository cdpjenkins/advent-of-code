package day07

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {

    var beams = setOf(input.beamStart())
    var splits = 0
    input.forEach { line ->
        val splitters = line.splitters()

        val newXs = beams.flatMap {
            if (it in splitters) {
                splits++
                setOf(it - 1, it + 1)
            } else {
                setOf(it)
            }
        }
        beams = newXs.toSet()
    }

    return splits
}

private fun part2(input: List<String>): Long {
    val finalBeamSuperpositions =
        input.fold(mapOf(input.beamStart() to 1L)) { beamSuperpositions, inputLine ->
            val splitters = inputLine.splitters()
            beamSuperpositions.flatMap { (x, n) ->
                when (x) {
                    in splitters -> listOf((x - 1) to n, (x + 1) to n)
                    else -> listOf(x to n)
                }
            }.combineBeamMaps()
        }

    return finalBeamSuperpositions.values.sum()
}

private fun List<String>.beamStart() = this.first().withIndex().find { (_, c) -> c == 'S' }!!.index
private fun String.splitters() = withIndex().filter { (_, c) -> c == '^' }.map { it.index }.toSet()

private fun List<Pair<Int, Long>>.combineBeamMaps() =
    groupBy({ it.first }, { it.second })
        .mapValues { it.value.sum() }

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
