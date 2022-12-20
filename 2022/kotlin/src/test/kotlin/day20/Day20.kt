package day20

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day20 {
    @Test
    internal fun `should mix list part 1 test input`() {
        val input = testInput

        val mixedList =
            input.parse()
                .mix()

        mixedList.map { it.number } shouldBe listOf(1, 2, -3, 4, 0, 3, -2)
    }

    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val result =
            input.parse()
            .   mix()
                .grooveCoordinates()

        result shouldBe 3
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val numbers = input.parse()

        val mixedListStour = numbers.mix()

        mixedListStour.grooveCoordinates() shouldBe 2622
    }

    @Test
    internal fun `part 2 test input`() {
        val input = testInput

        val numbers = input.parse()
            .map { it.times(811589153) }

        val ston = generateSequence(numbers) { it.mix() }.drop(10).first()

        val result =
            ston.grooveCoordinates()

        result shouldBe 1623178306
    }

    @Test
    internal fun `part 2 real input`() {
        val input = realInput

        val numbers = input.parse()
            .map { it.times(811589153) }

        val ston = generateSequence(numbers) { it.mix() }.drop(10).first()

        val result =
            ston.grooveCoordinates()

        result shouldBe 1538773034088
    }
}

private fun List<String>.parse() =
    mapIndexed { i, s ->
        IndexedNumber(i, s.toLong())
    }

private fun List<IndexedNumber>.grooveCoordinates(): Long {
    val indexOf0 = this.indexOfFirst { it.number == 0L }

    val n1000 = this[(indexOf0 + 1000) % this.size].number
    val n2000 = this[(indexOf0 + 2000) % this.size].number
    val n3000 = this[(indexOf0 + 3000) % this.size].number

    println(n1000)
    println(n2000)
    println(n3000)

    return n1000 + n2000 + n3000
}

private fun List<IndexedNumber>.mix(): List<IndexedNumber> {
    val thisList = this.toMutableList()

    (0..this.size-1).forEach { i ->
        thisList.mix(i)
    }

    return thisList.toList()
}

private fun MutableList<IndexedNumber>.mix(i: Int) {
    val elementIndex = this.indexOfFirst { it.index == i }

    val removedElement = this.removeAt(elementIndex)
    val newIndexThang = elementIndex + removedElement.number

    var insertAtIndex = newIndexThang.mod(this.size)
    if (insertAtIndex == 0) insertAtIndex += this.size // because why???
    this.add(insertAtIndex, removedElement)
}

data class IndexedNumber(val index: Int, val number: Long) {
    fun times(multiplicand: Long): IndexedNumber {
        return this.copy(number = number * multiplicand)
    }
}

val testInput =
    """
        1
        2
        -3
        3
        -2
        0
        4
    """.trimIndent().lines()

val realInput = readInputFileToList("day20.txt")
