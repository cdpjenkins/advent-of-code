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
    fun neighbours(): Set<Point> {
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

private fun part2(input: List<String>): Int {
    return 123
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

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
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
