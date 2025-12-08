package day08

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector3D

private fun part1(input: List<String>, desiredMerges: Int): Long {
    val positions = junctionBoxPositions(input)
    val sortedDistances = pairs(positions).sortedBy { it.straightLineDifferenceSquared }
    val subGraphs = positions.map { setOf(it.first) }.toMutableSet()

    for (it in sortedDistances.take(desiredMerges)) {
        subGraphs.mergeCircuits(it.i1, it.i2)
    }

    return subGraphs.map { it.size }
        .sortedDescending().take(3)
        .fold(1L) { acc, x -> acc * x }
}

private fun part2(input: List<String>): Long {
    val positions = junctionBoxPositions(input)
    val sortedDistances = pairs(positions).sortedBy { it.straightLineDifferenceSquared }
    val subGraphs = positions.map { setOf(it.first) }.toMutableSet()

    for (it in sortedDistances) {
        subGraphs.mergeCircuits(it.i1, it.i2)

        if (subGraphs.size == 1) {
            return positions[it.i1].second.x.toLong() * positions[it.i2].second.x.toLong()
        }
    }

    throw IllegalStateException("Never coaesced to one circuit :-(")
}


private fun pairs(positions: List<Pair<Int, Vector3D<Int>>>): List<PairOfPositions> =
    positions.flatMap { (i1, pos1) ->
        positions.dropWhile { it != (i1 to pos1) }.drop(1).map { (i2, pos2) ->
            PairOfPositions(i1, pos1, i2, pos2)
        }
    }.filter { it.pos1 != it.pos2 }


private fun MutableSet<Set<Int>>.mergeCircuits(nodeNum1: Int, nodeNum2: Int) {
    val set1 = this.find { it.contains(nodeNum1) }!!
    val set2 = this.find { it.contains(nodeNum2) }!!

    if (set1 != set2) {
        this.remove(set1)
        this.remove(set2)
        this.add(set1 union set2)
    }
}

data class PairOfPositions(val i1: Int, val pos1: Vector3D<Int>, val i2: Int, val pos2: Vector3D<Int>) {
    val dx = pos1.x - pos2.x
    val dy = pos1.y - pos2.y
    val dz = pos1.z - pos2.z
    val straightLineDifferenceSquared = dx.toLong() * dx.toLong() + dy.toLong() * dy.toLong() + dz.toLong() * dz.toLong()
}

private fun junctionBoxPositions(input: List<String>): List<Pair<Int, Vector3D<Int>>> = input.withIndex().map { (i, it) ->
    val (x, y, z) = it.split(",")

    i to Vector3D(x.toInt(), y.toInt(), z.toInt())
}


class Day08Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput, 10) shouldBe 40
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day08.txt"), 1000) shouldBe 47040L
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 25272L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day08.txt")) shouldBe 4884971896L
    }
}

val testInput =
    """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent().lines()
