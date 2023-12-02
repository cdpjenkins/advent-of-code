package day02

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day02 {
    @Test
    internal fun `part 1 sample input`() {
        part1(testInput) shouldBe 8
    }

    @Test
    internal fun `part 1 real input`() {
        part1(readInputFileToList("day02.txt")) shouldBe 2439
    }

    @Test
    internal fun `part 2 sample input`() {
        part2(testInput) shouldBe 2286
    }

    @Test
    internal fun `part 2 real input`() {
        part2(readInputFileToList("day02.txt")) shouldBe 63711
    }

    private fun part1(input: List<String>) =
        Game.parseGamesFrom(input)
            .filter { it.isPossibleIfCubesAreReplaced() }
            .sumOf { it.id }

    private fun part2(input: List<String>) =
        Game.parseGamesFrom(input)
            .sumOf { it.power() }
}

data class Game(
    val id: Int,
    val sets: List<GameSet>,
)  {
    fun isPossibleIfCubesAreReplaced(): Boolean =
        sets.maxOf { it.red } <= 12 &&
        sets.maxOf { it.green } <= 13 &&
        sets.maxOf { it.blue } <= 14

    fun power(): Int =
        sets.maxOf { it.red } *
        sets.maxOf { it.green } *
        sets.maxOf { it.blue }

    companion object {
        val regex = "^Game (\\d+): (.*)$".toRegex()

        fun parseGamesFrom(input: List<String>): List<Game> = input.map { parseGame(it) }

        fun parseGame(line: String): Game {
            val (idString, gamesString) = line.parseUsingRegex(regex).toList()
            val sets = gamesString
                .split(";")
                .map { GameSet.parse(it) }
            return Game(idString.toInt(), sets)
        }
    }
}

data class GameSet(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    companion object {
        val RED_REGEX = "(\\d+) red".toRegex()
        val GREEN_REGEX = "(\\d+) green".toRegex()
        val BLUE_REGEX = "(\\d+) blue".toRegex()

        fun parse(input: String) =
            GameSet(
                input.parseCube(RED_REGEX),
                input.parseCube(GREEN_REGEX),
                input.parseCube(BLUE_REGEX)
            )

        private fun String.parseCube(regex: Regex): Int {
            val numCubes = if (regex.containsMatchIn(this)) {
                val (numString) = parseUsingRegex(regex)
                numString.toInt()
            } else {
                0
            }
            return numCubes
        }
    }
}

val testInput =
    """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().lines()
