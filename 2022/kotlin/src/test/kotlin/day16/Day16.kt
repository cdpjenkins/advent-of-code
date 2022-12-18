package day16

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Day16 {

    @Disabled
    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val valves =
            parseInputToValves(input)

//        valves.toDigraph()

        val state = State(valves.keys.first(), valvesOpen = valves.values.filter { it.flowRate == 0 }.map { it.id }.toSet())

        val walk = walk(state, valves, mutableMapOf<State, Int>())

        println(walk)

        walk shouldBe 1651
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val valves =
            parseInputToValves(input)
        val state = State(valves.keys.first(), valvesOpen = valves.values.filter { it.flowRate == 0 }.map { it.id }.toSet())

        println("max heap: ${Runtime.getRuntime().maxMemory()}")

        val walk = walk(state, valves, mutableMapOf<State, Int>())

        println(walk)

        walk shouldBe 1651
    }
}

//private fun Map<String, Valve>.toDigraph() {
//
//}
//
//data class

fun walk(state: State, valves: Map<String, Valve>, memoMeDo: MutableMap<State, Int>): Int {
    if (state.secondsRemaining == 0) {
        return state.flow
    }

    val memoisedAnswer = memoMeDo[state]
    if (memoisedAnswer != null) {
        return memoisedAnswer
    }

    val stayStill = listOf(state.stayStill())
    val openCurrentValve =
        if (!(state.currentValve in state.valvesOpen)) {
            listOf(state.openCurrentValve(valves))
        } else {
            emptyList()
        }
    val moves = valves[state.currentValve]!!.tunnels.map { state.moveTo(it) }

    val possibleScores = (stayStill + openCurrentValve + moves).map { walk(it, valves, memoMeDo) }

    val max = possibleScores.max()
    memoMeDo[state] = max

    if (memoMeDo.size % 10000 == 0) {
        println(memoMeDo.size)
    }

    return max
}


fun Map<String, Valve>.toDot() {
    val nodes = this.values.map {
        """  "${it.id}" [label="${it.id} ${it.flowRate}" fillcolor="${it.toColour()}" ]"""
    }.joinToString("\n")

    val ston = this.map { (id, valve) ->
        val tunnelsString =
            valve.tunnels.map { """"$it"""" }
            .joinToString(" ")

        """  "$id" -> {${tunnelsString}}"""
    }.joinToString("\n")

    println(
        """
        digraph ston {
            node [style=filled]
        """.trimMargin())
    println(nodes)
    println(ston)
    println("}")
}

private fun Valve.toColour(): String {
    return if (flowRate == 0) "#ffffff" else "#ffaaaa"
}

//fun main() {
//    val vales = parseInputToValves(realInput)
//
//    vales.toDot()
//}

private fun parseInputToValves(input: List<String>) = input
    .map { Valve.parse(it) }
    .map { it.id to it }
    .toMap()

data class State(
    val currentValve: String,
    val valvesOpen: Set<String> = emptySet(),
    val flow: Int = 0,
    val secondsRemaining: Int = 30
) {
    fun stayStill(): State {
        return this.copy(
            secondsRemaining = secondsRemaining - 1,
        )
    }

    fun openCurrentValve(valves: Map<String, Valve>): State {
        return this.copy(
            valvesOpen = valvesOpen + currentValve,
            flow = flow + valves[currentValve]!!.flowRate * (secondsRemaining - 1),
            secondsRemaining = secondsRemaining - 1
        )
    }

    fun moveTo(toValve: String): State {
        return this.copy(
            currentValve = toValve,
            secondsRemaining = secondsRemaining - 1
        )
    }
}

data class Valve(
    val id: String,
    val flowRate: Int,
    val tunnels: List<String>
) {
    companion object {
        fun parse(it: String): Valve {
            val (id, flowRateString, tunnelsString) = it.parseUsingRegex(VALVE_REGEX)
            val tunnels = tunnelsString.split(", ")

            return Valve(id, flowRateString.toInt(), tunnels)
        }
    }
}

val VALVE_REGEX =
    "^Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z]{2}(?:, [A-Z]{2})*)$".toRegex()


val testInput =
    """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent().lines()

val realInput = readInputFileToList("day16.txt")