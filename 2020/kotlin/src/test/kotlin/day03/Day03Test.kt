package day03

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines

class Day03Test : FunSpec({
    test("day 3 part 1 with test input") {
        Terrain(testInput).treesHitOnSlope(Vector2D(3, 1)) shouldBe 7
    }

    test("day 3 part 1 with real input") {
        Terrain(realInput).treesHitOnSlope(Vector2D(3, 1)) shouldBe 220
    }

    test("day 3 part 2 with test input") {
        productOfTreesHitOnAllPossibleSlopes(testInput) shouldBe 336
    }

    test("day 3 part 2 with real input") {
        productOfTreesHitOnAllPossibleSlopes(realInput) shouldBe 2138320800
    }
})

private fun productOfTreesHitOnAllPossibleSlopes(input: List<String>): Int {
    val terrain = Terrain(input)

    return possibleSlopes
        .map { terrain.treesHitOnSlope(it) }
        .fold(1) { acc, i -> acc * i }
}

data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(that: Vector2D) = Vector2D(this.x + that.x, this.y + that.y)
}

data class Terrain(val testInput: List<String>) {
    fun treesHitOnSlope(slope: Vector2D) =
        generateSequence(Vector2D(0, 0)) { it + slope }
            .takeWhile { it.y < height }
            .filter { treeAt(it) }.count()

    private fun treeAt(it: Vector2D): Boolean {
        return testInput[it.y][it.x % width] == '#'
    }

    private val width: Int = testInput.first().length
    private val height: Int = testInput.size
}

private val possibleSlopes =
    listOf(
        Vector2D(1, 1),
        Vector2D(3, 1),
        Vector2D(5, 1),
        Vector2D(7, 1),
        Vector2D(1, 2)
    )

val realInput = readLines("Day03")

val testInput = """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
        """.trimIndent().lines()
