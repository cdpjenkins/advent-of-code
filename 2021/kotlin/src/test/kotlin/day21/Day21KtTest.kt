package day21

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day21KtTest {
    @Test
    fun `day 21 part 1 deterministic dice with test data`() {
        assertThat(day21Part1DeterministicDice(input), equalTo(739785))
    }

    @Test
    fun `day 21 part 2 dirac dice with test data`() {
        val (player1Wins, player2Wins) = day21Part2DiracDice(input)

        assertThat(player1Wins, equalTo(444356092776315L))
        assertThat(player2Wins, equalTo(341960390180808L))
    }
}

var input = """
            Player 1 starting position: 4
            Player 2 starting position: 8
        """.trimIndent()
