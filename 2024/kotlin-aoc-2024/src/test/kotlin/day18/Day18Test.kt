package day18

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import utils.Vector2D
import java.util.*
import kotlin.test.Ignore

private fun part1(input: List<String>, gridWidth: Int, gridHeight: Int, fallenBytes: Int): Int {
    val grid = input.parse(gridWidth, gridHeight, fallenBytes)

    val path = Historian(Vector2D(0, 0), grid)
        .findShortestPathUsingAStar(Vector2D(grid.width - 1, grid.height - 1))

    return path!!.size - 1
}

private fun part2(input: List<String>, gridWidth: Int, gridHeight: Int, fallenBytes: Int): Int {
    val grid = input.parse(gridWidth, gridHeight, fallenBytes)

    val path = Historian(Vector2D(0, 0), grid)
        .findShortestPathUsingAStar(Vector2D(grid.width - 1, grid.height - 1))

    return path!!.size - 1
}

private fun List<String>.parse(gridWidth: Int, gridHeight: Int, fallenBytes: Int): Grid {
    val bytesToFall = map { it.parseCoord() }

    val grid = Grid(
        bytesToFall,
        width = gridWidth,
        height = gridHeight,
        fallenBytes
    )
    return grid
}

data class Grid(
    val bytesToFall: List<Vector2D>,
    val width: Int,
    val height: Int,
    val fallenBytes: Int
) {
    fun isCorrupted(pos: Vector2D, historian: Historian) = historian.pos in bytesToFall.take(fallenBytes)

    fun asStringWith(path: List<Vector2D>): String {
        return (0..<height).map { y ->
            (0..<height).map { x ->
                val p1 = Vector2D(x, y)
                path.firstOrNull { it == p1 }
                    ?.let { 'O' }
                    ?: if (p1 in bytesToFall.take(12)) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }
}

data class Historian(val pos: Vector2D, val grid: Grid) {

    fun findShortestPathUsingAStar(endPos: Vector2D): List<Historian>? {
        fun heuristic(historian: Historian) = historian.pos.manhattanDistanceTo(endPos)

        val cameFrom = mutableMapOf<Historian, Historian>()
        val gScore = mutableMapOf<Historian, Int>().withDefault { Integer.MAX_VALUE }
        gScore[this] = 0
        val fScore = mutableMapOf<Historian, Int>().withDefault { Integer.MAX_VALUE }
        fScore[this] = heuristic(this)

        val openSet = PriorityQueue<Historian>(compareBy { fScore[it] } )
        openSet.offer(this)

        fun reconstructPath(current: Historian): List<Historian> {
            val totalPath = mutableListOf(current)
            var stonCurrent = current
            while (stonCurrent in cameFrom.keys) {
                stonCurrent = cameFrom[stonCurrent]!!
                totalPath.add(stonCurrent)
            }
            return totalPath.reversed()
        }

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()

            if (current.pos == endPos) {
                return reconstructPath(current)
            }

            openSet.remove(current)

            val neighbours = current.neighbours()
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

    private fun neighbours() =
        this.pos
            .neighbours()
            .filter { it.x in 0..<grid.width && it.y in 0..<grid.height }
            .filter { !grid.isCorrupted(it, this) }
            .map { Historian(it, grid) }


}

private fun Vector2D.neighbours() =
    listOf(
        Vector2D(x - 1, y),
        Vector2D(x + 1, y),
        Vector2D(x, y - 1),
        Vector2D(x, y + 1)
    )

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

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput, 7, 7, 12) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt"), 71, 71, 1024) shouldBe -1
    }

    @Test
    fun `finds shortest path using test data`() {
        val grid = testInput.parse(7, 7, 12)

        val path = Historian(Vector2D(0, 0), grid)
            .findShortestPathUsingAStar(Vector2D(grid.width - 1, grid.height - 1))

        grid.asStringWith(path!!.map { it.pos } ) shouldBe
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
