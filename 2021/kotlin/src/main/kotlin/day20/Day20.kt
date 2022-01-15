package day20

import readInput

fun main() {
    val input = readInput("Day20")

    println(input.enhanceNTimesAndReturnSize(2))
}

fun List<String>.enhanceNTimesAndReturnSize(n: Int): Int {
    val imageEnhancementAlgorithm = imageEnhancementAlgorithm()
    val initialImage = inputImage()

    val enhancementSequence = generateSequence(initialImage) { it.enhance(imageEnhancementAlgorithm) }
    val resultImage = enhancementSequence.drop(n).first()

    resultImage.print()

    return resultImage.size
}

private fun List<String>.inputImage(): Set<Point> {
    val inputImageString = drop(2)
    val initialImage = points(inputImageString)
    return initialImage
}

private fun List<String>.imageEnhancementAlgorithm() = first()

data class Point(val x: Int, val y: Int)

fun Set<Point>.print() {
    val minX = map { it.x }.minByOrNull { it }!! - 1
    val maxX = map { it.x }.maxByOrNull { it }!! + 1
    val minY = map { it.y }.minByOrNull { it }!! - 1
    val maxY = map { it.y }.maxByOrNull { it }!! + 1

    val str = (minY..maxY).map { y ->
        (minX..maxX).map { x ->
            if (Point(x, y) in this) "#" else "."
        }.joinToString("")
    }.joinToString("\n")
    println(str)
}

fun Set<Point>.enhance(imageEnhancementAlgorithm: String): Set<Point> {
    val minX = map { it.x }.minByOrNull { it }!! - 1
    val maxX = map { it.x }.maxByOrNull { it }!! + 1
    val minY = map { it.y }.minByOrNull { it }!! - 1
    val maxY = map { it.y }.maxByOrNull { it }!! + 1

    val newPoints = (minY..maxY).flatMap { y ->
        (minX..maxX).map { x ->
            val thingie = listOf(
                Point(x - 1, y - 1),
                Point(x, y - 1),
                Point(x + 1, y - 1),
                Point(x - 1, y),
                Point(x, y),
                Point(x + 1, y),
                Point(x - 1, y + 1),
                Point(x, y + 1),
                Point(x + 1, y + 1)
            ).map { it in this }

            val index = thingie.reversed().withIndex().map { (i, b) -> if (b) 1 shl i else 0 }.sum()
            val newPixel = imageEnhancementAlgorithm[index]

            when (newPixel) {
                '#' -> Point(x, y)
                '.' -> null
                else -> throw AssertionError(newPixel)
            }
        }
    }
        .filterNotNull()
        .toSet()

    return newPoints
}

fun points(inputImageString: List<String>): Set<Point> {
    val points = inputImageString.withIndex().flatMap { (y, row) ->
        row.withIndex().map { (x, c) ->
            when (c) {
                '#' -> Point(x, y)
                '.' -> null
                else -> throw AssertionError(row)
            }
        }
    }
        .filterNotNull()
        .toSet()
    return points
}
