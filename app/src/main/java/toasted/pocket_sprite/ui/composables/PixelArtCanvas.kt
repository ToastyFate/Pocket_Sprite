package toasted.pocket_sprite.ui.composables

import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
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
import toasted.pocket_sprite.util.DEFAULT_BITMAP_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_BITMAP_WIDTH
import toasted.pocket_sprite.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PixelArtCanvas(viewModel: MainViewModel) {
    val bitmapManager = viewModel.bmpManager
    val touchHandler = viewModel.touchHandler
    val fingerDrawEnabled by viewModel.fingerDrawEnabled.observeAsState(initial = true)
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val selectedColor by bitmapManager.selectedColor.observeAsState(initial = Color.Black)
    val numberOfPointers by viewModel.numberOfPointers.observeAsState(initial = 0)
    val bitmap = bitmapManager.bitmap.observeAsState(initial = Bitmap.createBitmap(
        DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888)).value
    val backgroundBitmap = bitmapManager.backgroundBitmap.observeAsState(initial =
        Bitmap.createBitmap(DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888)).value
    val scale = bitmapManager.scale.observeAsState(initial = 1f).value

    Log.d("TouchTest", "numberOfPointers: $numberOfPointers")
    Box(contentAlignment = Alignment.Center,
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
                        bitmapManager.updateScale(zoom)
                        rotation += rotate
                    }
                }

                detectDragGestures { change, _ ->
                    if (change.position.x >= 0 && change.position.x < bitmap.width &&
                        change.position.y >= 0 && change.position.y < bitmap.height
                    ) {

                        touchHandler.executeTouch(viewModel, change, this)
                    }

                }


            }
            .pointerInteropFilter { event ->
                // Handle touch events to update the bitmap
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        if (fingerDrawEnabled && viewModel.getPointerType(event) == "Finger") {
                            bitmapManager.setPixel(
                                event.x.toInt(),
                                event.y.toInt(),
                                selectedColor
                            )
                            bitmapManager.updateBitmap(bitmap)
                        }
                        true
                    }


                    MotionEvent.ACTION_POINTER_DOWN -> {
                        Log.d("TouchTest", "pointer down")
                        true
                    }

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
                        (bitmap.width.dp / scale),
                        (bitmap.height.dp / scale)
                    )
                    .background(Color.Gray)
            ) {
                DrawingPixelArtCanvas(bitmap = bitmap, backgroundBitmap = backgroundBitmap, viewModel = viewModel)
            }

        }
    }
}