package day17

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Comparator
import java.util.PriorityQueue
import kotlin.math.abs

private fun part1(input: List<String>): Int {

    val grid = Grid.of(input)

    val shortestPath = grid.shortestPath()

    println(shortestPath)

    return shortestPath.drop(1).sumOf { grid.cost[it.pos]!! }
}

data class Node(
    val pos: Pos,
    val direction: Direction,
    val stepsLeft: Int
) {
    fun neighbours(): List<Node> {

        if (this == Node(pos=Pos(x=1, y=7), direction=Direction.DOWN, stepsLeft=0)) {
            println("poopoo")
        }

        val turns =
            direction
                .validTurns()
                .filter { it != direction }

        val weActuallyTurned = turns.map { Node(pos + it, it, 2) }

        if (stepsLeft > 0) {
            return weActuallyTurned + Node(pos + direction, direction, stepsLeft - 1)
        } else {
            return weActuallyTurned
        }
    }
}

enum class Direction {
    UP {
        override fun validTurns(): List<Direction> {
            return listOf(UP, LEFT, RIGHT)
        }

        override fun addTo(pos: Pos): Pos {
            return Pos(pos.x, pos.y - 1)
        }
    }, DOWN {
        override fun validTurns(): List<Direction> {
            return listOf(DOWN, LEFT, RIGHT)
        }

        override fun addTo(pos: Pos): Pos {
            return Pos(pos.x, pos.y + 1)
        }
    }, LEFT {
        override fun validTurns(): List<Direction> {
            return listOf(LEFT, UP, DOWN)
        }

        override fun addTo(pos: Pos): Pos {
            return Pos(pos.x - 1, pos.y)
        }
    }, RIGHT {
        override fun validTurns(): List<Direction> {
            return listOf(RIGHT, UP, DOWN)
        }

        override fun addTo(pos: Pos): Pos {
            return Pos(pos.x + 1, pos.y)
        }
    };

    abstract fun validTurns(): List<Direction>
    abstract fun addTo(pos: Pos): Pos;
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
    private val width: Int = cost.keys.maxOf { it.x } + 1
    private val height: Int = cost.keys.maxOf { it.y } + 1

    val startPos = Pos(0, 0)
    val target: Pos = Pos(width - 1, height - 1)

    private fun heuristic(a: Pos): Int {
        return 0 //a.manhattenDistanceTo(target)
    }

    fun shortestPath(): List<Node> {
        // A* FTW

        val startNode = Node(startPos, Direction.RIGHT, 3)

        // came from
        val previousMap: MutableMap<Node, Node> = mutableMapOf()

        val gScore: MutableMap<Node, Int> = mutableMapOf(startNode to 0)

        val fScore: MutableMap<Node, Int> = mutableMapOf(startNode to heuristic(startNode.pos))

        val openSet: PriorityQueue<Node> = PriorityQueue(Comparator { o1, o2 -> fScore[o1]!! - fScore[o2]!! })
        openSet.add(startNode)

        while (openSet.isNotEmpty()) {
            val current = openSet.remove()

            if (current.pos == target) {
                return reconstructPath(current, previousMap)
            }

            val neighbours = current.neighbours()
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
                    '#'
                } else {
                    cost[pos]
                }
            }.joinToString("")
        }.joinToString("\n")
    }

    private fun Pos.isLegalBasedOn(current: Pos, previous: MutableMap<Pos, Pos>): Boolean {

        if (previous[current] == null) {
            return true
        } else if (previous[previous[current]!!] ==  null) {
            return true
        } else if (previous[previous[previous[current]!!]!!] == null) {
            return true
        }

        val lastFiveBlocks = listOf(
            previous[previous[previous[current]!!]!!]!!,
            previous[previous[current]!!]!!,
            previous[current]!!,
            current,
            this
        )

        return if (lastFiveBlocks.all { it.x == lastFiveBlocks[0].x }) {
            false
        } else if (lastFiveBlocks.all { it.y == lastFiveBlocks[0].y }) {
            false
        } else {
            true
        }
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


private fun part2(input: List<String>): Int {
    return 123
}

class Day17Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 102
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day17.txt")) shouldBe -1
    }
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

    @Test
    fun `can find correct shortest path through test data`() {
        val grid = Grid.of(testInput)

        val shortestPath = grid.shortestPath()

        grid.asStringWithPath(shortestPath) shouldBe
                """
                    ###34####1323
                    32####35#5623
                    32552456###54
                    3446585845#52
                    4546657867##6
                    14385987984#4
                    44578769877#6
                    36378779796##
                    465496798688#
                    456467998645#
                    12246868655##
                    25465488877#5
                    43226746555##
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

        grid.asStringWithPath(grid.shortestPath()) shouldBe
                """
                    ##9
                    9##
                    99#
                """.trimIndent()
    }

    @Test
    fun `lalala`() {


        //0 6
        //5 9
        //11 15
        //16 18
        //33 25
        //38 41
        //44 44
        //47 49

        // f
        //   36   0 6
        //   54   5 9
        //   66   11 15
        //   53   16 18
        //   87   33 25
        //   53   38 41
        //   63   44 44
        //   35   47 49
        //   33   xx 52






        // g
        //   36   0 6
        //   54   5 9
        //   66   11 15
        //   53   16 18
        //   87   33 25
        //   53   38 41
        //   63   44 44
        //   35   47 49
        //   33   x  52

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

        val shortestPath = grid.shortestPath()
        println(shortestPath.sumOf { grid.cost[it.pos]!! } )

        shortestPath.forEach { println(it) }

        grid.asStringWithPath(grid.shortestPath()) shouldBe
                """
                    #6
                    #4
                    #6
                    ##
                    8#
                    5#
                    ##
                    #5
                    ##
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
