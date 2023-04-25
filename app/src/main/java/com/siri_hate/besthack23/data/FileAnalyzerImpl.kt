package com.siri_hate.besthack23.data

import com.siri_hate.besthack23.domain.FileAnalyzer
import com.siri_hate.besthack23.domain.model.AnalyzedWord

private const val longestWordsAmount = 10

class FileAnalyzerImpl: FileAnalyzer {

    override fun analyzeFileContent(fileContent: String): List<AnalyzedWord> {
        val result = mutableListOf<AnalyzedWord>()

        val words = fileContent.split("[^\\p{L}]+".toRegex())

        // Ищем longestWordCount самых длинных слов в порядке убывания их длины
        val longestWords = words.asSequence().filter { word -> word.isNotEmpty() }
            .map { word -> word.replace("[^a-zA-ZА-Яа-я0-9]".toRegex(), "") }
            .distinct()
            .sortedByDescending { it.length }
            .take(longestWordsAmount)
            .toList()

        for (word in longestWords) {
            val wordLength = word.length
            val count = words.count { it == word }
            result.add(AnalyzedWord(word, wordLength, count))
        }


        return result
    }

}