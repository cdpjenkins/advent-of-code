package day01

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day01KtTest {
    @Test
    internal fun `counts number of increases of depth with sample data`() {
        assertThat(countDepthIncreasesFromStrings(day1TestInput), equalTo(7))
    }

    @Test
    internal fun `counts number of increases of sliding windows`() {
        assertThat(slidingWindows(day1TestInput), equalTo(5))
    }
}

val day1TestInput = """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
        """.trimIndent().lines()
