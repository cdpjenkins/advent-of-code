package day19

import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Day19 {
    @Test
    internal fun `part 1 checking blueprint 1`() {

        val input = testInput

        val ston = input.map { it.matches(BLUEPRINT_REGEX) }

        println(ston)

        val blueprints = parseBlueprints(input)

        blueprints.forEach { println(it) }

        val initialState = State(oreRobots = 1)

        val maxState = initialState.walk(blueprints[0])

        maxState.geodes shouldBe 9

        // TODO moar states
    }
}

data class State(
    val oreRobots: Int = 0,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geodes: Int = 0,
    val oreRobotsInProduction: Int = 0,
    val clayRobotsInProduction: Int = 0,
    val obsidianRobotsInProduction: Int = 0,
    val geodeRobotsInProduction: Int = 0,
    val secondsRemaining: Int = 24
) {
    fun walk(blueprint: Blueprint): State {

        if (secondsRemaining == 0) {
            return this
        }

        val possibleMoves = blueprint
            .moves
            .filter { it.isApplicableTo(this) }

        val possibleNextStates = possibleMoves.map {
            val newState = it.applyTo(this)

            val newState2 = newState.copy(
                ore = ore + newState.oreRobots,
                clay = clay + newState.clayRobots,
                obsidian = obsidian + newState.obsidianRobots,
                geodes = geodes + newState.geodeRobots,

                oreRobots = oreRobots + newState.oreRobotsInProduction,
                clayRobots = clayRobots + newState.clayRobotsInProduction,
                obsidianRobots = obsidianRobots + newState.obsidianRobotsInProduction,
                geodeRobots = geodeRobots + newState.geodeRobotsInProduction,

                oreRobotsInProduction = 0,
                clayRobotsInProduction = 0,
                obsidianRobotsInProduction = 0,
                geodeRobotsInProduction = 0,

                secondsRemaining = newState.secondsRemaining - 1
            )

            newState2
        }

        val possibleFinalStates = possibleNextStates.map { it.walk(blueprint) }

        val maxState = possibleFinalStates.maxBy { it.geodes }

        return maxState
    }
}

data class Move(
    val isApplicableTo: (State) -> Boolean,
    val applyTo: (State) -> State
)

data class Blueprint(
    val id: Int,
    val oreRobotOre: Int,
    val clayRobotOre: Int,
    val obsidianRobotOre: Int,
    val obsidianRobotClay: Int,
    val geodeRobotOre: Int,
    val geodeRobotObsidian: Int
) {
    val moves =
        // todo what about applying a rule twice in one minute???
        listOf(
            Move(
                { true },
                { it }
            ),
            Move(
                { it.ore >= oreRobotOre },
                {
                    it.copy(
                        ore = it.ore - oreRobotOre,
                        oreRobotsInProduction = 1
                    )
                }
            ),
            Move(
                { it.ore >= clayRobotOre },
                {
                    it.copy(
                        ore = it.ore - clayRobotOre,
                        clayRobotsInProduction = 1
                    )
                }
            ),
            Move(
                { it.ore >= obsidianRobotOre && it.clay >= obsidianRobotClay },
                {
                    it.copy(
                        ore = it.ore - obsidianRobotOre,
                        clay = it.clay - obsidianRobotClay,
                        obsidianRobotsInProduction = 1
                    )
                }
            ),
            Move(
                { it.ore >= geodeRobotOre && it.obsidian >= geodeRobotObsidian },
                {
                    it.copy(
                        ore = it.ore - geodeRobotOre,
                        clay = it.obsidian - geodeRobotObsidian,
                        geodeRobotsInProduction = 1
                    )
                }
            ),
        )
}

private fun parseBlueprints(input: List<String>): List<Blueprint> {
    val blueprints = input.map {
        val (idString, oreRobotOreString, clayRobotOreString, obsidianRobotOreString, obsidianRobotClayString, geodeRobotOreString, geodeRobotObsidianString) = it.parseUsingRegex(
            BLUEPRINT_REGEX
        )

        val blueprint = Blueprint(
            idString.toInt(),
            oreRobotOreString.toInt(),
            clayRobotOreString.toInt(),
            obsidianRobotOreString.toInt(),
            obsidianRobotClayString.toInt(),
            geodeRobotOreString.toInt(),
            geodeRobotObsidianString.toInt()
        )

        blueprint
    }
    return blueprints
}


val BLUEPRINT_REGEX = "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()

val testInput =
    """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent().lines()
