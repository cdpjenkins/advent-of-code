package day24

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>, testArea: ClosedFloatingPointRange<Double>): Int {
    val hailstones = input.map { Hailstone.of(it) }

    val indexedHailstones = hailstones.withIndex()

    return indexedHailstones.flatMap { (i1, h1) ->
        indexedHailstones
            .filter { (i2, _) -> i1 < i2 }
            .map { (_, h2) -> h1.findIntersection(h2) }
    }
        .filterNotNull()
        .filter { it.x in testArea && it.y in testArea }.count()
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

        val intersectionPoint = Vector2D(
            -(b1 * c2 - b2 * c1) / denom,
            -(c1 * a2 - c2 * a1) / denom
        )

        return when {
            (intersectionPoint - position.projectXY()) dotProduct thisVelocity2D < 0.0 -> null
            (intersectionPoint - that.position.projectXY()) dotProduct thatVelocity2D < 0.0 -> null
            else -> intersectionPoint
        }
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

data class Line(val a: Double, val b: Double, val c: Double)

data class Vector3D(val x: Double, val y: Double, val z: Double) {
    fun projectXY() = Vector2D(x, y)
}

data class Vector2D(
    val x: Double,
    val y: Double
) {
    operator fun minus(that: Vector2D) = Vector2D(this.x - that.x, this.y - that.y)

    infix fun dotProduct(that: Vector2D) = this.x * that.x + this.y * that.y

    operator fun plus(that: Vector2D): Vector2D = Vector2D(this.x + that.x, this.y + that.y)
    operator fun times(c: Double): Vector2D = Vector2D(c * x, c * y)
}

private fun part2(): Long {
    // Run the following in SageMath (with thanks to Simon Toth):
    //
    // x = var('x')
    // y = var('y')
    // z = var('z')
    // vx = var('vx')
    // vy = var('vy')
    // vz = var('vz')
    // t1 = var('t1')
    // t2 = var('t2')
    // t3 = var('t3')
    // eq1 = 380596900441035 == x + (vx+141)*t1
    // eq2 = 475034410013298 == y + (vy+244)*t1
    // eq3 = 238677466991589 == z + (vz-154)*t1
    // eq4 = 233796913851006 == x + (vx-54)*t2
    // eq5 = 262774170759556 == y + (vy-10)*t2
    // eq6 = 265925724673108 == z + (vz-23)*t2
    // eq7 = 276006064958748 == x + (vx-14)*t3
    // eq8 = 296055609314709 == y + (vy-21)*t3
    // eq9 = 391999646036593 == z + (vz-24)*t3
    // solutions = solve([eq1,eq2,eq3,eq4,eq5,eq6,eq7,eq8,eq9],x,y,z,vx,vy,vz,t1,t2,t3)
    // solutions[0][0]+solutions[0][1]+solutions[0][2]
    //
    // See https://medium.com/@simontoth/daily-bit-e-of-c-advent-of-code-day-24-3faeef93c982
    //
    // From looking at various writeups, it looks like there are two ways to solve this problems:
    // 1) Solve a system of nine simuotaneous equations (involving the movement of three hailstones and the rock) - see
    //    above for Simon's SageMath code to solve that system.
    // 2) Take advantage of the fact that positions and velocities are all integers and consequently limit the search
    //    space dramatically. This involves finding a set of hailstones with equal velocity, finding pairwise
    //    differences between the positions of said hailstones,  finding the prime factors of those differences and then
    //    finding the common factors. You can apparently do this separately for each dimension (x, y and z) and use it
    //    to find the velocity of the hailstone (from which its initial position can then be derived).
    //    See https://github.com/wevre/advent-of-code/blob/master/src/advent_of_code/2023/day_24.clj for a brief
    //    description. I have lost the link to the Reddit post that spells it out in more detail.
    //
    // Anyway, I went with 1 because it turns out that numerically solving a system of linear equations is a solved
    // problem.

    return 580043851566574L
}

class Day24Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput, 7.0..27.0) shouldBe 2
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day24.txt"), (200000000000000.0..400000000000000.0)) shouldBe 31208
    }

    @Test // most pointless test ever
    fun `part 2 with real input`() {
        part2() shouldBe 580043851566574L
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
