package day08

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>) =
    AntennaMap
        .parse(input)
        .findAntinodes()
        .size

class AntennaMap(
    val map: Map<Vector2D, Char>,
    val antennas: Map<Char, List<Vector2D>>,
    val width: Int,
    val height: Int
) {
    fun findAntinodes(): Set<Vector2D> =
        antennas
            .keys
            .flatMap { frequency -> findAntinodesForFrequency(frequency) }
            .toSet()

    private fun findAntinodesForFrequency(frequency: Char): Set<Vector2D> {
        val antennasOfThisFrequency = antennas[frequency]!!

        val pairs = antennasOfThisFrequency.flatMapIndexed { i, pos1 ->
            antennasOfThisFrequency.drop(i + 1).map { pos2 ->
                pos1 to pos2
            }
        }

        return pairs.flatMap { antinodesFor(it.first, it.second) }.toSet()
    }

    private fun antinodesFor(first: Vector2D, second: Vector2D): List<Vector2D> {
        val displacement = second - first

        val firstAntinode = second + displacement
        val secondAntinode = first - displacement

        return listOf(firstAntinode, secondAntinode)
            .filter { it.x >= 0 && it.y >= 0 && it.x < width && it.y < height }
    }

    fun showAntinodesAsString(): String {
        val antinodes = findAntinodes()

        return (0 until width).map { y ->
            (0 until height).map { x ->
                if (antinodes.contains(Vector2D(x, y))) '#'
                else map[Vector2D(x, y)] ?: '.'
            }.joinToString("")
        }.joinToString("\n")
    }

    companion object {
        fun parse(input: String): AntennaMap {
            return parse(input.trimIndent().lines())
        }

        fun parse(input: List<String>): AntennaMap {
            val map = input.flatMapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    Vector2D(x, y) to c
                }
            }.toMap()

            val antennaTypes = map.values.distinct().filter { it != '.' }

            val antennas = antennaTypes.map { type ->
                type to map.entries.filter { it.value == type }.map { it.key }
            }.toMap()

            val width = input[0].length
            val height = input.size

            return AntennaMap(map, antennas, width, height)
        }
    }
}

data class Vector2D(val x: Int, val y: Int) {
    operator fun minus(that: Vector2D): Vector2D =
        Vector2D(
            this.x - that.x,
            this.y - that.y
        )

    operator fun plus(that: Vector2D): Vector2D =
        Vector2D(
            this.x + that.x,
            this.y + that.y
        )
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day08Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 14
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day08.txt")) shouldBe 390
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
    fun `finds antinodes of two antennae`() {
        AntennaMap.parse(
            """
                ..........
                ..........
                ..........
                ....a.....
                ..........
                .....a....
                ..........
                ..........
                ..........
                ..........
            """).showAntinodesAsString() shouldBe
            """
                ..........
                ...#......
                ..........
                ....a.....
                ..........
                .....a....
                ..........
                ......#...
                ..........
                ..........
            """.trimIndent()
    }

    @Test
    fun `finds antinodes of three antennae`() {
        AntennaMap.parse(
            """
                ..........
                ..........
                ..........
                ....a.....
                ........a.
                .....a....
                ..........
                ..........
                ..........
                ..........
            """).showAntinodesAsString() shouldBe """
                ..........
                ...#......
                #.........
                ....a.....
                ........a.
                .....a....
                ..#.......
                ......#...
                ..........
                ..........
            """.trimIndent()
    }

}

val testInput =
    """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()
