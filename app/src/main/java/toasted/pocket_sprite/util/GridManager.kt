package toasted.pocket_sprite.util

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs


class GridManager {

    private val _gridEnabled = MutableLiveData(true)
    private val _gridCellSize = MutableLiveData(DEFAULT_GRID_CELL_SIZE)
    private val _gridHeight = MutableLiveData(DEFAULT_GRID_HEIGHT)
    private val _gridWidth = MutableLiveData(DEFAULT_GRID_WIDTH)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    val gridEnabled: LiveData<Boolean> = _gridEnabled
    val gridCellSize: LiveData<Int> = _gridCellSize
    val gridHeight: LiveData<Int> = _gridHeight
    val gridWidth: LiveData<Int> = _gridWidth
    val gridColor: LiveData<Color> = _gridColor

    private var checkerPatternBitmap: ImageBitmap? = null

    fun createGrid(canvas: DrawScope, scale: Float) {
        val gridEnabled = gridEnabled.value ?: return
        if (!gridEnabled) return
        if (checkerPatternBitmap == null || checkerPatternBitmap!!.width != canvas.size.width.toInt() || checkerPatternBitmap!!.height != canvas.size.height.toInt()) {
            checkerPatternBitmap = createBackgroundCheckerPattern(canvas.size)
        }

        // Draw the checker pattern
        checkerPatternBitmap?.let { canvas.drawImage(it, topLeft = Offset.Zero) }
        val gridCellSize = gridCellSize.value ?: return
        val gridColor = gridColor.value ?: return
        val paint = Paint().apply { color = gridColor }
        Log.d("Grid", "Canvas Height: ${canvas.size.height.toInt()}")
        Log.d("Grid", "Canvas height cells: ${canvas.size.height.toInt()/gridCellSize}")
        for (i in 0 until canvas.size.height.toInt()/gridCellSize) {
            canvas.drawContext.canvas.drawLine(
                Offset(0f, (i * gridCellSize).toFloat()),
                Offset(canvas.size.width, (i * gridCellSize).toFloat()),
                paint
            )
        }
        for (j in 0 until canvas.size.width.toInt()/gridCellSize) {
            canvas.drawContext.canvas.drawLine(
                Offset((j * gridCellSize).toFloat(), 0f),
                Offset((j * gridCellSize).toFloat(), canvas.size.height),
                paint
            )

        }
    }

    /**
     * Toggle grid enabled
     *
     */
    fun toggleGridEnabled() {
        val gridEnabled = gridEnabled.value ?: return
        _gridEnabled.value = !gridEnabled
    }

    /**
     * Set grid color
     *
     * @param color
     */
    fun setGridColor(color: Color) {
        _gridColor.value = color
    }

    /**
     * Create grid checker pattern
     *
     * @param size
     */
    private fun createBackgroundCheckerPattern(size: Size): ImageBitmap {
        val gridCellSize = gridCellSize.value ?: return ImageBitmap(0, 0)
        val paint = Paint().apply { color = Color.LightGray}
        // Create an off-screen bitmap and canvas
        val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        val canvas = Canvas(bitmap)
        for (i in 0 until size.height.toInt()) {
            for (j in 0 until size.width.toInt()) {
                if ((i + j) % 2 == 0) {
                    canvas.drawRect(
                        ((j * gridCellSize).toFloat() / .0625).toFloat(),
                        ((i * gridCellSize).toFloat() / .0625).toFloat(),
                        (((j + 1) * gridCellSize).toFloat() / .0625).toFloat(),
                        (((i + 1) * gridCellSize).toFloat() / .0625).toFloat(),
                        paint
                    )
                }
            }
        }
        return bitmap
    }

    fun interpolatePoints(coordinate1: Coordinate, coordinate2: Coordinate): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        var x1 = coordinate1.x
        var y1 = coordinate1.y
        var x2 = coordinate2.x
        var y2 = coordinate2.y
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
            coordinates.add(if (isSteep) Coordinate(y, x) else Coordinate(x, y))
            error -= dy
            if (error < 0) {
                y += yStep
                error += dx
            }
        }
        return coordinates

    }
}