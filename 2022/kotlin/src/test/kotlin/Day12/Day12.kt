package Day12

import FileUtil.readInputFileToList
import Point2D
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.PriorityQueue

class Day12 {
    @Test
    internal fun `part 1 test input`() {
        lengthOfShortedPathFromStartToEnd(testInput) shouldBe 31
    }

    @Test
    internal fun `part 1 real input`() {
        lengthOfShortedPathFromStartToEnd(realInput) shouldBe 330
    }

    @Test
    internal fun `part 2 test input`() {
        lengthOfShortedPathFromEndToGroundLevel(testInput) shouldBe 29
    }

    @Test
    internal fun `part 2 real input`() {
        lengthOfShortedPathFromEndToGroundLevel(realInput) shouldBe 321
    }

    private fun lengthOfShortedPathFromEndToGroundLevel(input: List<String>): Int {
        val (heightMap, _, end) = parseToMap(input)

        val path = breadthFirstSearch(
            heightMap,
            end,
            climbingDownwards,
            { it.height == 0 }
        )

        val pathLength = path.size - 1
        return pathLength
    }

    private fun lengthOfShortedPathFromStartToEnd(input: List<String>): Int {
        val (heightMap, start, end) = parseToMap(input)

        val path = breadthFirstSearch(
            heightMap,
            start,
            climbingUpwards
        ) { it: Square -> it == end }

        val pathLength = path.size - 1
        return pathLength
    }

    private fun breadthFirstSearch(
        heightMap: Map<Point2D, Square>,
        start: Square,
        isClimbable: (Square, Square) -> Boolean,
        endPredixcate: (Square) -> Boolean
    ): List<Point2D> {
        // Ew there is so much mutable state here. I wish this was more functional but that was a bit too tricky to
        // achieve.
        // One thing to bear in mind: you can't do two breadth first searchs on the same set of Squares - because the
        // first search mutates some of the state in the Squares. Consequently, you need to re-parse them if you want
        // to do another search.

        val openSet = PriorityQueue<Square> { lhs, rhs -> lhs.scoreSoFar - rhs.scoreSoFar }
        openSet.add(start)

        while (openSet.isNotEmpty()) {
            val current = openSet.remove()
            current.visited = true

            val endPredicate = endPredixcate

            if (endPredicate(current)) {
                return current.reconstructPath()
            }

            val reachableNeighbours = listOf(
                Point2D(current.position.x + 1, current.position.y),
                Point2D(current.position.x, current.position.y + 1),
                Point2D(current.position.x - 1, current.position.y),
                Point2D(current.position.x, current.position.y - 1)
            )
                .map { heightMap[it] }
                .filterNotNull()
                .filter { !it.visited }
                .filter { isClimbable(it, current) }

            reachableNeighbours.forEach { neighbour ->

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
        val charMap = input.withIndex().flatMap { (y, line) ->
            line.withIndex().map { (x, c) ->
                Point2D(x, y) to c
            }
        }.toMap()

        val start = charMap.entries.find { (_, c) -> c == 'S' }!!.key
        val end = charMap.entries.find { (_, c) -> c == 'E' }!!.key

        val heightMap = charMap.entries.associate { (p, c) ->
            val isStart: Boolean = c == 'S'
            val isEnd: Boolean = c == 'E'

            val heightChar = when (c) {
                'S' -> 'a'
                'E' -> 'z'
                else -> c
            }
            val height = heightChar - 'a'
            p to Square(p, height, isStart = isStart, isEnd = isEnd, null)
        }
        return Triple(heightMap, heightMap[start]!!, heightMap[end]!!)
    }
}

val climbingUpwards = { lhs: Square, rhs: Square -> lhs.height <= rhs.height + 1 }
val climbingDownwards = { lhs: Square, rhs: Square -> rhs.height <= lhs.height +1 }
data class Square(
    val position: Point2D,
    val height: Int,
    val isStart: Boolean,
    val isEnd: Boolean,
    var previousSquare: Square? = null,
    var scoreSoFar: Int  = Integer.MAX_VALUE,
    var visited: Boolean = false
) {
    fun reconstructPath(): List<Point2D> {
        if (previousSquare == null) {
            return listOf(this.position)
        } else {
            return previousSquare!!.reconstructPath() + this.position
        }
    }

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
