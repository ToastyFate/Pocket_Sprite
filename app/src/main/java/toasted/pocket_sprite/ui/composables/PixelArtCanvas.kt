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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import toasted.pocket_sprite.util.Coordinate
import toasted.pocket_sprite.util.DEFAULT_BITMAP_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_BITMAP_WIDTH
import toasted.pocket_sprite.util.DEFAULT_CANVAS_HEIGHT
import toasted.pocket_sprite.util.DEFAULT_CANVAS_WIDTH
import toasted.pocket_sprite.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PixelArtSurface(viewModel: MainViewModel) {
    val scale = viewModel.scaleFactor.observeAsState(initial = 1f)
    val bitmapManager = viewModel.bmpManager
    val numberOfPointers by viewModel.numberOfPointers.observeAsState(initial = 0)
    val bitmap = bitmapManager.bitmap.observeAsState(initial = Bitmap.createBitmap(
        DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888)).value
    val touchHandler = viewModel.touchHandler
    val fingerDrawEnabled by viewModel.fingerDrawEnabled.observeAsState(initial = true)
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val selectedColor by viewModel.selectedColor.observeAsState(initial = Color.Black)
    val canvasWidth = viewModel.canvasWidth.observeAsState(initial = DEFAULT_CANVAS_WIDTH.dp.value)
    val canvasHeight = viewModel.canvasHeight.observeAsState(initial = DEFAULT_CANVAS_HEIGHT.dp.value)
    val density = LocalDensity.current


    Log.d("TouchTest", "numberOfPointers: $numberOfPointers")
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .background(Color.DarkGray, MaterialTheme.shapes.extraSmall)
    ) {
        Box(modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                translationX = offset.x
                translationY = offset.y
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                if (numberOfPointers == 2) {
                    detectTransformGestures { _, pan, zoom, rotate ->
                        offset = Offset(offset.x + pan.x, offset.y + pan.y)
                        viewModel.setScaleFactor(zoom)
                        rotation += rotate
                    }
                }


                detectDragGestures { change, _ ->


                    if (viewModel.numberOfPointers.value == 1) {
                        touchHandler.executeTouch(viewModel, change, this, density)
                    }

                }


            }
            .pointerInteropFilter { event ->
                // Handle touch events to update the bitmap
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        if (fingerDrawEnabled && viewModel.getPointerType(event) == "Finger") {
                            viewModel.setNumberOfPointers(event.pointerCount)
                            val touchCoordinate = Coordinate(event.x.toInt(), event.y.toInt())
                            viewModel.bmpManager.updateCell(
                                touchCoordinate,
                                selectedColor, viewModel.gridMgr.gridCellSize.value ?: 1
                            )
                            viewModel.bmpManager.updateBitmap(bitmap)
                        }
                        true
                    }


                    MotionEvent.ACTION_POINTER_DOWN -> {
                        viewModel.setNumberOfPointers(event.pointerCount)
                        Log.d("TouchTest", "pointer down")
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        viewModel.setNumberOfPointers(event.pointerCount)
                        true
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        viewModel.setNumberOfPointers(event.pointerCount)
                        true
                    }


                    else -> false
                }
            }
        ) {

            DrawingPixelArtCanvas(bitmap = bitmap, viewModel = viewModel)

        }
    }
}