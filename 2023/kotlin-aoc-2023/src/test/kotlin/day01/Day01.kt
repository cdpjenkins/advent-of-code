package day01

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day01 {
    @Test
    internal fun `part 1 sample input`() {
        day1Part1SumFirstAndLastDigits(testInput) shouldBe 142
    }

    @Test
    internal fun `part 1 real input`() {
        day1Part1SumFirstAndLastDigits(readInputFileToList("day01.txt")) shouldBe 54951
    }

    @Test
    internal fun `part 2 sample input`() {
        day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(testInput2) shouldBe 281
    }

    @Test
    internal fun `part 2 real input`() {
        day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(readInputFileToList("day01.txt")) shouldBe 55218
    }

    @Test
    fun `correctly maps overlapping numbers`() {
        "ppkeightwo5ggthreet".substituteDigitsForWords() shouldBe "ppk825gg3t"
        "eightwothree".substituteDigitsForWords() shouldBe "823"
    }
}

// What a massive hack!
private fun String.substituteDigitsForWords() =
    replace("oneight", "18")
        .replace("twone", "21")
        .replace("threeight", "38")
        .replace("fiveeight", "58")
        .replace("sevenine", "79")
        .replace("eightwo", "82")
        .replace("eighthree", "83")
        .replace("nineight", "98")
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")

private fun day1Part1SumFirstAndLastDigits(input: List<String>) =
    input.map { line ->
        line.concatenateFirstAndLastDigitsAndConvertToInt()
    }.sum()

private fun day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(input: List<String>) =
    input.map {
        it.substituteDigitsForWords().concatenateFirstAndLastDigitsAndConvertToInt()
    }.sum()

private fun String.concatenateFirstAndLastDigitsAndConvertToInt(): Int {
    val digits = filter { it.isDigit() }
    return "${digits.first()}${digits.last()}".toInt()
}

val testInput =
    """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().lines()

val testInput2 =
    """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().lines()