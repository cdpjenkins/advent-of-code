package day01

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines

class Day01Kt : FunSpec({
    test("day 1 part 1 with test input") {
        productOfTwoValuesThatSumTo2020(testInput) shouldBe 514579
    }

    test("day 1 part 1 with real input") {
        productOfTwoValuesThatSumTo2020(realInput) shouldBe 970816
    }

    test("day 1 part 2 with test input") {
        productOfThreeValuesThatSumTo2020(testInput) shouldBe 241861950
    }

    test("day 1 part 2 with real input") {
        productOfThreeValuesThatSumTo2020(realInput) shouldBe 96047280
    }
})

fun productOfTwoValuesThatSumTo2020(input: List<String>): Long {
    val (num1, num2) = asLongs(input)
        .flatMap { num1 ->
            asLongs(input).map { num2 ->
                Pair(num1, num2)
            }
        }.find { (num1, num2) -> num1 + num2 == 2020L }!!

    return num1 * num2
}

fun productOfThreeValuesThatSumTo2020(input: List<String>): Long {
    val (num1, num2, num3) =
        asLongs(input).flatMap { num1 ->
            asLongs(input).flatMap { num2 ->
                asLongs(input).map { num3 ->
                    Triple(num1, num2, num3)
                }
            }
        }.find { (num1, num2, num3) -> num1 + num2 + num3 == 2020L }!!

    return num1 * num2 * num3
}

private fun asLongs(input: List<String>) =
    input.map { it.toLong() }
        .asSequence()


val realInput = readLines("day01")
val testInput = """
            1721
            979
            366
            299
            675
            1456
        """.trimIndent().lines()
