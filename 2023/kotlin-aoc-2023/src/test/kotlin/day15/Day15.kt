package day15

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: String): Int {
    val steps = input.split(",")

    val result = steps.map { it.computeHash() }.sum()

    return result
}

private fun part2(input: String): Int {
    return 123
}

private fun String.computeHash(): Int {
    var currentValue = 0

    this.forEach { c ->
        val asciiCode = c.code
        currentValue += asciiCode
        currentValue *= 17
        currentValue %= 256
    }

    return currentValue
}

class Day15Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1320
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day15.txt")[0]) shouldBe -1
    }
//
//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }

    @Test
    fun `hash works`() {
        "HASH".computeHash() shouldBe 52
        "rn=1".computeHash() shouldBe 30
        "cm-".computeHash() shouldBe 253
        "qp=3".computeHash() shouldBe 97
//        "rn=1".computeHash() shouldBe 30
//        "rn=1".computeHash() shouldBe 30
//        "rn=1".computeHash() shouldBe 30
//        "rn=1".computeHash() shouldBe 30
//        "rn=1".computeHash() shouldBe 30
    }
}

val testInput = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
