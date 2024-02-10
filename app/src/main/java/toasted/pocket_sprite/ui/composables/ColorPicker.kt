package toasted.pocket_sprite.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import toasted.pocket_sprite.viewmodel.MainViewModel


@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(MainViewModel(), "Grid")
}

@Composable
fun ColorPicker(viewModel: MainViewModel, colorPickerType: String) {
    val colorList by viewModel.colorList.observeAsState()
    val colors = colorList ?: return
    val numRows = colors.size / 6 + 1

    Box(modifier = Modifier
        .wrapContentSize(Alignment.Center)
        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.extraSmall)
    ) {

        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
            .size(6 * 27.dp, numRows * 27.dp)
        ) {
            for (j in 0 until numRows) {
                Row(horizontalArrangement = Arrangement.Absolute.Left, modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.shapes.extraSmall
                    )
                    .size(24 * 6.dp, 27.dp)

                ) {
                    val colorsLeft = colors.size - (j * 6)
                    for (i in 0 until colorsLeft.coerceAtMost(6)) {
                        Button(onClick = { viewModel.chooseColor(colors[i + (j * 6)], colorPickerType) },
                            colors = ButtonDefaults.buttonColors(colors[i + (j * 6)]), shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier
                            .size(24.dp)
                            .padding(1.5.dp)

                        ){

                        }
                    }
                }
            }
        }
    }
}