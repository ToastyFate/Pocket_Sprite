package toasted.pocket_sprite.viewmodel

import android.graphics.Bitmap
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

class MainViewModel: ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    private val _numberOfPointers = MutableLiveData(0)
    private val _fingerDrawEnabled = MutableLiveData(true)
    private val _gridEnabled = MutableLiveData(true)
    private val _gridWidth = MutableLiveData(DEFAULT_BITMAP_WIDTH.dp)
    private val _gridHeight = MutableLiveData(DEFAULT_BITMAP_HEIGHT.dp)
    private val _scaleWidth = MutableLiveData(1f)
    private val _scaleHeight = MutableLiveData(1f)
    private val _brushSize = MutableLiveData(1)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _cellSize = MutableLiveData(DEFAULT_CELL_SIZE)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
//    val fileContent: MutableLiveData<String> = _fileContent
    val numberOfPointers: MutableLiveData<Int> = _numberOfPointers
    val fingerDrawEnabled: MutableLiveData<Boolean> = _fingerDrawEnabled
    val gridEnabled: MutableLiveData<Boolean> = _gridEnabled
    val gridWidth: MutableLiveData<Dp> = _gridWidth
    val gridHeight: MutableLiveData<Dp> = _gridHeight
    val brushSize: MutableLiveData<Int> = _brushSize
    val gridColor: LiveData<Color> = _gridColor
    val selectedColor: MutableLiveData<Color> = _selectedColor
    val cellSize: MutableLiveData<Float> = _cellSize
    val colorList: MutableLiveData<List<Color>> = _colorList
    val scaleWidth: MutableLiveData<Float> = _scaleWidth
    val scaleHeight: MutableLiveData<Float> = _scaleHeight

    private val _bitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    private val _backgroundBitmap = MutableLiveData(Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888))
    val bitmap: MutableLiveData<Bitmap> = _bitmap
    val backgroundBitmap: MutableLiveData<Bitmap> = _backgroundBitmap

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

    fun updateBitmap(bitmap: Bitmap) {
       val newBitmap = Bitmap.createBitmap(bitmap)
       _bitmap.value = newBitmap
    }

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

//    fun updateCanvasSize(width: Float, height: Float) {
//        _bitmap.value?.apply {
//            val bmp = Bitmap.createBitmap(width.toInt() * cellSize.value!!.toInt() ,
//                height.toInt() * cellSize.value!!.toInt(), Bitmap.Config.ARGB_8888)
//            // Initialize the bitmap with a default color or pattern
//            _bitmap.value = bmp
//        }
//    }
//
//    fun updateBrushSize(size: Float) {
//        _brushSize.value = size.toInt()
//    }

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

    fun setNumberOfPointers(value: Int) {
        _numberOfPointers.value = value
    }

    fun setGridColor(color: Color) {
        _gridColor.value = color
    }

    fun setSelectedColor(color: Color) {
        _selectedColor.value = color
    }

    fun setCellSize(size: Float) {
        _cellSize.value = size
    }

    fun setFileContent(content: String) {
        _fileContent.value = content
    }

    fun getPointerType(event: MotionEvent): String {
        val pointerId = event.getPointerId(event.actionIndex)
        return when (event.getToolType(pointerId)) {
            MotionEvent.TOOL_TYPE_FINGER -> "Finger"
            MotionEvent.TOOL_TYPE_STYLUS -> "Stylus"
            else -> "Unknown"
        }
    }

}