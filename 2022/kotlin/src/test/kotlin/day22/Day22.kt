package day22

import ListUtils.splitByBlank
import Point2D
import RegexUtils.parseUsingRegex
import Vector2D
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import parseBoard

class Day22 {
    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val (boardInput, pathInput) = input.splitByBlank()

        val board =
            parseBoard(boardInput)
                .filter { it.value != ' ' }

        val startingPoint = board.startingPoint()

        val commandSeq = pathInput[0].parse().toList()
        val expandedCommands = commandSeq.flatMap { it.expand() }

        val initialState = State(startingPoint, Direction.EAST)

        val finalState = expandedCommands.fold(initialState) { state, action -> action.applyTo(state, board) }

        finalState shouldBe State(Point2D(7, 5), Direction.EAST)
        finalState.password() shouldBe 6032
    }
}

private fun String.parse(): Sequence<Action> {
    var input = this

    val LEFT_REGEX = "(L).*".toRegex()
    val RIGHT_REGEX = "(R).*".toRegex()
    val FORWARD_REGEX = "([0-9]+).*".toRegex()

    return sequence {
        while (input.isNotEmpty()) {
            when {
                input.matches(LEFT_REGEX) -> {
                    val (command) = input.parseUsingRegex(LEFT_REGEX)
                    input = input.removePrefix(command)
                    yield(Turn.LEFT)
                }
                input.matches(RIGHT_REGEX) -> {
                    val (command) = input.parseUsingRegex(RIGHT_REGEX)
                    input = input.removePrefix(command)
                    yield(Turn.RIGHT)
                }
                input.matches(FORWARD_REGEX) -> {
                    val (command) = input.parseUsingRegex(FORWARD_REGEX)
                    input = input.removePrefix(command)
                    yield(MoveForward(command.toInt()))
                }
                else -> {
                    throw IllegalArgumentException(input)
                }
            }
        }
    }
}

data class State(
    val position: Point2D,
    val direction: Direction
) {
    fun password() = 1000 * (position.y + 1) + 4 * (position.x + 1) + direction.facing
}

sealed interface Action {
    fun expand(): List<Action>
    fun applyTo(state: State, board: Map<Point2D, Char>): State
}

enum class Turn : Action {
    LEFT {
        override fun applyTo(state: State, board: Map<Point2D, Char>) =
            state.copy(direction = state.direction.turnLeft())
    },
    RIGHT {
        override fun applyTo(state: State, board: Map<Point2D, Char>) =
            state.copy(direction = state.direction.turnRight())
    };

    override fun expand() = listOf(this)
}
data class MoveForward(val distance: Int) : Action {
    override fun expand() = List(distance) { MoveForward(1) }
    override fun applyTo(state: State, board: Map<Point2D, Char>): State {
        require(distance == 1) { this.toString() }

        val moveVector = state.direction.vector()
        val tentativeNewPosition = state.position + moveVector
        val newPosition = when (board[tentativeNewPosition]) {
            '.' -> {
                tentativeNewPosition
            }

            '#' -> {
                state.position
            }
            null -> {
                when (state.direction) {
                    Direction.NORTH -> {
                        val y = board.keys.filter { it.x == state.position.x }.maxBy { it.y }.y
                        Point2D(state.position.x, y)
                    }
                    Direction.EAST -> {
                        val x = board.keys.filter { it.y == state.position.y }.minBy { it.y }.x
                        Point2D(x, state.position.y)
                    }

                    Direction.SOUTH -> {
                        val y = board.keys.filter { it.x == state.position.x }.minBy { it.y }.y
                        Point2D(state.position.x, y)
                    }

                    Direction.WEST -> {
                        val x = board.keys.filter { it.y == state.position.y }.maxBy { it.y }.x
                        Point2D(x, state.position.y)
                    }
                }
            }
            else -> {
                throw IllegalStateException("${tentativeNewPosition}")
            }
        }

        return state.copy(position = newPosition)
    }
}

enum class Direction(val facing: Int) {
    NORTH(3) {
        override fun turnLeft() = WEST
        override fun turnRight() = EAST
        override fun vector() = Vector2D(0, -1)
    },
    EAST(0) {
        override fun turnLeft() = NORTH
        override fun turnRight() = SOUTH
        override fun vector() = Vector2D(1, 0)
    },
    SOUTH(1) {
        override fun turnLeft() = EAST
        override fun turnRight() = WEST
        override fun vector() = Vector2D(0, 1)
    },
    WEST(2) {
        override fun turnLeft() = SOUTH
        override fun turnRight() = NORTH
        override fun vector() = Vector2D(-1, 0)
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun vector(): Vector2D
}

private fun Map<Point2D, Char>.startingPoint(): Point2D {
    val topRowY = keys
        .minBy { it.y }
        .y

    val startingPointX = keys
        .filter { it.y == topRowY }
        .minBy { it.x }
        .x

    return Point2D(startingPointX, topRowY)
}

val testInput =
    """
                ...#
                .#..
                #...
                ....
        ...#.......#
        ........#...
        ..#....#....
        ..........#.
                ...#....
                .....#..
                .#......
                ......#.
        
        10R5L5R10L4R5L5
    """.trimIndent().lines()
