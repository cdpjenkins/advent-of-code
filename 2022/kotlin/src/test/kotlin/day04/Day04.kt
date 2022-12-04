package day04

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import java.lang.Integer.min

class Day04 {
    @Test
    internal fun `part 1 with test data`() {
        testInput.numberOfRowsWhereOneRangeFullyContainsTheOther() shouldBe 2
    }

    @Test
    internal fun `part 1 with real data`() {
        realInput.numberOfRowsWhereOneRangeFullyContainsTheOther() shouldBe 511
    }

    @Test
    internal fun `part 2 with test data`() {
        testInput.numberOfRowsWithAnyOverlap() shouldBe 4
    }

    @Test
    internal fun `part 2 with real data`() {
        realInput.numberOfRowsWithAnyOverlap() shouldBe 821
    }

    private fun List<String>.numberOfRowsWhereOneRangeFullyContainsTheOther() =
        this
            .map(::parseRow)
            .count { it.oneRangeFullyContainsTheOther() }

    private fun List<String>.numberOfRowsWithAnyOverlap() =
        this
            .map(::parseRow)
            .count { it.overlapsAtAll() }

    private fun parseRow(it: String): ElfRow {
        val (min1, max1, min2, max2) = it.parseUsingRegex("([0-9]+)-([0-9]+),([0-9]+)-([0-9]+)")

        return ElfRow(
            ElfRange(min1.toInt(), max1.toInt()),
            ElfRange(min2.toInt(), max2.toInt())
        )
    }
}

data class ElfRange(val min: Int, val max: Int) {
    fun isContainedBy(that: ElfRange) = this.min >= that.min && this.max <= that.max
    fun overlapsWith(that: ElfRange) = max(this.min, that.min) <= min(this.max, that.max)
}

data class ElfRow(val elf1: ElfRange, val elf2: ElfRange) {
    fun oneRangeFullyContainsTheOther() = elf1.isContainedBy(elf2) || elf2.isContainedBy(elf1)
    fun overlapsAtAll() = elf1.overlapsWith(elf2)
}

val testInput =
    """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
    """.trimIndent().lines()

val realInput = readInputFileToList("day04.txt")
