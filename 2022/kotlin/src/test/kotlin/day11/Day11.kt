package day11

import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day11 {
    @Test
    internal fun `part 1 test data`() {
        val input = testInput

        val monkeys = parseMonkeys(input)
        monkeys.forEach { println(it) }

        

        1 shouldBe 2
    }

}

data class Monkey(
    val id: Int,
    val items: List<Int>,
    val operation: Operation,
    val divisibleBy: Int,
    val toMonkeyIfTrue: Int,
    val toMonkeyIfFalse: Int
) {
    companion object {
        fun parse(it: String): Monkey {
            require(it.matches(MONKEY_REGEX)) { it }

            val (thisMonkeyIdString,
                startingItemsString,
                operatorString,
                operandString,
                divisibleByString,
                toMonkeyIfTrueString,
                toMonkeyIfFalseString) = it.parseUsingRegex(MONKEY_REGEX)

            return Monkey(
                thisMonkeyIdString.toInt(),
                startingItemsString.parseStartingItemsList(),
                Operation.parseOperator(operatorString, operandString),
                divisibleByString.toInt(),
                toMonkeyIfTrueString.toInt(),
                toMonkeyIfFalseString.toInt()
            )
        }
    }
}

data class Operation(
    val operator: Operator,
    val operand: Int  // -1 means "old"
) {
    companion object {
        internal fun parseOperator(operatorString: String, operandString: String): Operation {
            val operator = Operator.parseOperator(operatorString)
            val operand = Operation.parseOperand(operandString)
            val operation = Operation(operator, operand)
            return operation
        }

        private fun parseOperand(operandString: String): Int =
            if (operandString == "old") -1
            else operandString.toInt()
    }
}

enum class Operator {
    ADD, MUL;

    companion object {
        internal fun parseOperator(s: String): Operator {
            return when (s) {
                "*" -> MUL
                "+" -> ADD
                else -> throw IllegalArgumentException(s)
            }
        }
    }
}

private fun parseMonkeys(input: List<String>): Sequence<Monkey> =
    sequence {
        var lines = input
        while (lines.isNotEmpty()) {
            this.yield(lines.takeWhile { line -> line.isNotBlank() })
            lines = lines.dropWhile { line -> line.isNotBlank() }.drop(1)
        }
    }.map { it.joinToString("\n") }.map {
        Monkey.parse(it)
    }

private fun String.parseStartingItemsList() = this.split(", ").map { it.toInt() }

val MONKEY_REGEX = """
Monkey (\d):
  Starting items: (\d+(?:, \d+)*)
  Operation: new = old ([*+]) (\d+|old)
  Test: divisible by (\d+)
    If true: throw to monkey (\d)
    If false: throw to monkey (\d)
        """.trimIndent().toRegex()

val testInput =
    """
        Monkey 0:
          Starting items: 79, 98
          Operation: new = old * 19
          Test: divisible by 23
            If true: throw to monkey 2
            If false: throw to monkey 3
        
        Monkey 1:
          Starting items: 54, 65, 75, 74
          Operation: new = old + 6
          Test: divisible by 19
            If true: throw to monkey 2
            If false: throw to monkey 0
        
        Monkey 2:
          Starting items: 79, 60, 97
          Operation: new = old * old
          Test: divisible by 13
            If true: throw to monkey 1
            If false: throw to monkey 3
        
        Monkey 3:
          Starting items: 74
          Operation: new = old + 3
          Test: divisible by 17
            If true: throw to monkey 0
            If false: throw to monkey 1
    """.trimIndent().lines()
