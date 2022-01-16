package day04

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.readLines

class Day04Test : FunSpec({
    test("day 4 part 1 with test data") {
        testInput.numValidPassports() shouldBe 2
    }

    test("day 4 part 1 with real data") {
        realInput.numValidPassports() shouldBe 237
    }

    test("can parse one passport") {
        testInputWithOnePasswpord.toPassports() shouldBe
                listOf(
                    mapOf(
                        "ecl" to "gry",
                        "pid" to "860033327",
                        "eyr" to "2020",
                        "hcl" to "#fffffd",
                        "byr" to "1937",
                        "iyr" to "2017",
                        "cid" to "147",
                        "hgt" to "183cm"
                    )
                )
    }
})

private fun List<String>.numValidPassports() =
    this.toPassports()
        .filter { it.isValid() }
        .count()

private fun Map<String, String>.isValid() = this.keys.containsAll(listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"))
private fun List<String>.toPassports() = splitList { it.isEmpty() }.map { it.parsePassport() }
private fun List<String>.parsePassport() =
    this.joinToString(" ")
        .split(" ")
        .map { it.parseField() }
        .toMap()
private fun String.parseField(): Pair<String, String> {
    val (name, value) = fieldRegex.find(this)!!.destructured
    return name to value
}

private fun <E> List<E>.splitList(
    accumulator: List<List<E>> = emptyList(),
    predicate: (E) -> Boolean
): List<List<E>> =
    if (this.isEmpty()) accumulator
    else this.dropWhile { !predicate(it) }
        .dropWhile(predicate)
        .splitList(accumulator + listOf(takeWhile { !predicate(it) }), predicate)

val fieldRegex = "([a-z]{3}):([#0-9a-z]+)".toRegex()

val testInputWithOnePasswpord =
    """
        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
        byr:1937 iyr:2017 cid:147 hgt:183cm
    """.trimIndent().lines()

val testInput =
    """
        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
        byr:1937 iyr:2017 cid:147 hgt:183cm
        
        iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
        hcl:#cfa07d byr:1929
        
        hcl:#ae17e1 iyr:2013
        eyr:2024
        ecl:brn pid:760753108 byr:1931
        hgt:179cm
        
        hcl:#cfa07d eyr:2025 pid:166559648
        iyr:2011 ecl:brn hgt:59in
    """.trimIndent().lines()

val realInput = readLines("Day04")
