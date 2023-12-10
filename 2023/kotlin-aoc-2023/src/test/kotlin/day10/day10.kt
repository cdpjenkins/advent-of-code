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
    return 123
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

            val initialAdjacencyMap = inputMap.map { (p, c) ->
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

            val (start, _) = inputMap
                .filter { (p, c) -> c == 'S' }
                .toList()
                .first()


            val pointsSdjacentToTheStart = listOf(start.north(), start.east(), start.south(), start.west())
                .filter { it in inputMap.keys }
                .filter { start in initialAdjacencyMap[it]!! }

            val adjacencyList = initialAdjacencyMap + (start to pointsSdjacentToTheStart)

            val pipeGraph = PipeGraph(adjacencyList, width, height, start)
            return pipeGraph
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

//    @Test
//    fun `part 1 with real input`() {
//        part1(readInputFileToList("day10.txt")) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }
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
