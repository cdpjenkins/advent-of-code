package day15

import FileUtil.readInputFileToList
import Point2D
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

@Disabled
class Day15 {
    @Test
    internal fun `part 1 test input`() {
        val input = testInput

        val sensorsAndBeacons = input.map { it.parseSensorAndBeacon() }

        val result = sensorsAndBeacons
            .map { it.numSquaresAtY(y = 10) }
            .sum()

        result shouldBe 26
    }

    @Disabled
    @Test
    internal fun `part 1 real input`() {
        val input = realInput

        val sensorsAndBeacons = input.map { it.parseSensorAndBeacon() }

        val squaresThatCantBeABeacon =
            sensorsAndBeacons
                .map { it.squaresThatCantBeABeacon(y = 10) }
                .map { it.size }
                .sum()

        squaresThatCantBeABeacon shouldBe 26
    }
}

private fun Set<Point2D>.dumpToString(): String {
    val minX = this.minBy { it.x }.x
    val minY = this.minBy { it.y }.y
    val maxX = this.maxBy { it.x }.x
    val maxY = this.maxBy { it.y }.y

    println("minY == $minY")

    val stringRepresentation = (minY..maxY).map { y ->
        (minX..maxX).map { x ->
            if (Point2D(x, y) in this) "#" else "."
        }.joinToString("")
    }.joinToString("\n")

    return stringRepresentation
}

data class SensorBeacon(val sensor: Point2D, val beacon: Point2D) {
    val radius = sensor.manhattenDistanceTo(beacon)

    fun numSquaresAtY(y: Int): Int {
        val dy = (y - sensor.y).absoluteValue

        val r = 1+ 2 * radius - (2 * dy)

        if (r < 0) {
            return 0
        } else {
            if (y == beacon.y) {
                return r - 1
            } else {
                return r
            }
        }
    }

    fun squaresThatCantBeABeacon(): Set<Point2D> {
        val minY = sensor.y - radius
        val maxY = sensor.y + radius
        val minX = sensor.x - radius
        val maxX = sensor.x + radius

        val pointsWithinRange =
            Point2D
                .iterateRange(minX, maxX, minY, maxY)
                .filter { squareIsWithinRange(it) }
                .toSet()
        val pointsWithinRangeButNotTheBeacon = pointsWithinRange.minus(beacon)

        return pointsWithinRangeButNotTheBeacon
    }

    fun squaresThatCantBeABeacon(y: Int): Set<Point2D> {
        val minY = sensor.y - radius
        val maxY = sensor.y + radius
        val minX = sensor.x - radius
        val maxX = sensor.x + radius

        val pointsWithinRange =
            (minX..maxX)
                .map { x -> Point2D(x, y) }
                .filter { squareIsWithinRange(it) }
                .toSet()
        val pointsWithinRangeButNotTheBeacon = pointsWithinRange.minus(beacon)

        return pointsWithinRangeButNotTheBeacon

    }

    private fun squareIsWithinRange(it: Point2D) = sensor.manhattenDistanceTo(it) <= radius
}

private fun String.parseSensorAndBeacon(): SensorBeacon {
    val (sensorX, sensorY, beaconX, beaconY) = this.parseUsingRegex(SENSOR_BEACON_REGEX)

    return SensorBeacon(Point2D(sensorX.toInt(), sensorY.toInt()), Point2D(beaconX.toInt(), beaconY.toInt()))
}

val SENSOR_BEACON_REGEX = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

val testInput =
    """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3
    """.trimIndent().lines()

val realInput = readInputFileToList("day15.txt")
