package day16

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines

class Day16Test : FunSpec({
    test("day 16 part 1 with test input") {
        sumOfInvalidValuesInNearbyTickets(testInput) shouldBe 71
    }

    test("day 16 part 1 with real input") {
        sumOfInvalidValuesInNearbyTickets(realInput) shouldBe 25916
    }

    test("day 16 part 2 with real input") {
        productOfDepartureFields(realInput) shouldBe 2564529489989L
    }
})

private fun productOfDepartureFields(input: List<String>): Long {
    val (fieldDefinitions, yourTicket, nearbyTickets) = input.parse()

    val validNearbyTickets = nearbyTickets.filter { it.isValidTicket(fieldDefinitions) }
    val mappings = solveAndReturnMappings(validNearbyTickets, fieldDefinitions)
    val mappingsForJustDepartureFields =
        mappings.filter { fieldDefinitions[it.value].name.startsWith("departure") }.toMap()

    return yourTicket.withIndex()
        .filter { (i, _) -> i in mappingsForJustDepartureFields.keys }
        .map { (_, v) -> v }
        .fold(1L) { acc, value -> acc * value }
}

private fun solveAndReturnMappings(
    validNearbyTickets: List<List<Int>>,
    fieldDefinitions: List<Rule>
): Map<Int, Int> =
    allMappingPossibilities(fieldDefinitions).solve(validNearbyTickets, fieldDefinitions)!!
        .map { it.first() }
        .withIndex()
        .map { (fieldInTicket, fieldIndex) -> fieldInTicket to fieldIndex }
        .toMap()

private fun allMappingPossibilities(fieldDefinitions: List<Rule>) =
    List(fieldDefinitions.count()) { fieldDefinitions.indices.toSet() }

private fun List<Set<Int>>.solve(
    validNearbyTickets: List<List<Int>>,
    rules: List<Rule>
): List<Set<Int>>? =
    toMutableList()
        .eliminateObviouslyImpossible(validNearbyTickets, rules)
        .let {
            if (it.isSolved()) it
            else if (it.isContradiction()) null
            else it.makeGuessesAndSolve(validNearbyTickets, rules)
        }

private fun MutableList<Set<Int>>.makeGuessesAndSolve(
    validNearbyTickets: List<List<Int>>,
    rules: List<Rule>
): List<Set<Int>> {
    val unsolvedTicketFieldWithFewestRemainingPossibilities = withIndex()
        .filter { (_, s) -> s.count() != 1 }
        .minByOrNull { (_, s) -> s.count() }!!.index

    return this[unsolvedTicketFieldWithFewestRemainingPossibilities].map { guessedFieldIndex ->
        makeGuessAndTryToSolve(
            unsolvedTicketFieldWithFewestRemainingPossibilities,
            guessedFieldIndex,
            validNearbyTickets,
            rules
        )
    }.filterNotNull().first()
}

private fun MutableList<Set<Int>>.makeGuessAndTryToSolve(
    unsolvedTicketFieldWithFewestPossibilities: Int,
    guessedFieldIndex: Int,
    validNearbyTickets: List<List<Int>>,
    rules: List<Rule>
): List<Set<Int>>? =
    this.guess(unsolvedTicketFieldWithFewestPossibilities, guessedFieldIndex)
        .solve(validNearbyTickets, rules)

private fun MutableList<Set<Int>>.guess(fieldInTicket: Int, guessedFieldDefinitionEndex: Int): MutableList<Set<Int>> =
    toMutableList().set(fieldInTicket, guessedFieldDefinitionEndex)
fun List<Set<Int>>.isSolved() = all { it.count() == 1 }
fun MutableList<Set<Int>>.isContradiction() = any { it.isEmpty() }

private fun MutableList<Set<Int>>.eliminateObviouslyImpossible(
    validNearbyTickets: List<List<Int>>,
    rules: List<Rule>
): MutableList<Set<Int>> {
    validNearbyTickets.forEach { ticket ->
        ticket.withIndex().forEach { (indexInTicket, field) ->
            rules.withIndex().forEach { (indexInRule, rule) ->
                if (!rule.validate(field)) {
                    this[indexInTicket] = this[indexInTicket] - indexInRule
                    if (this[indexInTicket].count() == 1) {
                        set(indexInTicket, this[indexInTicket].first())
                    }
                }
            }
        }
    }
    return this
}

private fun MutableList<Set<Int>>.set(
    indexInTicket: Int,
    indexInRules: Int
): MutableList<Set<Int>> {
    this[indexInTicket] = setOf(indexInRules)
    this.withIndex().forEach { (i, p) ->
        if (i != indexInTicket) {
            this[i] = p - indexInRules
        }
    }

    return this
}

private fun List<Int>.isValidTicket(rules: List<Rule>) = this.all { it.isValidAccordingToAtLeastOneRule(rules) }
private fun Int.isValidAccordingToAtLeastOneRule(rules: List<Rule>) = rules.filter { it.validate(this) }.count() > 0

private fun sumOfInvalidValuesInNearbyTickets(input: List<String>): Int {
    val (rules, _, nearbyTickets) = input.parse()
    return nearbyTickets
        .flatten()
        .filter { !it.isValidAccordingToAtLeastOneRule(rules) }
        .sum()
}

private fun List<String>.parse() =
    InputData(
        rules = this.takeWhile { it.isNotEmpty() }.map { it.parseRule() },
        yourTicket = getSection("your ticket:").first().parseTicket(),
        nearbyTickets = this.getSection("nearby tickets:").map { it.parseTicket() }
    )

private fun List<String>.getSection(header: String) =
    dropWhile { it != header }
        .dropWhile { it == header }
        .takeWhile { it.isNotEmpty() }

private fun String.parseTicket() = split(",").map { it.toInt() }

private fun String.parseRule(): Rule {
    val (name, min1String, max1String, min2String, max2String) =
        "([a-z ]+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)"
            .toRegex()
            .find(this)!!
            .destructured
    return Rule(name, min1String.toInt(), max1String.toInt(), min2String.toInt(), max2String.toInt())
}

data class InputData(
    val rules: List<Rule>,
    val yourTicket: List<Int>,
    val nearbyTickets: List<List<Int>>
)

data class Rule(val name: String, val min1: Int, val max1: Int, val min2: Int, val max2: Int) {
    fun validate(field: Int) = (min1..max1).contains(field) || (min2..max2).contains(field)
}

val testInput = """
            class: 1-3 or 5-7
            row: 6-11 or 33-44
            seat: 13-40 or 45-50

            your ticket:
            7,1,14

            nearby tickets:
            7,3,47
            40,4,50
            55,2,20
            38,6,12
        """.trimIndent().lines()

val realInput = readLines("day16")
