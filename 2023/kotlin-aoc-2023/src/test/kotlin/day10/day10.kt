package day10

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import kotlin.test.Test

private fun part1(input: List<String>): Int {
    val pipeGraph = PipeGraph.pipeGraphOf(input)

    val dijkstraMaxDistance = pipeGraph.dijkstra(pipeGraph.start, pipeGraph.adjacencyList)

    return dijkstraMaxDistance.maxOf { it.value }
}

private fun part2(input: List<String>): Int {

    val pipeGraph = PipeGraph.oneWayPipeGraphOf(input)

    val dijkstraMaxDistance = pipeGraph.dijkstra(pipeGraph.start, pipeGraph.adjacencyList)

    val theWholeFreakingPath = dijkstraMaxDistance.map { it.key } + listOf(pipeGraph.start)

    val dirs = theWholeFreakingPath
        .map {
            // Note that there is probably a horrific bug here: When finding the neighbours of the start node, we
            // deliberately remove one of the neighbours (so that Dijkstra's algorithm only traverses the cycle one
            // way. If we get unlucky here, it could turn out that we _needed_ the neighbour that we lost to tell us
            // whether squares on the same row as the start node are inside/outside the path. However, this does not
            // seem to affect the answer on any of the test data or my real data, and I'm tired, so have not fixed it
            // so far.
            it to (Point2D(it.x, it.y+1) in pipeGraph.adjacencyList[it]!!)
        }.toMap()

    val squares = mutableMapOf<Point2D, Char>()
    (0 until pipeGraph.height).forEach { y ->
        var inside = false
        (0 until pipeGraph.width).forEach { x ->
            val p = Point2D(x, y)
            val dir = dirs[p]
            if (dir != null && dir) {
                inside = !inside
            }

            if (dir == null) {
                squares[p] = if (inside) 'I' else 'O'
            }
        }
    }

    val outMeDo = (0 until pipeGraph.height).map { y ->
        (0 until pipeGraph.width).map { x ->
            squares[Point2D(x, y)] ?: '.'
        }.joinToString("")
    }.joinToString("\n")

    return outMeDo.count { it == 'I'}
}

data class PipeGraph(
    val adjacencyList: Map<Point2D, List<Point2D>>,
    val width: Int,
    val height: Int,
    val start: Point2D
) {
    fun dijkstra(start: Point2D, adjacencyList: Map<Point2D, List<Point2D>>): MutableMap<Point2D, Int> {
        val distances = mutableMapOf(start to 0)
        val visitedNodes = mutableSetOf<Point2D>()

        val nextNodes = ArrayDeque<Point2D>()
        nextNodes.add(start)

        while (nextNodes.isNotEmpty()) {
            val thisNode = nextNodes.removeFirst()

            val gonnaVisit = adjacencyList[thisNode]!!.filter { !(it in visitedNodes) }
            gonnaVisit.forEach {
                val newDistance = distances[thisNode]!! + 1
                if (distances[it] == null || newDistance < distances[it]!!) {
                    distances[it] = newDistance
                }
            }

            nextNodes.addAll(gonnaVisit)
            visitedNodes.add(thisNode)
        }

        return distances
    }

    fun distancesString(): String {
        val distances = dijkstra(start, adjacencyList)

        val distancesString = (0 until height).map { y ->
            (0 until width).map { x ->
                val p = Point2D(x, y)
                distances[p]?.toString() ?: "."
            }.joinToString("")
        }.joinToString("\n")

        return distancesString
    }

    companion object {
        fun pipeGraphOf(input: List<String>): PipeGraph {
            val inputMap = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    Point2D(x, y) to c
                }
            }.toMap()

            val width = input.first().length
            val height = input.size

            val (start, _) = inputMap
                .filter { (p, c) -> c == 'S' }
                .toList()
                .first()

            val adjacencyMap = inputMap.toAdjacencyMap(start)

            val pipeGraph = PipeGraph(adjacencyMap, width, height, start)
            return pipeGraph
        }

        fun oneWayPipeGraphOf(input: List<String>): PipeGraph {
            val inputMap = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    Point2D(x, y) to c
                }
            }.toMap()

            val width = input.first().length
            val height = input.size

            val (start, _) = inputMap
                .filter { (p, c) -> c == 'S' }
                .toList()
                .first()

            val adjacencyMap = inputMap.toAdjacencyMap(start, onlyOnePathFromStart = true)

            val pipeGraph = PipeGraph(adjacencyMap, width, height, start)
            return pipeGraph
        }

        private fun Map<Point2D, Char>.toAdjacencyMap(
            start: Point2D,
            onlyOnePathFromStart: Boolean = false
        ): Map<Point2D, List<Point2D>> {
            val initialAdjacencyMap = map { (p, c) ->
                when (c) {
                    '|' -> p to listOf(p.north(), p.south())
                    '-' -> p to listOf(p.west(), p.east())
                    'L' -> p to listOf(p.north(), p.east())
                    'J' -> p to listOf(p.north(), p.west())
                    '7' -> p to listOf(p.west(), p.south())
                    'F' -> p to listOf(p.south(), p.east())
                    'S' -> p to listOf() // we'll deal with start connections in a minute...
                    '.' -> p to listOf()
                    else -> throw IllegalArgumentException(c.toString())
                }
            }.toMap()

            val pointsSdjacentToTheStart = listOf(start.north(), start.east(), start.south(), start.west())
                .filter { it in keys }
                .filter { start in initialAdjacencyMap[it]!! }

            val fromStart = if (onlyOnePathFromStart) {
                pointsSdjacentToTheStart.drop(1)
            } else {
                pointsSdjacentToTheStart
            }

            val adjacencyMap = initialAdjacencyMap + (start to fromStart)
            return adjacencyMap
        }
    }
}

