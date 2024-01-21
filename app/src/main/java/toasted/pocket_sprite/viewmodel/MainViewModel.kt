package toasted.pocket_sprite.viewmodel

import android.graphics.Bitmap
import android.graphics.Point
import android.view.MotionEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import toasted.pocket_sprite.util.DEFAULT_BITMAP_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_BITMAP_WIDTH
import toasted.pocket_sprite.util.DEFAULT_CELL_SIZE
import kotlin.math.abs

/**
 * Main view model
 *
 * @constructor Create empty Main view model
 */
class MainViewModel: ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    private val _numberOfPointers = MutableLiveData(0)
    private val _fingerDrawEnabled = MutableLiveData(true)
    private val _gridEnabled = MutableLiveData(true)
    private val _gridWidth = MutableLiveData(DEFAULT_BITMAP_WIDTH.dp)
    private val _gridHeight = MutableLiveData(DEFAULT_BITMAP_HEIGHT.dp)
    private val _colorPickerEnabled = MutableLiveData(false)
    private val _scaleWidth = MutableLiveData(1f)
    private val _scaleHeight = MutableLiveData(1f)
    private val _brushSize = MutableLiveData(1)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _cellSize = MutableLiveData(DEFAULT_CELL_SIZE)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
//    val fileContent: LiveData<String> = _fileContent
    val numberOfPointers: LiveData<Int> = _numberOfPointers
    val fingerDrawEnabled: LiveData<Boolean> = _fingerDrawEnabled
    val gridEnabled: LiveData<Boolean> = _gridEnabled
    val gridWidth: LiveData<Dp> = _gridWidth
    val gridHeight: LiveData<Dp> = _gridHeight
    val brushSize: LiveData<Int> = _brushSize
    val gridColor: LiveData<Color> = _gridColor
    val selectedColor: LiveData<Color> = _selectedColor
    val cellSize: LiveData<Float> = _cellSize
    val colorList: LiveData<List<Color>> = _colorList
    val colorPickerEnabled: LiveData<Boolean> = _colorPickerEnabled
    val scaleWidth: LiveData<Float> = _scaleWidth
    val scaleHeight: LiveData<Float> = _scaleHeight

    private val _bitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    private val _backgroundBitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    val bitmap: LiveData<Bitmap> = _bitmap
    val backgroundBitmap: LiveData<Bitmap> = _backgroundBitmap

    /**
     * Interpolate points
     *
     * @param dx1
     * @param dy1
     * @param dx2
     * @param dy2
     * @return
     */
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
     * Get current bitmap
     *
     * @return
     */
    fun getCurrentBitmap(): Bitmap? {
        return _bitmap.value
    }

    /**
     * Update canvas size
     *
     * @param width
     * @param height
     */
    fun updateCanvasSize(width: Float, height: Float) {
        updateBackgroundBitmapSize(width, height)
        val currentBitmap = saveCurrentPixels()
        _bitmap.value?.apply {
            val bmp = Bitmap.createBitmap(width.toInt() * cellSize.value!!.toInt() ,
                height.toInt() * cellSize.value!!.toInt(), Bitmap.Config.ARGB_8888)
            // Initialize the bitmap with a default color or pattern
            _bitmap.value = bmp
        }

        loadPixels(currentBitmap)
    }

    /**
     * Update background bitmap size
     *
     * @param width
     * @param height
     */
    private fun updateBackgroundBitmapSize(width: Float, height: Float) {
        val currentBitmap = saveCurrentPixels()
        _backgroundBitmap.value?.apply {
            val bmp = Bitmap.createBitmap(width.toInt() * cellSize.value!!.toInt() ,
                height.toInt() * cellSize.value!!.toInt(), Bitmap.Config.ARGB_8888)
            // Initialize the bitmap with a default color or pattern
            createGridCheckerPattern(bmp)
            _backgroundBitmap.value = bmp
        }
        loadPixels(currentBitmap)
    }

    /**
     * Save current bitmap pixels
     *
     * @return
     */
    private fun saveCurrentPixels(): String {
        val bitmap = _bitmap.value ?: return ""
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
        val bitmap = _bitmap.value ?: return
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

    /**
     * Set number of pointers
     *
     * @param value
     */
    fun setNumberOfPointers(value: Int) {
        _numberOfPointers.value = value
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

    /**
     * Set file content
     *
     * @param content
     */
    fun setFileContent(content: String) {
        _fileContent.value = content
    }

    /**
     * Get pointer type
     *
     * @param event
     * @return
     */
    fun getPointerType(event: MotionEvent): String {
        val pointerId = event.getPointerId(event.actionIndex)
        return when (event.getToolType(pointerId)) {
            MotionEvent.TOOL_TYPE_FINGER -> "Finger"
            MotionEvent.TOOL_TYPE_STYLUS -> "Stylus"
            else -> "Unknown"
        }
    }

    /**
     * Toggle grid enabled
     *
     */
    fun toggleGridEnabled() {
        _gridEnabled.value = !_gridEnabled.value!!
    }

    /**
     * Toggle color picker
     *
     */
    fun toggleColorPicker() {
        _colorPickerEnabled.value = !_colorPickerEnabled.value!!
    }

    /**
     * Choose color
     *
     * @param color
     * @param colorPickerType
     */
    fun chooseColor(color: Color, colorPickerType: String) {
        if (colorPickerType == "Grid") {
            _gridColor.value = color
            toggleColorPicker()
        } else {
            _selectedColor.value = color
        }
    }

    /**
     * Toggle finger draw enabled
     *
     */
    fun toggleFingerDrawEnabled() {
        _fingerDrawEnabled.value = !_fingerDrawEnabled.value!!
    }

    /**
     * Display settings menu
     *
     */
    fun displaySettingsMenu() {

    }

}