package day10

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day10 {


    @Test
    internal fun `part 1 with simple test data`() {
        val simpleTestInput =
            """
                noop
                addx 3
                addx -5
            """.trimIndent().lines()

        val initialMachineState = parseToMachineState(simpleTestInput)

        val stateSeq = generateSequence(initialMachineState) { it.executeCycle() }

        val accumulatorValues = stateSeq.take(6).map { it.accumulator }.toList()

        accumulatorValues shouldBe listOf(
            1,
            1,
            1,
            4,
            4,
            -1
        )
    }

    @Test
    internal fun `part 1 test data`() {
        val initialMachineState = parseToMachineState(testInput)

        val sum = generateSequence(initialMachineState) { it.executeCycle() }
            .takeWhile { it.running }
            .map { it.accumulator }
            .toList()
            .withIndex()
            .map { it.copy(index = it.index + 1) }
            .drop(19)
            .chunked(40)
            .map { it.first() }.map { it.index * it.value }.sum()

        sum shouldBe 13140
    }

    @Test
    internal fun `part 1 real data`() {
        val initialMachineState = parseToMachineState(realInput)

        val sum = generateSequence(initialMachineState) { it.executeCycle() }
            .takeWhile { it.running }
            .map { it.accumulator }
            .toList()
            .withIndex()
            .map { it.copy(index = it.index + 1) }
            .drop(19)
            .chunked(40)
            .map { it.first() }.map { it.index * it.value }.sum()

        sum shouldBe 13140
    }
}

private fun parseToMachineState(input: List<String>): MachineState {
    val commands = parseCommands(input)
    val initialMachineState = MachineState(commandQueue = commands)
    return initialMachineState
}

private fun parseCommands(simpleTestInput: List<String>) = simpleTestInput.map { it.parseInstruction() }

private fun String.parseInstruction(): Command {
    val ADDX_REGEX = "^addx (-?\\d+)$".toRegex()
    val NOOP_REGEX = "^noop$".toRegex()

    when {
        matches(ADDX_REGEX) -> {
            val (operandString) = parseUsingRegex(ADDX_REGEX)
            return AddxCommand(operandString.toInt())
        }

        matches(NOOP_REGEX) -> {
            return NoopCommand
        }

        else -> throw IllegalArgumentException(this)
    }
}

data class MachineState(
    val running: Boolean = true,
    val accumulator: Int = 1,
    val microOpQueue: List<MicroOp> = listOf(),
    val commandQueue: List<Command>,
) {
    fun executeCycle(): MachineState {
        val machineState =
            if (microOpQueue.isEmpty()) {
                val nextCommand = if (commandQueue.isNotEmpty()) commandQueue.first() else StopCommand
                val nextMicroOpQueue = nextCommand.dispatch()

                MachineState(
                    accumulator = accumulator,
                    microOpQueue = nextMicroOpQueue,
                    commandQueue = commandQueue.drop(1)
                )
            } else {
                this
            }

        val nextMicroOp = machineState.microOpQueue.first()

        val remainingMicroOps = machineState.microOpQueue.drop(1)

        val nextMachineState =
            nextMicroOp.execute(machineState)
                .copy(microOpQueue = remainingMicroOps)

        return nextMachineState
    }
}

sealed interface MicroOp {
    fun execute(machineState: MachineState): MachineState
}

data class AddxMicroOp(val operand: Int) : MicroOp {
    override fun execute(machineState: MachineState): MachineState {
        return machineState.copy(accumulator = machineState.accumulator + operand)
    }
}

object NoopMicroOp : MicroOp {
    override fun execute(machineState: MachineState): MachineState {
        return machineState
    }
}

object StopMicroOp : MicroOp {
    override fun execute(machineState: MachineState): MachineState {
        return machineState.copy(running = false)
    }
}

sealed interface Command {
    fun dispatch(): List<MicroOp>
}

data class AddxCommand(val operand: Int) : Command {
    override fun dispatch(): List<MicroOp> {
        return listOf(NoopMicroOp, AddxMicroOp(operand))
    }
}

object NoopCommand : Command {
    override fun dispatch(): List<MicroOp> {
        return listOf(NoopMicroOp)
    }
}

object StopCommand : Command {
    override fun dispatch(): List<MicroOp> {
        return listOf(StopMicroOp)
    }
}

val testInput =
    """
        addx 15
        addx -11
        addx 6
        addx -3
        addx 5
        addx -1
        addx -8
        addx 13
        addx 4
        noop
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx -35
        addx 1
        addx 24
        addx -19
        addx 1
        addx 16
        addx -11
        noop
        noop
        addx 21
        addx -15
        noop
        noop
        addx -3
        addx 9
        addx 1
        addx -3
        addx 8
        addx 1
        addx 5
        noop
        noop
        noop
        noop
        noop
        addx -36
        noop
        addx 1
        addx 7
        noop
        noop
        noop
        addx 2
        addx 6
        noop
        noop
        noop
        noop
        noop
        addx 1
        noop
        noop
        addx 7
        addx 1
        noop
        addx -13
        addx 13
        addx 7
        noop
        addx 1
        addx -33
        noop
        noop
        noop
        addx 2
        noop
        noop
        noop
        addx 8
        noop
        addx -1
        addx 2
        addx 1
        noop
        addx 17
        addx -9
        addx 1
        addx 1
        addx -3
        addx 11
        noop
        noop
        addx 1
        noop
        addx 1
        noop
        noop
        addx -13
        addx -19
        addx 1
        addx 3
        addx 26
        addx -30
        addx 12
        addx -1
        addx 3
        addx 1
        noop
        noop
        noop
        addx -9
        addx 18
        addx 1
        addx 2
        noop
        noop
        addx 9
        noop
        noop
        noop
        addx -1
        addx 2
        addx -37
        addx 1
        addx 3
        noop
        addx 15
        addx -21
        addx 22
        addx -6
        addx 1
        noop
        addx 2
        addx 1
        noop
        addx -10
        noop
        noop
        addx 20
        addx 1
        addx 2
        addx 2
        addx -6
        addx -11
        noop
        noop
        noop
    """.trimIndent().lines()

val realInput = readInputFileToList("day10.txt")