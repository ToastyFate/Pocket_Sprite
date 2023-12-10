package toasted.pocket_sprite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.gridlayout.widget.GridLayout

val gridWidth = 256
val gridHeight = 256
val pixelSize = 16

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPixelArtScreen()
        }

    }

@Preview(showBackground = true)
@Composable
fun MyPixelArtScreen() {
    val grid = remember {
        mutableStateListOf<MutableList<GridItem>>()
    }

    for (y in 0 until gridHeight) {
        val row = remember {
            mutableStateListOf<GridItem>()
        }
        for (x in 0 until gridWidth) {
            row.add(GridItem("None", Color.Gray, pixelSize, pixelSize))
        }
        grid.add(row)
    }

    for (y in 0 until gridHeight) {
        for (x in 0 until gridWidth){
            drawRect(
                color = grid[y][x].getColor(),
                topLeft = Offset(x/100f, y/100f),
                size = Size(pixelSize.toFloat(), pixelSize.toFloat())
            )
        }
    }


}

@Composable
private fun drawRect(color: Color, topLeft: Offset, size: Size) {
    Canvas(modifier = Modifier) {
        drawRect(color, topLeft, size)
    }
}

    @Composable
fun PixelArtCanvas(grid: GridLayout, gridWidth: Int, gridHeight: Int, pixelSize: Dp) {

    Canvas(
        modifier = Modifier
            .size(pixelSize * gridWidth, pixelSize * gridHeight)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val x = (offset.x / pixelSize.toPx()).toInt()
                    val y = (offset.y / pixelSize.toPx()).toInt()
                }
            }

    ) {
        // Drawing logic...

    }
}
}



