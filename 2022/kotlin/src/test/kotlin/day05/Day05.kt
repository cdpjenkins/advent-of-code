package day05

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day05 {
    @Test
    internal fun `part 1 with sample input`() {
        solveForCrateMover9000(testInput) shouldBe "CMZ"
    }

    @Test
    internal fun `part 1 with real input`() {
        solveForCrateMover9000(realInput) shouldBe "HNSNMTLHQ"
    }

    @Test
    internal fun `part 2 with sample input`() {
        solveForCrateMover9001(testInput) shouldBe "MCD"
    }

    @Test
    internal fun `part 2 with real input`() {
        solveForCrateMover9001(realInput) shouldBe "RNLFDJMCT"
    }

    private fun solveForCrateMover9001(input: List<String>): String {
        val stacks = parseStacks(input)
        val moves = parseMovesFromInput(input)

        moves.forEach {
            it.applyCrateMover9001(stacks)
        }

        return stacks
            .map { it.last() }
            .joinToString("")
    }

    private fun solveForCrateMover9000(input: List<String>): String {
        val stacks = parseStacks(input)
        val moves = parseMovesFromInput(input)

        moves.forEach {
            it.applyCrateMover9000(stacks)
        }

        return stacks
            .map { it.last() }
            .joinToString("")
    }

    private fun parseMovesFromInput(input: List<String>): List<Move> =
        input
            .dropWhile { !it.isBlank() }
            .drop(1)
            .parseMoves()

    private fun parseStacks(input: List<String>): List<ArrayList<Char>> {
        val stacksInput = input.takeWhile { !it.isBlank() }

        val numStacks = stacksInput
            .last()
            .trim { it.isWhitespace() }
            .split(" +".toRegex())
            .size

        val stacks = makeStacks(stacksInput.dropLast(1), numStacks)
        return stacks
    }

    private fun List<String>.parseMoves(): List<Move> =
        map {
            val (numString, fromString, toString) = it.parseUsingRegex("^move (\\d+) from (\\d+) to (\\d+)$")

            val num = numString.toInt()
            val from = fromString.toInt()
            val to = toString.toInt()

            Move(num, from, to)
        }

    private fun makeStacks(stacksStrings: List<String>, numStacks: Int): List<ArrayList<Char>> {
        val stacks = (1..numStacks).map { ArrayList<Char>() }

        stacksStrings.forEach {row ->
            (0..numStacks-1).forEach {i ->
                val crate = row[4 * i + 1]

                if (crate != ' ') {
                    stacks[i].add(0, crate)
                }
            }
        }

        return stacks
    }
}

data class Move(val num: Int, val from: Int, val to: Int) {
    fun applyCrateMover9000(stacks: List<ArrayList<Char>>) {
        repeat(num) {
            val crate = stacks[from - 1].removeLast()
            stacks[to - 1].add(crate)
        }
    }

    fun applyCrateMover9001(stacks: List<ArrayList<Char>>) {
        val stack = ArrayList<Char>()

        repeat(num) {
            val crate = stacks[from - 1].removeLast()
            stack.add(0, crate)
        }

        stacks[to - 1].addAll(stack)
    }
}

val testInput =
    """
            [D]    
        [N] [C]    
        [Z] [M] [P]
         1   2   3 
        
        move 1 from 2 to 1
        move 3 from 1 to 3
        move 2 from 2 to 1
        move 1 from 1 to 2
    """.trimIndent().lines()

val realInput = readInputFileToList("day05.txt")