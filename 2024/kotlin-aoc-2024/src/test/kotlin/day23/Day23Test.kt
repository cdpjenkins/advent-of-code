package day23

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex

private fun part1(input: List<String>) =
    input.parse()
        .findTriples()
        .count { atLeastOneNodeStartsWithLetterT(it) }

private fun part2(input: List<String>) =
    input.parse()
        .findCliques()
        .maxBy { it.size }
        .toList()
        .sorted()
        .joinToString(",")

private fun atLeastOneNodeStartsWithLetterT(it: Triple<String, String, String>) =
    it.first.startsWith("t") ||
            it.second.startsWith("t") ||
            it.third.startsWith("t")

private fun MutableMap<String, Set<String>>.findTriples(): List<Triple<String, String, String>> {
    val triples = keys.flatMap { n1 ->
        val n1Neighbours = getValue(n1) //.filter { it > n1 }
        n1Neighbours.flatMap { n2 ->
            val n2Neighbours = getValue(n2)
            n2Neighbours
                .filter { it in n1Neighbours }
                .map { n3 -> Triple(n1, n2, n3) }
                .filter { it.isInOrder() }
        }
    }
    return triples
}

// I'm not actually sure if "clique" is the right name in graph theory for a subgraph where there exists an edge between
// every pair of nodes in the subgraph. Whatever, that's the name that I'm using here.
private fun Map<String, Set<String>>.findCliques(): MutableSet<Set<String>> {
    var visited = mutableSetOf<String>()
    val subGraphs = mutableSetOf<Set<String>>()

    fun visitMeDo(newNode: String, currentSubgraph: MutableSet<String>) {
        if (newNode !in visited) {
            val neighbours = this.getValue(newNode)
            if (currentSubgraph.all { it in neighbours }) {
                // yay, same clique subgraph thang
                currentSubgraph.add(newNode)
                visited.add(newNode)
                neighbours.forEach { neighbour ->
                    visitMeDo(neighbour, currentSubgraph)
                }
            }
            // else nope, it's not in the same subGraph
        }
    }

    this.keys.forEach { currentNode ->
        visited = mutableSetOf()
        val currentSubgraph = mutableSetOf(currentNode)
        visited.add(currentNode)

        this.getValue(currentNode).forEach { neighbour ->
            if (neighbour !in visited) {
                visitMeDo(neighbour, currentSubgraph)
            }
        }
        subGraphs.add(currentSubgraph)
    }

    return subGraphs
}


private fun List<String>.parse(): MutableMap<String, Set<String>> {
    val REGEX = """([a-z]{2})-([a-z]{2})""".toRegex()
    val tharMap = mutableMapOf<String, Set<String>>().withDefault { setOf() }
    forEach {
        val (node1, node2) = it.parseUsingRegex(REGEX)
        tharMap[node1] = tharMap.getValue(node1) + node2
        tharMap[node2] = tharMap.getValue(node2) + node1
    }
    return tharMap
}


class Day23Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 7
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day23.txt")) shouldBe 1378
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe "co,de,ka,ta"
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day23.txt")) shouldBe "bs,ey,fq,fy,he,ii,lh,ol,tc,uu,wl,xq,xv"
    }
}

private fun Triple<String, String, String>.isInOrder(): Boolean {
    return this.first < this.second  && this.second < this.third
}


val testInput =
    """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent().lines()
