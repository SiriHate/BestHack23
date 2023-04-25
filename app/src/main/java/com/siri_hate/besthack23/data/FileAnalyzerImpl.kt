package com.siri_hate.besthack23.data

import com.siri_hate.besthack23.domain.FileAnalyzer
import com.siri_hate.besthack23.domain.model.AnalyzedWord
import java.util.Locale

class FileAnalyzerImpl: FileAnalyzer {
    companion object {
        private const val longestWordsAmount = 10
        private val prefixes = arrayOf("в", "во", "взо", "вне", "внутри", "возо", "вы", "до", "еже", "за", "зако", "изо", "испод", "к", "кое", "ку", "меж", "междо", "между", "на", "над", "надо", "наи", "не", "недо", "ни", "низо", "о", "об", "обо", "около", "от", "ото", "па", "пере", "по", "под", "подо", "поза", "после", "пра", "пред", "преди", "предо", "про", "противо", "разо", "с", "со", "сверх", "среди", "су", "тре", "у", "без", "бес", "вз", "вс", "воз", "вос", "из", "ис", "низ", "нис", "обез", "обес", "раз", "рас", "роз", "рос", "через", "черес", "чрез", "чрес", "пре", "при")
        private val suffixes = arrayOf("к", "ик", "ек", "ок", "ёк", "еньк", "оньк", "ечк", "очк", "ушк", "юшк", "ышк", "ник", "чик", "щик", "тель", "ниц", "к", "ист", "ск", "ов", "ев", "н", "и", "е", "ова", "ева", "ть", "ти", "чь", "л", "ся", "сь")
        private val endings = arrayOf("а", "я", "о", "е", "ы", "и", "ой", "ий", "ый", "ая", "яя", "ое", "ее", "ые", "ие", "ов", "ев", "ей", "ого", "его", "ых", "их", "у", "ю", "ам", "ям", "ому", "ему", "ым", "им", "ом", "ами", "ями", "ыми", "ими", "ах", "ях", "ут", "ят", "ешь", "ете", "ишь", "ите", "ет", "ит")
        private val suffixesEndings = arrayOf("ка", "ки", "ке", "ко", "кё", "кеньк", "коньк", "кечк", "кочк", "кушк", "кюшк", "кышк", "ника", "чика", "щика", "тельа", "ница", "ка", "иста", "ска", "ова", "ева", "на", "ия", "ие", "ова", "ева", "ть", "ти", "чь", "ла", "ся", "сь", "ао", "ае", "аы", "аи", "аа", "аяой", "аяий", "аяый", "аяая", "аяяя", "аоо", "аее", "аые", "аие", "аов", "аев", "аей", "аого", "аего", "аой", "аей", "аых", "аих", "ау", "аю", "ама", "яма", "ому", "ему", "оый", "оий", "оый", "оея", "оеп", "оее", "ыео", "иео", "ово", "ево", "оей", "оего", "оего", "оой", "оей", "оых", "оих", "оу", "ою", "ом", "ем", "ами", "ями", "ыми", "ими", "ах", "ях", "ут", "ют", "ат", "ят", "ешь", "ете", "ишь", "ите", "ет", "ит", "к", "ик", "ек", "ок", "ёк", "еньк", "оньк", "ечк", "очк", "ушк", "юшк", "ышк", "ник", "чик", "щик", "тель", "ниц", "к", "ист", "ск", "ов", "ев", "н", "и", "е", "ова", "ева", "ть", "ти", "чь", "л", "ся", "сь" ,"а", "я", "о", "е", "ы", "и", "ой", "ий", "ый", "ая", "яя", "ое", "ее", "ые", "ие", "ов", "ев", "ей", "ого", "его", "ых", "их", "у", "ю", "ам", "ям", "ому", "ему", "ым", "им", "ом", "ами", "ями", "ыми", "ими", "ах", "ях", "ут", "ят", "ешь", "ете", "ишь", "ите", "ет", "ит")
    }

    private fun findRoot(word: String): String {
        var root = word

        for (prefix in prefixes) {
            if (word.startsWith(prefix)) {
                root = root.substring(prefix.length)
                break
            }
        }

        for (ending in endings) {
            if (root.endsWith(ending)) {
                root = root.substring(0, root.length - ending.length)
                break
            }
        }

        var suffixFound: Boolean
        do {
            suffixFound = false
            for (suffix in suffixes) {
                if (root.endsWith(suffix)) {
                    root = root.substring(0, root.length - suffix.length)
                    suffixFound = true
                    break
                }
            }
        } while (suffixFound)

        return root
    }





    override fun analyzeFileContent(fileContent: String): ArrayList<AnalyzedWord> {

        val words = fileContent.lowercase(Locale.ROOT).split("[^\\p{L}]+".toRegex())
        val topTen = ArrayList<AnalyzedWord>(10)

        for (word in words) {
            val root = findRoot(word)
            val existingWord = topTen.find { it.root == root }

            if (existingWord != null) {
                val index = topTen.indexOf(existingWord)
                topTen[index] = existingWord.copy(entries = existingWord.entries + 1)
            } else {
                val analyzedWord = AnalyzedWord(word, word.length, 1, root)
                var inserted = false

                for (i in 0 until topTen.size) {
                    if (analyzedWord.length > topTen[i].length) {
                        topTen.add(i, analyzedWord)
                        inserted = true
                        break
                    }
                }

                if (!inserted && topTen.size < 10) {
                    topTen.add(analyzedWord)
                }

                if (topTen.size > 10) {
                    topTen.removeAt(topTen.size - 1)
                }
            }
        }

        return topTen
    }

}