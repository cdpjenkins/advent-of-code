package day04

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import utils.realInput
import utils.splitList

class Day04Test : FunSpec({
    test("day 4 part 1 with test data") {
        testInput.numValidPassports() shouldBe 2
    }

    test("day 4 part 1 with real data") {
        realInput.numValidPassports() shouldBe 237
    }

    test("day 4 part 2 invalid passports are invalid") {
        testInputInvalidPassports.numValidPassportsCheckingFields() shouldBe 0
    }

    test("day 4 part 2 valid passports are valid") {
        testInputValidPassports.numValidPassportsCheckingFields() shouldBe 4
    }

    test("day 4 part 2 real data") {
        realInput.numValidPassportsCheckingFields() shouldBe 172
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

private fun List<String>.numValidPassportsCheckingFields() =
    this.toPassports()
        .filter { it.hasRequiredFields() }
        .filter { it.requiredFieldsHaveValidValues() }
        .count()

private fun Map<String, String>.requiredFieldsHaveValidValues() =
    this["byr"].isValidYear(1920..2002) &&
            this["iyr"].isValidYear(2010..2020) &&
            this["eyr"].isValidYear(2020..2030) &&
            this["hgt"].isValidHgt() &&
            this["hcl"]!!.matches(hclRegex) &&
            this["ecl"]!!.matches(eclRegex) &&
            this["pid"]!!.matches(pidRegex)

fun String?.isValidHgt() =
    if (this!!.matches(heightRegex)) {
        val (magnitudeString, unit) = heightRegex.find(this)!!.destructured
        val magnitude = magnitudeString.toInt()
        when (unit) {
            "cm" -> (150..193).contains(magnitude)
            "in" -> (59..76).contains(magnitude)
            else -> false
        }
    } else {
        false
    }

private fun String?.isValidYear(yearRange: IntRange) =
    this!!.matches("[0-9]{4}".toRegex()) && (yearRange).contains(toInt())

private fun List<String>.numValidPassports() =
    this.toPassports()
        .filter { it.hasRequiredFields() }
        .count()

private fun Map<String, String>.hasRequiredFields() = this.keys.containsAll(listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"))
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

val fieldRegex = "([a-z]{3}):([#0-9a-z]+)".toRegex()
val heightRegex = "([0-9]{2,3})(cm|in)".toRegex()
val hclRegex = "#[0-9a-f]{6}".toRegex()
val eclRegex = "amb|blu|brn|gry|grn|hzl|oth".toRegex()
val pidRegex = "[0-9]{9}".toRegex()


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

val realInput = realInput("Day04")

val testInputInvalidPassports = """
            eyr:1972 cid:100
            hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

            iyr:2019
            hcl:#602927 eyr:1967 hgt:170cm
            ecl:grn pid:012533040 byr:1946

            hcl:dab227 iyr:2012
            ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

            hgt:59cm ecl:zzz
            eyr:2038 hcl:74454a iyr:2023
            pid:3556412378 byr:2007
        """.trimIndent().lines()

val testInputValidPassports = """
            pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
            hcl:#623a2f

            eyr:2029 ecl:blu cid:129 byr:1989
            iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

            hcl:#888785
            hgt:164cm byr:2001 iyr:2015 cid:88
            pid:545766238 ecl:hzl
            eyr:2022

            iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
        """.trimIndent().lines()
