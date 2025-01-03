package day08

import utils.Vector2D

fun part1(input: List<String>) =
    AntennaMap.parse(input)
        .findAntinodesByTwiceDistance()
        .size

fun part2(input: List<String>) =
    AntennaMap.parse(input)
        .findAntinodesByResonantHarmonics()
        .size

class AntennaMap(
    val map: Map<Vector2D, Char>,
    val antennas: Map<Char, List<Vector2D>>,
    val width: Int,
    val height: Int
) {
    fun findAntinodesByTwiceDistance(): Set<Vector2D> =
        antennas
            .keys
            .flatMap { frequency -> findAntinodesForFrequencyByTwiceDistance(frequency) }
            .toSet()

    private fun findAntinodesForFrequencyByTwiceDistance(frequency: Char): Set<Vector2D> {
        val antennasOfThisFrequency = antennas[frequency]!!

        val pairs = antennasOfThisFrequency.flatMapIndexed { i, pos1 ->
            antennasOfThisFrequency.drop(i + 1).map { pos2 ->
                pos1 to pos2
            }
        }

        return pairs.flatMap { antinodesForAPairOfAntennaeByTwiceDistance(it.first, it.second) }.toSet()
    }

    private fun antinodesForAPairOfAntennaeByTwiceDistance(first: Vector2D, second: Vector2D): List<Vector2D> {
        val displacement = second - first

        val firstAntinode = second + displacement
        val secondAntinode = first - displacement

        return listOf(firstAntinode, secondAntinode)
            .filter { it.isWithinBoundsOfMap() }
    }

    private fun Vector2D.isWithinBoundsOfMap() = x >= 0 && y >= 0 && x < width && y < height

    fun showAntinodesAsString(): String {
        val antinodes = findAntinodesByTwiceDistance()

        return (0 until width).map { y ->
            (0 until height).map { x ->
                if (antinodes.contains(Vector2D(x, y))) '#'
                else map[Vector2D(x, y)] ?: '.'
            }.joinToString("")
        }.joinToString("\n")
    }

    fun findAntinodesByResonantHarmonics(): Set<Vector2D> =
        antennas
        .keys
        .flatMap { frequency -> findAntinodesForFrequencyByResonantHarmonics(frequency) }
        .toSet()

    private fun findAntinodesForFrequencyByResonantHarmonics(frequency: Char): Set<Vector2D> {
        val antennasOfThisFrequency = antennas[frequency]!!

        val pairs = antennasOfThisFrequency.flatMapIndexed { i, pos1 ->
            antennasOfThisFrequency.drop(i + 1).map { pos2 ->
                pos1 to pos2
            }
        }

        return pairs.flatMap { antinodesForAPairOfAntennaeByResonantHarmonics(it.first, it.second) }.toSet()
    }

    private fun antinodesForAPairOfAntennaeByResonantHarmonics(first: Vector2D, second: Vector2D): Set<Vector2D> {
        val displacement = second - first

        val goingOneWay = generateSequence(second) { it + displacement }
            .takeWhile { it.isWithinBoundsOfMap() }.toSet()

        val goingTheOtherWay = generateSequence(first) { it - displacement }
            .takeWhile { it.isWithinBoundsOfMap() }.toSet()

        return goingOneWay union goingTheOtherWay
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