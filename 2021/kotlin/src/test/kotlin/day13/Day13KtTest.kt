package day13

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day13KtTest {
    @Test
    internal fun `day 13 part 1`() {
        val numDots = day13Part1NumDotsAfterFirstFold(input)
        assertThat(numDots, equalTo(17))
    }
}

val input = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent().lines()
