package day14

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val platform = Platform.of(input)

    val rolled = platform.rollNorth()

    val result = rolled.roundedRocks.sumOf { rolled.height - it.y }

    return result
}

data class Platform(val width: Int, val height: Int, val roundedRocks: Set<Pos>, val cubeRocks: Set<Pos>) {
    companion object {
        fun of(input: List<String>): Platform {
            val roundedRocks = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    if (c == 'O') {
                        Pos(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
                .toSet()

            val cubeRocks = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    if (c == '#') {
                        Pos(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
                .toSet()

            val width = input.first().length
            val height = input.size

            val platform = Platform(width, height, roundedRocks, cubeRocks)
            return platform
        }
    }
    fun rollNorth(): Platform {
        val sorted = roundedRocks.sortedBy { it.y }

        return this.rollNorth(sorted)
    }

    private fun rollNorth(sortedRoundedRocks: List<Pos>): Platform {
        return if (sortedRoundedRocks.isEmpty()) {
            this
        } else {
            val roundedRock = sortedRoundedRocks.first()
            this.rollNorth(roundedRock).rollNorth(sortedRoundedRocks.drop(1))
        }
    }

    fun rollNorth(roundedRock: Pos): Platform {
        val newRock = rockRollsNorthTo(roundedRock)
        val removed = roundedRocks - roundedRock
        val newRoundedRocks = removed + newRock

        return this.copy(roundedRocks = newRoundedRocks)
    }

    fun rockRollsNorthTo(roundedRock: Pos): Pos {
        val stonList = (roundedRock.y - 1 downTo 0).takeWhile { y ->
            val pos = Pos(roundedRock.x, y)
            !(pos in roundedRocks) && !(pos in cubeRocks)
        }
        val rollingTo = stonList.lastOrNull()

        val newY = rollingTo ?: roundedRock.y

        val newRock = Pos(roundedRock.x, newY)
        return newRock
    }

    fun string(): String {
        return (0 until height).map { y ->
            (0 until width).map { x ->
                val pos = Pos(x, y)
                when (pos) {
                    in roundedRocks -> 'O'
                    in cubeRocks -> '#'
                    else -> '.'
                }
            }.joinToString("")
        }.joinToString("\n")
    }

}

data class Pos(val x: Int, val y: Int) {

}

private fun part2(input: List<String>): Int {
    return 123
}

class Day14Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 136
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day14.txt")) shouldBe -1
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
    fun `rolls single rock north correctly`() {
        val platform = Platform.of(testInput)

        val rolled = platform.rollNorth(Pos(0, 3))

        rolled.string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    O....##...
                    .O.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()

    }

    @Test
    fun `blocked rock does not roll`() {
        val platform = Platform.of(testInput)

        val rolled = platform.rollNorth(Pos(0, 1))

        rolled.string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    .....##...
                    OO.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()
    }

    @Test
    fun `rolls a rock until it hits another rock`() {
        val platform = Platform.of(testInput)

        val rolled = platform.rollNorth(Pos(0, 3))

        rolled.string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    O....##...
                    .O.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()
    }

    @Test
    fun `rolls simple rock north correctly`() {
        val platform = Platform.of(
            """
                ...
                O..
                ...
            """.trimIndent().lines()
        )

        val rolled = platform.rollNorth(Pos(0, 1))

        rolled.string() shouldBe
                """
                    O..
                    ...
                    ...
                """.trimIndent()
    }

    @Test
    fun `rolls simple rock north correctly to correct pos and stuff`() {
        val platform = Platform.of(testInput)

        val rolled = platform.rockRollsNorthTo(Pos(1, 3))

        rolled shouldBe Pos(1, 0)
    }

    @Test
    fun `rolls all rocks north correctly`() {
        val platform = Platform.of(testInput)

        val rolled = platform.rollNorth()

        rolled.string() shouldBe
                """
                    OOOO.#.O..
                    OO..#....#
                    OO..O##..O
                    O..#.OO...
                    ........#.
                    ..#....#.#
                    ..O..#.O.O
                    ..O.......
                    #....###..
                    #....#....
                """.trimIndent()
    }
}

val testInput =
    """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent().lines()
