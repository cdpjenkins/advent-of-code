package day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.FileUtil.readInputFileToString
import utils.RegexUtils.parseUsingRegex
import kotlin.collections.flatMap
import kotlin.test.Ignore

private fun part1(input: String): Long =
    parseRanges(input)
        .flatMap { it.filter(Long::consistsOfSameDigitSequenceRepeatedTwice) }
        .sum()


private fun part2(input: String) =
    parseRanges(input)
        .flatMap { it.filter(Long::consistsOfSameDigitSequenceRepeated ) }
        .sum()

private fun part2TheMoreEfficientWay(input: String) =
    parseRanges(input)
        .flatMap { it.allValidIdsInRange() }
        .sum()

private fun Long.consistsOfSameDigitSequenceRepeated(): Boolean = toString().consistsOfSameDigitSequenceRepeated()
private fun String.consistsOfSameDigitSequenceRepeated() = (2..length).any { consistsOfSameDigitSequenceRepeatNTimes(it) }

private fun LongRange.allValidIdsInRange(): List<Long> = enumerateAllValidIdsPossibleByLength(this).filter { it in this }

private fun enumerateAllValidIdsPossibleByLength(range: LongRange): Set<Long> {
    require(range.first <= range.last) { "Invalid range: $range" }

    return (range.first.toString().length..range.last.toString().length)
        .flatMap { length -> enumerateAllValidIdsUpToLength(length) }
        .toSet()
}

private fun enumerateAllValidIdsUpToLength(length: Int): List<Long> =
    (1..length / 2).flatMap { patternLength ->
        allValidIdsOfLength(patternLength)
            .map { it.repeat(length / patternLength) }
            .map { it.toLong() }
    }

private fun allValidIdsOfLength(length: Int) = (0..<10.pow(length)).map { it.toString() }

private fun String.consistsOfSameDigitSequenceRepeatNTimes(n: Int) =
    (length % n == 0) && this.substring(0, length / n).repeat(n) == this

private fun Long.consistsOfSameDigitSequenceRepeatedTwice(): Boolean = toString().consistsOfSameDigitSequenceRepeatNTimes(2)

private fun parseRanges(input: String): List<LongRange> = input.split(",").map { it.parseRange() }

private fun String.parseRange(): LongRange {
    val (start, end) = this.parseUsingRegex("""(\d+)-(\d+)""")

    return start.toLong()..end.toLong()
}

private fun Int.pow(n: Int): Int = (1..n).fold(1) { acc, _ -> acc * this }

class Day02Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1227775554L
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day02.txt")) shouldBe 19219508902L
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 4174379265L
    }

    @Test
    fun `part 2 the more efficient way with test input`() {
        part2TheMoreEfficientWay(testInput) shouldBe 4174379265L
    }

    @Ignore // too sloooooow
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day02.txt")) shouldBe 27180728081L
    }

    @Test
    fun `part 2 the more efficient way with real input`() {
        part2TheMoreEfficientWay(readInputFileToString("day02.txt")) shouldBe 27180728081L
    }
}

val testInput =
    """11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"""
