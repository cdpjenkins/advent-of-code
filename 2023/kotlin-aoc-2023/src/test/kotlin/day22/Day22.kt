package day22

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Ignore

private fun part1(input: List<String>) =
    input.parseToBricks()
        .fall()
        .supportMatrix()
        .toIsSoleSupportArray()
        .count { !it }

private fun part2(input: List<String>): Int {
    val fallenBricks = input.parseToBricks().fall()
    val supportMatrix = fallenBricks.supportMatrix()

    val mutableFallenBricks = fallenBricks.toMutableList()

    val results = mutableMapOf<Int, Int>()
    while (mutableFallenBricks.isNotEmpty()) {
        val thisBrick = mutableFallenBricks.removeLast()

        val bricksThatAreSolelySupportedBy =
            supportMatrix.bricksThatAreSolelySupportedBy(thisBrick.index, mutableSetOf(thisBrick.index)) - thisBrick.index

        results[thisBrick.index] = bricksThatAreSolelySupportedBy.size
    }

    return results.values.sum()
}

fun Array<BooleanArray>.bricksThatDirectlySupport(id: Int): Set<Int> =
    (0..<size).filter { this[id][it] }.toSet()

fun Array<BooleanArray>.bricksThatAreSolelySupportedBy(id: Int, supportSet: MutableSet<Int>): MutableSet<Int> {
    val directlyAbove = bricksThatAreDirectlySupportedBy(id, this)

    val notSupportedByAnythingElse = directlyAbove.filter {
        this.bricksThatDirectlySupport(it)
            .all { supporter -> supporter in supportSet }
    }

    supportSet += notSupportedByAnythingElse

    directlyAbove.filter { it in supportSet }
        .forEach { this.bricksThatAreSolelySupportedBy(it, supportSet) }

    return supportSet
}

fun bricksThatAreDirectlySupportedBy(id: Int, supportMatrix: Array<BooleanArray>): Set<Int> =
    (0..<supportMatrix.size).filter { supportMatrix[it][id] }.toSet()

private fun Array<BooleanArray>.toIsSoleSupportArray(): BooleanArray {
    val isSoleSupport = BooleanArray(size)
    for (possiblySupportedIndex in (0..<size)) {
        val supported = this[possiblySupportedIndex]

        val supports = supported.withIndex().filter { (i, b) -> b }
        if (supports.size == 1) {
            val index = supports.first().index
            isSoleSupport[index] = true
        }
    }
    return isSoleSupport
}

private fun List<Brick>.supportMatrix(): Array<BooleanArray> {
    val reversedBricks = reversed().toMutableList()

    val supportMatrix = Array(size) { BooleanArray(size) }
    while (reversedBricks.isNotEmpty()) {
        val thisBrick = reversedBricks.removeLast()

        val bricksWeSupport = reversedBricks.filter { thisBrick.supports(it) }

        bricksWeSupport.forEach {
            supportMatrix[it.index][thisBrick.index] = true
        }
    }
    return supportMatrix
}

private fun List<Brick>.fall(): List<Brick> {
    val sortedBricks = this.sortedBy { it.z1 }.toMutableList()
    sortedBricks.reverse()

    val fallenBricks = mutableListOf<Brick>()

    while (sortedBricks.isNotEmpty()) {
        val brick = sortedBricks.removeLast()

        val fallenBrick = brick.fallOnto(fallenBricks)

        fallenBricks.add(fallenBrick)
    }

    return fallenBricks.toList()
}

