package com.mu.tote2024.presentation.screen.prognosis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun PrognosisListScreen(
    viewModel: PrognosisListViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
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
                prognosis = result.data.filter { it.gameId in viewModel.gameIds }
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(prognosis) { item ->
            PrognosisItemScreen(
                game = viewModel.games.first { it.gameId == item.gameId },
                stakes = viewModel.stakes.filter { it.gameId == item.gameId }.sortedBy { it.gamblerNick }
            )
        }
    }
        /*Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.prognosis),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }*/
}