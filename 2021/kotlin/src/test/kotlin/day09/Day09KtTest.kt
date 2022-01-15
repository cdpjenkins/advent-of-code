package day09

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day09KtTest {
    @Test
    internal fun `day 9 part 1`() {
        assertThat(sumOfRiskLevelsOfLowPoints(input), equalTo(15))
    }

    @Test
    internal fun `day 9 part 2 basins`() {
        assertThat(productOfSizesOfTop3Basins(input), equalTo(1134))
    }
}

val input = """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
        """.trimIndent().lines()
