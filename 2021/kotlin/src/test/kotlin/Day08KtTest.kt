import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day08KtTest {
    @Test
    internal fun `day 08 part 1`() {
        val result = day08Part1CountDigits(lines)
        assertThat(result, equalTo(26))
    }

    @Test
    fun `day 08 part 2 me do`() {
        val result = day10Part2RepairIncompleteLines(lines)
        assertThat(result, equalTo(61229))
    }

    @Test
    internal fun `part 2 works out one mapping for test data`() {
        val signalPatterns = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab".split(" ")

        val mapping = workOutMapping(signalPatterns)

        assertThat(
            mapping,
            equalTo(
                mapOf(
                    'd' to setOf('a'),
                    'e' to setOf('b'),
                    'a' to setOf('c'),
                    'f' to setOf('d'),
                    'g' to setOf('e'),
                    'b' to setOf('f'),
                    'c' to setOf('g'),
                )
            )
        )
    }


}

val lines = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
""".trimIndent().lines()
