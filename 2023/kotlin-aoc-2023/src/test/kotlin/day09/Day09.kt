package day09

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import kotlin.test.Test

private fun part1(input: List<String>): Int =
    input.map { it.parseSeq() }
        .map { it.extrapolateNextValue() }
        .sum()

private fun part2(input: List<String>): Int =
    input.map { it.parseSeq() }
        .map { it.extrapolatePreviousValue() }
        .sum()

private fun String.parseSeq(): List<Int> = split(" ").map { it.toInt() }

private fun List<Int>.extrapolateNextValue(): Int =
    if (this.allEntriesAreEqual()) {
        this.first()
    } else {
        this.last() + computeDifferences().extrapolateNextValue()
    }

private fun List<Int>.extrapolatePreviousValue(): Int =
    if (this.allEntriesAreEqual()) {
        this.first()
    } else {
        this.first() - computeDifferences().extrapolatePreviousValue()
    }

private fun List<Int>.allEntriesAreEqual(): Boolean = this.all { it == this.first()}

private fun List<Int>.computeDifferences(): List<Int> =
    windowed(2)
        .map { (i, j) -> j - i }

class Day09Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 114
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day09.txt")) shouldBe 1637452029
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 2
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day09.txt")) shouldBe 908
    }

    @Test
    fun `can extrapolate the next value of a constant sequence`() {
        listOf(1, 1, 1, 1, 1, 1).extrapolateNextValue() shouldBe 1
    }

    @Test
    fun `can extrapolate the next value of a list with constant differences`() {
        listOf(1, 2, 3, 4, 5, 6).extrapolateNextValue() shouldBe 7
    }

    @Test
    fun `can extrapolate the next value of a list with constant second-order differences`() {
        listOf(0, 1, 4, 9, 16, 25, 36).extrapolateNextValue() shouldBe 49
    }

    @Test
    fun `can extrapolate the next value of a list with constant third-order differences`() {
        listOf(0, 1, 8, 27, 64, 125, 216).extrapolateNextValue() shouldBe 343
    }

    @Test
    fun `can extrapolate the previous value of a constant sequence`() {
        listOf(1, 1, 1, 1, 1, 1).extrapolatePreviousValue() shouldBe 1
    }

    @Test
    fun `can extrapolate the previous value of a list with constant differences`() {
        listOf(2, 3, 4, 5, 6).extrapolatePreviousValue() shouldBe 1
    }

    @Test
    fun `can extrapolate the previous value of a list with constant second-order differences`() {
        listOf(1, 4, 9, 16, 25, 36, 49).extrapolatePreviousValue() shouldBe 0
    }

    @Test
    fun `can extrapolate the previous value of a list with constant third-order differences`() {
        listOf(27, 64, 125, 216, 343).extrapolatePreviousValue() shouldBe 8
    }

    @Test
    fun `can compute differences of elements in a list`() {
        listOf(1, 2, 3, 4, 5, 6).computeDifferences() shouldBe listOf(1, 1, 1, 1, 1)
        listOf(0, 1, 4, 9, 16, 25, 36).computeDifferences() shouldBe listOf(1, 3, 5, 7, 9, 11)
    }
}

val testInput =
    """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent().lines()
