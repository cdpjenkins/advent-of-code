package day09

fun part1(input: String): Long =
    input.parseToBlocks()
        .compact()
        .checksum()

private fun List<Int>.checksum(): Long =
    this.filter { it != -1 }
        .mapIndexed { index, value -> (index * value).toLong() }
        .sum()

fun List<Int>.compact(): List<Int> {
    val disk = this.toMutableList()

    var freePointer = disk.indexOfFirst { it == -1 }
    var lastOccupiedPointer = disk.indexOfLast { it != -1 }

    while (freePointer < lastOccupiedPointer) {
        disk[freePointer] = disk[lastOccupiedPointer]
        disk[lastOccupiedPointer] = -1

        while (disk[freePointer] != -1)
            freePointer++
        while (disk[lastOccupiedPointer] == -1)
            lastOccupiedPointer--
    }

    return disk.toList()
}

fun List<Int>.isFullyCompacted(): Boolean {
    val firstFreeSpaceIndex = this.indexOfFirst { it == -1 }
    val lastOccupiedSpaceIndex = this.indexOfLast { it != -1 }

    return lastOccupiedSpaceIndex < firstFreeSpaceIndex
}

fun String.parseToBlocks(): List<Int> =
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

fun part2(input: String): Long {

    val (filesList, spacesList) = input.parseToFiles()

    val newFiles = mutableListOf<DiskFile>()
    val spaces = spacesList.toMutableList()

    filesList.reversed().forEach { file ->
        val spaceIndex = spaces.indexOfFirst { file.fitsIn(it) }

        if (spaceIndex == -1) {
            newFiles.add(file)
        } else {
            val space = spaces[spaceIndex]

            newFiles.add(file.copy(position = space.position))
            spaces[spaceIndex] =
                space.copy(position = space.position + file.length, length = space.length - file.length)
        }
    }

    return newFiles.map { it.checksum() }.sum()
}

fun String.parseToFiles(): Pair<List<DiskFile>, List<FreeSpace>> {
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
    fun fitsIn(space: FreeSpace) = space.length >= this.length && space.position < this.position
    fun checksum(): Long {
        // remember triangle numbers at school? hmmm?
        return (0 until length).map { p->
            ((position + p) * id).toLong()
        }.sum()
    }
}

data class FreeSpace(
    override val position: Int,
    override val length: Int,
    override val id: Int = -1,
) : Element {
    override fun asString() = List(length) { '.' }.joinToString("")
}


fun List<Int>.asString(): String {
    return this.map {
        require(it in -1..9)
        if (it == -1) '.'
        else '0' + it
    }.joinToString("")
}