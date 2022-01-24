package day15

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day15Test : FunSpec({
    test("part 1 with test data") {
        generateSeq("0,3,6").firstN(10) shouldBe listOf(0, 3, 6, 0, 3, 3, 1, 0, 4, 0)
    }

    test("part 1 with moar examples") {
        generateSeq("1,3,2").nth(2020) shouldBe 1
        generateSeq("2,1,3").nth(2020) shouldBe 10
        generateSeq("1,2,3").nth(2020) shouldBe 27
        generateSeq("2,3,1").nth(2020) shouldBe 78
        generateSeq("3,2,1").nth(2020) shouldBe 438
        generateSeq("3,1,2").nth(2020) shouldBe 1836
    }

    test("part 1 with real data") {
        generateSeq("0,6,1,7,2,19,20").nth(2020) shouldBe 706
    }

    xtest("disabled (takes too long to run) - part 2 with moar examples") {
        generateSeq("0,3,6").nth(30000000) shouldBe 175594
        generateSeq("1,3,2").nth(30000000) shouldBe 2578
        generateSeq("2,1,3").nth(30000000) shouldBe 3544142
        generateSeq("1,2,3").nth(30000000) shouldBe 261214
        generateSeq("2,3,1").nth(30000000) shouldBe 6895259
        generateSeq("3,2,1").nth(30000000) shouldBe 18
        generateSeq("3,1,2").nth(30000000) shouldBe 362
    }


    xtest("disabled (takes too long to run) - part 2 with real data") {
        generateSeq("0,6,1,7,2,19,20").nth(30000000) shouldBe 19331
    }
})

private fun <T> Sequence<T>.nth(n: Int) = this.drop(n-1).first()
private fun Sequence<Int>.firstN(n: Int) = take(n).toList()

private fun generateSeq(input: String): Sequence<Int> {
    val numbers = input.split(",").map { it.toInt() }.toMutableList()

    val latestOccurences = mutableMapOf<Int, Int>()
    numbers.dropLast(1)
        .withIndex()
        .forEach { (i, n) -> latestOccurences[n] = i }

    return sequence {
        numbers
            .dropLast(1)
            .withIndex()
            .forEach { (i, n) ->
                latestOccurences[n] = i
                this.yield(n)
            }
        this.yield(numbers.last())

        var i = numbers.size
        var lastNumber = numbers[i - 1]
        while (true) {
            val nextNumber = latestOccurences[lastNumber]?.let { i - it - 1 } ?: 0
            latestOccurences[lastNumber] = i - 1
            this.yield(nextNumber)
            lastNumber = nextNumber
            i++
        }
    }
}
