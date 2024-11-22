package day01

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    return 123
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day01Test {
    @Ignore
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day_template.txt")) shouldBe -1
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



    """.trimIndent().lines()
