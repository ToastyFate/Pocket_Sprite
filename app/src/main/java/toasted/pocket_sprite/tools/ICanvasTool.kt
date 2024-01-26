package toasted.pocket_sprite.tools

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import toasted.pocket_sprite.viewmodel.MainViewModel

interface ICanvasTool  {
    fun onDraw()
    fun executeTouch(viewModel: MainViewModel, change: PointerInputChange, pointerInput: PointerInputScope)

}