package day16_bfs

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

private fun part1_bfs(input: List<String>): Int {
    val grid = parseInput(input)

    return findNumEnergisedTiles(
        grid,
        BeamSegment(Pos(0, 0), Direction.RIGHT)
    )
}

private fun part2_bfs(input: List<String>): Int {
    val grid = parseInput(input)

    val up = (0 until grid.width).map { x ->
        findNumEnergisedTiles(grid, BeamSegment(Pos(x, grid.width - 1), Direction.UP))
    }

    val down = (0 until grid.width).map { x ->
        findNumEnergisedTiles(grid, BeamSegment(Pos(x, 0), Direction.DOWN))
    }

    val left = (0 until grid.height).map { y ->
        findNumEnergisedTiles(grid, BeamSegment(Pos(grid.height - 1, y), Direction.LEFT))
    }

    val right = (0 until grid.height).map { y ->
        findNumEnergisedTiles(grid, BeamSegment(Pos(0, y), Direction.RIGHT))
    }

    return (up + down + left + right).max()
}

private fun findNumEnergisedTiles(grid: Grid, beamSegment: BeamSegment): Int {
    if (beamSegment !in grid.exitNodes) {
        val energisingBeamSegments = grid.shineALightIn(beamSegment)

        return energisingBeamSegments.map { it.pos }.toSet().size
    } else {
        return 0
    }

}

private fun parseInput(input: List<String>): Grid {
    val squares = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            val pos = Pos(x, y)
            pos to Square(pos, c)
        }
    }.toMap()

    val grid = Grid(squares)
    return grid
}

data class Grid(
    val squares: Map<Pos, Square>,
    var exitNodes: MutableSet<BeamSegment> = mutableSetOf()
) {
    val width = squares.keys.maxOf { it.x } + 1
    val height = squares.keys.maxOf { it.y } + 1

    fun shineALightIn(beamSegment: BeamSegment): Set<BeamSegment> {
        val beamSegmentsToProcess: Queue<BeamSegment> = LinkedList()
        beamSegmentsToProcess.add(beamSegment)

        val processedBeamSegments = mutableSetOf<BeamSegment>()

        while (beamSegmentsToProcess.isNotEmpty()) {
            val segment = beamSegmentsToProcess.remove()

            if (hasExitedTheBuilding(segment)) {
                exitNodes.add(segment.previous().reverse())
            } else {
                if (segment !in processedBeamSegments) {
                    val newBeamSegments = squares[segment.pos]
                        ?.beamHitsSquare(segment)
                        ?: emptyList()
                    beamSegmentsToProcess.addAll(newBeamSegments.filter { it !in processedBeamSegments })
                }
            }

            processedBeamSegments.add(segment)
        }

        return processedBeamSegments.filter{ it.pos in squares.keys }.toSet()
    }

    private fun hasExitedTheBuilding(segment: BeamSegment) =
        (segment.pos.x == -1 && segment.direction == Direction.LEFT) ||
                (segment.pos.x == width  && segment.direction == Direction.RIGHT )||
                (segment.pos.y == -1 && segment.direction == Direction.UP) ||
                (segment.pos.y == height  && segment.direction == Direction.DOWN)

    fun energisedSquaresString(energisedSquares: Set<Pos>): String {
        return (0 until height).map { y ->
            (0 until width).map { x ->
                if (Pos(x, y) in energisedSquares) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")

    }

}

data class Pos(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Pos {
        return Pos(x + direction.dx, y + direction.dy)
    }

    operator fun minus(direction: Direction): Pos {
        return Pos(x - direction.dx, y - direction.dy)
    }

}

data class Square(
    val pos: Pos,
    val contains: Char
) {
    fun beamHitsSquare(beamSegment: BeamSegment): List<BeamSegment> {
        val nextBeams = when (contains) {
            '.' -> {
                // beam passes straight through
                listOf(beamSegment.next(beamSegment.direction))
            }

            '/' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.next(Direction.RIGHT))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.next(Direction.LEFT))
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.next(Direction.DOWN))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.next(Direction.UP))
                    }
                }
            }

            '\\' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.next(Direction.LEFT))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.next(Direction.RIGHT))
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.next(Direction.UP))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.next(Direction.DOWN))
                    }
                }
            }

            '-' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(
                            beamSegment.next(Direction.LEFT),
                            beamSegment.next(Direction.RIGHT)
                        )
                    }

                    Direction.DOWN -> {
                        listOf(
                            beamSegment.next(Direction.LEFT),
                            beamSegment.next(Direction.RIGHT)
                        )
                    }

                    Direction.LEFT -> {
                        listOf(beamSegment.next(beamSegment.direction))
                    }

                    Direction.RIGHT -> {
                        listOf(beamSegment.next(beamSegment.direction))
                    }
                }
            }

            '|' -> {
                when (beamSegment.direction) {
                    Direction.UP -> {
                        listOf(beamSegment.next(beamSegment.direction))
                    }

                    Direction.DOWN -> {
                        listOf(beamSegment.next(beamSegment.direction))
                    }

                    Direction.LEFT -> {
                        listOf(
                            beamSegment.next(Direction.UP),
                            beamSegment.next(Direction.DOWN)
                        )
                    }

                    Direction.RIGHT -> {
                        listOf(
                            beamSegment.next(Direction.UP),
                            beamSegment.next(Direction.DOWN)
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
    fun next(newDirection: Direction) = BeamSegment(pos + newDirection, newDirection)

    fun previous() = BeamSegment(pos - direction, direction)
    fun reverse() = BeamSegment(pos, direction.reverse())
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

    fun reverse() =
        when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }

    abstract val dx: Int
    abstract val dy: Int
}

class Day16BFSTest {
    @Test
    fun `part 1 with test input`() {
        part1_bfs(testInput) shouldBe 46
    }

    @Test
    fun `part 1 with real input`() {
        part1_bfs(readInputFileToList("day16.txt")) shouldBe 7482
    }

    @Test
    fun `part 2 with test input`() {
        part2_bfs(testInput) shouldBe 51
    }

    @Test
    fun `part 2 with real input`() {
        part2_bfs(readInputFileToList("day16.txt")) shouldBe 7896
    }

    @Test
    fun `energises the correct tiles for test inputa`() {
        val grid = parseInput(testInput)

        val energisedSquares = grid.shineALightIn(
            listOf(BeamSegment(Pos(0, 0), Direction.RIGHT)).first()
        ).map { it.pos }
            .toSet()

        val energisedSquaresString = grid.energisedSquaresString(energisedSquares)

        energisedSquaresString shouldBe
                """
                    ######....
                    .#...#....
                    .#...#####
                    .#...##...
                    .#...##...
                    .#...##...
                    .#..####..
                    ########..
                    .#######..
                    .#...#.#..
                """.trimIndent()
    }
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
