package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.ui.ColorDefeat
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorFine
import com.mu.tote2024.presentation.ui.ColorWin
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.asDateTime

@Composable
fun PrognosisItemScreen(
    game: GameModel,
    prognosis: PrognosisModel,
    stakes: List<StakeModel>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            Text(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                text = game.start.asDateTime()
            )
            Text(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                text = if (game.group in GROUPS.take(GROUPS_COUNT))
                    stringResource(R.string.group_no, game.group)
                else game.group
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            text = "${game.team1} - ${game.team2} ${game.goal1}:${game.goal2}"
        )
        if (game.addGoal1.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.height(20.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${stringResource(R.string.add_time)} ${game.addGoal1}:${game.addGoal2}"
                )
                if (game.penalty.isNotBlank()) {
                    Text(
                        modifier = Modifier.height(20.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        text = ", ${stringResource(R.string.by_penalty)} ${game.penalty}"
                    )
                }
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            text = "${stringResource(R.string.coefficients)}:"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) {
                Text(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    text = when (it) {
                        0 -> stringResource(R.string.on_win, String.format("%.2f", prognosis.coefficientForWin))
                        1 -> stringResource(R.string.on_draw, String.format("%.2f", prognosis.coefficientForDraw))
                        2 -> stringResource(R.string.on_defeat, String.format("%.2f", prognosis.coefficientForDefeat))
                        else -> ""
                    }
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.fine, String.format("%.2f", prognosis.coefficientForFine))
        )

        stakes.forEach { stake ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(.4f),
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stake.gamblerNick
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Text(
                            modifier = Modifier.height(20.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "${stake.goal1}:${stake.goal2}"
                        )
                        if (stake.addGoal1.isNotBlank()) {
                            Text(
                                modifier = Modifier.height(20.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                text = ", ${stringResource(R.string.add_time)} ${stake.addGoal1}:${stake.addGoal2}"
                            )
                        }
                    }
                    if (stake.penalty.isNotBlank()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "${stringResource(R.string.by_penalty)} ${stake.penalty}"
                        )
                    }
                }
                Text(
                    modifier = Modifier.weight(.2f),
                    textAlign = TextAlign.Center,
                    color = when (stake.place) {
                        1 -> ColorWin
                        2 -> ColorDraw
                        3 -> MaterialTheme.colorScheme.onSurface
                        else -> if (stake.points < 0) ColorFine
                            else ColorDefeat
                    },
                    text = String.format("%.2f", stake.points)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}