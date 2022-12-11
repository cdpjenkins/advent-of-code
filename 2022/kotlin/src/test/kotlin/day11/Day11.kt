package day11

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day11 {
    @Test
    fun `part 1 test data`() {
        val input = testInput

        val monkeys = parseMonkeys(input)

        val afterRound20 = generateSequence(monkeys) { it.oneRound(3) }.take(21).toList()[20]

        val productOfThrowses = afterRound20
            .map { it.numThrows }
            .sorted()
            .reversed()
            .take(2).reduce { x, y -> x * y }

        productOfThrowses shouldBe 10605
    }

    @Test
    fun `part 1 real data`() {
        val input = realInput

        val monkeys = parseMonkeys(input)

        val afterRound20 = generateSequence(monkeys) { it.oneRound(3) }.take(21).toList()[20]

        val productOfThrowses = afterRound20
            .map { it.numThrows }
            .sorted()
            .reversed()
            .take(2).reduce { x, y -> x * y }

        productOfThrowses shouldBe 99840
    }

    @Test
    fun `part 2 test data`() {
        val input = testInput

        val monkeys = parseMonkeys(input)

        val monkeySequence = generateSequence(monkeys) { it.oneRound(1) }.take(10001).toList()

        monkeySequence[1].map { it.numThrows } shouldBe listOf(2, 4, 3, 6)
        monkeySequence[20].map { it.numThrows } shouldBe listOf(99, 97, 8, 103)
        println(monkeySequence[1000].map { it.items })
        monkeySequence[1000].map { it.numThrows } shouldBe listOf(5204, 4792, 199, 5192)

        val afterRound20 = monkeySequence[10000]

        val productOfThrowses = afterRound20
            .map { it.numThrows }
            .sorted()
            .reversed()
            .take(2).reduce { x, y -> x * y }

        productOfThrowses shouldBe 2713310158
    }

    @Test
    fun `part 2 real data`() {
        val input = realInput

        val monkeys = parseMonkeys(input)

        val monkeySequence = generateSequence(monkeys) { it.oneRound(1) }.take(10001).toList()

        val afterRound20 = monkeySequence[10000]

        val productOfThrowses = afterRound20
            .map { it.numThrows }
            .sorted()
            .reversed()
            .take(2).reduce { x, y -> x * y }

        productOfThrowses shouldBe 20683044837L
    }

    @Test
    internal fun `after one round with test data`() {
        val input = testInput

        val monkeys = parseMonkeys(input)

        val monkeysAfterRoundOne = monkeys.oneRound(3)

//        monkeysAfterRoundOne.map { it.items } shouldBe listOf(
//            listOf(20, 23, 27, 26),
//            listOf(2080, 25, 167, 207, 401, 1046),
//            listOf(),
//            listOf()
//        )

        monkeysAfterRoundOne.map { it.numThrows } shouldBe listOf(2, 4, 3, 5)
    }

    @Test
    internal fun `first monkey inspects items - test data`() {
        val input = testInput

        val monkeys = parseMonkeys(input)

        val monkey = monkeys.first()

        val (updatedMonkey, throws) = monkey.inspectItems(3)

        throws shouldBe listOf(
            Throw(3, 500),
            Throw(3, 620)
        )

        updatedMonkey.numThrows shouldBe 2
    }
}

private fun List<Monkey>.oneRound(divideBy: Long): List<Monkey> {
    // procedural code and mutation of state coming up...
    val monkeys = this.toMutableList()

    (0..monkeys.size-1).forEach { i ->
        val monkey = monkeys[i]

        val (newMonkey, throws) = monkey.inspectItems(divideBy)

        monkeys[i] = newMonkey
        throws.forEach { thisThrow ->
            monkeys[thisThrow.toMonkey] = monkeys[thisThrow.toMonkey].throwTo(thisThrow.worryLevel)
        }
    }

    return monkeys.toList()
}

data class Throw(val toMonkey: Int, val worryLevel: Long)

data class Monkey(
    val id: Int,
    val items: List<Long>,
    val operation: Operation,
    val divisibleBy: Long,
    val toMonkeyIfTrue: Int,
    val toMonkeyIfFalse: Int,
    val numThrows: Long = 0,
    val modulus: Long = 0
) {
    fun inspectItems(divideBy: Long): Pair<Monkey, List<Throw>> {
        val throws = items.map { inspect(it, divideBy) }
        return Pair(
            this.copy(
                items = listOf(),
                numThrows = this.numThrows + throws.size
            ),
            throws
        )
    }

    private fun inspect(worryLevel: Long, divideBy: Long): Throw {
        val ston = operation.applyTo(worryLevel) / divideBy
        val newWorryLevel = ston % modulus
        val throwTo = if (newWorryLevel % divisibleBy == 0L) toMonkeyIfTrue else toMonkeyIfFalse

        return Throw(throwTo, newWorryLevel)
    }

    fun throwTo(worryLevel: Long): Monkey {
        return this.copy(items = items + worryLevel)
    }

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
                divisibleByString.toLong(),
                toMonkeyIfTrueString.toInt(),
                toMonkeyIfFalseString.toInt()
            )
        }
    }
}

data class Operation(
    val operator: Operator,
    val operand: Long  // -1 means "old"
) {
    fun applyTo(worryLevel: Long): Long {
        val actualOperand = if (operand == -1L) worryLevel else operand
        return operator.applyTo(worryLevel, actualOperand)
    }

    companion object {
        internal fun parseOperator(operatorString: String, operandString: String): Operation {
            val operator = Operator.parseOperator(operatorString)
            val operand = parseOperand(operandString)
            val operation = Operation(operator, operand)
            return operation
        }

        private fun parseOperand(operandString: String): Long =
            if (operandString == "old") -1
            else operandString.toLong()
    }
}

enum class Operator {
    ADD {
        override fun applyTo(worryLevel: Long, operand: Long) = worryLevel + operand
    },
    MUL {
        override fun applyTo(worryLevel: Long, operand: Long) = worryLevel * operand
    };

    abstract fun applyTo(worryLevel: Long, operand: Long): Long

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

private fun parseMonkeys(input: List<String>): List<Monkey> {
    val monkeys = sequence {
        var lines = input
        while (lines.isNotEmpty()) {
            this.yield(lines.takeWhile { line -> line.isNotBlank() })
            lines = lines.dropWhile { line -> line.isNotBlank() }.drop(1)
        }
    }.map { it.joinToString("\n") }.map {
        Monkey.parse(it)
    }.toList()

    val modulus = monkeys.map { it.divisibleBy }.reduce { x, y -> x * y }

    return monkeys.map { it.copy(modulus = modulus)}
}

private fun String.parseStartingItemsList() = this.split(", ").map { it.toLong() }

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

val realInput = readInputFileToList("day11.txt")