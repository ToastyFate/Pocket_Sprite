package toasted.pocket_sprite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class MyViewModel : ViewModel() {

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

    fun createProject(projectName: String, width: Int, height: Int) {
        viewModelScope.launch {
            val projectDir = File("MyProjects/$projectName")
            projectDir.mkdir()
            val projectFile = File(projectDir, "$projectName.psp")
            projectFile.createNewFile()
            projectFile.writeText("$width,$height")
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
            projectFile.createNewFile()
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