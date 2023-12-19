package day19

import ListUtils.splitByBlank
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val (ratings, workflows) = parseInput(input)

    ratings.forEach(::println)

    return 123
}

private fun parseInput(input: List<String>): Pair<Map<String, Workflow>, List<Ratings>> {
    val (workflowsInput, ratingsInput) = input.splitByBlank()

    val workflowsList = workflowsInput.map { Workflow.of(it) }
    val workflows = workflowsList.map { it.name to it }.toMap()
    val ratings = ratingsInput.map { Ratings.of(it) }
    return Pair(workflows, ratings)
}

data class Ratings(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    operator fun get(category: String): Int {
        return when (category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw IllegalArgumentException(category)
        }
    }

    companion object {
        fun of(input: String): Ratings {
            val regex = "^\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}$".toRegex()

            val (x, m, a, s) = input.parseUsingRegex(regex)

            return Ratings(
                x.toInt(),
                m.toInt(),
                a.toInt(),
                s.toInt()
            )
        }
    }

}

data class Workflow(
    val name: String,
    val rules: List<Rule>
) {
    fun applyTo(ratings: Ratings): String {
        val theRule = rules.first { it.matches(ratings) }
        val dest = theRule.applyTo(ratings)

        return dest
    }

    companion object {
        fun of(input: String): Workflow {
            val workflowRegex = "^([a-z]+)\\{(.+)\\}$".toRegex()

            val (name, rulesString) = input.parseUsingRegex(workflowRegex)

            val ruleStringList = rulesString.split(",")
            val rules = ruleStringList.map { Rule.of(it) }

            return Workflow(name, rules)
        }
    }
}

data class Rule(
    val category: String,
    val operator: Char,
    val operand: Int,
    val destination: String
) {
    fun matches(ratings: Ratings) =
        when (operator) {
            '<' -> ratings[category] < operand
            '>' -> ratings[category] > operand
            '_' -> true
            else -> throw IllegalStateException(this.toString())
        }

    fun applyTo(ratings: Ratings): String {
        return destination
    }

    companion object {
        fun of(it: String): Rule {
            println("gonna parse $it")

            if (it.contains(":")) {
                val (conditionStringWithColin, destination) = it.parseUsingRegex("^([^:]*:)?([AR]|[a-z]+)$")

                val conditionString = conditionStringWithColin.removeSuffix(":")
                println(conditionString)
                val (categoryRating, operatorString, operandString) =
                    conditionString.parseUsingRegex("^([xmas])([<>])([0-9]+)$")

                val rule = Rule(categoryRating, operatorString[0], operand = operandString.toInt(), destination)

                return rule
            } else {
                return Rule(category = "meh", operand = 0, operator = '_', destination = it)
            }
        }
    }
}

private fun part2(input: List<String>): Int {
    return 123
}

class Day19Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe -1
    }
//
//    @Ignore
//    @Test
//    fun `part 1 with real input`() {
//        part1(readInputFileToList("day_template.txt")) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with test input`() {
//        part2(testInput) shouldBe -1
//    }
//
//    @Ignore
//    @Test
//    fun `part 2 with real input`() {
//        part2(readInputFileToList("day_template.txt")) shouldBe -1
//    }

    @Test
    fun `apply workflow to input for fun and profit`() {
        val workflow = Workflow.of("px{a<2006:qkq,m>2090:A,rfg}")

        val ratings = Ratings.of("{x=1679,m=44,a=2067,s=496}")

        val dest = workflow.applyTo(ratings)

        dest shouldBe "rfg"

    }

    @Test
    fun `frikkin sequence`() {
        val (workflows, ratingsList) = parseInput(testInput)

        val ratings = Ratings.of("{x=787,m=2655,a=1222,s=2876}")

        val seqMeDouble = generateSequence("in") { workflows[it]!!.applyTo(ratings) }
            .takeWhile { it != "A" && it != "R" }
            .toList()

        seqMeDouble shouldBe listOf("in", "qqz", "qs", "lnx")
    }

    @Test
    fun `terminates with right thang`() {
        val (workflows, ratingsList) = parseInput(testInput)

        val ratings = Ratings.of("{x=787,m=2655,a=1222,s=2876}")

        val seqMeDouble = generateSequence("in") { workflows[it]!!.applyTo(ratings) }

        seqMeDouble.first { it == "A" || it == "R" } shouldBe "A"
    }

}

val testInput =
    """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent().lines()
