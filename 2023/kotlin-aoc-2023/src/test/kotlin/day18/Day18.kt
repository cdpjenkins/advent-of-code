package day18

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Long =
    input.map { Instruction.parseUsingPart1Rules(it) }
        .findAreaTheCleverWay()

private fun part2(input: List<String>): Long =
    input.map { Instruction.parseUsingPart2Rules(it) }
        .findAreaTheCleverWay()

private fun List<Instruction>.findAreaTheCleverWay(): Long {
    var x = 0L
    var y = 0L
    var area = 0L
    var perimeterLength = 0L

    forEach { instruction ->
        perimeterLength += instruction.distance

        when (instruction.direction) {
            Direction.R -> {
                x += instruction.distance
                area -= y * instruction.distance
            }

            Direction.D -> {
                y += instruction.distance
            }

            Direction.L -> {
                x -= instruction.distance
                area += y * instruction.distance
            }

            Direction.U -> {
                y -= instruction.distance
            }
        }
    }

    return area + (perimeterLength / 2) + 1
}

private fun MutableMap<Pos, Char>.asString(): String {
    val str = (minY()..maxY()).map { y ->
        (minX()..maxX()).map { x ->
            val stour = this[Pos(x, y)] ?: '.'
            stour
        }.joinToString("")
    }.joinToString("\n")
    return str
}

private fun parseLagoon(input: List<String>): MutableMap<Pos, Char> {
    val instructions = input.map { Instruction.parseUsingPart1Rules(it) }

    val lagoon = mutableMapOf<Pos, Char>()
    var currentLocation = Pos(0, 0)
    instructions.forEach { currentLocation = it.execute(lagoon, currentLocation) }
    return lagoon
}

fun MutableMap<Pos, Char>.minX() = minOf { (k, v) -> k.x }
fun MutableMap<Pos, Char>.maxX() = maxOf { (k, v) -> k.x }
fun MutableMap<Pos, Char>.minY() = minOf { (k, _) -> k.y }
fun MutableMap<Pos, Char>.maxY() = maxOf { (k, v) -> k.y }

data class Pos(
    val x: Int,
    val y: Int
) {
    operator fun plus(direction: Direction): Pos {
        return Pos(x + direction.dx, y + direction.dy)
    }

    operator fun plus(instruction: Instruction): Pos {
        return Pos(
            x + instruction.direction.dx * instruction.distance,
            y + instruction.direction.dy * instruction.distance
        )
    }
}

data class Instruction(
    val direction: Direction,
    val distance: Int,
) {
    companion object {
        val part1Regex = "^([UDLR]) (\\d+) \\((#[a-f0-9]{6})\\)$".toRegex()

        fun parseUsingPart1Rules(it: String): Instruction {
            val (directionString, distanceString, _) = it.parseUsingRegex(part1Regex)

            return Instruction(Direction.valueOf(directionString), distanceString.toInt())
        }

        val part2Regex = "^[UDLR] \\d+ \\(#([a-f0-9]{5})([a-f0-9])\\)$".toRegex()

        fun parseUsingPart2Rules(it: String): Instruction {
            val (distanceHexString, directionHexString) = it.parseUsingRegex(part2Regex)

            val direction = Direction.fromHex(directionHexString)

            return Instruction(direction, distanceHexString.toInt(16))
        }
    }

    fun execute(lagoon: MutableMap<Pos, Char>, currentLocation: Pos): Pos {
        var p = currentLocation
        for (i in (0 until distance)) {

            lagoon[p] = '#'
            p = p + direction
        }

        return p
    }
}

enum class Direction {
    U {
        override val dx: Int = 0
        override val dy: Int = -1
    }, D {
        override val dx: Int = 0
        override val dy: Int = 1
    }, L {
        override val dx: Int = -1
        override val dy: Int = 0
    }, R {
        override val dx: Int = 1
        override val dy: Int = 0
    };

    abstract val dx: Int
    abstract val dy: Int

    companion object {
        fun fromHex(hex: String): Direction {
            return when (hex) {
                "0" -> R
                "1" -> D
                "2" -> L
                "3" -> U
                else -> throw IllegalArgumentException(hex)
            }
        }
    }
}

class Day18Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 62
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day18.txt")) shouldBe 56923
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 952408144115L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day18.txt")) shouldBe -1
    }

    @Test
    fun `can parse and draw lagoon outline from test input`() {
        parseLagoon(testInput).asString() shouldBe
                """
            #######
            #.....#
            ###...#
            ..#...#
            ..#...#
            ###.###
            #...#..
            ##..###
            .#....#
            .######
        """.trimIndent()
    }
}

val testInput =
    """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent().lines()
