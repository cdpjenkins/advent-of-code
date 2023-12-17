package day17

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Comparator
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val grid = Grid.of(input)
    val shortestPath = grid.shortestPath(0 until 3)

    return shortestPath.drop(1).sumOf { grid.cost[it.pos]!! }
}

private fun part2(input: List<String>): Int {
    val grid = Grid.of(input)
    val shortestPath = grid.shortestPath(4 until 10)

    return shortestPath.drop(1).sumOf { grid.cost[it.pos]!! }
}

private fun <E> List<E>.findSegments() {
    var v = this.first()

    var counter = 0
    var i = 1

    println()
    println()

    while (i < this.size) {
        if (this[i] == v) {
            counter++
        } else {
            println(counter)

            if (counter < 4 || counter > 10) {
                throw IllegalStateException(counter.toString())
            }

            counter = 1
            v = this[i]
        }
        i++
    }
}

data class Node(
    val pos: Pos,
    val direction: Direction,
    val stepsTaken: Int = 0
) {

    fun neighbours(validStepsRange: IntRange): List<Node> {
        val turns =
            direction
                .validTurns()
                .filter { it != direction }

        val weActuallyTurned = turns.map { Node(pos + it, it, 1) }

        if (stepsTaken < validStepsRange.start) {
            return listOf(Node(pos + direction, direction, stepsTaken + 1))
        } else if (stepsTaken <= validStepsRange.endInclusive) {
            return weActuallyTurned + Node(pos + direction, direction, stepsTaken + 1)
        } else {
            return weActuallyTurned
        }
    }
}

enum class Direction {
    UP {
        override fun validTurns() = listOf(UP, LEFT, RIGHT)

        override fun addTo(pos: Pos) = Pos(pos.x, pos.y - 1)

        override fun toChar() = '^'
    }, DOWN {
        override fun validTurns() = listOf(DOWN, LEFT, RIGHT)

        override fun addTo(pos: Pos) = Pos(pos.x, pos.y + 1)

        override fun toChar() = 'v'
    }, LEFT {
        override fun validTurns() = listOf(LEFT, UP, DOWN)

        override fun addTo(pos: Pos) = Pos(pos.x - 1, pos.y)

        override fun toChar() = '<'
    }, RIGHT {
        override fun validTurns() = listOf(RIGHT, UP, DOWN)

        override fun addTo(pos: Pos) = Pos(pos.x + 1, pos.y)

        override fun toChar() = '>'
    };

    abstract fun validTurns(): List<Direction>
    abstract fun addTo(pos: Pos): Pos;
    abstract fun toChar(): Char
}

data class Pos(val x: Int, val y: Int) {
    fun manhattenDistanceTo(that: Pos): Int {
        return abs(this.x - that.x) +
                abs(this.y - that.y)
    }

    operator fun plus(direction: Direction): Pos {
        return direction.addTo(this)
    }
}

data class Grid(val cost: Map<Pos, Int>) {
    val width: Int = cost.keys.maxOf { it.x } + 1
    val height: Int = cost.keys.maxOf { it.y } + 1

    val startPos = Pos(0, 0)
    val target: Pos = Pos(width - 1, height - 1)

    private fun heuristic(a: Pos): Int {
        // this turns the algo from A* into Dijkstra's ... oops
        return 0 // a.manhattenDistanceTo(target)
    }

