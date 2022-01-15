package day11

import day11.Grid.Companion.FLASHING
import readInput

fun main() {
    val input = readInput("Day11")

    println(numberOfFlashesAfter100Steps(input))
    println(firstStepWhereAllFlash(input))
}

private fun numberOfFlashesAfter100Steps(input: List<String>): Int {
    val octopusSequence = generateSequence(Grid.of(input)) { it.step() }
    val thang = octopusSequence.drop(100).first()
    val numFlashes = thang.flashes
    return numFlashes
}

fun firstStepWhereAllFlash(input: List<String>): Int {
    val (index, _) = generateSequence(Grid.of(input)) { it.step() }
        .withIndex()
        .first { it.value.allOctopusesFlashAtOnce() }
    return index
}

data class Point(val x: Int, val y: Int)

data class Grid(val octopuses: Map<Point, Int>, val flashes: Int = 0) {
    fun step() =
        generateSequence(step1IncrementEnergyOfAllOctopuses()) { doTheFlashingThang(it) }
            .iterateUntilThereAreNoMoreChanges()
            .resetFlashersToZero()

    private fun Sequence<Grid>.iterateUntilThereAreNoMoreChanges(): Grid {
        val (grid, _) = this.zip(this.drop(1)).find { it.first == it.second }!!
        return grid
    }

    private fun resetFlashersToZero() = this.map { p, e -> if (e < 0) 0 else e }

    private fun doTheFlashingThang(grid: Grid) =
        grid.flashIfEnergyHighEnough()
            .incrementEnergyOfNeighboursOfFlashingOctopuses()
            .finishFlashing()

    private fun step1IncrementEnergyOfAllOctopuses() = map { point, energy -> energy + 1 }
    private fun flashIfEnergyHighEnough(): Grid {
        val numNewFlashes = this.octopuses.filter { it.value > 9 }.size
        val newGrid = this.map { p, e -> if (e > 9) FLASHING else e }

        return newGrid.copy(flashes = flashes + numNewFlashes)
    }
    private fun incrementEnergyOfNeighboursOfFlashingOctopuses() = this.map { p, e -> e + numFlashingNeighbours(p) }
    private fun finishFlashing(): Grid = this.map { p, e -> if (e < 0) HAS_FLASHED else e }

    private fun numFlashingNeighbours(point: Point) =
        listOf(
            Point(point.x - 1, point.y - 1),
            Point(point.x, point.y - 1),
            Point(point.x + 1, point.y - 1),
            Point(point.x - 1, point.y),
            Point(point.x + 1, point.y),
            Point(point.x - 1, point.y + 1),
            Point(point.x, point.y + 1),
            Point(point.x + 1, point.y + 1),
        )
            .map { octopuses[it]?.isFlashing() }
            .filterNotNull()
            .filter { it == true }
            .count()

    private fun map(f: (Point, Int) -> Int): Grid {
        val newOctopouses = this.octopuses.entries.map {
            val (point, energy) = it
            point to f(point, energy)
        }.toMap()

        return Grid(newOctopouses, flashes)
    }

    override fun toString() =
        (0..9).map { y ->
            (0..9).map { x ->
                val point = Point(x, y)
                val energy = octopuses[point]!!
                val energyString = when (energy) {
                    FLASHING -> "F"
                    HAS_FLASHED -> "H"
                    else -> energy.toString()
                }

                energyString
            }.joinToString("")
        }.joinToString("\n")

    fun allOctopusesFlashAtOnce() = this.octopuses.values.all { it == 0 }

    companion object {
        const val FLASHING = -999999
        const val HAS_FLASHED = -888888

        fun of(lines: List<String>, flashes: Int = 0): Grid {
            val octopuses = lines.withIndex().flatMap { (y, line) ->
                line.withIndex().map { (x, cell) ->
                    Point(x, y) to cell.toString().toInt()
                }
            }.toMap()

            return Grid(octopuses, flashes)
        }

        fun of(str: String): Grid = of(str.trimIndent().lines())
    }
}

private fun Int.isFlashing(): Boolean {
    return this == FLASHING
}
