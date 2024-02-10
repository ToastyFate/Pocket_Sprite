package toasted.pocket_sprite.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import toasted.pocket_sprite.ui.theme.Pocket_SpriteTheme
import toasted.pocket_sprite.viewmodel.StartViewModel
import java.io.File

@Composable
fun StartScreen(projectDir: File, viewModel: StartViewModel) {
    val showNewProjectDialog by viewModel.showNewProjectDialog.observeAsState(initial = false)
    val showOpenProjectDialog by viewModel.showOpenProjectDialog.observeAsState(initial = false)
    var drawCanvas by remember { mutableStateOf(false) }
    var projectFile by remember { mutableStateOf(File("")) }
    val projectList = viewModel.getProjectList(context = LocalContext.current)
    val showMenu by viewModel.showMenu.observeAsState(initial = true)
    val context = LocalContext.current



    if (showNewProjectDialog) {
        NewProjectDialog(
            onProjectCreate = { projectName, width, height ->
                viewModel.createProject(
                    context = context,
                    projectName = projectName,
                    width = width,
                    height = height
                )

                viewModel.setShowNewProjectDialog(false)
            },
            onDismiss = { viewModel.setShowNewProjectDialog(false) }
        )
    }

    if (showMenu) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { viewModel.setShowNewProjectDialog(true) ; viewModel.setShowMenu(false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start New Project", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.setShowOpenProjectDialog(true) ; viewModel.setShowMenu(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Existing Project", fontSize = 18.sp)

                DropdownMenu(expanded = showOpenProjectDialog, onDismissRequest = {
                    viewModel.setShowOpenProjectDialog(false)}, modifier = Modifier
                    .background(Color.White)
                    .size(350.dp, 200.dp), offset = DpOffset((-10).dp, 10.dp)
                ) {
                    projectList.forEach {
                        DropdownMenuItem(text = {Text(it)}, onClick =
                        { viewModel.setShowOpenProjectDialog(false); viewModel.setShowMenu(false)
                            drawCanvas = true; projectFile = File(projectDir, it) })
                    }
                }
            }
        }
    }
}

@Composable
fun NewProjectDialog(
    onProjectCreate: (String, Int, Int) -> Unit,
    onDismiss: () -> Unit
    ) {
        var canvasWidth by remember { mutableStateOf("") }
        var canvasHeight by remember { mutableStateOf("") }
        var projectName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = projectName,
            onValueChange = { projectName = it },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = canvasHeight,
            onValueChange = { canvasHeight = it },
            label = { Text("Canvas Height") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onProjectCreate(projectName, canvasWidth.toInt(), canvasHeight.toInt())}) {
            Text("Create Project")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Pocket_SpriteTheme {
        StartScreen(File(""), StartViewModel())
    }
}

