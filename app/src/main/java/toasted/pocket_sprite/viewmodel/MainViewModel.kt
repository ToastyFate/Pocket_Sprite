package toasted.pocket_sprite.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import toasted.pocket_sprite.ui.components.GridItem

class MainViewModel: ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    private val _showMenu = MutableLiveData(true)
    private val _gridWidth = MutableLiveData(64f)
    private val _gridHeight = MutableLiveData(64f)
    private val _pixelSize = MutableLiveData(1.dp)
    private val _brushSize = MutableLiveData(1.dp)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _cellSize = MutableLiveData(16f)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
    val fileContent: MutableLiveData<String> = _fileContent
    val showMenu: MutableLiveData<Boolean> = _showMenu
    val gridWidth: MutableLiveData<Float> = _gridWidth
    val gridHeight: MutableLiveData<Float> = _gridHeight
    val pixelSize: MutableLiveData<Dp> = _pixelSize
    private val gridColor: MutableLiveData<Color> = _gridColor
    val selectedColor: MutableLiveData<Color> = _selectedColor
    val cellSize: MutableLiveData<Float> = _cellSize
    val colorList: MutableLiveData<List<Color>> = _colorList
    private val _gridState = MutableLiveData<List<GridItem>>(initializeGrid())
    val gridState: MutableLiveData<List<GridItem>> = _gridState

    private fun initializeGrid(): List<GridItem> {
        val gridState = mutableListOf<GridItem>()
        for (y in 0 until gridHeight.value!!.toInt()/cellSize.value!!.toInt()) {
            for (x in 0 until gridWidth.value!!.toInt()/cellSize.value!!.toInt()) {
                gridState.add(GridItem(gridColor.value!!, x, y))
            }
        }
        return gridState
    }

    private fun updateGridState(gridState: List<GridItem>) {
        _gridState.value = gridState
    }

    fun getGridState(): List<GridItem> {
        return _gridState.value!!
    }

    fun setColorList(colorList: List<Color>) {
        _colorList.value = colorList
    }

    fun setBrushSize(size: Dp) {
        _brushSize.value = size
    }

    fun getGridItemIndex(x: Int, y: Int): Int {
        return (y * gridHeight.value!!.toInt()) + x
    }

    fun setGridItemColor(gridItemIndex: Int, color: Color) {
        val gridState = _gridState.value!!.toMutableList()
        gridState[gridItemIndex].setColor(color)
        updateGridState(gridState)
    }



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