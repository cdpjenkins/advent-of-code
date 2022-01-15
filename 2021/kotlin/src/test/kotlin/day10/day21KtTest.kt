package day10

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day10KtTest {
    @Test
    internal fun `day 10 part 1`() {
        assertThat(day10Part1SyntaxErrorScore(input), equalTo(26397))
    }

    @Test
    internal fun `day 10 part 2`() {
        assertThat(day10Part2RepairIncompleteLines(input), equalTo(288957))
    }

}

var input = """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
        """.trimIndent().lines()
