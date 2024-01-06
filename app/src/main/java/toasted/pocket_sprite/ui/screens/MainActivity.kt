@file:OptIn(ExperimentalComposeUiApi::class)

package toasted.pocket_sprite.ui.screens

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme
import toasted.pocket_sprite.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val projectDir = getProjectDirectory(this, "Projects")
        setContent {
            Pocket_SpriteTheme {
                //StartScreen(projectDir, viewModel = StartViewModel())
                val mainViewModel = MainViewModel()
                PixelArtApp(mainViewModel)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PixelArtAppPreview(){
    PixelArtApp(MainViewModel())
}

@Composable
fun PixelArtApp(viewModel: MainViewModel) {

    viewModel.createBitmap(LocalDensity.current)


    Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray, MaterialTheme.shapes.extraSmall)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {

            ColorPalette(viewModel)

            InfoBox(viewModel)

            PixelArtCanvas(viewModel)

        }
    }
}



@Composable
fun ColorPalette(viewModel: MainViewModel) {
    val colorList by viewModel.colorList.observeAsState()

    Box(modifier = Modifier
        .wrapContentSize(Alignment.Center)
        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.extraSmall)
    ) {

        Row(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.extraSmall)

        ) {
            for (i in 0 until colorList!!.size) {
                Box(modifier = Modifier
                    .size(24.dp)
                    .padding(1.5.dp)
                    .background(colorList!![i], MaterialTheme.shapes.extraSmall)
                    .pointerInteropFilter { event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                viewModel.selectedColor.value = colorList!![i]
                                //                                Log.d("TouchTest", "Color selected: ${colorList!![i]}")
                                true
                            }

                            else -> false
                        }
                    }
                    .offset((i * 24).dp, 0.dp)
                )
            }
        }
    }
}

@Composable
fun InfoBox(viewModel: MainViewModel) {
    val gridWidth by viewModel.gridWidth.observeAsState(initial = 64.dp)
    val gridHeight by viewModel.gridHeight.observeAsState(initial = 64.dp)

    Box(modifier = Modifier
        .wrapContentSize(Alignment.Center)
        .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall)
    ) {
        Column {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Grid Width: ${gridWidth.value.toInt()}  ", fontSize = 16.sp)
                Text(text = "Grid Height: ${gridHeight.value.toInt()}", fontSize = 16.sp)
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Brush Size: ${viewModel.brushSize.value!!.toInt()}", fontSize = 16.sp)
            }
        }
    }
}



@Composable
fun DrawingPixelArtCanvas(bitmap: Bitmap, viewModel: MainViewModel) {
    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
    Canvas(
        modifier = Modifier
            .background(Color.Gray)
            .size(bitmap.width.dp / cellSize.value, bitmap.height.dp / cellSize.value)
    ) {
        drawImage(
            image = bitmap.asImageBitmap().apply {
                // Initialize the bitmap with a default color or pattern
            },
            topLeft = Offset(0f, 0f),
        )

//


    }
}


@Composable
fun PixelArtCanvas(viewModel: MainViewModel) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
    val selectedColor by viewModel.selectedColor.observeAsState(initial = Color.Black)
    val numberOfPointers by viewModel.numberOfPointers.observeAsState(initial = 0)
    val bitmap = viewModel.bitmap.observeAsState(initial = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)).value

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
                detectTransformGestures { _, pan, zoom, rotate ->
                    scale *= zoom
                    if (numberOfPointers > 1) {
                        offset += pan
                        rotation += rotate
                    }
                }
            }
            .pointerInteropFilter { event ->
                // Handle touch events to update the bitmap
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val x =
                            (((event.x / cellSize.value) / scale).toInt()) // Convert to bitmap coordinates
                        val y =
                            (((event.y / cellSize.value) / scale).toInt())// Convert to bitmap coordinates
                        viewModel.updateCell(
                            x,
                            y,
                            selectedColor.toArgb()
                        )
                        viewModel.updateBitmap(bitmap)
                        viewModel.addPointer()
                        true
                    }

                    MotionEvent.ACTION_POINTER_DOWN -> {
                        viewModel.addPointer()
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x =
                            ((event.x / cellSize.value) / scale).toInt() // Convert to bitmap coordinates
                        val y =
                            ((event.y / cellSize.value) / scale).toInt()// Convert to bitmap coordinates
                        if (x >= 0 && x < bitmap.width && y >= 0 && y < bitmap.height) {
                            viewModel.updateCell(
                                x,
                                y,
                                selectedColor.toArgb()
                            )
                            viewModel.updateBitmap(bitmap)
                        }
                        true
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        viewModel.removePointer()
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        viewModel.removePointer()
                        true
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        viewModel.removePointer()
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
                DrawingPixelArtCanvas(bitmap = bitmap, viewModel = viewModel)
                Grid(bitmap = bitmap, viewModel = viewModel)
            }

        }
    }
}



@Composable
fun Grid(bitmap: Bitmap, viewModel: MainViewModel) {
    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
    val gridColor by viewModel.gridColor.observeAsState(initial = Color.DarkGray)

    Canvas(
        modifier = Modifier
            .size(bitmap.width.dp/cellSize.value, bitmap.height.dp/cellSize.value)
    ) {
        for (i in 0 until bitmap.width) {
            drawLine(
                color = gridColor,
                start = Offset((i * cellSize.value), 0f),
                end = Offset((i * cellSize.value) , bitmap.height.toFloat())
            )
        }
        for (j in 0 until bitmap.height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, ((j * cellSize.value))),
                end = Offset(bitmap.width.toFloat(), (j * cellSize.value))
            )
        }
    }
}







