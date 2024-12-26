package day22

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day22Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 37327623
    }

    @Ignore
    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day22.txt")) shouldBe -1
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInputForPart2) shouldBe 23
    }

    // This one is pretty slow. Even after optimising, it's still taking nearly 1.3 seconds.
    // Candidate for @Ignore...
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day22.txt")) shouldBe 1600
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

    @Test
    fun `price change sequence from 123`() {
        val priceList = priceSequence(123).take(10).toList()
        priceList shouldBe listOf(
            3,
            0,
            6,
            5,
            4,
            4,
            6,
            4,
            4,
            2,
        )

        priceList.differences() shouldBe
                listOf(
                    -3,
                    6,
                    -1,
                    -1,
                    0,
                    2,
                    -2,
                    0,
                    -2
                )
    }

    @Test
    fun `can find difference sequence in a longer list me do`() {
        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).findIndexOf(listOf(3, 4, 5, 6)) shouldBe 6
    }

    @Test
    fun `returns null if the sequence does not exist in the list`() {
        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).findIndexOf(listOf(10, 11, 12, 13)) shouldBe null
    }
}

val testInput =
    """
        1
        10
        100
        2024
    """.trimIndent().lines()

val testInputForPart2 =
    """
        1
        2
        3
        2024
    """.trimIndent().lines()



