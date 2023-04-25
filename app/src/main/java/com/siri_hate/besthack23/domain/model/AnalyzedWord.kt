package com.siri_hate.besthack23.domain.model

data class AnalyzedWord(
    val word: String,
    val length: Int,
    val entries: Int,
    val root: String
)