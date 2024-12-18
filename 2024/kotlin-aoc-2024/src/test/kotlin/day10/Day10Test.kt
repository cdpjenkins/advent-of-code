package day10

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 36
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day10.txt")) shouldBe 587
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 81
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day10.txt")) shouldBe 1340
    }
}

val testInput =
    """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()
