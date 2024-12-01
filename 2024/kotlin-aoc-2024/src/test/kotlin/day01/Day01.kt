package day01

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

private fun part1(input: List<String>): Int {
    val (leftList, rightList) = parseInput(input)

    return (leftList.sorted() zip rightList.sorted())
        .map { (first, second) -> abs(first - second) }
        .sum()
}

private fun part2(input: List<String>): Int {
    val (leftList, rightList) = parseInput(input)

    return leftList
        .map { similarityScore(it, rightList) }
        .sum()
}

private fun similarityScore(num: Int, rightList: List<Int>) = num * rightList.count { (it == num) }

private fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val linesAsInts = input.map { it.split(" +".toRegex()).map { it.toInt() } }

    val firsts = linesAsInts.map { it[0] }
    val seconds = linesAsInts.map { it[1] }

    return Pair(firsts, seconds)
}

class Day01Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 11
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day01.txt")) shouldBe 2192892
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 31
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day01.txt")) shouldBe 22962826
    }
}

val testInput =
    """
3   4
4   3
2   5
1   3
3   9
3   3
    """.trimIndent().lines()
