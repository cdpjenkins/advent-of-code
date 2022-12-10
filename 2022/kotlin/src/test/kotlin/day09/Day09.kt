package day09

import Point2D
import RegexUtils.parseUsingRegex
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day09 {
    @Ignore
    @Test
    internal fun `part 1 test data`() {

        // TODO - finish me!

        val input = testInput

        val instructions = input.flatMap { it.parseInstruction() }

        println(instructions)

        val initialPoint = Point2D(0, 0)

        instructions.fold(initialPoint) { p: Point2D, direction: Direction -> direction.execute(p) }
    }
}

private fun String.parseInstruction(): List<Direction> {
    val (directionString: String, distanceString) = this.parseUsingRegex("^(L|R|U|D) (\\d+)$")

    val num = distanceString.toInt()
    return List(num) { Direction.valueOf(directionString) }
}

enum class Direction {
    U, D, L, R;

    fun execute(p: Point2D): Point2D {
        return p
    }
}

val testInput =
    """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent().lines()
