package day02

import FileUtil.readInputFileToList
import day02.Move.*
import day02.Outcome.*
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

private fun part1Score(input: List<String>) = input.map { makeRoundForPart1(it) }.sumOf { it.score() }

private fun part2Score(input: List<String>) = input.map { makeRoundForPart2(it) }.sumOf { it.score() }

private fun makeRoundForPart1(inputLine: String): Round {
    val (opponentMoveString, yourMoveString) = inputLine.split(" ")

    val opponentMove1 = Move.fromABC(opponentMoveString)
    val yourMove = Move.fromXYZ(yourMoveString)

    return Round(opponentMove1, yourMove)
}

private fun makeRoundForPart2(inputLine: String): Round {
    val (opponentMoveString, desiredOutcomeString) = inputLine.split(" ")

    val opponentMove = Move.fromABC(opponentMoveString)
    val desiredOutcome = Outcome.fromXYZ(desiredOutcomeString)

    return Round.allPossibleRounds().find { it.opponentMove == opponentMove && it.outcome == desiredOutcome }!!
}

data class Round(val opponentMove: Move, val yourMove: Move) {
    fun score(): Int = yourMove.moveScore + outcome.score

    val outcome: Outcome =
        when (yourMove) {
            ROCK -> when (opponentMove) {
                ROCK -> DRAW
                PAPER -> LOSE
                SCISSORS -> WIN
            }
            PAPER -> when (opponentMove) {
                ROCK -> WIN
                PAPER -> DRAW
                SCISSORS -> LOSE
            }
            SCISSORS -> when (opponentMove) {
                ROCK -> LOSE
                PAPER -> WIN
                SCISSORS -> DRAW
            }
        }

    companion object {
        fun allPossibleRounds() =
            Move.values().flatMap {yourMove ->
                Move.values().map {opponentMove ->
                    Round(opponentMove, yourMove)
                }
            }
    }
}

private fun getRealInput() = readInputFileToList("day02.txt")

val testInput = """
            A Y
            B X
            C Z
        """.trimIndent().lines()

enum class Move(value: Int) {
    ROCK(0),
    PAPER(1),
    SCISSORS(2);

    val abcValue = (value + 'A'.code).toChar().toString()
    val xyzValue = (value + 'X'.code).toChar().toString()

    val moveScore = value + 1

    companion object {
        fun fromABC(moveString: String) = Move.values().first { it.abcValue == moveString }
        fun fromXYZ(moveString: String) = values().first { it.xyzValue == moveString }
    }
}

enum class Outcome(val score: Int, val value: String) {
    LOSE(0, "X"),
    DRAW(3, "Y"),
    WIN(6, "Z");

    companion object {
        fun fromXYZ(moveString: String) = Outcome.values().first { it.value == moveString }
    }
}
