package day13

import FileUtil.readInputFileToList
import ListUtils.splitByBlank
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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


class Day13Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 480
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day13.txt")) shouldBe 31552
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 875318608908L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day13.txt")) shouldBe 95273925552482L
    }
}

val testInput =
    """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279

    """.trimIndent().lines()
