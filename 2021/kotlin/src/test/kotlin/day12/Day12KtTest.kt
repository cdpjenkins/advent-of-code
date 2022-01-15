package day12

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class Day12KtTest {
    @Test
    fun `day 12 part 1 using test data`() {
        assertThat(findNumberOfLegalPaths(simpleInput), equalTo(10))
    }

    @Test
    fun `day 12 part 1 using slightly larger example`() {
        assertThat(findNumberOfLegalPaths(slightlyLargerExampleInput), equalTo(19))
    }

    @Test
    fun `day 12 part 1 even larger example input`() {
        assertThat(findNumberOfLegalPaths(evenLargerExampleInput), equalTo(226))
    }

    @Test
    fun `day 12 part 2 simple test data`() {
        val result = findNumberOfLegalPathsVisitingLittleCavesAtMostTwice(simpleInput)
        assertThat(result, equalTo(36))
    }

    @Test
    fun `day 12 part 2 using slightly larger example`() {
        assertThat(findNumberOfLegalPathsVisitingLittleCavesAtMostTwice(slightlyLargerExampleInput), equalTo(103))
    }

    @Test
    fun `day 12 part 2 even larger example input`() {
        assertThat(findNumberOfLegalPathsVisitingLittleCavesAtMostTwice(evenLargerExampleInput), equalTo(3509))
    }

}

val simpleInput = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent().lines()
val slightlyLargerExampleInput = """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent().lines()

val evenLargerExampleInput = """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent().lines()
