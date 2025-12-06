package day06

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Long {

    val mat = input.map { it.trim().split(" +".toRegex()) }


    val transposed = mat.transpose()


    return transposed.sumOf { it.evaluate() }

}

private fun List<String>.evaluate(): Long {
    val operator = this.last()
    val operands = this.dropLast(1).map { it.toLong() }

    return when (operator) {
        "+" -> operands.sum()
        "*" -> operands.fold(1L) { acc, x -> acc * x }
        else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
}

private fun List<List<String>>.transpose(): List<List<String>> {

    val len = this.first().size
    require(this.all { it.size == len })

    val transed = (0..<len).map { x ->
        (0..<this.size).map { y ->
            this[y][x]
        }
    }

    return transed

}

private fun part2(input: List<String>): Int {
    return 123
}

class Day06Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 4277556L
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day06.txt")) shouldBe 5060053676136L
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 3263827
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }
}

val testInput =
    """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +
    """.trimIndent().lines()
