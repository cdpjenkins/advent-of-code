package day19

import FileUtil.readInputFileToList
import ListUtils.splitByBlank
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Int {
    val (workflows, ratings) = parseInput(input)

    return ratings
        .filter { workflows.processAllTheWay(it) == "A" }
        .sumOf { it.ratingsSum() }
}

private fun part2(input: List<String>): Long {
    val (workflows, _) = parseInput(input)

    return workflows
        .applyTo(listOf(RatingsWithRange("in")))
        .sumOf { it.numCombinations }
}

fun Map<String, Workflow>.applyTo(ratingsWithRanges: List<RatingsWithRange>): List<RatingsWithRange> {
    val toProcess = ratingsWithRanges.toMutableList()
    val processed = mutableListOf<RatingsWithRange>()

    while (toProcess.isNotEmpty()) {
        val thang = toProcess.removeLast()

        when (thang.name) {
            "A" -> processed.add(thang)
            "R" -> {} // meh don't care about this one if it was rejected
            else -> {
                val result = this[thang.name]!!.applyTo(thang)
                toProcess.addAll(result)
            }
        }
    }

    return processed
}

private fun Map<String, Workflow>.processAllTheWay(rating: Ratings) =
    generateSequence("in") { this[it]!!.applyTo(rating) }
        .first { it == "A" || it == "R" }

private fun parseInput(input: List<String>): Pair<Map<String, Workflow>, List<Ratings>> {
    val (workflowsInput, ratingsInput) = input.splitByBlank()

    return Pair(
        workflowsInput.map { Workflow.of(it) }.map { it.name to it }.toMap(),
        ratingsInput.map { Ratings.of(it) }
    )
}

data class Range(
    val min: Int,
    val max: Int,
) {
    fun cutBy(operator: Char, operand: Int): Pair<Range?, Range?> =
        when (operator) {
            '<' -> when {
                operand <= min -> Pair(null, this)
                operand > max -> Pair(this, null)
                else -> Pair(Range(min, operand - 1), Range(operand, max))
            }
            '>' -> when {
                operand < min -> Pair(this, null)
                operand >= max -> Pair(null, this)
                else -> Pair(Range(operand + 1, max), Range(min, operand))
            }
            '_' -> Pair(this, null)
            else -> throw IllegalStateException(this.toString())
        }

    val size: Long = max.toLong() - min.toLong() + 1
}

