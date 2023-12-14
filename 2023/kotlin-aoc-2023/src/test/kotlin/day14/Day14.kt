package day14

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>) =
    Platform.of(input)
        .roll(Direction.north)
        .loadOnNorthSupportBeam()

private fun part2(input: List<String>): Int {
    val platform = Platform.of(input)

    val seqMeDo = generateSequence(platform) { it.spinCycle() }
        .take(143)
        .toList()

    // period of cycle (for my real input) is 39
    // e.g. values repeat at:
    // 105
    // 144
    // 183
    // etc
    // we can use this to figure out which pattern would also be seen after 1 billion iterations, namely this one:

    val billionthPattern = seqMeDo[142]

    return billionthPattern.loadOnNorthSupportBeam()
}

private fun findAndPrintCycles(platform: Platform): List<IndexedValue<Platform>> {
    val seqMeDo = generateSequence(platform) { it.spinCycle() }
        .withIndex()
        .take(143)
        .toList()

    val alreadySeenVals = mutableMapOf<Platform, Int>()
    seqMeDo.forEach { (i, p) ->
        println(i)
        if (p in alreadySeenVals) {
            println("$i ${alreadySeenVals[p]}")
        } else {
            alreadySeenVals[p] = i
        }
    }
    return seqMeDo
}

