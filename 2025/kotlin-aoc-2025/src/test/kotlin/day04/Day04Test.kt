package day04

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val bales = input.withIndex().flatMap { (y, row) ->
        row.withIndex().map { (x, c) ->
            if (c == '@') Vector2D(x, y) else null
        }
    }.filterNotNull()
        .toSet()

    return bales.count { it.numNeighboursIn(bales) < 4 }
}

private fun Vector2D.numNeighboursIn(bales: Set<Vector2D>): Int {
    return this.neighbours().count { it in bales }
}

private fun Vector2D.neighbours(): Set<Vector2D> {
    return setOf(
        this + Vector2D(-1, -1),
        this + Vector2D(-1, 0),
        this + Vector2D(-1, 1),
        this + Vector2D(0, -1),
        this + Vector2D(0, 1),
        this + Vector2D(1, -1),
        this + Vector2D(1, 0),
        this + Vector2D(1, 1)
    )
}


private fun part2(input: List<String>): Int {
    return 123
}

class Day04Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 13
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day04.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day04.txt")) shouldBe -1
    }
}

val testInput =
    """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
    """.trimIndent().lines()
