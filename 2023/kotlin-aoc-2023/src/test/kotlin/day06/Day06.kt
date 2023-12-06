package day06

import FileUtil.readInputFileToList
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

private fun part1(input: List<String>): Int {
    val timeLine = input[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
    val distanceLine = input[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }

    val games = (timeLine zip distanceLine)
    val results = games.map { (time, distance) -> numWaysYouCouldBeatRecord(time, distance) }
    return results.reduce { acc, i -> i * acc }
}

private fun part2(input: List<String>): Int {
    val time = input[0].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()
    val distance = input[1].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()

    return numWaysYouCouldBeatRecord(time, distance)
}

fun race(buttonTime: Long, totalTime: Long): Long = buttonTime * (totalTime - buttonTime)

fun findRoots(time: Double, distance: Double): Pair<Double, Double> {
    // Quadratic formula
    // If ax^2 + bx + c == 0
    // x == (b +- sqrt(b^2 -4ac)) / 2a

    val root1 = ((time) - sqrt(time * time - 4 * distance)) / 2
    val root2 = ((time) + sqrt(time * time - 4 * distance)) / 2
    return Pair(root1, root2)
}

fun bruteForceIt(totalTime: Long, distance: Long): Long {
    val num = (0..totalTime).map { race(it, totalTime) }.filter { it > distance }.count().toLong()
    return num
}

private fun numWaysYouCouldBeatRecord(time: Long, distance: Long): Int {
    val (root1, root2) = findRoots(time.toDouble(), distance.toDouble())

    val begin = if (ceil(root1) == floor(root1)) {
        ceil(root1 + 1)
    } else {
        ceil(root1)
    }.toInt()

    val end = ceil(root2).toInt()

    return end - begin
}

class Day06Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 288
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day06.txt")) shouldBe 227850
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 71503
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day06.txt")) shouldBe 42948149
    }

    @Test
    fun `analytic all the races`() {
        numWaysYouCouldBeatRecord(7, 9) shouldBe 4
        numWaysYouCouldBeatRecord(15, 40) shouldBe 8
        numWaysYouCouldBeatRecord(30, 200) shouldBe 9
    }

    @Test
    fun `try brute force race 2`() {
        bruteForceIt(
            totalTime = 15L,
            distance = 40L
        ) shouldBe 8
    }

    @Test
    fun `analytic race 2`() {
        numWaysYouCouldBeatRecord(
            time = 15L,
            distance = 40L
        ) shouldBe 8
    }


    @Test
    fun `try brute force race 3`() {
        bruteForceIt(
            totalTime = 30L,
            distance = 200L
        ) shouldBe 9
    }

    @Test
    fun `findRoots actually does find roots`() {
        val (root1, root2) = findRoots(7.0, 9.0)

        root1 shouldBe (1.6972243622680054 plusOrMinus 0.0001)
        root2 shouldBe (5.302775637731995 plusOrMinus 0.0001)
    }
}


val testInput =
    """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().lines()
