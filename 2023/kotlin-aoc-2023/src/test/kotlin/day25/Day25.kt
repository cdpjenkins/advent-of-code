package day25

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1_testInput(input: List<String>): Int {
    val graph = input.parse()

//    println("original graph:")
//    graph.dumpAsGraphviz()

    val graphAllCutUp = graph.removeEdges(
        listOf(
            "jqt" to "nvd",
            "hfx" to "pzl",
            "bvb" to "cmg"
        )
    )

//    println("all cut up:")
//    graphAllCutUp.dumpAsGraphviz()

    val component1 = graphAllCutUp.findConnectedComponent("jqt")
    val component2 = graphAllCutUp.findConnectedComponent(("nvd"))

    val result = component1.size * component2.size

    return result
}
private fun part1_realInput(input: List<String>): Int {
    val graph = input.parse()

//    println("original graph:")
//    graph.dumpAsGraphviz()

    // Edges to cut found (painfully) by looking at Graphviz svg output
    // Note that the output was sufficiently unclear that I cut several edges in each case until I got the right one
    val graphAllCutUp = graph.removeEdges(
        listOf(
            "ttv" to "sbv",
            "ttv" to "ztc",
            "ttv" to "sbf",
            // not znr

            "rpd" to "bnv",
            "rpd" to "hzx",
            "rpd" to "lss",
            "rpd" to "ppd",
            // not jvp

            "vfh" to "jdv",
            "vfh" to "bdj",
            "vfh" to "mmc",
            "vfh" to "mqv"
            // not fbt
        )
    )

//    println("all cut up:")
//    graphAllCutUp.dumpAsGraphviz()

    val component1 = graphAllCutUp.findConnectedComponent("rhg")
    val component2 = graphAllCutUp.findConnectedComponent("xsj")

    val result = component1.size * component2.size

    return result
}

private fun Map<String, Set<String>>.findConnectedComponent(node: String): Set<String> {
    val visited = mutableSetOf<String>()
    val toVisit = mutableListOf<String>(node)

    while (toVisit.isNotEmpty()) {
        val thisNode = toVisit.removeLast()
        val neighbours = this[thisNode]!!

        neighbours.forEach {
            if (it !in visited) {
                toVisit.add(it)
            }
        }

        visited.add(thisNode)
    }

    return visited.toSet()
}

fun Map<String, Set<String>>.removeEdges(edges: List<Pair<String, String>>): Map<String, Set<String>> {
    val mutableMap = this.toMutableMap()

    edges.forEach { (node1, node2) ->
        mutableMap.removeDirectedEdge(node1, node2)
        mutableMap.removeDirectedEdge(node2, node1)
    }

    return mutableMap.toMap()
}

private fun MutableMap<String, Set<String>>.removeDirectedEdge(
    node1: String,
    node2: String
) {
    val targets = this[node1]!!
    this[node1] = targets - node2
}

private fun Map<String, Set<String>>.dumpAsGraphviz() {
    println("graph G {")
    val sourceNodesAlreadyDone = mutableSetOf<String>()

    this.forEach { (sourceNode, v) ->
        v.forEach { targetNode ->
            if (targetNode !in sourceNodesAlreadyDone) {
                println("  ${sourceNode} -- ${targetNode}")
            }
        }
        sourceNodesAlreadyDone.add(sourceNode)
    }
    println("}")
}

private fun List<String>.parse(): Map<String, Set<String>> {
    val pairs = flatMap { it.parseComponent() }

    val groupBy =
        pairs.groupBy { it -> it.first }
            .map { (k, v) ->
                k to v.map { it.second }.toSet()
            }.toMap()

    return groupBy
}

private fun String.parseComponent(): List<Pair<String, String>> {
    val (component, componentList) = this.split(":")

    val rhsComponents = componentList.trim().split(" ")

    return rhsComponents.flatMap {
        listOf(
            component to it,
            it to component
        )
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day25Test {
    @Test
    fun `part 1 with test input`() {
        part1_testInput(testInput) shouldBe 54
    }

    @Test
    fun `part 1 with real input`() {
        part1_realInput(readInputFileToList("day25.txt")) shouldBe 514786
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }

    @Test
    fun `can parse component lists`() {
        val input = """
            abc: def ghi
            zyx: wvu abc
        """.trimIndent().lines()

        val graph = input.parse()

        graph shouldBe mapOf(
            "abc" to setOf("def", "ghi", "zyx"),
            "zyx" to setOf("wvu", "abc"),
            "def" to setOf("abc"),
            "ghi" to setOf("abc"),
            "wvu" to setOf("zyx")
        )
    }

    @Test
    fun `can remove edges`() {
        val input = """
            abc: def ghi
            zyx: wvu abc
        """.trimIndent().lines()

        val graph = input.parse()

        graph.removeEdges(listOf("abc" to "zyx")) shouldBe mapOf(
            "abc" to setOf("def", "ghi"),
            "zyx" to setOf("wvu"),
            "def" to setOf("abc"),
            "ghi" to setOf("abc"),
            "wvu" to setOf("zyx")
        )
    }

}

val testInput =
    """
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
    """.trimIndent().lines()
