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

data class GridItem(var color: Color, val x: Int, val y: Int)
data class GridRow(var row: List<GridItem>) {
    operator fun get(index: Int): GridItem {
        return row[index]
    }
}
data class Grid(var grid: List<GridRow>) {
    operator fun get(index: Int): GridRow {
        return grid[index]
    }
}

@Composable
fun PixelArtApp(){
    var gridState = Grid(grid = listOf())
    for(y in 0 until gridHeight.toInt()) {
        var newRow = GridRow(row = listOf())
        for(x in 0 until gridWidth.toInt()) {
            newRow.row += GridItem(color = Color.White, x = x, y = y)
        }
        gridState.grid += newRow
    }

    PixelArtCanvas(gridState = gridState, pixelSize = pixelSize)
}

@Composable
fun PixelArtCanvas(gridState: Grid, pixelSize: Dp) {
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
                    val row = gridState.grid[y]
                    val item = row[x]
                    Log.println(Log.INFO, "PixelArtCanvas", "x: $x, y: $y color: ${item.color}")
                    if (item.color == Color.DarkGray) {
                        Log.println(Log.INFO, "PixelArtCanvas", "x: $x, y: $y color: ${item.color}")
                        item.color = Color.White
                    }
                    true
                }
                else -> false
            }
        }

    ){
        val cellsPerRow = gridWidth.dp.toPx()/ pixelSize.toPx()
        val cellsPerColumn = gridHeight.dp.toPx() / pixelSize.toPx()
        val gridColor = Color.DarkGray
        for(y in 0 until cellsPerColumn.toInt()) {
            for(x in 0 until cellsPerRow.toInt()) {
                drawRect(
                    color = gridColor,
                    topLeft = Offset(x+(x * pixelSize.toPx()), y+(y * pixelSize.toPx())),
                    size = Size(pixelSize.toPx(), pixelSize.toPx()),
                )
                Log.println(Log.INFO, "PixelArtCanvas", "x: $x, y: $y")
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







