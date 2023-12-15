package day15

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: String): Int {
    val steps = input.split(",")

    val result = steps.map { it.computeHash() }.sum()

    return result
}

private fun part2(input: String): Long {
    val steps = input.split(",").map { Step.of(it) }

    val boxes = (0 until 256).map { i -> Box(i) }

    steps.forEach { it.execute(boxes) }

    return boxes
        .map { box -> box.focussingPower() }
        .sum()
}

data class Lens(
    val label: String,
    val focalLength: Int
)

data class Box(
    val indexOfBox: Int,
    val lenses: MutableList<Lens> = mutableListOf()
) {
    fun remove(label: String) {
        lenses.removeIf { it.label == label}
    }

    fun replaceOrAdd(label: String, focalLength: Int) {
        val newLens = Lens(label, focalLength)
        val index = lenses.indexOfFirst { it.label == label }

        if (index == -1) {
            lenses.add(newLens)
        } else {
            lenses[index] = newLens
        }
    }

    fun focussingPower(): Long =
        lenses.withIndex()
            .map { (lensIndex, lens) ->
                (indexOfBox + 1) * (lensIndex + 1) * lens.focalLength.toLong()
            }
            .sum()
}

// could be a sealed class hierarchy...
data class Step(
    val label: String,
    val operation: Char,
    val focalLength: Int
) {
    val boxNumber = label.computeHash()

    fun execute(boxes: List<Box>) {
        when (operation) {
            '-' -> {
                boxes[boxNumber].remove(label)
            }
            '=' -> {
                boxes[boxNumber].replaceOrAdd(label, focalLength)
            }
            else -> throw IllegalStateException(operation.toString())
        }
    }

    companion object {
        val regex = "([a-z]+)([\\-=])(\\d*)".toRegex()

        fun of(input: String): Step {
            val (label, operation, focalLengthString) = input.parseUsingRegex(regex)

            return Step(
                label,
                operation[0],
                if (focalLengthString.isEmpty()) 0 else focalLengthString.toInt()
            )
        }
    }
}

private fun String.computeHash() = this.fold(0) { currentValue, c -> ((currentValue + c.code) * 17) % 256 }

class Day15Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1320
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day15.txt")[0]) shouldBe 509784
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 145
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day15.txt")[0]) shouldBe 230197
    }

    @Test
    fun `hash works`() {
        "HASH".computeHash() shouldBe 52
        "rn=1".computeHash() shouldBe 30
        "cm-".computeHash() shouldBe 253
        "qp=3".computeHash() shouldBe 97
    }
}

val testInput = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
