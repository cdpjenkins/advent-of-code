package day09

import day09.Direction.*
import utils.FileUtil.readInputFileToList
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.Vector2D
import kotlin.test.Ignore
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane

private fun part1(input: List<String>): Long {

    val points = input.map { it.toPoint() }

    val pairs = (0..<points.size).flatMap { i ->
        ((i + 1)..<points.size).map { j ->
            points[i] to points[j]
        }
    }

    val areas = pairs.map { (p1, p2) ->
        rectangeAreaBetween(p1, p2)
    }

    val maxArea = areas.max()

    return maxArea
}

private fun part2(input: List<String>): Int {
    val points = input.map { it.toPoint() }

    val pairs = (0..<points.size).flatMap { i ->
        ((i + 1)..<points.size).map { j ->
            points[i] to points[j]
        }
    }

    val edges = (points + points.first()).zipWithNext().map { (p1, p2) -> Edge(p1, p2) }

    val upEdges = edges.filter { it.direction == UP }.sortedBy { it.p1.x }
    val rightEdges = edges.filter { it.direction == RIGHT }.sortedBy { it.p1.y }
    val downEdges = edges.filter { it.direction == DOWN }.sortedBy { it.p1.x }
    val leftEdges = edges.filter { it.direction == LEFT }.sortedBy { it.p1.y }

    val perimeter = Perimeter(upEdges, rightEdges, downEdges, leftEdges)


    perimeter.contains(Vector2D(5, 4))

//    val viableSquares = pairs.filter { (p1, p2) ->
//
//    }

//    println(viableSquares)
//    viableSquares.forEach { println(it) }
//
//
//
//    val thang = viableSquares.maxBy { (p1, p2) -> rectangeAreaBetween(p1, p2) }
//    println("thang")
//    println(thang)

    return -12
}

class Perimeter(
    val upEdges: List<Edge>,
    val rightEdges: List<Edge>,
    val downEdges: List<Edge>,
    val leftEdges: List<Edge>
) {
    fun contains(p: Vector2D) {
        upEdges.takeWhile { it.p1.x <= p.x }
        downEdges.takeWhile { it.p1.x <= p.x }


        leftEdges.takeWhile { it.p1.y <= p.y }
        rightEdges.takeWhile { it.p1.x <= p.y }


    }
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    companion object {
        fun of(p1: Vector2D, p2: Vector2D): Direction {
            return when {
                p1.x == p2.x -> {
                    when {
                        p2.y > p1.y -> DOWN
                        p1.y > p2.y -> UP
                        else -> throw IllegalArgumentException("oh noes: $p1, $p2")
                    }
                }
                p1.y == p2.y -> {
                    when {
                        p1.x > p2.x -> LEFT
                        else -> RIGHT
                    }
                }
                else -> {
                    throw IllegalArgumentException("strewth")
                }
            }
        }
    }
}

data class Edge(val p1: Vector2D, val p2: Vector2D) {
    val direction = Direction.of(p1, p2)

    fun coversPointY(p: Vector2D) {
        when (direction) {
            UP -> p.y in p2.y..p1.y
            DOWN ->  p.y in p1.y..p2.y
            else -> throw IllegalArgumentException("oh no: $direction")
        }
    }

    fun coversPointX(p: Vector2D) {
        when (direction) {
            LEFT -> p.x in p2.x..p1.x
            RIGHT ->  p.x in p1.x..p2.x
            else -> throw IllegalArgumentException("oh no: $direction")
        }
    }
}

private fun String.toPoint(): Vector2D {
    val (x, y) = split(",").map { it.toInt() }

    return Vector2D(x, y)
}

fun rectangeAreaBetween(v1: Vector2D, v2: utils.Vector2D): Long {
    return (Math.abs(v1.x.toLong() - v2.x.toLong()) + 1) * (Math.abs(v1.y.toLong() - v2.y.toLong()) + 1)
}

class EdgeVisualizerPanel(
    private val edges: List<Edge>,
    private val points: List<Vector2D>,
    val scale: Double = 40.0
) : JPanel() {
    private val offset = 50

    init {
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }

        val width = ((maxX - minX) * scale + offset * 2).toInt()
        val height = ((maxY - minY) * scale + offset * 2).toInt()

        preferredSize = Dimension(width, height)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        edges.forEach { edge ->
            val color = when (edge.direction) {
                UP -> Color.BLUE
                RIGHT -> Color.GREEN
                DOWN -> Color.RED
                LEFT -> Color.ORANGE
            }
            g2d.color = color
            g2d.drawLine(
                (edge.p1.x * scale + offset).toInt(),
                (edge.p1.y * scale + offset).toInt(),
                (edge.p2.x * scale + offset).toInt(),
                (edge.p2.y * scale + offset).toInt()
            )
        }

        g2d.color = Color.BLACK
        points.forEach { point ->
            g2d.fillOval(
                (point.x * scale + offset - 5).toInt(),
                (point.y * scale + offset - 5).toInt(),
                10,
                10
            )
        }
    }
}

fun visualizeEdges(input: List<String>, scale: Double) {
    val points = input.map { it.toPoint() }
    val edges = (points + points.first()).zipWithNext().map { (p1, p2) -> Edge(p1, p2) }

    val panel = EdgeVisualizerPanel(edges, points, scale)
    val scrollPane = JScrollPane(panel)

    val frame = JFrame("Edge Visualization")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(scrollPane)
    frame.setSize(800, 800)
    frame.isVisible = true
}

private fun Vector2D.scaleBy(scale: Int): Vector2D {
    return Vector2D(x / scale, y / scale)
}

class Day09Test {

    @Test
    fun `part 1 with test input`() {
        part1(testInput) shouldBe 50
    }

    @Test
    fun `part 1 with real input`() {
        part1(readInputFileToList("day09.txt")) shouldBe 4763040296L
    }

    @Ignore
    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe -1
    }

    @Ignore
    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day_template.txt")) shouldBe -1
    }

    @Ignore
    @Test
    fun `visualize edges with test input`() {
        visualizeEdges(testInput, scale = 40.0)
        Thread.sleep(10000)
    }

    @Ignore
    @Test
    fun `visualize edges with real input`() {
        visualizeEdges(readInputFileToList("day09.txt"), scale = 1.0/50)
        Thread.sleep(10000)
    }
}

val testInput =
    """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent().lines()
