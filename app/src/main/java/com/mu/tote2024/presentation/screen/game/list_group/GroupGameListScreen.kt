package com.mu.tote2024.presentation.screen.game.list_group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.GROUP_N
import com.mu.tote2024.presentation.utils.withParam

@Composable
fun GroupGameListScreen(
    viewModel: GroupGameListViewModel = hiltViewModel(),
    toGame: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var gameList by remember { mutableStateOf<List<GameModel>>(listOf()) }
    var flagList by remember { mutableStateOf<List<GameFlagsModel>>(listOf()) }
    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading = true
        }

        is UiState.Success -> {
            isLoading = false

            flagList = viewModel.listGameFlags

            gameList = result.data
                .filter { game -> game.group == viewModel.group }
                .sortedBy { it.gameId.toInt() }
        }

        is UiState.Error -> {
            isLoading = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(
            title = GROUP_N.withParam(viewModel.group ?: "")
        )
        Divider(
            modifier = Modifier.padding(bottom = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
        ) {
            items(gameList) { game ->
                GroupGameItemScreen(
                    game = game,
                    flagList = if (flagList.isNotEmpty()) {
                        flagList.first { it.gameId == game.gameId }
                    } else {
                        GameFlagsModel()
                    },
                    onClick = { toGame(game.gameId) }
                )
                Divider(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 4.dp
                    ),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}