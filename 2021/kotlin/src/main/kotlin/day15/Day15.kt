package day15

import readInput
import java.util.*

fun main() {
    val input = readInput("Day15")

    val startTime = System.currentTimeMillis()
    println(day15Part1DijsktraMinPath(input))
    println(day15Part2DijkstraMinPathFiveTimesBigger(input))
    val endTime = System.currentTimeMillis()
    println("Time taken: ${endTime - startTime}")
}

fun day15Part1DijsktraMinPath(input: List<String>): Int {
    val width = input[0].length
    val height = input.size

    val grid = inputToGrid(input)

    return dijkstraSearch(grid, Point(0, 0), Point(width - 1, height - 1))
}

fun day15Part2DijkstraMinPathFiveTimesBigger(input: List<String>): Int {
    val initialGrid = inputToGrid(input)

    val width = input[0].length
    val height = input.size

    val grid =
        (0..(height * 5) - 1).flatMap { y ->
            (0..(width * 5) - 1).map { x ->
                Square(Point(x, y), multipliedRiskValue(x, y, width, height, initialGrid), Int.MAX_VALUE,)
            }
        }

    return dijkstraSearch(Grid(grid, width * 5, height * 5), Point(0, 0), Point(width * 5 - 1, height * 5 - 1))
}

fun multipliedRiskValue(x: Int, y: Int, width: Int, height: Int, initialGrid: Grid): Int {
    val xMod = x % width
    val yMod = y % height
    val xPos = x / width
    val yPos = y / height

    val increasedRisk = initialGrid[Point(xMod, yMod)].risk + xPos + yPos - 1
    return increasedRisk.mod(9) + 1
}

private fun inputToGrid(input: List<String>): Grid {
    val grid = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            Square(Point(x, y), char.toString().toInt(), Integer.MAX_VALUE)
        }
    }
    val width = input[0].length
    val height = input.size

    return Grid(grid, width, height)
}

private fun dijkstraSearch(
    grid: Grid,
    origin: Point,
    destination: Point
): Int {
    val comparator = object : Comparator<Point> {
        override fun compare(o1: Point, o2: Point): Int =
            grid[o1].distance - grid[o2].distance
    }
    val unvisited = PriorityQueue(comparator).also { it.addAll(grid.nodes) }
    grid[origin].distance = 0

    while (!unvisited.isEmpty() && !grid[destination].visited) {
        val nextNode = unvisited.remove()
        visit(nextNode, grid, unvisited)
    }

    return grid[destination].distance
}

private fun visit(
    currentNode: Point,
    grid: Grid,
    unvisited: PriorityQueue<Point>
) {
    val square = grid[currentNode]
    val unvisitedNeighbours = grid.neighboursOf(currentNode.x, currentNode.y)
        .filter { !it.visited }

    for (neighbour in unvisitedNeighbours) {
        val newDistance = square.distance + neighbour.risk
        if (newDistance < neighbour.distance) {
            unvisited.remove(neighbour.location)
            neighbour.distance = newDistance
            unvisited.add(neighbour.location)
        }
    }
    square.visited = true
}

data class Point(val x: Int, val y: Int)
data class Square(
    val location: Point,
    val risk: Int,
    var distance: Int,
    var visited: Boolean = false
)

data class Grid(val grid: List<Square>, val width: Int, val height: Int) {
    val nodes: Collection<Point> = grid.map { it.location }

    operator fun get(o1: Point): Square = grid.get(o1.y * width + o1.x)

    fun neighboursOf(x: Int, y: Int): List<Square> {
        return listOf(
            Point(x - 1, y),
            Point(x, y - 1),
            Point(x + 1, y),
            Point(x, y + 1)
        )
            .filter { it.x >= 0 && it.x < width &&
                    it.y >= 0 && it.y < height }
            .map { this[it] }
    }
}

