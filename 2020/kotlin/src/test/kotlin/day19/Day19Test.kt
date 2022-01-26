package day19

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput
import utils.splitList
import java.lang.AssertionError

class Day19Test : FunSpec({
    test("part 1 with test data") {
        val (symbols, _) = parse(testInput)

        "ababbb" shouldMatch symbols
        "abbbab" shouldMatch symbols
        "bababa" shouldNotMatch symbols
        "aaabbb" shouldNotMatch symbols
        "aaaabbb" shouldNotMatch symbols
    }

    test("part 1 with real data") {
        val (symbols, messages) = parse(realInput("day19"))

        val count = messages.filter { it.completelyMatches(symbols) }.count()
        count shouldBe 198
    }

    test("part 2 with test data") {
        val (symbols, messages) = parse(part2TestData)

        messages.filter { it.completelyMatches(symbols) }.count() shouldBe 12
    }

    test("part 2 with real data") {
        val (symbols, messages) = parse(realInput("day19-2"))

        messages.filter { it.completelyMatches(symbols) }.count() shouldBe 372
    }

    test("matching terminals") {
        Terminal("a").matches(emptyMap(), "abcdef") shouldBe listOf(Match("bcdef"))
        Terminal("b").matches(emptyMap(), "abcdef") shouldBe listOf()
    }

    test("matching sequences") {
        SequenceSymbol(Terminal("a"), Terminal("b")).matches(emptyMap(), "abcdef") shouldBe listOf(Match("cdef"))
        SequenceSymbol(Terminal("a"), Terminal("b")).matches(emptyMap(), "cdef") shouldBe listOf()
    }

    test("matching either rules") {
        EitherSymbol(Terminal("a"), Terminal("b")).matches(emptyMap(), "abc") shouldBe listOf(Match("bc"))
        EitherSymbol(Terminal("a"), Terminal("b")).matches(emptyMap(), "bcd") shouldBe listOf(Match("cd"))
        EitherSymbol(Terminal("a"), Terminal("b")).matches(emptyMap(), "cde") shouldBe listOf()
    }

    test("matching plus symbol") {
        PlusSymbol(Terminal("a")).matches(emptyMap(), "a") shouldBe listOf(Match(""))
        PlusSymbol(Terminal("a")).matches(emptyMap(), "aa") shouldBe listOf(Match(""), Match("a"))
        PlusSymbol(Terminal("a")).matches(emptyMap(), "aaa") shouldBe listOf(Match(""), Match("a"), Match("aa"))
    }
})


private fun parse(input: List<String>): Pair<Map<String, Symbol>, List<String>> {
    val (rulesList: List<String>, messagesList) = input.splitList { it.isEmpty() }
    return Pair(rulesList.parseSymbols(), messagesList)
}

private infix fun String.shouldMatch(symbols: Map<String, Symbol>) = completelyMatches(symbols) shouldBe true
private infix fun String.shouldNotMatch(symbols: Map<String, Symbol>) = completelyMatches(symbols) shouldBe false

private fun String.completelyMatches(symbols: Map<String, Symbol>): Boolean {
    val matches = symbols["0"]!!.matches(symbols, this)
    return matches.any { it.rest.isEmpty() }
}

private fun List<String>.parseSymbols() = this.map { it.parseRule() }.toMap()

private fun String.parseRule(): Pair<String, Symbol> {
    val (id, subRules) = "^([0-9]+):(.*)$".toRegex().find(this)!!.destructured

    val eitherChildren: List<SequenceSymbol> =
        subRules.trim()
            .split(spacesRegex)
            .splitList { it == "|" }.map { eitherOption ->
                val symbol: List<Symbol> = eitherOption.map { symbol: String ->
                    if (symbol.all { it.isDigit() }) {
                        ReferenceSymbol(symbol)
                    } else if (symbol.first() == '"' && symbol.last() == '"') {
                        Terminal(symbol.drop(1).dropLast(1))
                    } else if (symbol.last() == '+') {
                        PlusSymbol(Terminal(symbol.dropLast(1)))
                    } else {
                        throw AssertionError(symbol)
                    }
                }
                SequenceSymbol(symbol)
            }

    return id to EitherSymbol(eitherChildren)
}

