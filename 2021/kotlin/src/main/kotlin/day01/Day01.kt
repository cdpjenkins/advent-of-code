package day01

import readInput

fun main() {
    val input = readInput("Day01")
    println(countDepthIncreasesFromStrings(input))
    println(slidingWindows(input))
}

fun countDepthIncreasesFromStrings(input: List<String>): Int = countDepthIncreases(input.map { it.toInt() })

private fun countDepthIncreases(measurements: List<Int>): Int =
        measurements.zip(measurements.drop(1))
                .filter { it.second > it.first }
                .count()

// I wish there was a ternary zip function in the Kotlina stdlib
fun slidingWindows(input: List<String>): Int {
    val xs = input.map(String::toInt)
    val rollingSums = (1..xs.size - 2).map { xs[it - 1] + xs[it] + xs[it + 1] }
    return countDepthIncreases(rollingSums)
}
