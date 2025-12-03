package day03

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.collections.sumOf

private fun part1(input: List<String>): Long = input.sumOf { it.maxJoltageOfLength(2) }
private fun part2(input: List<String>): Long = input.sumOf { it.maxJoltageOfLength(12) }

private fun String.maxJoltageOfLength(n: Int) = maxJoltageStringOfLength(n).toLong()
private fun String.maxJoltageStringOfLength(n: Int): String =
    when (n) {
        0 -> ""
        else -> {
            val maxDigit = this.dropLast(n - 1).max()
            "${maxDigit}" + this.dropWhile { it != maxDigit }.drop(1).maxJoltageStringOfLength(n - 1)
        }
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

    @Test
    fun `max joltage string of size 0 is empty string`() {
        "1234".maxJoltageStringOfLength(0) shouldBe ""
    }

    @Test
    fun `max joltage of length 1 in string of length 1 should be formed by the single digit in that string`() {
        "5".maxJoltageStringOfLength(1) shouldBe "5"
    }

    @Test
    fun `max joltage of length 1 in a string with multiple chars should be formed by the largest digit in that string`() {
        "13579864".maxJoltageStringOfLength(1) shouldBe "9"
    }

    @Test
    fun `max joltage of length 2 in a string with 2 digits should be formed by those two digits`() {
        "56".maxJoltageStringOfLength(2) shouldBe "56"
    }

    @Test
    fun `max joltage of length 2 from a string with 3 digits should be formed by finding the largest possible joltage`() {
        "123".maxJoltageStringOfLength(2) shouldBe "23"
    }

    @ParameterizedTest
    @CsvSource(
        "987654321111111, 987654321111",
        "811111111111119, 811111111119",
        "234234234234278, 434234234278",
        "818181911112111, 888911112111"
    )
    fun `max joltage for sample values should be correct`(battery: String, expectedMaxJoltage: String) {
        battery.maxJoltageStringOfLength(12) shouldBe expectedMaxJoltage
    }
}

val testInput =
    """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent().lines()
