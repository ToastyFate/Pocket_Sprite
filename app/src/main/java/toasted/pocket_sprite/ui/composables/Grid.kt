//package toasted.pocket_sprite.ui.composables
//
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.size
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.unit.dp
//import toasted.pocket_sprite.viewmodel.MainViewModel
//
//@Composable
//fun Grid(bitmap: Bitmap, canvas: Canvas, viewModel: MainViewModel) {
//    val cellSize = viewModel.cellSize.observeAsState(initial = 16f)
//    val gridColor by viewModel.gridColor.observeAsState(initial = Color.DarkGray)
//
////    Canvas(
////        modifier = Modifier
////            .size(bitmap.width.dp/cellSize.value, bitmap.height.dp/cellSize.value)
////    ){
//
//    val paint = android.graphics.Paint().apply { color = gridColor.toArgb() }
//        for (i in 0 until bitmap.height) {
//            canvas.drawLine(
//                (0f, (i * cellSize.value)),
//                Offset((i * cellSize.value), 0f),
//                Offset(bitmap.width.toFloat(), (i * cellSize.value)),
//                Offset((i * cellSize.value) , bitmap.height.toFloat()),
//                paint
//            )
//        }
//        for (j in 0 until bitmap.width) {
//            drawLine(
//                color = gridColor,
//                start = Offset(0f, (j * cellSize.value)),
//                end = Offset(bitmap.width.toFloat(), (j * cellSize.value))
//            )
//
//        }
//    }
////}