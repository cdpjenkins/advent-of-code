package day09

import FileUtil.readInputFileToList
import Point2D
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day09 {
    @Test
    internal fun `part 1 test data`() {
        val input = testInput
        val instructions = input.flatMap { it.parseInstruction() }
        val initialRope = makeRope()
        val path = instructions.runningFold(initialRope) { rope: Rope, direction: Direction -> rope.execute(direction) }

        val visitedSet = path
            .map { it.knots.last() }
            .toSet()
        val positionsVisited = visitedSet
            .size

        positionsVisited shouldBe 13
    }

    @Test
    internal fun `part 1 real data`() {
        val input = realInput

        val instructions = input.flatMap { it.parseInstruction() }
        val initialRope = makeRope()
        val path = instructions.runningFold(initialRope) { rope: Rope, direction: Direction -> rope.execute(direction) }

        val visitedSet = path
            .map { it.knots.last() }
            .toSet()
        val positionsVisited = visitedSet
            .size

        positionsVisited shouldBe 6030
    }

    @Test
    internal fun `part 2 test data`() {
        // TODO - finish me!
        val input = part2TestInput
        val instructions = input.flatMap { it.parseInstruction() }
        val initialRope = makeRope(numKnots = 10)
        val path = instructions.runningFold(initialRope) { rope: Rope, direction: Direction -> rope.execute(direction) }

        val visitedSet = path
            .map { it.knots.last() }
            .toSet()
        val positionsVisited = visitedSet
            .size

        positionsVisited shouldBe 36
    }



    @Test
    internal fun `part 2 real data`() {
        // TODO - finish me!
        val input = realInput

        val instructions = input.flatMap { it.parseInstruction() }
        val initialRope = makeRope(numKnots = 10)
        val path = instructions.runningFold(initialRope) { rope: Rope, direction: Direction -> rope.execute(direction) }

        val visitedSet = path
            .map { it.knots.last() }
            .toSet()
        val positionsVisited = visitedSet
            .size

        positionsVisited shouldBe 2545
    }

    private fun dumpPath(visitedSet: Set<Point2D>) {
        (-30..30).forEach { y ->
            (-30..30).forEach { x ->
                if (visitedSet.contains(Point2D(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}

private fun makeRope(numKnots: Int = 2) = Rope(List(numKnots) { Point2D(0, 0) } )

private fun String.parseInstruction(): List<Direction> {
    val (directionString: String, distanceString) = this.parseUsingRegex("^(L|R|U|D) (\\d+)$")

    val num = distanceString.toInt()
    return List(num) { Direction.valueOf(directionString) }
}

data class Rope(
    val knots: List<Point2D>
) {
    fun execute(direction: Direction): Rope {
        val newHead = direction.execute(knots.first())

        val newKnots = mutableListOf(newHead)

        (1..knots.size - 1).forEach { i ->
            val knotWeAreFollowing = newKnots[i - 1]
            val newKnot = knots[i].follow(knotWeAreFollowing)
            newKnots.add(newKnot)
        }

        return Rope(newKnots.toList())
    }
}

private fun Point2D.follow(head: Point2D): Point2D {
    val dx = head.x - this.x
    val dy = head.y - this.y

    return when {
        dx > 1 -> {
            when {
                dy > 0 -> Point2D(this.x + 1, this.y + 1)
                dy < 0 -> Point2D(this.x + 1, this.y - 1)
                else -> Point2D(this.x + 1, this.y)
            }
        }
        dx < -1 -> {
            when {
                dy > 0 -> Point2D(this.x - 1, this.y + 1)
                dy < 0 -> Point2D(this.x - 1, this.y - 1)
                else -> Point2D(this.x - 1, this.y)
            }
        }
        else -> {
            when {
                dy > 1 ->
                    when {
                        dx < 0 -> Point2D(this.x - 1, this.y + 1)
                        dx > 0 -> Point2D(this.x + 1, this.y + 1)
                        else -> Point2D(this.x, this.y + 1)
                    }
                dy < -1 ->
                    when {
                        dx < 0 -> Point2D(this.x - 1, this.y - 1)
                        dx > 0 -> Point2D(this.x + 1, this.y - 1)
                        else -> Point2D(this.x, this.y - 1)
                    }
                else -> this
            }
        }
    }
}

enum class Direction {
    U {
        override fun execute(p: Point2D) = p.copy(y = p.y - 1)
    },
    D {
        override fun execute(p: Point2D) = p.copy(y = p.y + 1)
    },
    L {
        override fun execute(p: Point2D) = p.copy(x = p.x - 1)
    },
    R {
        override fun execute(p: Point2D) = p.copy(x = p.x + 1)
    };

    abstract fun execute(p: Point2D): Point2D
}

val testInput =
    """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent().lines()

val part2TestInput =
    """
        R 5
        U 8
        L 8
        D 3
        R 17
        D 10
        L 25
        U 20
    """.trimIndent().lines()

val realInput = readInputFileToList("day09.txt")