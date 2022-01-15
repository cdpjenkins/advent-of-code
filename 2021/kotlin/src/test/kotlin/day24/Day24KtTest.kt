package day24

import org.junit.jupiter.api.Test

internal class Day24KtTest {
    @Test
    internal fun `day 24 part 1`() {
        val input = """
            inp w
            add z w
            mod z 2
            div w 2
            add y w
            mod y 2
            div w 2
            add x w
            mod x 2
            div w 2
            mod w 2
        """.trimIndent().lines()

        transpile(input)
    }
}
