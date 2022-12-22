package day21

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import StringUtils.indentBy
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day21Part1 {
    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val nodes = input.map { Part1MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        val result = monkeyMap["root"]!!.evaluate(monkeyMap)

        result shouldBe 152
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val nodes = input.map { Part1MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        val result = monkeyMap["root"]!!.evaluate(monkeyMap)

        result shouldBe 70674280581468
    }

    @Test
    internal fun `part 2 test input`() {
        val input = testInput

        val nodes = input.map { Part1MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        monkeyMap["root"]!!.dumpToStdout(monkeyMap)
    }

    @Test
    internal fun `part 2 real input`() {
        val input = realInput

        val nodes = input.map { Part1MonkeyNode.parse(it) }

        val monkeyMap = nodes.map { it.monkeyId to it }.toMap()

        monkeyMap["root"]!!.dumpToStdout(monkeyMap)
    }
}

sealed interface Part1MonkeyNode {
    val monkeyId: String

    fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long

    fun dumpToStdout(monkeys: Map<String, Part1MonkeyNode>, indent: Int = 0) {
        println("${indentBy(indent)}${this}")
        children(monkeys).forEach {
            it.dumpToStdout(monkeys, indent+1)
        }
    }

    fun children(monkeys: Map<String, Part1MonkeyNode>): List<Part1MonkeyNode>

    fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode

    companion object {
        fun parse(inputLine: String): Part1MonkeyNode =
            listOf(RootNodePart1, NumberNodePart1, PlusNodePart1, MinusNodePart1, MultiplyNodePart1, DivideNodePart1)
                .firstOrNull { it.matches(inputLine) }
                ?.parse(inputLine)
                ?: throw IllegalArgumentException(inputLine)
    }
}

interface Part1MonkeyNodeCompanion {
    fun matches(inputLine: String): Boolean {
        return inputLine.matches(regex())
    }
    fun parse(inputLine: String): Part1MonkeyNode
    fun regex(): Regex
}

interface BinaryNodePart1 : Part1MonkeyNode {
    override val monkeyId: String
    val operandMonkey1: String
    val operandMonkey2: String

    override fun children(monkeys: Map<String, Part1MonkeyNode>): List<Part1MonkeyNode> {
        return listOf(
            monkeys[operandMonkey1]!!,
            monkeys[operandMonkey2]!!
        )
    }
}

data class NumberNodePart1(
    override val monkeyId: String,
    val number: Long
) : Part1MonkeyNode {
    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (monkeyId, numberString) = inputLine.parseUsingRegex(regex())
            return NumberNodePart1(monkeyId, numberString.toLong())
        }

        override fun regex() = "^([a-z]{4}): (\\d+)$".toRegex()
    }

    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        return number
    }

    override fun children(monkeys: Map<String, Part1MonkeyNode>): List<Part1MonkeyNode> {
        return listOf()
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        if (monkeyId == "humn") {
            return HumnNode(monkeyId)
        } else {
            return NumberNode(monkeyId, number)
        }
    }
}

data class RootNodePart1(
    override val monkeyId: String,
    override val operandMonkey1: String,
    override val operandMonkey2: String
) : BinaryNodePart1 {
    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        return monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) +
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        return RootNode(
            monkeyId,
            part1Map[operandMonkey1]!!.toMonkeyNode(part1Map),
            part1Map[operandMonkey2]!!.toMonkeyNode(part1Map)
        )

    }

    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (operand1, operand2) = inputLine.parseUsingRegex(regex())
            return RootNodePart1("root", operand1, operand2)
        }

        override fun regex() = "^root: ([a-z]{4}) \\+ ([a-z]{4})$".toRegex()
    }
}

data class PlusNodePart1(
    override val monkeyId: String,
    override val operandMonkey1: String,
    override val operandMonkey2: String
) : BinaryNodePart1 {

    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) +
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)

        return result
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        return PlusNode(
            monkeyId,
            part1Map[operandMonkey1]!!.toMonkeyNode(part1Map),
            part1Map[operandMonkey2]!!.toMonkeyNode(part1Map)
        )
    }

    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return PlusNodePart1(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) \\+ ([a-z]{4})$".toRegex()
    }
}

data class MinusNodePart1(
    override val monkeyId: String,
    override val operandMonkey1: String,
    override val operandMonkey2: String
) : BinaryNodePart1 {
    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) -
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)
        return result
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        return MinusNode(
            monkeyId,
            part1Map[operandMonkey1]!!.toMonkeyNode(part1Map),
            part1Map[operandMonkey2]!!.toMonkeyNode(part1Map)
        )
    }

    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return MinusNodePart1(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) - ([a-z]{4})$".toRegex()
    }
}

data class MultiplyNodePart1(
    override val monkeyId: String,
    override val operandMonkey1: String,
    override val operandMonkey2: String
) : BinaryNodePart1 {
    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        val result = monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) *
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)
        return result
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        return MultiplyNode(
            monkeyId,
            part1Map[operandMonkey1]!!.toMonkeyNode(part1Map),
            part1Map[operandMonkey2]!!.toMonkeyNode(part1Map)
        )
    }

    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return MultiplyNodePart1(monkeyId, operand1, operand2)
        }

        override fun regex() = "^([a-z]{4}): ([a-z]{4}) \\* ([a-z]{4})$".toRegex()
    }
}


data class DivideNodePart1(
    override val monkeyId: String,
    override val operandMonkey1: String,
    override val operandMonkey2: String
) : BinaryNodePart1 {

    override fun evaluate(monkeyMap: Map<String, Part1MonkeyNode>): Long {
        return monkeyMap[operandMonkey1]!!.evaluate(monkeyMap) /
                monkeyMap[operandMonkey2]!!.evaluate(monkeyMap)
    }

    override fun toMonkeyNode(part1Map: Map<String, Part1MonkeyNode>): MonkeyNode {
        return DivideNode(
            monkeyId,
            part1Map[operandMonkey1]!!.toMonkeyNode(part1Map),
            part1Map[operandMonkey2]!!.toMonkeyNode(part1Map)
        )
    }

    companion object : Part1MonkeyNodeCompanion {
        override fun parse(inputLine: String): Part1MonkeyNode {
            val (monkeyId, operand1, operand2) = inputLine.parseUsingRegex(regex())
            return DivideNodePart1(monkeyId, operand1, operand2)
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
