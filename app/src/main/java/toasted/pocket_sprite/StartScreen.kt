package toasted.pocket_sprite

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun StartScreen() {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /* TODO: Handle new project action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start New Project", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Handle open existing project action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Existing Project", fontSize = 18.sp)
        }


    }
}

@Preview
@Composable
fun NewProjectDialog() {
    var canvasWidth by remember { mutableStateOf("") }
    var canvasHeight by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = { /* TODO: Handle new project action */ },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = canvasWidth,
            onValueChange = { canvasWidth = it },
            label = { Text("Canvas Width") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = canvasHeight,
            onValueChange = { canvasHeight = it },
            label = { Text("Canvas Height") },
            modifier = Modifier.fillMaxWidth()
        )







    }
}
