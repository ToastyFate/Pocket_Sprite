package toasted.pocket_sprite.ui.composables

import android.graphics.Bitmap
import android.util.Size
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
import toasted.pocket_sprite.util.DEFAULT_CANVAS_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_CANVAS_WIDTH
import toasted.pocket_sprite.viewmodel.MainViewModel


@Composable
fun DrawingPixelArtCanvas(bitmap: Bitmap, viewModel: MainViewModel) {
    val scale = viewModel.scaleFactor.observeAsState(initial = 1f)
    val canvasSize = viewModel.canvasSize.observeAsState(initial = Size(DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT))

    Canvas(modifier = Modifier
        .background(Color.Gray)
        .size(canvasSize.value.width.dp * scale.value, canvasSize.value.height.dp * scale.value)

    ) {
        viewModel.gridMgr.createGrid(canvas = this, scale = scale.value)
        drawImage(
                image = bitmap.asImageBitmap().apply {
                    // Initialize the bitmap with a default color or pattern

                },
                topLeft = Offset(0f, 0f),
            )
    }
}