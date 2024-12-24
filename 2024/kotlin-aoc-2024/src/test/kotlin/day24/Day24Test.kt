package day24

import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class Day24Test {
    @Test
    fun `part 1 with simple test input`() {
        val input = simpleTestInput

        val device = Device.of(input)
        device.getValueFor("z00") shouldBe 0
        device.getValueFor("z01") shouldBe 0
        device.getValueFor("z02") shouldBe 1

        part1(simpleTestInput) shouldBe 4
    }

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 2024
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day24.txt")) shouldBe 58367545758258
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day24.txt")) shouldBe "bpf,fdw,hcc,hqc,qcw,z05,z11,z35"
    }

    @Ignore
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `can set input and compute shizzle`() {
        val initialDevice = Device.of(readInputFileToList("day24.txt"))

        val device = initialDevice
            .withX(0x1FFFFFFFF0)
            .withY(0x0FFFFFFFF1)

        // this fails due to the gates that need swapping
        device.getZ().toHexString() shouldBe (0x1FFFFFFFF0 + 0x0FFFFFFFF1).toHexString()

        // Possible future improvement: Actually implement swapping of gates so we can
        // guarantee that we're fixing the right gates
    }

    @Test
    fun `can generate graphviz graph`() {
        Device.of(readInputFileToList("day24.txt")).printGraphViz()
    }
}

val simpleTestInput =
    """
        x00: 1
        x01: 1
        x02: 1
        y00: 0
        y01: 1
        y02: 0

        x00 AND y00 -> z00
        x01 XOR y01 -> z01
        x02 OR y02 -> z02
    """.trimIndent().lines()

val testInput =
    """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1
        
        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
    """.trimIndent().lines()
