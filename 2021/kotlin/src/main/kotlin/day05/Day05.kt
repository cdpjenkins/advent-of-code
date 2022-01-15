package day05

import readInput
import java.lang.AssertionError
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val input = readInput("Day05")
    println(day5Part1CountIntersections(input))
    println(day5Part2CountIntersectionsIncludingDiagonals(input))
}

fun day5Part1CountIntersections(input: List<String>): Int =
    input
        .map { it.makeLine() }
        .filter { it.isVerticalOrHorizontal() }
        .markLines()
        .filter { it.value > 1 }
        .size

fun day5Part2CountIntersectionsIncludingDiagonals(input: List<String>): Int =
    input
        .map { it.makeLine() }
        .filter { it.isVerticalHorizontalOrDiagonal() }
        .markLines()
        .filter { it.value > 1 }
        .size

fun List<Line>.markLines(): Map<Point, Int> {
    val mutableMap = mutableMapOf<Point, Int>()

    for (line in this) {
        for (point in line.points()) {
            mutableMap.markPoint(point)
        }
    }

    return mutableMap
}

private fun MutableMap<Point, Int>.markPoint(point: Point) {
    this[point] = getOrDefault(point, 0) + 1
}

data class Point(val x:Int, val y: Int) {

}

fun String.makeLine(): Line {
    val (x1, y1, x2, y2) = regex.find(this)!!.destructured.toList().map { it.toInt() }
    return Line(x1, y1, x2, y2)
}

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val minX = min(x1, x2)
    val maxX = max(x1, x2)
    val minY = min(y1, y2)
    val maxY = max(y1, y2)

    fun isVertical() = x1 == x2
    fun isHorizontal() = y1 == y2
    private fun isDiagonal() = Math.abs(x1 - x2) == Math.abs(y1 - y2)
    fun isVerticalOrHorizontal() = isVertical() || isHorizontal()
    fun isVerticalHorizontalOrDiagonal() = isVertical() || isHorizontal() || isDiagonal()

    fun points(): List<Point> =
        if (isVertical()) {
            (minY..(maxY)).map { Point(x1, it) }
        } else if (isHorizontal()) {
            (minX..(maxX)).map { Point(it, y1) }
        } else if (isDiagonal()) {
            val gradient = (y2 - y1) / (x2 - x1) // cannot compute this for vertical line
            if (gradient == 1) {
                (minX..maxX).map { Point(it, minY + (it - minX) * gradient) }
            } else {
                (minX..maxX).map { Point(it, maxY + (it - minX) * gradient) }
            }
        } else {
            throw AssertionError(this.toString())
        }
}

val regex = Regex("([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)")
