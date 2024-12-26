package day19

import utils.ListUtils.splitByBlank

fun part1(input: List<String>): Int {
    val (availableTowelsLines, designs) = input.splitByBlank()
    val availableTowels = availableTowelsLines[0].split(", ")

    val towelsRegex = ("(" + availableTowels.joinToString("|") + ")+").toRegex()

    return designs.count { towelsRegex.matches(it) }
}

fun part2(input: List<String>): Long {
    val (availableTowelsLines, designs) = input.splitByBlank()
    val availableTowels = availableTowelsLines[0].split(", ")

    return designs.sumOf { solveUsingDFS(it, availableTowels) }
}

private fun solveUsingDFS(
    design: String,
    availableTowels: List<String>
): Long {
    val cache = mutableMapOf<String, Long>()

    fun towelCombinations(design: String, availableTowels: List<String>): Long =
        cache[design]
            ?: if (design.isEmpty()) 1
            else availableTowels
                .filter { design.startsWith(it) }
                .sumOf { towelCombinations(design.removePrefix(it), availableTowels) }
                .also { cache[design] = it }

    return towelCombinations(design, availableTowels)
}
