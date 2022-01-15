package day06

import readInput

fun main() {
    val input = readInput("Day06")

    println(day06Part1NumberOfLanternFish(input))
    println(day06Part2NumberOfLanternFish(input))
}

fun day06Part1NumberOfLanternFish(input: List<String>) = numFishAfter(input, generations = 80)

fun day06Part2NumberOfLanternFish(input: List<String>) = numFishAfter(input, generations = 256)

private fun numFishAfter(input: List<String>, generations: Int): Long {
    var fish: Map<Int, Long> = input[0]
        .split(",")
        .map { it.toInt() }
        .groupingBy { it }
        .eachCount()
        .map { it.key to it.value.toLong() }
        .toMap()

    for (i in (1..generations)) {
        fish = oneGeneration(fish)
    }

    return fish.getOrDefault(0, 0L) +
        fish.getOrDefault(1, 0L) +
        fish.getOrDefault(2, 0L) +
        fish.getOrDefault(3, 0L) +
        fish.getOrDefault(4, 0L) +
        fish.getOrDefault(5, 0L) +
        fish.getOrDefault(6, 0L) +
        fish.getOrDefault(7, 0L) +
        fish.getOrDefault(8, 0L)
}

private fun oneGeneration(startingFish: Map<Int, Long>): Map<Int, Long> {
    val mutableFish = startingFish.toMutableMap()
    mutableFish[-1] = mutableFish.getOrDefault(0, 0L)
    mutableFish[0] = mutableFish.getOrDefault(1, 0L)
    mutableFish[1] = mutableFish.getOrDefault(2, 0L)
    mutableFish[2] = mutableFish.getOrDefault(3, 0L)
    mutableFish[3] = mutableFish.getOrDefault(4, 0L)
    mutableFish[4] = mutableFish.getOrDefault(5, 0L)
    mutableFish[5] = mutableFish.getOrDefault(6, 0L)
    mutableFish[6] = mutableFish.getOrDefault(7, 0L) + mutableFish.getOrDefault(-1, 0L)
    mutableFish[7] = mutableFish.getOrDefault(8, 0L)
    mutableFish[8] = mutableFish.getOrDefault(-1, 0L)
    mutableFish[-1] = 0L

    return mutableFish
}
