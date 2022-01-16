package day06

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines
import utils.splitList

class Day06Test : FunSpec({
    test("day 6 part 1 with real data") {
        val sumOfQuestionCounts = realInput
            .splitList { it.isEmpty() }
            .map { it.parseGroup() }
            .map { it.size }
            .sum()

        sumOfQuestionCounts shouldBe 6735
    }

    test("day 6 part 2 with real data") {
        val thang = realInput
            .splitList { it.isEmpty() }
            .map { it.questionsThatEveroneAnswered() }
        thang.forEach { println(it) }
        val sumOfQuestionCounts = thang
            .map { it.size }
            .sum()

        sumOfQuestionCounts shouldBe 3221
    }
})

private fun List<String>.questionsThatEveroneAnswered(): Set<Char> {
    val sets = this.map { it.toSet() }
    return sets.fold(sets.first()) { acc, chars -> acc.intersect(chars) }
}

private fun <E> List<E>.parseGroup() = joinToString("").toSet()

val realInput = readLines("day06")
