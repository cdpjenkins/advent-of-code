package day03

import readInput
import java.lang.AssertionError

fun main() {
    val input = readInput("Day03")

    println(day3Part1CalculatePowerConsumption(input))
    println(day3Part2CalculateLifeSupportRating(input))
}

fun day3Part1CalculatePowerConsumption(input: List<String>): Int {
    val gamma = (0..input[0].length - 1)
        .map { input.findMostCommonBit(it) }
        .joinToString("")
    val epsilon = gamma.invert()

    return gamma.toInt(2) * epsilon.toInt(2)
}

fun day3Part2CalculateLifeSupportRating(input: List<String>): Int =
    calculateOxygenGeneratorBits(input).toInt(2) * calculateCO2ScrubberBits(input).toInt(2)

fun calculateOxygenGeneratorBits(input: List<String>, position: Int = 0): String {
    println("$position $input")

    val mostCommonBit = input.findMostCommonBit(position)
    val filteredList = input.filter { it.hasBitAtPosition(position, mostCommonBit) }
    if (filteredList.size == 1) {
        return filteredList.first()
    } else if (filteredList.size == 0) {
        throw AssertionError(filteredList.toString())
    } else {
        return calculateOxygenGeneratorBits(filteredList, position + 1)
    }
}

fun calculateCO2ScrubberBits(input: List<String>, position: Int = 0): String {
    println("$position $input")

    val leastCommonBit = input.findMostCommonBit(position).flipBit()
    val filteredList = input.filter { it.hasBitAtPosition(position, leastCommonBit) }
    if (filteredList.size == 1) {
        return filteredList.first()
    } else if (filteredList.size == 0) {
        throw AssertionError(filteredList.toString())
    } else {
        return calculateCO2ScrubberBits(filteredList, position + 1)
    }
}

private fun String.hasBitAtPosition(position: Int, bit: Char) = this[position] == bit

private fun String.invert(): String =
    this.map(Char::flipBit)
        .joinToString("")

private fun Char.flipBit() =
    when (this) {
        '0' -> '1'
        '1' -> '0'
        else -> throw AssertionError(this)
    }

private fun List<String>.findMostCommonBit(position: Int): Char =
    this.map { it[position] }
        .groupingBy { it }
        .eachCount()
        .let { if ((it['1'] ?: 0) >= (it['0'] ?: 0)) '1' else '0'}

