package com.example.eray_roge.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eray_roge.R
import com.example.eray_roge.ui.screen.GameRuleContent

@Composable
fun GameRuleLayout(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.game_rule_title),
                style = MaterialTheme.typography.titleMedium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
            ) {
                GameRuleContent(gameRule = stringResource(R.string.game_rule_1))
                GameRuleContent(gameRule = stringResource(R.string.game_rule_2))
                GameRuleContent(gameRule = stringResource(R.string.game_rule_3))
            }
        }
    }
}