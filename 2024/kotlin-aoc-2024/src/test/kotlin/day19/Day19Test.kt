package day19

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank

private fun part1(input: List<String>): Int {
    val (availableTowelsLines, designs) = input.splitByBlank()
    val availableTowels = availableTowelsLines[0].split(", ")

    val towelsRegex = ("(" + availableTowels.joinToString("|") + ")+").toRegex()

    return designs.count { towelsRegex.matches(it) }
}

private fun part2(input: List<String>): Long {
    val (availableTowelsLines, designs) = input.splitByBlank()
    val availableTowels = availableTowelsLines[0].split(", ")

    return solveUsingDFS(designs, availableTowels)
}

private fun solveUsingDFS(
    designs: List<String>,
    availableTowels: List<String>
): Long {
    val cache = mutableMapOf<String, Long>()

    fun towelCombinations(design: String, availableTowels: List<String>): Long {
        if (design in cache) {
            return cache[design]!!
        }
        if (design.isEmpty()) {
            return 1
        } else {
            val applicableTowels = availableTowels.filter { design.startsWith(it) }
            val thisResult = applicableTowels.map {
                towelCombinations(design.removePrefix(it), availableTowels)
            }.sum()

            cache[design] = thisResult

            return thisResult
        }
    }

    val result = designs.sumOf { towelCombinations(it, availableTowels) }
    return result
}



class Day19Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 6
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day19.txt")) shouldBe 374
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 16
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day19.txt")) shouldBe 1100663950563322
    }
}

val testInput =
    """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()
