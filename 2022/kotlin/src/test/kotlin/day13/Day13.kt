package day13

import FileUtil.readInputFileToList
import ListUtils.splitByBlank
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day13 {
    @Test
    internal fun `part 1 test data`() {
        val input = testInput

        val pairsLists = input.splitByBlank()
        val pairs = pairsLists.map { it.parsePair() }
        val resultsList = pairs.map { (left, right) -> left.compareWith(right) > 0 }
        val indexedSton = resultsList.withIndex().filter { (i, b) -> b }
        val result = indexedSton.map { (i, b) -> i + 1 }.sum()
        result shouldBe 13
    }

    @Test
    internal fun `part 1 real data`() {
        val input = realInput

        val pairsLists = input.splitByBlank()
        val pairs = pairsLists.map { it.parsePair() }
        val resultsList = pairs.map { (left, right) -> left.compareWith(right) > 0 }
        val indexedSton = resultsList.withIndex().filter { (i, b) -> b }
        val result = indexedSton.map { (i, b) -> i + 1 }.sum()
        result shouldBe 5330
    }


    @Test
    internal fun `part 2 test data`() {
        val input = testInput

        val pairsLists = input.splitByBlank()
        val pairs = pairsLists.map { it.parsePair() }

        val separator1 = "[[2]]".parsePacket()
        val separator2 = "[[6]]".parsePacket()

        val flatten = pairs.flatten() + separator1 + separator2

        val mutableList = flatten.toMutableList()

        mutableList.sortWith { lhs: Node, rhs: Node -> lhs.compareWith(rhs) }
        mutableList.reverse()

        val index1 = mutableList.withIndex()
            .find { (i, n) -> n == separator1 }!!
            .index + 1

        val index2 = mutableList.withIndex()
            .find { (i, n) -> n == separator2 }!!
            .index + 1

        val result = index1 * index2

        result shouldBe 140
    }

    @Test
    internal fun `part 2 real data`() {
        val input = realInput

        val pairsLists = input.splitByBlank()
        val pairs = pairsLists.map { it.parsePair() }

        val separator1 = "[[2]]".parsePacket()
        val separator2 = "[[6]]".parsePacket()

        val flatten = pairs.flatten() + separator1 + separator2

        val mutableList = flatten.toMutableList()

        mutableList.sortWith { lhs: Node, rhs: Node -> lhs.compareWith(rhs) }
        mutableList.reverse()

        val index1 = mutableList.withIndex()
            .find { (i, n) -> n == separator1 }!!
            .index + 1

        val index2 = mutableList.withIndex()
            .find { (i, n) -> n == separator2 }!!
            .index + 1

        val result = index1 * index2

        result shouldBe 27648
    }
}

private fun List<String>.parsePair(): List<Node> {
    require(size == 2) { "It's not a pair if it's got ${size} elements: $this" }

    val nodes = this.map { it.parsePacket() }
    return nodes
}

private fun String.parsePacket(): Node {
    var stack = listOf<Node>()
    var remainingInput = this

    while (remainingInput.isNotEmpty()) {
        if (remainingInput.first() == '[') {
            stack = stack + ListStartNode
            remainingInput = remainingInput.drop(1)
        } else if (remainingInput.first() == ',') {
            remainingInput = remainingInput.drop(1)
        } else if (remainingInput.first().isDigit()) {
            val digitsString: String = remainingInput.takeWhile { it.isDigit() }
            val tharInt = digitsString.toInt()
            val tharRest = remainingInput.dropWhile { it.isDigit() }

            val integerNode = IntegerNode(tharInt)
            stack = stack + integerNode

            remainingInput = tharRest
        } else if (remainingInput.first() == ']') {
            remainingInput = remainingInput.drop(1)
            val nodes = stack.takeLastWhile { !(it is ListStartNode) }
            stack = stack.dropLastWhile { !(it is ListStartNode) }
            stack = stack.dropLast(1)
            val listNode = ListNode(nodes)
            stack = stack + listNode
        } else {
            throw IllegalArgumentException(remainingInput)
        }
    }

    if (stack.size != 1) {
        println(stack)
        println(this)
        throw (IllegalArgumentException("$stack"))
    }

    return stack.first()
}

sealed interface Node {
    fun compareWith(right: Node): Int
}

data class IntegerNode(val value: Int) : Node {
    override fun compareWith(right: Node): Int {
        if (right is IntegerNode) {
            return right.value - value
        } else if (right is ListNode) {
            val listMeDo = ListNode(listOf(this))
            return listMeDo.compareWith(right)
        } else {
            throw IllegalArgumentException("$right")
        }
    }
}

data class ListNode(val list: List<Node>) : Node {
    override fun compareWith(right: Node): Int {
        val rhs = if (right is ListNode) right else ListNode(listOf(right))

        if (list.isEmpty()) {
            if (rhs.list.isEmpty()) {
                return 0
            } else {
                return 1
            }
        } else {
            if (rhs.list.isNotEmpty()) {
                val comparison = list.first().compareWith(rhs.list.first())
                if (comparison != 0) {
                    return comparison
                } else {
                    return ListNode(list.drop(1)).compareWith((ListNode(rhs.list.drop(1))))
                }
            } else {
                return -1
            }
        }
    }
}

object ListStartNode : Node {
    override fun compareWith(right: Node): Int {
        throw IllegalArgumentException("this should never get called innit")
    }
}

val testInput =
    """
        [1,1,3,1,1]
        [1,1,5,1,1]

        [[1],[2,3,4]]
        [[1],4]

        [9]
        [[8,7,6]]

        [[4,4],4,4]
        [[4,4],4,4,4]

        [7,7,7,7]
        [7,7,7]

        []
        [3]

        [[[]]]
        [[]]

        [1,[2,[3,[4,[5,6,7]]]],8,9]
        [1,[2,[3,[4,[5,6,0]]]],8,9]
    """.trimIndent().lines()
val realInput = readInputFileToList("day13.txt")
