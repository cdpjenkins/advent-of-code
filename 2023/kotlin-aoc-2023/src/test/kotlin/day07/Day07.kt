package day07

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>): Int =
    input.map { HandAndBid.of(it) }
        .sortedWith(ComparingByPart1Rules)
        .withIndex()
        .sumOf { (i, hand) -> (i + 1) * hand.bid }

object ComparingByPart1Rules : Comparator<HandAndBid> {
    override fun compare(o1: HandAndBid, o2: HandAndBid): Int {
        val comparedTypes = o1.hand.type.compareTo(o2.hand.type)
        if (comparedTypes != 0) return comparedTypes

        return compareLists(o1.hand.hand.map { valueOf(it) }, o2.hand.hand.map { valueOf(it) })
    }

    fun compareLists(list1: List<Int>, list2: List<Int>): Int {
        require(list1.size == list2.size)

        for (i in (0 until list1.size)) {
            val diff = list1[i] - list2[i]
            if (diff != 0) return diff
        }

        return 0
    }

    private fun valueOf(card: Char): Int {
        return when (card) {
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'T' -> 10
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> throw IllegalArgumentException(card.toString())
        }
    }
}

@JvmInline
value class Hand(val hand: String) {
    val frequencies: Map<Char, Int> get() = hand.groupingBy { it }.eachCount()
    val frequencyValues: List<Int> get() = frequencies.values.toList().sorted()
    val type: Type get() = Type.entries.first { it.matches(this) }
}

data class HandAndBid(
    val hand: Hand,
    val bid: Int = 0,
) {
    companion object {
        val handRegex = "^([AKQJT98765432]{5}) (\\d+)$".toRegex()

        fun of(inputString: String): HandAndBid {

            val (handString, bidString) = inputString.parseUsingRegex(handRegex)

            return HandAndBid(Hand(handString), bidString.toInt())
        }
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

enum class Type {
    HIGH_CARD {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(1, 1, 1, 1, 1)
    },
    ONE_PAIR {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(1, 1, 1, 2)
    },
    TWO_PAIR {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(1, 2, 2)
    },
    THREE_OF_A_KIND {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(1, 1, 3)
    },
    FULL_HOUSE {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(2, 3)
    },
    FOUR_OF_A_KIND {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(1, 4)

    },
    FIVE_OF_A_KIND {
        override fun matches(hand: Hand) = hand.frequencyValues == listOf(5)
    };

    abstract fun matches(hand: Hand): Boolean
}

class Day07Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 6440
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day07.txt")) shouldBe 253954294
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
    fun `correctly derives type of hand`() {
        Hand("KKKKK").type shouldBe Type.FIVE_OF_A_KIND
        Hand("KKKKQ").type shouldBe Type.FOUR_OF_A_KIND
        Hand("KKKQJ").type shouldBe Type.THREE_OF_A_KIND
        Hand("KAKQQ").type shouldBe Type.TWO_PAIR
        Hand("234QQ").type shouldBe Type.ONE_PAIR
        Hand("23456").type shouldBe Type.HIGH_CARD
    }

    @Test
    fun `KK677 beats KTJJT`() {
        ComparingByPart1Rules.compare(HandAndBid(Hand("KK677")), HandAndBid(Hand("KTJJT"))) shouldBeGreaterThan 0
    }
}

val testInput =
    """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent().lines()
