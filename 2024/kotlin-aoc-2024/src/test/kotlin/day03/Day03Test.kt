package day03

import utils.FileUtil.readInputFileToString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day03Test {
    @Test
    fun `part 1 with test input`() {
        part1("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))") shouldBe 161
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day03.txt")) shouldBe 170068701
    }

    @Test
    fun `part 2 with test input`() {
        part2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") shouldBe 48
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day03.txt")) shouldBe 78683433
    }
}

