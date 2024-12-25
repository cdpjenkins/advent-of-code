package day25

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {

    val (locks, keys) = parse(input)

    println("keys")
    keys.forEach { key ->
        println(key.joinToString(","))
    }

    val cock = keys.map { key ->
        locks.count { lock ->
            fits(key, lock)
        }
    }.sum()

    return cock
}

private fun parse(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val thangs = input.splitByBlank()

    val locks = thangs.filter { it.isLock() }.map { it.countMeDo() }
    val keys = thangs.filter { it.isKey() }.map { it.countMeDo() }

    return Pair(locks, keys)
}

fun fits(key: List<Int>, lock: List<Int>) = key.zip(lock).all { (a, b) -> a + b <= 5 }

private fun List<String>.countMeDo(): List<Int> {
    require(this.all { it.length == 5 })

    return (0..<5).map { x ->
        (0..<7).count { y ->
            this[y][x] == '#'
        } - 1
    }
}

private fun List<String>.isLock() = this.first().all { it == '#' }
private fun List<String>.isKey() = this.last().all { it == '#' }

private fun part2(input: List<String>): Int {
    return 123
}

class Day25Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 3
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day25.txt")) shouldBe -1
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

    @Test
    fun `can work out if a key fits a lock`() {
        fits(listOf(0,5,3,4,3), listOf(5,0,2,1,3)) shouldBe false
        fits(listOf(0,5,3,4,3), listOf(3,0,2,0,1)) shouldBe true
    }
}

val testInput =
    """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....

        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....

        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####

        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####

        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        #####
    """.trimIndent().lines()
