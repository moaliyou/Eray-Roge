package com.example.eray_roge.data

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

val allWords = setOf(
    "aar", // 3
    "bocool", // 6
    "cowro", // 5
    "dhowrasho", // 9
    "filasho", // 7
    "inkiraad", // 8
    "qoor" // 4
)

private val wordLengthMap: Map<Int, String> = allWords.associateBy({ it.length }, { it })

internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""