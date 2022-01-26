package day09

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput

class Day09Test : FunSpec({

    test("day 9 part 1 with test data") {
        part1(testInput, windowSize = 5) shouldBe 127
    }

    test("day 9 part 1 with real data") {
        part1(realInput, windowSize = 25) shouldBe 133015568
    }
    
    test("day 9 part 2 with test data") {
        part2(testInput, windowSize = 5) shouldBe 62
    }

    test("day 9 part 2 with real data") {
        part2(realInput, windowSize = 25) shouldBe 16107959
    }
})

private fun part2(input: List<String>, windowSize: Int): Long {
    val ints = input.map { it.toLong() }
    val invalidNumber = findInvalidNumber(ints, windowSize)

    return (0..ints.size - 1).flatMap { a: Int ->
        (0..ints.size - 1).map { b: Int ->
            Pair(a, b)
        }
    }
        .find { (a, b) -> a != b && ints.slice(a..b).sum() == invalidNumber }!!
        .let { (a, b) ->
            ints.slice(a..b).let { it.minOrNull()!! + it.maxOrNull()!! }
        }
}

private fun findInvalidNumber(numbers: List<Long>, windowSize: Int): Long {
    val (invalidNumber, _) =
        numbers.withIndex()
            .filter { (i, _) -> i > windowSize }
            .map { (i, number) -> number to numbers.slice(i - windowSize..i - 1) }
            .find { (number, previousNumbers) -> number !in sumsOf2Of(previousNumbers) }!!
    return invalidNumber
}

private fun part1(input: List<String>, windowSize: Int): Long {
    val ints = input.map { it.toLong() }
    return findInvalidNumber(ints, windowSize)
}

fun sumsOf2Of(previousNumbers: List<Long>) =
    previousNumbers.flatMap { a ->
        previousNumbers.map { b ->
            if (a != b) a + b
            else null
        }
    }.filterNotNull()

val testInput =
    """
        35
        20
        15
        25
        47
        40
        62
        55
        65
        95
        102
        117
        150
        182
        127
        219
        299
        277
        309
        576
    """.trimIndent().lines()

val realInput = realInput("day09")
