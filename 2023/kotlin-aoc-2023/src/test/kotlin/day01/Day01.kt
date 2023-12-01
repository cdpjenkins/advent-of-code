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
    internal fun `part 2 with test input - the possibly less hacky way`() {
        part2ThePossiblyLessHackyWay(testInput2) shouldBe 281
    }

    @Test
    internal fun `part 2 with real input - the possibly less hacky way`() {
        part2ThePossiblyLessHackyWay(readInputFileToList("day01.txt")) shouldBe 55218
    }

    @Test
    fun `correctly maps overlapping numbers`() {
        "ppkeightwo5ggthreet".substituteDigitsForWords() shouldBe "ppk825gg3t"
        "eightwothree".substituteDigitsForWords() shouldBe "823"
    }

    @Test
    fun `creates correct digit sequence`() {
        digitSequence("1abc2").toList() shouldBe listOf(1, 2)

        digitSequence("two1nine").toList() shouldBe listOf(2, 1, 9)
        digitSequence("eightwothree").toList() shouldBe listOf(8, 2, 3)
        digitSequence("abcone2threexyz").toList() shouldBe listOf(1, 2, 3)
        digitSequence("xtwone3four").toList() shouldBe listOf(2, 1, 3, 4)
        digitSequence("4nineeightseven2").toList() shouldBe listOf(4, 9, 8, 7, 2)
        digitSequence("zoneight234").toList() shouldBe listOf(1, 8, 2, 3, 4)
        digitSequence("7pqrstsixteen").toList() shouldBe listOf(7, 6)
    }
}

private fun day1Part1SumFirstAndLastDigits(input: List<String>) =
    input.map { line ->
        line.concatenateFirstAndLastDigitsAndConvertToInt()
    }.sum()

private fun day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(input: List<String>) =
    input.map {
        it.substituteDigitsForWords().concatenateFirstAndLastDigitsAndConvertToInt()
    }.sum()

private fun part2ThePossiblyLessHackyWay(input: List<String>) =
    input.map { line ->
        val digits = digitSequence(line)
        "${digits.first()}${digits.last()}".toInt()
    }.sum()

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

private fun digitSequence(line: String): Sequence<Int> {
    return sequence {
        var remainingLine = line

        while (remainingLine.isNotEmpty()) {
            val maybeDigit = maybeDigit(remainingLine)
            if (maybeDigit != null) {
                yield(maybeDigit)
            }
            remainingLine = remainingLine.drop(1)
        }
    }
}

val digitsMap = mapOf(
    "0" to 0,
    "1" to 1,
    "one" to 1,
    "2" to 2,
    "two" to 2,
    "3" to 3,
    "three" to 3,
    "4" to 4,
    "four" to 4,
    "5" to 5,
    "five" to 5,
    "6" to 6,
    "six" to 6,
    "7" to 7,
    "seven" to 7,
    "8" to 8,
    "eight" to 8,
    "9" to 9,
    "nine" to 9
)

private fun maybeDigit(s: String): Int? = digitsMap.entries.firstOrNull() { s.startsWith(it.key) }?.value

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