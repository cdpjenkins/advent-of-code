package day05

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {

    val (freshIngredientIdRangesLines, availableIngredientIdsLines) = input.splitByBlank()
    val freshIngredientRanges = freshIngredientIdRangesLines.map {
        val (startString, endString) = it.split("-")
        startString.toLong()..endString.toLong()
    }
    val availableIngredientIds = availableIngredientIdsLines.map { it.toLong() }


    val numFreshIngredients = availableIngredientIds.count { id ->
        freshIngredientRanges.any { id in it }
    }




    return numFreshIngredients
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day05Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 3
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day05.txt")) shouldBe 643
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
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent().lines()
