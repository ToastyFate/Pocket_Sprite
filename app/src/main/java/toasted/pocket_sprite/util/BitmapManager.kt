package toasted.pocket_sprite.util

import android.graphics.Bitmap
import android.util.Log
import android.util.Size

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BitmapManager {

    private val _bitmapSize = MutableLiveData(
        Size(
            DEFAULT_CANVAS_WIDTH,
            DEFAULT_CANVAS_HEIGHT
        )
    )
    private val _bitmapWidth = MutableLiveData(DEFAULT_BITMAP_WIDTH.dp)
    private val _bitmapHeight = MutableLiveData(DEFAULT_BITMAP_HEIGHT.dp)
    private val _bitmap = MutableLiveData(
        Bitmap.createBitmap(
            DEFAULT_BITMAP_WIDTH,
            DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888
        )
    )
    val bitmapSize: LiveData<Size> = _bitmapSize
    val bitmapWidth: LiveData<Dp> = _bitmapWidth
    val bitmapHeight: LiveData<Dp> = _bitmapHeight
    val bitmap: LiveData<Bitmap> = _bitmap


    /**
     * Create drawing bitmap
     *
     * @param density
     */
    fun createDrawingBitmap(density: Density) {
        val bitmapSize = bitmapSize.value ?: Size(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT)
        val width = with(density) {bitmapSize.width.dp.toPx()}
        val height = with(density) {bitmapSize.height.dp.toPx()}
        Log.d("BitmapManager", "Bitmap size: (${width}, ${height})")
        val bmp = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888
        )
        for (i in 0 until width.toInt()) {
            for (j in 0 until height.toInt()) {
                bmp.setPixel(i, j, Color.Transparent.toArgb())
            }
        }
        // Initialize the bitmap with a default color or pattern
        _bitmap.value = bmp
    }

    /**
     * Get current bitmap
     *
     * @return
     */
    fun getCurrentBitmap(): Bitmap? {
        return _bitmap.value
    }

    /**
     * Update bitmap
     *
     * @param bitmap
     */
    fun updateBitmap(bitmap: Bitmap) {
        val newBitmap = Bitmap.createBitmap(bitmap)
        _bitmap.value = newBitmap
    }

    /**
     * Set pixel
     *
     * @param x
     * @param y
     * @param color
     */
    fun setPixel(x: Int, y: Int, color: Int) {
        val bitmap = bitmap.value ?: return
        bitmap.setPixel(x, y, color)
        _bitmap.value = bitmap
    }


    /**
     * Update cell
     *
     * @param x
     * @param y
     * @param color
     */
    fun updateCell(coordinate: Coordinate, color: Color, gridCellSize: Int) {
        _bitmap.value?.let { bitmap ->
            Log.d("BitmapManager", "Updating cell: (${coordinate.x}, ${coordinate.y})")
            Log.d("BitmapManager", "Updating cell: (${coordinate.x/gridCellSize}, ${coordinate.y/gridCellSize})")
            Log.d("BitmapManager", "Updating cell: (${coordinate.x/gridCellSize} * ${gridCellSize}, ${coordinate.y/gridCellSize} * ${gridCellSize})")
            val startX = (coordinate.x / gridCellSize) * gridCellSize
            Log.d("BitmapManager", "Updating cell: (${startX}, ${startX + gridCellSize})")
            val startY = (coordinate.y / gridCellSize) * gridCellSize
            Log.d("BitmapManager", "Updating cell: (${startY}, ${startY + gridCellSize})")
            for (i in startX until startX + gridCellSize) {
                for (j in startY until startY + gridCellSize) {
                    if (i < bitmap.width && j < bitmap.height) {
                        bitmap.setPixel(i, j, color.toArgb())
                    }
                }
            }

            // Trigger an update to notify observers, if necessary
            _bitmap.value = bitmap
        }
    }


    /**
     * Update cells
     *
     * @param event
     * @param scale
     */
    fun updateCells(interpolatedCoordinates: List<Coordinate>, gridCellSize: Int, selectedColor: Color) {
        for (coordinate in interpolatedCoordinates) {
            updateCell(coordinate, selectedColor, gridCellSize)
        }
    }


    /**
     * Save current bitmap pixels
     *
     * @return
     */
    private fun saveCurrentBitmap(): String {
        val bitmap = bitmap.value ?: return ""
        val stringBuilder = StringBuilder()
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val color = bitmap.getPixel(i, j)
                stringBuilder.append(String.format("#%06X", 0xFFFFFF and color))
                stringBuilder.append(" ")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    /**
     * Load bitmap pixels
     *
     * @param bitmapString
     */
    private fun loadBitmap(bitmapString: String) {
        val bitmap = bitmap.value ?: return
        val lines = bitmapString.split("\n")
        for (i in lines.indices) {
            val line = lines[i]
            val colors = line.split(" ")
            for (j in colors.indices) {
                val color = Color(android.graphics.Color.parseColor(colors[j]))
                bitmap.setPixel(i, j, color.toArgb())
            }
        }
        _bitmap.value = bitmap
    }
}