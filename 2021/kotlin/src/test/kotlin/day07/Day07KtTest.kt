package day07

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day07KtTest {
    @Test
    internal fun name() {
        val fuel = day07MinFuel(input) { it }

        assertThat(fuel, equalTo(37))
    }
}

val input = """
    16,1,2,0,4,2,7,1,2,14
""".trimIndent().lines()