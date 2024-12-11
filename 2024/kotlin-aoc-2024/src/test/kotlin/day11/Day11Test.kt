package day11

import FileUtil.readInputFileToString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 55312
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day11.txt")) shouldBe 216996
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 65601038650482
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day11.txt")) shouldBe 257335372288947L
    }
}

val testInput = "125 17"
