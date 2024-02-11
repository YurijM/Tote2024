package com.mu.tote2024.presentation.screen.game.list_group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.utils.asTime

@Composable
fun GroupGameItemScreen(
    viewModel: GroupGameListViewModel = hiltViewModel(),
    game: GameModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
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
                modifier = Modifier.padding(end = 4.dp),
                text = game.team1,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
            BasicTextField(
                modifier = Modifier
                    .background(Color.LightGray)
                    .width(28.dp)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                value = game.goal1,
                onValueChange = { newValue -> viewModel.onEvent(GroupGameListEvent.OnGoalChange(1, newValue)) },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
        }
        Text(
            text = " : ",
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(1f)
        ) {
            BasicTextField(
                modifier = Modifier
                    .background(Color.LightGray)
                    .width(28.dp)
                    .padding(horizontal = 4.dp, vertical = 0.dp),
                value = game.goal2,
                onValueChange = { newValue -> viewModel.onEvent(GroupGameListEvent.OnGoalChange(2, newValue)) },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = game.team2,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
