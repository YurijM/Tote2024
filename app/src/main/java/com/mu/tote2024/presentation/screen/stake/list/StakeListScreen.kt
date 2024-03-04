package com.mu.tote2024.presentation.screen.stake.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.toLog

@Composable
fun StakeListScreen(
    viewModel: StakeListViewModel = hiltViewModel(),
    toStake: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var stakes by remember { mutableStateOf<List<StakeModel>>(listOf()) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.result) {
        toLog("state: ${state.result}")
        when (state.result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                toLog("stakes: ${viewModel.stakes.size}")
                isLoading = false
                stakes = viewModel.stakes
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(stakes.sortedBy { it.gameId.toInt() }) { stake ->
            Text(text = stake.gameId)
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}