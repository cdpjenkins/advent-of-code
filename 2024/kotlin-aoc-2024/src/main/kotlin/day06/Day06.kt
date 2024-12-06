package day06

fun part1(input: List<String>): Int {
    val (map, start) = input.parseInput()
    val poses = pathUntilWeFallOffTheMap(start, map)

    return poses.size
}

fun part2(input: List<String>): Int {
    val (map, start) = input.parseInput()
    val pointsOnOriginalPath = pathUntilWeFallOffTheMap(start, map) - start.position

    return pointsOnOriginalPath.count { resultsInCycle(start, map + (it to 'O')) }
}

private fun Map<Point2D, Char>.isObstacleAt(it: Point2D) = this[it] == '#' || this[it] == 'O'

fun pathUntilWeFallOffTheMap(
    start: Guard,
    map: Map<Point2D, Char>,
): Set<Point2D> =
    generateSequence(start) { it.move(map) }
        .takeWhile { it.isWithinBoundsOf(map) }
        .map { it.position }
        .toSet()

fun pathUntilWeDetectACycle(
    start: Guard,
    map: Map<Point2D, Char>
): Set<Point2D> {
    val seq = generateSequence(start) { it.move(map) }

    val seenAlready = mutableSetOf<Guard>()
    val path = mutableListOf<Guard>()
    run fromLook@ {
        seq.forEach {
            if (it in seenAlready) {
                return@fromLook
            } else if (!it.isWithinBoundsOf(map)) {
                return@fromLook
            } else {
                seenAlready.add(it)
                path.add(it)
            }
        }
    }

    return path.map { it.position }.toSet()
}

fun resultsInCycle(
    start: Guard,
    map: Map<Point2D, Char>,
): Boolean {
    val seq = generateSequence(start) { it.move(map) }

    val seenAlready = mutableSetOf<Guard>()

    seq.forEach {
        if (it in seenAlready) {
            return true
        } else if (!it.isWithinBoundsOf(map)) {
            return false
        } else {
            seenAlready.add(it)
        }
    }

    // we should never get here - but meh
    return false
}

fun List<String>.parseInput(): Pair<Map<Point2D, Char>, Guard> {
    val map = flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Point2D(x, y) to c }
    }.toMap()

    val start = map.entries.firstOrNull { (_, c) -> c == '^' }!!.key

    return Pair(map, Guard(start, Direction.UP))
}

fun asString(map: Map<Point2D, Char>, poses: Set<Point2D>): String {
    return (0 .. map.keys.maxOf { it.y }).map { y ->
        (0 .. map.keys.maxOf { it.x }).map { x ->
            if (Point2D(x, y) in poses) {
                'X'
            } else {
                map[Point2D(x, y)]
            }
        }.joinToString("")
    }.joinToString("\n")
}

data class Guard(val position: Point2D, val direction: Direction) {
    fun move(map: Map<Point2D, Char>): Guard {
        val newDirection = maybeTurn(map)
        return Guard(newDirection.move(position), newDirection)
    }

    private fun maybeTurn(map: Map<Point2D, Char>): Direction =
        if (map.isObstacleAt(direction.move(position))) Guard(position, direction.turnRight()).maybeTurn(map)
        else direction

    fun isWithinBoundsOf(map: Map<Point2D, Char>): Boolean {
        return position in map.keys
    }
}

data class Point2D(val x: Int, val y: Int)
enum class Direction {
    UP {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
        override fun move(p: Point2D) = Point2D(p.x, p.y - 1)
    },
    DOWN {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
        override fun move(p: Point2D) = Point2D(p.x, p.y + 1)
    },
    LEFT {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
        override fun move(p: Point2D) = Point2D(p.x - 1, p.y)
    },
    RIGHT {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
        override fun move(p: Point2D) = Point2D(p.x + 1, p.y)
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun move(p: Point2D): Point2D
}