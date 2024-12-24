package day14

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import utils.Vector2D

private fun part1(input: List<String>, width: Int, height: Int) =
    generateSequence(Bathroom.of(input, width, height)) { it.step() }
        .drop(100)
        .first()
        .robotsInQuadrants()
        .fold(1) { acc, x -> acc * x }

private fun part2(input: List<String>, width: Int, height: Int): Int {
//    val startPos = 7400
//    val (i, s) =
//        generateSequence(Bathroom.of(input, width, height)) { it.step() }
//            .drop(startPos)
//            .mapIndexed { i, b -> i to b.asString() }
//            .find { (i, s) -> s.contains("##########") }
//            ?: throw IllegalStateException("Couldn't find the tree :-(")
//
//    println(s)
//
//    return i + startPos
    val result = 7492
    val tree = generateSequence(Bathroom.of(input, width, height)) { it.step() }
        .drop(result)
        .first()
        .asString()

    //    println(tree)
    require(tree.contains("###############################"))

    return result

}

data class Bathroom(val width: Int, val height: Int, val robots: List<Robot>) {
    fun step() = copy(robots = robots.map { it.step(this) })

    fun robotsInQuadrants(): List<Int> {
        val topLeftQuadrant = Rectangle(Vector2D(0, 0), width / 2, height / 2)
        val topRightQuadrant = Rectangle(Vector2D(width / 2 + 1, 0), width / 2, height / 2)
        val bottomLeftQuadrant = Rectangle(Vector2D(0, height / 2 + 1), width / 2, height / 2)
        val bottomRightQuadrant = Rectangle(Vector2D(width / 2 + 1, height / 2 + 1), width / 2, height / 2)

        return listOf(
            robots.count { topLeftQuadrant.contains(it.p) },
            robots.count { topRightQuadrant.contains(it.p) },
            robots.count { bottomLeftQuadrant.contains(it.p) },
            robots.count { bottomRightQuadrant.contains(it.p) }
        )
    }

    fun asString(): String {
        return (0 until height).map { y ->
            (0 until width).map { x ->
                if (Vector2D(x, y) in robots.map { it.p }) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }

    companion object {
        fun of(
            input: List<String>,
            width: Int,
            height: Int
        ): Bathroom {
            val ROBOT_REGEX = """^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""".toRegex()
            val robots = input.map {
                val (px, py, vx, vy) = it.parseUsingRegex(ROBOT_REGEX)
                Robot(Vector2D(px.toInt(), py.toInt()), Vector2D(vx.toInt(), vy.toInt()))
            }

            return Bathroom(width = width, height = height, robots = robots)
        }
    }
}

class Rectangle(val topLeft: Vector2D, val width: Int, val height: Int) {
    fun contains(pos: Vector2D) = (pos - topLeft).let { it.x in 0..<width && it.y in 0..<height }
}

data class Robot(val p: Vector2D, val v: Vector2D) {
    fun step(bathroom: Bathroom) = copy(p = (p + v).teleportToBeWithin(bathroom))
}

private fun Vector2D.teleportToBeWithin(bathroom: Bathroom) =
    Vector2D(
        x.mod(bathroom.width),
        y.mod(bathroom.height)
    )

class Day14Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput, 11, 7) shouldBe 12
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day14.txt"), 101, 103) shouldBe 208437768
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day14.txt"), 101, 103) shouldBe 7492
    }
}

val testInput =
    """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines()
