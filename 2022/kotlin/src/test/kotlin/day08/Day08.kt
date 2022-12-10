package day08

import FileUtil.readInputFileToList
import Point2D
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

    @Test
    internal fun `part 2 test data`() {
        parseGrid(testInput).highestScenicScore() shouldBe 8
    }

    @Test
    internal fun `part 2 real data`() {
        parseGrid(realInput).highestScenicScore() shouldBe 672280
    }

    private fun numTreesVisible(input: List<String>): Int {
        val grid = parseGrid(input)
        return grid
            .trees
            .filter { (pos, _) -> grid.isVisible(pos) }
            .size
    }

    private fun parseGrid(input: List<String>): Grid {
        val width = input[0].length
        val height = input.size

        val gridMap: Map<Point2D, Int> = input.withIndex().flatMap { (y, row) ->
            row.withIndex().map { (x, c) ->
                Point2D(x, y) to c.toString().toInt()
            }
        }.toMap()

        val grid = Grid(gridMap, width, height)
        return grid
    }
}

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

    private fun viewWest(pos: Point2D) = view(treeAt(pos), goWest(pos))
    private fun viewSouth(pos: Point2D) = view(treeAt(pos), goSouth(pos))
    private fun viewEast(pos: Point2D) = view(treeAt(pos), goEast(pos))
    private fun viewNorth(pos: Point2D) = view(treeAt(pos), goNorth(pos))


    private fun view(thisTree: Int, trees: List<Int>): List<Int> {
        val treesViewedMinusOne = trees.takeWhile { it < thisTree }
        val remainingTrees = trees.dropWhile { it < thisTree }
        if (remainingTrees.isNotEmpty() ) {
            return treesViewedMinusOne + remainingTrees.first()
        } else {
            return treesViewedMinusOne
        }
    }

    fun scenicScore(pos: Point2D): Int {
        return viewWest(pos).size *
                viewSouth(pos).size *
                viewEast(pos).size *
                viewNorth(pos).size
    }

    fun highestScenicScore(): Int {
        val maxScore = trees.keys.map {
            scenicScore(it)
        }.max()

        return maxScore
    }
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