package day07

import readInput
import java.lang.Math.abs

fun main() {
    val input = readInput("Day07")

    println(day07MinFuel(input, fuelConsumptionFn = { it }))
    println(day07MinFuel(input, fuelConsumptionFn = { it * (it + 1) / 2 }))
}

fun day07MinFuel(input: List<String>, fuelConsumptionFn: (Int) -> Int): Int {
    val positions: List<Int> = input[0]
        .split(",")
        .map { it.toInt() }
    val minFuel = (positions.minOrNull()!!..positions.maxOrNull()!!)
        .map { totalFuelTo(it, positions, fuelConsumptionFn) }
        .minOrNull()!!

    return minFuel
}

fun totalFuelTo(position: Int, positions: List<Int>, fuelToTravel: (Int) -> Int): Int {
    return positions
        .map { fuelToTravel(abs(it - position)) }
        .sum()
}
