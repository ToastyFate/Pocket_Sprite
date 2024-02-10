package toasted.pocket_sprite.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FileManager {

    private val _fileContent = MutableLiveData<String>()
    val fileContent: LiveData<String> = _fileContent


    /**
     * Set file content
     *
     * @param content
     */
    fun setFileContent(content: String) {
        _fileContent.value = content
    }

    fun saveFile() {
        val content = fileContent.value ?: return
        TODO()
    }
}