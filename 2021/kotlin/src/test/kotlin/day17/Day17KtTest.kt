package day17

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class Day17KtTest {

    @Test
    internal fun `day 17 part 1 highest y coordinate reachable that still hits target area`() {
        assertThat(highestYCoordinateReachable(testInput.parse()), equalTo(45))
    }

    @Test
    internal  fun `day 17 part 2 number of velocities that hit target area`() {
        assertThat(numberOfVelocitiesThatCauseProbeToHitTargetArea(testInput.parse()), equalTo(112))
    }

    @Test
    internal fun `can calculate probe path`() {
        assertThat(
            Probe.trajectory(Vector2D(7, 2))
                .take(8)
                .map { it.position }
                .toList(),
            equalTo(
                listOf(
                    Vector2D(0, 0),
                    Vector2D(7, 2),
                    Vector2D(13, 3),
                    Vector2D(18, 3),
                    Vector2D(22, 2),
                    Vector2D(25, 0),
                    Vector2D(27, -3),
                    Vector2D(28, -7)
                )
            )
        )
    }
}

const val testInput = "target area: x=20..30, y=-10..-5"
