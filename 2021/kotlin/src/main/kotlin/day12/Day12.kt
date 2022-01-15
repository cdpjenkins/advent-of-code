package day12

import readInput

fun main() {
    val input = readInput("Day12")

    println(findNumberOfLegalPaths(input))
    println(findNumberOfLegalPathsVisitingLittleCavesAtMostTwice(input))
}

fun findNumberOfLegalPathsVisitingLittleCavesAtMostTwice(input: List<String>): Int {
    val edges = input.parse()
    val paths = findPathsVisitingLittleCavesAtMostTwice(edges)

    return paths.size
}

fun findPathsVisitingLittleCavesAtMostTwice(
    edges: List<Pair<String, String>>,
    pathSoFar: List<String> = listOf("start")
): List<List<String>> {
    val currentNode = pathSoFar.last()

    if (currentNode == "end") {
        return listOf(pathSoFar)
    }

    val potentialTargets = edges
        .filter { it.first == currentNode }
        .map { it.second }

    return potentialTargets
        .filter { !((it == "start" || it == "end") && it in pathSoFar) }
        .filter { !(it.isLittleCave() && it.appearsTwiceIn(pathSoFar)) }
        .flatMap { findPathsVisitingLittleCavesAtMostTwice(edges, pathSoFar + it) }
}

private fun String.appearsTwiceIn(pathSoFar: List<String>): Boolean {
    val appearsMoreThanOnce = pathSoFar.filter { it == this }.size >= 1
    val appearsMoreThanTwice = pathSoFar.filter { it == this }.size >= 2
    val anyOtherSmallCaveAppearsTwice =
        pathSoFar
            .groupingBy { it }
            .eachCount()
            .filter { it.key.isLittleCave() }
            .filter { it.key != this }
            .filter { it.value >= 2 }
            .isNotEmpty()

    return (appearsMoreThanOnce && anyOtherSmallCaveAppearsTwice) || appearsMoreThanTwice
}

fun findNumberOfLegalPaths(input: List<String>): Int {
    val edges = input.parse()
    val paths = findPaths(edges)

    return paths.size
}

fun findPaths(
    edges: List<Pair<String, String>>,
    pathSoFar: List<String> = listOf("start")
): List<List<String>> {
    val currentNode = pathSoFar.last()

    if (currentNode == "end") {
        return listOf(pathSoFar)
    }

    return edges
        .filter { it.first == currentNode }
        .map { it.second }
        .filter { !(it.isLittleCave() && it in pathSoFar) }
        .flatMap { findPaths(edges, pathSoFar + it) }
}

private fun List<String>.parse() =
    flatMap {
        val (source, target) = regex.find(it)?.destructured ?: throw IllegalArgumentException(it)
        listOf(source to target, target to source)
    }


fun String.isLittleCave() = all { it.isLowerCase() }

val regex = "(start|[a-z]+|[A-Z]+)-(end|[a-z]+|[A-Z]+)".toRegex()
