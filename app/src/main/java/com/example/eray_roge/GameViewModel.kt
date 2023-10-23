package com.example.eray_roge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                        isGuessedWordWrong = false,
                        score = updatedScore,
                        isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                        isGuessedWordWrong = false,
                        currentScrambledWord = pickRandomWordAndShuffle(),
                        score = updatedScore,
                        currentWordCount = currentState.currentWordCount.inc(),
                )
            }
        }
    }

    fun checkUserGuess() {
        if (userGuess.isNotBlank() && userGuess.isNotEmpty()) {
            val currentScore = _uiState.value.score
            if (userGuess.equals(currentWord, ignoreCase = true)) {
                // User's guess is correct, increase the score
                // and call updateGameState() to prepare the game for next round
                updateGameState(updatedScore = currentScore.plus(SCORE_INCREASE))
            } else {
                if (currentScore > 0) {
                    updateGameState(
                            updatedScore = currentScore.minus(WRONG_WORD_SCORE_DECREASE)
                    )
                }

                // User's guess is wrong, show an error
                _uiState.update { currentState ->
                    currentState.copy(
                            isGuessedWordWrong = true
                    )
                }
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }
}