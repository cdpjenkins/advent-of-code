package day02

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day02 {
    @Test
    internal fun `part 1 sample input`() {
        val input = testInput
        val result = part1(input)
        result shouldBe 8
    }

    @Test
    internal fun `part 1 real input`() {
        val input = readInputFileToList("day02.txt")
        val result = part1(input)
        result shouldBe 2439
    }

    @Test
    internal fun `part 2 sample input`() {
        val input = testInput
        val result = part2(input)
        result shouldBe 2286
    }

    @Test
    internal fun `part 2 real input`() {
        val input = readInputFileToList("day02.txt")
        val result = part2(input)
        result shouldBe 63711
    }

    private fun part1(input: List<String>): Int {
        val regex = "^Game (\\d+): (.*)$".toRegex()
        val games = input.map {
            Game.parseGame(it, regex)
        }

        println(games)

        for (game in games) {
            println(game.gameNum)
            for (set in game.sets) {
                println("  $set")
            }

        }

        val gamesThatAreValie = games.filter { it.isPossibleIfCubesAreReplaced() }
            .map { it.gameNum }

        val result = gamesThatAreValie
            .sum()
        return result
    }

    private fun part2(input: List<String>): Int {
        val regex = "^Game (\\d+): (.*)$".toRegex()
        val games = input.map {
            Game.parseGame(it, regex)
        }

        println(games)

        for (game in games) {
            println(game.gameNum)
            for (set in game.sets) {
                println("  $set")
            }
        }

        val gamesThatAreValie = games
            .map { it.power() }

        val result = gamesThatAreValie
            .sum()
        return result
    }


//    @Test
//    internal fun `part 1 real input`() {
////        day1Part1SumFirstAndLastDigits(readInputFileToList("day01.txt")) shouldBe 54951
//    }

//    @Test
//    internal fun `part 2 sample input`() {
//        day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(testInput2) shouldBe 281
//    }
//
//    @Test
//    internal fun `part 2 real input`() {
//        day1Part2SumFirstAndLastDigitsAfterSubstitutingDigitsForWords(readInputFileToList("day01.txt")) shouldBe 55218
//    }
}

data class Game(
    val gameNum: Int,
    val sets: List<GameSet>,
)  {
    fun isPossibleIfCubesAreReplaced(): Boolean {
        val totalReds = sets.map { it.red }.max()
        val totalGreen = sets.map { it.green }.max()
        val totalBlues = sets.map { it.blue }.max()

        return totalReds <= 12 &&
                totalGreen <= 13 &&
                totalBlues <= 14
    }

    fun power(): Int {
        val maxRed = sets.map { it.red }.max()
        val maxGreen = sets.map { it.green }.max()
        val maxBlue = sets.map { it.blue }.max()

        return maxRed * maxGreen * maxBlue
    }

    companion object {
        fun parseGame(it: String, regex: Regex): Game {
            val meh = it.parseUsingRegex(regex)

            val thingies = meh.toList()

            val gameNum = thingies[0].toInt()

            val sets = thingies[1].split(";").map {
                GameSet.parse(it)
            }
            return Game(gameNum, sets)
        }

    }
}

data class GameSet(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    companion object {
        fun parse(input: String): GameSet {
            val redRegex = "(\\d+) red".toRegex()
            val greenRegex = "(\\d+) green".toRegex()
            val blueRegex = "(\\d+) blue".toRegex()

            val red = if (redRegex.containsMatchIn(input)) {
                val (redString) = input.parseUsingRegex(redRegex)
                redString.toInt()
            } else {
                0
            }

            val green = if (greenRegex.containsMatchIn(input)) {
                val (greenString) = input.parseUsingRegex(greenRegex)
                greenString.toInt()
            } else {
                0
            }



            val blue = if (blueRegex.containsMatchIn(input)) {
                val (blueString) = input.parseUsingRegex(blueRegex)
                blueString.toInt()
            } else {
                0
            }

            return GameSet(red, green, blue)

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
