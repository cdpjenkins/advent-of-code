package day03

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class Day03Test {
    @Test
    internal fun `day 3 part 1 test input`() {
        assertThat(day3Part1CalculatePowerConsumption(day3TestInput), equalTo(198))
    }

    @Test
    internal fun `day 3 part 2 calculates oxygen generator rating`() {
        assertThat(day3Part2CalculateLifeSupportRating(day3TestInput), equalTo(230))

    }
}

val day3TestInput = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
        """.trimIndent().lines()
