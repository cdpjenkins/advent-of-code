package day15

import utils.Direction
import utils.FileUtil.readInputFileToList
import utils.ListUtils.splitByBlank
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import utils.toGrid
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {
    val (warehouse, directions) = parse(input)

    return warehouseSequence(warehouse, directions)
        .last()
        .sumOfGPSCoords()
}

private fun warehouseSequence(
    warehouse: Warehouse,
    directions: String,
) = sequence {
    var mutableWarehouse = warehouse

    directions.forEach { direction ->
        yield(mutableWarehouse)
        mutableWarehouse = mutableWarehouse.moveRobot(direction)
    }
}

private fun parse(input: List<String>): Pair<Warehouse, String> {
    val (mapLines, directionLines) = input.splitByBlank()

    val gridWithStartMarked = mapLines.toGrid()
    val robotStart: Vector2D = gridWithStartMarked.entries.first { (_, c) -> c == '@' }.key

    val grid = gridWithStartMarked.map { (p, c) -> p to if (c == '@') '.' else c }.toMap()

    return Pair(
        Warehouse(grid, robotStart),
        directionLines.joinToString("")
    )
}

data class Warehouse(val grid: Map<Vector2D, Char>, val robotPosition: Vector2D) {
    private val width: Int = grid.keys.maxOf { it.x } + 1
    private val height: Int = grid.keys.maxOf { it.y } + 1

    fun asString() =
        (0 until height).map { y ->
            (0 until width).map { x ->
                val p = Vector2D(x, y)
                val targetSquare = grid[p]
                when {
                    p == robotPosition -> '@'
                    targetSquare == '@' -> '.'
                    else -> grid[p]!!
                }
            }.joinToString("")
        }.joinToString("\n")

    fun moveRobot(c: Char): Warehouse {
        val direction = Direction.of(c)

        val tentativeNewRobotPos = direction.move(robotPosition)

        return if (grid[tentativeNewRobotPos] == 'O') {
            val (newWarehouse, success) = tryMoveBoxes(tentativeNewRobotPos, direction)
            if (success) {
                newWarehouse.moveRobot(c)
            } else {
                this
            }
        } else if (grid[tentativeNewRobotPos] == '#') {
            this
        } else {
            this.copy(robotPosition = tentativeNewRobotPos)
        }
    }

    private fun tryMoveBoxes(pos: Vector2D, direction: Direction): Pair<Warehouse, Boolean> {
        require(grid[pos] == 'O')

        val nextPos = direction.move(pos)
        val nextSquare = grid[nextPos]!!

        return when (nextSquare) {
            '#' -> Pair(this, false)
            '.' -> {
                val mutableGrid = grid.toMutableMap()
                mutableGrid[pos] = '.'
                mutableGrid[nextPos] = 'O'
                Pair(this.copy(grid=mutableGrid), true)
            }
            'O' -> {
                val (newWarehouse, success) = tryMoveBoxes(nextPos, direction)
                if (success) {
                    newWarehouse.tryMoveBoxes(pos, direction)
                } else {
                    Pair(this, false)
                }
            }
            else -> throw IllegalStateException("$nextSquare $this")
        }
    }

    fun sumOfGPSCoords(): Int {

        fun Vector2D.gpsCoord() = x + 100 * y

        return grid.filter { (_, v) -> v == 'O' }
            .map { it.key }
            .map { it.gpsCoord() }
            .sum()
    }

}

private fun part2(input: List<String>): Int {
    return 123
}

class Day15Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 10092
    }

    @Test
    fun `part 1 with simple test input`() {
        part1(simpleTestInput) shouldBe 2028
    }


    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day15.txt")) shouldBe 1383666
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day15.txt")) shouldBe -1
    }

    @Test
    fun `can parse warehouse and move robot`() {
        val (warehouse, directions) = parse(simpleTestInput)

        val warehouseSequence = warehouseSequence(warehouse, directions).toList()


        warehouseSequence.take(16).map { it.asString() } shouldBe listOf(
                """
                    ########
                    #..O.O.#
                    ##@.O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #..O.O.#
                    ##@.O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #.@O.O.#
                    ##..O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #.@O.O.#
                    ##..O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #..@OO.#
                    ##..O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #...@OO#
                    ##..O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #...@OO#
                    ##..O..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #......#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##..@..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##..@..#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.@...#
                    #...O..#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.....#
                    #..@O..#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.....#
                    #...@O.#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.....#
                    #....@O#
                    #.#.O..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.....#
                    #.....O#
                    #.#.O@.#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent(),
                """
                    ########
                    #....OO#
                    ##.....#
                    #.....O#
                    #.#O@..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent()
        )





    }
}

val simpleTestInput =
    """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########

        <^^>>>vv<v>>v<<
    """.trimIndent().lines()

val testInput =
    """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().lines()
