package day22

fun part1(input: List<String>): Long {
    return input
        .map { it.toInt() }
        .sumOf { find2000thSecret(it).toLong() }
}

fun part2(input: List<String>): Int {
    val bananasBySeq = mutableMapOf<List<Int>, Int>().withDefault { 0 }
    input.priceLists().forEach { findSeqsAndUpdateBananas(it, bananasBySeq) }

    return bananasBySeq.values.max()
}

fun findSeqsAndUpdateBananas(prices: List<Int>, bananasBySeq: MutableMap<List<Int>, Int>) {
    val seenSequences = mutableSetOf<List<Int>>()

    prices.differences().windowed(4, 1).forEachIndexed { i, it ->
        if (it !in seenSequences) {
            seenSequences.add(it)
            bananasBySeq.put(it, bananasBySeq.getValue(it) + prices[i + 4])
        }
    }
}

fun List<Int>.findIndexOf(listOf: List<Int>) =
    this.windowed(4, 1)
        .withIndex()
        .firstOrNull { (_, l) -> l == listOf }
        ?.let { it.index + listOf.size - 1}

fun List<String>.priceLists() =
    this.map { it.toInt() }
        .map { priceList(it) }

private fun priceList(secretNumber: Int) =
    priceSequence(secretNumber)
    .take(2001)       // the initial secret forms the first element in the sequence so we need 2001 elements in total
    .toList()


fun find2000thSecret(secret: Int): Int {
    return secretSequence(secret)
        .drop(2000)
        .first()
}

fun Int.price() = this % 10
fun secretSequence(initialSecret: Int) = generateSequence(initialSecret) { nextSecretNumber(it) }
fun priceSequence(initialSecret: Int) = secretSequence(initialSecret).map { it.price() }
fun List<Int>.differences() = zipWithNext().map { (a, b) -> b - a }

fun nextSecretNumber(secret: Int): Int {
    val step1 = (secret.shl(6) xor secret) and 0xFFFFFF
    val step2 = (step1.shr(5) xor step1) and 0xFFFFFF
    val step3 = (step2.shl(11) xor step2) and 0xFFFFFF

    return step3
}
