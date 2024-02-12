package com.mu.tote2024.presentation.screen.game.list_group

import androidx.compose.foundation.clickable
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
import com.mu.tote2024.presentation.utils.asTime

@Composable
fun GroupGameItemScreen(
    game: GameModel,
    flagList: GameFlagsModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Игра №${game.gameId}",
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = game.start.asTime(),
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
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.team1,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
            ShowFlag(flagList.flag1)
            Text(
                text = game.goal1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = " : ",
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = game.goal2,
                color = MaterialTheme.colorScheme.onSurface
            )
            ShowFlag(flagList.flag2)
            Text(
                text = game.team2,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
