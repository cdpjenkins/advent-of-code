package day10

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D

private fun part1(input: List<String>): Int {
    val map = input.parse()
    val trailheads = map.filter { it.value == 0 }.map { it.key }
    val summits = trailheads.map {it.findReachableSummitsByBFS(map) }
    return summits.sumOf { it.size }
}

private fun part2(input: List<String>): Int {
    val map = input.parse()
    val trailheads = map.filter { it.value == 0 }.map { it.key }
    val trails = trailheads.map { it.findPathsByDFS(map) }
    return trails.sumOf { it.size }
}

private fun Vector2D.findPathsByDFS(map: Map<Vector2D, Int>): List<List<Vector2D>> {
    val paths = mutableListOf<List<Vector2D>>()

    fun dfs(current: Vector2D, pathSoFar: MutableList<Vector2D>) {
        if (current !in map) return
        pathSoFar.add(current)

        if (map[current] == 9) {
            paths.add(pathSoFar)
        }

        val currentHeight = map[current]!!
        current.getNeighbours()
            .filter { it in map }
            .filter { map[it]!! == currentHeight + 1 }
            .forEach { neighbour ->
                dfs(neighbour, pathSoFar)
            }
    }

    dfs(this, mutableListOf())
    return paths
}

fun List<String>.parse() =
    flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Vector2D(x, y) to char.digitToInt()
        }
    }.toMap()

private fun Vector2D.findReachableSummitsByBFS(map: Map<Vector2D, Int>): Collection<Vector2D> {
    val queue = ArrayDeque<Vector2D>()
    val visited = mutableSetOf<Vector2D>()
    val summits = mutableSetOf<Vector2D>()

    queue.add(this)
    visited.add(this)

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentHeight = map[current]!!

        if (map[current] == 9) {
            summits.add(current)
        }

        current.getNeighbours()
            .filter { it in map }
            .filter { it !in visited }
            .filter { (map[it]!!) == currentHeight + 1 }
            .forEach { neighbour ->
                queue.add(neighbour)
                visited.add(neighbour)
            }
    }

    return summits.toList()
}

fun Vector2D.getNeighbours(): List<Vector2D> {
    return listOf(
        Vector2D(x - 1, y),
        Vector2D(x + 1, y),
        Vector2D(x, y - 1),
        Vector2D(x, y + 1)
    )
}

class Day10Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 36
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day10.txt")) shouldBe 587
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 81
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day10.txt")) shouldBe 1340
    }
}

val testInput =
    """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()
