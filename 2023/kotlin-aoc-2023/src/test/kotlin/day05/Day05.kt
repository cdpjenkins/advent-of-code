package day05

import FileUtil.readInputFileToList
import ListUtils.parseSpaceSeparatedNumberList
import ListUtils.splitByBlank
import RegexUtils.parseUsingRegex
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

private fun part1(input: List<String>): Long {
    val initialSeeds = parseInitialSeeds(input)
    val mappings: Map<Pair<String, String>, Mapping> = parseMappings(input)

    return initialSeeds.minOf { mappings.mapItAllTheWay(it) }
}

private fun part2(input: List<String>): Long {
    val initialSeedsRanges = parseInitialSeedsAsRanges(input)
    val mappings: Map<Pair<String, String>, Mapping> = parseMappings(input)

    val mappedOnce = mappings.mapSeedRangesAllTheWay(initialSeedsRanges)

    println(mappedOnce)

//    return initialSeeds.minOf { mappings.mapItAllTheWay(it) }

    return mappedOnce.minOf { it.start }

}

private fun parseMappings(testInput: List<String>): Map<Pair<String, String>, Mapping> =
    testInput
        .splitByBlank()
        .drop(1)
        .map { it.parseMap() }
        .map { Pair(it.from, it.to) to it }
        .toMap()

val headerRegex = "([a-z]+)-to-([a-z]+) map:"
private fun List<String>.parseMap(): Mapping {
    val (from, to) = this.first().parseUsingRegex(headerRegex)

    return Mapping(
        from,
        to,
        ranges = drop(1).map { parseRange(it) }
    )
}

private fun parseRange(it: String): MappingRange {
    val (destinationRangeStart, sourceRangeStart, rangeLength) = parseSpaceSeparatedNumberList(it)

    return MappingRange(destinationRangeStart, sourceRangeStart, rangeLength)
}

private fun Map<Pair<String, String>, Mapping>.mapItAllTheWay(seed: Long): Long {
    // bit of a hack but it works for now
    val soil = this["seed" to "soil"]!!.mapNumber(seed)
    val fertilizer = this["soil" to "fertilizer"]!!.mapNumber(soil)
    val water = this["fertilizer" to "water"]!!.mapNumber(fertilizer)
    val light = this["water" to "light"]!!.mapNumber(water)
    val temperature = this["light" to "temperature"]!!.mapNumber(light)
    val humidity = this["temperature" to "humidity"]!!.mapNumber(temperature)
    val location = this["humidity" to "location"]!!.mapNumber(humidity)

    return location
}

private fun Map<Pair<String, String>, Mapping>.mapSeedRangesAllTheWay(
    initialSeedsRanges: List<SeedRange>
): List<SeedRange> {

    // bit of a hack but it works for now
    val soil = this["seed" to "soil"]!!.mapRanges(initialSeedsRanges)
    val fertilizer = this["soil" to "fertilizer"]!!.mapRanges(soil)
    val water = this["fertilizer" to "water"]!!.mapRanges(fertilizer)
    val light = this["water" to "light"]!!.mapRanges(water)
    val temperature = this["light" to "temperature"]!!.mapRanges(light)
    val humidity = this["temperature" to "humidity"]!!.mapRanges(temperature)
    val location = this["humidity" to "location"]!!.mapRanges(humidity)

    return location
}

class Mapping(
    val from: String,
    val to: String,
    val ranges: List<MappingRange>
) {
    fun mapNumber(sourceNumber: Long): Long =
        ranges
            .firstNotNullOfOrNull { it.mapNumber(sourceNumber) }
            ?: sourceNumber

    fun mapRange(seedRange: SeedRange): List<SeedRange> {
        return ranges.mapNotNull { it.mapRange(seedRange) }
    }

    fun mapRanges(seedRanges: List<SeedRange>): List<SeedRange> {

        return seedRanges.flatMap {
            this.mapRange(it)
        }
    }

}
data class MappingRange(
    val destinationRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long
) {
    val sourceRange = SeedRange(sourceRangeStart, rangeLength)

    fun mapNumber(sourceNumber: Long): Long? {
        val offset = sourceNumber - sourceRangeStart
        return if (offset >= 0 && offset < rangeLength) {
            destinationRangeStart + offset
        } else {
            null
        }
    }

    fun mapRange(thatRange: SeedRange): SeedRange? {
        val overlap = sourceRange.overlapWith(thatRange)
        return overlap
    }


}

val seedsToPlantRegex = "^seeds: (.*)$".toRegex()

private fun parseInitialSeeds(testInput: List<String>): List<Long> {
    val (seedsListString) = testInput.first().parseUsingRegex(seedsToPlantRegex)

    return seedsListString
        .split(" ")
        .map { it.toLong() }
}

fun parseInitialSeedsAsRanges(input: List<String>): List<SeedRange> {
    val (seedsListString) = testInput.first().parseUsingRegex(seedsToPlantRegex)

    val seedsList = parseSpaceSeparatedNumberList(seedsListString)

    val ranges = seedsList.chunked(2).map { (start, length) -> SeedRange(start, length) }

    return ranges
}

data class SeedRange(
    val start: Long,
    val length: Long
) {
    val endExclusive = start + length

    fun overlapWith(that: SeedRange): SeedRange? {
        val start = maxOf(this.start, that.start)
        val end = minOf(this.endExclusive, that.endExclusive)

        if (end <= start) return null

        return SeedRange(start, end - start)
    }
}

private infix fun Long.within(seedRange: SeedRange): Boolean {
    val offset = this - seedRange.start
    return offset < seedRange.length
}

class Day05Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 35
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day05.txt")) shouldBe 165788812L
    }

    @Disabled
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 46
    }


    @Test
    fun `seed-to-soil mappings work correctly`() {
        val mappings = parseMappings(testInput)
        val seedToSoil = mappings["seed" to "soil"]!!

        seedToSoil.mapNumber(79) shouldBe 81
        seedToSoil.mapNumber(14) shouldBe 14
        seedToSoil.mapNumber(55) shouldBe 57
        seedToSoil.mapNumber(13) shouldBe 13
    }


    @Test
    fun `non-overlapping ranges do not overlap`() {
        SeedRange(0, 5).overlapWith(SeedRange(10, 5)) shouldBe null
        SeedRange(10, 5).overlapWith(SeedRange(0, 5)) shouldBe null
    }

    @Test
    fun `overlapping range where first range starts first returns overlap`() {
        SeedRange(0, 5).overlapWith(SeedRange(2, 5)) shouldBe SeedRange(2, 3)
    }

    // there are probably loads more tests for overlapping ranges but meh, the logic looks good now
}


val testInput =
    """
        seeds: 79 14 55 13
        
        seed-to-soil map:
        50 98 2
        52 50 48
        
        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15
        
        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4
        
        water-to-light map:
        88 18 7
        18 25 70
        
        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13
        
        temperature-to-humidity map:
        0 69 1
        1 0 69
        
        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent().lines()