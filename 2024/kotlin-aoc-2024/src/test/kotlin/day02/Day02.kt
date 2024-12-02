package day02

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>) =
    input.map { it.parseReport() }
        .count { it.isSafe() }


private fun part2(input: List<String>) =
    input.map { it.parseReport() }
        .count { it.isSafeWithDampening() }

private fun String.parseReport() = this.split(" ").map { it.toInt() }
private fun List<Int>.isSafe() = this.isAllAscendingGradually() || this.isAllDescendingGradually()
private fun List<Int>.isAllAscendingGradually() = this.zipWithNext().all { (a, b) -> a - b in (-3..-1) }
private fun List<Int>.isAllDescendingGradually() = this.zipWithNext().all { (a, b) -> a - b in (1..3) }
private fun List<Int>.isSafeWithDampening() = indices.any { i -> withElementRemovedAt(i).isSafe() }
private fun List<Int>.withElementRemovedAt(i: Int) = this.toMutableList().apply { removeAt(i) }.toList()

class Day02Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 2
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day02.txt")) shouldBe 472
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 4
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day02.txt")) shouldBe 520
    }
}

val testInput =
    """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()