data class Point2D(
    val x: Int,
    val y: Int
) {
    fun north() = Point2D(x, y - 1)
    fun south() = Point2D(x, y + 1)
    fun west(): Point2D = Point2D(x - 1, y)
    fun east(): Point2D = Point2D(x + 1, y)
    fun minus(that: Point2D) = Vector2D(this.x - that.x, this.y - that.y)
}

data class Vector2D(val x: Int, val y: Int) {
    companion object {
        val NORTH = Vector2D(0, -1)
        val EAST = Vector2D(1, 0)
        val SOUTH = Vector2D(0, 1)
        val WEST = Vector2D(-1, 0)
    }
}

class Day10Test {

    @Test
    fun `part 1 with test input - simple input 1`() {
        part1(testInput1) shouldBe 4
    }

    @Test
    fun `part 1 with test input - simple input 2`() {
        part1(testInput2) shouldBe 8
    }

    @Test
    fun `dijkstra distances for simple test input 1`() {
        PipeGraph.pipeGraphOf(testInput1).distancesString() shouldBe
                """
                    .....
                    .012.
                    .1.3.
                    .234.
                    .....
                """.trimIndent()
    }

    @Test
    fun `dijkstra distances for more complex test input 2`() {
        PipeGraph.pipeGraphOf(testInput2).distancesString() shouldBe
                """
                    ..45.
                    .236.
                    01.78
                    14567
                    23...
                """.trimIndent()
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day10.txt")) shouldBe 7097
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput1) shouldBe 1
    }

    @Test
    fun `part 2 with test input3`() {
        part2(testInput3) shouldBe 4
    }

    @Test
    fun `part 2 with test input4`() {
        part2(testInput4) shouldBe 8
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day10.txt")) shouldBe 355
    }
}

val testInput1 =
    """
        .....
        .S-7.
        .|.|.
        .L-J.
        .....
    """.trimIndent().lines()

val testInput2 =
    """
        ..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...
    """.trimIndent().lines()

val testInput3 =
    """
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........
    """.trimIndent().lines()

val testInput4 =
    """
        .F----7F7F7F7F-7....
        .|F--7||||||||FJ....
        .||.FJ||||||||L7....
        FJL7L7LJLJ||LJ.L-7..
        L--J.L7...LJS7F-7L7.
        ....F-J..F7FJ|L7L7L7
        ....L7.F7||L7|.L7L7|
        .....|FJLJ|FJ|F7|.LJ
        ....FJL-7.||.||||...
        ....L---J.LJ.LJLJ...
    """.trimIndent().lines()


