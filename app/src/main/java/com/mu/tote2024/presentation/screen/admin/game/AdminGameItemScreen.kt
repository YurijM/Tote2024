package com.mu.tote2024.presentation.screen.admin.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.components.ShowFlag
import com.mu.tote2024.presentation.utils.asDateTime

@Composable
fun AdminGameItemScreen(
    game: GameModel,
    listFlags: GameFlagsModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(.5f),
            text = "Игра №${game.gameId}",
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = game.start.asDateTime(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(.5f),
            text = "группа ${game.group}",
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = game.team1,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
            ShowFlag(listFlags.flag1)
            Text(
                //modifier = Modifier.padding(horizontal = 4.dp),
                text = game.goal1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = ":",
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                //modifier = Modifier.padding(horizontal = 4.dp),
                text = game.goal2,
                color = MaterialTheme.colorScheme.onSurface
            )
            ShowFlag(listFlags.flag2)
            Text(
                text = game.team2,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
