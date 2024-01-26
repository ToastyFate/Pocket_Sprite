package toasted.pocket_sprite.viewmodel

import android.view.MotionEvent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import toasted.pocket_sprite.util.BitmapManager
import toasted.pocket_sprite.util.ToolManager
import toasted.pocket_sprite.util.TouchInputHandler

/**
 * Main view model
 *
 * @constructor Create empty Main view model
 */
class MainViewModel: ViewModel() {

    private val bitmapManager = BitmapManager()
    private val touchInputHandler = TouchInputHandler()
    private val toolManager = ToolManager()
    private val _fileContent = MutableLiveData<String>()
    private val _numberOfPointers = MutableLiveData(0)
    private val _fingerDrawEnabled = MutableLiveData(true)
    private val _gridEnabled = MutableLiveData(true)
    private val _colorPickerEnabled = MutableLiveData(false)
    private val _brushSize = MutableLiveData(1)
    private val _gridColor = MutableLiveData(Color.DarkGray)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue,
        Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
//    val fileContent: LiveData<String> = _fileContent
    val numberOfPointers: LiveData<Int> = _numberOfPointers
    val fingerDrawEnabled: LiveData<Boolean> = _fingerDrawEnabled
    val gridEnabled: LiveData<Boolean> = _gridEnabled
    val brushSize: LiveData<Int> = _brushSize
    val gridColor: LiveData<Color> = _gridColor
    val colorList: LiveData<List<Color>> = _colorList
    val colorPickerEnabled: LiveData<Boolean> = _colorPickerEnabled
    val bmpManager = bitmapManager
    val touchHandler = touchInputHandler
    val toolMgr = toolManager

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
        val selectedColor = bitmapManager.selectedColor
        if (colorPickerType == "Grid") {
            _gridColor.value = color
            toggleColorPicker()
        } else {
            bitmapManager.setSelectedColor(color)
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