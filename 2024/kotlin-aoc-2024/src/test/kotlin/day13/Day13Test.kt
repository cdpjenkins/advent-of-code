package day13

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 480
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day13.txt")) shouldBe 31552
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 875318608908L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day13.txt")) shouldBe 95273925552482L
    }
}

val testInput =
    """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279

    """.trimIndent().lines()
