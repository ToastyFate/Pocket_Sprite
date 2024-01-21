package toasted.pocket_sprite.util

import android.graphics.Point
import kotlin.math.abs

fun interpolatePoints(dx1: Int, dy1: Int, dx2: Int, dy2: Int): List<Point> {
    val points = mutableListOf<Point>()
    var x1 = dx1
    var y1 = dy1
    var x2 = dx2
    var y2 = dy2
    val isSteep = abs(y2 - y1) > abs(x2 - x1)
    if (isSteep) {
        var t = x1
        x1 = y1
        y1 = t
        t = x2
        x2 = y2
        y2 = t
    }
    if (x1 > x2) {
        var t = x1
        x1 = x2
        x2 = t
        t = y1
        y1 = y2
        y2 = t
    }
    val dx = x2 - x1
    val dy = abs(y2 - y1)
    var error = dx / 2
    val yStep = if (y1 < y2) 1 else -1
    var y = y1
    for (x in x1 until x2 + 1) {
        points.add(if (isSteep) Point(y, x) else Point(x, y))
        error -= dy
        if (error < 0) {
            y += yStep
            error += dx
        }
    }
    return points
}

