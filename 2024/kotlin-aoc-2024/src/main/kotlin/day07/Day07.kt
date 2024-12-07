package day07

import RegexUtils.parseUsingRegex

fun part1(input: List<String>): Long =
    input.map { it.parseEquation() }
        .filter { it.isValidUsingPlusAndMultiply() }
        .sumOf { it.testValue }


fun part2(input: List<String>): Long =
    input.map { it.parseEquation() }
        .filter { it.isValidUsingPlusAndMultiplyAndConcat() }
        .sumOf { it.testValue }

val LINE_REGEX = """^(\d+): ([\d ]+$)""".toRegex()
private fun String.parseEquation(): Equation {
    val (testValueString, equationString) = this.parseUsingRegex(LINE_REGEX)

    return Equation(
        testValueString.toLong(),
        equationString.split(" ").map { it.toLong() }
    )
}

data class Equation(val testValue: Long, val operands: List<Long>) {
    fun isValidUsingPlusAndMultiply() =
        testValue in evaluateUsingPlusAndMultiply(operands.first(), operands.drop(1))

    fun evaluateUsingPlusAndMultiply(acc: Long, rest: List<Long>): List<Long> =
        if (rest.isEmpty()) {
            listOf(acc)
        } else {
            val firstPossibility = acc + rest.first()
            val secondPossibility = acc * rest.first()
            evaluateUsingPlusAndMultiply(firstPossibility, rest.drop(1)) +
                    evaluateUsingPlusAndMultiply(secondPossibility, rest.drop(1))
        }

    fun isValidUsingPlusAndMultiplyAndConcat() =
        testValue in evaluateUsingPlusAndMultiplyAndConcat(operands.first(), operands.drop(1))

    fun evaluateUsingPlusAndMultiplyAndConcat(acc: Long, rest: List<Long>): List<Long> =
        if (rest.isEmpty()) {
            listOf(acc)
        } else {
            val firstPossibility = acc + rest.first()
            val secondPossibility = acc * rest.first()
            val thirdPossibility = acc concatWith rest.first()
            evaluateUsingPlusAndMultiplyAndConcat(firstPossibility, rest.drop(1)) +
                    evaluateUsingPlusAndMultiplyAndConcat(secondPossibility, rest.drop(1)) +
                    evaluateUsingPlusAndMultiplyAndConcat(thirdPossibility, rest.drop(1))
        }

    private infix fun Long.concatWith(that: Long): Long = (this.toString() + that.toString()).toLong()
}
