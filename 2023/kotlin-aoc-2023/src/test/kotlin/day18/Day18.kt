package day18

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.max
import kotlin.math.min

private fun Pair<Int, Int>.sorted() = if (first < second) this else second to first

private fun part1(input: List<String>): Long {
    val instructions = input.map { Instruction.parseUsingPart1Rules(it) }

    return instructions.findAreaTheCleverWay()
}

private fun part2(input: List<String>): Long {
    val instructions = input.map { Instruction.parseUsingPart2Rules(it) }

    return instructions.findAreaTheCleverWay()
}

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

    println("area: $area")
    println("perimeter length: " + perimeterLength)

    return area + (perimeterLength / 2) + 1
}

private fun part1_meh(input: List<String>): Int {
    val lines = Line.parseToLines(input)

    val verticalLines = lines.filter { it.direction == Direction.U || it.direction == Direction.D }

    val sortedInterestingYs =
        (verticalLines.map { it.start.y } +
            verticalLines.map { it.end.y })
            .toSet().sorted()

    val yIntervals = sortedInterestingYs.zipWithNext().map { it.sorted() }

    val slicedIntervals = sliceThoseIntervals(yIntervals)

    val area = slicedIntervals.map { (start, end) ->
        println("$start to $end")

        val linesThatIntersect = verticalLines.filter { it.intersectsWith(y = start) }.sortedBy { it.start.x }.toList()

        println(linesThatIntersect)

        val horizThangs = horizontalRangeThingies(linesThatIntersect)

        val horizontalGroundCovered = horizThangs.sumOf { it.endInclusive + 1 - it.start }

        println(horizontalGroundCovered)

        horizontalGroundCovered * (end + 1 - start)
    }.sum()

    val maxY = lines.filter { it.direction == Direction.L }.maxBy { it.start.y }
    println("maxY: $maxY")

    return area
}

// TODO meh just use pairs
fun horizontalRangeThingies(inScope: List<Line>): List<IntRange> {
    val rangeThingies = mutableListOf<IntRange>()
    var remaining = inScope

    while (remaining.isNotEmpty()) {
        val startThang = remaining.first()
        require(startThang.direction == Direction.U) {
            println("bad thang: $inScope")
            "urgh ${startThang}"
        }
        val gotRidOfAllTheStarts = remaining.
            dropWhile { it.direction == Direction.U }
        val endThang = gotRidOfAllTheStarts.takeWhile { it.direction == Direction.D }.last()

        rangeThingies.add(startThang.start.x..endThang.start.x)
        remaining = gotRidOfAllTheStarts.dropWhile { it.direction == Direction.D }
    }

    return rangeThingies.toList()
}

private fun sliceThoseIntervals(yIntervals: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val slicedIntervals: List<Pair<Int, Int>> = yIntervals.flatMap {
        val (start, end) = it

        if (end - start == 0) {
            listOf(it)
        } else if (end - start == 1) {
            listOf(start to start)
        } else {
            listOf(start to start, (start + 1) to (end - 1))
        }
    } + (yIntervals.last().second to yIntervals.last().second)
    return slicedIntervals
}

private fun List<Instruction>.toLines(): Sequence<Line> {
    var pos = Pos(0, 0)
    val lines = sequence {
        forEach {
            val newPos = pos + it
            this.yield(Line(pos, newPos, it.direction))

            pos = newPos
        }
    }
    return lines
}

data class Line(
    val start: Pos,
    val end: Pos,
    val direction: Direction
) {
    companion object {
        fun parseToLines(input: List<String>): Sequence<Line> {
            val instructions = input.map { Instruction.parseUsingPart1Rules(it) }

            return instructions.toLines()
        }
    }

    fun intersectsWith(y: Int): Boolean {
        check((direction == Direction.U || direction == Direction.D)) { direction.toString() }

        return y in (min(start.y, end.y)..max(start.y, end.y))
    }
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

    @Test
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

    @Test
    fun `can slice up intervals`() {
        val unsliced = listOf(
            0 to 1,
            1 to 3,
            3 to 10
        )

        val sliced = sliceThoseIntervals(unsliced)

        sliced shouldBe listOf(
            0 to 0,
            1 to 1,
            2 to 2,
            3 to 3,
            4 to 9,
            10 to 10
        )
    }

    @Test
    fun `can dudify ranges`() {
        val someLines = listOf(
            Line(Pos(0, 0), Pos(0, 0), Direction.U),
            Line(Pos(10, 0), Pos(10, 0), Direction.U),
            Line(Pos(20, 0), Pos(20, 0), Direction.D),
            Line(Pos(30, 0), Pos(30, 0), Direction.U),
            Line(Pos(40, 0), Pos(40, 0), Direction.D)
        )

        horizontalRangeThingies(someLines) shouldBe listOf(0..20, 30..40)

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
