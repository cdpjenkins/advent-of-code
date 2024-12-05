package day05

import ListUtils.splitByBlank

fun part1(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    return updates
        .filter { it.isValidUpdate(rules) }
        .map { it.middlePageNumber() }
        .sum()
}

fun part2(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    return updates
        .filterNot { it.isValidUpdate(rules) }
        .map { it.fixOrdering(rules) }
        .map { it.middlePageNumber() }
        .sum()
}

fun List<Int>.isValidUpdate(rules: List<Pair<Int, Int>>) = zipWithNext().all { pair -> pair in rules }
fun List<Int>.middlePageNumber() = this[size / 2]
fun List<Int>.fixOrdering(rules: List<Pair<Int, Int>>) =
    if (this.isValidUpdate(rules)) this
    else this.sortedWith(rules.toComparator())

fun List<Pair<Int, Int>>.toComparator(): Comparator<Int> {
    return Comparator { a, b ->
        when {
            Pair(a, b) in this -> -1
            Pair(b, a) in this -> 1
            else -> throw IllegalStateException("${a},${b}")
        }
    }
}

fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val (pageOrderingRulesLines, updatesLines) = input.splitByBlank()

    return Pair(
        pageOrderingRulesLines.map { it.parseRule() },
        updatesLines.map { it.parseUpdate() }
    )
}

fun String.parseUpdate() = split(",").map { it.toInt() }
fun String.parseRule() = this.split("|").map { it.toInt() }.let { Pair(it[0], it[1]) }