package day14

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import readInput

class Day14KtTest {
    @Test
    internal fun `day 14 part 1 with test data`() {
        val day14Part1 = day14Part1(input)

        assertThat(day14Part1, equalTo(1588))
    }

    @Test
    internal fun `day 14 part 1 with real date`() {
        val input = readInput("Day14")
        assertThat(day14Part1(input), equalTo(2584))
    }

    @Test
    internal fun `day 14 part 2 with test data`() {
        assertThat(day14Part2(input), equalTo(2188189693529L))
    }

    @ParameterizedTest
    @CsvSource(
        "NNCB,NCNBCHB",
        "NBCCNBBBCBHCB,NBBBCNCCNBBNBNBBCHBHHBCHB"
    )
    internal fun `processStep is vaguely sane`(inputPolymer: String, outputPolymer: String) {
        val (_, rulesMap) = parseInput(input)

        assertThat(processStep(inputPolymer, rulesMap), equalTo(outputPolymer))
    }
}

val input = """
            NNCB

            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent().lines()
