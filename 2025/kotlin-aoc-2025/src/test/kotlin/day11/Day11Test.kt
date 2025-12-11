package day11

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.test.Ignore

private fun part1(input: List<String>): Long {
    val graph = input.toGraph()

    return graph.countPathsMemoised("you", "out")
}

private fun part2(input: List<String>): Long {
    val graph = input.toGraph()

    val tofft = graph.countPathsMemoised("svr", "fft")
    val todac = graph.countPathsMemoised("fft", "dac")
    val toout = graph.countPathsMemoised("dac", "out")

    return tofft * todac * toout
}

fun Map<String, List<String>>.countPathsMemoised(
    source: String,
    dest: String
): Long {
    val cache = mutableMapOf<String, Long>()

    fun dfs(thisNode: String): Long =
        if (thisNode == dest) {
            1
        } else if (cache.contains(thisNode)) {
            cache[thisNode]!!
        } else {
            (this[thisNode]?.sumOf { dfs(it) } ?: 0)
                .also { cache[thisNode] = it }
        }

    return dfs(source)
}

private fun List<String>.toGraph(): Map<String, List<String>> {
    val graph = associate {
        val (device, outputsString) = it.parseUsingRegex("""^([a-z]+): (.*)$""")

        val outputs = outputsString.split(" ")

        device to outputs
    }
    return graph
}

fun printGraphViz(graph: Map<String, List<String>>) {
    println("digraph {")
    graph.entries.forEach { node -> node.printGraphViz() }
    println("}")
}

private fun Map.Entry<String, List<String>>.printGraphViz() {
    val (node, outputs) = this
    outputs.forEach { output ->
        println("  $node -> $output")
    }
}

class Day11Test {

    @Test
    fun `part 1 with test input`() {
        part1(part1TestInput) shouldBe 5
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day11.txt")) shouldBe 688
    }

    @Test
    fun `part 2 with test input`() {
        part2(part2TestInput) shouldBe 2
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day11.txt")) shouldBe 293263494406608L
    }

    @Ignore
    @Test
    fun `part 2 graphviz`() {
        val graph = readInputFileToList("day11.txt").toGraph()

        printGraphViz(graph)
    }
}

val part1TestInput =
    """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent().lines()

val part2TestInput =
    """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
    """.trimIndent().lines()
