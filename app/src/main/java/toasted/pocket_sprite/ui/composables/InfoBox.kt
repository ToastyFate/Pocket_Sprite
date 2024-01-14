package toasted.pocket_sprite.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import toasted.pocket_sprite.viewmodel.MainViewModel

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