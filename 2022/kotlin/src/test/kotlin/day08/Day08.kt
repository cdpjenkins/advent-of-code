package day08

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day08 {

    @Test
    internal fun `part 1 tast data`() {
        numTreesVisible(testInput) shouldBe 21
    }

    @Test
    internal fun `part 1 real data`() {
        numTreesVisible(realInput) shouldBe 1698
    }

    private fun numTreesVisible(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        val gridMap: Map<Point2D, Int> = input.withIndex().flatMap { (y, row) ->
            row.withIndex().map { (x, c) ->
                Point2D(x, y) to c.toString().toInt()
            }
        }.toMap()

        val grid = Grid(gridMap, width, height)
        val visibleTrees: Map<Point2D, Int> = grid.trees.filter { (pos, _) -> grid.isVisible(pos) }

        return visibleTrees.size
    }
}

data class Point2D(
    val x: Int,
    val y: Int
)

data class Grid(
    val trees: Map<Point2D, Int>,
    val width: Int,
    val height: Int
) {
    fun isVisible(pos: Point2D): Boolean {
        return this.isVisibleNorth(pos) ||
                this.isVisibleEast(pos) ||
                this.isVisibleSouth(pos) ||
                this.isVisibleWest(pos)
    }

    private fun treeAt(pos: Point2D): Int = trees[pos]!!
    private fun treeAt(x: Int, y: Int): Int = trees[Point2D(x, y)]!!

    private fun isVisibleWest(pos: Point2D) = goWest(pos).none { it >= treeAt(pos) }
    private fun isVisibleSouth(pos: Point2D) = goSouth(pos).none { it >= treeAt(pos) }
    private fun isVisibleEast(pos: Point2D) = goEast(pos).none { it >= treeAt(pos) }
    private fun isVisibleNorth(pos: Point2D) = goNorth(pos).none { it >= treeAt(pos) }

    private fun goWest(pos: Point2D): List<Int> = (pos.x-1 downTo 0).map { x -> treeAt(x, pos.y) }
    private fun goSouth(pos: Point2D) = (pos.y+1 .. height-1).map { y -> treeAt(pos.x, y) }
    private fun goEast(pos: Point2D) = (pos.x+1..width-1).map { x-> treeAt(x, pos.y) }
    private fun goNorth(pos: Point2D): List<Int> = (pos.y-1 downTo 0).map { y -> treeAt(pos.x, y) }
}

val testInput =
    """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent().lines()

val realInput = readInputFileToList("day08.txt")