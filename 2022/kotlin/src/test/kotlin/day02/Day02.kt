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

    return Round(Move.fromABC(opponentMove), Move.fromXYZ(yourMove))
}

private fun makeRoundForPart2(inputLine: String): Round {
    val (opponentMoveString, desiredOutcomeString) = inputLine.split(" ")

    val opponentMove = Move.fromABC(opponentMoveString)
    val desiredOutcome = desiredOutcomeString[0] - 'X'

    val yourMove = when (opponentMove) {
        Move.ROCK -> when (desiredOutcome) {
            MUST_LOSE -> Move.SCISSORS
            MUST_DRAW -> Move.ROCK
            MUST_WIN -> Move.PAPER
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }

        Move.PAPER -> when (desiredOutcome) {
            MUST_LOSE -> Move.ROCK
            MUST_DRAW -> Move.PAPER
            MUST_WIN -> Move.SCISSORS
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }

        Move.SCISSORS -> when (desiredOutcome) {
            MUST_LOSE -> Move.PAPER
            MUST_DRAW -> Move.SCISSORS
            MUST_WIN -> Move.ROCK
            else -> throw java.lang.IllegalArgumentException(desiredOutcome.toString())
        }
    }

    return Round(opponentMove, yourMove)
}

data class Round(val opponentMove: Move, val yourMove: Move) {
    fun score(): Int {
        return yourMove.moveScore() + scoreForContest(yourMove, opponentMove)
    }

    private fun scoreForContest(yourMove: Move, opponentMove: Move): Int {
        return when (yourMove) {
            Move.ROCK -> when (opponentMove) {
                Move.ROCK -> DRAW
                Move.PAPER -> LOSE
                Move.SCISSORS -> WIN
            }
            Move.PAPER -> when (opponentMove) {
                Move.ROCK -> WIN
                Move.PAPER -> DRAW
                Move.SCISSORS -> LOSE
            }
            Move.SCISSORS -> when (opponentMove) {
                Move.ROCK -> LOSE
                Move.PAPER -> WIN
                Move.SCISSORS -> DRAW
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
enum class Move(val value: Int) {
    ROCK(0),
    PAPER(1),
    SCISSORS(2);

    fun moveScore() = value + 1

    companion object {
        fun fromABC(moveString: String) = Move.values().first { it.value == moveString[0] - 'A' }
        fun fromXYZ(moveString: String) = values().first { it.value == moveString[0] - 'X' }
    }
}

const val LOSE = 0
const val DRAW = 3
const val WIN = 6

const val MUST_LOSE = 0
const val MUST_DRAW = 1
const val MUST_WIN = 2
