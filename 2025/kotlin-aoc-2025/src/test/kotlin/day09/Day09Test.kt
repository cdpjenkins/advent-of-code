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
import kotlin.math.max
import kotlin.math.min

private fun part1(input: List<String>): Long {
    val points = input.map { it.toPoint() }
    val pairs = points.pairs()

    return pairs.maxOf { (p1, p2) -> Rectangle.of(p1, p2).area }
}

private fun part2(input: List<String>): Long {
    val points = input.map { it.toPoint() }
    val pairs = points.pairs()
    val edges = (points + points.first()).zipWithNext().map { (p1, p2) -> Edge.of(p1, p2) }

    val rectangles = pairs.map { (p1, p2) -> Rectangle.of(p1, p2) }
    val possibleRectangles = rectangles.filter { it.isPossibleWithin(edges) }

    return possibleRectangles.maxOf { it.area }
}

private fun List<Vector2D>.pairs(): List<Pair<Vector2D, Vector2D>> =
    (0..<size).flatMap { i ->
        ((i + 1)..<size).map { j ->
            this[i] to this[j]
        }
    }

data class Rectangle(
    val leftX: Int,
    val topY: Int,
    val rightX: Int,
    val bottomY: Int
) {
    val topEdge: Edge by lazy { Edge(Vector2D(leftX, topY), Vector2D(rightX, topY), HORIZONTAL) }
    val bottomEdge: Edge by lazy { Edge(Vector2D(leftX, bottomY), Vector2D(rightX, bottomY), HORIZONTAL) }
    val rightEdge: Edge by lazy { Edge(Vector2D(rightX, topY), Vector2D(rightX, bottomY), VERTICAL) }
    val leftEdge: Edge by lazy { Edge(Vector2D(leftX, topY), Vector2D(leftX, bottomY), VERTICAL) }

    val area: Long by lazy { (rightX.toLong() - leftX + 1) * (bottomY - topY + 1)  }

    fun isPossibleWithin(edges: List<Edge>): Boolean {
        return !edges.any { this@Rectangle.intersectedBy(Edge.of(it.p1, it.p2)) }
    }

    companion object {
        fun of(start: Vector2D, end: Vector2D): Rectangle =
            Rectangle(
                min(start.x, end.x),
                min(start.y, end.y),
                max(start.x, end.x),
                max(start.y, end.y)
            )
    }
}

private fun Rectangle.intersectedBy(edge: Edge) =
    when (edge.direction) {
        VERTICAL -> {
            edge.intersects(topEdge) || edge.intersects(bottomEdge) ||
                    (edge.p1.x in inclusiveRange(leftX, rightX) &&
                            edge.p1.y in exclusiveRange(topY, bottomY) &&
                            edge.p2.y in exclusiveRange(topY, bottomY)
                            )
        }

        HORIZONTAL -> {
            edge.intersects(leftEdge) || edge.intersects(rightEdge) ||
                    (edge.p1.y in inclusiveRange(topY, bottomY) &&
                            edge.p1.x in exclusiveRange(leftX, rightX) &&
                            edge.p2.x in exclusiveRange(leftX, rightX)
                            )
        }
    }

private fun exclusiveRange(x1: Int, x2: Int) = min(x1, x2)..max(x1, x2)
fun inclusiveRange(x1: Int, x2: Int) = (min(x1, x2)+1)..<max(x1, x2)


enum class Direction {
    VERTICAL, HORIZONTAL;

    companion object {
        fun of(p1: Vector2D, p2: Vector2D) =
            when {
                p1.x == p2.x -> VERTICAL
                p1.y == p2.y -> HORIZONTAL
                else -> throw IllegalArgumentException("strewth: $p1, $p2")
            }
    }
}

data class Edge(val p1: Vector2D, val p2: Vector2D, val direction: Direction) {
    fun intersects(that: Edge) =
        when (this.direction) {
            VERTICAL if that.direction == HORIZONTAL -> {
                this.p1.x in inclusiveRange(that.p1.x, that.p2.x) && that.p1.y in inclusiveRange(this.p1.y, this.p2.y)
            }
            HORIZONTAL if that.direction == VERTICAL -> {
                this.p1.y in inclusiveRange(that.p1.y, that.p2.y) && that.p1.x in inclusiveRange(this.p1.x, this.p2.x)
            }
            else -> {
                throw IllegalArgumentException("$this, $that")
            }
        }

    companion object {
        fun of(p1: Vector2D, p2: Vector2D): Edge {
            val direction = Direction.of(p1, p2)
            return Edge(p1, p2, direction)
        }
    }
}

private fun String.toPoint(): Vector2D {
    val (x, y) = split(",").map { it.toInt() }

    return Vector2D(x, y)
}

class EdgeVisualiserPanel(
    val edges: List<Edge>,
    val points: List<Vector2D>,
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
                VERTICAL -> Color.BLUE
                HORIZONTAL -> Color.RED
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

fun visualiseEdges(input: List<String>, scale: Double) {
    val points = input.map { it.toPoint() }
    val edges = (points + points.first()).zipWithNext().map { (p1, p2) -> Edge.of(p1, p2) }

    val panel = EdgeVisualiserPanel(edges, points, scale)
    val scrollPane = JScrollPane(panel)

    val frame = JFrame("Edge Visualization")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(scrollPane)
    frame.setSize(800, 800)
    frame.isVisible = true
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

    @Test
    fun `part 2 with test input`() {
        part2(testInput) shouldBe 24L
    }

    @Test
    fun `part 2 with real input`() {
        part2(readInputFileToList("day09.txt")) shouldBe 1396494456L
    }

    @Ignore
    @Test
    fun `visualise edges with test input`() {
        visualiseEdges(testInput, scale = 40.0)
        Thread.sleep(10000)
    }

    @Ignore
    @Test
    fun `visualise edges with real input`() {
        visualiseEdges(readInputFileToList("day09.txt"), scale = 1.0/25)
        Thread.sleep(100000)
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
