package day18

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import utils.Vector2D
import java.util.*

private fun part1(input: List<String>, gridWidth: Int, gridHeight: Int, fallenBytes: Int): Int {
    val grid = input.parse(gridWidth, gridHeight, fallenBytes)

    val path = grid.findShortestPathUsingAStar()

    return path!!.size - 1
}

private fun part2(input: List<String>, gridWidth: Int, gridHeight: Int, initialGuess: Int): String {
    // Binary search would likely have been much more efficient than this approach. But this works well enough to
    // get the answer in less than two seconds for my input on my machine.

    val totallyFallenGrid = input.parse(gridWidth, gridHeight, fallenBytes = input.size)

    var guess = initialGuess
    do {
        val path = totallyFallenGrid
            .copy(bytesToFall = totallyFallenGrid.bytesToFall.take(guess))
            .findShortestPathUsingAStar()
        if (path != null) {
            guess = totallyFallenGrid.bytesToFall.withIndex().first { (_, v) -> v in path }.index + 1
        }
    } while (path != null)

    return totallyFallenGrid.bytesToFall[guess - 1].let { "${it.x},${it.y}" }
}

data class Grid(
    val bytesToFall: List<Vector2D>,
    val width: Int,
    val height: Int
) {
    fun isCorrupted(pos: Vector2D) = pos in bytesToFall

    fun findShortestPathUsingAStar(): List<Vector2D>? {
        val startPos = Vector2D(0, 0)
        val endPos = Vector2D(width - 1, height - 1)

        fun heuristic(pos: Vector2D) = pos.manhattanDistanceTo(endPos)

        val cameFrom = mutableMapOf<Vector2D, Vector2D>()
        val gScore = mutableMapOf<Vector2D, Int>().withDefault { Integer.MAX_VALUE }
        gScore[startPos] = 0
        val fScore = mutableMapOf<Vector2D, Int>().withDefault { Integer.MAX_VALUE }
        fScore[startPos] = heuristic(startPos)

        val openSet = PriorityQueue<Vector2D>(compareBy { fScore[it] } )
        openSet.offer(startPos)

        fun reconstructPath(endPos: Vector2D): List<Vector2D> {
            val totalPath = mutableListOf(endPos)
            var current = endPos
            while (current in cameFrom.keys) {
                current = cameFrom[current]!!.also { totalPath.add(it) }
            }
            return totalPath.reversed()
        }

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()

            if (current == endPos) {
                return reconstructPath(current)
            }

            val neighbours = neighbours(current)
            neighbours.forEach { neighbour ->
                val tentativeGScore = gScore.getValue(current) + 1
                if (tentativeGScore < gScore.getValue(neighbour)) {
                    cameFrom[neighbour] = current
                    gScore[neighbour] = tentativeGScore
                    fScore[neighbour] = tentativeGScore + heuristic(neighbour)
                    if (neighbour !in openSet) {
                        openSet.add(neighbour)
                    }
                }
            }
        }

        return null
    }

    fun neighbours(pos: Vector2D) =
        listOf(
            Vector2D(pos.x - 1, pos.y),
            Vector2D(pos.x + 1, pos.y),
            Vector2D(pos.x, pos.y - 1),
            Vector2D(pos.x, pos.y + 1)
        )
            .filter { it.x in 0..<width && it.y in 0..<height }
            .filter { !isCorrupted(pos) }

    fun asStringWith(path: List<Vector2D>): String {
        return (0..<height).map { y ->
            (0..<height).map { x ->
                val pos = Vector2D(x, y)
                path.firstOrNull { it == pos }
                    ?.let { 'O' }
                    ?: if (pos in bytesToFall.take(12)) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }
}

private fun List<String>.parse(gridWidth: Int, gridHeight: Int, fallenBytes: Int): Grid {
    val bytesToFall = map { it.parseCoord() }

    return Grid(bytesToFall.take(fallenBytes), gridWidth, gridHeight)
}

val COORDS_REGEX = """^(\d+),(\d+)$""".toRegex()
private fun String.parseCoord(): Vector2D = parseUsingRegex(COORDS_REGEX).toList().toVector()

private fun List<String>.toVector(): Vector2D {
    require(this.size == 2)

    return Vector2D(this[0].toInt(), this[1].toInt())
}

class Day18Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput, 7, 7, 12) shouldBe 22
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day18.txt"), 71, 71, 1024) shouldBe 290
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput, 7, 7, 12) shouldBe "6,1"
    }

    @Test
    fun `part 2 with real input`() {
        val nonCheatyInitialGuess = 1024
        val cheatyInitialGuess = 2980
        part2(readInputFileToList("day18.txt"), 71, 71, nonCheatyInitialGuess) shouldBe "64,54"
    }

    @Test
    fun `finds shortest path using test data`() {
        val grid = testInput.parse(7, 7, 12)

        val path = grid
            .findShortestPathUsingAStar()

        grid.asStringWith(path!!) shouldBe
            """
                OO.#OOO
                .O#OO#O
                .OOO#OO
                ...#OO#
                ..#OO#.
                .#.O#..
                #.#OOOO
            """.trimIndent()
    }
}

val testInput =
    """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent().lines()
