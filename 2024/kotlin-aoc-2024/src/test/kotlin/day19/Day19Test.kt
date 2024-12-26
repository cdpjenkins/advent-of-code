package day19

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {

    val (availableTowelsLines, designs) = input.splitByBlank()

    val availableTowels = availableTowelsLines[0].split(", ")

    val towelsRegex = ("(" + availableTowels.joinToString("|") + ")+").toRegex()

    return designs.count { towelsRegex.matches(it) }
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day19Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 6
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day19.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
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
