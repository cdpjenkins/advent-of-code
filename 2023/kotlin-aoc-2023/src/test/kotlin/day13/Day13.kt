package day13

import FileUtil.readInputFileToList
import ListUtils.splitByBlank
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val patterns = input
        .splitByBlank()
        .map { it.parse() }

    val rowMirrors =
        patterns
            .map { it.findRowMirror() }
            .filterNotNull()

    val columnMirrors =
        patterns
            .map { it.findColumnMirror() }
            .filterNotNull()

    return columnMirrors.sum() + rowMirrors.sum() * 100
}

private fun part2(input: List<String>) =
    input.splitByBlank()
        .map { it.parse() }
        .map { it.findMirrorWithSmudge() }
        .filterNotNull()
        .sum()

private fun Map<Point2D, Char>.findMirrorWithSmudge(): Int? {
    val originalColMirror = this.findColumnMirror()
    val originalRowMirror = this.findRowMirror()

    return (0 until height()).flatMap { y ->
        (0 until width()).map { x ->
            val smudged = smudgeAt(Point2D(x, y))

            val colMirror = smudged.findAllColumnMirrors().firstOrNull { it != originalColMirror }
            val rowMirror = smudged.findAllRowMirrors().firstOrNull { it != originalRowMirror }

            if (colMirror != null && colMirror != originalColMirror) {
                colMirror
            } else {
                if (rowMirror != null && rowMirror != originalRowMirror) {
                    rowMirror * 100
                } else {
                    null
                }
            }
        }
    }.filterNotNull().firstOrNull()
}

private fun  Map<Point2D, Char>.findRowMirror() = (1 until height()).firstOrNull { this.hasRowMirrorAt(it) }
private fun  Map<Point2D, Char>.findColumnMirror() = (1 until width()).firstOrNull { this.hasColumnMirrorAt(it) }

private fun  Map<Point2D, Char>.findAllRowMirrors() = (1 until height()).filter { this.hasRowMirrorAt(it) }
private fun  Map<Point2D, Char>.findAllColumnMirrors() = (1 until width()).filter { this.hasColumnMirrorAt(it) }

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

private fun Map<Point2D, Char>.string(): String {
    return (0 until height()).map {
        this.row(it)
    }.joinToString("\n")
}

private fun Map<Point2D, Char>.smudgeAt(pos: Point2D): Map<Point2D, Char> {
    val mutableMap = this.toMutableMap()
    mutableMap[pos] = mutableMap[pos].smudge()
    return mutableMap.toMap()
}

private fun Char?.smudge() = if (this == '.') '#' else '.'

class Day13Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 405
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day13.txt")) shouldBe 26957
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 400
    }

    @Ignore   // this is too inefficient to run all the time!
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day13.txt")) shouldBe 42695
    }

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
        mapWithColumnMirror.findColumnMirror() shouldBe 5
    }

    @Test
    fun `can invert at pos`() {
        """
            ...
            ...
            ...
        """.trimIndent().lines().parse()
            .smudgeAt(Point2D(1, 1)).string() shouldBe
        """
            ...
            .#.
            ...
        """.trimIndent()
    }

    @Test
    fun `smudge pattern with column mirror to get row mirror`() {
        mapWithColumnMirror.smudgeAt(Point2D(0, 0)).string() shouldBe
                """
                    ..##..##.
                    ..#.##.#.
                    ##......#
                    ##......#
                    ..#.##.#.
                    ..##..##.
                    #.#.##.#.
                """.trimIndent()

        mapWithColumnMirror.smudgeAt(Point2D(0, 0)).findRowMirror() shouldBe 3
    }

    @Test
    fun `smudged tricky input should have mirror at column 5`() {
        trickyInput.findMirrorWithSmudge() shouldBe 8
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

val trickyInput =
    """
        ## #.#.##.#.# #
        .. ##.####.## .
        ## ...#..#... #
        .. ...#..#... .
        ## ##.####.#. #
        .. #.#.##.#.# .
        ## ###....### #
        .. ####..#### .
        .. ########## .
        ## ..##..##.. #
        .. .#......#. .
        .. .#.####.#. .
        ## ####..#### #
        ## #.######.# #
        ## ..##..##.. #
    """.trimIndent().lines().parse()
