package day23

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.RegexUtils.parseUsingRegex
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    return 123
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day23Test {
    @Ignore
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day_template.txt")) shouldBe -1
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
    fun `finds strongly connected components`() {
        val REGEX = """([a-z]{2})-([a-z]{2})""".toRegex()
        val graph = testInput.flatMap {
            val (node1, node2) = it.parseUsingRegex(REGEX)
            listOf(
                node1 to node2,
                node2 to node1
            )
        }.toMap()
        // ^^^^ gaaa no we need a multimap
        // or Map<String, Set<String>> !

        graph.findStronglyConnectComponents()

    }
}

private fun Map<String, String>.findStronglyConnectComponents() {
    // Tarjan's strongly connected components algorithm
    // see https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm



    val s = ArrayDeque<String>()
    val indices = mutableMapOf<String, Int>()
    val lowlinks = mutableMapOf<String, Int>()
    val onStack = mutableSetOf<String>()
    var index = 0


    fun strongConnect(v: String) {
        indices[v] = index
        lowlinks[v] = index
        index++

        s.addLast(v)
        onStack.add(v)

        // Consider successors of v
        // TODO you are here
        // but remember to represent the graph as a multimap first
    }

    this.keys.forEach { v ->
        if (!indices.containsKey(v)) {
            strongConnect(v)
        }
    }


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
