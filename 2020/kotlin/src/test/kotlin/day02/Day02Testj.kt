package day02

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput

class Day02Test : FunSpec({
    test("day 2 part 1 with test input") {
        countPasswordsValidByFrequency(testInput) shouldBe 2
    }

    test("day 2 part 1 with real input") {
        countPasswordsValidByFrequency(realInput) shouldBe 655
    }

    test("day 2 part 2 with test input") {
        countPasswordsValidByPosition(testInput) shouldBe 1
    }

    test("day 2 part 2 with real input") {
        countPasswordsValidByPosition(realInput) shouldBe 673
    }
})

fun countPasswordsValidByFrequency(input: List<String>): Int {
    val count = input.map { it.parse() }
        .filter { it.isValidByFrequencies() }
        .count()
    return count
}

fun countPasswordsValidByPosition(input: List<String>) =
    input.map { it.parse() }
        .filter { it.isValidByPositions() }
        .count()

private fun String.parse(): PasswordRule {
    val (min, max, character, password) = regex.find(this)!!.destructured
    val passwordRule = PasswordRule(min.toInt(), max.toInt(), character.first(), password)
    return passwordRule
}

data class PasswordRule(val min: Int, val max: Int, val character: Char, val password: String) {
    fun isValidByFrequencies() = password.occurenciesOf(character) in min..max
    fun isValidByPositions() = (password[min - 1] == character) xor (password[max - 1] == character)

    private fun String.occurenciesOf(c: Char) = frequencies()[c]
    private fun String.frequencies() =
        this.groupingBy { it }
            .eachCount()
}

val regex = "([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)".toRegex()

val testInput = """
            1-3 a: abcde
            1-3 b: cdefg
            2-9 c: ccccccccc
        """.trimIndent().lines()

val realInput = realInput("day02")
