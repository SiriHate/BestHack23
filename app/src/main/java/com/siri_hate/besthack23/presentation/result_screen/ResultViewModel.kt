package com.siri_hate.besthack23.presentation.result_screen

import androidx.lifecycle.ViewModel
import com.siri_hate.besthack23.domain.FileAnalyzer
import com.siri_hate.besthack23.domain.model.AnalyzedWord
import com.vpr.vk_test_voice_recorder.data.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val fileAnalyzer: FileAnalyzer,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _analyzedWords = MutableStateFlow<List<AnalyzedWord>>(listOf())
    val analyzedWords: StateFlow<List<AnalyzedWord>> = _analyzedWords.asStateFlow()

    private val _computationTime = MutableStateFlow<Long>(0)
    val computationTime: StateFlow<Long> = _computationTime.asStateFlow()

    fun analyzeFileContent(fileContent: String) = CoroutineScope(defaultDispatcher).launch {
        val startTime = System.currentTimeMillis()
        _analyzedWords.value = fileAnalyzer.analyzeFileContent(fileContent)
        val endTime = System.currentTimeMillis()
        _computationTime.value = endTime - startTime
    }

}