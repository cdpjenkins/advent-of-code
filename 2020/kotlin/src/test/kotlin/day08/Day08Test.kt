package day08

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput
import java.lang.AssertionError

class Day08Test : FunSpec({
    test("day 8 part 1 using test input") {
        lastAccBeforeLoop(testInput) shouldBe 5
    }

    test("day 8 part 1 using real input") {
        lastAccBeforeLoop(realInput) shouldBe 1487
    }

    test("day 8 part 2 using test data") {
        findFinalAccAfterFixing(Interpreter.of(testInput)) shouldBe 8
    }

    test("day 8 part 2 using real data") {
        findFinalAccAfterFixing(Interpreter.of(realInput)) shouldBe 1607
    }
})

private fun findFinalAccAfterFixing(interpreter: Interpreter) =
    (0..interpreter.instructions.size - 1)
        .map { i -> interpreter.runWithFlippedInstructionAt(i) }
        .find { it.terminated() }!!
        .registerFile
        .acc

private fun lastAccBeforeLoop(input: List<String>) =
    generateSequence(Interpreter.of(input)) { it.run() }
        .takeWhile { !it.looping() }
        .last()
        .registerFile
        .acc

class Interpreter(
    val instructions: List<Instruction>,
    val registerFile: RegisterFile = RegisterFile(0, 0),
    val visitedInstructions: Set<Int> = emptySet()
) {
    fun run() =
        Interpreter(
            instructions,
            instructions[registerFile.pc].execute(registerFile),
            visitedInstructions + registerFile.pc
        )

    fun terminated() = registerFile.pc >= instructions.count()
    fun looping() = registerFile.pc in visitedInstructions

    fun runWithFlippedInstructionAt(i: Int) =
        generateSequence(flipInstructionAt(i)) { it.run() }
            .find { it.looping() || it.terminated() }!!

    fun flipInstructionAt(i: Int): Interpreter {
        val mutatedInstructions = instructions.toMutableList()
        mutatedInstructions[i] = mutatedInstructions[i].flip()
        return Interpreter(mutatedInstructions)
    }

    companion object {
        fun of(input: List<String>): Interpreter {
            val instructions = input.withIndex().map { (i, instruction) -> instruction.parseInstruction(i) }
            return Interpreter(instructions)
        }
    }
}

class RegisterFile(
    val acc: Int,
    val pc: Int
) {
    fun nop() = RegisterFile(acc, pc + 1)
    fun acc(value: Int) = RegisterFile(acc + value, pc + 1)
    fun jmp(value: Int) = RegisterFile(acc, pc + value)
}

class Instruction(
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

    fun flip(): Instruction {
        val flippedOperation = when (operation) {
            "nop" -> "jmp"
            "jmp" -> "nop"
            else -> operation
        }
        return Instruction(i, flippedOperation, value)
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

val realInput = realInput("day08")
