package day08

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines
import java.lang.AssertionError

class Day08Test : FunSpec({
    test("day 8 part 1 using test input") {
        lastAccBeforeLoop(testInput) shouldBe 5
    }

    test("day 8 part 1 using real input") {
        lastAccBeforeLoop(realInput) shouldBe 1487
    }
})

private fun lastAccBeforeLoop(input: List<String>): Int =
    generateSequence(Interpreter.of(input)) { it.run() }
        .takeWhile { it.registerFile.pc !in it.visitedInstructions }
        .last()
        .registerFile
        .acc

data class Interpreter(
    val instructions: List<Instruction>,
    val registerFile: RegisterFile = RegisterFile(0, 0),
    val visitedInstructions: Set<Int> = emptySet()
) {
    fun run(): Interpreter {
        return Interpreter(
            instructions,
            instructions[registerFile.pc].execute(registerFile),
            visitedInstructions + registerFile.pc
        )
    }

    companion object {
        fun of(input: List<String>): Interpreter {
            val instructions = input.withIndex().map { (i, instruction) -> instruction.parseInstruction(i) }
            return Interpreter(instructions)
        }
    }
}

data class RegisterFile(
    val acc: Int,
    val pc: Int
) {
    fun nop() = RegisterFile(acc, pc + 1)
    fun acc(value: Int) = RegisterFile(acc + value, pc + 1)
    fun jmp(value: Int) = RegisterFile(acc, pc + value)
}

data class Instruction(
    val i: Int,
    val operation: String,
    val value: Int
) {
    fun execute(registerFile: RegisterFile) =
        when (operation) {
            "nop" -> registerFile.nop()
            "acc" -> registerFile.acc(value)
            "jmp" -> registerFile.jmp(value)
            else -> throw AssertionError()
        }
}


private fun String.parseInstruction(i: Int): Instruction {
    val (operation, sign, value) = regex.find(this)!!.destructured
    val ValueInt = value.toInt()
    val signedValue = if (sign == "+") ValueInt else -ValueInt

    return Instruction(i, operation, signedValue)
}

val regex = "^(nop|acc|jmp) ([+-])([0-9]+)$".toRegex()

val testInput = """
            nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6
        """.trimIndent().lines()

val realInput = readLines("day08")
