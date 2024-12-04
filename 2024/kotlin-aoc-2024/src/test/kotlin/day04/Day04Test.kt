package day04

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 18
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day04.txt")) shouldBe 2530
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 9
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day04.txt")) shouldBe 1921
    }

    @Test
    fun `finds xmas in horizontal`() {
        val input = """
            ....
            XMAS
            ....
            ....
        """.trimIndent().lines()

        part1(input) shouldBe 1
    }

    @Test
    fun `finds xmas in diagonal down right`() {
        val input = """
            X...
            .M..
            ..A.
            ...S
        """.trimIndent().lines()

        part1(input) shouldBe 1
    }

    @Test
    fun `finds xmas in diagonal up left`() {
        val input = """
            S...
            .A..
            ..M.
            ...X
        """.trimIndent().lines()

        part1(input) shouldBe 1
    }

    @Test
    fun `finds xmas in diagonal down left`() {
        val input = """
            ...X
            ..M.
            .A..
            S...
        """.trimIndent().lines()

        part1(input) shouldBe 1
    }
    @Test
    fun `finds xmas in diagonal up right`() {
        val input = """
            S...
            .A..
            ..M.
            ...X
        """.trimIndent().lines()

        part1(input) shouldBe 1
    }
}

val testInput =
    """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()

