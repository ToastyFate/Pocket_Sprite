package toasted.pocket_sprite.data

import android.content.Context
import java.io.File

fun getProjectDirectory(context: Context, dirName: String): File {
    val directory = File(context.filesDir, dirName)
    if (!directory.exists()) {
        directory.mkdir()
    }
    return directory
}

fun writeFileToProjectDir(context: Context, dirName: String, fileName: String, data: String) {
    val file = File(getProjectDirectory(context, dirName), fileName)
    file.writeText(data)
}

fun readFileFromProjectDir(context: Context, dirName: String, fileName: String): String {
    val file = File(getProjectDirectory(context, dirName), fileName)
    return if (file.exists()) file.readText() else "File not found"
}

fun listFilesInProjectDir(context: Context, dirName: String): List<String> {
    return getProjectDirectory(context, dirName).list()?.toList() ?: emptyList()
}

fun deleteFileFromProjectDir(context: Context, dirName: String, fileName: String) {
    val file = File(getProjectDirectory(context, dirName), fileName)
    file.delete()
}

fun renameFileInProjectDir(context: Context, dirName: String, oldName: String, newName: String) {
    val oldFile = File(getProjectDirectory(context, dirName), oldName)
    val newFile = File(getProjectDirectory(context, dirName), newName)
    oldFile.renameTo(newFile)
}

fun copyFileInProjectDir(context: Context, dirName: String, oldName: String, newName: String) {
    val oldFile = File(getProjectDirectory(context, dirName), oldName)
    val newFile = File(getProjectDirectory(context, dirName), newName)
    oldFile.copyTo(newFile)
}

fun moveFileInProjectDir(context: Context, dirName: String, oldName: String, newName: String) {
    val oldFile = File(getProjectDirectory(context, dirName), oldName)
    val newFile = File(getProjectDirectory(context, dirName), newName)
    oldFile.renameTo(newFile)
}
