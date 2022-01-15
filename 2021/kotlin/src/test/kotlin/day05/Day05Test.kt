package day05

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day5Test {
    @Test
    internal fun `day 5 part 1 marks vents`() {
        assertThat(day5Part1CountIntersections(input), equalTo(5))
    }

    @Test
    internal fun `day 5 part 2 marks vents incl diagonals`() {
        assertThat(day5Part2CountIntersectionsIncludingDiagonals(input), equalTo(12))
    }
}

val input = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent().lines()
