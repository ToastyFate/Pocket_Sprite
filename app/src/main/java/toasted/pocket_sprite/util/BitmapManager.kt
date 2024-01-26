package toasted.pocket_sprite.util

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BitmapManager() {

    private val _gridWidth = MutableLiveData(DEFAULT_BITMAP_WIDTH.dp)
    private val _gridHeight = MutableLiveData(DEFAULT_BITMAP_HEIGHT.dp)
    private val _scaleWidth = MutableLiveData(1f)
    private val _scaleHeight = MutableLiveData(1f)
    private val _cellSize = MutableLiveData(DEFAULT_CELL_SIZE)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _bitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH,
        DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    private val _backgroundBitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH,
        DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    val gridWidth: LiveData<Dp> = _gridWidth
    val gridHeight: LiveData<Dp> = _gridHeight
    val scaleWidth: LiveData<Float> = _scaleWidth
    val scaleHeight: LiveData<Float> = _scaleHeight
    val cellSize: LiveData<Float> = _cellSize
    val selectedColor: LiveData<Color> = _selectedColor
    val bitmap: LiveData<Bitmap> = _bitmap
    val backgroundBitmap: LiveData<Bitmap> = _backgroundBitmap



    /**
     * Create drawing bitmap
     *
     * @param density
     */
    fun createDrawingBitmap(density: Density) {
        val gridWidth = gridWidth.value ?: return
        val gridHeight = gridHeight.value ?: return
        val cellSize = cellSize.value ?: return
        val width = with(density) { gridWidth.toPx() }
        val height = with(density) { gridHeight.toPx() }
        val bmp = Bitmap.createBitmap(width.toInt() * cellSize.toInt() ,
            height.toInt() * cellSize.toInt(), Bitmap.Config.ARGB_8888)
        for (i in 0 until bmp.width) {
            for (j in 0 until bmp.height) {
                bmp.setPixel(i, j, Color.Transparent.toArgb())
            }
        }
        // Initialize the bitmap with a default color or pattern
        _bitmap.value = bmp
    }

    /**
     * Create background bitmap
     *
     * @param density
     */
    fun createBackgroundBitmap(density: Density) {
        val gridWidth = gridWidth.value ?: return
        val gridHeight = gridHeight.value ?: return
        val cellSize = cellSize.value ?: return
        val width = with(density) { gridWidth.toPx() }
        val height = with(density) { gridHeight.toPx() }
        val bmp = Bitmap.createBitmap(width.toInt() * cellSize.toInt() ,
            height.toInt() * cellSize.toInt(), Bitmap.Config.ARGB_8888)
        // Initialize the bitmap with a default color or pattern
        createGridCheckerPattern(bmp)
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
     * Update cell
     *
     * @param x
     * @param y
     * @param color
     */
    fun updateCell(x: Int, y: Int, color: Color) {
        _bitmap.value?.let { bitmap ->
            val cellSize = cellSize.value?.toInt() ?: return
            val startX = x * cellSize
            val startY = y * cellSize
            for (i in startX until startX + cellSize) {
                for (j in startY until startY + cellSize) {
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
    fun updateCells(event: MotionEvent, scale: Float) {
        val cellSize = cellSize.value ?: return
        val selectedColor = selectedColor.value ?: return
        val x =
            (((event.x / cellSize) / scale).toInt()) // Convert to bitmap coordinates
        val y =
            (((event.y / cellSize) / scale).toInt())// Convert to bitmap coordinates
        updateCell(
            x,
            y,
            selectedColor
        )
    }

    /**
     * Update background bitmap size
     *
     * @param width
     * @param height
     */
    private fun updateBackgroundBitmapSize(width: Float, height: Float) {
        val cellSize = cellSize.value ?: return
        val currentBitmap = saveCurrentPixels()
        _backgroundBitmap.value?.apply {
            val bmp = Bitmap.createBitmap(width.toInt() * cellSize.toInt() ,
                height.toInt() * cellSize.toInt(), Bitmap.Config.ARGB_8888)
            // Initialize the bitmap with a default color or pattern
            createGridCheckerPattern(bmp)
            _backgroundBitmap.value = bmp
        }
        loadPixels(currentBitmap)
    }

    /**
     * Update canvas size
     *
     * @param width
     * @param height
     */
    fun updateCanvasSize(width: Float, height: Float) {
        val cellSize = cellSize.value ?: return
        updateBackgroundBitmapSize(width, height)
        val currentBitmap = saveCurrentPixels()
        _bitmap.value?.apply {
            val bmp = Bitmap.createBitmap(width.toInt() * cellSize.toInt() ,
                height.toInt() * cellSize.toInt(), Bitmap.Config.ARGB_8888)
            // Initialize the bitmap with a default color or pattern
            _bitmap.value = bmp
        }

        loadPixels(currentBitmap)
    }

    /**
     * Save current bitmap pixels
     *
     * @return
     */
    private fun saveCurrentPixels(): String {
        val bitmap = bitmap.value ?: return ""
        val cellSize = cellSize.value ?: return ""
        val stringBuilder = StringBuilder()
        for (i in 0 until bitmap.width / cellSize.toInt()) {
            for (j in 0 until bitmap.height / cellSize.toInt()) {
                val color = bitmap.getPixel(i * cellSize.toInt(), j * cellSize.toInt())
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
     * @param pixels
     */
    private fun loadPixels(pixels: String) {
        val bitmap = bitmap.value ?: return
        val lines = pixels.split("\n")
        for (i in lines.indices) {
            val line = lines[i]
            val colors = line.split(" ")
            for (j in colors.indices) {
                val color = Color(android.graphics.Color.parseColor(colors[j]))
                updateCell(i, j, color)
            }
        }
        _bitmap.value = bitmap
    }

    /**
     * Create grid checker pattern
     *
     * @param bitmap
     */
    private fun createGridCheckerPattern(bitmap: Bitmap) {
        val cellSize = cellSize.value ?: return
        for (i in 0 until bitmap.width / (cellSize.toInt() * 18)) {
            for (j in 0 until bitmap.height / (cellSize.toInt() * 18)) {
                for (k in 0 until cellSize.toInt() * 8) {
                    for (l in 0 until cellSize.toInt() * 8) {
                        bitmap.setPixel(i * cellSize.toInt() * 16 + k, j * cellSize.toInt() * 16 + l, Color.LightGray.toArgb())
                    }
                }
                for (k in cellSize.toInt() * 8 until cellSize.toInt() * 16) {
                    for (l in cellSize.toInt() * 8 until cellSize.toInt() * 16) {
                        bitmap.setPixel(i * cellSize.toInt() * 16 + k, j * cellSize.toInt() * 16 + l, Color.LightGray.toArgb())
                    }
                }
            }
        }
        _backgroundBitmap.value = bitmap
    }

    fun getCurrentScale(): Float {
        val scaleWidth = scaleWidth.value ?: return 1f
        val scaleHeight = scaleHeight.value ?: return 1f
        return scaleWidth * scaleHeight
    }

    /**
     * Set selected color
     *
     * @param color
     */
    fun setSelectedColor(color: Color) {
        _selectedColor.value = color
    }

    /**
     * Set cell size
     *
     * @param size
     */
    fun setCellSize(size: Float) {
        _cellSize.value = size
    }
}