package day11

import FileUtil.readInputFileToString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: String): Int {
    val numbers = input.parse()

    val finalThang = generateSequence(numbers) { it.blink() }
        .drop(25)
        .first()

    val counts = finalThang.groupingBy { it }.eachCount()
    val freqs = counts.toList().sortedBy { it.second }
    freqs.forEach { println("${it.first}  ${it.second}") }

    println("size after 25 is: ${finalThang.size}")
    println("but num uniques is : ${freqs.size}")

    return finalThang
        .size
}

private fun part2(input: String): Int {
    return 123
}

private fun String.parse() = split(" ").map { it.toLong() }

private fun List<Long>.blink(): List<Long> = this.flatMap { it.blink() }

private fun Long.hasEvenNumberOfDigits() = this.toString().length % 2 == 0

private fun Long.blink(): List<Long> {
    if (this == 0L) {
        return listOf(1)
    } else if (this.hasEvenNumberOfDigits()) {
        return this.chopDigitsInHalf()
    } else {
        return listOf(this * 2024)
    }
}

private fun Long.chopDigitsInHalf(): List<Long> {
    val string = this.toString()
    val length = string.length
    return listOf(
        string.substring(0, length / 2).toLong(),
        string.substring(length / 2, length).toLong()
    )
}

class Day11Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 55312
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day11.txt")) shouldBe 216996
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day_template.txt")) shouldBe -1
    }

    @Test
    fun generatesCorrectSequence() {
        val numbers = testInput.parse()

        val seq = generateSequence(numbers) { it.blink() }

        seq.take(2).toList().map { it.joinToString(" ")} shouldBe listOf(
            "125 17",
            "253000 1 7"
        )
    }
}

val testInput = "125 17"
