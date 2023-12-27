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
        .findAllThePaths()

private fun part2(input: List<String>): Int {
    val tharGraph = Graph
        .of(
            input.map { it.replace("v", ".") }
                .map { it.replace(">", ".") }
        )

//    println(tharGraph.graph.count { it.size == 1 })
//    println(tharGraph.graph.count { it.size == 2 })
//    println(tharGraph.graph.count { it.size > 2 })

    val contractedGraph = tharGraph.contractThyself()

    contractedGraph.graph.withIndex().forEach { (i, e) ->
        if (e.isNotEmpty()) println("${i} -> ${e}")
    }

//    return -123

    // this is going to result in an enormous combinatorial explosion for part 2
    // Plan is to contract the graph such that the graph is weighted and only branching nodes are actually nodes
    // in the graph.
    // but...
    // I don't see how that is going to make this horrific brute-force search much more efficient. The graph isn't
    // going to branch any less than before. It's just going to have fewer pointless intermediate nodes...
    return contractedGraph
        .findAllThePaths()

    return 123
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
    val currentNode: Int,
    val previousNodes: BitSet
) {
    val length: Int = previousNodes.cardinality() - 1 // don't count the start node innut

    fun isComplete(endNode: Int) = currentNode == endNode
    fun andThen(newNode: Int): Path {
        val newPreviousNodes = previousNodes.clone() as BitSet
        newPreviousNodes.set(newNode)

        return Path(newNode, newPreviousNodes)
    }

    companion object {
        fun startingAt(startNode: Int, numNodes: Int): Path {
            val nodes = BitSet(numNodes)
            nodes.set(startNode)
            return Path(
                currentNode = startNode,
                previousNodes = nodes
            )
        }
    }
}

data class Edge(val targetVertex: Int)

data class Graph(
    val width: Int,
    val height: Int,
    val graph: List<List<Edge>>,
    val startNode: Int,
    val endNode: Int
) {

    fun contractThyself(): Graph {
        val nodesToKeep =
            graph.withIndex()
                .filter { (n, neighbours) ->
                    neighbours.size >= 3 || n == startNode || n == endNode
                }
                .map {(i, neighbours) -> i to neighbours }
                .toMap()

        val mutableGraph = graph.toMutableList()
        nodesToKeep.forEach { (i, neighbours) ->
            println("${i} ${neighbours}")

            val newNeighbours = neighbours.map { neighbour ->
                // terrible naming!
                val contractionTarget = findContractionTarget(from = i, neighbour = neighbour.targetVertex)

//                println("contraction target: ${contractionTarget}")

                Edge(contractionTarget)
            }

            mutableGraph[i] = newNeighbours
        }

        mutableGraph.indices.forEach {
            if (it !in nodesToKeep.keys) {
                mutableGraph[it] = emptyList()
            }
        }

        return this.copy(graph = mutableGraph.toList())
    }

    private fun findContractionTarget(from: Int, neighbour: Int): Int {
        val nextNeighbours =
            graph[neighbour]
                .filter { it.targetVertex != from }

        return if (nextNeighbours.size == 1) {
            findContractionTarget(neighbour, nextNeighbours.first().targetVertex)
        } else {
            neighbour
        }
    }

    fun findAllThePaths(): Int {
        val activePaths = mutableListOf(Path.startingAt(startNode, width * height))
        var maxPathLength = 0

        while (activePaths.isNotEmpty()) {

//            println("number of paths: ${activePaths.size}")
//            println("max len:         ${maxPathLength}")

            val thisPath = activePaths.removeLast()

            if (thisPath.isComplete(endNode)) {
                maxPathLength = max(maxPathLength, thisPath.length)
            } else {
                val nextNodes = graph[thisPath.currentNode]
                    .filter { !thisPath.previousNodes[it.targetVertex] }

                val nextPaths = nextNodes.map { thisPath.andThen(it.targetVertex) }

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

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 154
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day23.txt")) shouldBe -1
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

fun main() {
    val input = readInputFileToList("day23.txt")
    val tharResult = part2(input)
    println(tharResult)
}
