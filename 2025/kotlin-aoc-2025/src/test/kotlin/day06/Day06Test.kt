package day06

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank

private fun part1(input: List<String>): Long {
    val mat = input.map { it.trim().split(" +".toRegex()) }
    val transposed = mat.transpose()
    return transposed.sumOf { it.evaluate() }
}

private fun part2(input: List<String>): Long {

    val tr = input.transposeStringList()


    val expressionsStrings = tr.map { it.trim() }.splitByBlank()
    val ston = expressionsStrings.map {
        val first = it.first()

        val operator = first.last()
        val stons = listOf(it.first().dropLast(1).trim()) + it.drop(1)

        val longs = stons.map { it.toLong() }

        evaluate("$operator", longs)
    }

    return ston.sum()

}

private fun List<String>.transposeStringList(): List<String> {
    val len = this.first().length
    require(this.all { it.length == len })

    val transed = (0..<len).map { x ->
        (0..<this.size).map { y ->
            this[y][x]
        }.joinToString("")
    }

    return transed

}

private fun List<String>.evaluate(): Long {
    val operator = this.last()
    val operands = this.dropLast(1).map { it.toLong() }

    return evaluate(operator, operands)
}

private fun evaluate(operator: String, operands: List<Long>): Long {
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


class Day06Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 4277556L
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day06.txt")) shouldBe 5060053676136L
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 3263827
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day06.txt")) shouldBe 9695042567249L
    }
}

val testInput =
    """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
    """.trimIndent().lines()
