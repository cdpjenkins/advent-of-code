package day20

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Long {
    val modules = parseModules(input)

    modules.dumpGraph()

    val (h, l) = pulseSequence(modules)
        .take(1000)
        .flatMap { it }
        .fold(Pair<Long, Long>(0, 0)) { (h, l), pulse ->
            if (pulse.type == PulseType.low) {
                Pair(h, l+1)
            } else {
                Pair(h+1, l)
            }
        }

    return h * l
}

private fun Map<String, Module>.dumpGraph() {

    println("digraph ston {")

    this.forEach { (k, v) ->
        val destinations = v.destinations
            .map {
                this[it]?.let { module ->
                    "${module.type}_${module.name}"
                } ?: it
            }
            .joinToString(", ")
        println(" ${v.type}_${v.name} -> { $destinations }")
    }

    println("}")
}

private fun part2(input: List<String>): Long {
    val modules = parseModules(input)

    pulseSequence(modules)
        .withIndex()
        .map { (i, ps) ->
            i to ps.filter {
                it.type == PulseType.high && (it.source == "br" || it.source == "lf" || it.source == "rz" || it.source == "fk")
            }
        }
        .take(5000)
        .filter { (i, ps) -> ps.isNotEmpty()}
        .forEach { (i, ps) ->
            val sources = ps.map { "${it.source} ${it.type}" }

            println("${i + 1} ${sources}")
        }

    // it turns out that these four modules give the input of the conjunction module that feeds the rx module. They
    // give a high pulse every x button presses, where x is one of the numbers below.
    // It turns out that each of the x values is prime so we could have just multiplied them together and saved all the
    // hassle of computing the lcm but hey ho, this is ever so slightly more generalised and applicable to non-prime
    // numbers as well. Assuming we haven't screwed up the lcm calculation, which is entirely possible.
    return lcm(3877, 3911, 4057, 4079)
}

fun gcd(a: Long, b: Long): Long =
    if (b > 0) gcd(b, a % b)
    else a

fun gcd(a: Long, b: Long, c: Long, d: Long): Long {
    return gcd(
        gcd(a, b),
        gcd(c, d)
    )
}

fun lcm(a: Long, b: Long): Long = a * (b / gcd(a, b))

fun lcm(a: Long, b: Long, c: Long, d: Long): Long {
    return a * b * c * d / gcd(a, b, c, d) // hope this actually works innit
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
    abstract val type: String

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
    override val type = "conjunction"

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
    override val type = "flipflop"

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
    override val type = ""

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

fun pulseSequence(modules: Map<String, Module>) =
    sequence {
        val queue = ArrayDeque<Pulse>()

        while(true) {
            queue.addLast(Pulse("button", PulseType.low, "broadcaster"))

            val mutableListOfPulses = mutableListOf<Pulse>()

            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()

                mutableListOfPulses.add(pulse)

                val destination = modules[pulse.destination]

                val moarPulses = destination?.receivePulse(pulse) ?: listOf()

                queue.addAll(moarPulses)
            }

            yield(mutableListOfPulses.toList())
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
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day20.txt")) shouldBe 250924073918341L
    }

    @Test
    fun `simple input does interesting things`() {
        val modules = parseModules(simpleTestInput)

        pulseSequence(modules)
            .take(1)
            .flatMap { it }
            .map { it.toLogString() }
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

        pulseSequence(modules)
            .take(4)
            .flatMap { it }
            .map { it.toLogString() }
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
