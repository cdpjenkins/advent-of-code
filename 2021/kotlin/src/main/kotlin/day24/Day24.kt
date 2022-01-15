package day24

import readInput
import java.lang.AssertionError

fun main() {
    val input = readInput("Day24")

    transpile(input)
}

fun transpile(input: List<String>) {
    val lastWritten = mutableMapOf(
        "w" to 0,
        "x" to 0,
        "y" to 0,
        "z" to 0
    )

    val kotlinInstructions = input.withIndex().map { (i, instruction) ->
        val (operation, operand1, _, operand2) = regex.find(instruction)?.destructured ?: throw AssertionError(
            instruction
        )
        val v1 = "${operand1}${i}"
        val v2 = if (!operand2.isEmpty()) {
            if (operand2[0].isDigit() || operand2[0] == '-') {
                operand2
            } else {
                "${operand2}${lastWritten[operand2]
                }"
            }
        } else {
            ""
        }
        val ston = when (operation) {
            "inp" -> {
                lastWritten[operand1] = i
                "val $v1 = input"
            }
            "add" -> {
                val kot = "val ${v1} = ${operand1}${lastWritten[operand1]} + ${v2}"
                lastWritten[operand1] = i
                kot
            }
            "mul" -> {
                val kot = "val ${v1} = ${operand1}${lastWritten[operand1]} * ${v2}"
                lastWritten[operand1] = i
                kot
            }
            "div" -> {
                val kot = "val ${v1} = ${operand1}${lastWritten[operand1]} / ${v2}"
                lastWritten[operand1] = i
                kot
            }
            "mod" -> {
                val kot = "val ${v1} = ${operand1}${lastWritten[operand1]} % ${v2}"
                lastWritten[operand1] = i
                kot
            }
            "eql" -> {
                val kot = "val ${v1} = if (${operand1}${lastWritten[operand1]} == ${v2}) 1 else 0"
                lastWritten[operand1] = i
                kot
            }
            else -> {
                throw AssertionError(operation)
            }
        }
        println(operation)
        println(lastWritten)
        ston
    }
    val message = kotlinInstructions.joinToString("\n")
    println("""
        val w0 = 0
        val x0 = 0
        val y0 = 0
        val z0 = 0
    """.trimIndent())
    println(message)
}

val regex = "(inp|add|mul|div|mod|eql) ([wxyz])( (-?[wxyz0-9]+))?".toRegex()
