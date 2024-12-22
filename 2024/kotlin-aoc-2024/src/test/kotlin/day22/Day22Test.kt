package day22

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Long {
    return input
        .map { it.toInt() }
        .sumOf { find2000thSecretMeDo(it).toLong() }
}

fun find2000thSecretMeDo(secret: Int): Int {
    return secretSequence(secret)
        .drop(2000)
        .first()
}

fun secretSequence(initialSecret: Int) = generateSequence(initialSecret) { nextSecretNumber(it) }

fun nextSecretNumber(secret: Int): Int {
    val step1 = (secret.shl(6) xor secret) and 0xFFFFFF
    val step2 = (step1.shr(5) xor step1) and 0xFFFFFF
    val step3 = (step2.shl(11) xor step2) and 0xFFFFFF

    return step3
}


private fun part2(input: List<String>): Int {
    return 123
}

class Day22Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 37327623
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day22.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }

    @Test
    fun `secret sequence from 123`() {
        secretSequence(123)
            .drop(1)
            .take(10)
            .toList() shouldBe
                listOf(
                    15887950,
                    16495136,
                    527345,
                    704524,
                    1553684,
                    12683156,
                    11100544,
                    12249484,
                    7753432,
                    5908254
                )
    }

}

val testInput =
    """
        1
        10
        100
        2024
    """.trimIndent().lines()
