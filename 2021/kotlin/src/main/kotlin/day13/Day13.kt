package day13

import readInput

fun main() {
    val input = readInput("Day13")

    println(day13Part1NumDotsAfterFirstFold(input))
    day13Part2AlltheFolds(input).print()
}

fun day13Part1NumDotsAfterFirstFold(input: List<String>) =
    input.dots()
        .foldIt(input.folds().first())
        .size

fun day13Part2AlltheFolds(input: List<String>) =
    input.folds()
        .fold(input.dots()) { dots, fold -> dots.foldIt(fold) }

private fun List<String>.folds(): List<Fold> =
    this.dropWhile { it.isNotEmpty() }
        .drop(1)
        .map { it.parseFold() }

private fun List<String>.dots() =
    this.takeWhile { it.isNotEmpty() }
        .map { it.parseDot() }
        .toSet()

private fun Set<Dot>.print() {
    val minX = this.map { it.x }.minOrNull() ?: throw AssertionError("Urgh $this")
    val maxX = this.map { it.x }.maxOrNull() ?: throw AssertionError("Urgh $this")
    val minY = this.map { it.y }.minOrNull() ?: throw AssertionError("Urgh $this")
    val maxY = this.map { it.y }.maxOrNull() ?: throw AssertionError("Urgh $this")

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            print(if (Dot(x, y) in this) "#" else ".")
        }
        println()
    }
    println()
}

private fun Set<Dot>.foldIt(fold: Fold): Set<Dot> =
    (this + map { fold.reflect(it) })
        .filter { fold.dotIsBeforeTheFold(it) }
        .toSet()

private fun String.parseFold(): Fold {
    val (axis, ordinal) = ("fold along ([xy])=([0-9]+)".toRegex()
        .find(this)
        ?.destructured
        ?: throw AssertionError("No way: $this"))

    return Fold(makeAxis(axis), ordinal.toInt())
}

private fun String.parseDot(): Dot{
    val (x, y) = ("^([0-9]+),([0-9]+)$".toRegex()
        .find(this)
        ?.destructured
        ?: throw AssertionError("Oh noes!"))

    return Dot(x.toInt(), y.toInt())
}

data class Dot(val x: Int, val y: Int)

enum class Axis {
    X {
        override fun reflect(ordinal: Int, dot: Dot) = Dot(dot.x.reflectAbout(ordinal), dot.y)
        override fun dotIsBeforeTheFold(dot: Dot, ordinal: Int) = dot.x <= ordinal
    },
    Y {
        override fun reflect(ordinal: Int, dot: Dot) = Dot(dot.x, dot.y.reflectAbout(ordinal))
        override fun dotIsBeforeTheFold(dot: Dot, ordinal: Int) = dot.y <= ordinal
    };

    abstract fun reflect(ordinal: Int, dot: Dot): Dot
    abstract fun dotIsBeforeTheFold(dot: Dot, ordinal: Int): Boolean
}

private fun Int.reflectAbout(foldOrdinal: Int) =
    foldOrdinal - (this - foldOrdinal)


fun makeAxis(axisString: String) =
    when (axisString) {
        "x" -> Axis.X
        "y" -> Axis.Y
        else -> throw AssertionError(axisString)
    }

data class Fold(val axis: Axis, val ordinal: Int) {
    fun reflect(it: Dot): Dot = axis.reflect(ordinal, it)
    fun dotIsBeforeTheFold(dot: Dot): Boolean = axis.dotIsBeforeTheFold(dot, this.ordinal)
}
