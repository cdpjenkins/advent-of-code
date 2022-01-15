package day24

fun doOneCell(input: Int, p1: Int, p2: Int, p3: Int, z: Int): Int {
    val x = z % 26 + p2
    val predicate = x != input

    println(String.format("input == %d z == %8d p1 == %2d p2 == %3d p3 = %3d x == %d %s", input, z, p1, p2, p3, x, predicate.toString()))

    return if (predicate) {
        z / p1 * 26 + input + p3
    } else {
        z / p1
    }
}

fun makeCell(p1: Int, p2: Int, p3: Int) = { input: Int, z: Int -> doOneCell(input, p1, p2, p3, z) }

val cells = listOf(
    makeCell(1, 10, 12),
    makeCell(1, 10, 10),
    makeCell(1, 12, 8),
    makeCell(1, 11, 4),
    makeCell(26, 0, 3),
    makeCell(1, 15, 10),
    makeCell(1, 13, 6),
    makeCell(26, -12, 13),
    makeCell(26, -15, 8),
    makeCell(26, -15, 1),
    makeCell(26, -4, 7),
    makeCell(1, 10, 6),
    makeCell(26, -5, 9),
    makeCell(26, -12, 9),
)

fun calculateTheThing(modelNumber: String): Int {
    var result = 0

    for ((i, cell) in cells.withIndex()) {
        val input = modelNumber[i].toString().toInt()
        result = cell(input, result)
    }

    return result
}

fun main() {
    val modelNumber = "11815671117121"
    val result = calculateTheThing(modelNumber)
    if (result == 0) {
        println("winner! $modelNumber $result")
    }
}

// Attempts for largest:
// 93848993419899 - too low
// 93859971419899
// 93859993419899

// 93959993429899 Is the winner (found with a bit of manual searching

// Attempts for smallest:
// 11815671117121 - is the winner. Again found manually.