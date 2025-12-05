package day05

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import kotlin.math.max
import kotlin.math.min

private fun part1(input: List<String>): Int {
    val (freshIngredientRanges, availableIngredientIds) = input.parse()

    return availableIngredientIds.count { id ->
        freshIngredientRanges.any { id in it }
    }
}

private fun part2(input: List<String>): Long {
    val (freshIngredientRanges, _) = input.parse()

    return mergedRanges(freshIngredientRanges).sumOf { it.last - it.first + 1 }
}

private fun mergedRanges(freshIngredientRanges: List<LongRange>): MutableList<LongRange> {
    val rangesToMerge = freshIngredientRanges.sortedBy { it.first }
    val mergedRanges = mutableListOf<LongRange>()

    rangesToMerge.forEach { range ->
        if (mergedRanges.isEmpty()) {
            mergedRanges.add(range)
        } else {
            val lastRange = mergedRanges.last()

            if (range.canMergeInto(lastRange)) {
                val mergedLastRange = lastRange.mergeWith(range)
                mergedRanges.removeLast()
                mergedRanges.add(mergedLastRange)
            } else {
                mergedRanges.add(range)
            }
        }
    }
    return mergedRanges
}

private fun LongRange.canMergeInto(lastRange: LongRange?): Boolean = lastRange == null || first in lastRange
private fun LongRange.mergeWith(that: LongRange): LongRange {
    return min(this.first, that.first)..max(this.last, that.last)
}

private fun List<String>.parse(): Pair<List<LongRange>, List<Long>> {
    val (freshIngredientIdRangesLines, availableIngredientIdsLines) = splitByBlank()
    val freshIngredientRanges = freshIngredientIdRangesLines.map {
        val (startString, endString) = it.split("-")
        startString.toLong()..endString.toLong()
    }
    val availableIngredientIds = availableIngredientIdsLines.map { it.toLong() }
    return Pair(freshIngredientRanges, availableIngredientIds)
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

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 14
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day05.txt")) shouldBe 342018167474526
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
