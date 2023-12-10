package toasted.pocket_sprite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme

val gridWidth = 256f
val gridHeight = 256f
val pixelSize = 4f


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
fun PixelArtCanvas() {

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
    ){
        val cellsPerRow = gridWidth.dp.toPx()/ pixelSize.dp.toPx()
        val cellsPerColumn = gridHeight.dp.toPx() / pixelSize.dp.toPx()
        val gridColor = Color.DarkGray
        for(y in 0 until cellsPerColumn.toInt()) {
            for(x in 0 until cellsPerRow.toInt()) {
                drawRect(
                    color = gridColor,
                    topLeft = Offset(x+(x * pixelSize.dp.toPx()), y+(y * pixelSize.dp.toPx())),
                    size = Size(pixelSize.dp.toPx(), pixelSize.dp.toPx()),
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
        PixelArtCanvas()
    }
}