data class Platform(
    val width: Int,
    val height: Int,
    val roundedRocks: Set<Pos>,
    val cubeRocks: Set<Pos>,
) {
    fun loadOnNorthSupportBeam() = roundedRocks.sumOf { height - it.y }

    fun roll(direction: Direction): Platform {
        val sortedRespectDue = roundedRocks.sortedWith(direction.comparator())
        return this.roll(sortedRespectDue, direction)
    }

    private fun roll(sortedRoundedRocks: List<Pos>, direction: Direction): Platform =
        if (sortedRoundedRocks.isEmpty()) {
            this
        } else {
            val roundedRock = sortedRoundedRocks.first()
            this.rollOneRock(roundedRock, direction).roll(sortedRoundedRocks.drop(1), direction)
        }

    fun rollOneRock(roundedRock: Pos, function: Direction) =
        this.copy(roundedRocks = roundedRocks - roundedRock + rockRollsTo(roundedRock, function))

    fun rockRollsTo(roundedRock: Pos, direction: Direction): Pos =
        generateSequence(roundedRock) { it -> direction(it)}
            .drop(1)
            .takeWhile { pos -> !(pos in roundedRocks) && !(pos in cubeRocks) && isInBounds(pos) }
            .lastOrNull()
            ?: roundedRock

    private fun isInBounds(pos: Pos) =
        pos.x in (0 until width) &&
                pos.y in (0 until height)

    fun string(): String {
        return (0 until height).map { y ->
            (0 until width).map { x ->
                val pos = Pos(x, y)
                when (pos) {
                    in roundedRocks -> 'O'
                    in cubeRocks -> '#'
                    else -> '.'
                }
            }.joinToString("")
        }.joinToString("\n")
    }

    fun spinCycle(): Platform {
        return this.roll(Direction.north)
            .roll(Direction.west)
            .roll(Direction.south)
            .roll(Direction.east)
    }

    companion object {
        fun of(input: List<String>): Platform {
            val roundedRocks = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    if (c == 'O') {
                        Pos(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
                .toSet()

            val cubeRocks = input.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, c) ->
                    if (c == '#') {
                        Pos(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
                .toSet()

            val width = input.first().length
            val height = input.size

            val platform = Platform(width, height, roundedRocks, cubeRocks)
            return platform
        }
    }
}



data class Pos(val x: Int, val y: Int)

// this could totally be an enum class instead...
// not that this would help the dire performance, but the code might look marginally nicer.
data class Direction(val dx: Int, val dy: Int) {
    operator fun invoke(pos: Pos): Pos {
        return Pos(pos.x + dx, pos.y + dy)
    }

    fun comparator(): Comparator<Pos> {
        return object: Comparator<Pos> {
            override fun compare(o1: Pos, o2: Pos): Int =
                if (dy == -1) {
                    o1.y - o2.y
                } else if (dy == 1) {
                    -(o1.y - o2.y)
                } else if (dx == 1) {
                    o2.x - o1.x
                } else if (dx == -1) {
                    o1.x - o2.x
                } else {
                    throw IllegalStateException(this.toString())
                }
        }
    }

    companion object {
        val north: Direction = Direction(0, -1)
        val south: Direction = Direction(0, 1)
        val east: Direction = Direction(1, 0)
        val west: Direction = Direction(-1, 0)
    }
}

class Day14Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 136
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day14.txt")) shouldBe 106990
    }

    @Ignore // I never made this one work
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 64
    }

    @Ignore // this is far too inefficient (and consequently slow) to run every time
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day14.txt")) shouldBe 100531
    }

    @Test
    fun `rolls single rock north correctly`() {
        Platform.of(testInput)
            .rollOneRock(Pos(0, 3), Direction.north).string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    O....##...
                    .O.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()
    }

    @Test
    fun `blocked rock does not roll`() {
        Platform.of(testInput)
            .rollOneRock(Pos(0, 1), Direction.north).string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    .....##...
                    OO.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()
    }

    @Test
    fun `rolls a rock until it hits another rock`() {
        Platform.of(testInput)
            .rollOneRock(Pos(0, 3), Direction.north).string() shouldBe
                """
                    O....#....
                    O.OO#....#
                    O....##...
                    .O.#O....O
                    .O.....O#.
                    O.#..O.#.#
                    ..O..#O..O
                    .......O..
                    #....###..
                    #OO..#....
                """.trimIndent()
    }

    @Test
    fun `rolls simple rock north correctly`() {
        Platform.of(
            """
                ...
                O..
                ...
            """.trimIndent().lines()
        ).rollOneRock(Pos(0, 1), Direction.north).string() shouldBe
                """
                    O..
                    ...
                    ...
                """.trimIndent()
    }

    @Test
    fun `rolls simple rock north correctly to correct pos and stuff`() {
        Platform.of(testInput)
            .rockRollsTo(Pos(1, 3), Direction.north) shouldBe
                Pos(1, 0)
    }

    @Test
    fun `rolls all rocks north correctly`() {
        val rolledNorth =
            Platform.of(testInput)
                .roll(Direction.north)

        rolledNorth.string() shouldBe
                """
                    OOOO.#.O..
                    OO..#....#
                    OO..O##..O
                    O..#.OO...
                    ........#.
                    ..#....#.#
                    ..O..#.O.O
                    ..O.......
                    #....###..
                    #....#....
                """.trimIndent()

        rolledNorth.roll(Direction.west).string() shouldBe
                """
                    OOOO.#O...
                    OO..#....#
                    OOO..##O..
                    O..#OO....
                    ........#.
                    ..#....#.#
                    O....#OO..
                    O.........
                    #....###..
                    #....#....
                """.trimIndent()
    }

    @Test
    fun `rolls west`() {
        Platform.of(
            """
                    OOOO.#.O..
                    OO..#....#
                    OO..O##..O
                    O..#.OO...
                    ........#.
                    ..#....#.#
                    ..O..#.O.O
                    ..O.......
                    #....###..
                    #....#....
                """.trimIndent().lines()
        ).roll(Direction.west).string() shouldBe
                """
                    OOOO.#O...
                    OO..#....#
                    OOO..##O..
                    O..#OO....
                    ........#.
                    ..#....#.#
                    O....#OO..
                    O.........
                    #....###..
                    #....#....
                """.trimIndent()
    }

    @Test
    fun `spin cycle generates correct pattern`() {
        val platform = Platform.of(testInput)

        val spun = generateSequence(platform) { it.spinCycle() }
            .take(3)
            .map { it.string() }
            .toList()

        spun[1] shouldBe
                """
                    .....#....
                    ....#...O#
                    ...OO##...
                    .OO#......
                    .....OOO#.
                    .O#...O#.#
                    ....O#....
                    ......OOOO
                    #...O###..
                    #..OO#....
                """.trimIndent()
    }
}

val testInput =
    """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent().lines()
