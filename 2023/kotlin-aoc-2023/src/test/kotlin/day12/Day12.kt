package day12

import FileUtil.readInputFileToList
import ListUtils.toIntList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import kotlin.test.Test

private fun part1(input: List<String>): Int {

    val tharLines = input.map { it.parseLine() }

    val lalala = tharLines.map { (springs, nums) -> findNumValidSubstitutions(springs, nums) }

    return lalala.sum()
}

fun findNumValidSubstitutions(springs: String, nums: List<Int>): Int {
    val ston = allSubstitutions(springs).count { it.isValidAccordingTo(nums) }
    return ston
}

val springsRegex = "(#+)".toRegex()
private fun String.isValidAccordingTo(nums: List<Int>): Boolean {
    val matches = springsRegex.findAll(this)

    val lengths = matches.map { it.value.length }.toList()

    return lengths == nums
}

private fun String.parseLine(): Pair<String, List<Int>> {
    val r = "^([?.#]+) ([0-9,]+)$".toRegex()

    val (springs, numsString) = this.parseUsingRegex(r)

    val ns = numsString.toIntList(separator = ",")

    return Pair(springs, ns)
}

fun allSubstitutions(springs: String): Sequence<String> {
    val numWildcards = springs.count { it == '?' }

    val numPossibilities = 1.shl(numWildcards)

    println("numposs $numPossibilities")

    return sequence {
        for (poss in (0 until numPossibilities)) {
            val sb = StringBuffer()
            var hashIndex = 0
            springs.withIndex().forEach { (i, c) ->
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
        part1(readInputFileToList("day12.txt")) shouldBe -1
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
        allSubstitutions("#?#").toSet() shouldBe setOf("#.#", "###")

    }

    @Test
    fun `substitutions are sane with two wildcards`() {
        allSubstitutions("??").toSet() shouldBe setOf("..", ".#", "#.", "##")
        allSubstitutions("?.?").toSet() shouldBe setOf("...", "..#", "#..", "#.#")
    }

    @Test
    fun `detects valid spring strings`() {
        ".#...#....###".isValidAccordingTo("1,1,3".toIntList()) shouldBe true
//        "#####...#.##.###".isValidAccordingTo("5,1,2,3".toIntList()) shouldBe true
    }

    @Test
    fun `detects invalid spring strings`() {
        "#####.##".isValidAccordingTo("3,1,2".toIntList()) shouldBe false
        "###.#.#".isValidAccordingTo("5,1,2,3".toIntList()) shouldBe false
    }

    @Test
    fun `ston`() {
        val (springs, nums) = "???.### 1,1,3".parseLine()

        val subs = allSubstitutions(springs).toList()

        println(subs.size)

//        subs.forEach {
//            println(it)
//        }

        val validSubs = subs.filter { it.isValidAccordingTo(nums) }
        println(validSubs)

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
