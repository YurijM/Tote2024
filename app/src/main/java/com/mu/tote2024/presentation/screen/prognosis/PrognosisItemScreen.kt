package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.StakeModel

@Composable
fun PrognosisItemScreen(
    game: GameModel,
    stakes: List<StakeModel>
) {
    Column {
        Text(
            text = "${game.team1} - ${game.team2} ${game.goal1}:${game.goal2}"
        )

        stakes.forEach { stake ->
            Text(
                text = "${stake.gamblerNick} ${stake.goal1}:${stake.goal2}"
            )
        }
    }
}