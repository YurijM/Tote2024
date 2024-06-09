package com.mu.tote2024.presentation.screen.stake.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.ShowFlag
import com.mu.tote2024.presentation.utils.asDateTime

@Composable
fun StakeItemScreen(
    stake: StakeModel,
    start: String,
    flags: GameFlagsModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.game_no, stake.gameId),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = start.asDateTime(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stake.team1,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
                ShowFlag(flags.flag1)
                Text(
                    text = stake.goal1,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = " : ",
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stake.goal2,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ShowFlag(flags.flag2)
                Text(
                    text = stake.team2,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        if (stake.addGoal1.isNotBlank() && stake.addGoal1.isNotBlank()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    lineHeight = 1.em,
                    text = "дополнительное время ${stake.addGoal1} : ${stake.addGoal2}"
                )
                if (stake.penalty.isNotBlank()) {
                    Text(
                        lineHeight = 1.em,
                        text = "по пенальти победит ${stake.penalty}"
                    )
                }
            }

            /*Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "дополнительное время ${stake.addGoal1} : ${stake.addGoal2}"
                )
            }
            if (stake.penalty.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "по пенальти победила ${stake.penalty}"
                    )
                }
            }*/
        }
    }
}
