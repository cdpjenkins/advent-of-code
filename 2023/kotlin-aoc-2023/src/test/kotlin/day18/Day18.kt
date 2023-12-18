package day18

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

private fun part1(input: List<String>): Int {

    val lagoon = parseLagoon(input)

    lagoon.digOutInterior()

    return lagoon.count()
}

private fun MutableMap<Pos, Char>.digOutInterior() {
    this.floodFill(Pos(minX() - 1, minY() - 1))

    this.forEach { (pos, c) ->
    }

    (minY()..maxY()).forEach { y ->
        (minX()..maxX()).forEach { x ->
            val pos = Pos(x, y)
            val c = this[pos]

            if (c == null) {
                this[pos] = '#'
            } else if (c == 'x') {
                this.remove(pos)
            }
        }
    }

//    for (y in (minY()..maxY())) {
//        var inside = false
//        for (x in (minX()..maxX())) {
//            if (y == 1) {
//                println("poopoo")
//            }
//
//            val pos = Pos(x, y)
//            if (this[pos] == '#') {
//                inside = !inside
//            }
//
//            if (inside) {
//                this[pos] = '#'
//            }
//        }
//    }
}

private fun MutableMap<Pos, Char>.floodFill(initialPos: Pos) {

    val minX = minX()
    val maxX = maxX()
    val minY = minY()
    val maxY = maxY()

    val stack = Stack<Pos>()

    stack.push(initialPos)

    while (stack.isNotEmpty()) {
        val pos = stack.pop()

        val c = this[pos]
        if (c == null) {

            this[pos] = 'x'

            listOf(
                pos + Direction.U,
                pos + Direction.D,
                pos + Direction.L,
                pos + Direction.R
            ).filter {
                it.x > minX - 3 &&
                        it.x < maxX + 3 &&
                        it.y > minY - 3 &&
                        it.y < maxY + 3
            }.forEach { stack.push(it) }
        }

    }
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
    val regex = "^([UDLR]) (\\d+) \\((#[a-f0-9]{6})\\)$".toRegex()

    val instructions = input.map {
        val (directionString, distanceString, colourString) = it.parseUsingRegex(regex)

        Instruction(Direction.valueOf(directionString), distanceString.toInt(), colourString)
    }

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
}

data class Instruction(
    val direction: Direction,
    val distance: Int,
    val colourString: String
) {
    fun execute(lagoon: MutableMap<Pos, Char>, currentLocation: Pos): Pos {
        var p = currentLocation
        for (i in (0 until distance)) {

            lagoon[p] = '#'
            p = p + direction
        }

        return p
    }
}

private fun part2(input: List<String>): Int {
    return 123
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

    @org.junit.jupiter.api.Test
    fun `can dig out the interior from test input`() {
        val lagoon = parseLagoon(testInput)

        lagoon.digOutInterior()

        lagoon.asString() shouldBe
                """
                    #######
                    #######
                    #######
                    ..#####
                    ..#####
                    #######
                    #####..
                    #######
                    .######
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
