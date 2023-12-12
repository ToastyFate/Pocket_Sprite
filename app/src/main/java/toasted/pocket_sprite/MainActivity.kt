@file:OptIn(ExperimentalComposeUiApi::class)

package toasted.pocket_sprite

import android.os.Bundle
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
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme
import kotlin.math.sqrt

var gridWidth = 256f
var pixelSize = 16.dp
val gridColor = Color.DarkGray
val selectedColor = Color.Black


class MainActivity : ComponentActivity() {

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
    val gridSize = sqrt(gridWidth.toDouble()).toInt()
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
fun infoText(gridSize: Int, pixelSize: Dp, scale: Float) {
    Box(contentAlignment = Alignment.TopStart, modifier = Modifier
        .background(Color.White)
    ){
        Text(text = "gridSize: $gridSize \n pixelSize: $pixelSize \n scale: $scale", color = Color.Black, modifier = Modifier
            .align(Alignment.TopStart)
            .background(Color.White))
    }
}


@Composable
fun PixelArtCanvas(gridState: MutableList<GridItem>, gridSize: Int, pixelSize: Dp) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current
    infoText(gridSize = gridSize, pixelSize = pixelSize, scale = scale)
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)
    ) {
        val inverseScale = 1f / scale
        val adjustedOffset = -offset / scale
        Canvas(modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, Zoom, _ ->
                    scale *= Zoom
                    offset += pan
                }
            }
            .size(256.dp)
            .background(Color.LightGray)
            .pointerInteropFilter { event ->
                val pixelValue = with(density) { pixelSize.toPx() }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val adjustedx = (event.x * inverseScale) + adjustedOffset.x
                        val adjustedy = (event.y * inverseScale) + adjustedOffset.y

                        val x = adjustedx.toInt() / pixelValue.toInt()
                        val y = adjustedy.toInt() / pixelValue.toInt()

                        val index = gridState.indexOfFirst { it.getXY().first == x && it.getXY().second == y }
                        if (index in gridState.indices) {
                            gridState[index] = gridState[index].copy(color = selectedColor)
                        }
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val adjustedx = (event.x * inverseScale) + adjustedOffset.x
                        val adjustedy = (event.y * inverseScale) + adjustedOffset.y

                        val x = adjustedx.toInt() / pixelValue.toInt()
                        val y = adjustedy.toInt() / pixelValue.toInt()

                        val index = gridState.indexOfFirst { it.getXY().first == x && it.getXY().second == y }
                        if (index in gridState.indices) {
                            gridState[index] = gridState[index].copy(color = selectedColor)
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
}

@Preview(showBackground = true)
@Composable
fun ShowPixelArtCanvas() {
    Pocket_SpriteTheme {
        PixelArtApp()
    }
}







