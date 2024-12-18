package day17

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe "4,6,3,5,6,3,5,2,1,0"
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day17.txt")) shouldBe "5,1,3,4,3,7,2,1,7"
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day17.txt")) shouldBe 216584205979245L
    }

    @Test
    fun `executing bst instruction writes operand modulo 8 to register B`() {
        val state = """
            Register A: 0
            Register B: 0
            Register C: 9

            Program: 2,6
        """.trimIndent().lines().parseToMachineState()

        val newState = state.executeUntilHalt()

        newState shouldBe state.copy(
            registerA = 0,
            registerB = 1,
            registerC = 9,
            programCounter = 2
        )
    }

    @Test
    fun `executing out instruction outputs combo operand modulo 8`() {
        val state = """
            Register A: 10
            Register B: 0
            Register C: 0

            Program: 5,0,5,1,5,4
        """.trimIndent().lines().parseToMachineState()

        val newState = state.executeUntilHalt()

        newState shouldBe state.copy(
            registerA = 10,
            registerB = 0,
            registerC = 0,
            programCounter = 6,
            output = listOf(0, 1, 2)
        )
    }

    @Test
    fun `jnz jumps if A is non-zero`() {
        val state = """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent().lines().parseToMachineState()

        val newState = state.executeUntilHalt()

        newState.output shouldBe listOf(4,2,5,6,7,7,7,7,3,1,0)
        newState.registerA shouldBe 0
    }

    @Test
    fun `bxl computes xor or b and literal arg and puts result in b`() {
        val state = """
            Register A: 0
            Register B: 29
            Register C: 0

            Program: 1,7
        """.trimIndent().lines().parseToMachineState()

        val newState = state.executeUntilHalt()

        newState.registerB shouldBe 26
    }

    @Test
    fun `bxc computers xor of b and c and puts result in b`() {
        val state = """
            Register A: 0
            Register B: 2024
            Register C: 43690

            Program: 4,0
        """.trimIndent().lines().parseToMachineState()

        val newState = state.executeUntilHalt()

        newState.registerB shouldBe 44354
    }

    @Test
    fun `can execute the program using the simulated computer`() {
        val state = """
            Register A: 216584205979245
            Register B: 0
            Register C: 0
            
            Program: 2,4,1,3,7,5,1,5,0,3,4,2,5,5,3,0
        """.trimIndent().lines().parseToMachineState()

        state.executeUntilHalt().output shouldBe listOf(2,4,1,3,7,5,1,5,0,3,4,2,5,5,3,0)
    }

    @Test
    fun `can execute the program in kotlin`() {
        val output = executeProgramInKotlin(initialA = 216584205979327).toList()
        output shouldBe listOf(2,4,1,3,7,5,1,5,0,3,4,2,5,5,3,0)
    }
}

val testInput =
    """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent().lines()
