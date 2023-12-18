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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.data.getProjectDirectory
import toasted.pocket_sprite.ui.components.GridItem
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme
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
                    //StartScreen(projectDir, viewModel = StartViewModel())
                    PixelArtApp(viewModel = MainViewModel())
                }
            }
        }

    }
}


@Composable
fun PixelArtApp(viewModel: MainViewModel = MainViewModel()){
    val gridWidth by viewModel.gridWidth.observeAsState(initial = 256f)
    val gridHeight by viewModel.gridHeight.observeAsState(initial = 256f)
    val pixelSize by viewModel.pixelSize.observeAsState(initial = 1.dp)
    val gridColor by viewModel.gridColor.observeAsState(initial = Color.DarkGray)
    val selectedColor by viewModel.selectedColor.observeAsState(initial = Color.Black)
    val cellSize by viewModel.cellSize.observeAsState(initial = 16f)


    val gridState = remember { mutableStateListOf<GridItem>().apply {
        for (i in 0 until gridHeight.toInt()) {
            for (j in 0 until gridWidth.toInt()) {
                add(GridItem(color = gridColor, x = j , y = i))
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
    val pixelValue = with(LocalDensity.current) { pixelSize.toPx() }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var numberOfPointers by remember { mutableIntStateOf(0) }

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)


    ) {
        Canvas(
            modifier = Modifier
                .size((cellSize * gridWidth).dp, (cellSize * gridHeight).dp)
                .background(Color.White)
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
                .pointerInteropFilter { event ->
                    Log.d("TouchTest", "Action: ${event.action}")
                    Log.d("TouchTest", "Event type: $event")
                    Log.d("TouchTest", "Pointer count: ${event.pointerCount}")

                    val x = (event.x) / scale
                    val y = (event.y) / scale
                    val cellX = (x /pixelValue).toInt()
                    Log.d("TouchTest", "$pixelValue")
                    val cellY = (y /pixelValue).toInt()
                    val index = cellY * gridHeight.toInt() + cellX
                    Log.d("TouchTest", "Index: $index")

                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (event.pointerCount == 1 && index in gridState.indices) {
                                gridState[index] = gridState[index].copy(color = Color.Black) // Update the cell color
                                Log.d("TouchTest", "Action down at: ${event.x}, ${event.y}")
                            }
                            numberOfPointers = event.pointerCount
                            true
                        }

                        MotionEvent.ACTION_POINTER_DOWN -> {
                            Log.d("TouchTest", "Pointer down at: ${event.x}, ${event.y}")
                            numberOfPointers = event.pointerCount
                            true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (event.pointerCount == 1 && index in gridState.indices) {
                                gridState[index] =
                                    gridState[index].copy(color = Color.Black) // Update the cell color
                                for (i in 0 until event.historySize) {
                                    val histX = (event.getHistoricalX(i)) / scale
                                    val histY = (event.getHistoricalY(i)) / scale
                                    val histCellX = (histX / pixelValue).toInt()
                                    val histCellY = (histY / pixelValue).toInt()
                                    val histIndex = histCellY * gridHeight.toInt() + histCellX

                                    if (histIndex in gridState.indices) {
                                        gridState[histIndex] =
                                            gridState[histIndex].copy(color = selectedColor)
                                    }
                                }
                            }
                            numberOfPointers = event.pointerCount
                            Log.d("TouchTest", "Action move at: ${event.x}, ${event.y}")
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            Log.d("TouchTest", "Action up at: ${event.x}, ${event.y}")
                            numberOfPointers = event.pointerCount
                            true
                        }

                        MotionEvent.ACTION_POINTER_UP -> {
                            Log.d("TouchTest", "Pointer up at: ${event.x}, ${event.y}")
                            numberOfPointers = event.pointerCount
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
                drawRect(
                    color = color,
                    topLeft = Offset(x *pixelValue, y * pixelValue),
                    size = Size(pixelSize.toPx(), pixelSize.toPx())
                )
            }
        }
    }
    Text(text = "gridSize: ${gridWidth} x ${gridHeight} \n cellSize: $cellSize \n scale: ${(scale*100).toInt()}%", color = Color.Black, modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ShowPixelArtCanvas() {
    Pocket_SpriteTheme {
        PixelArtApp()
    }
}







