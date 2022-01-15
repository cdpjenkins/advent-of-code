package day04

import readInput
import java.lang.AssertionError

fun main() {
    val input = readInput("Day04")

    println(day4Part1BingoScore(input))
    println(day4Part2BingoScore(input))
}

fun day4Part1BingoScore(input: List<String>): Int {
    val numbersCalled = input.first().split(",").map { it.toInt() }
    val boards = createBoards(input.drop(2))

    return play(boards, numbersCalled)
}

fun day4Part2BingoScore(input: List<String>): Int {
    val numbersCalled = input.first().split(",").map { it.toInt() }
    val boards = createBoards(input.drop(2))

    return playToLose(boards, numbersCalled).first()
}

fun play(boards: List<Board>, numbers: List<Int>): Int {
    if (numbers.isEmpty()) {
        throw AssertionError("got to the end of the input innit")
    } else {
        val updatedBoards = boards.map { it.callNumber(numbers.first()) }

        val winningBoards = updatedBoards.filter { it.isWon() }
        if (!winningBoards.isEmpty()) {
            return winningBoards.first().calculateScore() * numbers.first()
        } else {
            return play(updatedBoards, numbers.drop(1))
        }
    }
}

fun playToLose(boards: List<Board>, numbers: List<Int>): List<Int> {
    if (!numbers.isEmpty()) {
        val updatedBoards = boards.map { it.callNumber(numbers.first()) }

        val winningBoards = updatedBoards.filter { it.isWon() }
        val nonWinningBoards = updatedBoards.filter { !it.isWon() }
        if (!winningBoards.isEmpty()) {
            return playToLose(nonWinningBoards, numbers.drop(1)) + winningBoards.first().calculateScore() * numbers.first()
        }

        return playToLose(nonWinningBoards, numbers.drop(1))
    } else {
        return emptyList()
    }
}

fun createBoards(input: List<String>, boards: List<Board> = emptyList()): List<Board> =
    if (input.isEmpty()) {
        boards
    } else {
        val boardInput = input.takeWhile { !it.trim().isEmpty() }
        val rest = input.dropWhile { !it.trim().isEmpty() }.drop(1)

        createBoards(rest, boards + makeBoard(boardInput))
    }

fun makeBoard(input: List<String>): Board {
    val squares =
        input.map { it.trim().split("\\s+".toRegex()).map(String::toInt) }
            .withIndex()
            .flatMap { (row, rowContents) ->
                rowContents.withIndex().map { (col, value) ->
                    Pair(Location(col, row), value)
                }
            }
            .toMap()

    val board = Board(squares)

    return board
}

data class Location(val col: Int, val row: Int)

data class Board(val squares: Map<Location, Int>, val marks: Set<Location> = emptySet()) {
    fun callNumber(number: Int): Board {
        val newMarks = squares.filter { it.value == number }.map { it.key }

        return Board(squares, marks + newMarks)
    }

    fun isWon(): Boolean {
        val thingie = (rows() + cols()).filter { it ->
            it.all { ston: Location -> marks.contains(ston) }
        }

        return !thingie.isEmpty()
    }

    fun calculateScore(): Int {
        val allLocations = (0..4).flatMap { y ->
            (0..4).map { x ->
                Location(x, y)
            }
        }

        val unmarkedLocations = allLocations.filter { !marks.contains(it) }

        println("unmarked locations " + unmarkedLocations)

        val score = unmarkedLocations.map { squares[it]!! }.sum()
        return score
    }
}

fun rows(): List<List<Location>> {
    return (0..4).map { y ->
        (0..4).map { x ->
            Location(x, y)
        }
    }
}

fun cols(): List<List<Location>> {
   return  (0..4).map { x ->
        (0..4).map { y ->
            Location(x, y)
        }
    }
}

