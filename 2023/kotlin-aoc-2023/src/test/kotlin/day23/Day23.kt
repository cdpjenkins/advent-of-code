package day23

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.max
import kotlin.test.Ignore

private fun part1(input: List<String>) =
    Graph
        .of(input)
        .findLongestPath()

private fun part2(input: List<String>): Int {
    val inputWithoutTheSlopes =
        input.map { it.replace("v", ".") }
            .map { it.replace(">", ".") }
    val contractedGraph =
        Graph.of(inputWithoutTheSlopes)
            .contractThyself()

    return contractedGraph.findLongestPath()
}

private fun neighoursOf(
    c: Char,
    x: Int,
    y: Int,
    inputMap: Map<Point, Char>
): List<Point> {
    return if (c == '#') {
        emptyList()
    } else {
        when (c) {
            '>' -> listOf(Point(x + 1, y))
            'v' -> listOf(Point(x, y + 1))
            else -> {
                Point(x, y).neighbours()
                    .filter { inputMap[it] != null && inputMap[it] != '#' }
            }
        }
    }
}

data class Path(
    val currentVertex: Int,
    val previousBVertices: BitSet,
    val length: Int
) {
    fun isComplete(endNode: Int) = currentVertex == endNode
    fun andThen(newEdge: Edge): Path {
        val newPreviousNodes = previousBVertices.clone() as BitSet
        newPreviousNodes.set(newEdge.targetVertex)

        return Path(newEdge.targetVertex, newPreviousNodes, length + newEdge.weight)
    }

    companion object {
        fun startingAt(startNode: Int, numNodes: Int): Path {
            val nodes = BitSet(numNodes)
            nodes.set(startNode)
            return Path(
                currentVertex = startNode,
                previousBVertices = nodes,
                length = 0
            )
        }
    }
}

data class Edge(
    val targetVertex: Int,
    val weight: Int = 1
)

data class Graph(
    val width: Int,
    val height: Int,
    val graph: List<List<Edge>>,
    val startVertex: Int,
    val endVertex: Int
) {

    fun contractThyself(): Graph {
        val VerticesToKeep =
            graph.withIndex()
                .filter { (n, neighbours) -> neighbours.size >= 3 || n == startVertex || n == endVertex }
                .map { (i, neighbours) -> i to neighbours }
                .toMap()

        val mutableGraph = graph.toMutableList()
        VerticesToKeep.forEach { (i, neighbours) ->
            val newNeighbours = neighbours.map { neighbour ->
                // terrible naming!
                findContractionTarget(from = i, neighbour = neighbour.targetVertex)
            }

            mutableGraph[i] = newNeighbours
        }

        mutableGraph.indices.forEach {
            if (it !in VerticesToKeep.keys) {
                mutableGraph[it] = emptyList()
            }
        }

        return this.copy(graph = mutableGraph.toList())
    }

    /** Basically we're going to perform edge contraction by removing any vertex that are not branching points... if
     * I'm at zertex A and going to vertex B means that the only place I can go next is to vertex C and then to vertex
     * D then I just remove the intermediate vertices and edges and just add an edge from vertex A directly to vertex D
     * (with weight equal to the sum of the weights of the edges that I just eliminated).
     */
    private fun findContractionTarget(from: Int, neighbour: Int, weight: Int = 1): Edge {
        val nextNeighbours =
            graph[neighbour]
                .filter { it.targetVertex != from }

        return if (nextNeighbours.size == 1) {
            // ha in the general case, the new weight should be weight + the weight of the edge that we just eliminated
            // but we forgot to make that information available to this function :-)
            findContractionTarget(neighbour, nextNeighbours.first().targetVertex, weight + 1)
        } else {
            Edge(neighbour, weight)
        }
    }

    fun findLongestPath(): Int {
        val activePaths = mutableListOf(Path.startingAt(startVertex, width * height))
        var maxPathLength = 0

        while (activePaths.isNotEmpty()) {
            val thisPath = activePaths.removeLast()

            if (thisPath.isComplete(endVertex)) {
                maxPathLength = max(maxPathLength, thisPath.length)
            } else {
                val nextVertices = graph[thisPath.currentVertex]
                    .filter { !thisPath.previousBVertices[it.targetVertex] }

                val nextPaths = nextVertices.map { thisPath.andThen(it) }

                activePaths.addAll(nextPaths)
            }
        }

        return maxPathLength
    }

    companion object {
        fun of(input: List<String>): Graph {
            val height = input.size
            val width = input.first().length

            val inputMap = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) -> Point(x, y) to c }
            }.toMap()

            val startPoint = (0..<width)
                .map { Point(it, 0) }
                .first { inputMap[it] == '.' }

            val endPoint = (0..<width)
                .map { Point(it, height - 1) }
                .first { inputMap[it] == '.' }

            val neighboursMap = inputMap.map { (p, c) ->
                val (x, y) = p
                val neighbours = neighoursOf(c, x, y, inputMap)
                p to neighbours
            }.toMap()

            val graphMutable = MutableList<List<Edge>>(width * height) { listOf() }
            (0..<height).forEach { y ->
                (0..<width).forEach { x ->
                    val i = y * width + x
                    val p = Point(x, y)

                    graphMutable[i] = neighboursMap[p]!!.map { (nx, ny) ->
                        Edge(ny * width + nx)
                    }
                }
            }

            val actualGraph = Graph(width, height, graphMutable.toList(), startPoint.toIndex(height), endPoint.toIndex(height))
            return actualGraph
        }

    }
}

data class Point(val x: Int, val y: Int) {
    fun toIndex(height: Int) = y * height + x

    fun neighbours() =
        listOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x, y - 1)
        )
}

class Day23Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 94
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day23.txt")) shouldBe 2018
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 154
    }

    @Ignore  // far too inefficient to run every time
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day23.txt")) shouldBe 6406
    }
}

val testInput =
    """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent().lines()

