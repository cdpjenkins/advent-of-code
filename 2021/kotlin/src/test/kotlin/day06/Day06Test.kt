package day06

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class Day06 {
    @Test
    internal fun `day 06 part 1 how many lantern fish from test input`() {
        val result = day06Part1NumberOfLanternFish(day06Input)
        assertThat(result, equalTo(5934))
    }
}

private val day06Input: List<String> = """
    3,4,3,1,2
""".trimIndent().lines()
