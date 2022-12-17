package day17

import org.junit.jupiter.api.Test

class Day17 {


    @Test
    internal fun `part 1 test input`() {
        val testInput = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

        val input = testInput

        val shapesInput = listOf(shape1, shape2, shape3, shape4, shape5)

        val shapes = shapesInput.map { Shape.parseShape(it) }

        shapes.forEach { it.dump() }
    }
}


data class Shape(val pixels: List<List<Char>>) {
    fun dump() {
        TODO("Not yet implemented")
    }

    companion object {
        fun parseShape(input: String): Shape {
            val ston = input.lines().mapIndexed { y, l ->
                l.mapIndexed { x, c ->
                    c
                }
            }

            return Shape(ston)
        }
    }
}

val shape1 =
    """
        ####
    """.trimIndent()

val shape2 =
    """
        .#.
        ###
        .#.
    """.trimIndent()

val shape3 =
    """
        ..#
        ..#
        ###
    """.trimIndent()

val shape4 =
    """
        #
        #
        #
        #
    """.trimIndent()

val shape5 =
    """
        ##
        ##
    """.trimIndent()