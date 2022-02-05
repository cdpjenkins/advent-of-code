package day20

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput
import utils.splitList

class Day20Test : FunSpec({
    test("part 1 with test data") {
        part1ProductOfIDsOfCorners(testInput) shouldBe 20899048083289L
    }

    test("part 1 with real data") {
        part1ProductOfIDsOfCorners(realInput("day20")) shouldBe 68781323018729L
    }
})

private fun part1ProductOfIDsOfCorners(input: List<String>): Long {
    val splitList = input.splitList { it.isEmpty() }

    val tiles = splitList.map {
        it.parseTile()
    }

    tiles.forEach { it.print() }

    val allPossibleBorders = tiles
        .flatMap {
            listOf(
                it.topBorder(),
                it.rightBorder(),
                it.bottomBorder(),
                it.leftBorder(),

                it.topBorder().reversed(),
                it.rightBorder().reversed(),
                it.bottomBorder().reversed(),
                it.leftBorder().reversed()
            )
        }
    val frequencies = allPossibleBorders
        .groupingBy { it }
        .eachCount()

    val productOfIDsOfCorners = tiles
        .filter { tile -> tile.numExteriorBorders(frequencies) == 2 }
        .map { it.id }
        .fold(1L) { acc, i -> acc * i }

    return productOfIDsOfCorners
}


private fun List<String>.parseTile(): Tile {
    val headerRow = this.first()
    val (id) = "^Tile ([0-9]+):$".toRegex().find(headerRow)!!.destructured
    val bodyRows = this.drop(1)
    val squares = bodyRows.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            Point(x, y) to c
        }
    }.toMap()

    return Tile(id.toInt(), squares)
}

data class Tile(val id: Int, val squares: Map<Point, Char>) {

    fun numExteriorBorders(frequencies: Map<String, Int>) =
        borders()
            .map { frequencies[it] }
            .count { it == 1 }

    fun print() {
        (0..9).forEach { y ->
            (0..9).forEach { x ->
                print(squares[Point(x, y)])
            }
            println()
        }
        println()
    }

    fun topBorder() = pointsRange(TOP_POINTS)
    fun rightBorder() = pointsRange(RIGHT_POINTS)
    fun bottomBorder() = pointsRange(BOTTOM_POINTS)
    fun leftBorder() = pointsRange(LEFT_POINTS)

    private fun pointsRange(points: List<Point>) =
        points.map { squares[it] }
            .joinToString("")

    private fun borders() = listOf(
        topBorder(),
        rightBorder(),
        bottomBorder(),
        leftBorder()
    )
}

data class Point(val x: Int, val y: Int)

val TOP_POINTS = (0..9).map { Point(it, 0) }
val RIGHT_POINTS = (0..9).map { Point(9, it) }
val BOTTOM_POINTS = (0..9).map { Point(it, 9) }
val LEFT_POINTS = (0..9).map { Point(0, it) }

val testInput =
    """
        Tile 2311:
        ..##.#..#.
        ##..#.....
        #...##..#.
        ####.#...#
        ##.##.###.
        ##...#.###
        .#.#.#..##
        ..#....#..
        ###...#.#.
        ..###..###

        Tile 1951:
        #.##...##.
        #.####...#
        .....#..##
        #...######
        .##.#....#
        .###.#####
        ###.##.##.
        .###....#.
        ..#.#..#.#
        #...##.#..

        Tile 1171:
        ####...##.
        #..##.#..#
        ##.#..#.#.
        .###.####.
        ..###.####
        .##....##.
        .#...####.
        #.##.####.
        ####..#...
        .....##...

        Tile 1427:
        ###.##.#..
        .#..#.##..
        .#.##.#..#
        #.#.#.##.#
        ....#...##
        ...##..##.
        ...#.#####
        .#.####.#.
        ..#..###.#
        ..##.#..#.

        Tile 1489:
        ##.#.#....
        ..##...#..
        .##..##...
        ..#...#...
        #####...#.
        #..#.#.#.#
        ...#.#.#..
        ##.#...##.
        ..##.##.##
        ###.##.#..

        Tile 2473:
        #....####.
        #..#.##...
        #.##..#...
        ######.#.#
        .#...#.#.#
        .#########
        .###.#..#.
        ########.#
        ##...##.#.
        ..###.#.#.

        Tile 2971:
        ..#.#....#
        #...###...
        #.#.###...
        ##.##..#..
        .#####..##
        .#..####.#
        #..#.#..#.
        ..####.###
        ..#.#.###.
        ...#.#.#.#

        Tile 2729:
        ...#.#.#.#
        ####.#....
        ..#.#.....
        ....#..#.#
        .##..##.#.
        .#.####...
        ####.#.#..
        ##.####...
        ##..#.##..
        #.##...##.

        Tile 3079:
        #.#.#####.
        .#..######
        ..#.......
        ######....
        ####.#..#.
        .#...#.##.
        #.#####.##
        ..#.###...
        ..#.......
        ..#.###...
    """.trimIndent().lines()
