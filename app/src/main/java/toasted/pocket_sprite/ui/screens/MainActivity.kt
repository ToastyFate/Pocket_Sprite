@file:OptIn(ExperimentalComposeUiApi::class)

package toasted.pocket_sprite.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import toasted.pocket_sprite.ui.components.GridItem
import toasted.pocket_sprite.data.getProjectDirectory
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme
import toasted.pocket_sprite.viewmodel.StartViewModel
import androidx.compose.runtime.livedata.observeAsState
import toasted.pocket_sprite.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val projectDir = getProjectDirectory(this, "Projects")
        setContent {
            Pocket_SpriteTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    StartScreen(projectDir, viewModel = StartViewModel())
                }
            }
        }

    }
}


@Composable
fun PixelArtApp(viewModel: MainViewModel = MainViewModel()){
    val gridWidth by viewModel.gridWidth.observeAsState(initial = 256f)
    val gridHeight by viewModel.gridHeight.observeAsState(initial = 256f)
    val pixelSize by viewModel.pixelSize.observeAsState(initial = 16.dp)
    val gridColor by viewModel.gridColor.observeAsState(initial = Color.DarkGray)
    val selectedColor by viewModel.selectedColor.observeAsState(initial = Color.Black)
    val cellSize by viewModel.cellSize.observeAsState(initial = 16f)

    val cellsX = (gridWidth / pixelSize.value)
    val cellsY = (gridHeight / pixelSize.value)
    val gridState = remember { mutableStateListOf<GridItem>().apply {
        for (i in 0 until cellsY.toInt()) {
            for (j in 0 until cellsX.toInt()) {
                add(GridItem(color = gridColor, x = i , y = j))
            }
        }
    }
    }
    PixelArtCanvas(gridState = gridState, cellSize = cellSize.toInt(), pixelSize = pixelSize,
        gridWidth = gridWidth, gridHeight = gridHeight, selectedColor = selectedColor)
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PixelArtCanvas(gridState: MutableList<GridItem>, cellSize: Int, pixelSize: Dp,
                   gridWidth: Float, gridHeight: Float, selectedColor: Color) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var numberOfPointers by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale *= zoom

                if (numberOfPointers > 1) {
                    offset += pan
                }
            }
        }

    ) {
        Canvas(
            modifier = Modifier
                .size(gridWidth.dp, gridHeight.dp)
                .background(Color.White)
                .pointerInteropFilter { event ->
                    val pixelValue = with(density) { pixelSize.toPx() }
                    val adjustedX = (event.x) / scale
                    val adjustedY = (event.y) / scale

                    val cellX = (adjustedX / pixelValue).toInt().coerceIn(0, (cellSize - 1))
                    val cellY = (adjustedY / pixelValue).toInt().coerceIn(0, (cellSize - 1))
                    val index = cellY * cellSize + cellX

                    when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            if (index in gridState.indices) {
                                gridState[index] = gridState[index].copy(color = Color.Black) // Update the cell color
                            }
                            true
                        }
                        else -> false
                    }
                }
        ) {
            gridState.forEach {
                val x = it.getXY().first
                val y = it.getXY().second
                val color = it.getColor()
                val pixelValue = with(density) { pixelSize.toPx() }
                drawRect(
                    color = color,
                    topLeft = Offset(x * pixelValue, y * pixelValue),
                    size = Size(pixelValue, pixelValue)
                )
            }
        }
    }
    Text(text = "gridSize: ${gridWidth.toInt()} x ${gridHeight.toInt()} \n cellSize: $cellSize \n scale: ${(scale*100).toInt()}%", color = Color.Black, modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ShowPixelArtCanvas() {
    Pocket_SpriteTheme {
        PixelArtApp()
    }
}







