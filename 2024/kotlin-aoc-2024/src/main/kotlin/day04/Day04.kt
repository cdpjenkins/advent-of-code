package day04

fun part1(input: List<String>) =
    countXmas(
        horizontalsForwards(input) +
                horizontalsBackwards(input) +
                verticalsDownards(input) +
                verticalsUpwards(input) +
                diagonalsDownRight(input) +
                diagonalsUpLeft(input) +
                diagonalsDownLeft(input) +
                diagonalsUpRight(input)
    )

fun part2(input: List<String>) =
    (0 until input.width() - 2).flatMap { x ->
        (0 until input.height() - 2).map { y ->
            if (isX_MAS(input, x, y)) {
                true
            } else {
                null
            }
        }
    }
        .filterNotNull()
        .count()

private fun horizontalsForwards(input: List<String>) = input
private fun horizontalsBackwards(input: List<String>) = horizontalsForwards(input).map { it.reversed() }
private fun diagonalsUpRight(input: List<String>) = diagonalsDownLeft(input).map { it.reversed() }
private fun diagonalsDownLeft(input: List<String>) = (input.width() * 2 - 2 downTo 0)
    .map { startX ->
        (0 until input.height()).map { y ->
            val x = startX - y
            if (x in (0 until input.width())) {
                input[y][x]
            } else {
                null
            }
        }
            .filterNotNull()
            .joinToString("")
    }
private fun diagonalsUpLeft(input: List<String>) = diagonalsDownRight(input).map { it.reversed() }
private fun diagonalsDownRight(input: List<String>) =
    (input.width() - 1 downTo -input.width())
        .map { startX ->
            (0 until input.height()).map { y ->
                val x = startX + y
                if (x in (0 until input.width())) {
                    input[y][x]
                } else {
                    null
                }
            }
                .filterNotNull()
                .joinToString("")
        }
private fun verticalsUpwards(input: List<String>) = verticalsDownards(input).map { it.reversed() }
private fun verticalsDownards(input: List<String>) =
    (0 until input.width()).map { x ->
        (0 until input.height()).map { y ->
            input[y][x]
        }.joinToString("")
    }


private fun List<String>.width() = this[0].length
private fun List<String>.height() = size

val XMAS_REGEX = "XMAS".toRegex()
private fun countXmas(horizontals: List<String>): Int {
    val total = horizontals.map {
        val found = XMAS_REGEX.findAll(it)
        found.count()
    }.sum()
    return total
}

fun isX_MAS(input: List<String>, x: Int, y: Int): Boolean {
    return masOnMainDiagonal(input, x, y)  && masOnOtherDiagonal(input, x, y)
}

fun masOnOtherDiagonal(input: List<String>, x: Int, y: Int): Boolean {
    return (input[y][x+2] == 'M' && input[y+1][x+1] == 'A' && input[y+2][x] == 'S') ||
            (input[y][x+2] == 'S' && input[y+1][x+1] == 'A' && input[y+2][x] == 'M')
}

fun masOnMainDiagonal(input: List<String>, x: Int, y: Int): Boolean {
    return (input[y][x] == 'M' && input[y+1][x+1] == 'A' && input[y+2][x+2] == 'S') ||
            (input[y][x] == 'S' && input[y+1][x+1] == 'A' && input[y+2][x+2] == 'M')
}