    // TODO - an IntRange actually might not be the best thing to use ere. It's not like there's a range and we just
    // need to be within that range. Rather, we do different things depending on whether we are before, inside or
    // after the range.
    fun shortestPath(validStepsRange: IntRange): List<Node> {
        // A* FTW
        val startNode = Node(startPos, Direction.RIGHT)

        val previousMap: MutableMap<Node, Node> = mutableMapOf()
        val gScore: MutableMap<Node, Int> = mutableMapOf(startNode to 0)
        val fScore: MutableMap<Node, Int> = mutableMapOf(startNode to heuristic(startNode.pos))

        val openSet: PriorityQueue<Node> = PriorityQueue(Comparator { o1, o2 -> fScore[o1]!! - fScore[o2]!! })
        openSet.add(startNode)

        while (openSet.isNotEmpty()) {
            val current = openSet.remove()

            if (current.pos == target) {
                if (current.stepsTaken < validStepsRange.start) {
                    // alas we can't stop!
                    continue
                }

                return reconstructPath(current, previousMap)
            }

            val neighbours = current.neighbours(validStepsRange)
            val neighboursInBounds = neighbours
                .filter { it.pos in cost.keys }

            neighboursInBounds
                .forEach { neighbour ->
                    val tentativeScore = gScore[current]!! + cost[neighbour.pos]!!

                    if (tentativeScore < (gScore[neighbour] ?: Int.MAX_VALUE)) {
                        previousMap[neighbour] = current
                        gScore[neighbour] = tentativeScore
                        fScore[neighbour] = tentativeScore + heuristic(neighbour.pos)

                        if (neighbour !in openSet) {
                            openSet.add(neighbour)
                        }
                    }
                }
        }

        throw IllegalStateException("arghghghggh")
    }

    private fun reconstructPath(target: Node, previous: MutableMap<Node, Node>): List<Node> {
        var current = target

        val path = mutableListOf(current)

        while (previous[current] != null) {
            current = previous[current]!!
            path.add(current)
        }

        return path.reversed()

    }

    fun asStringWithPath(shortestPath: List<Node>): String {
        val pathElemrnts = shortestPath.toSet()
        return (0 until height).map { y ->
            (0 until width).map { x ->
                val pos = Pos(x, y)
                if (pos in pathElemrnts.map { it.pos } ) {
                    pathElemrnts.first{ it.pos == pos}.direction.toChar()
//                    '#'
                } else {
                    cost[pos]
                }
            }.joinToString("")
        }.joinToString("\n")
    }

    companion object {
        fun of(input: List<String>): Grid {
            val values = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    Pos(x, y) to c.toString().toInt()
                }
            }.toMap()

            return Grid(values)
        }
    }
}

class Day17Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 102
    }

    @Ignore
    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day17.txt")) shouldBe 722
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 94
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {

        // 889 is too low


        // 894 ???

        part2(readInputFileToList("day17.txt")) shouldBe 894
    }

    @Test
    fun `part 2 shorted path on pathalogical input`() {
        val grid = Grid.of(
            """
                111111111111
                999999999991
                999999999991
                999999999991
                999999999991
            """.trimIndent().lines()
        )

        val shortestPath = grid.shortestPath(4 until 10)

        grid.asStringWithPath(shortestPath) shouldBe
                """
                    >>>>>>>>1111
                    9999999v9991
                    9999999v9991
                    9999999v9991
                    9999999v>>>>
                """.trimIndent()

        shortestPath.drop(1).sumOf { grid.cost[it.pos]!! } shouldBe 71
    }

    @Test
    fun `can find correct shortest path through test data`() {
        val grid = Grid.of(testInput)

        val shortestPath = grid.shortestPath(0 until 3)

        grid.asStringWithPath(shortestPath) shouldBe
                """
                    >>>34^>>>1323
                    32v>>>35v5623
                    32552456v>>54
                    3446585845v52
                    4546657867v>6
                    14385987984v4
                    44578769877v6
                    36378779796v>
                    465496798688v
                    456467998645v
                    12246868655<v
                    25465488877v5
                    43226746555v>
                """.trimIndent()
    }

    @Test
    fun `can find correct shortest path through really simple grid`() {
        val grid = Grid.of(
            """
                129
                922
                991
            """.trimIndent().lines()
        )

        grid.asStringWithPath(grid.shortestPath(0 until 3)) shouldBe
                """
                    >>9
                    9v>
                    99v
                """.trimIndent()
    }

    @Test
    fun `can find a path on interesting but simple input`() {
        val grid = Grid.of(
            """
                36
                54
                66
                53
                87
                53
                63
                35
                33
            """.trimIndent().lines()
        )

        grid.asStringWithPath(grid.shortestPath(0 until 3)) shouldBe
                """
                    >6
                    v4
                    v6
                    v>
                    8v
                    5v
                    <v
                    v5
                    v>
                """.trimIndent()
    }
}

val testInput =
    """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """.trimIndent().lines()
