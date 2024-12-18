package day13

import utils.ListUtils.splitByBlank
import utils.RegexUtils.parseUsingRegex
import kotlin.math.floor

fun part1(input: List<String>) =
    input.machines()
        .map(Machine::solve)
        .filter(Solution::isValid)
        .map(Solution::cost)
        .sum()
        .toInt()

fun part2(input: List<String>) =
    input.machines()
        .map(Machine::add10TrillionToPrizeCoordinates)
        .map(Machine::solve)
        .filter(Solution::isValid)
        .map(Solution::cost)
        .sum()
        .toLong()

private fun List<String>.machines(): List<Machine> =
    this.splitByBlank()
        .map { Machine.of(it) }

data class Solution(val a: Double, val b: Double) {
    fun isValid() = floor(a) == a && floor(b) == b
    fun cost() = a * 3 + b * 1
}

data class Machine(
    val ax: Double,
    val ay: Double,
    val bx: Double,
    val by: Double,
    val px: Double,
    val py: Double,
) {
    fun solve() = Solution(
        a = (px * by - py * bx) / (ax * by - ay * bx),
        b = (px * ay - py * ax) / (bx * ay - by * ax)
    )

    fun add10TrillionToPrizeCoordinates() =
        this.copy(
            px = px + 10000000000000.0,
            py = py + 10000000000000.0
        )

    companion object {
        private val BUTTON_REGEX = """^Button [AB]: X\+(\d+), Y\+(\d+)$""".toRegex()

        fun of(it: List<String>): Machine {
            val (buttonAXString, buttonAYString) = it[0].parseUsingRegex(BUTTON_REGEX)
            val (buttonBXString, buttonBYString) = it[1].parseUsingRegex(BUTTON_REGEX)
            val (prizeXString, prizeYString) = it[2].parseUsingRegex("""Prize: X=(\d+), Y=(\d+)""".toRegex())

            return Machine(
                ax = buttonAXString.toDouble(),
                ay = buttonAYString.toDouble(),
                bx = buttonBXString.toDouble(),
                by = buttonBYString.toDouble(),
                px = prizeXString.toDouble(),
                py = prizeYString.toDouble()
            )
        }
    }
}
