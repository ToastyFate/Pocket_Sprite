package toasted.pocket_sprite.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.util.Coordinate
import toasted.pocket_sprite.viewmodel.MainViewModel

class BrushTool: ICanvasTool  {

    private val toolName = "Brush"
    private val toolIcon = Icons.Default.Create
    private val toolDescription = "Draw on the canvas"

    override fun onDraw() {
        TODO()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun executeTouch(viewModel: MainViewModel, change: PointerInputChange, pointerInput: PointerInputScope, density: Density) {
        val bitmapManager = viewModel.bmpManager
        val selectedColor = viewModel.selectedColor.value ?: return
        val canvasHeight = viewModel.canvasHeight.value ?: return
        val canvasWidth = viewModel.canvasWidth.value ?: return
        val gridCellSize = viewModel.gridMgr.gridCellSize.value ?: return
        val width = with(density) {canvasWidth.dp.toPx()}
        val height = with(density) {canvasHeight.dp.toPx()}
        val canvasTouchCoordinate = Coordinate(change.position.x.toInt(), change.position.y.toInt())
        val prevCanvasTouchCoordinate = Coordinate(change.previousPosition.x.toInt(),
            change.previousPosition.y.toInt())

        if (canvasTouchCoordinate.x in 0..<width.toInt() &&
            canvasTouchCoordinate.y in 0 ..<height.toInt()) {

            val interpolatedCoordinates = viewModel.gridMgr.interpolatePoints(prevCanvasTouchCoordinate, canvasTouchCoordinate)
            if (viewModel.touchHandler.pixelPerfectEnabled.value == true){
                val pixelPerfectCoordinates = mutableListOf<Coordinate>()
                for (coordinate in interpolatedCoordinates){
                    val coordinateCell = viewModel.touchHandler.getPointCell(gridCellSize, coordinate)
                    if (coordinateCell !in pixelPerfectCoordinates) {
                        viewModel.touchHandler.pixelPerfectTouch(coordinate, coordinateCell.x, coordinateCell.y, gridCellSize).let {
                            if (it) {
                                pixelPerfectCoordinates.add(coordinateCell)
                            }
                        }
                    }
                }

            }
            bitmapManager.updateCells(interpolatedCoordinates, viewModel.gridMgr.gridCellSize.value ?: 1, selectedColor)
        }
        change.consume()
        bitmapManager.getCurrentBitmap()?.let { bitmapManager.updateBitmap(it) }
    }
}