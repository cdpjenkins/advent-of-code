package day18

import FileUtil.readInputFileToList
import Point3D
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day18 {

    @Test
    internal fun `part 1 really simple test input`() {
        val input = reallySimpleTestInput

        val result = numOpenFacesFor(input)
        result shouldBe 10
    }

    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val result = numOpenFacesFor(input)
        result shouldBe 64
    }

    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val result = numOpenFacesFor(input)
        result shouldBe 3564
    }

    @Test
    internal fun `part 2 test input`() {
        val input = testInput

        val cubes = input.map { it.parse() }.toSet()

        println("volume: ${volume(cubes)}")


        val minX = cubes.map { it.x }.min() - 2
        val minY = cubes.map { it.y }.min() - 2
        val minZ = cubes.map { it.z }.min() - 2
        val maxX = cubes.map { it.x }.max() + 2
        val maxY = cubes.map { it.y }.max() + 2
        val maxZ = cubes.map { it.z }.max() + 2
        val pointsOutsideDroplet =
            Point3D(minX, minY, minZ)
                .findConnectedComponent(cubes, minX, minY, minZ, maxX, maxY, maxZ)

        val result =
            cubes
                .flatMap { it.openFaces(cubes) }
                .filter { (it in pointsOutsideDroplet) }

        result.size shouldBe 58
    }

    @Test
    internal fun `part 2 real input`() {
        val input = realInput

        val cubes = input.map { it.parse() }.toSet()

        val minX = cubes.map { it.x }.min() - 2
        val minY = cubes.map { it.y }.min() - 2
        val minZ = cubes.map { it.z }.min() - 2
        val maxX = cubes.map { it.x }.max() + 2
        val maxY = cubes.map { it.y }.max() + 2
        val maxZ = cubes.map { it.z }.max() + 2
        val pointsOutsideDroplet =
            Point3D(minX, minY, minZ)
                .findConnectedComponent(cubes, minX, minY, minZ, maxX, maxY, maxZ)

        println("num points outside: ${pointsOutsideDroplet.size}")

        val result =
            cubes
                .flatMap { it.openFaces(cubes) }
                .filter { (it in pointsOutsideDroplet) }

        result.size shouldBe 2106
    }

    private fun volume(cubes: Set<Point3D>): Int {
        val minX = cubes.map { it.x }.min() - 2
        val minY = cubes.map { it.y }.min() - 2
        val minZ = cubes.map { it.z }.min() - 2
        val maxX = cubes.map { it.x }.max() + 2
        val maxY = cubes.map { it.y }.max() + 2
        val maxZ = cubes.map { it.z }.max() + 2

        val volume = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)

        return volume
    }

    private fun numOpenFacesFor(input: List<String>): Int {
        val cubes = input.map { it.parse() }.toSet()

        return cubes
            .flatMap { it.openFaces(cubes) }
            .size
    }
}

private fun Point3D.findConnectedComponent(
    cubes: Set<Point3D>,
    minX: Int,
    minY: Int,
    minZ: Int,
    maxX: Int,
    maxY: Int,
    maxZ: Int
): List<Point3D> {
    val thisComponent = mutableSetOf(this)
    val openSet =
        this
            .openFaces(cubes)
            .filter { it.withinCuboid(minX, minY, minZ, maxX, maxY, maxZ) }
            .toMutableList()

    while (openSet.isNotEmpty()) {
        val current = openSet.removeLast()

        if (current in thisComponent) {
            // meh
        } else {
            thisComponent.add(current)
            openSet.addAll(current
                .openFaces(cubes)
                .filter { it.withinCuboid(minX, minY, minZ, maxX, maxY, maxZ) }
            )
        }
    }

    return thisComponent.toList()
}

private fun Point3D.openFaces(cubes: Set<Point3D>) = faces().filter { it !in cubes }

private fun String.parse(): Point3D {
    val (x, y, z) = this
        .split(",")
        .map { it.toInt() }

    return Point3D(x, y, z)
}

val reallySimpleTestInput =
    """
        1,1,1
        2,1,1
    """.trimIndent().lines()

val testInput =
    """
        2,2,2
        1,2,2
        3,2,2
        2,1,2
        2,3,2
        2,2,1
        2,2,3
        2,2,4
        2,2,6
        1,2,5
        3,2,5
        2,1,5
        2,3,5
    """.trimIndent().lines()

val realInput = readInputFileToList("day18.txt")
