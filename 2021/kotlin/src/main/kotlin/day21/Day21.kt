package day21

import java.io.File
import kotlin.AssertionError

fun main() {
    val input = File("src/main/resources", "Day21.txt").readText()

    println(day21Part1DeterministicDice(input))
    println(day21Part2DiracDice(input))
}

fun day21Part2DiracDice(input: String): Pair<Long, Long> {
    val initialGameState = input.initialGameState()

    // wish I could find an idiomatic and fast way to do this with and immutable map and pure functions...
    val universes: MutableMap<GameState, Long> = mutableMapOf(initialGameState to 1L)
    val wins: Array<Long> = arrayOf(-1L, 0L, 0L)

    while (!universes.isEmpty()) {
        val (state, frequency) = universes.popFirstEntry()

        // could use a Pascal's triangle and save from computing the same move several times in a lot of cases
        for (d1 in 1..3) {
            for (d2 in 1..3) {
                for (d3 in 1..3) {
                    val newState = state.playOneTurn(d1 + d2 + d3)
                    val winner = newState.winner(winningScore = 21)

                    if (winner == null) {
                        universes[newState] = universes.getOrDefault(newState, 0) + frequency
                    } else {
                        wins[winner] += frequency
                    }
                }
            }
        }
    }

    return Pair(wins[1], wins[2])
}

private fun MutableMap<GameState, Long>.popFirstEntry(): MutableMap.MutableEntry<GameState, Long> {
    val entry = iterator().next()
    remove(entry.key)
    return entry
}

fun day21Part1DeterministicDice(input: CharSequence): Int {
    val gameState = input.initialGameState()

    val game = DeterministicGame(
        gameState = gameState
    )

    while (game.gameState.winner() == null) {
        game.playTurn()
    }

    val losingScore = game.gameState.losingScore()

    return losingScore * game.diceLastValue
}

private fun CharSequence.initialGameState(): GameState {
    val regex = "Player 1 starting position: ([0-9]{1,2})\nPlayer 2 starting position: ([0-9]{1,2})".toRegex()
    val (p1Start, p2Start) = regex.find(this)
        ?.destructured
        ?.toList()
        ?.map { it.toInt() }
        ?: throw AssertionError("bang")

    val gameState = GameState(
        player1State = PlayerState(
            score = 0,
            position = p1Start
        ),
        player2State = PlayerState(
            score = 0,
            position = p2Start
        )
    )

    return gameState

}

data class PlayerState(
    val score: Int,
    val position: Int
) {
    fun playOneTurn(diceTotal: Int) = PlayerState(
        position = modPositionRange(position + diceTotal),
        score = score + modPositionRange(position + diceTotal)
    )

    private fun modPositionRange(position: Int) = ((position - 1) % 10) + 1
}

data class GameState(
    val player1State: PlayerState,
    val player2State: PlayerState,
    val nextPlayerTurn: Int = 1
) {
    fun playOneTurn(diceTotal: Int): GameState {
        val nextGameState = when (nextPlayerTurn) {
            1 -> copy(player1State = player1State.playOneTurn(diceTotal))
            2 -> copy(player2State = player2State.playOneTurn(diceTotal))
            else -> throw AssertionError("Bad nextPlayerTurn: $nextPlayerTurn")
        }

        return nextGameState.copy(nextPlayerTurn = nextPlayer(nextGameState.nextPlayerTurn))
    }

    private fun nextPlayer(currentPlayer: Int) = 2 - currentPlayer + 1

    fun losingScore(): Int =
        when {
            winner() == 1 -> player2State.score
            winner() == 2 -> player1State.score
            else -> throw AssertionError("urgh")
        }

    fun winner(winningScore: Int = 1000): Int? {
        if (player1State.score >= winningScore) return 1
        if (player2State.score >= winningScore) return 2
        return null
    }
}

data class DeterministicGame(
    var diceLastValue: Int = 0,
    var gameState: GameState
) {
    var turnsPlayed: Int = 0

    fun playTurn() {
        turnsPlayed++

        val diceValue1 = ++diceLastValue
        val diceValue2 = ++diceLastValue
        val diceValue3 = ++diceLastValue

        val diceTotal = diceValue1 + diceValue2 + diceValue3

        gameState = gameState.playOneTurn(diceTotal)
    }
}

