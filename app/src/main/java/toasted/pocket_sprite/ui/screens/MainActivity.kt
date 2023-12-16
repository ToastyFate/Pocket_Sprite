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
import toasted.pocket_sprite.viewmodel.MyViewModel

var gridWidth = 2000f
var gridHeight = 2000f
var pixelSize = 16.dp
val gridColor = Color.DarkGray
val selectedColor = Color.Black
val cellSize = pixelSize.value.toInt()


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
                    StartScreen(projectDir, viewModel = MyViewModel())
                }
            }
        }

    }
}


@Composable
fun PixelArtApp(){
    val cellsX = (gridWidth / pixelSize.value)
    val cellsY = (gridHeight / pixelSize.value)
    val gridSize = cellsX * cellsY
    val gridState = remember { mutableStateListOf<GridItem>().apply {
        for (i in 0 until gridSize.toInt()) {
            add(GridItem(color = gridColor, x = i % cellsX.toInt(), y = i / cellsX.toInt()))
        }
    }
    }
    PixelArtCanvas(gridState = gridState, cellSize = cellSize, pixelSize = pixelSize)
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PixelArtCanvas(gridState: MutableList<GridItem>, cellSize: Int, pixelSize: Dp) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var numberOfPointers by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)
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
        Canvas(modifier = Modifier
            .size(gridWidth.dp, gridHeight.dp)
            .background(Color.LightGray)
            .pointerInteropFilter { event ->
                val pixelValue = with(density) { pixelSize.toPx() }
                Log.d("PixelArtCanvas", "pixelValue: $pixelValue")
                when (event.action) {


                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        numberOfPointers = 1
                        val adjustedX = event.x / scale
                        val adjustedY = event.y / scale
                        val x = (adjustedX / pixelValue).toInt()
                        val y = (adjustedY / pixelValue).toInt()
                        Log.d("PixelArtCanvas", "number of pointers: $numberOfPointers")
                        val index = y * cellSize + x
                        Log.d("PixelArtCanvas", "index: $index")
                        if (index in gridState.indices) {
                            gridState[index] = gridState[index].copy(color = selectedColor)
                        }
                    }

                    MotionEvent.ACTION_POINTER_DOWN -> {
                        numberOfPointers++
                    }

                    MotionEvent.ACTION_UP -> {
                        numberOfPointers = 0
                        Log.d("PixelArtCanvas", "number of pointers: $numberOfPointers")
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        numberOfPointers--
                    }

                }
                true
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







