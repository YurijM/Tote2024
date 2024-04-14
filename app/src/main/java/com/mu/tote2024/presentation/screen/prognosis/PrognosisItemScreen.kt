package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.ui.ColorDefeat
import com.mu.tote2024.presentation.ui.ColorDraw
import com.mu.tote2024.presentation.ui.ColorFine
import com.mu.tote2024.presentation.ui.ColorWin

@Composable
fun PrognosisItemScreen(
    game: GameModel,
    prognosis: PrognosisModel,
    stakes: List<StakeModel>
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "${game.team1} - ${game.team2} ${game.goal1}:${game.goal2}"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            text = "коэффициенты"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                color = ColorWin,
                style = MaterialTheme.typography.labelSmall,
                text = "за победу ${String.format("%.2f", prognosis.coefficientForWin)}"
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = ", "
            )
            Text(
                color = ColorDraw,
                style = MaterialTheme.typography.labelSmall,
                text = "за ничью ${String.format("%.2f", prognosis.coefficientForDraw)}"
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = ", "
            )
            Text(
                color = ColorDefeat,
                style = MaterialTheme.typography.labelSmall,
                text = "за поражение ${String.format("%.2f", prognosis.coefficientForDefeat)}"
            )
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = ", "
            )
            Text(
                color = ColorFine,
                style = MaterialTheme.typography.labelSmall,
                text = "штраф ${String.format("%.2f", prognosis.coefficientForFine)}"
            )
        }


        stakes.forEach { stake ->
            Text(
                text = "${stake.gamblerNick} ${stake.goal1}:${stake.goal2}, очков ${String.format("%.2f", stake.points)}"
            )
        }
    }
}