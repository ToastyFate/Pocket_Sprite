package toasted.pocket_sprite.tools

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density
import toasted.pocket_sprite.viewmodel.MainViewModel

class SelectTool: ICanvasTool {
    override fun onDraw() {
        TODO("Not yet implemented")
    }

    override fun executeTouch(
        viewModel: MainViewModel,
        change: PointerInputChange,
        pointerInput: PointerInputScope,
        density: Density
    ) {
        TODO()
    }

}