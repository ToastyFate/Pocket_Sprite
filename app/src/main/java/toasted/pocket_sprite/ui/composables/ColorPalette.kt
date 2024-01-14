package toasted.pocket_sprite.ui.composables

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import toasted.pocket_sprite.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
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