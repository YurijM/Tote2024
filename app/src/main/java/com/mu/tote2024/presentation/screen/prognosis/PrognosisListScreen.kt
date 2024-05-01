package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun PrognosisListScreen(
    viewModel: PrognosisListViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var games by remember { mutableStateOf<List<GameModel>>(listOf()) }
    var stakes by remember { mutableStateOf<List<StakeModel>>(listOf()) }
    var prognosis by remember { mutableStateOf<List<PrognosisModel>>(listOf()) }

    val state by viewModel.state.collectAsState()
    val result = state.result
    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                games = result.data
                    .filter { it.start.toDouble() < System.currentTimeMillis() }
                    .sortedByDescending { it.gameId }
                stakes = viewModel.stakes
                prognosis = viewModel.prognosis
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Title(title = stringResource(id = R.string.prognosis))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(games) { game ->
                prognosis.find { it.gameId == game.gameId }?.let { item ->
                    PrognosisItemScreen(
                        game = game,
                        prognosis = item,
                        stakes = stakes.filter { stake -> stake.gameId == game.gameId }.sortedBy { it.gamblerNick }
                    )
                }
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}