package toasted.pocket_sprite.viewmodel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    private val _numberOfPointers = MutableLiveData(0)
    private val _gridWidth = MutableLiveData(64.dp)
    private val _gridHeight = MutableLiveData(64.dp)
    private val _brushSize = MutableLiveData(1)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _cellSize = MutableLiveData(16f)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
//    val fileContent: MutableLiveData<String> = _fileContent
    val numberOfPointers: MutableLiveData<Int> = _numberOfPointers
    val gridWidth: MutableLiveData<Dp> = _gridWidth
    val gridHeight: MutableLiveData<Dp> = _gridHeight
    val brushSize: MutableLiveData<Int> = _brushSize
    val gridColor: LiveData<Color> = _gridColor
    val selectedColor: MutableLiveData<Color> = _selectedColor
    val cellSize: MutableLiveData<Float> = _cellSize
    val colorList: MutableLiveData<List<Color>> = _colorList


    private val _bitmap = MutableLiveData(Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888))
    val bitmap: MutableLiveData<Bitmap> = _bitmap

    fun createBitmap(density: Density) {
        val width = with(density) { gridWidth.value!!.toPx() }
        val height = with(density) { gridHeight.value!!.toPx() }
        val bmp = Bitmap.createBitmap(width.toInt() * cellSize.value!!.toInt() ,
            height.toInt() * cellSize.value!!.toInt(), Bitmap.Config.ARGB_8888)
        createGridCheckerPattern(bmp)
        // Initialize the bitmap with a default color or pattern
        _bitmap.value = bmp
    }

   fun updateBitmap(bitmap: Bitmap) {
       val newBitmap = Bitmap.createBitmap(bitmap)
       _bitmap.value = newBitmap
    }

    fun updateCell(x: Int, y: Int, color: Int) {
        _bitmap.value?.let { bitmap ->
            val cellSize = _cellSize.value?.toInt() ?: return
            val startX = x * cellSize
            val startY = y * cellSize
            for (i in startX until startX + cellSize) {
                for (j in startY until startY + cellSize) {
                    if (i < bitmap.width && j < bitmap.height) {
                        bitmap.setPixel(i, j, color)
                    }
                }
            }

            // Trigger an update to notify observers, if necessary
            _bitmap.value = bitmap
        }
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
        for (i in 0 until bitmap.width / (_cellSize.value!!.toInt() * 18)) {
            for (j in 0 until bitmap.height / (_cellSize.value!!.toInt() * 18)) {
                for (k in 0 until _cellSize.value!!.toInt() * 8) {
                    for (l in 0 until _cellSize.value!!.toInt() * 8) {
                        bitmap.setPixel(i * _cellSize.value!!.toInt() * 16 + k, j * _cellSize.value!!.toInt() * 16 + l, Color.LightGray.toArgb())
                    }
                }
                for (k in _cellSize.value!!.toInt() * 8 until _cellSize.value!!.toInt() * 16) {
                    for (l in _cellSize.value!!.toInt() * 8 until _cellSize.value!!.toInt() * 16) {
                        bitmap.setPixel(i * _cellSize.value!!.toInt() * 16 + k, j * _cellSize.value!!.toInt() * 16 + l, Color.LightGray.toArgb())
                    }
                }
            }
        }
    }

    fun addPointer() {
        _numberOfPointers.value = _numberOfPointers.value?.plus(1)
    }

    fun removePointer() {
        _numberOfPointers.value = _numberOfPointers.value?.minus(1)
    }

    fun setNumberOfPointers(numberOfPointers: Int) {
        _numberOfPointers.value = numberOfPointers
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

}