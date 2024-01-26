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
    val bitmapManager = viewModel.bmpManager
    val scale = bitmapManager.scale.observeAsState(initial = 1f)
    val gridEnabled = viewModel.gridEnabled.observeAsState(initial = true)
    val gridColor = viewModel.gridColor.observeAsState(initial = Color.DarkGray)
    Canvas(
        modifier = Modifier
            .background(Color.Gray)
            .size(bitmap.width.dp / scale.value, bitmap.height.dp / scale.value)
    ) {
        drawImage(
            image = backgroundBitmap.asImageBitmap().apply {
                // Initialize the bitmap with a default color or pattern
                this.width
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
            for (i in 0 until bitmap.height / scale.value.toInt()) {
                drawLine(
                    gridColor.value,
                    Offset(0f, (i * scale.value)),
                    Offset(bitmap.width.toFloat(), (i * scale.value))
                )
            }
            for (j in 0 until bitmap.width / scale.value.toInt()) {
                drawLine(
                    gridColor.value,
                    Offset((j * scale.value), 0f),
                    Offset((j * scale.value), bitmap.height.toFloat())
                )
            }
        }
    }
}