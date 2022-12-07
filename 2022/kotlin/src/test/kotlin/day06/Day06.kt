package day06

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day06 {
    @Test
    internal fun `part 1 with test data`() {
        findStartOfPacketMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb") shouldBe 7
    }

    @Test
    internal fun `part 1 with read data`() {
        findStartOfPacketMarker(realInput) shouldBe 1542
    }

    @Test
    internal fun `part 2 with test data`() {
        findStartOfMessageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb") shouldBe 19
    }

    @Test
    internal fun `part 2 with real data`() {
        findStartOfMessageMarker(realInput) shouldBe 3153
    }

    private fun findStartOfPacketMarker(testInput: String) = findFirstUniqueSubSequence(testInput, 4)
    private fun findStartOfMessageMarker(testInput: String) = findFirstUniqueSubSequence(testInput, 14)

    private fun findFirstUniqueSubSequence(input: String, windowSize: Int): Int {
        val index = input
            .windowed(windowSize)
            .withIndex()
            .map { (i, w) -> Pair(i, w.toSet())
            }
            .find { (_, w) -> w.size == windowSize }
            ?.first
            ?: throw IllegalArgumentException(input)
        return index + windowSize
    }
}

val realInput = readInputFileToList("day06.txt")[0]
