package Day12

import FileUtil.readInputFileToList
import Point2D
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.PriorityQueue
import kotlin.math.absoluteValue

class Day12 {
    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val (heightMap, start, end) = parseToMap(input)

        val path = aStarSearch(heightMap, start, end)

        path.size - 1 shouldBe 31
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val (heightMap, start, end) = parseToMap(input)

        val path = aStarSearch(heightMap, start, end)

        path.size - 1 shouldBe 330
    }

    @Test
    internal fun `part 2 test input`() {
        val input = testInput

        val (heightMap, start, end) = parseToMap(input)

        val potentialStartingPoints = heightMap.filter { (pos, square) -> square.height == 0 }
        val minPathLength = potentialStartingPoints
            .map { (pos, square) ->
                val (heightMap, start, end) = parseToMap(input)
                aStarSearch(heightMap, square, end)
            }
            .filter { it.isNotEmpty() }
            .map { it.size - 1 }
            .min()

       minPathLength shouldBe 29
    }

    @Test
    internal fun `part 2 real input`() {
        val input = realInput

        val (heightMap, start, end) = parseToMap(input)

        val potentialStartingPoints = heightMap.filter { (pos, square) -> square.height == 0 }
        val minPathLength = potentialStartingPoints
            .map { (pos, square) ->
                val (heightMap, start, end) = parseToMap(input)
                aStarSearch(heightMap, square, end)
            }
            .filter { it.isNotEmpty() }
            .map { it.size - 1 }
            .min()

        minPathLength shouldBe 321
    }

    private fun aStarSearch(heightMap: Map<Point2D, Square>, start: Square, end: Square): List<Point2D> {
        val openSet = PriorityQueue<Square> { lhs, rhs ->
            (lhs.scoreSoFar + lhs.heuristic(end)) - (rhs.scoreSoFar + rhs.heuristic(end))
        }
        openSet.add(start)

        while (openSet.isNotEmpty()) {
            val current = openSet.remove()
            current.visited = true

            if (current == end) {
                // TODO - dunnit
                return current.reconstructPath()
            }

            val reachableNeighbours = current.reachableNeighbours(heightMap)

            reachableNeighbours.forEach {neighbour ->

                val tentativeScore = current.scoreSoFar + 2

                if (tentativeScore < neighbour.scoreSoFar) {
                    neighbour.previousSquare = current
                    neighbour.scoreSoFar = tentativeScore
                    if (neighbour in openSet) {
                        // not sure if we need to do this in order to cause the priority queue to see the updated score...
                        openSet.remove(neighbour)
                    }
                    openSet.add(neighbour)
                }
            }
        }

        return listOf()
    }

    private fun parseToMap(input: List<String>): Triple<Map<Point2D, Square>, Square, Square> {
        // TODO prob don't need
        val girdWidth = input[0].length
        val gridHeight = input.size

        val charMap = input.withIndex().flatMap { (y, line) ->
            line.withIndex().map { (x, c) ->
                Point2D(x, y) to c
            }
        }.toMap()

        val start = charMap.entries.find { (p, c) -> c == 'S' }!!.key
        val end = charMap.entries.find { (p, c) -> c == 'E' }!!.key

        val heightMap = charMap.entries.map { (p, c) ->
            val isStart: Boolean = c == 'S'
            val isEnd: Boolean = c == 'E'

            val heightChar = when (c) {
                'S' -> 'a'
                'E' -> 'z'
                else -> c
            }
            val height = heightChar - 'a'
            p to Square(p, height, isStart = isStart, isEnd = isEnd, null)
        }.toMap()
        return Triple(heightMap, heightMap[start]!!, heightMap[end]!!)
    }
}

data class Square(
    val position: Point2D,
    val height: Int,
    val isStart: Boolean,
    val isEnd: Boolean,
    var previousSquare: Square? = null,
    var scoreSoFar: Int  = Integer.MAX_VALUE,
    var visited: Boolean = false
) {
    fun visited(): Boolean = previousSquare != null

    fun heightChar() = 'a' + height

    fun heuristic(end: Square) =
        (25 - height) +
                (this.position.x - end.position.x).absoluteValue +
                (this.position.y - end.position.y).absoluteValue

    fun reconstructPath(): List<Point2D> {
        if (previousSquare == null) {
            return listOf(this.position)
        } else {
            return previousSquare!!.reconstructPath() + this.position
        }
    }

    fun reachableNeighbours(heightMap: Map<Point2D, Square>): List<Square> =
        listOf(
            Point2D(position.x + 1, position.y),
            Point2D(position.x, position.y + 1),
            Point2D(position.x - 1, position.y),
            Point2D(position.x, position.y - 1)
        )
            .map { heightMap[it] }
            .filterNotNull()
            .filter { !it.visited }
            .filter { it.height <= height + 1 }
}

val testInput =
    """
        Sabqponm
        abcryxxl
        accszExk
        acctuvwj
        abdefghi
    """.trimIndent().lines()

val realInput = readInputFileToList("day12.txt")
