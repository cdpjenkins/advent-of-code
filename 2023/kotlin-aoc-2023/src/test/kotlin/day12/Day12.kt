package day12

import FileUtil.readInputFileToList
import ListUtils.toIntList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import kotlin.test.Test

private fun part1(input: List<String>): Int {
    val records = input.map { it.parseLine() }

    return records.map { findNumValidSubstitutions(it) }.sum()
}

fun findNumValidSubstitutions(record: ConditionRecord): Int {
    return allSubstitutionsBruteForce(record.springs).count { it.isValidAccordingTo(record.groups) }
}

val springsRegex = "(#+)".toRegex()
private fun String.isValidAccordingTo(nums: List<Int>): Boolean {
    val lengths = springsRegex.findAll(this).map { it.value.length }.toList()

    return lengths == nums
}

private fun String.parseLine(): ConditionRecord {
    return ConditionRecord.of(this)
}

data class ConditionRecord(
    val springs: String,
    val groups: List<Int>
) {
    companion object {
        val regex = "^([?.#]+) ([0-9,]+)$".toRegex()

        fun of(s: String): ConditionRecord {
            val (springs, numsString) = s.parseUsingRegex(regex)

            return ConditionRecord(
                springs,
                numsString.toIntList(separator = ",")
            )
        }
    }
}

fun allSubstitutionsBruteForce(springs: String): Sequence<String> {
    val numWildcards = springs.count { it == '?' }

    val numPossibilities = 1.shl(numWildcards)

    return sequence {
        for (poss in (0 until numPossibilities)) {
            val sb = StringBuffer()
            var hashIndex = 0
            springs.forEach { c ->
                if (c == '?') {
                    val replacement = if ((1.shl(hashIndex) and poss) != 0) '#' else '.'
                    sb.append(replacement)
                    hashIndex++
                } else {
                    sb.append(c)
                }
            }

            yield(sb.toString())
        }
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day12Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 21
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day12.txt")) shouldBe 7705
    }
//
//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }

    @Test
    fun `substitutions are sane for single wildcard`() {
//        allSubstitutions("?").toSet() shouldBe setOf(".", "#")
        allSubstitutionsBruteForce("#?#").toSet() shouldBe setOf("#.#", "###")
    }

    @Test
    fun `substitutions are sane with two wildcards`() {
        allSubstitutionsBruteForce("??").toSet() shouldBe setOf("..", ".#", "#.", "##")
        allSubstitutionsBruteForce("?.?").toSet() shouldBe setOf("...", "..#", "#..", "#.#")
    }

    @Test
    fun `detects valid spring strings`() {
        ".#...#....###".isValidAccordingTo("1,1,3".toIntList()) shouldBe true
        "#####...#.##.###".isValidAccordingTo("5,1,2,3".toIntList()) shouldBe true
    }

    @Test
    fun `detects invalid spring strings`() {
        "#####.##".isValidAccordingTo("3,1,2".toIntList()) shouldBe false
        "###.#.#".isValidAccordingTo("5,1,2,3".toIntList()) shouldBe false
    }

    @Test
    fun `this record only has one possible substitution`() {
        val (springs, nums) = "???.### 1,1,3".parseLine()

        val subs = allSubstitutionsBruteForce(springs).toList()

        val validSubs = subs.filter { it.isValidAccordingTo(nums) }

        validSubs.size shouldBe 1
    }
}

val testInput =
    """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent().lines()
