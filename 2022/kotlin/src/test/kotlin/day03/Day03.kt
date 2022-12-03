package day03

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class Day03 {

    @Test
    internal fun `part 1 sample input`() {
        val score = testInput.sumOf(String::rucksackScore)

        score shouldBe 157
    }

    @Test
    internal fun `part 1 real input`() {
        val score = realInput.sumOf(String::rucksackScore)

        score shouldBe 7766
    }

    @Test
    internal fun `part2 test input`() {
        val intersects = testInput.chunked(3).map { it.part2Score() }

        val sumOf = intersects.sumOf(Char::priority)

        sumOf shouldBe 70
    }

    @Test
    internal fun `part2 real input`() {
        val intersects = realInput.chunked(3).map { it.part2Score() }

        val sumOf = intersects.sumOf(Char::priority)

        sumOf shouldBe 2415
    }
}

private fun List<String>.part2Score(): Char {
    val sets = this.map { it.toSet() }
    val intersectMeDo = sets.reduce(Set<Char>::intersect)

    if (intersectMeDo.size != 1) {
        throw IllegalArgumentException(intersectMeDo.toString())
    }

    return intersectMeDo.first()
}

private fun String.rucksackScore(): Int {
    val compartmentSize = this.length / 2

    val compartment1 = this.take(compartmentSize).toSet()
    val compartment2 = this.drop(compartmentSize).toSet()

    val intersect = compartment1.intersect(compartment2)

    println(intersect)

    return intersect.map { it.priority() }.sum()
}

private fun Char.priority(): Int {
    if (this >= 'a' && this <= 'z') {
        return this - 'a' + 1
    } else if (this >= 'A' && this <= 'Z') {
        return this - 'A' + 27
    } else {
        throw IllegalArgumentException(this.toString())
    }
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
