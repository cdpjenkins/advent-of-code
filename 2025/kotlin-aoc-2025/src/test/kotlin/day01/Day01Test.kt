package day01

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.math.abs

private fun part1(input: List<String>) =
    input.parseToRotationInstructions()
        .runningFold(50) { acc, move -> acc + move }
        .count { it.isAtZero() }

private fun part2(input: List<String>) =
    input.parseToRotationInstructions()
        .flatMap { it.toListOfSingleRotationsOneAtATime() }
        .runningFold(50) { acc, move -> acc + move }
        .count { it.isAtZero() }

private fun Int.isAtZero(): Boolean = this.mod(100) == 0

private fun Int.toListOfSingleRotationsOneAtATime(): List<Int> =
    when {
        this < 0 -> List(abs(this)) { -1 }
        else -> List(this) { 1 }
    }

private fun List<String>.parseToRotationInstructions(): List<Int> = map { it.parseToRotationInstruction() }

private fun String.parseToRotationInstruction(): Int {
    val (direction, distanceString) = parseUsingRegex("""([LR])(\d+)""")
    val distance = distanceString.toInt()

    return when (direction) {
        "L" -> -distance
        "R" -> distance
        else -> throw IllegalArgumentException("Invalid direction: ${direction}")
    }
}

class Day01Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 3
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day01.txt")) shouldBe 1084
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 6
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day01.txt")) shouldBe 6475
    }

    @Test
    fun `mod function works as expected`() {
        100.mod(100) shouldBe 0
        (-1).mod(100) shouldBe 99
    }
}

val testInput =
    """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
    """.trimIndent().lines()
