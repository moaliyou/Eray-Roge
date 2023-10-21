package com.example.eray_roge

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eray_roge.ui.theme.ErayRogeTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
    ) {
        GameStatus(
            score = gameUiState.score,
            modifier = modifier
        )
        GameLayout(
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            currentScrambledWord = gameUiState.currentScrambledWord,
            userGuess = gameViewModel.userGuess,
            isGuessWrong = gameUiState.isGuessedWordWrong,
            wordCount = gameUiState.currentWordCount,
            onSubmit = { gameViewModel.checkUserGuess() },
            onSkip = { gameViewModel.skipWord() },
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
        )
    }
    if (gameUiState.isGameOver) {
        FinalScoreDialog(
            score = gameUiState.score,
            onPlayAgain = { gameViewModel.resetGame() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(
    modifier: Modifier = Modifier,
    onUserGuessChanged: (String) -> Unit,
    userGuess: String,
    isGuessWrong: Boolean,
    onKeyboardDone: () -> Unit,
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    currentScrambledWord: String,
    wordCount: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                WordCounterLayout(
                    wordCount = wordCount,
                    modifier = Modifier
                        .align(Alignment.End)
                )
                Text(
                    text = currentScrambledWord,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = stringResource(R.string.game_description),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                OutlinedTextField(
                    value = userGuess,
                    onValueChange = onUserGuessChanged,
                    label = {
                        if (isGuessWrong) {
                            Text(stringResource(R.string.wrong_guess))
                        } else {
                            Text(stringResource(R.string.enter_your_word))
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = colorScheme.surface,
                        focusedBorderColor = colorScheme.surface,
                        disabledBorderColor = colorScheme.surface,
                    ),
                    isError = isGuessWrong,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onKeyboardDone() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                GameActionButtons(
                    onSubmit = onSubmit,
                    onSkip = onSkip,
                    modifier = Modifier
                        .align(Alignment.End),
                )
            }
        }
    }
}

@Composable
fun GameActionButtons(
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onSkip
        ) {
            Text(text = stringResource(R.string.skip_button))
        }
        Button(
            onClick = onSubmit
        ) {
            Text(text = stringResource(R.string.submit_button))
        }
    }
}

@Composable
fun WordCounterLayout(
    modifier: Modifier = Modifier,
    wordCount: Int
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            colorScheme.primary
        )
    ) {
        Text(
            text = stringResource(R.string.word_counter, wordCount),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(
                    top = 6.dp,
                    bottom = 6.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        )
    }
}

@Composable
fun GameStatus(
    score: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = stringResource(R.string.game_score, score),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        title = { Text(text = stringResource(R.string.congratulations)) },
        text = { Text(text = stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    ErayRogeTheme {
        GameScreen(
            modifier = Modifier
                .padding(16.dp)
        )
    }
}