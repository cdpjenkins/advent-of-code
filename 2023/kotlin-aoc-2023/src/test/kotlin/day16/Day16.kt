package day16

import FileUtil.readInputFileToList
import day16.BeamSegment.Companion.move
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {

    val squares = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            val pos = Pos(x, y)
            pos to Square(pos, c)
        }
    }.toMap()

    val grid = Grid(squares)

    grid.shineALightIn(BeamSegment(Pos(0, 0), Direction.RIGHT))

    val result = grid.squares.values.count { it.energised }

    return result
}

private fun part2(input: List<String>): Int {
    return 123
}

data class Grid(val squares: Map<Pos, Square>) {
    fun shineALightIn(beamSegment: BeamSegment) {
        val square = squares[beamSegment.pos]

        if (square == null) {
            // meh we are done here
        } else {
            // could totally us polymorphism for this...
            val moarBeamSegments = square.beamHitsSquare(beamSegment)

            moarBeamSegments.forEach { shineALightIn(it) }
        }
    }
}

data class Pos(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Pos {
        return Pos(x + direction.dx, y + direction.dy)
    }
}

data class Square(
    val pos: Pos,
    val contains: Char,
    var energised: Boolean = false,
    val energisingBeamSegments: MutableSet<BeamSegment> = mutableSetOf()
) {
    fun beamHitsSquare(beamSegment: BeamSegment): List<BeamSegment> {

        if (beamSegment in energisingBeamSegments) {
            return listOf()
        }

        energised = true

        energisingBeamSegments.add(beamSegment)

        val nextBeams = when (contains) {
            '.' -> {
                // beam passes straight through
                listOf(beamSegment.move(beamSegment.direction))
            }

            '/' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.move(Direction.RIGHT))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.move(Direction.LEFT))
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.move(Direction.DOWN))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.move(Direction.UP))
                    }
                }
            }

            '\\' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.move(Direction.LEFT))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.move(Direction.RIGHT))
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.move(Direction.UP))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.move(Direction.DOWN))
                    }
                }
            }

            '-' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(
                            beamSegment.move(Direction.LEFT),
                            beamSegment.move(Direction.RIGHT)
                        )
                    }

                    Direction.DOWN -> {
                        listOf(
                            beamSegment.move(Direction.LEFT),
                            beamSegment.move(Direction.RIGHT)
                        )
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.move(beamSegment.direction))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.move(beamSegment.direction))
                    }
                }
            }

            '|' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.move(beamSegment.direction))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.move(beamSegment.direction))
                    }

                    Direction.LEFT -> {
                        listOf(
                            beamSegment.move(Direction.UP),
                            beamSegment.move(Direction.DOWN)
                        )
                    }

                    Direction.RIGHT -> {
                        listOf(
                            beamSegment.move(Direction.UP),
                            beamSegment.move(Direction.DOWN)
                        )
                    }
                }
            }

            else -> throw IllegalStateException(contains.toString())
        }

        return nextBeams
    }

}

data class BeamSegment(
    val pos: Pos,
    val direction: Direction
) {
    companion object {
        fun BeamSegment.move(newDirection: Direction) =
            BeamSegment(pos + newDirection, newDirection)
    }
}

enum class Direction {
    UP {
        override val dx: Int = 0
        override val dy: Int = -1
    }, DOWN {
        override val dx: Int = 0
        override val dy: Int = 1
    }, LEFT {
        override val dx: Int = -1
        override val dy: Int = 0
    }, RIGHT {
        override val dx: Int = 1
        override val dy: Int = 0
    };

    abstract val dx: Int
    abstract val dy: Int
}


class Day16Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 46
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day16.txt")) shouldBe -1
    }

//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }
}

val testInput =
    """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent().lines()
