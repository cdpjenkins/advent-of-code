package day11

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.test.Ignore
import kotlin.test.fail

private fun part1(input: List<String>): Int {

    val graph = input.toGraph()

    return countPaths(graph, "you", "out")
}

private fun countPaths(graph: Map<String, List<String>>, startNode: String, endNode: String): Int {
    var paths = listOf(listOf(startNode))
    var completedPaths = emptyList<List<String>>()
    while (paths.isNotEmpty()) {
        val newPaths = paths.flatMap { path ->
            val lastElement = path.last()
            println(lastElement)
            val candidates = graph[lastElement] ?: emptyList()
            candidates.map { path + it }
        }
        val newCompletedPaths = newPaths.filter { it.last() == endNode }
        val newActivePaths = newPaths.filter { it.last() != endNode }

        completedPaths += newCompletedPaths
        paths = newActivePaths
    }
    return completedPaths.size
}

private fun part2(input: List<String>): Int {

    val graph = input.toGraph()

    val tofft = countPaths(graph, "svr", "fft")
    val todac = countPaths(graph, "fft", "dac")
    val toout = countPaths(graph, "dac", "out")

    println(tofft)
    println(todac)
    println(toout)



    return tofft * todac * toout
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

    @Ignore // currently goes forever ...
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day11.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 graphviz`() {
        val input = readInputFileToList("day11.txt")

        val graph = input.toGraph()

        printGraphViz(graph)

        fail()
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
