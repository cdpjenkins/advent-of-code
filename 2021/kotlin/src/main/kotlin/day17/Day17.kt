package day17

import day17.Vector2D.Companion.ZERO
import readInput
import kotlin.math.sign

fun main() {
    val targetArea = readInput("day17").first().parse()

    println(highestYCoordinateReachable(targetArea))
    println(numberOfVelocitiesThatCauseProbeToHitTargetArea(targetArea))
}

fun numberOfVelocitiesThatCauseProbeToHitTargetArea(targetArea: TargetArea) = trajectoriesThatHit(targetArea).size

fun highestYCoordinateReachable(targetArea1: TargetArea) =
    trajectoriesThatHit(targetArea1).map { it.maxY() }.maxByOrNull { it }

private fun trajectoriesThatHit(targetArea: TargetArea) =
    (0..targetArea.xMax).flatMap { vx ->
        (targetArea.yMin..(-targetArea.yMin)).map { vy ->
            Vector2D(vx, vy)
        }
    }.map { initialVelocity: Vector2D ->
        Probe.trajectory(initialVelocity).takeWhile { it.position.x <= targetArea.xMax && it.position.y >= targetArea.yMin }
    }.filter { trajectory ->
        trajectory.find { it.within(targetArea) } != null
    }

private fun Sequence<Probe>.maxY() = maxByOrNull { it.position.y }!!.position.y

data class TargetArea(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

fun String.parse(): TargetArea {
    val (xMin, xMax, yMin, yMax) = inputRegex.find(this)!!.destructured
    return TargetArea(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt())
}

data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(that: Vector2D): Vector2D = Vector2D(this.x + that.x, this.y + that.y)

    companion object {
        val ZERO = Vector2D(0, 0)
    }
}

data class Probe(val position: Vector2D = ZERO, val velocity: Vector2D) {
    fun step(): Probe =
        Probe(
            position + velocity,
            Vector2D(velocity.x - velocity.x.sign, velocity.y - 1)
        )

    fun within(targetArea: TargetArea) =
        targetArea.xMin <= this.position.x &&
                this.position.x <= targetArea.xMax &&
                targetArea.yMin <= this.position.y &&
                this.position.y <= targetArea.yMax

    companion object {
        fun trajectory(initialVelocity: Vector2D) = generateSequence(Probe(velocity = initialVelocity)) { it.step() }
    }
}

val inputRegex = "target area: x=(-?[0-9]+)..(-?[0-9]+), y=(-?[0-9]+)..(-?[0-9]+)".toRegex()
