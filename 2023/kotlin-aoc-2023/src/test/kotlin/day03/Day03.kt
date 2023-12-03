package day03

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

val partNumberRegex = "(\\d+)".toRegex()
val symbolRegex = "([^\\d\\s.])".toRegex()

private fun part1(testInput: List<String>): Int {
    val (_, partNumbers) = parseInput(testInput)

    return partNumbers.sumOf { it.partNumber }
}

private fun part2(testInput: List<String>): Int {
    val (symbols, partNumbers) = parseInput(testInput)

    return symbols.sumOf { it.gearRatio(partNumbers) }
}

private fun parseInput(testInput: List<String>): Pair<List<Symbol>, List<PartNumber>> {
    val symbols = testInput.withIndex()
        .flatMap { (y, line) -> parseSymbols(y, line) }
    val partNumbers = testInput.withIndex()
            .flatMap { (y, line) -> parseNumbers(y, line) }
            .filter { it.isAdjacentToAnyOf(symbols) }

    return Pair(symbols, partNumbers)
}

fun parseSymbols(y: Int, line: String) =
    symbolRegex
        .findAll(line)
        .map { Symbol(it.value, it.range.start, y) }

fun parseNumbers(y: Int, line: String) =
    partNumberRegex
        .findAll(line)
        .map { PartNumber(it.value.toInt(), y, it.range.start, it.range.endInclusive) }

data class PartNumber(
    val partNumber: Int,
    val y: Int,
    val start: Int,
    val end: Int
) {
    fun isAdjacentToAnyOf(symbols: List<Symbol>) = symbols.any { isAdjacentTo(it) }

    fun isAdjacentTo(symbol: Symbol) =
        symbol.x in (start - 1)..(end + 1) &&
        symbol.y in (y-1)..(y+1)
}

data class Symbol(
    val value: String,
    val x: Int,
    val y: Int
) {
    fun gearRatio(partNumbers: List<PartNumber>): Int =
        partNumbers.filter { it.isAdjacentTo(this) }.let {
            if (value == "*" && it.size == 2) {
                it[0].partNumber * it[1].partNumber
            } else {
                0
            }
        }
}

class Day03Test {
    @Test
    internal fun `part 1 sample input`() {
        part1(testInput) shouldBe 4361
    }

    @Test
    internal fun `part 1 real input`() {
        part1(readInputFileToList("day03.txt")) shouldBe 546312
    }

    @Test
    internal fun `part 2 sample input`() {
        part2(testInput) shouldBe 467835
    }

    @Test
    internal fun `part 2 real input`() {
        part2(readInputFileToList("day03.txt")) shouldBe 87449461
    }

    @Test
    fun `non-adjacent symbol is not adjacent`() {
        val symbol = Symbol("*", 3, 1)
        val partNumber = PartNumber(114, 0, 5, 7)

        partNumber.isAdjacentTo(symbol) shouldBe false
    }

    @Test
    fun `adjacent symbol is adjacent`() {
        val partNumber = PartNumber(partNumber=633, y=2, start=6, end=8)
        val symbol = Symbol("&", 6, 3)

        partNumber.isAdjacentTo(symbol) shouldBe true
    }

    val testInput =
        """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...${'$'}.*....
            .664.598..
        """.trimIndent().lines()
}

