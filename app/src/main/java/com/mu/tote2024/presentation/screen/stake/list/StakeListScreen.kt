package com.mu.tote2024.presentation.screen.stake.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun StakeListScreen(
    viewModel: StakeListViewModel = hiltViewModel(),
    toStake: (String, String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var stakes by remember { mutableStateOf<List<StakeModel>>(listOf()) }

    val state by viewModel.state.collectAsState()
    val result = state.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                stakes = result.data
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
        Title(title = stringResource(id = R.string.stakes))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
        ) {
            items(stakes) { stake ->
                StakeItemScreen(
                    stake = stake,
                    start = viewModel.listStart.find { it.gameId == stake.gameId }?.start ?: "",
                    flags = if (viewModel.flags.isNotEmpty()) {
                        viewModel.flags.first { it.gameId == stake.gameId }
                    } else {
                        GameFlagsModel()
                    },
                    onClick = { toStake(stake.gameId, stake.gamblerId) }
                )
                HorizontalDivider(
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