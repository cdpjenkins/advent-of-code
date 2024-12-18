package day12

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun `part 1 with simple test input`() {
        part1(simpleTestInput) shouldBe 140
    }

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1930
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day12.txt")) shouldBe 1464678
    }

    @Test
    fun `part 2 with simple test input`() {
        part2(simpleTestInput) shouldBe 80
    }

    @Test
    fun `part 2 with  test input`() {
        part2(testInput) shouldBe 1206
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day12.txt")) shouldBe 877492
    }
}

val simpleTestInput =
    """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()

val testInput =
    """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent().lines()
