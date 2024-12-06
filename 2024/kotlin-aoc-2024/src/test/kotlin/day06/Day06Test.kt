package day06

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 41
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day06.txt")) shouldBe 5242
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 6
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day06.txt")) shouldBe 1424
    }

    @Test
    fun `generates correct path from test data`() {
        val (map, start) = testInput.parseInput()
        val poses = pathUntilWeFallOffTheMap(start, map)

        val posesString = asString(map, poses)

        posesString shouldBe """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ..XXXXX#X.
            ..X.X.X.X.
            .#XXXXXXX.
            .XXXXXXX#.
            #XXXXXXX..
            ......#X..
        """.trimIndent()
    }

    @Test
    fun `finds correct cycle with tweaked map`() {
        val (map, start) = inputOf(
            """
                ....#.....
                .........#
                ..........
                ..#.......
                .......#..
                ..........
                .#.O^.....
                ........#.
                #.........
                ......#...
            """
        )

        asString(map, pathUntilWeDetectACycle(start, map)) shouldBe """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ....X..#X.
            ....X...X.
            .#.OXXXXX.
            ........#.
            #.........
            ......#...
        """.trimIndent()
    }

    @Test
    fun `finds correct cycle with another tweaked map`() {
        val (map, start) = inputOf(
            """
                ....#.....
                .........#
                ..........
                ..#.......
                .......#..
                ..........
                .#..^.....
                ......O.#.
                #.........
                ......#...
            """
        )

        resultsInCycle(start, map) shouldBe true

        asString(map, pathUntilWeDetectACycle(start, map)) shouldBe """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ..XXXXX#X.
            ..X.X.X.X.
            .#XXXXXXX.
            ......O.#.
            #.........
            ......#...
        """.trimIndent()
    }

    private fun inputOf(s: String) = s.trimIndent().lines().parseInput()
}

val testInput =
    """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()
