package day25

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class Day25Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 3
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day25.txt")) shouldBe 3495
    }

    @Test
    fun `can work out if a key fits a lock`() {
        listOf(0, 5, 3, 4, 3).fitsIn(listOf(5,0,2,1,3)) shouldBe false
        listOf(0, 5, 3, 4, 3).fitsIn(listOf(3,0,2,0,1)) shouldBe true
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
