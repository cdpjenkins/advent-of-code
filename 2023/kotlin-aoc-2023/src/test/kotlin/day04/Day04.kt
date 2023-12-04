package day03

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val cards = input.map { Card.of(it) }

    return cards.sumOf { it.score }
}

private fun part2(input: List<String>): Int {
    val cards = input.map { Card.of(it) }

    // urgh, mutating state :-(
    cards.withIndex().forEach { (i, card) ->
        for (j in (1..card.matches)) {
            cards[i + j].numInstances += card.numInstances
        }
    }

    return cards.sumOf { it.numInstances }
}

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val yourNumbers: List<Int>,
    var numInstances: Int = 1
) {
    val matches = yourNumbers.filter { it in winningNumbers }.size
    val score = matches.pow2()

    companion object {
        val CARD_REGEX = "^Card(?:\\s+)(\\d+):([^|]+)\\|(.*)$".toRegex()
        val WHITESPACE_REGEX = "\\s+".toRegex()

        fun of(line: String): Card {
            val (idString, winningNumbersString, yourNumbersString) = line.parseUsingRegex(CARD_REGEX)

            return Card(
                idString.toInt(),
                winningNumbersString.parseToIntList(),
                yourNumbersString.parseToIntList()
            )
        }

        private fun String.parseToIntList() = trim().split(WHITESPACE_REGEX).map { it.toInt() }
    }
}

private fun Int.pow2(): Int =
    when (this) {
        0 -> 0
        1 -> 1
        else -> 2 * (this - 1).pow2()
    }

class Day04Test {
    @Test
    internal fun `part 1 sample input`() {
        part1(testInput) shouldBe 13
    }

    @Test
    internal fun `part 1 real input`() {
        part1(readInputFileToList("day04.txt")) shouldBe 18653
    }

    @Test
    internal fun `part 2 sample input`() {
        part2(testInput) shouldBe 30
    }

    @Test
    internal fun `part 2 real input`() {
        part2(readInputFileToList("day04.txt")) shouldBe 30
    }

    @Test
    fun `card with 2 winning numbers has score of 2`() {
        val card = Card(
            id = 3,
            winningNumbers = listOf(1, 21, 53, 59, 44),
            yourNumbers = listOf(69, 82, 63, 72, 16, 21, 14, 1)
        )

        card.score shouldBe 2
    }

    val testInput =
        """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent().lines()
}
