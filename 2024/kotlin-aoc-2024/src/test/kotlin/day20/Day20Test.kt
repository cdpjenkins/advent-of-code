package day20

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import utils.toGrid

private fun part1(input: List<String>) =
    input.toGrid()
        .possibleCheats(maximumCheatDistance = 2)
        .filter { it >= 100 }
        .size

private fun part2(input: List<String>) =
    input.toGrid()
        .possibleCheats(maximumCheatDistance = 20)
        .filter { it >= 100 }
        .size

private fun Map<Vector2D, Char>.possibleCheats(maximumCheatDistance: Int): List<Int> {
    val routeWithDistanceFromStart = findRouteWithNoCheatsUsingAStar().withIndex()

    val positionsWithDistanceFromStart = routeWithDistanceFromStart.associate { (i, p) -> p to i }

    val possibleCheats = routeWithDistanceFromStart.flatMap { (_, p) ->
        val possibleCheatsForPosition = findPossibleCheatsWithin(p, positionsWithDistanceFromStart, maximumCheatDistance)
        possibleCheatsForPosition.map { positionsWithDistanceFromStart[it]!! - positionsWithDistanceFromStart[p]!! - p.manhattanDistanceTo(it) }
    }
    return possibleCheats
}

private fun Map<Vector2D, Char>.end() = entries.find { (_, c) -> c == 'E' }!!.key
private fun Map<Vector2D, Char>.start() = entries.find { (_, c) -> c == 'S' }!!.key

fun findPossibleCheatsWithin(p: Vector2D, distances: Map<Vector2D, Int>, maxCheatDistance: Int) =
    distances.keys.filter { it.manhattanDistanceTo(p) <= maxCheatDistance }

private fun Map<Vector2D, Char>.findRouteWithNoCheatsUsingAStar(): List<Vector2D> {
    val startThang = start()
    val endThang = end()

    val openSet = mutableSetOf(startThang)
    val cameFrom = mutableMapOf<Vector2D, Vector2D>()
    val gScore = mutableMapOf(startThang to 0)
    val fScore = mutableMapOf(startThang to heuristicUsingManhattanDistance(startThang, endThang))

    while (openSet.isNotEmpty()) {
        val current = openSet.minByOrNull { fScore[it] ?: Integer.MAX_VALUE } ?: break

        if (current == endThang) {
            return reconstructPath(cameFrom, current)
        }

        openSet.remove(current)

        for (neighbour in neighbours(current)) {
            val tentativeGScore = (gScore[current] ?: Integer.MAX_VALUE) + 1

            if (tentativeGScore < (gScore[neighbour] ?: Integer.MAX_VALUE)) {
                cameFrom[neighbour] = current
                gScore[neighbour] = tentativeGScore
                fScore[neighbour] = tentativeGScore + heuristicUsingManhattanDistance(neighbour, endThang)

                if (neighbour !in openSet) {
                    openSet.add(neighbour)
                }
            }
        }
    }

    return emptyList() // No path found
}

private fun heuristicUsingManhattanDistance(a: Vector2D, b: Vector2D) = a.manhattanDistanceTo(b)

private fun Map<Vector2D, Char>.neighbours(point: Vector2D) =
    listOf(
        Vector2D(point.x + 1, point.y),
        Vector2D(point.x - 1, point.y),
        Vector2D(point.x, point.y + 1),
        Vector2D(point.x, point.y - 1)
    ).filter { it in this.keys && this[it] != '#' }

private fun reconstructPath(cameFrom: Map<Vector2D, Vector2D>, current: Vector2D): List<Vector2D> {
    val path = mutableListOf(current)
    var currentNode = current

    while (currentNode in cameFrom) {
        currentNode = cameFrom[currentNode]!!
        path.add(0, currentNode)
    }

    return path
}

class Day20Test {
    @Test
    fun `part 1 with test input`() {

        testInput.toGrid()
            .possibleCheats(maximumCheatDistance = 2)
            .frequenciesOfSavings() shouldBe
                listOf(
                    2 to 14,
                    4 to 14,
                    6 to 2,
                    8 to 4,
                    10 to 2,
                    12 to 3,
                    20 to 1,
                    36 to 1,
                    38 to 1,
                    40 to 1,
                    64 to 1
                )
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day20.txt")) shouldBe 1521
    }

    @Test
    fun `part 2 with test input`() {
        testInput.toGrid()
            .possibleCheats(20)
            .frequenciesOfSavings()
            .filter { it.first >= 50 } shouldBe
                listOf(
                    50 to 32,
                    52 to 31,
                    54 to 29,
                    56 to 39,
                    58 to 25,
                    60 to 23,
                    62 to 20,
                    64 to 19,
                    66 to 12,
                    68 to 14,
                    70 to 12,
                    72 to 22,
                    74 to 4,
                    76 to 3
                )
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day20.txt")) shouldBe 1013106
    }

    @Test
    fun `total time without cheating for testData`() {
        testInput.toGrid()
            .findRouteWithNoCheatsUsingAStar()
            .size - 1 shouldBe 84
    }

    private fun List<Int>.frequenciesOfSavings() =
        groupingBy { it }
            .eachCount()
            .entries
            .sortedBy { it.key }
            .filter { it.key > 0 }
            .map { it.key to it.value }
}

val testInput =
    """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent().lines()
