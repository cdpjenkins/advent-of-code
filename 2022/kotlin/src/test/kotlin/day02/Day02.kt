package day02

import FileUtil.readInputFileToList
import day02.Move.*
import day02.Result.*
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
    val (opponentMove, yourMove) = inputLine.split(" ")

    return Round(Move.fromABC(opponentMove), Move.fromXYZ(yourMove))
}

private fun makeRoundForPart2(inputLine: String): Round {
    val (opponentMoveString, desiredOutcomeString) = inputLine.split(" ")

    val opponentMove = Move.fromABC(opponentMoveString)
    val desiredOutcome = Result.fromXYZ(desiredOutcomeString)

    val yourMove = when (opponentMove) {
        ROCK -> when (desiredOutcome) {
            LOSE -> SCISSORS
            DRAW -> ROCK
            WIN -> PAPER
        }

        PAPER -> when (desiredOutcome) {
            LOSE -> ROCK
            DRAW -> PAPER
            WIN -> SCISSORS
        }

        SCISSORS -> when (desiredOutcome) {
            LOSE -> PAPER
            DRAW -> SCISSORS
            WIN -> ROCK
        }
    }

    return Round(opponentMove, yourMove)
}

data class Round(val opponentMove: Move, val yourMove: Move) {
    fun score(): Int {
        return yourMove.moveScore() + resultForContest(yourMove, opponentMove).score
    }

    private fun resultForContest(yourMove: Move, opponentMove: Move): Result {
        return when (yourMove) {
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
    }
}


private fun getRealInput() = readInputFileToList("day02.txt")

val testInput = """
            A Y
            B X
            C Z
        """.trimIndent().lines()


// TODO enum class innit
enum class Move(private val value: Int) {
    ROCK(0),
    PAPER(1),
    SCISSORS(2);

    val abcValue = (value + 'A'.code).toChar().toString()
    val xyzValue = (value + 'X'.code).toChar().toString()

    fun moveScore() = value + 1

    companion object {
        fun fromABC(moveString: String) = Move.values().first { it.abcValue == moveString }
        fun fromXYZ(moveString: String) = values().first { it.xyzValue == moveString }
    }
}

enum class Result(val score: Int, val value: String) {
    LOSE(0, "X"),
    DRAW(3, "Y"),
    WIN(6, "Z");

    companion object {
        fun fromXYZ(moveString: String) = Result.values().first { it.value == moveString }
    }
}
