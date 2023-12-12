package day12

import FileUtil.readInputFileToList
import ListUtils.toIntList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Long {
    val records = input.map { it.parseLine() }

    return records.map {
        val numSubstitutionsTheCleverWay = it.numSubstitutionsTheCleverWay()
        println(numSubstitutionsTheCleverWay)
        numSubstitutionsTheCleverWay
    }.sum()
}

private fun part2(input: List<String>): Long {
    val r = input.map { it.parseLine() }

    val springRecords = r.map {
        ConditionRecord(
            it.springs + "?" + it.springs + "?" +  it.springs + "?" +  it.springs + "?" +  it.springs,
            it.groups + it.groups + it.groups + it.groups + it.groups
        )
    }

    return springRecords.map { it.numSubstitutionsTheCleverWay() }.sum()
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

data class ParserState(
    val i: Int,
    val currentlyConsumingAGroup: Boolean,
    val springsInGroup: Int,
    val groupIndex: Int,
    val totalWildcardsConsumed: Int,
    val str:String  = ""
) {
    fun consume(c: Char): ParserState {
//        println("say ${c}")
        val tempi = i + 1
        when (c) {
            '.' -> {
                return this.copy(
                    i = tempi,
                    currentlyConsumingAGroup = false,
                    groupIndex = groupIndex,
                    str = str + c
                )
            }
            '#' -> {
                return this.copy(
                    i = tempi,
                    currentlyConsumingAGroup = true,
                    springsInGroup = if (currentlyConsumingAGroup) springsInGroup + 1 else 1,
                    groupIndex = if (currentlyConsumingAGroup) groupIndex else groupIndex + 1,
                    str = str + c
                )
            }
            else -> {
                // We don't know how to consume a wildcard here. Caller should call us twice, once for . once for #
                // in order to get _two_ states.
                throw IllegalArgumentException("?")
            }
        }
    }
}


data class ConditionRecord(
    val springs: String,
    val groups: List<Int>
) {
    val totalWildcards = springs.count { it == '?' }

    fun numSubstitutionsTheCleverWay(): Long {
        val initialState = ParserState(
            i = 0,
            currentlyConsumingAGroup = false,
            springsInGroup = 0,
            groupIndex = -1,
            totalWildcardsConsumed = 0
        )


        return findAllTheSubstitutionsInnit(initialState)
    }

    fun spaces(n: Int): String {
        return (0..n).map { " " }.joinToString()
    }

    private fun findAllTheSubstitutionsInnit(state: ParserState): Long {

//        println("${spaces(state.i)}$state")

//        if (state.str == "##.") {
//            println("poo")
//        }

        if (state.groupIndex >= groups.size) {
            return 0
        }

        if (!state.currentlyConsumingAGroup && state.springsInGroup < groups.at(state.groupIndex)) {
            return 0L
        }

        if (state.groupIndex >= 0 && state.springsInGroup > groups.at(state.groupIndex)) {
            return 0
        }

        if (state.totalWildcardsConsumed > totalWildcards) {
            return 0
        }

        if (state.groupIndex >= 0 && state.springsInGroup > groups.at(state.groupIndex)) {
            return 0L
        }

        if (state.i == springs.length) {
            if (state.totalWildcardsConsumed == totalWildcards) {
                if (state.groupIndex == groups.size - 1 && state.springsInGroup == groups.at(state.groupIndex)) {
//                    println("${spaces(state.i + 1)}return 1 ${state.str}")
                    return 1
                } else {
                    return 0
                }
            } else {
                return 0
            }
        }

//        println("${spaces(state.i)} consume ${springs[state.i]}")


        return when (springs[state.i]) {
            '?' -> {
                val consumed = state.copy(
                    totalWildcardsConsumed = state.totalWildcardsConsumed + 1,
                )
                findAllTheSubstitutionsInnit(consumed.consume('.')) +
                        findAllTheSubstitutionsInnit(consumed.consume('#'))
            }
            else -> {
                findAllTheSubstitutionsInnit(state.consume(springs[state.i]))
            }
        }
    }

    private fun List<Int>.at(i: Int) = if (i >= 0) this[i] else -1

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

class Day12Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 21
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day12.txt")) shouldBe 7705
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 525152
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day12.txt")) shouldBe -1
    }

    @Test
    fun `substitutions are sane for single wildcard`() {
        allSubstitutionsBruteForce("?").toSet() shouldBe setOf(".", "#")
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

    @Test
    fun `can find num substitutions the clever way for v simple input2`() {
//        ConditionRecord.of("? 1").numSubstitutionsTheCleverWay() shouldBe 1
//        ConditionRecord.of("?? 2").numSubstitutionsTheCleverWay() shouldBe 1
//        ConditionRecord.of(".? 2").numSubstitutionsTheCleverWay() shouldBe 0
        ConditionRecord.of("#? 2").numSubstitutionsTheCleverWay() shouldBe 1
//        ConditionRecord.of(".? 1").numSubstitutionsTheCleverWay() shouldBe 1
//        ConditionRecord.of("??? 3").numSubstitutionsTheCleverWay() shouldBe 1
    }

    @Test
    fun `can find num substitutions the clever way inputs with two groups`() {
        ConditionRecord.of("??.? 2,1").numSubstitutionsTheCleverWay() shouldBe 1
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
