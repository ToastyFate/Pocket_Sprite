package toasted.pocket_sprite.ui.composables

import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PixelArtCanvas(viewModel: MainViewModel) {
    var fingerDrawEnabled by remember { mutableStateOf(true) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
    val selectedColor by viewModel.selectedColor.observeAsState(initial = Color.Black)
    val numberOfPointers by viewModel.numberOfPointers.observeAsState(initial = 0)
    val bitmap = viewModel.bitmap.observeAsState(initial = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)).value
    val backgroundBitmap = viewModel.backgroundBitmap.observeAsState(initial = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)).value

    Log.d("TouchTest", "numberOfPointers: $numberOfPointers")
    BoxWithConstraints(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .background(Color.DarkGray, MaterialTheme.shapes.extraSmall)
    ) {

        Surface(shape = MaterialTheme.shapes.extraSmall, color = Color.DarkGray, modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                if (numberOfPointers > 1) {
                    detectTransformGestures { _, pan, zoom, rotate ->
                        offset = Offset(offset.x + pan.x, offset.y + pan.y)
                        scale *= zoom
                        rotation += rotate
                    }
                }
                detectDragGestures { change, _ ->
                    val x =
                        ((change.position.x / cellSize.value) / scale).toInt() // Convert to bitmap coordinates
                    val y =
                        ((change.position.y / cellSize.value) / scale).toInt() // Convert to bitmap coordinates
                    if (x >= 0 && x < bitmap.width && y >= 0 && y < bitmap.height) {
                        val prevX = ((change.previousPosition.x / cellSize.value) / scale).toInt()
                        val prevY = ((change.previousPosition.y / cellSize.value) / scale).toInt()

                        val interpolatedPoints = viewModel.interpolatePoints(prevX, prevY, x, y)

                        for (point in interpolatedPoints) {
                            viewModel.updateCell(point.x, point.y, selectedColor)
                        }

                    }

                    change.consume()
                    viewModel.getCurrentBitmap()?.let { viewModel.updateBitmap(it) }

                }



            }
            .pointerInteropFilter { event ->
                // Handle touch events to update the bitmap
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        if (fingerDrawEnabled && viewModel.getPointerType(event) == "Finger") {
                            viewModel.updateCells(event, scale)
                            viewModel.updateBitmap(bitmap)
                        }
                        true
                    }


                    MotionEvent.ACTION_POINTER_DOWN -> {
                        Log.d("TouchTest", "pointer down")
                        true
                    }

//                    MotionEvent.ACTION_MOVE -> {
//                        if (fingerDrawEnabled && viewModel.getPointerType(event) == "Finger") {
//                            val x = ((event.x / cellSize.value) / scale).toInt() // Convert to bitmap coordinates
//                            val y = ((event.y / cellSize.value) / scale).toInt()// Convert to bitmap coordinates
//                            if (x >= 0 && x < bitmap.width && y >= 0 && y < bitmap.height && event.historySize > 0) {
//                                val prevX = ((event.getHistoricalX(0)/ cellSize.value) / scale).toInt()
//                                val prevY = ((event.getHistoricalY(0)/ cellSize.value) / scale).toInt()
//                                val interpolatedPoints = viewModel.interpolatePoints(prevX, prevY, x, y)
//                                for (point in interpolatedPoints) {
//                                    viewModel.updateCell(point.x, point.y, selectedColor)
//                                }
//
//                            }
//
//                        }
//                        viewModel.updateBitmap(bitmap)
//                        true
//                    }

                    MotionEvent.ACTION_UP -> {
                        viewModel.setNumberOfPointers(0)
                        true
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        viewModel.setNumberOfPointers(1)
                        true
                    }


                    else -> false
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(
                        (bitmap.width.dp / cellSize.value),
                        (bitmap.height.dp / cellSize.value)
                    )
                    .background(Color.Gray)
            ) {
                DrawingPixelArtCanvas(bitmap = bitmap, backgroundBitmap = backgroundBitmap, viewModel = viewModel)
            }

        }
    }
}