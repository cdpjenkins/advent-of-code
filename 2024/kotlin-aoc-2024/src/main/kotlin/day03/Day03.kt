package day03

import RegexUtils.parseUsingRegex

fun part1(input: String) =
    """mul\((\d+),(\d+)\)""".toRegex().findAll(input)
        .map { executeMul(it.value) }
        .sum()

fun part2(input: String): Int {
    val matches = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)""".toRegex().findAll(input)

    // urgh horrible imperative code, need to make this more functional
    var enabled = true
    var accum = 0
    matches.forEach {
        val command = it.value

        if (command == "do()") {
            enabled = true
        } else if (command == "don't()") {
            enabled = false
        } else {
            if (enabled) {
                accum += executeMul(command)
            }
        }
    }

    return accum
}

private fun executeMul(it: String): Int {
    val (lhs, rhs) = it.parseUsingRegex("""mul\((\d+),(\d+)\)""".toRegex())

    return lhs.toInt() * rhs.toInt()
}
