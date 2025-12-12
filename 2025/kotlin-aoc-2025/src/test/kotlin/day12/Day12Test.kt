package day12

import io.kotest.inspectors.forAll
import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.ListUtils.splitByBlank
import utils.RegexUtils.parseUsingRegex
import utils.Vector2D
import kotlin.test.Ignore

private fun part1(input: List<String>): Int {

    val sections = input.splitByBlank()

    val shapesSectionStrings = sections.dropLast(1)
    val shapes = shapesSectionStrings.map { it.toShape() }
    shapes.forEach { println(it.toStringRepresentation()); println() }

    val regionsSectionStrings = sections.last()
    val regions = regionsSectionStrings.map { it.parseRegion() }
    println(regions)

    return 123
}

private fun List<String>.toShape(): Shape {
//    val indexLine = this.first()
//    val (_) = indexLine.parseUsingRegex("""^(\d+):$""")

    val shapeLines = this.drop(1)
    shapeLines.size shouldBe 3
    shapeLines.forAll { it.length shouldBe 3 }

    val points = shapeLines.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, c) ->
            if (c == '#') Vector2D(x, y) else null
        }
    }.filterNotNull()
        .toSet()

    return Shape(points)
}

private fun String.parseRegion(): Region {
    val (widthString, heightString, presentsString) = this.parseUsingRegex("""^(\d+)x(\d+): ([ \d]+)$""")
    val presents = presentsString.split(" ").map { it.toInt() }
    return Region(widthString.toInt(), heightString.toInt(), presents)
}

data class Region(
    val width: Int,
    val height: Int,
    val presents: List<Int>
)

data class Shape(val points: Set<Vector2D>) {
    fun toStringRepresentation(): String {
        return (0..<3).joinToString("\n") { y ->
            (0..<3).map { x ->
                if (points.contains(Vector2D(x, y))) '#' else '.'
            }.joinToString("")
        }
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day12Test {
    @Ignore
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day_template.txt")) shouldBe -1
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
}

val testInput =
    """
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
    """.trimIndent().lines()
