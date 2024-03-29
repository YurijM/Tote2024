package com.mu.tote2024.presentation.screen.admin.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminGameListScreen(
    viewModel: AdminGameListViewModel = hiltViewModel()
) {
    val isLoading = remember { mutableStateOf(false) }
    var gameList by remember { mutableStateOf<List<GameModel>>(listOf()) }
    var flagList by remember { mutableStateOf<List<GameFlagsModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            flagList = viewModel.listGameFlags

            gameList = result.data
                .sortedBy { it.gameId.toInt() }
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(stringResource(id = R.string.admin_game_list))
        OkAndCancel(
            titleOk = stringResource(id = R.string.load),
            enabledOk = true,
            showCancel = false,
            onOK = { viewModel.onEvent(AdminGameListEvent.OnLoad) },
            onCancel = {}
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
                AdminGameItemScreen(
                    game = game,
                    listFlags = if (flagList.isNotEmpty()) {
                        flagList.first { it.gameId == game.gameId }
                    } else {
                        GameFlagsModel()
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }

    if (isLoading.value) {
        AppProgressBar()
    }
}

