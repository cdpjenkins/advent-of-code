package day02

fun part1(input: List<String>) =
    input.map { it.parseReport() }
        .count { it.isSafe() }

fun part2(input: List<String>) =
    input.map { it.parseReport() }
        .count { it.isSafeWithDampening() }

private fun String.parseReport() = this.split(" ").map { it.toInt() }
private fun List<Int>.isSafe() = this.isAllAscendingGradually() || this.isAllDescendingGradually()
private fun List<Int>.isAllAscendingGradually() = this.zipWithNext().all { (a, b) -> a - b in (-3..-1) }
private fun List<Int>.isAllDescendingGradually() = this.zipWithNext().all { (a, b) -> a - b in (1..3) }
private fun List<Int>.isSafeWithDampening() = indices.any { i -> withElementRemovedAt(i).isSafe() }
private fun List<Int>.withElementRemovedAt(i: Int) = this.toMutableList().apply { removeAt(i) }.toList()
