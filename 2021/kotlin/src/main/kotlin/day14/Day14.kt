package day14

import readInput
import kotlin.AssertionError

fun main() {
    val input = readInput("Day14")

    println(day14Part1(input))
    println(day14Part2(input))
}

fun day14Part2(input: List<String>): Long {
    val (polymer, rules) = parseInput(input)
    val enhancedRules: Map<String, List<String>> = enhancedRules(rules)

    val frequencies: Map<String, Long> = polymer
        .zip(polymer.drop(1))
        .map { "${it.first}${it.second}" }
        .groupingBy { it }
        .eachCount()
        .map { it.key to it.value.toLong() }
        .toMap()

    val fortiethGeneration =
        generateSequence(frequencies) { processStepUsingFrequencies(it, enhancedRules) }
            .drop(40)
            .first()

    val elementFrequencies: Map<String, Long> = (fortiethGeneration.map { it.key[0].toString() to it.value } +
            (polymer.last().toString() to 1L))
        .groupingBy { it.first }
        .fold(0L) { acc, elem -> acc + elem.second }

    val (_, maxFreq) = elementFrequencies.maxByOrNull { it.value } ?: throw AssertionError("Oh golly")
    val (_, minFreq) = elementFrequencies.minByOrNull { it.value } ?: throw AssertionError("Oh poo")

    return maxFreq - minFreq
}

private fun processStepUsingFrequencies(
    frequencies: Map<String, Long>,
    enhancedRules: Map<String, List<String>>
): Map<String, Long> {
    val nextGeneration =
        frequencies.map { (key, frequency) ->
            enhancedRules[key]?.map {
                it to frequency
            }
        }
            .flatMap { it!! }
            .groupingBy { it.first }
            .fold(0L) { acc, elem -> acc + elem.second }
    return nextGeneration
}

private fun enhancedRules(rules: Map<String, String>) =
    rules.map { it.key to listOf("${it.key[0]}${it.value}", "${it.value}${it.key[1]}") }
        .toMap()

fun day14Part1(input: List<String>): Int {
    val (polymer, rules) = parseInput(input)

    val resultingPolymer = generateSequence(polymer) { processStep(it, rules) }
        .drop(10)
        .first()
    val frequencies = resultingPolymer.groupingBy { it }.eachCount()

    val max = frequencies.values.maxOrNull() ?: throw AssertionError("curses!!!1")
    val min = frequencies.values.minOrNull() ?: throw AssertionError("arghghgh!!!1")

    return max - min
}

fun processStep(
    polymer: String,
    rulesMap: Map<String, String>
): String =
    polymer
        .zip(polymer.drop(1))
        .map {
            it.first + (rulesMap["${it.first}${it.second}"] ?: throw AssertionError("Waaaaa"))
        }
        .joinToString("")
        .plus(polymer.last())

fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
    val polymer = input.first()

    val insertionRules = input.drop(2)

    val rulesMap = insertionRules
        .map { it.parseRuleLine() }
        .toMap()
    return Pair(polymer, rulesMap)
}

fun String.parseRuleLine(): Pair<String, String> {
    val regex = "([A-Z]{2}) -> ([A-Z])".toRegex()

    val (pair, toInsert) = regex.find(this)
        ?.destructured
        ?: throw AssertionError("Oh noes!")

    return pair to toInsert
}

