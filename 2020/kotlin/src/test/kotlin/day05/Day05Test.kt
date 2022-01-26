package day05

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput

class Day05Test : FunSpec({
    test("day 5 part 1 with real data") {
        val highestSeatId = realInput
            .map { it.seatId() }
            .maxOrNull()

        highestSeatId shouldBe 871
    }

    test("day 5 part 2 with real data") {
        val seats = realInput
            .map { it.seatId() }
            .toSet()

        val yourSeat = (0..1023).find {
            !(it in seats) && (it - 1) in seats && (it + 1) in seats
        }

        yourSeat shouldBe 640
    }

    test("can parse a seat") {
        "BFFFBBFRRR".seatId() shouldBe 567
    }
})

private fun String.seatId() =
    this.replace('R', '1')
        .replace('L', '0')
        .replace('B', '1')
        .replace('F', '0')
        .toInt(2)

val realInput = realInput("day05")
