package com.siri_hate.besthack23.domain

import android.net.Uri
import java.io.File

interface FileManager {
    fun getFileName(uri: Uri?): String
    fun getFileContent(uri: Uri?): String
}