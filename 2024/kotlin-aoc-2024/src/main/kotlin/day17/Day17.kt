package day17

import utils.ListUtils.splitByBlank
import utils.RegexUtils.parseUsingRegex

fun part1(input: List<String>): String =
    input.parseToMachineState()
        .executeUntilHalt()
        .output
        .joinToString(",")

fun part2(input: List<String>): Long {
    val program = input.parseToMachineState().program

    return findValuesOfAThatCauseProgramToPrintItself(program, 0, 1).first()
}

/**
 * Note that, for my input at least, there is more than one initial value of register A that causes the program to
 * print itself. This I did not anticipate.
 *
 * Note that I was hoping to come up with an analytical solution (rather than trial and error/brute force, which is what
 * this method does). But I couldn't figure it out.
 */
fun findValuesOfAThatCauseProgramToPrintItself(program: List<Long>, a: Long, length: Int): List<Long> =
    if (length > program.size) listOf(a)
    else (0..7)
        .map { aMod8 -> a.shl(3) or aMod8.toLong() }
        .filter { attempt -> executeProgramInKotlin(attempt).toList() == program.takeLast(length) }
        .flatMap { nextA -> findValuesOfAThatCauseProgramToPrintItself(program, nextA, length + 1) }

/**
 * This is the machine from *my* input, reimplemented in Kotlin for speed and fun.
 */
fun executeProgramInKotlin(initialA: Long): Sequence<Long> =
    sequence {
        var a = initialA

        while (a > 0) {
            val aMod8 = a.mod(8L)
            val shiftMeDoAmount = a shr ((aMod8) xor 3).toInt()
            val b = ((aMod8 xor 6) xor shiftMeDoAmount).mod(8L)
            yield(b)
            a = a shr 3
        }
    }

fun List<Long>.disassemble() = this.windowed(2, 2).map { disassembleInstruction(it[0], it[1]) }
fun disassembleInstruction(instruction: Long, operand: Long) = "${Instruction.of(instruction)}(${operand})"

fun List<String>.parseToMachineState(): ComputerState {
    val (registersLines, programLines) = splitByBlank()

    val (registerAString) = registersLines[0].parseUsingRegex("""^Register [ABC]: (\d+)$""")
    val (registerBString) = registersLines[1].parseUsingRegex("""^Register [ABC]: (\d+)$""")
    val (registerCString) = registersLines[2].parseUsingRegex("""^Register [ABC]: (\d+)$""")

    val registerAValue = registerAString.toLong()
    val registerBValue = registerBString.toLong()
    val registerCValue = registerCString.toLong()

    val (programString) = programLines[0].parseUsingRegex("""Program: ([\d,]+)""")
    val program = programString.split(",").map { it.toLong() }

    return ComputerState(registerAValue, registerBValue, registerCValue, program, programCounter = 0)
}

enum class Instruction(val opcode: Long) {
    ADV(0) {
        override fun execute(state: ComputerState) =
            state.copy(
                programCounter = state.programCounter + 2,
                registerA = state.registerA.shr(state.comboOperand().toInt())
            )
    },
    BXL(1) {
        override fun execute(state: ComputerState) =
            state.copy(
                programCounter = state.programCounter + 2,
                registerB = state.registerB xor state.literalOperand()
            )
    },
    BST(2) {
        override fun execute(state: ComputerState) =
            state.copy(
                registerB = state.comboOperand().mod(8).toLong(),
                programCounter = state.programCounter + 2
            )
    },
    JNZ(3) {
        override fun execute(state: ComputerState) =
            if (state.registerA == 0L) state.copy(programCounter = state.programCounter + 2)
            else state.copy(programCounter = state.literalOperand().toInt())
    },
    BXC(4) {
        override fun execute(state: ComputerState) =
            state.copy(
                programCounter = state.programCounter + 2,
                registerB = state.registerB xor state.registerC
            )
    },
    OUT(5) {
        override fun execute(state: ComputerState) =
            state.copy(
                programCounter = state.programCounter + 2,
                output = state.output + state.comboOperand().mod(8)
            )
    },
    BDV(6) {
        override fun execute(state: ComputerState) =
            state.copy(
                programCounter = state.programCounter + 2,
                registerB = state.registerA / 1.shl(state.comboOperand().toInt())
            )
    },
    CDV(7) {
        override fun execute(state: ComputerState): ComputerState =
            state.copy(
                programCounter = state.programCounter + 2,
                registerC = state.registerA / 1.shl(state.comboOperand().toInt())
            )
    };

    abstract fun execute(state: ComputerState): ComputerState

    companion object {
        fun of(opcode: Long) = entries.first { it.opcode == opcode }
    }
}

data class ComputerState(
    val registerA: Long,
    val registerB: Long,
    val registerC: Long,
    val program: List<Long>,
    val programCounter: Int = 0,
    val output: List<Int> = emptyList()
) {
    fun comboOperand() =
        program[programCounter + 1].let {
            when (it) {
                in (0..3) -> it
                4L -> registerA
                5L -> registerB
                6L -> registerC
                else -> throw IllegalStateException("Invalid combo: $it")
            }
        }
    fun executeUntilHalt(): ComputerState =
        generateSequence(this) { it.executeOneInstruction() }
            .first { it.isHalted() }
    fun executeOneInstruction() = Instruction.of(program[programCounter]).execute(this)
    fun isHalted() = programCounter >= program.size
    fun literalOperand() = program[programCounter+1]
}
