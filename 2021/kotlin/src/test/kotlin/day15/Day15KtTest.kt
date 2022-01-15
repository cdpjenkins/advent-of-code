package day15

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day15KtTest {
    @Test
    internal fun `day 15 part 1`() {
        assertThat(day15Part1DijsktraMinPath(input), equalTo(40))
    }

    @Test
    internal fun `day 15 part 2`() {
        assertThat(day15Part2DijkstraMinPathFiveTimesBigger(input), equalTo(315))
    }
}

val input = """
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
        """.trimIndent().lines()
