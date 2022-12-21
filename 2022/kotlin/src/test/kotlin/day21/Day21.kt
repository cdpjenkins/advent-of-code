package day21

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day21 {
    @Test
    internal fun `part 1 test input`() {
        val input = realInput

        val nodes = input.map { MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        val result = monkeyMap["root"]!!.evaluate(monkeyMap)

        result shouldBe 152
    }


    @Test
    internal fun `part 1 real input`() {
        val input = testInput

        val nodes = input.map { MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        val result = monkeyMap["root"]!!.evaluate(monkeyMap)

        result shouldBe 152
    }
}

sealed interface MonkeyNode {
    val monkeyId: String

    fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long

    companion object {
        fun parse(inputLine: String): MonkeyNode =
            listOf(NumberNode, PlusNode, MinusNode, MultiplyNode, DivideNode)
                .firstOrNull { it.matches(inputLine) }
                ?.parse(inputLine)
                ?: throw IllegalArgumentException(inputLine)
    }
}

interface MonkeyNodeCompanion {
    fun matches(inputLine: String): Boolean {
        return inputLine.matches(regex())
    }
    fun parse(inputLine: String): MonkeyNode
    fun regex(): Regex
}

data class NumberNode(
    override val monkeyId: String,
    val number: Long
) : MonkeyNode {
    companion object : MonkeyNodeCompanion {
        override fun parse(inputLine: String): MonkeyNode {
            val (monkeyId, numberString) = inputLine.parseUsingRegex(regex())
            return NumberNode(monkeyId, numberString.toLong())
        }

        override fun regex() = "^([a-z]{4}): (\\d+)$".toRegex()
    }

    override fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long {

//        println("${monkeyId} ${number}")

        return number
    }
}

data class PlusNode(
    override val monkeyId: String,
    val operandMonkey1: String,
    val operandMonkey2: String
) : MonkeyNode {

    override fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) +
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)

//        println("${monkeyId} ${result}")

        return result
    }

    companion object : MonkeyNodeCompanion {
        override fun parse(inputLine: String): MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return PlusNode(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) \\+ ([a-z]{4})$".toRegex()
    }
}

data class MinusNode(
    override val monkeyId: String,
    val operandMonkey1: String,
    val operandMonkey2: String
) : MonkeyNode {
    override fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) -
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)

//        println("${monkeyId} ${result}")

        return result
    }

    companion object : MonkeyNodeCompanion {
        override fun parse(inputLine: String): MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return MinusNode(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) - ([a-z]{4})$".toRegex()
    }
}

data class MultiplyNode(
    override val monkeyId: String,
    val operandMonkey1: String,
    val operandMonkey2: String
) : MonkeyNode {

    override fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) *
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)

//        println("${monkeyId} ${result}")

        return result
    }

    companion object : MonkeyNodeCompanion {
        override fun parse(inputLine: String): MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return MultiplyNode(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) \\* ([a-z]{4})$".toRegex()
    }
}

data class DivideNode(
    override val monkeyId: String,
    val operandMonkey1: String,
    val operandMonkey2: String
) : MonkeyNode {

    override fun evaluate(monkeyMap: Map<String, MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) /
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)

//        println("${monkeyId} ${result}")

        return result
    }

    companion object : MonkeyNodeCompanion {
        override fun parse(inputLine: String): MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return DivideNode(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) / ([a-z]{4})$".toRegex()
    }
}

val testInput =
    """
        root: pppw + sjmn
        dbpl: 5
        cczh: sllz + lgvd
        zczc: 2
        ptdq: humn - dvpt
        dvpt: 3
        lfqf: 4
        humn: 5
        ljgn: 2
        sjmn: drzm * dbpl
        sllz: 4
        pppw: cczh / lfqf
        lgvd: ljgn * ptdq
        drzm: hmdt - zczc
        hmdt: 32
    """.trimIndent().lines()

val realInput = readInputFileToList("day21.txt")
