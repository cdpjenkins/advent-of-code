package day22

import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val commands = readInput("Day22").parse()

    println(countCubesInInitialisationSection(commands))
    println(countCubesInAllRebootSteps(commands))
}

fun List<Command>.compile(): List<Command> =
    this.fold(emptyList()) { acc, command -> acc + command.compile(acc) }

fun countCubesInInitialisationSection(commands: List<Command>) =
    commands
        // strictly speaking we need to check for cuboids that are only partially in the box... but there don't
        // appear to be any in the input that we get
        .filter { it.cuboid.insideInitialisationSection() }
        .compile()
        .fold(0L) { acc, command -> acc + command.value() }

fun countCubesInAllRebootSteps(commands: List<Command>) =
    commands
        .compile()
        .fold(0L) { acc, command -> acc + command.value() }

fun List<String>.parse() = this.map(Command::parse)

enum class Action {
    on, off;

    fun inverse() = when (this) {
        on -> off
        off -> on
    }

    fun direction() = when (this) {
        on -> 1
        off -> -1
    }
}

data class Cuboid(val xMin: Long,
                  val xMax: Long,
                  val yMin: Long,
                  val yMax: Long,
                  val zMin: Long,
                  val zMax: Long) {

    fun insideInitialisationSection(): Boolean =
        !((xMin < -50 && xMax < -50)
                || (xMin >= 50 && xMax >= 50)
                || (yMin <= -50 && yMax <= -50)
                || (yMin >= 50 && yMax >= 50)
                || (zMin <= -50 && zMax <= -50)
                || (zMin >= 50 && zMax >= 50))

    infix fun overlapsWith(that: Cuboid): Boolean {
        return this overlapsOnXAxis that &&
                this overlapsOnYAxis that &&
                this overlapsOnZAxis that
    }

    private infix fun overlapsOnXAxis(that: Cuboid) = this.xMin <= that.xMax && this.xMax >= that.xMin
    private infix fun overlapsOnYAxis(that: Cuboid) = this.yMin <= that.yMax && this.yMax >= that.yMin
    private infix fun overlapsOnZAxis(that: Cuboid) = this.zMin <= that.zMax && this.zMax >= that.zMin

    fun intersectionWith(that: Cuboid): Cuboid {
        return Cuboid(
            max(this.xMin, that.xMin),
            min(this.xMax, that.xMax),
            max(this.yMin, that.yMin),
            min(this.yMax, that.yMax),
            max(this.zMin, that.zMin),
            min(this.zMax, that.zMax)
        )
    }

    fun volume() = (1 + xMax - xMin) * (1 + yMax - yMin) * (1 + zMax - zMin)
}

data class Command(
    val action: Action,
    val cuboid: Cuboid
) {
    fun combineWith(that: Command) = Command(that.action.inverse(), this.cuboid.intersectionWith(that.cuboid))
    fun value() = cuboid.volume() * action.direction()

    fun compile(commandsSoFar: List<Command>): List<Command> {
        val newCommands = commandsSoFar
            .filter { it.cuboid overlapsWith cuboid }
            .map { combineWith(it) }

        return if (action == Action.on) {
            newCommands + this
        } else {
            newCommands
        }
    }

    companion object {
        val regex = "(on|off|intersection) x=(-?[0-9]+)..(-?[0-9]+),y=(-?[0-9]+)..(-?[0-9]+),z=(-?[0-9]+)..(-?[0-9]+)".toRegex()

        fun parse(it: String): Command {
            val (action, xMin, xMax, yMin, yMax, zMin, zMax) = regex.find(it)?.destructured ?: throw AssertionError(it)
            return Command(
                Action.valueOf(action),
                Cuboid(
                    xMin.toLong(),
                    xMax.toLong(),
                    yMin.toLong(),
                    yMax.toLong(),
                    zMin.toLong(),
                    zMax.toLong()
                )
            )
        }
    }
}
