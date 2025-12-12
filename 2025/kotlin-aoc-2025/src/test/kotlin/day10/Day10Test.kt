package day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.Status
import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.test.Ignore

private fun part1(input: List<String>) = input.sumOf { it.solvePart1() }
private fun part2(input: List<String>) = input.sumOf { it.solveOneLinePart2() }


fun String.solvePart1(): Int {
    val input = split(" ")

    val indicatorLightDiagram = parseIndicatorLightDiagram(input.first())
    val buttonWiringSchematics = input.drop(1).dropLast(1).map { parseButtonWiringSchematics(it) }
    val joltageRequirements = parseJoltageRequirements(input.last())

    val initialLights = (0..<indicatorLightDiagram.size).map { false }
    var lights = setOf(initialLights)
    var numIterations = 0
    while (true) {
        numIterations++
        val newLights = lights.flatMap { lights ->
            buttonWiringSchematics.map { it.applyTo(lights) }
        }.toSet()

        if (newLights.contains(indicatorLightDiagram)) {
            return numIterations
        }

        lights = newLights
    }
}

fun parseJoltageRequirements(lastBit: String): List<Int> {
    val (joltageRequirementsStrings) = lastBit.parseUsingRegex("""^\{([\d,]*)\}$""")
    val joltageRequirements = joltageRequirementsStrings.split(",").map { it.toInt() }
    return joltageRequirements
}

fun parseButtonWiringSchematics(string: String): List<Int> {
    val (buttonWiringSchematicsString) = string.parseUsingRegex("""^\(([\d,]+)\)$""")
    return buttonWiringSchematicsString.split(",").map { it.toInt() }
}

fun parseIndicatorLightDiagram(input: String): List<Boolean> {
    val (indicatorLightDiagramString) = input.parseUsingRegex("""^\[([.#]+)\]$""")
    val indicatorLightDiagram = indicatorLightDiagramString.map { it == '#' }
    return indicatorLightDiagram
}

fun List<Int>.applyTo(lights: List<Boolean>): List<Boolean> {
    val mutableLights = lights.toMutableList()
    this.forEach { n ->
        mutableLights[n] = !mutableLights[n]
    }

    return mutableLights
}

private fun String.solveOneLinePart2(): Int {
    val input = this.split(" ")

    val indicatorLightDiagram = parseIndicatorLightDiagram(input.first())
    val buttonWiringSchematics = input.drop(1).dropLast(1).map { parseButtonWiringSchematics(it) }
    val joltageRequirements = parseJoltageRequirements(input.last())

    val ctx = Context()
    val opt = ctx.mkOptimize()
    val vars = buttonWiringSchematics.indices.map { ctx.mkIntConst("b$it") }
    vars.forEach { opt.Add(ctx.mkGe(it, ctx.mkInt(0))) }

    joltageRequirements.withIndex().forEach { (i, joltages) ->
        val terms = buttonWiringSchematics.withIndex().filter { i in it.value }.map { vars[it.index] }
        if (terms.isNotEmpty()) {
            val sum = if (terms.size == 1) terms[0]
            else ctx.mkAdd(*terms.toTypedArray<IntExpr>())
            opt.Add(ctx.mkEq(sum, ctx.mkInt(joltages)))
        } else if (joltages != 0) {
            throw RuntimeException("arghghghgh")
        }
    }
    opt.MkMinimize(ctx.mkAdd(*vars.toTypedArray<IntExpr>()))
    return if (opt.Check() == Status.SATISFIABLE) {
        vars.sumOf { opt.model.evaluate(it, false).toString().toInt() }
    } else {
        0
    }
}

class Day10Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 7
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day10.txt")) shouldBe 500
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day10.txt")) shouldBe 19763
    }

    @Test
    fun `can parse indicator light diagram`() {
        parseIndicatorLightDiagram("[.##.]") shouldBe listOf(false, true, true, false)
    }

    @Test
    fun `can parse button wiring schematics`() {
        parseButtonWiringSchematics("(1,3)") shouldBe listOf(1, 3)
    }

    @Test
    fun `can parse joltage requirements`() {
        parseJoltageRequirements("{3,5,4,7}") shouldBe listOf(3, 5, 4, 7)
    }
}

val testInput =
    """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent().lines()
