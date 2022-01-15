package day16

import readInput

fun main() {
    val input = readInput("Day16").first()

    println(versionSum(input))
    println(evaluate(input))
}

fun evaluate(hex: String): Long =
    BinarySource.of(hex)
        .parsePacket()
        .evaluate()

fun versionSum(hex: String) =
    BinarySource.of(hex)
        .parsePacket()
        .versionSum()

private fun String.pad(): String {
    val bitsToPad = if (length % 4 != 0) {
        4 - (length % 4)
    } else {
        0
    }
    return "0".repeat(bitsToPad) + this
}


data class BinarySource(var binaryString: String) {
    fun parsePacket(): Packet {
        val version = take(3).toInt(2)
        val packetTypeId = TypeID.of(take(3).toInt(2))

        if (packetTypeId == TypeID.LITERAL) {
            var accumulator = 0L
            do {
                var done = false

                val chunk = take(5)
                if (chunk.first() == '0') {
                    done = true
                }

                accumulator *= 16
                accumulator += chunk.drop(1).toInt(2)
            } while (!done)

            return Literal(version, accumulator)
        } else {
            val lengthTypeID = take(1)

            when (lengthTypeID) {
                "0" -> {
                    val length = take(15).toInt(2)

                    val subPacketsSource = BinarySource(take(length))
                    val subPackets = mutableListOf<Packet>()
                    while (subPacketsSource.binaryString.isNotEmpty()) {
                        subPackets.add(subPacketsSource.parsePacket())
                    }
                    return packetTypeId.operator(version, subPackets)
                }
                "1" -> {
                    val numSubPackets = take(11).toInt(2)

                    val subPackets = mutableListOf<Packet>()
                    for (i in 1..numSubPackets) {
                        subPackets.add(parsePacket())
                    }
                    return packetTypeId.operator(version, subPackets)
                }
                else -> {
                    throw AssertionError(lengthTypeID)
                }
            }
        }
    }

    private fun take(bits: Int): String {
        val taken = binaryString.take(bits)
        binaryString = binaryString.drop(bits)
        return taken
    }

    companion object {
        private fun String.toBinary() = this.map { hexDigitToBinaryString(it) }.joinToString("")
        private fun hexDigitToBinaryString(hexDigit: Char) = hexDigit.toString().toInt(radix = 16).toString(radix = 2).pad()
        fun of(hex: String) = BinarySource(hex.toBinary())
    }
}


sealed class Packet(val version: Int) {
    abstract fun versionSum(): Int
    abstract fun evaluate(): Long
}

data class Literal(val literalVersion: Int, val value: Long): Packet(literalVersion) {
    override fun versionSum() = this.version
    override fun evaluate() = value
}

data class Operator(val operatorVersion: Int, val subPackets: List<Packet>, val sum: TypeID): Packet(operatorVersion) {
    override fun versionSum() = this.version + subPackets.sumOf { it.versionSum() }
    override fun evaluate(): Long = sum.evaluate(this.subPackets)
}

enum class TypeID(val id: Int) {
    SUM(0) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>): Long = subPackets.sumOf { it.evaluate() }
    },
    PRODUCT(1) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) = subPackets.map { it.evaluate() }.fold(1L) { acc, x -> acc * x }
    },
    MINIMUM(2) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) = subPackets.map { it.evaluate() }.minByOrNull { it }!!
    },
    MAXIMUM(3) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) = subPackets.map { it.evaluate() }.maxByOrNull { it }!!
    },
    LITERAL(4) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>): Long {
            throw AssertionError("Due to sloppy coding, this does not get called")
        }
    },
    GREATER_THAN(5) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) =
            if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1L else 0L
    },
    LESS_THAN(6) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) =
            if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1L else 0L
    },
    EQUAL_TO(7) {
        override fun operator(version: Int, subPackets: List<Packet>): Operator = Operator(version, subPackets, this)
        override fun evaluate(subPackets: List<Packet>) =
            if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1L else 0L
    };

    abstract fun operator(version: Int, subPackets: List<Packet>): Operator
    abstract fun evaluate(subPackets: List<Packet>): Long

    companion object {
        fun of(value: Int) = values().first { it.id == value }
    }
}
