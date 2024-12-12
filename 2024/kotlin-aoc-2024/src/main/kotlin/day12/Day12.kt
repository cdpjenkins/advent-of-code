package day12

fun part1(input: List<String>): Int {
    val map = input.parseToMap()
    val regions = map.keys.regions(neighboursIn(map))
    return regions.sumOf { it.area() * it.perimeter() }
}

fun part2(input: List<String>): Int {
    val map: Map<Point2D, Char> = input.parseToMap()
    val regions: Set<Set<Point2D>> = map.keys.regions(neighboursIn(map))
    return regions.sumOf { it.numSides() * it.area() }
}

fun Set<Point2D>.area() = this.size

fun Set<Point2D>.perimeter() =
    this.flatMap { it.neighbours() }
        .count { it !in this }

fun Set<Point2D>.numSides() =
    listOf(
        leftEdges(),
        rightEdges(),
        topEdges(),
        bottomEdges()
    ).flatten()
        .size

fun neighboursIn(map: Map<Point2D, Char>) =
    { point: Point2D -> point.neighboursIn(map).filter { map[point] == map[it] } }

fun Set<Point2D>.leftEdges() =
    this.filter { it.leftNeighbour() !in this }
        .toSet()
        .regions(neighboursFn = Point2D::verticalNeighbours)

fun Set<Point2D>.rightEdges() =
    this.filter { it.rightNeighbour() !in this }
        .toSet()
        .regions(neighboursFn = Point2D::verticalNeighbours)

fun Set<Point2D>.topEdges() =
    this.filter { it.upNeighbour() !in this }
        .toSet()
        .regions(neighboursFn = Point2D::horizontalNeighbours)

fun Set<Point2D>.bottomEdges() =
    this.filter { it.downNeighbour() !in this }
        .toSet()
        .regions(neighboursFn = Point2D::horizontalNeighbours)

fun Set<Point2D>.regions(neighboursFn: (Point2D) -> List<Point2D>): Set<Set<Point2D>> {
    val visited = mutableSetOf<Point2D>()

    fun floodFillToFindAllSquaresWithinRegion(p: Point2D, region: MutableSet<Point2D>) {
        if (p in this && p !in visited) {
            visited.add(p)
            region.add(p)
            neighboursFn(p).forEach { floodFillToFindAllSquaresWithinRegion(it, region) }
        }
    }

    val regions = mutableSetOf<Set<Point2D>>()
    forEach { p ->
        if (p !in visited) {
            val region = mutableSetOf(p)
            floodFillToFindAllSquaresWithinRegion(p, region)
            regions.add(region)
        }
    }
    return regions
}

data class Point2D(val x: Int, val y: Int) {
    fun neighboursIn(map: Map<Point2D, Char>): List<Point2D> =
        neighbours().filter { it in map.keys }

    fun neighbours() =
        listOf(
            leftNeighbour(),
            rightNeighbour(),
            upNeighbour(),
            downNeighbour()
        )

    fun upNeighbour() = Point2D(x, y - 1)
    fun downNeighbour() = Point2D(x, y + 1)
    fun leftNeighbour() = Point2D(x - 1, y)
    fun rightNeighbour() = Point2D(x + 1, y)

    fun verticalNeighbours() = listOf(upNeighbour(), downNeighbour())
    fun horizontalNeighbours() = listOf(leftNeighbour(), rightNeighbour())
}

fun List<String>.parseToMap() =
    flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c ->
            Point2D(x, y) to c
        }
    }.toMap()
