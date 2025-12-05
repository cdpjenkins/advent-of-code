package day04

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D

private fun part1(input: List<String>): Int {
    val bales = parseToBales(input)

    return bales.count { it.canRemove(bales) }
}

private fun Vector2D.canRemove(bales: Set<Vector2D>): Boolean = numNeighboursIn(bales) < 4

private fun part2(input: List<String>): Int {
    val (initialBales, numNeighboursOf) = parseToBalesWithNeighboursPrecomputed(input)

    val bales = initialBales.toMutableSet()
    val candidates = initialBales.toMutableList()

    while (!candidates.isEmpty()) {
        val candidate = candidates.removeLast()

        if (candidate in bales && candidate.canRemoveAccordingTo(numNeighboursOf)) {
            bales.remove(candidate)

            val neighbours = candidate.neighbours()
            neighbours.forEach { numNeighboursOf[it] = numNeighboursOf.getValue(it) - 1 }
            candidates.addAll(neighbours)
        }
    }

    return initialBales.size - bales.size
}

private fun Vector2D.canRemoveAccordingTo(numNeighboursMap: Map<Vector2D, Int>) = numNeighboursMap.getValue(this) < 4

private fun Vector2D.numNeighboursIn(bales: Set<Vector2D>) = this.neighbours().count { it in bales }

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

private fun parseToBales(input: List<String>): Set<Vector2D> {
    return input.withIndex().flatMap { (y, row) ->
        row.withIndex().map { (x, c) ->
            if (c == '@') Vector2D(x, y) else null
        }
    }.filterNotNull()
        .toSet()
}

private fun parseToBalesWithNeighboursPrecomputed(input: List<String>): Pair<Set<Vector2D>, MutableMap<Vector2D, Int>> {
    val width = input.first().length
    val height = input.size

    val numNeighbours = mapOf<Vector2D, Int>().toMutableMap().withDefault { 0 }

    val numNeighboursArray = List(width * height) { 0 }.toMutableList()

    val bales = input.withIndex().flatMap { (y, row) ->
        row.withIndex().map { (x, c) ->
            if (c == '@') {
                val pos = Vector2D(x, y)

                pos.neighbours().filter { it.isInBounds(width, height) }.forEach {
                    numNeighbours[it] = numNeighbours.getValue(it) + 1

                    numNeighboursArray[width * y + x]++
                }

                pos
            } else {
                null
            }
        }
    }.filterNotNull()
        .toSet()
    return Pair(bales, numNeighbours)
}

private fun Vector2D.isInBounds(width: Int, height: Int): Boolean {
    return x >= 0 && x < width &&
            y >= 0 && y < height
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
        part2(readInputFileToList("day04.txt")) shouldBe 8758
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
