package toasted.pocket_sprite.util

import android.graphics.Point
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density
import androidx.lifecycle.MutableLiveData
import toasted.pocket_sprite.viewmodel.MainViewModel

class TouchInputHandler {

    private val _pixelPerfectEnabled = MutableLiveData(false)
    val pixelPerfectEnabled = _pixelPerfectEnabled

    fun executeTouch(viewModel: MainViewModel,change: PointerInputChange, pointerInput: PointerInputScope, density: Density) {
        val currentTool = viewModel.toolMgr.currentTool.value ?: return
        currentTool.executeTouch(viewModel, change, pointerInput, density)
    }

    fun pixelPerfectTouch(coordinate: Coordinate, cellX: Int, cellY: Int, gridCellSize: Int): Boolean {
        val point = Point(coordinate.x, coordinate.y)

        // Define triangles based on the cellX and cellY
        val topLeftTriangle = arrayOf(
            Point(cellX, cellY),
            Point(cellX + 8, cellY),
            Point(cellX, cellY + 8)
        )

        val topRightTriangle = arrayOf(
            Point(cellX + gridCellSize, cellY),
            Point(cellX + 8, cellY),
            Point(cellX + gridCellSize, cellY + 8)
        )

        val bottomLeftTriangle = arrayOf(
            Point(cellX, cellY + gridCellSize),
            Point(cellX, cellY + 8),
            Point(cellX + 8, cellY + gridCellSize)
        )

        val bottomRightTriangle = arrayOf(
            Point(cellX + gridCellSize, cellY + gridCellSize),
            Point(cellX + gridCellSize, cellY + 8),
            Point(cellX + 8, cellY + gridCellSize)
        )

        // Define other triangles similarly...

        // Check if the point is in any of the triangles
        return isPointInTriangle(point, topLeftTriangle[0], topLeftTriangle[1], topLeftTriangle[2])
                || isPointInTriangle(point, topRightTriangle[0], topRightTriangle[1], topRightTriangle[2])
                || isPointInTriangle(point, bottomLeftTriangle[0], bottomLeftTriangle[1], bottomLeftTriangle[2])
                || isPointInTriangle(point, bottomRightTriangle[0], bottomRightTriangle[1], bottomRightTriangle[2])
    }

    fun getPointCell(gridCellSize: Int, coordinate: Coordinate): Coordinate {
        val cellX = (coordinate.x / gridCellSize)
        val cellY = (coordinate.y / gridCellSize)
        return Coordinate(cellX, cellY)
    }

    fun togglePixelPerfect() {
        val pixelPerfectValue = _pixelPerfectEnabled.value ?: false
        _pixelPerfectEnabled.value = !pixelPerfectValue
    }


}