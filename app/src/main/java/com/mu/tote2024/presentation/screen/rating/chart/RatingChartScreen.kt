package com.mu.tote2024.presentation.screen.rating.chart

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState
import java.text.DecimalFormat

@Composable
fun RatingChartScreen(
    viewModel: RatingChartViewModel = hiltViewModel()
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
                    .filter { it.gamblerId == viewModel.gamblerId && it.gameId.toInt() in viewModel.gameIds }
                    .sortedBy { it.gameId.toInt() }
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

        LazyColumn {
            items(stakes) { stake ->
                Text(text = "${stake.gameId}, ${stake.place}")
            }
        }

    if (isLoading) {
        AppProgressBar()
    }
}