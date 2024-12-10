package day09

import FileUtil.readInputFileToString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day09Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1928
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day09.txt").trimEnd()) shouldBe 6346871685398L
    }

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 2858
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day09.txt").trimEnd()) shouldBe 6373055193464L
    }

    @Test
    fun `can parse test input`() {
        "12345".parseToBlocks().asString() shouldBe "0..111....22222"
        testInput.parseToBlocks().asString() shouldBe "00...111...2...333.44.5555.6666.777.888899"
    }

    @Test
    fun `can compact a disk`() {
        val disk = testInput.parseToBlocks()
        disk.isFullyCompacted() shouldBe false
        disk.compact().asString() shouldBe "0099811188827773336446555566.............."
    }

    @Test
    fun `can parse simple input into files`() {
        val (files, _) = "12345".parseToFiles()
        filesAsString(files) shouldBe "0..111....22222"
    }

    @Test
    fun `can parse test input into files`() {
        val (files, _) = testInput.parseToFiles()
        filesAsString(files) shouldBe "00...111...2...333.44.5555.6666.777.888899"
    }
}

fun filesAsString(files: List<DiskFile>): String {
    val last = files.maxBy { it.position }
    val mutableList = List(last.position + last.length ) { -1 }.toMutableList()

    files.forEach { file ->
        (0 until file.length).forEach { i ->
            mutableList[file.position + i] = file.id
        }
    }

    return mutableList.asString()
}

val testInput = """2333133121414131402"""
