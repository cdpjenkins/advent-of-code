package day02

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    internal fun `calculates sub position from test data`() {
        assertThat(part1CalculatePosition(day2TestInput), equalTo(150))
    }

    @Test
    internal fun `calculates sub position for part 2 using test data`() {
        assertThat(part2CalculatePosition(day2TestInput), equalTo(900))
    }
}

val day2TestInput = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent().lines()
