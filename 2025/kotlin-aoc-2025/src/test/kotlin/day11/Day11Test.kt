package day11

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {

    val graph = input.associate {
        val (device, outputsString) = it.parseUsingRegex("""^([a-z]+): (.*)$""")

        val outputs = outputsString.split(" ")

        device to outputs
    }

    var paths = listOf(listOf("you"))
    var completedPaths = emptyList<List<String>>()
    while (paths.isNotEmpty()) {
        val newPaths = paths.flatMap { path ->
            val lastElement = path.last()
            val candidates = graph[lastElement]!!
            candidates.map { path + it }
        }
        val newCompletedPaths = newPaths.filter { it.last() == "out" }
        val newActivePaths = newPaths.filter { it.last() != "out" }

        completedPaths += newCompletedPaths
        paths = newActivePaths
    }

    println(completedPaths)
    println(completedPaths.size)

    return completedPaths.size
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day11Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 5
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day11.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }
}

val testInput =
    """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent().lines()
