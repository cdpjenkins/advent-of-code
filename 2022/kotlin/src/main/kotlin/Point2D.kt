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