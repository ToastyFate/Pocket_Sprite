package toasted.pocket_sprite.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import toasted.pocket_sprite.viewmodel.MainViewModel

class TouchInputHandler {

    data class Point(val x: Float, val y: Float)

    fun executeTouch(viewModel: MainViewModel,change: PointerInputChange, pointerInput: PointerInputScope) {
        val currentTool = viewModel.toolMgr.currentTool.value ?: return
        currentTool.executeTouch(viewModel, change, pointerInput)
    }

    fun pixelPerfectTouch(offset: Offset, cellX: Float, cellY: Float): Boolean {
        val point = Point(offset.x, offset.y)

        // Define triangles based on the cellX and cellY
        val topLeftTriangle = arrayOf(
            Point(cellX, cellY),
            Point(cellX + 10, cellY),
            Point(cellX, cellY + 10)
        )

        val topRightTriangle = arrayOf(
            Point(cellX + 16, cellY),
            Point(cellX + 10, cellY),
            Point(cellX + 16, cellY + 10)
        )

        val bottomLeftTriangle = arrayOf(
            Point(cellX, cellY + 16),
            Point(cellX, cellY + 10),
            Point(cellX + 10, cellY + 16)
        )

        val bottomRightTriangle = arrayOf(
            Point(cellX + 16, cellY + 16),
            Point(cellX + 16, cellY + 10),
            Point(cellX + 10, cellY + 16)
        )

        // Define other triangles similarly...

        // Check if the point is in any of the triangles
        return isPointInTriangle(point, topLeftTriangle[0], topLeftTriangle[1], topLeftTriangle[2])
                || isPointInTriangle(point, topRightTriangle[0], topRightTriangle[1], topRightTriangle[2])
                || isPointInTriangle(point, bottomLeftTriangle[0], bottomLeftTriangle[1], bottomLeftTriangle[2])
                || isPointInTriangle(point, bottomRightTriangle[0], bottomRightTriangle[1], bottomRightTriangle[2])
    }

    fun getPointCell(viewModel: MainViewModel, point: android.graphics.Point): Point {
        val cellSize = viewModel.bmpManager.cellSize.value ?: return Point(0f, 0f)
        val cellX = (point.x / cellSize).toInt()
        val cellY = (point.y / cellSize).toInt()
        return Point(cellX.toFloat(), cellY.toFloat())
    }


}