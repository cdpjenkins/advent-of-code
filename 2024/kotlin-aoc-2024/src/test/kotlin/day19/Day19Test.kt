package day19

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

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
