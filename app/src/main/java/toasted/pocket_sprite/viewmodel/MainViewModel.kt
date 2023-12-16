package toasted.pocket_sprite.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    private val _showMenu = MutableLiveData(true)
    private val _gridWidth = MutableLiveData(256f)
    private val _gridHeight = MutableLiveData(256f)
    private val _pixelSize = MutableLiveData(16.dp)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _cellSize = MutableLiveData(16f)
    val fileContent: MutableLiveData<String> = _fileContent
    val showMenu: MutableLiveData<Boolean> = _showMenu
    val gridWidth: MutableLiveData<Float> = _gridWidth
    val gridHeight: MutableLiveData<Float> = _gridHeight
    val pixelSize: MutableLiveData<Dp> = _pixelSize
    val gridColor: MutableLiveData<Color> = _gridColor
    val selectedColor: MutableLiveData<Color> = _selectedColor
    val cellSize: MutableLiveData<Float> = _cellSize


    fun setShowMenu(show: Boolean) {
        _showMenu.value = show
    }

    fun setGridWidth(width: Float) {
        _gridWidth.value = width
    }

    fun setGridHeight(height: Float) {
        _gridHeight.value = height
    }

    fun setPixelSize(size: Dp) {
        _pixelSize.value = size
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