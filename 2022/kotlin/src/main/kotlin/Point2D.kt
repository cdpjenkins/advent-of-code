import kotlin.math.absoluteValue

data class Point2D(
    val x: Int,
    val y: Int
) {
    fun manhattenDistanceTo(that: Point2D): Int {
        val dx = (this.x - that.x).absoluteValue
        val dy = (this.y - that.y).absoluteValue
        return dx + dy
    }

    operator fun plus(s: Vector2D) = Point2D(this.x + s.dx, this.y + s.dy)

    companion object {
        fun iterateRange(
            minX: Int,
            maxX: Int,
            minY: Int,
            maxY: Int
        ) = (minY..maxY).flatMap { y ->
            (minX..maxX).map { x ->
                Point2D(x, y)
            }
        }
    }
}

data class Vector2D(
    val dx: Int,
    val dy: Int
)

fun parseBoard(input: List<String>) =
    input.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            Point2D(x, y) to c
        }
    }.toMap()
