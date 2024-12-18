package day07

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 3749
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day07.txt")) shouldBe 7710205485870L
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 11387L
    }

//    @Ignore // bit too slow and inefficient to run every time
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day07.txt")) shouldBe 20928985450275L
    }
}

val testInput =
    """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()
