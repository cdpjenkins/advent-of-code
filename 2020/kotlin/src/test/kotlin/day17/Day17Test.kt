package day17

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput

class Day17Test :FunSpec({
    test("day 17 part 1 with test data") {
        numAliveAfter6Steps(testInput) shouldBe 112
    }

    test("day 17 part 1 with real data") {
        val realInput = realInput("day17")
        numAliveAfter6Steps(realInput) shouldBe 207
    }

    test("day 17 part 2 with test data") {
        numAliveAfter6Steps4D(testInput) shouldBe 848
    }

    test("day 17 part 2 with real data") {
        val realInput = realInput("day17")
        numAliveAfter6Steps4D(realInput) shouldBe 2308
    }


})

private fun numAliveAfter6Steps(input: List<String>) =
    generateSequence(toPoints(input)) { it.step() }
        .drop(6)
        .first()
        .count()

private fun toPoints(input: List<String>) =
    input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            if (char == '#') Point3D(x, y, 0) else null
        }
    }
        .filterNotNull()
        .toSet()

private fun numAliveAfter6Steps4D(input: List<String>) =
    generateSequence(toPoints4D(input)) { it.step4D() }
        .drop(6)
        .first()
        .count()

private fun toPoints4D(input: List<String>) =
    input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            if (char == '#') Point4D(x, y, 0, 0) else null
        }
    }.filterNotNull()
        .toSet()

private fun Set<Point3D>.step(): Set<Point3D> {
    val numNeighboursMap =
        this.flatMap { it.neighbours() }
            .groupingBy { it }
            .eachCount()

    return numNeighboursMap.map { (key, value) ->
        if (key in this) {
            if (value == 2 || value == 3) {
                key
            } else {
                null
            }
        } else {
            if (value == 3) {
                key
            } else {
                null
            }
        }
    }.filterNotNull()
        .toSet()
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun neighbours() =
        (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).map { dz ->
                    Point3D(x + dx, y + dy, z + dz)
                }
            }
        }.filter { it != this }
}

data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun neighbours() =
        (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).flatMap { dz ->
                    (-1..1).map { dw ->
                        Point4D(x + dx, y + dy, z + dz, w + dw)
                    }
                }
            }
        }.filter { it != this }
}

private fun Set<Point4D>.step4D(): Set<Point4D> {
    val numNeighboursMap =
        this.flatMap { it.neighbours() }
            .groupingBy { it }
            .eachCount()

    return numNeighboursMap.map { (key, value) ->
        if (key in this) {
            if (value == 2 || value == 3) {
                key
            } else {
                null
            }
        } else {
            if (value == 3) {
                key
            } else {
                null
            }
        }
    }.filterNotNull()
        .toSet()
}


val testInput = """
            .#.
            ..#
            ###
        """.trimIndent().lines()