// Best hacky attempt at a recursive-descent parser follows. This would not work if the grammar was left-recursive.
sealed interface Symbol {
    fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match>
}

data class ReferenceSymbol(val reference: String) : Symbol {
    override fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match> {
        return symbols[reference]!!.matches(symbols, str)
    }
}

data class Terminal(val symbolStr: String) : Symbol {
    override fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match> {
        if (str.startsWith(symbolStr)) {
            return listOf(Match(str.removePrefix(symbolStr)))
        } else {
            return listOf()
        }
    }
}

data class SequenceSymbol(val children: List<Symbol>) : Symbol {
    constructor(vararg symbols: Symbol) : this(symbols.asList())

    override fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match> {
        if (this.children.isEmpty()) {
            return listOf(Match(str))
        }
        val matches: Iterable<Match> = this.children.first().matches(symbols, str)

        val subMatches = matches.flatMap { match: Match ->
            SequenceSymbol(children.drop(1)).matches(symbols, match.rest)
        }

        return subMatches
    }
}

data class EitherSymbol(val children: List<Symbol>) : Symbol {
    constructor(vararg ston: Symbol) : this(ston.asList())

    override fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match> {
        return children.flatMap { it.matches(symbols, str) }
    }
}

data class PlusSymbol(val symbol: Symbol, val max: Int = 90) : Symbol {
    override fun matches(symbols: Map<String, Symbol>, str: String): Iterable<Match> {
        if (max <= 0) {
            return listOf()
        } else {
            val firstMatch: Iterable<Match> = symbol.matches(symbols, str)
            if (firstMatch.count() == 0) {
                return listOf()
            } else {
                return PlusSymbol(symbol, max - 1).matches(symbols, str.drop(1)) + firstMatch
            }
        }
    }
}

data class Match(val rest: String)

val spacesRegex = "\\s".toRegex()

val testInput =
    """
        0: 4 1 5
        1: 2 3 | 3 2
        2: 4 4 | 5 5
        3: 4 5 | 5 4
        4: "a"
        5: "b"

        ababbb
        bababa
        abbbab
        aaabbb
        aaaabbb
    """.trimIndent().lines()

val part2TestData =
    """
        42: 9 14 | 10 1
        9: 14 27 | 1 26
        10: 23 14 | 28 1
        1: "a"
        11: 42 31 | 42 11 31
        5: 1 14 | 15 1
        19: 14 1 | 14 14
        12: 24 14 | 19 1
        16: 15 1 | 14 14
        31: 14 17 | 1 13
        6: 14 14 | 1 14
        2: 1 24 | 14 4
        0: 8 11
        13: 14 3 | 1 12
        15: 1 | 14
        17: 14 2 | 1 7
        23: 25 1 | 22 14
        28: 16 1
        4: 1 1
        20: 14 14 | 1 15
        3: 5 14 | 16 1
        27: 1 6 | 14 18
        14: "b"
        21: 14 1 | 1 14
        25: 1 1 | 1 14
        22: 14 14
        8: 42 | 42 8
        26: 14 22 | 1 20
        18: 15 15
        7: 14 5 | 1 21
        24: 14 1

        abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
        bbabbbbaabaabba
        babbbbaabbbbbabbbbbbaabaaabaaa
        aaabbbbbbaaaabaababaabababbabaaabbababababaaa
        bbbbbbbaaaabbbbaaabbabaaa
        bbbababbbbaaaaaaaabbababaaababaabab
        ababaaaaaabaaab
        ababaaaaabbbaba
        baabbaaaabbaaaababbaababb
        abbbbabbbbaaaababbbbbbaaaababb
        aaaaabbaabaaaaababaa
        aaaabbaaaabbaaa
        aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
        babaaabbbaaabaababbaabababaaab
        aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
    """.trimIndent().lines()
