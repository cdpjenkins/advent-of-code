import java.lang.Math.abs

fun main() {
    val input = readInput("Day19")

    val scanners = input.parse().findAllTheScanners()

    println(scanners.findNumberOfBeacons())
    println(scanners.maxManhattenDistance())
}

fun List<String>.maxManhattenDistance() = parse().findAllTheScanners().maxManhattenDistance()

fun Set<ScannerReport>.maxManhattenDistance() =
    flatMap {
        map { that -> it.location.manhattenDistance(that.location) }
    }.maxOfOrNull { it }!!

fun List<String>.findNumberOfBeacons() =
    this.parse()
        .findAllTheScanners()
        .findAllTheBeacons()
        .size

fun Set<ScannerReport>.findNumberOfBeacons() = findAllTheBeacons().size

fun Set<ScannerReport>.findAllTheBeacons(): Set<Vector3D> =
    this.fold(emptySet()) { acc, report -> acc.union(report.scans) }

private fun List<ScannerReport>.findAllTheScanners(): Set<ScannerReport> {
    var correctlyOrientedSet: Set<ScannerReport> = setOf(this[0])
    var remaining = minus(correctlyOrientedSet)

    while (!remaining.isEmpty()) {
        correctlyOrientedSet.forEach { correctlyOrientedReport ->
            remaining.forEach {
                if (correctlyOrientedReport.scannerNumber != it.scannerNumber) {
                    val maybeMatch = correctlyOrientedReport.tryToMatchWith(it)
                    if (maybeMatch != null) {
                        println("${correctlyOrientedReport.scannerNumber} -> ${it.scannerNumber}")
                        remaining = remaining.minus(it)
                        correctlyOrientedSet = correctlyOrientedSet.plus(maybeMatch)
                    }
                }
            }
        }
    }
    return correctlyOrientedSet
}

private fun List<String>.parse(): MutableList<ScannerReport> {
    var input = this

    val scannerReports = mutableListOf<ScannerReport>()
    while (!input.isEmpty()) {
        input = input.dropWhile { it.isEmpty() }
        val header = input.first()
        val headerRegex = "--- scanner ([0-9]+) ---".toRegex()
        val (scannerNumber) = headerRegex.find(header)!!.destructured

        val coordLines = input.drop(1).takeWhile { it.isNotEmpty() }
        val scans = coordLines.map {
            val (x, y, z) = "(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)".toRegex().find(it)!!.destructured
            Vector3D(x.toInt(), y.toInt(), z.toInt())
        }.toSet()

        scannerReports.add(ScannerReport(scannerNumber.toInt(), scans, Vector3D.ZERO))

        input = input.dropWhile { it.isNotEmpty() }
    }
    return scannerReports
}

data class ScannerReport(val scannerNumber: Int, val scans: Set<Vector3D>, val location: Vector3D) {
    fun tryToMatchWith(that: ScannerReport): ScannerReport? {
        this.scans.forEach { thisScan ->
            Matrix3D.EVERY_TRANSFORM.forEach { rotation ->
                val rotatedScans = that.scans.map { rotation * it }
                rotatedScans.forEach { thatScan ->
                    val scansWithCorrectOrientation = rotatedScans.map { it - thatScan + thisScan }.toSet()
                    if (scansWithCorrectOrientation.intersect(scans).size == 12) {
                        return that.copy(scans = scansWithCorrectOrientation, location = thatScan - thisScan)
                    }
                }
            }
        }

        // we did not find a match
        return null
    }
}

data class Vector3D(val x: Int, val y: Int, val z: Int) {
    fun magnitudeSquared() = x*x + y*y + z*z

    operator fun plus(that: Vector3D) =
        Vector3D(
            this.x + that.x,
            this.y + that.y,
            this.z + that.z
        )

    operator fun minus(that: Vector3D) =
        Vector3D(
            this.x - that.x,
            this.y - that.y,
            this.z - that.z
        )

    operator fun unaryMinus() = Vector3D(-this.x, -this.y, -this.z)

    fun manhattenDistance(that: Vector3D) = abs(this.x - that.x) + abs(this.y - that.y) + abs(this.z - that.z)

    companion object {
        val ZERO = Vector3D(0, 0, 0)
    }
}

data class Matrix3D(
    val m11: Int,
    val m21: Int,
    val m31: Int,
    val m12: Int,
    val m22: Int,
    val m32: Int,
    val m13: Int,
    val m23: Int,
    val m33: Int
) {
    operator fun times(v: Vector3D): Vector3D =
        Vector3D(
            m11 * v.x + m21 * v.y + m31 * v.z,
            m12 * v.x + m22 * v.y + m32 * v.z,
            m13 * v.x + m23 * v.y + m33 * v.z
        )

    operator fun times(m: Matrix3D): Matrix3D =
        Matrix3D(
            m11 * m.m11 + m21 * m.m12 + m31 * m.m13,
            m11 * m.m21 + m21 * m.m22 + m31 * m.m23,
            m11 * m.m31 + m21 * m.m32 + m31 * m.m33,
            m12 * m.m11 + m22 * m.m12 + m32 * m.m13,
            m12 * m.m21 + m22 * m.m22 + m32 * m.m23,
            m12 * m.m31 + m22 * m.m32 + m32 * m.m33,
            m13 * m.m11 + m23 * m.m12 + m33 * m.m13,
            m13 * m.m21 + m23 * m.m22 + m33 * m.m23,
            m13 * m.m31 + m23 * m.m32 + m33 * m.m33
        )

    fun print() {
        println("$m11 $m21 $m31")
        println("$m12 $m22 $m32")
        println("$m13 $m23 $m33")
    }

    companion object {
        val IDENTITY =
            Matrix3D(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
            )

        val ROTATE_X_AXIS =
            Matrix3D(
                1, 0, 0,
                0, 0, -1,
                0, 1, 0
            )

        val ROTATE_Y_AXIS =
            Matrix3D(
                0, 0, 1,
                0, 1, 0,
                -1, 0, 0
            )

        val ROTATE_Z_AXIS =
            Matrix3D(
                0, -1, 0,
                1, 0, 0,
                0, 0, 1
            )

        val EVERY_TRANSFORM = listOf(
            IDENTITY,
            ROTATE_X_AXIS * IDENTITY,
            ROTATE_X_AXIS * ROTATE_X_AXIS * IDENTITY,
            ROTATE_X_AXIS * ROTATE_X_AXIS * ROTATE_X_AXIS * IDENTITY,
            ROTATE_Y_AXIS * IDENTITY,
            ROTATE_Y_AXIS * ROTATE_Y_AXIS * ROTATE_Y_AXIS * IDENTITY
        ).flatMap {
            listOf(
                it,
                ROTATE_Z_AXIS * it,
                ROTATE_Z_AXIS * ROTATE_Z_AXIS * it,
                ROTATE_Z_AXIS * ROTATE_Z_AXIS * ROTATE_Z_AXIS * it
            )
        }
    }
}
