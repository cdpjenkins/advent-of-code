package day07

import FileUtil.readInputFileToList
import RegexUtils.parseUsingRegex
import StringUtils.indentBy
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.IllegalArgumentException

class Day07 {

    @Test
    internal fun `part 1 with test data`() {
        sumOfSizesOfDirsUnder10000(testInput) shouldBe 95437
    }

    @Test
    internal fun `part 1 with real data`() {
        sumOfSizesOfDirsUnder10000(realInput) shouldBe 1908462
    }

    @Test
    internal fun `part 2 with test data`() {
        sizeOfSmallestDirThatFreesUpEnoughSpace(testInput) shouldBe 24933642
    }

    @Test
    internal fun `part 2 with real data`() {
        sizeOfSmallestDirThatFreesUpEnoughSpace(realInput) shouldBe 3979145
    }

    private fun sizeOfSmallestDirThatFreesUpEnoughSpace(input: List<String>): Long {
        val dirStructure = buildDirTree(input)

        val totalSizeUsed = dirStructure.size()
        val freeSpace = 70000000 - totalSizeUsed
        val spaceNeeded = 30000000 - freeSpace

        val nodeToDelete = dirStructure
            .traverse()
            .filter { it is DirectoryNode }
            .sortedBy { it.size() }
            .first { it.size() >= spaceNeeded }

        val sizeToDelete = nodeToDelete.size()
        return sizeToDelete
    }

    private fun sumOfSizesOfDirsUnder10000(input: List<String>): Long =
        buildDirTree(input)
            .traverse()
            .filter { it is DirectoryNode }
            .filter { it.size() <= 100000 }
            .sumOf { it.size() }

    private fun buildDirTree(input: List<String>): Inode =
        parseCommands(input)
            .buildDirStructure()

    private fun parseCommands(input: List<String>): MutableList<Command> {
        var remainingInput = input
        val commands = mutableListOf<Command>()

        while (!remainingInput.isEmpty()) {
            val (command, nextInput) = parseNextCommand(remainingInput)
            remainingInput = nextInput
            commands.add(command)
        }
        return commands
    }
}

private fun List<Command>.buildDirStructure(): Inode {
    if (this.first() != CdCommand("/")) {
        throw IllegalArgumentException(this.first().toString())
    }

    val root = DirectoryNode("/", null)
    var currentNode = root

    LsCommand(listOf()).execute(currentNode)

    this.drop(1).forEach { currentNode = it.execute(currentNode) }

    return root
}

sealed interface Inode {
    val name: String
    val parent: DirectoryNode?
    var children: MutableList<Inode>
    fun size(): Long
    fun dumpToStdout(indent: Int = 0) {
        println("${indentBy(indent)}$this ${size()}")
        children.forEach { it.dumpToStdout(indent + 2) }
    }
    fun traverse(): List<Inode> {
        // probably horrifically inefficient in Kotlin... TODO make it a seq or something
        return listOf(this) + children.flatMap { it.traverse() }
    }
}

data class DirectoryNode(override val name: String, override val parent: DirectoryNode?) : Inode {
    override var children: MutableList<Inode> = mutableListOf()
    override fun size(): Long = children.map { it.size() }.sum()
}

data class FileNode(override val name: String, override val parent: DirectoryNode, val size: Long) : Inode {
    override var children: MutableList<Inode> = mutableListOf()
    override fun size(): Long = size
}

sealed interface Command {
    fun execute(currentNode: DirectoryNode): DirectoryNode
}
data class CdCommand(val path: String): Command {
    override fun execute(currentNode: DirectoryNode): DirectoryNode =
        if (this.path == "..") {
            currentNode.parent!!
        } else {
            currentNode.children.find { it.name == this.path }!! as DirectoryNode
        }

    companion object {
        val REGEX = "^\\$ cd (.*)$".toRegex()
    }
}

data class LsCommand(val entries: List<Entry>) : Command {
    override fun execute(currentNode: DirectoryNode): DirectoryNode {
        val children = entries.map {
            it.toNode(currentNode)
        }
        currentNode.children.addAll(children)

        return currentNode
    }

    companion object {
        val REGEX = "^\\\$ ls$".toRegex()
    }
}

sealed interface Entry {
    fun toNode(parent: DirectoryNode): Inode
}

data class DirEntry(val name: String) : Entry {
    override fun toNode(parent: DirectoryNode): Inode {
        return DirectoryNode(name, parent)
    }

    companion object {
        val REGEX = "^dir (.*)$".toRegex()
    }
}

data class FileEntry(val name: String, val size: Long) : Entry {
    override fun toNode(parent: DirectoryNode): Inode {
        return FileNode(name, parent, this.size)
    }

    companion object {
        val REGEX = "^(\\d+) (.*)$".toRegex()
    }
}

fun parseNextCommand(input: List<String>): Pair<Command, List<String>> {
    val command = input.first()
    val rest = input.drop(1)

    when {
        command.matches(CdCommand.REGEX) -> {
            val (path) = command.parseUsingRegex(CdCommand.REGEX)
            return Pair(CdCommand(path), rest)
        }
        command.matches(LsCommand.REGEX) -> {
            val dirEntriesStrings = rest.takeWhile { !it.startsWith("$") }
            val entries = dirEntriesStrings.map { it.parseDirEntry() }

            return Pair(LsCommand(entries), rest.dropWhile { !it.startsWith("$") } )
        }
        else -> {
            throw IllegalArgumentException(command)
        }
    }
}

private fun String.parseDirEntry(): Entry {
    when {
        this.matches(DirEntry.REGEX) -> {
            val (name) = this.parseUsingRegex(DirEntry.REGEX)
            return DirEntry(name)
        }
        this.matches(FileEntry.REGEX) -> {
            val (size, name) = this.parseUsingRegex(FileEntry.REGEX)
            return FileEntry(name, size.toLong())
        }
        else -> throw IllegalArgumentException(this)
    }
}

val testInput =
    """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent().lines()

val realInput = readInputFileToList("day07.txt")
