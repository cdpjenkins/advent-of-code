package day16

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Day16KtTest {
    @ParameterizedTest
    @CsvSource(
        "8A004A801A8002F478,16",
        "620080001611562C8802118E34,12",
        "C0015000016115A2E0802F182340,23",
        "A0016C880162017C3686B18A3D4780,31"
    )
    internal fun `day 16 part 1`(hex: String, expectedSum: Int) {
        assertThat(versionSum(hex), equalTo(expectedSum))
    }

    @ParameterizedTest
    @CsvSource(
        "C200B40A82,3",
        "04005AC33890,54",
        "880086C3E88112,7",
        "CE00C43D881120,9",
        "D8005AC2A8F0,1",
        "F600BC2D8F,0",
        "9C005AC2F8F0,0",
        "9C0141080250320F1802104A08,1"
    )
    internal fun `day 16 part 2 evaluates packets`(hex: String, expectedResult: Long) {
        assertThat(evaluate(hex), equalTo(expectedResult))
    }

    @Test
    fun `can parse literal`() {
        assertThat(BinarySource.of("D2FE28").parsePacket(), equalTo(Literal(6, 2021)))
    }

    @Test
    fun `parses operator with sub-packets defined by length`() {
        assertThat(
            BinarySource.of("38006F45291200").parsePacket(),
            equalTo(
                Operator(
                    1,
                    listOf(
                        Literal(6, 10),
                        Literal(2, 20)
                    ),
                    TypeID.LESS_THAN
                )
            )
        )
    }

    @Test
    fun `parses operator with sub-packets defined by number`() {
        assertThat(
            BinarySource.of("EE00D40C823060").parsePacket(),
            equalTo(
                Operator(
                    7,
                    listOf(
                        Literal(2, 1),
                        Literal(4, 2),
                        Literal(1, 3)
                    ),
                    TypeID.MAXIMUM
                )
            )
        )
    }
}
