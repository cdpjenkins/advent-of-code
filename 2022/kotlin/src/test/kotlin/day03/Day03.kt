package day03

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class Day03 {

    @Test
    internal fun `part 1 sample input`() {
        testInput.sumOfRucksackScores() shouldBe 157
    }

    @Test
    internal fun `part 1 real input`() {
        realInput.sumOfRucksackScores() shouldBe 7766
    }

    @Test
    internal fun `part2 test input`() {
        testInput.sumOfItemsSharedInGroupsOfThree() shouldBe 70
    }

    @Test
    internal fun `part2 real input`() {
        realInput.sumOfItemsSharedInGroupsOfThree() shouldBe 2415
    }
}

private fun List<String>.sumOfRucksackScores() = sumOf(String::rucksackScore)

private fun List<String>.sumOfItemsSharedInGroupsOfThree(): Int =
    chunked(3)
        .map(::scoreOfSharedItemInGroupOfThreeRucksacks)
        .sumOf(::priority)

private fun scoreOfSharedItemInGroupOfThreeRucksacks(rucksacks: List<String>): Char =
    rucksacks.map { it.toSet() }
        .reduce(Set<Char>::intersect)
        .assertSingleElement()
        .first()

private fun Set<Char>.assertSingleElement(): Set<Char> =
    if (size != 1) {
        throw IllegalArgumentException(toString())
    } else {
        this
    }

private fun String.rucksackScore(): Int =
    take(length / 2).toSet()
        .intersect(drop(length / 2).toSet())
        .sumOf { priority(it) }

private fun priority(item: Char): Int =
    when (item) {
        in 'a'..'z' -> item - 'a' + 1
        in 'A'..'Z' -> item - 'A' + 27
        else -> throw IllegalArgumentException(item.toString())
    }

val testInput =
    """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent().lines()

val realInput = readInputFileToList("day03.txt")
