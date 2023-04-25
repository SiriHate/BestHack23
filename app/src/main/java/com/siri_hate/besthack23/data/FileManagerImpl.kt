package com.siri_hate.besthack23.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.siri_hate.besthack23.domain.FileManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManagerImpl @Inject constructor(private val context: Context) : FileManager {

    override fun getFileName(uri: Uri?): String {
        try {
            context.contentResolver.query(uri!!, null, null, null, null)?.let { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val name = cursor.getString(nameIndex)
                cursor.close()
                return name
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "" //todo wrap in status(success/error) class
    }

    override fun getFileContent(uri: Uri?): String {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            inputStream?.close()
            reader.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

}