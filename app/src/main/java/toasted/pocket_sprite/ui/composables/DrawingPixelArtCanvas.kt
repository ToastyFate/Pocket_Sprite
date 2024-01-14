package toasted.pocket_sprite.ui.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.viewmodel.MainViewModel

@Composable
fun DrawingPixelArtCanvas(bitmap: Bitmap, backgroundBitmap: Bitmap, viewModel: MainViewModel) {
    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
    val gridEnabled = viewModel.gridEnabled.observeAsState(initial = true)
    val gridColor = viewModel.gridColor.observeAsState(initial = Color.DarkGray)
    Canvas(
        modifier = Modifier
            .background(Color.Gray)
            .size(bitmap.width.dp / cellSize.value, bitmap.height.dp / cellSize.value)
    ) {
        drawImage(
            image = backgroundBitmap.asImageBitmap().apply {
                // Initialize the bitmap with a default color or pattern

            },
            topLeft = Offset(0f, 0f),
        )
        drawImage(
                image = bitmap.asImageBitmap().apply {
                    // Initialize the bitmap with a default color or pattern
                },
                topLeft = Offset(0f, 0f),
            )

        if (gridEnabled.value) {
            for (i in 0 until bitmap.height / cellSize.value.toInt()) {
                drawLine(
                    gridColor.value,
                    Offset(0f, (i * cellSize.value.toInt()).toFloat()),
                    Offset(bitmap.width.toFloat(), (i * cellSize.value.toInt()).toFloat())
                )
            }
            for (j in 0 until bitmap.width / cellSize.value.toInt()) {
                drawLine(
                    gridColor.value,
                    Offset((j * cellSize.value.toInt().toFloat()), 0f),
                    Offset((j * cellSize.value.toInt()).toFloat(), bitmap.height.toFloat())
                )
            }
        }
    }
}