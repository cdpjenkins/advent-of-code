package day11

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.test.Test

private fun part1(input: List<String>): Int {



    val expandedStars = parseAndExpand(input)

    val twiceTotalDistance = expandedStars.map { thisStar ->
        expandedStars.map { thatStar ->
            thisStar.manhattenDistanceTo(thatStar)
        }.sum()
    }.sum()

    return twiceTotalDistance / 2
}

private fun gimmeAStringRepresentation(expandedStars: List<Point2D>): String {
    val expandedWidth = expandedStars.maxOf { it.x } + 1
    val expandedHeight = expandedStars.maxOf { it.y } + 1


    val outMeDo = (0 until expandedHeight).map { y ->
        (0 until expandedWidth).map { x ->
            if (Point2D(x, y) in expandedStars) '#' else '.'
        }.joinToString("")
    }.joinToString("\n")
    return outMeDo
}

private fun parseAndExpand(input: List<String>): List<Point2D> {
    val unexpandedStars = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            if (c == '#') {
                Point2D(x, y)
            } else {
                null
            }
        }
    }.filterNotNull()
        .toSet()

    val unexpandedWidth = unexpandedStars.maxOf { it.x } + 1
    val unexpandedHeight = unexpandedStars.maxOf { it.y } + 1

    val blankCols = (0 until unexpandedWidth).map { x ->
        val blankCol = unexpandedStars.none { it.x == x }
        if (blankCol) x else null
    }.filterNotNull()

    val blankRows = (0 until unexpandedHeight).map { y ->
        val blankRow = unexpandedStars.none { it.y == y }
        if (blankRow) y else null
    }.filterNotNull()

    println("blank rows: ${blankRows}")
    println("blank cols: ${blankCols}")


    val expandedStars = unexpandedStars.map { star ->
        val expandX = blankCols.count { it < star.x }
        val expandY = blankRows.count { it < star.y }

        println("expanding: $star")
        println("$expandX $expandY")

        println("${Point2D(star.x + expandX, star.y + expandY)}")

        Point2D(star.x + expandX, star.y + expandY)
    }
    return expandedStars
}

private fun part2(input: List<String>): Int {
    return 123
}

data class Point2D(
    val x: Int,
    val y: Int
) {
    fun manhattenDistanceTo(that: Point2D) = abs(this.x - that.x) + abs(this.y - that.y)
}

class Day11Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 374
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day11.txt")) shouldBe 9608724
    }

    @Test
    fun `lolz`() {
        val input = testInput
        val expandedStars = parseAndExpand(input)
        val outMeDo = gimmeAStringRepresentation(expandedStars)


        outMeDo shouldBe
                """
                    ....#........
                    .........#...
                    #............
                    .............
                    .............
                    ........#....
                    .#...........
                    ............#
                    .............
                    .............
                    .........#...
                    #....#.......
                """.trimIndent()
    }

//
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
}

val testInput =
    """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent().lines()
