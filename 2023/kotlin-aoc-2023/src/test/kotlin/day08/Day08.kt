package day08

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val (nodes, instructionsSeq) = parse(input)

    val initialNode = nodes["AAA"]!!
    val targetNode = nodes["ZZZ"]!!

    val (numSteps, _) =
        nodeSeq(instructionsSeq, initialNode, nodes)
            .withIndex()
            .find { (i, n) -> n == targetNode }!!

    return numSteps
}

private fun parse(input: List<String>): Pair<Map<String, Node>, Sequence<Char>> {
    val instructions = input.first()
    val nodes = input.drop(2)
        .map { Node.parse(it) }
        .map { it.id to it }
        .toMap()
    val instructionsSeq = generateSequence { instructions.toList() }.flatten()
    return Pair(nodes, instructionsSeq)
}

fun nodeSeq(instructionSeq: Sequence<Char>, initialNode: Node, nodes: Map<String, Node>): Sequence<Node> {
    return sequence {
        var currentNode = initialNode

        instructionSeq.forEach { instruction ->
            yield(currentNode)

            if (instruction == 'L') {
                currentNode = nodes[currentNode.left]!!
            } else if (instruction == 'R') {
                currentNode = nodes[currentNode.right]!!
            } else {
                throw IllegalArgumentException(instruction.toString())
            }
        }
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

data class Node(
    val id: String,
    val left: String,
    val right: String
) {
    companion object {
        val nodeRegex = "([A-Z]{1,3}) = \\(([A-Z]{1,3}), ([A-Z]{1,3})\\)".toRegex()

        fun parse(line: String): Node {
            val (id, left, right) =  line.parseUsingRegex(nodeRegex)
            return Node(id, left, right)
        }
    }
}

class Day08Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 2
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day08.txt")) shouldBe 20777
    }

//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput2) shouldBe 6
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }
}

val testInput =
    """
        RL
        
        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()

val testInput2 =
    """
        LR
        
        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent().lines()
