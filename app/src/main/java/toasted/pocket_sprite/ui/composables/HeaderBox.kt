package toasted.pocket_sprite.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import toasted.pocket_sprite.viewmodel.MainViewModel


@Preview
@Composable
fun HeaderBoxPreview() {
    HeaderBox(MainViewModel())
}

@Composable
fun HeaderBox(viewModel: MainViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
    ) {

        FilledTonalIconToggleButton( checked = false,
            onCheckedChange = { viewModel.DisplaySettingsMenu() }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
            )
        }
    }
}
