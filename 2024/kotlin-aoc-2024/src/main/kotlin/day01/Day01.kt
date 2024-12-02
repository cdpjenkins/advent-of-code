package day01

import kotlin.math.abs

fun part1(input: List<String>): Int {
    val (leftList, rightList) = parseInput(input)

    return (leftList.sorted() zip rightList.sorted())
        .map { (first, second) -> abs(first - second) }
        .sum()
}

fun part2(input: List<String>): Int {
    val (leftList, rightList) = parseInput(input)

    val frequenciesInRightList = rightList.groupingBy { it }.eachCount().withDefault { 0 }

    return leftList
        .map { similarityScore(it, frequenciesInRightList) }
        .sum()
}

private fun similarityScore(num: Int, rightList: Map<Int, Int>) = num * (rightList.getValue(num))

private fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val linesAsInts = input.map { it.split(" +".toRegex()).map { it.toInt() } }

    val firsts = linesAsInts.map { it[0] }
    val seconds = linesAsInts.map { it[1] }

    return Pair(firsts, seconds)
}
