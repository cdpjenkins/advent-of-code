package day13

import FileUtil.readInputFileToList
import ListUtils.splitByBlank
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val patterns = input.splitByBlank().map {
        it.parse()
    }

    val rowMirrors =
        patterns
            .map { it.findRowMirror() }
            .filterNotNull()

    val columnMirrors =
        patterns
            .map { it.findColumnMirror() }
            .filterNotNull()

    val result = columnMirrors.filterNotNull().sum() + rowMirrors.filterNotNull().sum() * 100

    return result
}

private fun  Map<Point2D, Char>.findRowMirror() = (1 until height()).firstOrNull { this.hasRowMirrorAt(it) }
private fun  Map<Point2D, Char>.findColumnMirror() = (1 until width()).firstOrNull { this.hasColumnMirrorAt(it) }

private fun Map<Point2D, Char>.height() = this.keys.maxOf { it.y } + 1
private fun Map<Point2D, Char>.width() = this.keys.maxOf { it.x } + 1


private fun Map<Point2D, Char>.hasRowMirrorAt(row: Int) =
    (0 until height()).all { y ->
        this.row(row + y) == this.row(row - y - 1) ||
                this.row(row + y).isEmpty() ||
                this.row(row - y - 1).isEmpty()
    }

private fun Map<Point2D, Char>.hasColumnMirrorAt(column: Int) =
    (0 until width()).all { x ->
        this.column(column + x) == this.column(column - x - 1) ||
                this.column(column + x).isEmpty() ||
                this.column(column - x - 1).isEmpty()
    }

private fun Map<Point2D, Char>.row(y: Int): String =
    this
        .filter { (k, v) -> k.y == y }
        .map { (k, v) -> k.x to v }
        .toList()
        .sortedBy { (k, v) -> k }
        .map { (k, v) -> v }
        .joinToString("")

private fun Map<Point2D, Char>.column(x: Int): String =
    this
        .filter { (k, v) -> k.x == x }
        .map { (k, v) -> k.y to v }
        .toList()
        .sortedBy { (k, v) -> k }
        .map { (k, v) -> v }
        .joinToString("")

private fun Map<Point2D, Char>.dump() {
    (0 until height()).forEach {
        println(this.row(it))
    }
}

fun List<String>.parse(): Map<Point2D, Char> =
    withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            Point2D(x, y) to c
        }
    }.toMap()

data class Point2D(
    val x: Int,
    val y: Int
)

private fun part2(input: List<String>): Int {
    return 123
}

class Day13Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 405
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day13.txt")) shouldBe -1
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
    fun `can extract rows`() {
        mapWithRowMirror.row(0) shouldBe "#...##..#"
    }

    @Test
    fun `can find row mirror`() {
        mapWithRowMirror.findRowMirror() shouldBe 4
    }

    @Test
    fun `can extract column`() {
        mapWithColumnMirror.column(0) shouldBe "#.##..#"
    }

    @Test
    fun `can find column mirror`() {
        mapWithColumnMirror.hasColumnMirrorAt(5)

        mapWithColumnMirror.findColumnMirror() shouldBe 5
    }
}

val testInput =
    """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.
        
        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent().lines()

val mapWithRowMirror =
    """
        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent().lines().parse()

val mapWithColumnMirror =
    """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.
    """.trimIndent().lines().parse()