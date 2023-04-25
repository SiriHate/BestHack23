package com.siri_hate.besthack23.presentation.welcome_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.siri_hate.besthack23.domain.FileManager
import com.vpr.vk_test_voice_recorder.data.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val fileManager: FileManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _fileName = MutableStateFlow<String>("")
    val fileName: StateFlow<String> = _fileName.asStateFlow()

    private val _fileContent = MutableStateFlow<String>("")
    val fileContent: StateFlow<String> = _fileContent.asStateFlow()


    fun getFileName(uri: Uri?) = CoroutineScope(ioDispatcher).launch {
        _fileName.value = fileManager.getFileName(uri)
    }
    fun getFileContent(uri: Uri?) = CoroutineScope(ioDispatcher).launch {
        _fileContent.value = fileManager.getFileContent(uri)
    }
}