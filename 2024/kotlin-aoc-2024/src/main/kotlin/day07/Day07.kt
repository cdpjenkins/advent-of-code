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
        testValue in evaluateUsingPlusAndMultiplyAndConcat(testValue, operands.first(), operands.drop(1))

    fun evaluateUsingPlusAndMultiplyAndConcat(target: Long, acc: Long, rest: List<Long>): List<Long> =
        if (acc > target) {
            emptyList()
        } else if (rest.isEmpty()) {
            listOf(acc)
        } else {
            val firstPossibility = acc + rest.first()
            val secondPossibility = acc * rest.first()
            val thirdPossibility = acc concatWith rest.first()
            evaluateUsingPlusAndMultiplyAndConcat(target, firstPossibility, rest.drop(1)) +
                    evaluateUsingPlusAndMultiplyAndConcat(target, secondPossibility, rest.drop(1)) +
                    evaluateUsingPlusAndMultiplyAndConcat(target, thirdPossibility, rest.drop(1))
        }

    // concatenating using strings is slooooow
    //    private infix fun Long.concatWith(that: Long): Long = (this.toString() + that.toString()).toLong()

    // concatenating using actual maths is slightly faster
    // with thanks to Cesar Tron-Lozai for finding this algorithm and posting on LJC Slack
    private infix fun Long.concatWith(that: Long): Long {
        var thatVar = that
        var multiplier = 1

        while (thatVar > 0) {
            multiplier *= 10
            thatVar /= 10
        }

        return this * multiplier + that
    }

    // wish I could figure out how to do this using log10... might be quicker, who knows. The floating point until is
    // not exactly likely to be doing anything else, so I really hope it would be.
}
