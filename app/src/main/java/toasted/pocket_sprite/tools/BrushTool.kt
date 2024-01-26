package toasted.pocket_sprite.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import toasted.pocket_sprite.util.interpolatePoints
import toasted.pocket_sprite.viewmodel.MainViewModel

class BrushTool: ICanvasTool  {

    private val toolName = "Brush"
    private val toolIcon = Icons.Default.Create
    private val toolDescription = "Draw on the canvas"

    override fun onDraw() {
        TODO()
    }

    override fun executeTouch(viewModel: MainViewModel, change: PointerInputChange, pointerInput: PointerInputScope) {
        val bitmapManager = viewModel.bmpManager
        val cellSize = bitmapManager.cellSize.value ?: return
        val scale = bitmapManager.getCurrentScale()
        val selectedColor = bitmapManager.selectedColor.value ?: return
        val bitmap = bitmapManager.bitmap.value ?: return

        val event = change.position
        val x =
            ((event.x / cellSize) / scale).toInt() // Convert to bitmap coordinates
        val y =
            ((event.y / cellSize) / scale).toInt() // Convert to bitmap coordinates
        if (x >= 0 && x < bitmap.width && y >= 0 && y < bitmap.height) {
            val prevX = ((change.previousPosition.x / cellSize) / scale).toInt()
            val prevY = ((change.previousPosition.y / cellSize) / scale).toInt()

            val interpolatedPoints = interpolatePoints(prevX, prevY, x, y)

            for (point in interpolatedPoints) {
                if (point.x >= 0 && point.x < bitmap.width && point.y >= 0 && point.y < bitmap.height) {
                    val pointInCell = viewModel.touchHandler.getPointCell(viewModel, point)
                    if (viewModel.touchHandler.pixelPerfectTouch(
                            event,
                            pointInCell.x,
                            pointInCell.y
                        )
                    )
                        continue
                    bitmapManager.updateCell(point.x, point.y, selectedColor)
                }
            }

        }

        change.consume()
        bitmapManager.getCurrentBitmap()?.let { bitmapManager.updateBitmap(it) }

    }
}