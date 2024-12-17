package day16

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import java.util.*

private fun part1(input: List<String>): Int {
    val (map, start, endPos) = parse(input)

    return findDCheapestPathByDijkstra(map, start, endPos)
}

private fun part2(input: List<String>): Int {
    val (map, start, endPos) = parse(input)

    return findAllCheapestPathsUsingModifiedDijkstra(map, start, endPos)
        .flatten()
        .map { it.pos }
        .toSet()
        .size
}

private fun parse(input: List<String>): Triple<Map<Vector2D, Char>, Reindeer, Vector2D> {
    val map = input.parseMap()

    val startPos = map.entries.first { (_, c) -> c == 'S' }.key
    val start = Reindeer(startPos, Direction.EAST)
    val endPos = map.entries.first { (_, c) -> c == 'E' }.key

    return Triple(map, start, endPos)
}

private fun List<String>.parseMap() =
    flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            Vector2D(x, y) to c
        }
    }.toMap()

fun findDCheapestPathByDijkstra(map: Map<Vector2D, Char>, reindeerStart: Reindeer, end: Vector2D): Int {
    val unvisited = PriorityQueue<Pair<Reindeer, Int>>(compareBy { it.second })
    val visited = mutableSetOf<Reindeer>()
    val distances = mutableMapOf<Reindeer, Int>().withDefault { Int.MAX_VALUE }
    val paths = mutableMapOf<Reindeer, MutableList<Reindeer>>()

    unvisited.offer(reindeerStart to 0)
    distances[reindeerStart] = 0

    while (unvisited.isNotEmpty()) {
        val (currentReindeer, distance) = unvisited.poll()

        if (currentReindeer.pos == end) {
            return distance
        }

        if (currentReindeer in visited) continue
        visited.add(currentReindeer)

        val possibleMoves = Move.entries.filter { map[it.move(currentReindeer).pos] != '#' }

        for (move in possibleMoves) {
            val nextReindeer = move.move(currentReindeer)

            val newDistance = distance + move.score()
            if (newDistance < distances.getValue(nextReindeer)) {
                distances[nextReindeer] = newDistance
                paths[nextReindeer] = mutableListOf(currentReindeer)
                unvisited.offer(nextReindeer to newDistance)
            } else if (newDistance == distances.getValue(nextReindeer)) {
                paths[nextReindeer]!!.add(currentReindeer)
            }
        }
    }

    return -1
}

fun findAllCheapestPathsUsingModifiedDijkstra(map: Map<Vector2D, Char>, reindeerStart: Reindeer, end: Vector2D): List<List<Reindeer>> {
    val unvisited = PriorityQueue<Pair<Reindeer, Int>>(compareBy { it.second })
    val visited = mutableSetOf<Reindeer>()
    val distances = mutableMapOf<Reindeer, Int>().withDefault { Int.MAX_VALUE }
    val parents = mutableMapOf<Reindeer, MutableList<Reindeer>>()

    unvisited.offer(reindeerStart to 0)
    distances[reindeerStart] = 0

    var pathLengthToEnd: Int? = null

    while (unvisited.isNotEmpty()) {
        val (currentReindeer, distance) = unvisited.poll()

        if (currentReindeer in visited) continue

        if (pathLengthToEnd != null && distance > pathLengthToEnd) {
            break
        }

        visited.add(currentReindeer)

        if (currentReindeer.pos == end) {
            pathLengthToEnd = distance
        } else {
            val possibleMoves = Move.entries.filter { map[it.move(currentReindeer).pos] != '#' }

            for (move in possibleMoves) {
                val nextReindeer = move.move(currentReindeer)

                val newDistance = distance + move.score()
                if (newDistance < distances.getValue(nextReindeer)) {
                    distances[nextReindeer] = newDistance
                    parents[nextReindeer] = mutableListOf(currentReindeer)
                    unvisited.offer(nextReindeer to newDistance)
                } else if (newDistance == distances.getValue(nextReindeer)) {
                    parents[nextReindeer]!!.add(currentReindeer)
                }
            }
        }
    }

    val paths = Direction.entries.flatMap {
        reconstructBackwardsPaths(parents, Reindeer(end, it))
    }

    return paths
}


private fun reconstructBackwardsPaths(parents: Map<Reindeer, List<Reindeer>>, end: Reindeer): List<List<Reindeer>> {
    fun reconstructPathInternal(current: Reindeer): List<List<Reindeer>> =
        parents[current]?.map { parent ->
            reconstructPathInternal(parent).flatMap { path -> path + current }
        } ?: listOf(listOf(current))

    return reconstructPathInternal(end)
}

private fun Map<Vector2D, Char>.asStringWith(tharPaths: Set<Vector2D>): String {
    return (0..this.keys.maxOf { it.y }).map { y ->
        (0..this.keys.maxOf { it.x }).map { x ->
            val p1 = Vector2D(x, y)
            tharPaths.firstOrNull { it == p1 }
                ?.let { 'O' }
                ?: this[p1]!!
        }.joinToString("")
    }.joinToString("\n")
}

data class Reindeer(val pos: Vector2D, val direction: Direction)
enum class Move {
    ROTATE_ANTICLOCKWISE {
        override fun score() = 1000
        override fun move(reindeerStart: Reindeer) = reindeerStart.copy(direction = reindeerStart.direction.turnLeft())
    },
    ROTATE_CLOCKWISE {
        override fun score() = 1000
        override fun move(reindeerStart: Reindeer) = reindeerStart.copy(direction = reindeerStart.direction.turnRight())
    },
    MOVE_FORWARD {
        override fun score() = 1
        override fun move(reindeerStart: Reindeer) =
            reindeerStart.copy(pos = reindeerStart.direction.move(reindeerStart.pos))
    };

    abstract fun score(): Int
    abstract fun move(reindeerStart: Reindeer): Reindeer
}

enum class Direction {
    NORTH {
        override fun turnLeft() = WEST
        override fun turnRight() = EAST
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(0, -1)
        override fun asChar() = '^'
    },
    SOUTH {
        override fun turnLeft() = EAST
        override fun turnRight() = WEST
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(0, 1)
        override fun asChar() = 'v'
    },
    EAST {
        override fun turnLeft() = NORTH
        override fun turnRight() = SOUTH
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(1, 0)
        override fun asChar() = '>'
    },
    WEST {
        override fun turnLeft() = SOUTH
        override fun turnRight() = NORTH
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(-1, 0)
        override fun asChar() = '<'
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun move(pos: Vector2D): Vector2D
    abstract fun asChar(): Char?
}

class Day16Test {
    @Test
    fun `part 1 with test input`() {
        part1(simpleTestInput) shouldBe 7036
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day16.txt")) shouldBe 88468
    }

    @Test
    fun `part 2 with simple test input`() {
        part2(simpleTestInput) shouldBe 45
    }

    @Test
    fun `part 2 with moar complex test input`() {
        part2(moarComplexTestInput) shouldBe 64
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day16.txt")) shouldBe 616
    }
}

val simpleTestInput =
    """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent().lines()

val moarComplexTestInput =
    """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent().lines()
