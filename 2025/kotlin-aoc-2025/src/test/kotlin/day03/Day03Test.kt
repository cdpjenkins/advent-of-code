package day03

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {

    val joltages = input.map { line ->
        val indexedLine = line.withIndex().toList()
        val allButLast = indexedLine.dropLast(1)
        val (maxIndex, firstDigit) = allButLast.maxBy { (_, c) -> c }

        val (_, secondDigit) = indexedLine.drop(maxIndex + 1).maxBy { (_, c) -> c }

        "${firstDigit}${secondDigit}".toInt()
    }

    return joltages.sum()
}

private fun part2(input: List<String>): Long {
    val joltages = input.map { line ->
        sequence {
            var remainingChars = line

            for (n in 12 downTo 1) {
                val indexedChars = remainingChars.withIndex().toList()
                val allButLast = indexedChars.dropLast(n - 1)
                val (maxIndex, firstDigit) = allButLast.maxBy { (_, c) -> c }
                remainingChars = remainingChars.drop(maxIndex + 1)

                yield(firstDigit)
            }
        }.joinToString("").toLong()
    }

    return joltages.sum()
}

class Day03Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 357
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day03.txt")) shouldBe 17766
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 3121910778619
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day03.txt")) shouldBe 176582889354075
    }
}

val testInput =
    """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent().lines()
