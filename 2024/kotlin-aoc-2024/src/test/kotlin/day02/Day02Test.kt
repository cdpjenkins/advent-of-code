package day02

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 2
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day02.txt")) shouldBe 472
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 4
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day02.txt")) shouldBe 520
    }
}

val testInput =
    """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()
