package day24

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Ignore

private fun part1(input: List<String>, testArea: ClosedFloatingPointRange<Double>): Int {
    val hailstones = input.map { Hailstone.of(it) }

    val indexedHailstones = hailstones.withIndex()

    val stonstour = indexedHailstones.flatMap { (i1, h1) ->
        indexedHailstones.map { (i2, h2) ->
            if (i1 < i2) {

                h1.findIntersection(h2)?.let {
                    if (it.x in testArea && it.y in testArea) {
                        1
                    } else {
                        0
                    }
                } ?: 0
            } else {
                0
            }
        }
    }

    return stonstour.sum()
}

data class Hailstone(val position: Vector3D, val velocity: Vector3D) {

    fun toLine(): Line {
        val a = velocity.y
        val b = -velocity.x
        val c = velocity.y * position.x - velocity.x * position.y

        return Line(a, b, c)
    }

    fun findIntersection(that: Hailstone): Vector2D? {

        val thisVelocity2D = this.velocity.projectXY()
        val thatVelocity2D = that.velocity.projectXY()

        val (a1, b1, c1) = this.toLine()
        val (a2, b2, c2) = that.toLine()

        val denom = a1 * b2 - a2 * b1
        if (denom == 0.0) return null

        val x = -(b1 * c2 - b2 * c1) / denom
        val y = -(c1 * a2 - c2 * a1) / denom

        val intersectionpoint = Vector2D(x, y)

        val fuck = (intersectionpoint - this.position.projectXY()) dotProduct thisVelocity2D
        if (fuck < 0.0) {
            return null
        }

        val you = (intersectionpoint - that.position.projectXY()) dotProduct thatVelocity2D
        if (you < 0.0) {
            return null
        }

        return intersectionpoint

//        val thisVelocity2D = this.velocity.projectXY()
//        val thatVelocity2D = that.velocity.projectXY()
//
//        if (abs(1.0 - (thisVelocity2D.normalise() dotProduct thatVelocity2D.normalise())) < 0.001) {
//            return null
//        }
//
//        val thisPosition2D = this.position.projectXY()
//
//        val displacement2D = that.position.projectXY() - this.position.projectXY()
//
//        val normal = thatVelocity2D.rotate90Degrees().normalise()
//
//        val normalisedDisplacement = displacement2D dotProduct normal
//
//        val vClosingTheGap = thisVelocity2D dotProduct normal
//
//        val t = normalisedDisplacement / vClosingTheGap
//
//
//        val intersectionPoint = thisPosition2D + thisVelocity2D * abs(t)
//
//        return intersectionPoint
    }

    private fun determinant(ac: Vector2D, bd: Vector2D): Double {
        val a = ac.x
        val b = bd.x
        val c = ac.y
        val d = bd.y

        return a * d - b * c
    }

    companion object {
        val regex = "^(-?\\d+), +(-?\\d+), +(-?\\d+) +@ +(-?\\d+), +(-?\\d+), +(-?\\d+)$".toRegex()

        fun of(it: String): Hailstone {
            val (px, py, pz, vx, vy, vz) = it.parseUsingRegex(regex)

            return Hailstone(
                Vector3D(px.toDouble(), py.toDouble(), pz.toDouble()),
                Vector3D(vx.toDouble(), vy.toDouble(), vz.toDouble())
            )
        }
    }
}

data class Line(val a: Double, val b: Double, val c: Double) {

}

data class Vector3D(val x: Double, val y: Double, val z: Double) {
    fun projectXY(): Vector2D {
        return Vector2D(x, y)
    }
}

data class Vector2D(
    val x: Double,
    val y: Double
) {
    operator fun minus(that: Vector2D): Vector2D {
        return Vector2D(this.x - that.x, this.y - that.y)
    }

    infix fun dotProduct(that: Vector2D): Double {
        return this.x * that.x + this.y * that.y
    }

    fun normalise(): Vector2D {
        val m = magnitude()
        return Vector2D(x / m, y / m)
    }

    fun magnitude(): Double = sqrt(x*x + y*y)
    operator fun plus(that: Vector2D): Vector2D = Vector2D(this.x + that.x, this.y + that.y)
    operator fun times(c: Double): Vector2D = Vector2D(c * x, c * y)
    fun rotate90Degrees() = Vector2D(-y, x)
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day24Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput, 7.0..27.0) shouldBe 2
    }

    @Test
    fun `part 1 with real input`() {
        // 32797 too high
        // 32690 too high

        part1(readInputFileToList("day24.txt"), (200000000000000.0..400000000000000.0)) shouldBe 31208
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
    fun `parallel lines`() {
        val a = Hailstone.of("18, 19, 22 @ -1, -1, -2")
        val b = Hailstone.of("20, 25, 34 @ -2, -2, -4")

        a.findIntersection(b) shouldBe null
    }

    @Test
    fun `lalala annoying one`() {
        val a = Hailstone.of("19, 13, 30 @ -2,  1, -2")
        val b = Hailstone.of("20, 19, 15 @  1, -5, -33")

        a.findIntersection(b) shouldBe null
    }

    @Test
    fun ston() {
        Hailstone.of("19, 13, 30 @ -2, 1, -2").findIntersection(
            Hailstone.of("18, 19, 22 @ -1, -1, -2")
        ) shouldBeAbout Vector2D(x=14.333, y=15.333)

        Hailstone.of("19, 13, 30 @ -2, 1, -2").findIntersection(
            Hailstone.of("20, 25, 34 @ -2, -2, -4")
        ) shouldBeAbout Vector2D(x=11.667, y=16.667)

        Hailstone.of("19, 13, 30 @ -2, 1, -2").findIntersection(
            Hailstone.of("12, 31, 28 @ -1, -2, -1")
        ) shouldBeAbout Vector2D(x=6.2, y=19.4)

        Hailstone.of("19, 13, 30 @ -2, 1, -2").findIntersection(
            Hailstone.of("20, 19, 15 @ 1, -5, -3")
        ) shouldBe null

        Hailstone.of("18, 19, 22 @ -1, -1, -2").findIntersection(
            Hailstone.of("20, 25, 34 @ -2, -2, -4")
        ) shouldBe null

        Hailstone.of("18, 19, 22 @ -1, -1, -2").findIntersection(
            Hailstone.of("12, 31, 28 @ -1, -2, -1")
        ) shouldBeAbout Vector2D(x=-6.0, y=-5.0)

        Hailstone.of("18, 19, 22 @ -1, -1, -2").findIntersection(
            Hailstone.of("20, 19, 15 @ 1, -5, -3")
        ) shouldBe null

        Hailstone.of("20, 25, 34 @ -2, -2, -4").findIntersection(
            Hailstone.of("12, 31, 28 @ -1, -2, -1")
        ) shouldBeAbout Vector2D(x=-2.0, y=3.0)

        Hailstone.of("18, 19, 22 @ -1, -1, -2").findIntersection(
            Hailstone.of("20, 19, 15 @ 1, -5, -3")
        ) shouldBe null

        Hailstone.of("20, 25, 34 @ -2, -2, -1").findIntersection(
            Hailstone.of("20, 19, 15 @ 1, -5, -3")
        ) shouldBe null
    }
}

private infix fun Vector2D?.shouldBeAbout(that: Vector2D) {
    this shouldNotBe null
    this!!
    this.x shouldBe (that.x plusOrMinus 0.001)
    this.y shouldBe (that.y plusOrMinus 0.001)
}

val testInput =
    """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent().lines()
