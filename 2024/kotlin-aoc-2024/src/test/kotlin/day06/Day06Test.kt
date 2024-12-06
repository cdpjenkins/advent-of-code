package day06

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val (map, start) = input.parse()
    val poses = path(start, map)

    return poses.size
}

private fun part2(input: List<String>): Int {
    val (map, start) = input.parse()
    val poses = path(start, map)

    val posesThangie = poses - start



    return 123
}

private fun path(
    start: Point2D,
    map: Map<Point2D, Char>,
): Set<Point2D> =
    generateSequence(Guard(start, Direction.UP)) { it.move(map) }
        .takeWhile { it.isWithinBoundsOf(map) }
        .map { it.p }
        .toSet()

private fun pathNoCycles(
    start: Point2D,
    map: Map<Point2D, Char>,
): Set<Point2D> =
    generateSequence(Guard(start, Direction.UP)) { it.move(map) }
        .takeWhile { it.isWithinBoundsOf(map) && it.p != start }
        .map { it.p }
        .toSet()

private fun List<String>.parse(): Pair<Map<Point2D, Char>, Point2D> {
    val map = flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Point2D(x, y) to c }
    }.toMap()

    val start = map.entries.firstOrNull { (p, c) -> c == '^' }!!.key

    return Pair(map, start)
}

fun asString(map: Map<Point2D, Char>, poses: Set<Point2D>): String {
    return (0 .. map.keys.maxOf { it.y }).map { y ->
        (0 .. map.keys.maxOf { it.x }).map { x ->
            if (Point2D(x, y) in poses) {
                'X'
            } else {
                map[Point2D(x, y)]
            }
        }.joinToString("")
    }.joinToString("\n")
}

data class Guard(val p: Point2D, val d: Direction) {
    fun move(map: Map<Point2D, Char>): Guard {
        val possibleNewPosition = d.move(p)
        if (map[possibleNewPosition] == '#') {
            val newDirection = d.turnRight()
            val newPosition = newDirection.move(p)
            return Guard(newPosition, newDirection)
        } else {
            return Guard(possibleNewPosition, d)
        }
    }

    fun isWithinBoundsOf(map: Map<Point2D, Char>): Boolean {
        return p in map.keys
    }
}

data class Point2D(val x: Int, val y: Int)
enum class Direction {
    UP {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
        override fun move(p: Point2D) = Point2D(p.x, p.y - 1)
    },
    DOWN {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
        override fun move(p: Point2D) = Point2D(p.x, p.y + 1)
    },
    LEFT {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
        override fun move(p: Point2D) = Point2D(p.x - 1, p.y)
    },
    RIGHT {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
        override fun move(p: Point2D) = Point2D(p.x + 1, p.y)
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun move(p: Point2D): Point2D
}

private fun List<String>.width() = this[0].length
private fun List<List<String>>.height() = size

class Day06Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 41
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day06.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }

    @Test
    fun `generates correct path from test data`() {
        val (map, start) = testInput.parse()
        val poses = path(start, map)

        val posesString = asString(map, poses)

        posesString shouldBe """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ..XXXXX#X.
            ..X.X.X.X.
            .#XXXXXXX.
            .XXXXXXX#.
            #XXXXXXX..
            ......#X..
        """.trimIndent()

    }
}

val testInput =
    """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()
