package day20

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import utils.parseToGrid
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val (grid, start, end) = parse(input)

    return cheatsThatSaveAtLeast100(grid, start, end).size
}

private fun cheatsThatSaveAtLeast100(
    grid: Map<Vector2D, Char>,
    start: Vector2D,
    end: Vector2D,
): List<Pair<Vector2D, Int>> {
    val fastestRoute = grid.findRoute(start, end)
    val fastestRouteWithoutCheats = fastestRoute.size - 1

    val timePerCheat =
        grid.filter { (p, c) -> c == '#' }

            .map { (p, c) ->
                val mutableGrid = grid.gridWithWallWipedOutAt(p)

                val fastestRouteWithThisCheat = mutableGrid.findRoute(start, end).size - 1
                p to fastestRouteWithThisCheat
            }

    val timeSavedPerCheat = timePerCheat.map { (p, l) ->
        p to fastestRouteWithoutCheats - l
    }

    return timeSavedPerCheat.sortedBy { (p, saved) -> saved }
        .filter { (p, saved) -> saved >= 100 }
}

private fun Map<Vector2D, Char>.gridWithWallWipedOutAt(p: Vector2D): MutableMap<Vector2D, Char> {
    val mutableGrid = toMutableMap()
    mutableGrid[p] = '.'
    return mutableGrid
}

private fun parse(input: List<String>): Triple<Map<Vector2D, Char>, Vector2D, Vector2D> {
    val grid = input.parseToGrid()

    val start = grid.entries.find { (k, v) -> v == 'S' }!!.key
    val end = grid.entries.find { (k, v) -> v == 'E' }!!.key
    return Triple(grid, start, end)
}

private fun Map<Vector2D, Char>.asStringWith(tharPaths: Collection<Vector2D>): String {
    return (0..this.keys.maxOf { it.y }).map { y ->
        (0..this.keys.maxOf { it.x }).map { x ->
            val p1 = Vector2D(x, y)
            tharPaths.firstOrNull { it == p1 }
                ?.let { 'O' }
                ?: this[p1]!!
        }.joinToString("")
    }.joinToString("\n")
}

private fun Map<Vector2D, Char>.findRoute(start: Vector2D, end: Vector2D): List<Vector2D> {
    val startThang = Thang(start, true)

    val openSet = mutableSetOf(startThang)
    val cameFrom = mutableMapOf<Thang, Thang>()
    val gScore = mutableMapOf(startThang to 0)
    val fScore = mutableMapOf(startThang to heuristicUsingManhattenDistance(startThang, end))

    while (openSet.isNotEmpty()) {
        val current = openSet.minByOrNull { fScore[it] ?: Integer.MAX_VALUE } ?: break

        if (current.pos == end) {
            return reconstructPath(cameFrom, current).map { it.pos }
        }

        openSet.remove(current)

        for (neighbor in neighbours(current)) {
            val tentativeGScore = (gScore[current] ?: Integer.MAX_VALUE) + 1

            if (tentativeGScore < (gScore[neighbor] ?: Integer.MAX_VALUE)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = tentativeGScore + heuristicUsingManhattenDistance(neighbor, end)

                if (neighbor !in openSet) {
                    openSet.add(neighbor)
                }
            }
        }
    }

    return emptyList() // No path found
}

data class Thang(val pos: Vector2D, val hasTunneled: Boolean = true) {

}

private fun heuristicUsingManhattenDistance(a: Thang, b: Vector2D): Int {
    return kotlin.math.abs(a.pos.x - b.x) + kotlin.math.abs(a.pos.y - b.y)
}

private fun Map<Vector2D, Char>.neighboursSton(point: Vector2D): List<Vector2D> {
    return listOf(
        Vector2D(point.x + 1, point.y),
        Vector2D(point.x - 1, point.y),
        Vector2D(point.x, point.y + 1),
        Vector2D(point.x, point.y - 1)
    )
}

private fun Map<Vector2D, Char>.neighbours(point: Thang): List<Thang> {
    if (point.hasTunneled) {
        return listOf(
            Thang(Vector2D(point.pos.x + 1, point.pos.y)),
            Thang(Vector2D(point.pos.x - 1, point.pos.y)),
            Thang(Vector2D(point.pos.x, point.pos.y + 1)),
            Thang(Vector2D(point.pos.x, point.pos.y - 1))
        ).filter { it.pos in this.keys && this[it.pos] != '#' }
    } else {
        return listOf(
            Thang(Vector2D(point.pos.x + 1, point.pos.y), true),
            Thang(Vector2D(point.pos.x - 1, point.pos.y), true),
            Thang(Vector2D(point.pos.x, point.pos.y + 1), true),
            Thang(Vector2D(point.pos.x, point.pos.y - 1), true)
        )
    }
}

private fun reconstructPath(cameFrom: Map<Thang, Thang>, current: Thang): List<Thang> {
    val path = mutableListOf(current)
    var currentNode = current

    while (currentNode in cameFrom) {
        currentNode = cameFrom[currentNode]!!
        path.add(0, currentNode)
    }

    return path
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day20Test {

    @Test
    fun `part 1 with real input`() {
        // good lord this is currently really really slow
        // good lord this is currently really really slow
        part1(readInputFileToList("day20.txt")) shouldBe 1521
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day20.txt")) shouldBe -1
    }

    @Test
    fun `total time without cheating for testData`() {
        val (grid, start, end) = parse(testInput)

        grid.findRoute(start, end).size - 1 shouldBe 84
    }
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
