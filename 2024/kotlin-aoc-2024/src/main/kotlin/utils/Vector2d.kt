package utils

// This will work great until we need Longs instead and Kotlin generics aren't quite up to it...
data class Vector2D(val x: Int, val y: Int) {
    operator fun minus(that: Vector2D): Vector2D =
        Vector2D(
            this.x - that.x,
            this.y - that.y
        )

    operator fun plus(that: Vector2D): Vector2D =
        Vector2D(
            this.x + that.x,
            this.y + that.y
        )
}
