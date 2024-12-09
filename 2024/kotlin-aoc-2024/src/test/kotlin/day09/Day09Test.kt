package day09

import FileUtil.readInputFileToString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

private fun part1(input: String): Long =
    input.parseToBlocks()
        .compact()
        .checksum()

private fun List<Int>.checksum(): Long =
    this.filter { it != -1 }
        .mapIndexed { index, value -> (index * value).toLong() }
        .sum()

private fun List<Int>.compact(): List<Int> {
    val disk = this.toMutableList()

    while (!disk.isFullyCompacted()) {
        val firstFreeSpaceIndex = disk.indexOfFirst { it == -1 }
        val lastOccupiedSpaceIndex = disk.indexOfLast { it != -1 }

        disk[firstFreeSpaceIndex] = disk[lastOccupiedSpaceIndex]
        disk[lastOccupiedSpaceIndex] = -1
    }

    return disk.toList()
}

private fun List<Int>.isFullyCompacted(): Boolean {
    val firstFreeSpaceIndex = this.indexOfFirst { it == -1 }
    val lastOccupiedSpaceIndex = this.indexOfLast { it != -1 }

    return lastOccupiedSpaceIndex < firstFreeSpaceIndex
}

private fun String.parseToBlocks(): List<Int> =
    this.pad()
        .map { it.digitToInt() }
        .windowed(2, 2)
        .flatMapIndexed { i, (fileLangth, freeSpaceLength) ->
            List(fileLangth) { i } + List(freeSpaceLength) { -1 }
        }

private fun String.pad(): String {
    return if (this.length.isOdd()) this + '0'
    else this
}

private fun Int.isOdd() = this % 2 == 1
private fun Int.isEven() = !isOdd()

private fun part2(input: String): Int {

    val (files, spaces) = input.parseToFiles()

    println("files: $files")
    println("spaces: $spaces")

//    val listIterator = frikkinLinkedList.listIterator()


//    println(diskAsFiles)

    return 123
}

private fun String.parseToFiles(): Pair<List<DiskFile>, List<FreeSpace>> {
    val thangs = this
        .pad()
        .map { it.digitToInt() }

    var position = 0
    val files = mutableListOf<DiskFile>()
    val spaces = mutableListOf<FreeSpace>()
    thangs.withIndex().forEach { (i, length) ->
        val id = i/2
        if (i.isEven()) {
            files.add(DiskFile(position, length, id))
        } else {
            spaces.add(FreeSpace(position, length))
        }
        position += length
    }

    return Pair(files.toList(), spaces.toList())
}

sealed interface Element {
    fun asString(): String

    val id: Int
    val length: Int
    val position: Int
}

data class DiskFile(
    override val position: Int,
    override val length: Int,
    override val id: Int,
) : Element {
    override fun asString() = List(length) { '0' + id}.joinToString("")
}

data class FreeSpace(
    override val position: Int,
    override val length: Int,
    override val id: Int = -1,
) : Element {
    override fun asString() = List(length) { '.' }.joinToString("")
}

class Day09Test {
    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 1928
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToString("day09.txt").trimEnd()) shouldBe 6346871685398L
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToString("day09.txt")) shouldBe -1
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
        val (files, freeSpaces) = "12345".parseToFiles()
        filesAsString(files, freeSpaces) shouldBe "0..111....22222"
    }

    @Test
    fun `can parse test input into files`() {
        val (files, freeSpaces) = testInput.parseToFiles()
        filesAsString(files, freeSpaces) shouldBe "00...111...2...333.44.5555.6666.777.888899"
    }

    private fun filesAsString(files: List<DiskFile>, freeSpaces: List<FreeSpace>): String {
        val lastFreeSpace = freeSpaces.last()


        val mutableList = List(lastFreeSpace.position + lastFreeSpace.length) { -1 }.toMutableList()

        files.forEach { file ->
            (0 until file.length).forEach { i ->
                mutableList[file.position + i] = file.id
            }
        }

        return mutableList.asString()
    }
}

private fun List<Int>.asString(): String {
    return this.map {
        require(it in -1..9)
        if (it == -1) '.'
        else '0' + it
    }.joinToString("")
}

val testInput = """2333133121414131402"""
