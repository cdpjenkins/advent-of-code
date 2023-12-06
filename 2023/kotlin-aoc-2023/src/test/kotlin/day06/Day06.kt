package day06

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.math.sqrt
import kotlin.time.times

fun race(buttonTime: Int, totalTime: Int): Int {
    return buttonTime * (totalTime - buttonTime)
}

fun findRootsDouble(time: Double, distance: Double): Pair<Double, Double> {
    // Quadratic formula
    // If ax^2 + bx + c == 0
    // x == (b +- sqrt(b^2 -4ac)) / 2a

    val root1 = ((time) - sqrt(time * time - 4 * distance)) / 2
    val root2 = ((time) + sqrt(time * time - 4 * distance)) / 2

    return Pair(root1, root2)
}


class Day06Test {
    @Test
    fun `a few test races`() {
        val totalTime = 7

        race(0, totalTime) shouldBe 0
        race(1, totalTime) shouldBe 6
        race(2, totalTime) shouldBe 10
        race(3, totalTime) shouldBe 12
        race(4, totalTime) shouldBe 12
        race(5, totalTime) shouldBe 10
        race(6, totalTime) shouldBe 6
        race(7, totalTime) shouldBe 0
    }

    @Test
    fun `findRootsDouble actually does find roots`() {
        val (root1, root2) = findRootsDouble(7.0, 9.0)

        root1 shouldBe (1.6972243622680054 plusOrMinus 0.0001)
        root2 shouldBe (5.302775637731995 plusOrMinus 0.0001)
    }



//    @Test
//    fun `part 1 with test input`() {
//        part1(testInput) shouldBe 35
//    }
//
//    @Test
//    fun `part 1 with real input`() {
//        part1(readInputFileToList("day05.txt")) shouldBe 165788812L
//    }
//
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe 46
//    }
//
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day05.txt")) shouldBe 1928058L
//    }


    // there are probably loads more tests for overlapping ranges but meh, the logic looks good now
}


val testInput =
    """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().lines()
