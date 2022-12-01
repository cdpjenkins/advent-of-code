import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day01 {
    @Test
    internal fun `part 1 sample input`() {
        testInput.caloriesForEachElf().max() shouldBe 24000
    }

    @Test
    internal fun `part 1 real input`() {
        readInputFileToList("day01.txt").caloriesForEachElf().max() shouldBe 67633
    }

    @Test
    internal fun `part 2 sample input`() {
        sumOfTopThreeCalories(testInput) shouldBe 45000
    }

    @Test
    internal fun `part 2 real input`() {
        sumOfTopThreeCalories(readInputFileToList("day01.txt")) shouldBe 199628
    }
}

private fun sumOfTopThreeCalories(input: List<String>) = input.caloriesForEachElf().sorted().reversed().take(3).sum()

private fun List<String>.caloriesForEachElf(): List<Int> {
    val partitions = splitByBlank(this)

    val calorieses = partitions.map {
        parseElf(it)
    }
    return calorieses
}

private fun parseElf(it: List<String>): Int =
    it.map(Integer::parseInt)
        .sum()

fun splitByBlank(input: List<String>): List<List<String>> =
    if (input.isEmpty()) {
        listOf()
    } else {
        val restInput = input.dropWhile { !it.isEmpty() }.drop(1)
        listOf(input.takeWhile { !it.isEmpty() }) + splitByBlank(restInput)
    }

val testInput =
    """
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent().lines()