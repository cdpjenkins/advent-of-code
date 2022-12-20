package day20

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day20 {

    @Test
    internal fun `should mix list part 1 test input`() {
        val input = testInput

        val numbers = input.map(String::toInt)

        val mixedListStour = numbers.mix()

        mixedListStour shouldBe listOf(1, 2, -3, 4, 0, 3, -2)
    }

    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val numbers = input.map(String::toInt)

        val mixedListStour = numbers.mix()

        mixedListStour.grooveCoordinates() shouldBe 3
    }



    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val numbers = input.map(String::toInt)

        val mixedListStour = numbers.mix()

        mixedListStour.grooveCoordinates() shouldBe 3
    }
}

private fun List<Int>.grooveCoordinates(): Int {
    val indexOf0 = this.indexOfFirst { it == 0 }

    val n1000 = this[(indexOf0 + 1000) % this.size]
    val n2000 = this[(indexOf0 + 2000) % this.size]
    val n3000 = this[(indexOf0 + 3000) % this.size]
    return n1000 + n2000 + n3000
}

private fun List<Int>.mix(): List<Int> {
    val thisList =
        this
            .mapIndexed { i, n -> IndexedNumber(i, n) }
            .toMutableList()

    (0..this.size-1).forEach { i ->
        thisList.mix(i)
    }

    val newListHere =
        thisList.mapIndexed() { i, number ->
            IndexedNumber(i, number.number)
        }

    return newListHere.map { it.number }
}

private fun MutableList<IndexedNumber>.mix(i: Int) {
    val elementIndex = this.indexOfFirst { it.index == i }

    val removedElement = this.removeAt(elementIndex)
    val newIndexThang = elementIndex + removedElement.number

    var insertAtIndex = newIndexThang.mod(this.size)
    if (insertAtIndex == 0) insertAtIndex += this.size // because why???
    this.add(insertAtIndex, removedElement)
}

data class IndexedNumber(val index: Int, val number: Int)

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
