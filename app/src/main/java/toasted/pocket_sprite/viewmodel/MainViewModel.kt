package toasted.pocket_sprite.viewmodel

import android.util.Size
import android.view.MotionEvent
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import toasted.pocket_sprite.util.BitmapManager
import toasted.pocket_sprite.util.DEFAULT_CANVAS_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_CANVAS_WIDTH
import toasted.pocket_sprite.util.DEFAULT_SCALE_FACTOR
import toasted.pocket_sprite.util.GridManager
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
    private val gridManager = GridManager()
    private val _scaleFactor = MutableLiveData(DEFAULT_SCALE_FACTOR)
    private val _canvasSize = MutableLiveData(Size(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT))
    private val _canvasWidth = MutableLiveData(DEFAULT_CANVAS_WIDTH)
    private val _canvasHeight = MutableLiveData(DEFAULT_CANVAS_WIDTH)
    private val _numberOfPointers = MutableLiveData(0)
    private val _fingerDrawEnabled = MutableLiveData(true)
    private val _colorPickerEnabled = MutableLiveData(false)
    private val _selectedColor = MutableLiveData(Color.Black)
    private val _brushSize = MutableLiveData(1)
    private val _colorList = MutableLiveData(listOf(Color.Black, Color.White, Color.Red, Color.Blue,
        Color.Green, Color.Yellow, Color.Magenta, Color.Cyan))
    val scaleFactor: LiveData<Float> = _scaleFactor
    val canvasSize: LiveData<Size> = _canvasSize
    val canvasWidth: LiveData<Int> = _canvasWidth
    val canvasHeight: LiveData<Int> = _canvasHeight
    val numberOfPointers: LiveData<Int> = _numberOfPointers
    val fingerDrawEnabled: LiveData<Boolean> = _fingerDrawEnabled
    val brushSize: LiveData<Int> = _brushSize
    val colorList: LiveData<List<Color>> = _colorList
    val colorPickerEnabled: LiveData<Boolean> = _colorPickerEnabled
    val selectedColor: LiveData<Color> = _selectedColor
    val bmpManager = bitmapManager
    val touchHandler = touchInputHandler
    val toolMgr = toolManager
    val gridMgr = gridManager

    /**
     * Set number of pointers
     *
     * @param value
     */
    fun setNumberOfPointers(value: Int) {
        _numberOfPointers.value = value
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
            gridManager.setGridColor(color)
            toggleColorPicker()
        } else {
            setSelectedColor(color)
        }
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
     * Toggle finger draw enabled
     *
     */
    fun toggleFingerDrawEnabled() {
        _fingerDrawEnabled.value = !_fingerDrawEnabled.value!!
    }

    fun setScaleFactor(zoom: Float) {
        val value = _scaleFactor.value ?: return
        _scaleFactor.value = value * zoom
    }

    /**
     * Display settings menu
     *
     */
    fun displaySettingsMenu() {

    }

}