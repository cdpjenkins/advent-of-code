package day10

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val input = readInput("Day10")

    println(day10Part1SyntaxErrorScore(input))
    println(day10Part2RepairIncompleteLines(input))
}

fun day10Part1SyntaxErrorScore(input: List<String>): Int = input.map { calculateScoreForString(it) }.sum()

fun day10Part2RepairIncompleteLines(input: List<String>): Long {
    val scores = input
        .filter { !it.isCorrupted() }
        .map { it.calculateRepairScore() }
        .sorted()

    return scores[scores.size / 2]
}

private fun String.calculateRepairScore(): Long {
    val stack = ArrayDeque<Char>()

    for (c in this) {
        when (c) {
            '(' -> stack.addLast(c)
            '[' -> stack.addLast(c)
            '{' -> stack.addLast(c)
            '<' -> stack.addLast(c)
            else -> if (c == counterpart(stack.removeLast())) {
                // we're good
            } else {
                throw IllegalArgumentException(this)
            }
        }
    }

    var score = 0L
    for (c in stack.reversed()) {
        score *= 5
        score += when (c) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> throw IllegalArgumentException(this)
        }
    }
    return score
}

fun String.isCorrupted() = calculateScoreForString(this) > 0

fun calculateScoreForString(it: String): Int {
    println(it)

    val stack = ArrayDeque<Char>()

    for (c in it) {
        when (c) {
            '(' -> stack.addLast(c)
            '[' -> stack.addLast(c)
            '{' -> stack.addLast(c)
            '<' -> stack.addLast(c)
            else -> if (c == counterpart(stack.removeLast())) {
                // we're good
            } else {
                return syntaxErrorScore(c)
            }
        }
    }

    return 0
}

fun counterpart(c: Char) =
    when (c) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalArgumentException(c.toString())
    }

fun syntaxErrorScore(c: Char) =
    when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException(c.toString())
    }