data class Brick(
    val index: Int,
    val x1: Int,
    val y1: Int,
    val z1: Int,
    val x2: Int,
    val y2: Int,
    val z2: Int
) {
    fun supports(that: Brick): Boolean {
        val overlaps = overlapsWith(that)
        val adjacentZs = this.z2 == that.z1 - 1

        return overlaps && adjacentZs
    }

    private fun xOverlapsWith(that: Brick) = max(this.x1, that.x1) <= min(this.x2, that.x2)
    private fun yOverlapsWith(that: Brick) = max(this.y1, that.y1) <= min(this.y2, that.y2)
    fun overlapsWith(that: Brick) = this.xOverlapsWith(that) && this.yOverlapsWith(that)

    fun fallOnto(fallenBricks: MutableList<Brick>): Brick {
        val filter = fallenBricks.filter { it.overlapsWith(this) }

        // strictly speaking, fallenBricks should already be sorted in z order...
        // TODO maybe try to use this... but not yet

        val brickThatWeAreFallingOnto = filter.maxByOrNull { it.z2 }

        val ourNewZThang = if (brickThatWeAreFallingOnto != null) brickThatWeAreFallingOnto.z2 + 1 else 1
        val amountFallen = z1 - ourNewZThang
        return Brick(this.index, this.x1, this.y1, ourNewZThang, this.x2, this.y2, this.z2 - amountFallen)
    }


    companion object {
        val regex = "^(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)$".toRegex()

        fun of(line: String, index: Int): Brick {
            val (x1, y1, z1, x2, y2, z2) = line.parseUsingRegex(regex)

            return Brick(
                index,
                x1.toInt(),
                y1.toInt(),
                z1.toInt(),
                x2.toInt(),
                y2.toInt(),
                z2.toInt()
            )
        }
    }
}

class Day22Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 5
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day22.txt")) shouldBe 501
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 7
    }

    @Ignore // this is much too inefficient to run every time
    // I feel really bad for brute-forcing this. But not quite bad enough to go away and study graph algorithms and
    // find a way to implement it more efficiently.
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day22.txt")) shouldBe 80948
    }

    @Test
    fun `bricks obscuring other bricks`() {
        val brick1 = Brick(1, 0, 0, 1, 10, 10, 1)
        val brick2 = Brick(2, 5, 5, 2, 15, 15, 2)
        val brick3 = Brick(3, 20, 20, 3, 30, 30, 3)
        val brick4 = Brick(4, 0, 0, 4, 10, 10, 11)

        brick1.supports(brick2) shouldBe true
        brick1.supports(brick3) shouldBe false
        brick1.supports(brick4) shouldBe false
    }

    @Test
    fun `moar overlappingness`() {
        val A = Brick.of("1,0,1~1,2,1", 0)
        val C = Brick.of("0,2,3~2,2,3", 1)

        A.overlapsWith(C) shouldBe true
    }

    @Test
    fun `moar falling bricks`() {
        val A = Brick.of("1,0,1~1,2,1", 0)
        val C = Brick.of("0,2,3~2,2,3", 1)

        val unfallen = listOf(A, C)

        val fallen = unfallen.fall()

        fallen shouldBe listOf(
                Brick(index=0, x1=1, y1=0, z1=1, x2=1, y2=2, z2=1),
                Brick(index=1, x1=0, y1=2, z1=2, x2=2, y2=2, z2=2),
            )
    }

    @Test
    fun `falling bricks`() {
        val bricks = testInput.parseToBricks()

        val sortedBricks = bricks.fall()

        sortedBricks shouldBe listOf(
            Brick(index=0, x1=1, y1=0, z1=1, x2=1, y2=2, z2=1),
            Brick(index=1, x1=0, y1=0, z1=2, x2=2, y2=0, z2=2),
            Brick(index=2, x1=0, y1=2, z1=2, x2=2, y2=2, z2=2),
            Brick(index=3, x1=0, y1=0, z1=3, x2=0, y2=2, z2=3),
            Brick(index=4, x1=2, y1=0, z1=3, x2=2, y2=2, z2=3),
            Brick(index=5, x1=0, y1=1, z1=4, x2=2, y2=1, z2=4),
            Brick(index=6, x1=1, y1=1, z1=5, x2=1, y2=1, z2=6)
        )

    }
}

private fun List<String>.parseToBricks() =
    withIndex().map { (i, line) -> Brick.of(line, i) }

val testInput =
    """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent().lines()
