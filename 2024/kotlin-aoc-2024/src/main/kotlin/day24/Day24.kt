package day24

import utils.ListUtils.splitByBlank
import utils.RegexUtils.parseUsingRegex

fun part1(input: List<String>) =
    Device.of(input)
        .getZ()

data class Device(val gatesMap: Map<String, Gate>) {
    fun getValueFor(gateId: String) = gatesMap[gateId]!!.getValue(this)

    fun getZ(): Long =
        gatesMap.keys
            .filter { it.startsWith("z") }.sorted().reversed()
            .map { getValueFor(it) }
            .fold(0L) { acc, x -> acc.shl(1) + x }

    fun withX(x: Long): Device {
        val newGates = gatesMap.toMutableMap()
        var xVar = x
        for (i in (0 until 64)) {
            val nodeName = String.format("x%02d", i)
            newGates[nodeName] = Input(nodeName, (xVar and 1).toInt())
            xVar = xVar shr 1
        }

        return this.copy(gatesMap = newGates)
    }

    fun withY(y: Long): Device {
        val newGates = gatesMap.toMutableMap()
        var yVar = y
        for (i in (0 until 64)) {
            val nodeName = String.format("y%02d", i)
            newGates[nodeName] = Input(nodeName, (yVar and 1).toInt())
            yVar = yVar shr 1
        }

        return this.copy(gatesMap = newGates)
    }

    /**
     * To use the output of this, copy it into a file called `day24.dot` and run something like:
     *
     * `dot -Tsvg day24.dot  >output.svg`
     *
     * Then spend hours staring at the resulting circuit, trying to work out which bits are screwed up.
     */
    fun printGraphViz() {
        println("digraph {")
        gatesMap.entries.forEach { (_, gate) -> gate.printGraphViz() }
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

fun part2(input: List<String>): String {
    // Found manually, making use of GraphViz to visualise the cir  cuitry:
    //
    // z05 and bpf... Z should be fed by XORs
    // z11 and hcc
    // hqc and qcw    (feeds z24 and the next stage)
    // z35 and fdw

    return "bpf,fdw,hcc,hqc,qcw,z05,z11,z35"
}
