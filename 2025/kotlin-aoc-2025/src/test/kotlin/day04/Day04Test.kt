package day04

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val bales = parseToBales(input)

    return bales.count { it.canRemove(bales) }
}

private fun Vector2D.canRemove(bales: Set<Vector2D>): Boolean = numNeighboursIn(bales) < 4

private fun part2(input: List<String>): Int {
    val initialBales = parseToBales(input)
    var bales = initialBales
    var done = false

    while (!done) {
        val newBales = bales.filter { !it.canRemove(bales) }.toSet()
        if (newBales.size < bales.size) {
            bales = newBales
        } else {
            done = true
        }
    }

    println(bales.size)
    println(initialBales.size)

    return initialBales.size - bales.size
}


private fun parseToBales(input: List<String>): Set<Vector2D> = input.withIndex().flatMap { (y, row) ->
    row.withIndex().map { (x, c) ->
        if (c == '@') Vector2D(x, y) else null
    }
}.filterNotNull()
    .toSet()

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



class Day04Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 13
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day04.txt")) shouldBe 1397
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 43
    }

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
