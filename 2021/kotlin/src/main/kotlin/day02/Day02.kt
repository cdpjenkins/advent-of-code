package day02

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val input = readInput("Day02")

    println(part1CalculatePosition(input))
    println(part2CalculatePosition(input))
}

fun part1CalculatePosition(input: List<String>): Int {
    val finalPosition = input.fold(Position(0, 0), { position, command -> position.move(command) })

    return finalPosition.depth * finalPosition.horizontal
}

fun part2CalculatePosition(input: List<String>): Int {
    val finalPosition = input.fold(PositionWithAim(0, 0, 0), { position, command -> position.move(command) })

    return finalPosition.depth * finalPosition.horizontal
}

class PositionWithAim(val horizontal: Int, val depth: Int, val aim: Int) {
    fun move(dHorizontal: Int, dDepth: Int, dAim: Int) =
        PositionWithAim(horizontal + dHorizontal, depth + dDepth, aim + dAim)

    fun move(commandString: String): PositionWithAim {
        val (command, magnitude) = commandString.parse()
        return move(command, magnitude)
    }

    fun move(command: String, magnitude: String) =
        when (command) {
            "forward" -> move(magnitude.toInt(), magnitude.toInt() * aim, 0)
            "up" -> move(0, 0, -magnitude.toInt())
            "down" -> move(0, 0, magnitude.toInt())
            else -> throw IllegalArgumentException(command)
        }

}

fun String.parse() = split(" ")

data class Position(val horizontal: Int, val depth: Int) {
    fun move(dHorizontal: Int, dDepth: Int) = Position(horizontal + dHorizontal, depth + dDepth)

    fun move(commandString: String): Position {
        val (command, magnitude) = commandString.parse()
        return move(command, magnitude)
    }

    fun move(command: String, magnitude: String) =
        when (command) {
            "forward" -> move(magnitude.toInt(), 0)
            "up" -> move(0, -magnitude.toInt())
            "down" -> move(0, magnitude.toInt())
            else -> throw IllegalArgumentException(command)
        }
}
