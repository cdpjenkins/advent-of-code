package day24

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import utils.RegexUtils.parseUsingRegex
import kotlin.test.Ignore

private fun part1(input: List<String>) =
    Device.of(input)
        .getZNumber()

data class Device(val gatesMap: Map<String, Gate>) {
    fun getValueFor(gateId: String) = gatesMap[gateId]!!.getValue(this)

    fun getZNumber(): Long =
        gatesMap.keys
            .filter { it.startsWith("z") }.sorted().reversed()
            .map { getValueFor(it) }
            .fold(0L) { acc, x -> acc.shl(1) + x }

    fun withX(x: Long): Device {
        val newGates = gatesMap.toMutableMap()
        var xVar = x
        for (i in (0 until 64)) {
            val cunt = String.format("x%02d", i)
            newGates[cunt] = Input(cunt, (xVar and 1).toInt())
            xVar = xVar shr 1
        }

        return this.copy(gatesMap = newGates)
    }

    fun withY(y: Long): Device {
        val newGates = gatesMap.toMutableMap()
        var yVar = y
        for (i in (0 until 64)) {
            val cunt = String.format("y%02d", i)
            newGates[cunt] = Input(cunt, (yVar and 1).toInt())
            yVar = yVar shr 1
        }

        return this.copy(gatesMap = newGates)
    }

    fun getZ(): Long {
        var zVar = 0L
        for (i in (63 downTo 0)) {
            zVar = zVar shl 1

            val thisNodeId = String.format("z%02d", i)
            val thisBit = gatesMap[thisNodeId]?.getValue(this) ?: 0

            zVar = zVar or thisBit.toLong()
        }

        return zVar
    }

    fun printGraphViz() {
        println("digraph {")
        gatesMap.entries
            .forEach { (id, gate) ->
            gate.printGraphViz()
        }
        println("}")
    }

    companion object {
        fun of(input: List<String>): Device {
            return Device((parseInputs(input) + parseGates(input)).toMap())
        }

        private fun parseGates(input: List<String>): List<Pair<String, Operator>> {
            val GATES_REGEX = """^([a-z0-9]{3}) (AND|OR|XOR) ([a-z0-9]{3}) -> ([a-z0-9]{3})$""".toRegex()
            val (_, gatesLines) = input.splitByBlank()
            val gates = gatesLines.map {
                val (inputGate1, operator, inputGate2, outputGate) = it.parseUsingRegex(GATES_REGEX)
                Operator(operator, inputGate1, inputGate2, outputGate)
            }
            return gates.map { it.outputGate to it }
        }

        private fun parseInputs(input: List<String>): List<Pair<String, Input>> {
            val INPUTS_REGEX = """^([xy]\d\d): ([01])$"""
            val (inputsLines, _) = input.splitByBlank()

            val inputs = inputsLines.map {
                val (nameString, valueString) = it.parseUsingRegex(INPUTS_REGEX)
                Input(nameString, valueString.toInt())
            }
            return inputs.map { it.name to it }
        }
    }
}

sealed interface Gate {
    fun getValue(device: Device): Int
    fun printGraphViz()
}

data class Operator(val operator: String, val inputGate1: String, val inputGate2: String, val outputGate: String): Gate {
    override fun getValue(device: Device): Int {
        val input1 = device.gatesMap[inputGate1]!!.getValue(device)
        val input2 = device.gatesMap[inputGate2]!!.getValue(device)

        return when (operator) {
            "AND" -> computeAND(input1, input2)
            "XOR" -> computeXOR(input1, input2)
            "OR" -> computeOR(input1, input2)
            else -> throw IllegalArgumentException("Unknown operator: $operator")
        }
    }

    override fun printGraphViz() {
        println("  ${outputGate}  [color=\"${operator.toColour()}\"]")
        println("  ${inputGate1} -> ${outputGate} [label=\"${operator}\"] [color=\"${operator.toColour()}\"]")
        println("  ${inputGate2} -> ${outputGate} [label=\"${operator}\"] [color=\"${operator.toColour()}\"]")
    }

    private fun computeAND(input1: Int, input2: Int) = if (input1 == 1 && input2 == 1) 1 else 0
    private fun computeOR(input1: Int, input2: Int) = if (input1 == 1 || input2 == 1) 1 else 0
    private fun computeXOR(input1: Int, input2: Int) = if (input1 != input2) 1 else 0
}

private fun String.toColour() =
    when (this) {
        "AND" -> "red"
        "OR" -> "green"
        "XOR" -> "blue"
        else -> throw IllegalArgumentException("Unknown colour: $this")
    }

data class Input(val name: String, val value: Int): Gate {
    override fun getValue(device: Device) = value
    override fun printGraphViz() {
        // dim byd
    }
}

private fun part2(input: List<String>): String {
    // Found manually, making use of GraphViz to visualise the cir  cuitry:
    //
    // z05 and bpf... Z should be fed by XORs
    // z11 and hcc
    // hqc and qcw    (feeds z24 and the next stage)
    // z35 and fdw

    return "bpf,fdw,hcc,hqc,qcw,z05,z11,z35"
}

class Day24Test {

    @Test
    fun `part 1 with simple test input`() {
        val input = simpleTestInput

        val device = Device.of(input)
        device.getValueFor("z00") shouldBe 0
        device.getValueFor("z01") shouldBe 0
        device.getValueFor("z02") shouldBe 1

        part1(simpleTestInput) shouldBe 4
    }

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 2024
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day24.txt")) shouldBe 58367545758258
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day24.txt")) shouldBe "bpf,fdw,hcc,hqc,qcw,z05,z11,z35"
    }

    @Ignore
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `can set input and compute shizzle`() {
        val initialDevice = Device.of(readInputFileToList("day24.txt"))

        val device = initialDevice
            .withX(0x1FFFFFFFF0)
            .withY(0x0FFFFFFFF1)

        // this fails due to the gates that need swapping
        device.getZ().toHexString() shouldBe (0x1FFFFFFFF0 + 0x0FFFFFFFF1).toHexString()

        // Possible future improvement: Actually implement swapping of gates so we can
        // guarantee that we're fixing the right gates
    }

    @Test
    fun `can generate graphviz graph`() {
        Device.of(readInputFileToList("day24.txt")).printGraphViz()
    }
}

val simpleTestInput =
    """
        x00: 1
        x01: 1
        x02: 1
        y00: 0
        y01: 1
        y02: 0

        x00 AND y00 -> z00
        x01 XOR y01 -> z01
        x02 OR y02 -> z02
    """.trimIndent().lines()

val testInput =
    """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1
        
        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
    """.trimIndent().lines()

// candidates
//
// z05 and bpf... Z should be fed by XORs
// z11 and hcc
// hqc and qcw    (feeds z24 and the next stage)
// z35 and fdw

// bpf,fdw,hcc,hqc,qcw,z05,z11,z35
