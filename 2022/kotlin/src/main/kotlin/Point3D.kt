data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun neighbours(): List<Point3D> =
        (z - 1..z + 1).flatMap { z ->
            (y - 1..y + 1).flatMap { y ->
                (x - 1..x + 1).map { x ->
                    Point3D(x, y, z)
                }
            }
        }.filter { it != this }

    fun faces(): List<Point3D> =
        listOf(
            Point3D(x + 1, y, z),
            Point3D(x - 1, y, z),
            Point3D(x, y + 1, z),
            Point3D(x, y - 1, z),
            Point3D(x, y, z + 1),
            Point3D(x, y, z - 1)
        )

    fun withinCuboid(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): Boolean {
        return x in (minX..maxX) &&
                y in (minY..maxY) &&
                z in (minZ..maxZ)
    }
}
