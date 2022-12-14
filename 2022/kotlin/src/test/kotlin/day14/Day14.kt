package day14

import FileUtil.readInputFileToList
import Point2D
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day14 {
    @Test
    internal fun `parses input correctly`() {
        val input = testInput

        val ston = parseInput(input)

        val stringMeDo = ston.dump()

        stringMeDo shouldBe
                """
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    ........#.
                    #########.
                """.trimIndent()
    }

    @Test
    internal fun `drops sand correctly`() {
        val input = testInput

        val grid = parseInput(input)

        val gridSeq = generateSequence(grid) { dropSand(it) }

        val finalState = gridSeq.last().dump()
        finalState shouldBe
                """
                    ......o...
                    .....ooo..
                    ....#ooo##
                    ...o#ooo#.
                    ..###ooo#.
                    ....oooo#.
                    .o.ooooo#.
                    #########.
                """.trimIndent()
    }

    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val grid = parseInput(input)

        val gridSeq = generateSequence(grid) { dropSand(it) }

        val finalState = gridSeq.last()
        val numGrains = finalState.squares.values.filter { it == 'o' }.size

        numGrains shouldBe 24
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val grid = parseInput(input)

        val gridSeq = generateSequence(grid) { dropSand(it) }

        val finalState = gridSeq.last()
//        println(finalState.dump())

        val numGrains = finalState.squares.values.filter { it == 'o' }.size

        numGrains shouldBe 715
    }


    private fun dropSand(grid: Grid): Grid? {
        if (grid.squares[Point2D(500, 0)] == 'o') {
            return null
        }

        val traceSandPath = grid.traceSandPath(Point2D(500, 0)).toList()
        val pointWhereItLanded = traceSandPath.last()
        val newGrid = if (grid.isOnGrid(pointWhereItLanded)) {
            grid.addSandGrainAt(pointWhereItLanded)
        } else {
            null
        }
        return newGrid
    }

    private fun parseInput(input: List<String>): Grid {
        val lineSegments = input.flatMap { it.parseLineSegments() }

        val ston = mutableMapOf<Point2D, Char>()
        lineSegments.forEach { it.drawOn(ston) }
        return Grid(ston)
    }
}

data class Grid(val squares: Map<Point2D, Char>) {
    private val minX = squares.keys.map { it.x }.min()
    private val minY = squares.keys.map { it.y }.min()
    private val maxX = squares.keys.map { it.x }.max()
    private val maxY = squares.keys.map { it.y }.max()

    fun dump(): String {
        val stringBuilder = StringBuilder()

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                stringBuilder.append(squares.getOrElse(Point2D(x, y), { '.' }))
            }
            stringBuilder.append("\n")
        }

        return stringBuilder
            .toString()
            .dropLast(1)
    }

    fun traceSandPath(startPoint: Point2D): Sequence<Point2D> {
        val path =
            generateSequence(startPoint) { fall(it) }
                .takeWhile { !isFallingForever(it) }
        return path
    }

    private fun fall(point2D: Point2D): Point2D? {
        val below = below(point2D)
        val downAndLeft = downAndLeft(point2D)
        val downAndRight = downAndRight(point2D)

        return listOf(below, downAndLeft, downAndRight)
            .firstOrNull() { squares[it].isFree() }
    }

    private fun below(point2D: Point2D): Point2D = Point2D(point2D.x, point2D.y + 1)
    private fun downAndLeft(point2D: Point2D): Point2D = Point2D(point2D.x - 1, point2D.y + 1)
    private fun downAndRight(point2D: Point2D): Point2D = Point2D(point2D.x + 1, point2D.y + 1)

    private fun Char?.isFree() = this == '.' || this == null
    internal fun isOnGrid(pos: Point2D): Boolean {
        return pos.x in minX..maxX &&
                pos.y <= maxY
    }

    internal fun isFallingForever(pos: Point2D) = pos.y > maxY + 1

    fun addSandGrainAt(pointWhereItLanded: Point2D): Grid {
        val mutableSquares = squares.toMutableMap()
        mutableSquares[pointWhereItLanded] = 'o'
        return Grid(mutableSquares.toMap())
    }
}


private fun String.parseLineSegments(): List<LineSegment> {
    val sections = this.split(" -> ")
    val points = sections.map { it.parsePoint() }
    val lineSegments =
        points.windowed(2, 1)
            .map { (p1, p2) -> LineSegment(p1, p2) }

    return lineSegments
}

private fun LineSegment.drawOn(ston: MutableMap<Point2D, Char>) {
    val points: List<Point2D> = when (this.direction) {
        Direction.UP -> (start.y downTo end.y).map { y -> Point2D(start.x, y) }
        Direction.DOWN -> (start.y..end.y).map { y -> Point2D(start.x, y) }
        Direction.LEFT -> (start.x downTo end.x).map { x -> Point2D(x, start.y) }
        Direction.RIGHT -> (start.x..end.x).map { x -> Point2D(x, start.y) }
    }

    points.forEach { p -> ston[p] = '#' }
}


private fun String.parsePoint(): Point2D {
    val (x, y) = this.parseUsingRegex(SECTION_REGEX)

    return Point2D(x.toInt(), y.toInt())
}

data class LineSegment(val start: Point2D, val end: Point2D) {
    val direction: Direction =
        if (start.x == end.x) {
            if (start.y > end.y) {
                Direction.UP
            } else {
                Direction.DOWN
            }
        } else {
            if (start.x > end.x) {
                Direction.LEFT
            } else {
                Direction.RIGHT
            }
        }
}

enum class Direction { UP, DOWN, LEFT, RIGHT }

val SECTION_REGEX = "(\\d+),(\\d+)".toRegex()

val testInput =
    """
        498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent().lines()

val realInput = readInputFileToList("day14.txt")
