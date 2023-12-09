package day08

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val (nodes, instructions) = parse(input)

    return nodes.stepsToTarget(instructions, "AAA") { it == "ZZZ" }
}

private fun part2(input: List<String>): Long {
    val (nodes, instructions) = parse(input)

    return nodes.keys.filter { it.endsWith("A") }
        .map { node -> nodes.stepsToTarget(instructions, node) { it.endsWith("Z") } }
        .map { it.toLong() }
        .reduce { acc, it -> lcm(acc, it) }
}

fun Map<String, Node>.stepsToTarget(instructions: String, initialNodeId: String, targetPredicate: (String) -> Boolean): Int {
    val initialNode = this[initialNodeId]!!

    val (numSteps, _) =
        nodeSeq(instructions.seq(), initialNode, this)
            .withIndex()
            .find { (_, n) -> targetPredicate(n.id) }!!

    return numSteps
}

fun gcd(a: Long, b: Long): Long =
    if (b > 0) gcd(b, a % b)
    else a

fun lcm(a: Long, b: Long): Long = a * (b / gcd(a, b))

private fun String.seq(): Sequence<Char> = generateSequence { toList() }.flatten()

private fun parse(input: List<String>): Pair<Map<String, Node>, String> {
    val instructions = input.first()
    val nodes = input.drop(2)
        .map { Node.parse(it) }
        .map { it.id to it }
        .toMap()
    return Pair(nodes, instructions)
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

data class Node(
    val id: String,
    val left: String,
    val right: String,
) {
    companion object {
        val nodeRegex = "([A-Z0-9]{1,3}) = \\(([A-Z0-9]{1,3}), ([A-Z0-9]{1,3})\\)".toRegex()

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

    @Test
    fun `part 2 with test input`() {
        part2(testInput2) shouldBe 6
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day08.txt")) shouldBe 13289612809129L
    }

    @Test
    fun `can compute gcd of two numbers`() {
        gcd(16, 12) shouldBe 4
        gcd(99, 15) shouldBe 3
    }

    @Test
    fun `can compute lcm of two numbers`() {
        lcm(12, 8) shouldBe 24
        // etc
    }
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
