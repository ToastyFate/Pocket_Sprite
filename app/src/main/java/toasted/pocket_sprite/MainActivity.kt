@file:OptIn(ExperimentalComposeUiApi::class)

package toasted.pocket_sprite

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme

val gridWidth = 256f
val gridHeight = 256f
val pixelSize = 16.dp
val gridColor = Color.DarkGray
val selectedColor = Color.Black


class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pocket_SpriteTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ShowPixelArtCanvas()
                }
            }
        }

    }
}



@Composable
fun PixelArtApp(){
    val gridSize = Math.sqrt(gridWidth.toDouble()).toInt()
    val gridState = remember { mutableStateListOf<GridItem>().apply {
        for(y in 0 until gridSize) {
            for(x in 0 until  gridSize) {
                add(GridItem(color = gridColor, x = x, y = y))
            }

        }
        }}
    PixelArtCanvas(gridState = gridState, gridSize = gridSize, pixelSize = pixelSize)
}

@Composable
fun PixelArtCanvas(gridState: MutableList<GridItem>, gridSize: Int, pixelSize: Dp) {
    val density = LocalDensity.current
    Canvas(modifier = Modifier
        .size(256.dp)
        .background(Color.LightGray)
        .pointerInteropFilter { event ->
            val pixelValue = with(density) { pixelSize.toPx() }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = (event.x / pixelValue).toInt()
                    val y = (event.y / pixelValue).toInt()
                    val index = y * gridSize + x
                    if (index in gridState.indices) {
                        gridState[index] = GridItem(color = selectedColor, x = x, y = y)
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = (event.x / pixelValue).toInt()
                    val y = (event.y / pixelValue).toInt()
                    val index = y * gridSize + x
                    if (index in gridState.indices) {
                        gridState[index] = GridItem(color = selectedColor, x = x, y = y)
                    }
                    true
                }

                else -> false
            }
        }

    ){
        gridState.forEach() {
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

@Preview(showBackground = true)
@Composable
fun ShowPixelArtCanvas() {
    Pocket_SpriteTheme {
        PixelArtApp()
    }
}







