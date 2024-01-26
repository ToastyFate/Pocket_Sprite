package toasted.pocket_sprite.util

import androidx.lifecycle.MutableLiveData
import toasted.pocket_sprite.tools.BrushTool
import toasted.pocket_sprite.tools.ICanvasTool

class ToolManager {

    private val _currentTool = MutableLiveData<ICanvasTool>(BrushTool())
    val currentTool: MutableLiveData<ICanvasTool> = _currentTool
}