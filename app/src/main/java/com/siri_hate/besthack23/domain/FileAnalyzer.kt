package com.siri_hate.besthack23.domain

import com.siri_hate.besthack23.domain.model.AnalyzedWord

interface FileAnalyzer {
    fun analyzeFileContent(fileContent: String): List<AnalyzedWord>
}