package toasted.pocket_sprite.util

import android.graphics.Bitmap
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
    private val _scale = MutableLiveData(1f)
    private val _scaleWidth = MutableLiveData(DEFAULT_BITMAP_WIDTH)
    private val _scaleHeight = MutableLiveData(DEFAULT_BITMAP_HEIGHT)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _bitmap = MutableLiveData(
        Bitmap.createBitmap(
            DEFAULT_BITMAP_WIDTH,
            DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888
        )
    )
    private val _backgroundBitmap = MutableLiveData(
        Bitmap.createBitmap(
            DEFAULT_BITMAP_WIDTH,
            DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888
        )
    )
    val gridWidth: LiveData<Dp> = _gridWidth
    val gridHeight: LiveData<Dp> = _gridHeight
    val scale: LiveData<Float> = _scale
    val scaleWidth: LiveData<Int> = _scaleWidth
    val scaleHeight: LiveData<Int> = _scaleHeight
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
        val scale = scale.value ?: return
        val width = with(density) { gridWidth.toPx() }
        val height = with(density) { gridHeight.toPx() }
        val bmp = Bitmap.createBitmap(
            width.toInt() * scale.toInt(),
            height.toInt() * scale.toInt(), Bitmap.Config.ARGB_8888
        )
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
        val scale = scale.value ?: return
        val width = with(density) { gridWidth.toPx() }
        val height = with(density) { gridHeight.toPx() }
        val bmp = Bitmap.createBitmap(
            width.toInt() * scale.toInt(),
            height.toInt() * scale.toInt(), Bitmap.Config.ARGB_8888
        )
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

    fun scaleBitmap(bitmap: Bitmap) {
        val scale = scale.value ?: return
        bitmap.reconfigure(
            (bitmap.width * scale).toInt(),
            (bitmap.height * scale).toInt(),
            Bitmap.Config.ARGB_8888
        )
    }

    fun setPixel(x: Int, y: Int, color: Color) {
        _bitmap.value?.let { bitmap ->
            val scale = scale.value ?: return
            val startX = (x * scale).toInt()
            val startY = (y * scale).toInt()
            for (i in startX until startX + scale.toInt()) {
                for (j in startY until startY + scale.toInt()) {
                    if (i < bitmap.width && j < bitmap.height) {
                        bitmap.setPixel(i, j, color.toArgb())
                    }
                }
            }
            _bitmap.value = bitmap
        }
    }

//    /**
//     * Update cell
//     *
//     * @param x
//     * @param y
//     * @param color
//     */
//    fun updateCell(x: Int, y: Int, color: Color) {
//        _bitmap.value?.let { bitmap ->
//            val scale = scale.value ?: return
//            val startX = x * scale
//            val startY = y * scale
//            for (i in startX until startX + cellSize) {
//                for (j in startY until startY + cellSize) {
//                    if (i < bitmap.width && j < bitmap.height) {
//                        bitmap.setPixel(i, j, color.toArgb())
//                    }
//                }
//            }
//
//            // Trigger an update to notify observers, if necessary
//            _bitmap.value = bitmap
//        }
//    }


//    /**
//     * Update cells
//     *
//     * @param event
//     * @param scale
//     */
//    fun updateCells(event: MotionEvent, scale: Float) {
//        val cellSize = cellSize.value ?: return
//        val selectedColor = selectedColor.value ?: return
//        val x =
//            (((event.x / cellSize) / scale).toInt()) // Convert to bitmap coordinates
//        val y =
//            (((event.y / cellSize) / scale).toInt())// Convert to bitmap coordinates
//        updateCell(
//            x,
//            y,
//            selectedColor
//        )
//    }

    /**
     * Update background bitmap size
     *
     * @param width
     * @param height
     */
    private fun updateBackgroundBitmapSize(width: Float, height: Float) {
        val currentBitmap = saveCurrentPixels()
        val scale = scale.value ?: return
        _backgroundBitmap.value?.apply {
            val bmp = Bitmap.createBitmap(
                width.toInt() * scale.toInt(),
                height.toInt() * scale.toInt(), Bitmap.Config.ARGB_8888
            )
            // Initialize the bitmap with a default color or pattern
            createGridCheckerPattern(bmp)
            _backgroundBitmap.value = bmp
        }
        loadPixels(currentBitmap)
    }

    private fun reconfigureBitmaps() {
        val scaleWidth = scaleWidth.value ?: return
        val scaleHeight = scaleHeight.value ?: return
        val config = Bitmap.Config.ARGB_8888
        _bitmap.value?.reconfigure(scaleWidth, scaleHeight, config)
        _backgroundBitmap.value?.reconfigure(scaleWidth, scaleHeight, config)
    }

    /**
     * Update canvas size
     *
     * @param width
     * @param height
     */
    fun updateCanvasSize(width: Float, height: Float) {
        val scale = scale.value ?: return
        updateBackgroundBitmapSize(width, height)
        val currentBitmap = saveCurrentPixels()
        _bitmap.value?.apply {
            val bmp = Bitmap.createBitmap(
                width.toInt() * scale.toInt(),
                height.toInt() * scale.toInt(), Bitmap.Config.ARGB_8888
            )
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
        val scale = scale.value ?: return ""
        val stringBuilder = StringBuilder()
        for (i in 0 until bitmap.width / scale.toInt()) {
            for (j in 0 until bitmap.height / scale.toInt()) {
                val color = bitmap.getPixel(i * scale.toInt(), j * scale.toInt())
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
                setPixel(i, j, color)
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
        val scale = scale.value ?: return
        for (i in 0 until bitmap.width / (scale.toInt() * 18)) {
            for (j in 0 until bitmap.height / (scale.toInt() * 18)) {
                for (k in 0 until scale.toInt() * 8) {
                    for (l in 0 until scale.toInt() * 8) {
                        bitmap.setPixel(
                            i * scale.toInt() * 16 + k,
                            j * scale.toInt() * 16 + l,
                            Color.LightGray.toArgb()
                        )
                    }
                }
                for (k in scale.toInt() * 8 until scale.toInt() * 16) {
                    for (l in scale.toInt() * 8 until scale.toInt() * 16) {
                        bitmap.setPixel(
                            i * scale.toInt() * 16 + k,
                            j * scale.toInt() * 16 + l,
                            Color.LightGray.toArgb()
                        )
                    }
                }
            }
        }
        _backgroundBitmap.value = bitmap
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
     * Update scale
     *
     * @param zoom
     */
    fun updateScale(zoom: Float) {
        val bitmap = bitmap.value ?: return
        val scale = scale.value ?: return
        val newScale = scale * zoom
        _scale.value = newScale
        _scaleWidth.value = (newScale * bitmap.width).toInt()
        _scaleHeight.value = (newScale * bitmap.height).toInt()
        reconfigureBitmaps()
    }
}