data class RatingsWithRange(
    val name: String,
    val x: Range = Range(1, 4000),
    val m: Range = Range(1, 4000),
    val a: Range = Range(1, 4000),
    val s: Range = Range(1, 4000)
) {
    fun get(category: String): Range {
        return when (category) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw IllegalArgumentException(category)
        }
    }

    fun with(category: String, range: Range): RatingsWithRange {
        return when (category) {
            "x" -> this.copy(x = range)
            "m" -> this.copy(m = range)
            "a" -> this.copy(a = range)
            "s" -> this.copy(s = range)
            else -> throw IllegalArgumentException(category)
        }
    }

    val numCombinations: Long = x.size * m.size * a.size * s.size
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

    fun ratingsSum(): Int = x + m + a + s

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
    fun applyTo(ratings: Ratings): String =
        rules
            .first { it.matches(ratings) }
            .applyTo(ratings)

    fun applyTo(a: RatingsWithRange): List<RatingsWithRange> {
        check(name == a.name) { "$name != ${a.name}" }

        val toApplyTo = mutableListOf<RatingsWithRange>(a)
        val doneApplyingTo = mutableListOf<RatingsWithRange>()

        rules.forEach { rule ->
            if (toApplyTo.isNotEmpty()) {
                val ratingsWithRange = toApplyTo.removeLast()

                val (applied, notApplied) = rule.applyTo(ratingsWithRange)

                if (applied != null) doneApplyingTo.add(applied)
                if (notApplied != null) toApplyTo.add(notApplied)
            }
        }

        if (toApplyTo.isNotEmpty()) {
            throw IllegalStateException("arghghgh ${toApplyTo}")
        }

        return doneApplyingTo
    }

    companion object {
        val workflowRegex = "^([a-z]+)\\{(.+)\\}$".toRegex()
        fun of(input: String): Workflow {
            val (name, rulesString) = input.parseUsingRegex(workflowRegex)

            val rules = rulesString.split(",").map { Rule.of(it) }

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

    fun applyTo(ratingsWithRange: RatingsWithRange): Pair<RatingsWithRange?, RatingsWithRange?> {
        if (category == "meh") {
            return Pair(ratingsWithRange.copy(name = destination), null)
        }
        val applicableRange = ratingsWithRange.get(category)

        val (positiveCut, negativeCut) = applicableRange.cutBy(operator, operand)

        val positiveRange = positiveCut?.let { ratingsWithRange.with(category, it) }
        val negativeRange = negativeCut?.let { ratingsWithRange.with(category, it) }

        return Pair(positiveRange?.copy(name = destination), negativeRange?.copy(name = destination))
    }

    companion object {
        val ruleRegex = "^([^:]*:)?([AR]|[a-z]+)$".toRegex()
        val conditionRegex = "^([xmas])([<>])([0-9]+)$".toRegex()
        fun of(it: String): Rule =
            if (it.contains(":")) {
                val (conditionStringWithColon, destination) = it.parseUsingRegex(ruleRegex)

                val conditionString = conditionStringWithColon.removeSuffix(":")

                val (categoryRating, operatorString, operandString) =
                    conditionString.parseUsingRegex(conditionRegex)

                Rule(categoryRating, operatorString[0], operand = operandString.toInt(), destination)
            } else {
                Rule(category = "meh", operand = 0, operator = '_', destination = it)
            }
    }
}

class Day19Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 19114
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day19.txt")) shouldBe 489392
    }

    @Test
    fun `part 2 with test input`() {
        // urhghghg very close but the wrong freaking answer
        part2(testInput) shouldBe 167409079868000
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day19.txt")) shouldBe 134370637448305
    }

    @Test
    fun `apply workflow to input for fun and profit`() {
        val workflow = Workflow.of("px{a<2006:qkq,m>2090:A,rfg}")

        val ratings = Ratings.of("{x=1679,m=44,a=2067,s=496}")

        workflow.applyTo(ratings) shouldBe "rfg"
    }

    @Test
    fun `workflow sends us through the correct sequence`() {
        val (workflows, _) = parseInput(testInput)

        val ratings = Ratings.of("{x=787,m=2655,a=1222,s=2876}")

        val seqMeDouble = generateSequence("in") { workflows[it]!!.applyTo(ratings) }
            .takeWhile { it != "A" && it != "R" }
            .toList()

        seqMeDouble shouldBe listOf("in", "qqz", "qs", "lnx")
    }

    @Test
    fun `terminates with right thang`() {
        val (workflows, _) = parseInput(testInput)

        val ratings = Ratings.of("{x=787,m=2655,a=1222,s=2876}")

        generateSequence("in") { workflows[it]!!.applyTo(ratings) }
            .first { it == "A" || it == "R" } shouldBe "A"
    }

    @Test
    fun `can apply a trivial rule to a range`() {
        val resultRange = Workflow.of("in{A}").applyTo(RatingsWithRange("in"))

        resultRange shouldBe listOf(RatingsWithRange("A"))
        resultRange.sumOf { it.numCombinations } shouldBe 4000L * 4000L * 4000L * 4000L
    }

    @Test
    fun `can apply a rule that splits a range`() {
        val ratingsWithRange = RatingsWithRange("cheese", x = Range(100, 3900))
        val workflow = Workflow.of("cheese{x<2000:ston,A}")

        workflow.applyTo(ratingsWithRange) shouldBe listOf(
            RatingsWithRange("ston", x = Range(100, 1999)),
            RatingsWithRange("A", x = Range(2000, 3900))
        )
    }

    @Test
    fun `cutting up a range for fun and profit`() {
        val range = Range(100, 200)

        range.cutBy('<', 100) shouldBe Pair(null, Range(100, 200))
        range.cutBy('<', 101) shouldBe Pair(Range(100, 100), Range(101, 200))
        range.cutBy('<', 200) shouldBe Pair(Range(100, 199), Range(200, 200))
        range.cutBy('<', 201) shouldBe Pair(Range(100, 200), null)

        range.cutBy('>', 99) shouldBe Pair(Range(100, 200), null)
        range.cutBy('>', 100) shouldBe Pair(Range(101, 200), Range(100, 100))
        range.cutBy('>', 199) shouldBe Pair(Range(200, 200), Range(100, 199))
        range.cutBy('>', 200) shouldBe Pair(null, Range(100, 200))

        range.cutBy('_', 12345678) shouldBe Pair(Range(100, 200), null)
    }

    @Test
    fun `correctly calculates number of combinations`() {
        // (1, 4000), (1, 838), (1, 1716), (1351, 2770)
        RatingsWithRange(
            "whatever",
            Range(1, 4000),
            Range(1, 838),
            Range(1, 1716),
            Range(1351, 2770)
        ).numCombinations shouldBe 8167885440000L
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
