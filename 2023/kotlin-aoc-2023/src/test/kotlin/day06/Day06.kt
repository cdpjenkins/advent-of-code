package day06

import FileUtil.readInputFileToList
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun race(buttonTime: Long, totalTime: Long): Long {
    return buttonTime * (totalTime - buttonTime)
}

fun findRootsDouble(time: Double, distance: Double): Pair<Double, Double> {
    // Quadratic formula
    // If ax^2 + bx + c == 0
    // x == (b +- sqrt(b^2 -4ac)) / 2a

    val root1 = ((time) - sqrt(time * time - 4 * distance)) / 2
    val root2 = ((time) + sqrt(time * time - 4 * distance)) / 2

    println("root1: ${root1}")
    println("root2: ${root2}")



    return Pair(root1, root2)
}

fun bruteForceIt(totalTime: Long, distance: Long): Long {
    val num = (0..totalTime).map { race(it, totalTime) }.filter { it > distance }.count().toLong()
    return num
}

private fun numWaysYouCouldBeatRecord(time: Double, distance: Double): Int {
    val (root1, root2) = findRootsDouble(time, distance)

    val begin = if (ceil(root1) == floor(root1)) {
        ceil(root1 + 1)
    } else {
        ceil(root1)
    }.toInt()

    val end = ceil(root2).toInt()

    println("begin: ${begin}")
    println("end:   ${end}")

    val rangeMeDo = end - begin
    return rangeMeDo
}

class Day06Test {

    @Test
    fun `part 1 with test input`() {
        val timeLine = testInput[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
        val distanceLine = testInput[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }

        println(timeLine)
        println(distanceLine)

        val games = (timeLine zip distanceLine)

        println(games)

        val results = games.map { (time, distance) -> numWaysYouCouldBeatRecord(time.toDouble(), distance.toDouble()) }
        println(results)

        val tharResult = results.reduce { acc, i -> i * acc }

        println(tharResult)

        tharResult shouldBe 288
    }



    @Test
    fun `part 1 with real input`() {
        val input = readInputFileToList("day06.txt")


        val timeLine = input[0].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
        val distanceLine = input[1].split(":")[1].trim().split("\\s+".toRegex()).map { it.toLong() }

        println(timeLine)
        println(distanceLine)

        val games = (timeLine zip distanceLine)

        println(games)

        val results = games.map { (time, distance) -> bruteForceIt(time, distance) }
        println(results)

        val tharResult = results.reduce { acc, i -> i * acc }

        println(tharResult)

        tharResult shouldBe 227850
    }

    @Test
    fun `part 2 with test input`() {
        val timeLine = testInput[0].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()
        val distanceLine = testInput[1].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()


        val time = timeLine
        val distance = distanceLine

        val result = bruteForceIt(time, distance)
        println(result)


        result shouldBe 71503
    }

    @Test
    fun `part 2 with real input`() {
        val input = readInputFileToList("day06.txt")

        val timeLine = input[0].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()
        val distanceLine = input[1].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()


        val time = timeLine
        val distance = distanceLine

        val result = numWaysYouCouldBeatRecord(time.toDouble(), distance.toDouble())
        println(result)


        result shouldBe -1

        fail("arghghgh")
    }


    @Test
    fun `analytic the races`() {
        numWaysYouCouldBeatRecord(7.0, 9.0) shouldBe 4
        numWaysYouCouldBeatRecord(15.0, 40.0) shouldBe 8
        numWaysYouCouldBeatRecord(30.0, 200.0) shouldBe 9
    }

    @Test
    fun `try brute force race 1`() {
        val totalTime = 7L

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
    fun `try brute force race 2`() {
        val totalTime = 15L
        val distance = 40L

        for (buttonTime in (0..totalTime)) {
            println("${buttonTime}\t${race(buttonTime, totalTime)}")
        }

        val num = bruteForceIt(totalTime, distance)

        num shouldBe 8
    }



    @Test
    fun `analytic race 2`() {
        val totalTime = 15L
        val distance = 40L

        val num = numWaysYouCouldBeatRecord(totalTime.toDouble(), distance.toDouble())

        num shouldBe 8
    }


    @Test
    fun `try brute force race 3`() {
        val totalTime = 30L
        val distance = 200L

        for (buttonTime in (0..totalTime)) {
            println("${buttonTime}\t${race(buttonTime, totalTime)}")
        }

        val num = bruteForceIt(totalTime, distance)

        num shouldBe 9
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
