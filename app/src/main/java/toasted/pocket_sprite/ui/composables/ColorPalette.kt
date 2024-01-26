package toasted.pocket_sprite.ui.composables

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import toasted.pocket_sprite.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPalette(viewModel: MainViewModel) {
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
                Log.d("TouchTest", "row: ${j} ${numRows}")
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
                        Box(modifier = Modifier
                            .size(24.dp)
                            .padding(1.5.dp)
                            .background(colors[i + (j * 6)], MaterialTheme.shapes.extraSmall)
                            .pointerInteropFilter { event ->
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        viewModel.bmpManager.setSelectedColor(colors[i + (j * 6)])
                                        true
                                    }

                                    else -> false
                                }
                            }
                            .offset((i * 27).dp, 0.dp)
                        )
                    }
                }
            }
        }
    }
}