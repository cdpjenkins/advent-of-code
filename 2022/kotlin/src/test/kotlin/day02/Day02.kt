package day02

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day02 {
    @Test
    internal fun `part 1 sample input`() {
        part1Score(testInput) shouldBe 15
    }

    @Test
    internal fun `part 1 real input`() {
        val realInput = getRealInput()
        part1Score(realInput) shouldBe 12535
    }

    @Test
    internal fun `part 2 test input`() {
        part2Score(testInput) shouldBe 12
    }

    @Test
    internal fun `part 2 real input`() {
        part2Score(getRealInput()) shouldBe 15457
    }
}

private fun part1Score(input: List<String>) =
    input.map { makeRoundForPart1(it) }
        .map { it.score() }
        .sum()

private fun part2Score(input: List<String>) =
    input.map { makeRoundForPart2(it) }
        .map { it.score() }
        .sum()

private fun makeRoundForPart1(inputLine: String): Round {
    val (opponentMove, yourMove) = inputLine.split(" ")

    return Round(opponentMove[0] - 'A', yourMove[0] - 'X')
}

private fun makeRoundForPart2(inputLine: String): Round {
    val (opponentMoveString, desiredOutcomeString) = inputLine.split(" ")

    val opponentMove = opponentMoveString[0] - 'A'
    val desiredOutcome = desiredOutcomeString[0] - 'X'

    val yourMove = when (opponentMove) {
        ROCK -> when (desiredOutcome) {
            MUST_LOSE -> SCISSORS
            MUST_DRAW -> ROCK
            MUST_WIN -> PAPER
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }

        PAPER -> when (desiredOutcome) {
            MUST_LOSE -> ROCK
            MUST_DRAW -> PAPER
            MUST_WIN -> SCISSORS
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }

        SCISSORS -> when (desiredOutcome) {
            MUST_LOSE -> PAPER
            MUST_DRAW -> SCISSORS
            MUST_WIN -> ROCK
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }
        else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
    }

    return Round(opponentMove, yourMove)
}


data class Round(val opponentMove: Int, val yourMove: Int) {
    fun score(): Int {
        return yourMove + 1 + scoreForContest(yourMove, opponentMove)
    }

    private fun scoreForContest(yourMove: Int, opponentMove: Int): Int {
        return when (yourMove) {
            ROCK -> when (opponentMove) {
                ROCK -> DRAW
                PAPER -> LOSE
                SCISSORS -> WIN
                else -> throw IllegalArgumentException(opponentMove.toString())
            }
            PAPER -> when (opponentMove) {
                ROCK -> WIN
                PAPER -> DRAW
                SCISSORS -> LOSE
                else -> throw IllegalArgumentException(opponentMove.toString())

            }
            SCISSORS -> when (opponentMove) {
                ROCK -> LOSE
                PAPER -> WIN
                SCISSORS -> DRAW
                else -> throw IllegalArgumentException(opponentMove.toString())
            }
            else -> throw IllegalArgumentException(yourMove.toString())
        }
    }

}


private fun getRealInput() = readInputFileToList("day02.txt")

val testInput = """
            A Y
            B X
            C Z
        """.trimIndent().lines()


// TODO enum class innit
const val ROCK = 0
const val PAPER = 1
const val SCISSORS = 2

const val LOSE = 0
const val DRAW = 3
const val WIN = 6

const val MUST_LOSE = 0
const val MUST_DRAW = 1
const val MUST_WIN = 2
