package toasted.pocket_sprite.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import toasted.pocket_sprite.viewmodel.MainViewModel


@Preview
@Composable
fun SettingsMenuPreview() {
    SettingsMenu(MainViewModel())
}

@Composable
fun SettingsMenu(viewModel: MainViewModel) {
    val gridEnabled = viewModel.gridEnabled.observeAsState(initial = true)
    Box(modifier = Modifier
        .size(300.dp, 500.dp)
        .background(color = MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraSmall)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(text = "Canvas Settings", textAlign = TextAlign.Center, color =
                MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray, MaterialTheme.shapes.extraSmall)
            )
            Row {
                Text(text = "Grid Enabled",color = MaterialTheme.colorScheme.primary, style =
                    MaterialTheme.typography.titleLarge, modifier = Modifier
                    .padding(top = 9.dp)
                )
                Switch(checked = gridEnabled.value,  onCheckedChange = { viewModel.toggleGridEnabled() }
                    , modifier = Modifier
                    .padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Grid Color", textAlign = TextAlign.Center ,color = MaterialTheme.colorScheme.primary, style =
                    MaterialTheme.typography.titleLarge, modifier = Modifier
                    .padding(top = 9.dp)
                )
                ColorPickerButton(viewModel = viewModel, modifier = Modifier
                    .padding(start = 8.dp, top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun ColorPickerButton(viewModel: MainViewModel, modifier: Modifier) {
    val gridColor by viewModel.gridColor.observeAsState(initial = Color.Black)
    val colorPickerEnabled by viewModel.colorPickerEnabled.observeAsState(initial = false)
    val colorList by viewModel.colorList.observeAsState()
    val colors = colorList ?: return

    Button(onClick = { viewModel.toggleColorPicker() },colors = ButtonDefaults.buttonColors(gridColor), shape = MaterialTheme.shapes.extraSmall, border = BorderStroke(2.dp, Color.Black), modifier = modifier
        .size(24.dp)
    ){
        DropdownMenu(expanded = colorPickerEnabled, onDismissRequest = { viewModel.toggleColorPicker() },
            modifier = Modifier
            .size(6 * 24.dp, (colors.size/6+1) * 36.dp)
        ) {
            ColorPicker(viewModel = viewModel, colorPickerType = "Grid")
        }
    }
}
