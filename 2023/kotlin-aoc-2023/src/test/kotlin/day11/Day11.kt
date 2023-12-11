package day11

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.test.Test

private fun part1(input: List<String>): Long {
    val expandedStars = input.parseAndExpand()

    val twiceTotalDistance = expandedStars.map { thisStar ->
        expandedStars.map { thatStar ->
            thisStar.manhattenDistanceTo(thatStar)
        }.sum()
    }.sum()

    return twiceTotalDistance / 2
}

private fun List<Point2D>.StringRepresentation(): String {
    val expandedWidth = maxOf { it.x } + 1
    val expandedHeight = maxOf { it.y } + 1

    val outMeDo = (0 until expandedHeight).map { y ->
        (0 until expandedWidth).map { x ->
            if (Point2D(x, y) in this) '#' else '.'
        }.joinToString("")
    }.joinToString("\n")
    return outMeDo
}

private fun List<String>.parseAndExpand(
    factor: Long = 2
): List<Point2D> {
    val unexpandedStars = withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            if (c == '#') {
                Point2D(x.toLong(), y.toLong())
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

    val expandedStars = unexpandedStars.map { star ->
        val expandX = (blankCols.count { it < star.x }) * (factor - 1)
        val expandY = (blankRows.count { it < star.y }) * (factor - 1)

        Point2D(star.x + expandX, star.y + expandY)
    }
    return expandedStars
}

private fun part2(input: List<String>, factor: Long): Long {
    val expandedStars = input.parseAndExpand(factor)

    expandedStars
        .sortedBy { it.y }
        .forEach {
        println(it)
    }

    val twiceTotalDistance = expandedStars.map { thisStar ->
        expandedStars.map { thatStar ->
            thisStar.manhattenDistanceTo(thatStar)
        }.sum()
    }.sum()

    return twiceTotalDistance / 2
}

data class Point2D(
    val x: Long,
    val y: Long
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
    fun `part 2 with test input, factor is 10`() {
        part2(testInput, 10) shouldBe 1030L
    }
    @Test
    fun `part 2 with test input, factor is 100`() {
        part2(testInput, 100) shouldBe 8410L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day11.txt"), factor = 1000000) shouldBe -1
    }


    @Test
    fun `expand by 1`() {
        testInput
            .parseAndExpand()
            .StringRepresentation() shouldBe
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

    @Test
    fun `expand by 2`() {
        testInput
            .parseAndExpand()
            .StringRepresentation() shouldBe
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


//    @Test
//    fun `expand very small test input by 2`() {
//        testInputVerySmall
//            .parseAndExpand(factor = 2)
//            .StringRepresentation() shouldBe
//                """
//                    ....
//                    ....
//                    ....
//                    ...#
//                """.trimIndent()
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

val testInputVerySmall =
    """
        ...
        .#.
        ...
    """.trimIndent().lines()
