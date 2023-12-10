package toasted.pocket_sprite

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme

val gridWidth = 256
val gridHeight = 256
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


@Composable
fun PixelArtCanvas() {

    Canvas(modifier = Modifier.fillMaxSize() ){
        val cellsPerRow = gridWidth / pixelSize.toPx()
        val cellsPerColumn = gridHeight / pixelSize.toPx()
        val gridColor = Color.Black
        for(y in 0 until cellsPerColumn.toInt()) {
            for(x in 0 until cellsPerRow.toInt()) {
                drawRect(
                    color = gridColor,
                    topLeft = Offset((x * pixelSize.toPx()), (y * pixelSize.toPx())),
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
        PixelArtCanvas()
    }
}







