package utils

enum class Direction {
    NORTH {
        override fun turnLeft() = WEST
        override fun turnRight() = EAST
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(0, -1)
        override fun asChar() = '^'
    },
    SOUTH {
        override fun turnLeft() = EAST
        override fun turnRight() = WEST
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(0, 1)
        override fun asChar() = 'v'
    },
    EAST {
        override fun turnLeft() = NORTH
        override fun turnRight() = SOUTH
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(1, 0)
        override fun asChar() = '>'
    },
    WEST {
        override fun turnLeft() = SOUTH
        override fun turnRight() = NORTH
        override fun move(pos: Vector2D): Vector2D = pos + Vector2D(-1, 0)
        override fun asChar() = '<'
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun move(pos: Vector2D): Vector2D
    abstract fun asChar(): Char?

    companion object {
        fun of(c: Char) =
            when (c) {
                '^' -> NORTH
                '>' -> EAST
                '<' -> WEST
                'v' -> SOUTH
                else -> throw IllegalArgumentException("$c")
            }
    }
}