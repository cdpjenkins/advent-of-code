package day11

fun part1(input: String) =
    input.parse()
        .solve(25)

fun part2(input: String) =
    input.parse()
        .solve(75)

private fun List<Long>.solve(n: Int): Long =
    generateSequence(toFrequencies()) { it.blink() }
        .drop(n)
        .first()
        .values
        .sum()

private fun List<Long>.toFrequencies(multiplier: Long = 1) =
    this.groupingBy { it }
        .eachCount()
        .map { (key, value) -> key to (value*multiplier) }
        .toMap()

private fun Map<Long, Long>.blink(): MutableMap<Long, Long> {
    val newFrequencies = mutableMapOf<Long, Long>()

    this.map { (num, freq) -> num.blink().toFrequencies(multiplier = freq) }
        .forEach {
            it.forEach { (key, value) ->
                newFrequencies[key] = newFrequencies.getOrDefault(key, 0) + value
            }
        }

    return newFrequencies
}

fun String.parse() = split(" ").map { it.toLong() }
private fun Long.hasEvenNumberOfDigits() = this.toString().length % 2 == 0

private fun Long.blink(): List<Long> =
    when {
        this == 0L -> listOf(1)
        this.hasEvenNumberOfDigits() -> this.chopDigitsInHalf()
        else -> listOf(this * 2024)
    }

private fun Long.chopDigitsInHalf(): List<Long> {
    val string = this.toString()
    val length = string.length
    return listOf(
        string.substring(0, length / 2).toLong(),
        string.substring(length / 2, length).toLong()
    )
}
