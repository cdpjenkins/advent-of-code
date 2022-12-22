package day21

import StringUtils.indentBy
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.IllegalArgumentException

/**
 * Because I can't be bothered working with the the part 1 code and also can't be bothered to refactor it!
 */
class Day21Part2 {

    @Test
    internal fun `part 2 test input`() {
        findHumnValueFor(testInput) shouldBe 301
    }

    @Test
    internal fun `part 2 real input`() {
        findHumnValueFor(realInput) shouldBe 3243420789721
    }
}

private fun findHumnValueFor(input: List<String>) = buildTree(input)
    .simplify()
    .findHumnValue()

private fun buildTree(input: List<String>): MonkeyNode {
    val nodes = input.map { Part1MonkeyNode.parse(it) }

    val part1Map = nodes.map { it.monkeyId to it }.toMap()

    val nodesMap = part1Map.values.map {
        val node = it.toMonkeyNode(part1Map)
        node.monkeyId to node
    }.toMap()

    val rootNode = nodesMap["root"]!!
    return rootNode
}

sealed interface MonkeyNode{
    val monkeyId: String

    fun dumpToStdout(indent: Int = 0)
    fun simplify(): MonkeyNode
    fun findHumnValue(requiredValue: Long = -1): Long
}

data class NumberNode(override val monkeyId: String, val number: Long) : MonkeyNode {
    override fun dumpToStdout(indent: Int) {
        println("${indentBy(indent)}${this}")
    }

    override fun simplify() = this
    override fun findHumnValue(requiredValue: Long) = throw IllegalArgumentException(this.toString())
}

interface BinaryNode : MonkeyNode {
    override val monkeyId: String
    val operand1: MonkeyNode
    val operand2: MonkeyNode

    override fun dumpToStdout(indent: Int) {
        println("${indentBy(indent)}${this}")
        operand1.dumpToStdout(indent+1)
        operand2.dumpToStdout(indent+1)
    }
}

data class RootNode(
    override val monkeyId: String,
    override val operand1: MonkeyNode,
    override val operand2: MonkeyNode,
) : BinaryNode {
    override fun simplify() = RootNode(monkeyId, operand1.simplify(), operand2.simplify())

    override fun findHumnValue(requiredValue: Long) =
        when {
            operand1 is NumberNode -> operand2.findHumnValue(operand1.number)
            operand2 is NumberNode -> operand1.findHumnValue(operand2.number)
            else -> throw IllegalArgumentException(this.toString())
        }
}

data class HumnNode(
    override val monkeyId: String,
) : MonkeyNode {
    override fun dumpToStdout(indent: Int) {
        println("${indentBy(indent)}${this}")
    }

    override fun simplify() = this
    override fun findHumnValue(requiredValue: Long) = requiredValue
}

data class PlusNode(
    override val monkeyId: String,
    override val operand1: MonkeyNode,
    override val operand2: MonkeyNode,
) : BinaryNode {
    override fun simplify(): MonkeyNode {
        val op1 = operand1.simplify()
        val op2 = operand2.simplify()
        if (op1 is NumberNode && op2 is NumberNode) {
            return NumberNode(this.monkeyId, op1.number + op2.number)
        } else {
            return PlusNode(this.monkeyId, op1, op2)
        }
    }

    override fun findHumnValue(requiredValue: Long) =
        when {
            operand1 is NumberNode -> operand2.findHumnValue(requiredValue - operand1.number)
            operand2 is NumberNode -> operand1.findHumnValue(requiredValue - operand2.number)
            else -> throw IllegalArgumentException(this.toString())
        }
}

data class MinusNode(
    override val monkeyId: String,
    override val operand1: MonkeyNode,
    override val operand2: MonkeyNode,
) : BinaryNode {
    override fun simplify(): MonkeyNode {
        val op1 = operand1.simplify()
        val op2 = operand2.simplify()
        if (op1 is NumberNode && op2 is NumberNode) {
            return NumberNode(this.monkeyId, op1.number - op2.number)
        } else {
            return MinusNode(this.monkeyId, op1, op2)
        }
    }

    override fun findHumnValue(requiredValue: Long) =
        when {
            operand1 is NumberNode -> operand2.findHumnValue(operand1.number - requiredValue)
            operand2 is NumberNode -> operand1.findHumnValue(requiredValue + operand2.number)
            else -> throw IllegalArgumentException(this.toString())
        }
}

data class MultiplyNode(
    override val monkeyId: String,
    override val operand1: MonkeyNode,
    override val operand2: MonkeyNode,
) : BinaryNode {
    override fun simplify(): MonkeyNode {
        val op1 = operand1.simplify()
        val op2 = operand2.simplify()
        if (op1 is NumberNode && op2 is NumberNode) {
            return NumberNode(this.monkeyId, op1.number * op2.number)
        } else {
            return MultiplyNode(this.monkeyId, op1, op2)
        }
    }

    override fun findHumnValue(requiredValue: Long) =
        when {
            operand1 is NumberNode -> operand2.findHumnValue(requiredValue / operand1.number)
            operand2 is NumberNode -> operand1.findHumnValue(requiredValue / operand2.number)
            else -> throw IllegalArgumentException(this.toString())
        }
}

data class DivideNode(
    override val monkeyId: String,
    override val operand1: MonkeyNode,
    override val operand2: MonkeyNode,
) : BinaryNode {
    override fun simplify(): MonkeyNode {
        val op1 = operand1.simplify()
        val op2 = operand2.simplify()
        if (op1 is NumberNode && op2 is NumberNode) {
            return NumberNode(this.monkeyId, op1.number / op2.number)
        } else {
            return DivideNode(this.monkeyId, op1, op2)
        }
    }

    override fun findHumnValue(requiredValue: Long) =
        when {
            operand1 is NumberNode -> operand2.findHumnValue(operand1.number / requiredValue)
            operand2 is NumberNode -> operand1.findHumnValue(requiredValue * operand2.number)
            else -> throw IllegalArgumentException(this.toString())
        }
}
