import java.lang.AssertionError

fun main() {
    val input = readInput("Day08")

    println(day08Part1CountDigits(input))
    println(day10Part2RepairIncompleteLines(input))
}

fun day10Part2RepairIncompleteLines(input: List<String>) = input.map { decodeOneLine(it) }.sum()

private fun decodeOneLine(line: String): Int {
    val (signalPatternsString, outputValue) = line.split(" | ")
    val signalPatterns = signalPatternsString.split(" ")

    val mappings = workOutMapping(signalPatterns)

    return outputValue.split(" ")
        .map { unmappedSegments ->
            val mappedSegments = unmappedSegments.map { mappings[it]!!.first() }.toSet()
            segmentsToDigit[mappedSegments]!!
        }
        .joinToString("")
        .toInt()
}

fun workOutMapping(signalPatterns: List<String>): Map<Char, Set<Char>> {
    val signalPatternsSet = signalPatterns.map { it.toSet() }.toSet()

    val frequenciesInInput = signalPatterns
        .joinToString("")
        .groupingBy { it }
        .eachCount()

    val mappings: MutableMap<Char, Set<Char>> = mappingsWithAllPossibilities()
        .narrowBasedOn(frequenciesInInput)
        .narrowBasedOnFindingAIn7ButNot1(signalPatterns)

    if (mappings.isComplete()) {
        return mappings
    } else {
        return guess(mappings, signalPatternsSet)
    }
}

fun guess(mappings: MutableMap<Char, Set<Char>>, signalPatternsSet: Set<Set<Char>>): HashMap<Char, Set<Char>> {
    val (char, possibilities) = mappings.entries
        .filter { it.value.size > 1 }
        .minByOrNull { it.value.size }!!

    for (choice in possibilities) {
        val copyOfMappings = HashMap(mappings)
        copyOfMappings.narrowPossibilities(char, setOf(choice))

        if (copyOfMappings.isComplete()) {
            if (copyOfMappings.isCoherent(signalPatternsSet)) {
                return copyOfMappings
            }
        }

        // strictly speaking, we might need to recursively call this function and potentially make more guesses (and
        // be prepared to backtrack if they don't work out) but it doesn't seem to be required for the input that we
        // get
    }

    throw AssertionError("Oh poo")
}

private fun MutableMap<Char, Set<Char>>.isCoherent(signalPatternsSet: Set<Set<Char>>): Boolean {
    val mappedSignals = signalPatternsSet.map {
        it.map { this[it]!!.first() }.toSet()
    }

    return segmentsFor.values.toSet() == mappedSignals.toSet()
}

val segmentsFor: Map<Int, Set<Char>> = mapOf(
    0 to "abcefg".toSet(),
    1 to "cf".toSet(),
    2 to "acdeg".toSet(),
    3 to "acdfg".toSet(),
    4 to "bcdf".toSet(),
    5 to "abdfg".toSet(),
    6 to "abdefg".toSet(),
    7 to "acf".toSet(),
    8 to "abcdefg".toSet(),
    9 to "abcdfg".toSet(),
)

val segmentsToDigit: Map<Set<Char>, Int> = segmentsFor.entries.associate{(k,v)-> v to k}

private fun MutableMap<Char, Set<Char>>.isComplete() = this.values.all { it.size == 1 }

private fun MutableMap<Char, Set<Char>>.narrowBasedOnFindingAIn7ButNot1(
    signalPatterns: List<String>
): MutableMap<Char, Set<Char>> {
    val charsIn7 = signalPatterns.filter { it.length == 3 }.first().toSet()
    val charsIn1 = signalPatterns.filter { it.length == 2 }.first().toSet()
    val charIn7ButNot1 = charsIn7.subtract(charsIn1).first()
    narrowPossibilities(charIn7ButNot1, setOf('a'))

    return this
}

private fun MutableMap<Char, Set<Char>>.narrowBasedOn(
    frequenciesInInput: Map<Char, Int>
): MutableMap<Char, Set<Char>> {
    frequenciesInInput.forEach { (char, frequency) ->
        val possibilities: Set<Char> = lettersByFrequency[frequency]!!
        narrowPossibilities(char, possibilities)
    }

    return this
}

private fun mappingsWithAllPossibilities() = chars.map { inputChar ->
    inputChar to chars.toSet()
}.toMap().toMutableMap()

private fun MutableMap<Char, Set<Char>>.narrowPossibilities(
    char: Char,
    possibilities: Set<Char>
) {
    if (this[char]!!.size > 1) {
        this[char] = this[char]!!.intersect(possibilities)

        if (this[char]!!.size == 1) {
            val value = this[char]!!.first()

            chars.forEach {
                if (it != char) {
                    narrowPossibilities(it, chars.subtract(setOf(value)))
                }
            }
        }
    }
}

val chars = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g')

val lettersByFrequency = mapOf(
    4 to setOf('e'),
    6 to setOf('b'),
    7 to setOf('d', 'g'),
    8 to setOf('a', 'c'),
    9 to setOf('f')
)

fun day08Part1CountDigits(input: List<String>): Int {
    val ston = input.flatMap {
        val (_, outputValue) = it.split(" | ")

        val numbers = outputValue.split(" ")

        println(numbers)

        numbers.map {
            when (it.length) {
                2 -> 1
                4 -> 4
                3 -> 7
                7 -> 8
                else -> -1
            }
        }
    }

    val numbers = ston
        .filter { it == 1 || it == 4 || it == 7 || it == 8 }

    return numbers.size
}
