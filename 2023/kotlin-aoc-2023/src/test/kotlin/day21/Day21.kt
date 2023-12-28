package day21

import FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: List<String>, numSteps: Int = 6): Int {
    val graph = input.parseGraph()

    return generateSequence(startSet(graph)) { step(it, graph) }
        .drop(numSteps)
        .first()
        .size
}

private fun part2(input: List<String>): Long {
    // See https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
    // for the theory behind this (which I did _not_ work out by myself)

    val graph = input.parseGraph()

    val lala = generateSequence(startSet(graph)) { step(it, graph) }
        .accumulateSteps(graph)
        .take(134 + 134)
        .toList().last()

    val numEvenStepsInFullSquare = lala.filter { (_, n) -> n % 2 == 0 }.count()
    val numOddStepsInFullSquare = lala.filter { (_, n) -> n % 2 == 1 }.count()

    val stepsInEvenCorner = lala.filter { (_, n) -> n % 2 == 0 && n > 65 }.count()
    val stepsInOddCorner = lala.filter { (_, n) -> n % 2 == 1 && n > 65 }.count()

    val n = 202300L

    val numEvenSquares = n * n
    val numOddSquares = (n + 1) * (n + 1)
    val numOddCorners = n + 1
    val numEvenCorners = n

    return numOddSquares * numOddStepsInFullSquare +
            numEvenSquares * numEvenStepsInFullSquare -
            (numOddCorners * stepsInOddCorner) +
            (numEvenCorners * stepsInEvenCorner)
}

// this is a horrifically inefficient way of doing this!
fun Sequence<Map<Point, Int>>.accumulateSteps(graph: Map<Point, Char>): Sequence<Map<Point, Int>> {
    var accumulated = mapOf<Point, Int>()

    val ston = this

    return sequence {
        ston.forEach {
            val filterNotNull = graph.map { (p, _) ->
                when (p) {
                    in accumulated -> p to accumulated[p]!!
                    in it -> p to it[p]!!
                    else -> null
                }
            }.filterNotNull()
                .toMap()
            accumulated = filterNotNull
            yield(accumulated)
        }
    }
}

fun startSet(graph: Map<Point, Char>): Map<Point, Int> =
    graph
        .entries
        .first { (_, c) -> c == 'S' }
        .let { (p, _) -> mapOf(p to 0) }

private fun List<String>.parseGraph() =
    this.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            Point(x, y) to c
        }
    }.toMap()

fun step(points: Map<Point, Int>, graph: Map<Point, Char>): Map<Point, Int> =
    points
        .flatMap { (p, n) -> p.neighboursTo(n + 1) }
        .filter { (it, _) -> it in graph.keys && graph[it] != '#' }
        .toMap()

data class Point(val x: Int, val y: Int) {
    private fun neighbours(): Set<Point> {
        return setOf(
            Point(this.x, this.y - 1),
            Point(this.x, this.y + 1),
            Point(this.x - 1, this.y),
            Point(this.x + 1, this.y)
        )
    }

    fun neighboursTo(nPlusOne: Int): List<Pair<Point, Int>> {
        return neighbours().map {
            it to nPlusOne
        }
    }
}

class Day21Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput, numSteps = 6) shouldBe 16
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day21.txt"), numSteps = 64) shouldBe 3699
    }

    @Ignore // too horrifically inefficient to run every time
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day21.txt")) shouldBe 613391294577878L
    }

    @Test
    fun `parsing the graph for fun and profit`() {
        val graph = testInput.parseGraph()

        graph.asString() shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##..#.
                    ..#.#...#..
                    ....#.#....
                    .##..S####.
                    .##..#...#.
                    .......##..
                    .##.#.####.
                    .##..##.##.
                    ...........
                """.trimIndent()
    }

    @Test
    fun `lots of steps`() {
        val graph = testInput.parseGraph()
        val start = startSet(graph)

        val stepList = generateSequence(start) { step(it, graph) }
            .take(7)
            .toList()

        graph.asString(stepList[1]) shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##..#.
                    ..#.#...#..
                    ....#O#....
                    .##.OS####.
                    .##..#...#.
                    .......##..
                    .##.#.####.
                    .##..##.##.
                    ...........
                """.trimIndent()

        graph.asString(stepList[2]) shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##..#.
                    ..#.#O..#..
                    ....#.#....
                    .##O.O####.
                    .##.O#...#.
                    .......##..
                    .##.#.####.
                    .##..##.##.
                    ...........
                """.trimIndent()

        graph.asString(stepList[3]) shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##..#.
                    ..#.#.O.#..
                    ...O#O#....
                    .##.OS####.
                    .##O.#...#.
                    ....O..##..
                    .##.#.####.
                    .##..##.##.
                    ...........
                """.trimIndent()

        graph.asString(stepList[6]) shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##.O#.
                    .O#O#O.O#..
                    O.O.#.#.O..
                    .##O.O####.
                    .##.O#O..#.
                    .O.O.O.##..
                    .##.#.####.
                    .##O.##.##.
                    ...........
                """.trimIndent()

        graph.asStringWithDistances(stepList[6]) shouldBe
                """
                    ...........
                    .....###.#.
                    .###.##.6#.
                    .6#6#6.6#..
                    6.6.#.#.6..
                    .##6.6####.
                    .##.6#6..#.
                    .6.6.6.##..
                    .##.#.####.
                    .##6.##.##.
                    ...........
                """.trimIndent()
    }

    @Test
    fun `even and odd parity squares`() {
        val graph = testInput.parseGraph()
        val start = startSet(graph)

        val stepList = generateSequence(start) { step(it, graph) }
            .take(30)
            .toList()

        stepList[9].size shouldBe 29
        stepList[10].size shouldBe 33
        stepList[11].size shouldBe 35
        stepList[12].size shouldBe 40
        stepList[13].size shouldBe 39
        stepList[14].size shouldBe 42
        stepList[15].size shouldBe 39
        stepList[16].size shouldBe 42
        stepList[17].size shouldBe 39
        stepList[18].size shouldBe 42
        stepList[19].size shouldBe 39
        stepList[20].size shouldBe 42
    }
}

private fun Map<Point, Char>.asString(): String {
    val width = this.maxOf { (p, _) -> p.x } + 1
    val height = this.maxOf { (p, _) -> p.y } + 1

    val str = (0..<height).map { y ->
        (0..<width).map { x ->
            this[Point(x, y)]
        }.joinToString("")
    }.joinToString("\n")

    return str
}

private fun  Map<Point, Char>.asString(points: Map<Point, Int>): String {
    val width = this.maxOf { (p, _) -> p.x } + 1
    val height = this.maxOf { (p, _) -> p.y } + 1

    val str = (0..<height).map { y ->
        (0..<width).map { x ->
            val p = Point(x, y)
            if (p in points) {
                'O'
            } else {
                this[p]
            }
        }.joinToString("")
    }.joinToString("\n")

    return str
}

private fun  Map<Point, Char>.asStringWithDistances(points: Map<Point, Int>): String {
    val width = this.maxOf { (p, _) -> p.x } + 1
    val height = this.maxOf { (p, _) -> p.y } + 1

    val str = (0..<height).map { y ->
        (0..<width).map { x ->
            val p = Point(x, y)
            if (p in points) {
                points[p]
            } else {
                this[p]
            }
        }.joinToString("")
    }.joinToString("\n")

    return str
}

val testInput =
    """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent().lines()
