package toasted.pocket_sprite.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class StartViewModel : ViewModel() {

    private val _fileContent = MutableLiveData<String>()
    val fileContent: LiveData<String> = _fileContent
    private val _showMenu = MutableLiveData(true)
    val showMenu: LiveData<Boolean> = _showMenu
    private val _showNewProjectDialog = MutableLiveData(false)
    private val _showOpenProjectDialog = MutableLiveData(false)
    val showNewProjectDialog: LiveData<Boolean> = _showNewProjectDialog
    val showOpenProjectDialog: LiveData<Boolean> = _showOpenProjectDialog

    fun setShowMenu(show: Boolean) {
        _showMenu.value = show
    }

    fun setShowNewProjectDialog(show: Boolean) {
        _showNewProjectDialog.value = show
    }

    fun setShowOpenProjectDialog(show: Boolean) {
        _showOpenProjectDialog.value = show
    }

    fun createProject(context: Context, projectName: String, width: Int, height: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val projectsDir = File(context.filesDir, "MyProjects")
                if (!projectsDir.exists()) {
                    projectsDir.mkdir()
                    Log.d("StartViewModel", "Created MyProjects directory")
                }

                val projectDir = File(projectsDir, projectName)
                if (!projectDir.exists()) {
                    projectDir.mkdir()
                    Log.d("StartViewModel", "Created $projectName directory")
                }

                val projectFile = File(projectDir, "$projectName.txt")
                if (!projectFile.exists()) {
                    projectFile.createNewFile()
                    Log.d("StartViewModel", "Created $projectName.txt file")
                }

                val content = "$width,$height"
                projectFile.writeText(content)
            } catch (e: IOException) {
                // Handle the exception, e.g., log it or inform the user
                Log.e("StartViewModel", "Error creating project: ${e.message}")
            }
        }
    }

    fun getProjectList(context: Context): List<String> {
        val projectsDir = File(context.filesDir, "MyProjects")
        return if (projectsDir.exists()) {
            projectsDir.list()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun openProject(projectName: String) {
        viewModelScope.launch {
            val projectDir = File("MyProjects/$projectName")
            val projectFile = File(projectDir, "$projectName.psp")
            val content = projectFile.readText()
            _fileContent.postValue(content)
        }
    }

    fun saveProject(projectName: String, data: String) {
        viewModelScope.launch {
            val projectDir = File("MyProjects/$projectName")
            val projectFile = File(projectDir, "$projectName.psp")
            projectFile.writeText(data)
        }
    }

    fun saveAsProject(projectName: String, data: String) {
        viewModelScope.launch {
            val projectDir = File("MyProjects/$projectName")
            projectDir.mkdir()
            val projectFile = File(projectDir, "$projectName.psp")
            withContext(Dispatchers.IO) {
                projectFile.createNewFile()
            }
            projectFile.writeText(data)
        }
    }

    fun setCanvasSize(projectName: String, width: Int, height: Int) {
        viewModelScope.launch {
            val projectDir = File("MyProjects/$projectName")
            val projectFile = File(projectDir, "$projectName.psp")
            val content = projectFile.readText()
            val newContent = "$width,$height"
            projectFile.writeText(newContent)
        }
    }

    fun getCanvasSize(projectName: String): Pair<Int, Int> {
        val projectDir = File("MyProjects/$projectName")
        val projectFile = File(projectDir, "$projectName.psp")
        val content = projectFile.readText()
        val size = content.split(",")
        return Pair(size[0].toInt(), size[1].toInt())
    }

}