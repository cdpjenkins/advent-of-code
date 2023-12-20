package day20

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Long {
    val modules = parseModules(input)

    val (h, l) = pulseSequence(modules, numButtonPresses = 1000)
        .fold(Pair<Long, Long>(0, 0)) { (h, l), pulse ->
            if (pulse.type == PulseType.low) {
                Pair(h, l+1)
            } else {
                Pair(h+1, l)
            }
        }

    return h * l
}

private fun part2(input: List<String>): Int {
    return 123
}

enum class PulseType {
    low, high;
}

data class Pulse(val source: String, val type: PulseType, val destination: String) {
    fun toLogString(): String {
        return "${source} -${type}-> ${destination}"
    }
}

sealed class Module(
    val name: String,
    val destinations: List<String>
) {
    fun receivePulse(pulse: Pulse): List<Pulse> {
        val pulse = Pulse(pulse.source, pulse.type, this.name)

        val newPulses = internalReceivePulse(pulse)

        return newPulses?.let {pulseType ->
            destinations.map { destination ->
                Pulse(this.name, pulseType, destination)
            }
        } ?: listOf()
    }

    abstract fun internalReceivePulse(
        pulse: Pulse
    ): PulseType?

    companion object {
        val regex = "^([%&]?)([a-z]+) -> ([a-z, ]+)$".toRegex()
        fun of(input: String): Module {
            val (type, name, destinationsString) = input.parseUsingRegex(regex)

            val destinations = destinationsString.split(", ")

            return if (type == "" && name == "broadcaster") {
                Broadcaster(destinations)
            } else if (type == "%") {
                FlipFlop(name, destinations)
            } else if (type =="&") {
                Conjunction(name, destinations)
            } else {
                throw IllegalArgumentException(input)
            }
        }
    }
}

class Conjunction(
    name: String,
    destinations: List<String>
) : Module(name, destinations) {
    val receivedFromInputs: MutableMap<String, PulseType> = mutableMapOf()

    override fun internalReceivePulse(pulse: Pulse): PulseType? {
        receivedFromInputs[pulse.source] = pulse.type

        return if (receivedFromInputs.all { (k, v) -> v == PulseType.high }) {
            PulseType.low
        } else {
            PulseType.high
        }
    }

    fun addSource(sourceName: String) {
        receivedFromInputs[sourceName] = PulseType.low
    }
}

enum class FlipFlopState {
    ON, OFF;

    fun flip(): FlipFlopState =
        when (this) {
            ON -> OFF
            OFF -> ON
        }
}

class FlipFlop(
    name: String,
    destinations: List<String>,
    var state: FlipFlopState = FlipFlopState.OFF
) : Module(name, destinations) {
    override fun internalReceivePulse(pulse: Pulse): PulseType? {
        return when (pulse.type) {
            PulseType.high -> null
            PulseType.low -> {
                val oldState = state
                state = state.flip()

                when (oldState) {
                    FlipFlopState.ON -> PulseType.low
                    FlipFlopState.OFF -> PulseType.high
                }
            }
        }
    }
}

class Broadcaster(destinations: List<String>) : Module("broadcaster", destinations) {
    override fun internalReceivePulse(pulse: Pulse) = pulse.type
}

fun parseModules(input: List<String>): Map<String, Module> {
    val modules = input.map {
        val module = Module.of(it)
        module.name to module
    }.toMap()

    modules.forEach { (targetName, targetModule) ->
        if (targetModule is Conjunction) {
            val sourceModulesSendingToThisModule = modules.filter { (sourceName, sourceModule) ->
                targetName in sourceModule.destinations
            }

            sourceModulesSendingToThisModule.forEach {(sourceName, _) ->
                targetModule.addSource(sourceName)
            }
        }
    }

    return modules
}

fun pulseSequence(modules: Map<String, Module>, numButtonPresses: Int = 1) =
    sequence {
        val queue = ArrayDeque<Pulse>()

        for (i in (0 until numButtonPresses)) {
            queue.addLast(Pulse("button", PulseType.low, "broadcaster"))

            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()

                yield(pulse)

                val destination = modules[pulse.destination]

                val moarPulses = destination?.receivePulse(pulse) ?: listOf()

                queue.addAll(moarPulses)
            }
        }
    }

class Day20Test {

    @Test
    fun `part 1 with simple test input`() {
        part1(simpleTestInput) shouldBe 32000000L
    }

    @Test
    fun `part 1 with less simple test input`() {
        part1(testInput2) shouldBe 11687500
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day20.txt")) shouldBe 817896682L
    }
//
//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }

    @Test
    fun `simple input does interesting things`() {
        val modules = parseModules(simpleTestInput)

        pulseSequence(modules).map { it.toLogString() }
            .joinToString("\n") shouldBe
                """
                    button -low-> broadcaster
                    broadcaster -low-> a
                    broadcaster -low-> b
                    broadcaster -low-> c
                    a -high-> b
                    b -high-> c
                    c -high-> inv
                    inv -low-> a
                    a -low-> b
                    b -low-> c
                    c -low-> inv
                    inv -high-> a
                """.trimIndent()
    }

    @Test
    fun `less simple input does more interesting things`() {
        val modules = parseModules(testInput2)

        pulseSequence(modules, numButtonPresses = 4).map { it.toLogString() }
            .joinToString("\n") shouldBe
                """
                    button -low-> broadcaster
                    broadcaster -low-> a
                    a -high-> inv
                    a -high-> con
                    inv -low-> b
                    con -high-> output
                    b -high-> con
                    con -low-> output
                    button -low-> broadcaster
                    broadcaster -low-> a
                    a -low-> inv
                    a -low-> con
                    inv -high-> b
                    con -high-> output
                    button -low-> broadcaster
                    broadcaster -low-> a
                    a -high-> inv
                    a -high-> con
                    inv -low-> b
                    con -low-> output
                    b -low-> con
                    con -high-> output
                    button -low-> broadcaster
                    broadcaster -low-> a
                    a -low-> inv
                    a -low-> con
                    inv -high-> b
                    con -high-> output
                """.trimIndent()
    }
}

val simpleTestInput =
    """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
    """.trimIndent().lines()

val testInput2 =
    """
        broadcaster -> a
        %a -> inv, con
        &inv -> b
        %b -> con
        &con -> output
    """.trimIndent().lines()
