package toasted.pocket_sprite.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import toasted.pocket_sprite.ui.composables.HeaderBox
import toasted.pocket_sprite.ui.composables.PixelArtSurface
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
    viewModel.bmpManager.createDrawingBitmap(LocalDensity.current)


    Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray, MaterialTheme.shapes.extraSmall)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {

            HeaderBox(viewModel)

            PixelArtSurface(viewModel)

        }
    }
}